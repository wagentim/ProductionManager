package de.etas.tef.config.controller;

import java.util.Collections;
import java.util.List;

import de.etas.tef.config.entity.CellIndex;
import de.etas.tef.config.entity.ConfigBlock;
import de.etas.tef.config.entity.ConfigFile;
import de.etas.tef.config.entity.KeyValuePair;
import de.etas.tef.config.helper.Constants;

public abstract class AbstractController implements IController
{
	private IController parent = null;
	
	@Override
	public IController getParent()
	{
		return parent;
	}

	@Override
	public void setParent(IController parent)
	{
		this.parent = parent;
	}
	
	@Override
	public void setInputConfigFile(String filePath)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteParameters(int[] selectedItems, String text)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void saveFile(String targetFilePath)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void parameterChanged(CellIndex cell, String newValue)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public ConfigBlock getSelectedConfigBlock()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] getAllBlocks()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setSelectedBlock(String blockName)
	{
		// TODO Auto-generated method stub
		
	}
	
	public void setShowConfigBlocks(String text)
	{}
	
	@Override
	public void updateBlockName(String oldValue, String newValue)
	{
		// TODO Auto-generated method stub
		
	}
	
	public String[] getShowConfigBlocks() { return Constants.EMPTY_STRING_ARRAY;}
	
	public boolean isEditingLocked()
	{
		return true;
	}

	public void setEditingLocked(boolean isEditingLocked)
	{
	}

	public void parameterAdded(KeyValuePair kvp)
	{}
	
	public ConfigFile getConfigFile() {return null;}
	
	public void addConfigBlock(ConfigBlock cb){}
	
	public void removeBlock(String blockName){}
	
	public void copyBlock(String blockName) {}
	
	public ConfigBlock getCopyBlock() {return null;}
	
	public void copyParameters(int[] selectedItems) {}
	
	public List<KeyValuePair> getCopyParameters(){return Collections.emptyList();}
	
	public void setConfigFile(ConfigFile configFile){}
	
	public int getFocusedElement(){return Constants.FOCUS_NONE;}
	public void setFocusedElement(int element){}
}
