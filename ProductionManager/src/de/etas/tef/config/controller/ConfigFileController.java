package de.etas.tef.config.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import de.etas.tef.config.action.ActionManager;
import de.etas.tef.config.entity.CellIndex;
import de.etas.tef.config.entity.ConfigBlock;
import de.etas.tef.config.entity.ConfigFile;
import de.etas.tef.config.entity.KeyValuePair;
import de.etas.tef.config.helper.CompositeID;
import de.etas.tef.config.helper.Constants;
import de.etas.tef.config.helper.IConfigFileWorker;
import de.etas.tef.config.helper.InitFileWorker;
import de.etas.tef.config.helper.Validator;

public class ConfigFileController extends AbstractController
{
	// worker is used to parser the file and also save the content to the file
	private IConfigFileWorker worker = null;
	
	// input file
	private ConfigFile configFile = null;
	private ConfigBlock selectedConfigBlock = null;
	private List<ConfigBlock> showConfigBlocks = null;
	private boolean isEditingLocked = true;
	private MainController mainController;
	
	private int lastFocusedElement = Constants.FOCUS_NONE; 

	public ConfigFileController(MainController mainController)
	{
		worker = new InitFileWorker();
		showConfigBlocks = new ArrayList<ConfigBlock>();
		this.mainController = mainController;
	}
	
	@Override
	public void setInputConfigFile(String filePath)
	{
		if( !Validator.INSTANCE().validFile(filePath, false) )
		{
			ActionManager.INSTANCE.sendAction(Constants.ACTION_LOG_WRITE_ERROR, CompositeID.COMPOSITE_ALONE,  "Input File is Wrong!!");
			return;
		}
		
		setConfigFile(parserConfigFile(filePath));
	}

	private ConfigFile parserConfigFile(String inputFile)
	{
		ConfigFile result = new ConfigFile();
		
		try
		{
			worker.readFile(inputFile, result);
		}
		catch (IOException e)
		{
			ActionManager.INSTANCE.sendAction(Constants.ACTION_LOG_WRITE_ERROR, CompositeID.COMPOSITE_ALONE, "I/O Error by parsing inputFile: " + inputFile);
			e.printStackTrace();
		}
		
		return result;
	}

	@Override
	public String[] getAllBlocks()
	{
		if( null != configFile )
		{
			return getBlockNames(configFile.getConfigBlocks());
		}
		
		return Constants.EMPTY_STRING_ARRAY;
	}

	@Override
	public void setSelectedBlock(String blockName)
	{
		
		if( null == configFile )
		{
			return;
		}

		List<ConfigBlock> blocks = configFile.getConfigBlocks();

		for (int i = 0; i < blocks.size(); i++)
		{
			if (blockName.equals(blocks.get(i).getBlockName()))
			{
				selectedConfigBlock = blocks.get(i);
				break;
			}
		}
	}
	
	public void setConfigFile(ConfigFile cf)
	{
		this.configFile = cf;
	}

	@Override
	public void parameterChanged(CellIndex cell, String newValue)
	{
		ConfigBlock cb = getSelectedConfigBlock();
		
		int row = cell.getRow();
		
		if (row < 0 || null == cb)
		{
			return;
		}

		KeyValuePair pair = cb.getParameter(cell.getRow());

		int column = cell.getColumn();

		switch (column)
		{
		case 0:
			pair.setKey(newValue);
			break;
		case 1:
			pair.setValue(newValue);
			break;
		case 2:
			pair.setOther(newValue);
			break;
		}
	}

	public ConfigBlock getSelectedConfigBlock()
	{
		return selectedConfigBlock;
	}

	@Override
	public void saveFile(String filePath)
	{
		try
		{
			worker.writeFile(filePath, configFile);
		}
		catch (IOException e)
		{
			ActionManager.INSTANCE.sendAction(Constants.ACTION_LOG_WRITE_ERROR, CompositeID.COMPOSITE_ALONE, "Error by writing data to file: " + filePath);
			e.printStackTrace();
		}
	}

	@Override
	public void deleteParameters(int[] selectedItems, String text)
	{
		if( null == text || text.isEmpty() || null == selectedItems|| selectedItems.length < 1)
		{
			ActionManager.INSTANCE.sendAction(Constants.ACTION_LOG_WRITE_ERROR, CompositeID.COMPOSITE_ALONE, "ERROR by deleting the parameters");
			return;
		}
		
		ConfigBlock currBlock = selectedConfigBlock;
		
		List<KeyValuePair> paras = currBlock.getAllParameters();
		
		for(int i = selectedItems.length - 1; i >= 0 ; i--)
		{
			paras.remove(selectedItems[i]);
		}
	}

	@Override
	public void setShowConfigBlocks(String text)
	{
		if( null == text || null == configFile )
		{
			return;
		}

		List<ConfigBlock> blocks = configFile.getConfigBlocks();
		showConfigBlocks.clear();

		for (int i = 0; i < blocks.size(); i++)
		{
			if (blocks.get(i).getBlockName().toLowerCase().contains(text.toLowerCase()))
			{
				showConfigBlocks.add(blocks.get(i));
			}
		}
	}

	@Override
	public String[] getShowConfigBlocks()
	{
		return getBlockNames(showConfigBlocks);
	}

	private String[] getBlockNames(List<ConfigBlock> blocks)
	{
		if (null == blocks || blocks.isEmpty())
		{
			return Constants.EMPTY_STRING_ARRAY;
		}

		String[] result = new String[blocks.size()];

		for (int i = 0; i < blocks.size(); i++)
		{
			result[i] = blocks.get(i).getBlockName();
		}

		Arrays.sort(result);

		return result;
	}

	@Override
	public void updateBlockName(String oldValue, String newValue)
	{
		getSelectedConfigBlock().setBlockName(newValue);
	}

	public boolean isEditingLocked()
	{
		return isEditingLocked;
	}

	public void setEditingLocked(boolean isEditingLocked)
	{
		this.isEditingLocked = isEditingLocked;
	}

	public void parameterAdded(KeyValuePair kvp)
	{
		selectedConfigBlock.addParameterInLast(kvp);
	}
	
	public ConfigFile getConfigFile()
	{
		return configFile;
	}
	
	public void addConfigBlock(ConfigBlock cb)
	{
		getConfigFile().addConfigBlock(cb);
	}
	
	public void removeBlock(String blockName)
	{
		ConfigBlock selected = getSelectedConfigBlock();
		
		if( !blockName.equalsIgnoreCase(selected.getBlockName()) )
		{
			ActionManager.INSTANCE.sendAction(Constants.ACTION_LOG_WRITE_ERROR, CompositeID.COMPOSITE_ALONE, "Required Delete Block: " + blockName + " is not equal to current selected Block: " + selected.getBlockName());
			return;
		}
		
		configFile.getConfigBlocks().remove(selected);
	}
	
	public void copyBlock(String blockName) 
	{
		mainController.setCopyBlock(getSelectedConfigBlock());
	}
	
	public ConfigBlock getCopyBlock()
	{
		return mainController.getCopyBlock();
	}
	
	public void copyParameters(int[] selectedItems) 
	{
		List<KeyValuePair> paras = getSelectedConfigBlock().getAllParameters();
		List<KeyValuePair> result = new ArrayList<KeyValuePair>();
		
		for(int i = 0; i < selectedItems.length; i++)
		{
			try
			{
				result.add(paras.get(selectedItems[i]).clone());
			}
			catch (CloneNotSupportedException e)
			{
				e.printStackTrace();
			}
		}
		
		mainController.copyParameters(result);
		
	}
	
	public List<KeyValuePair> getCopyParameters()
	{
		List<KeyValuePair> values = mainController.getCopyParameters();
		
		if( null == values )
		{
			return Collections.emptyList();
		}
		
		ConfigBlock cb = getSelectedConfigBlock();
		
		Iterator<KeyValuePair> it = values.iterator();
		
		while(it.hasNext())
		{
			cb.addParameterInLast(it.next());
		}
		
		return values;
	}
	
	public int getFocusedElement()
	{
		return lastFocusedElement;
	}
	public void setFocusedElement(int element)
	{
		this.lastFocusedElement = element;
	}
}
