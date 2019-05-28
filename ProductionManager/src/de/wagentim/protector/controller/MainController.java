package de.wagentim.protector.controller;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import de.wagentim.protector.db.IDBController;
import de.wagentim.protector.entity.CellIndex;
import de.wagentim.protector.entity.Record;
import de.wagentim.protector.helper.Constants;

public class MainController
{
	private List<Record> itemList = Collections.emptyList();
	private IDBController dbHandler = null;
	private boolean isEditingBlocked = true;
	private String selectedItem = Constants.EMPTY_STRING;
	
	public MainController()
	{
//		dbHandler = new DBHandler();
	}
	
	public void loadData()
	{
	}
	
	public String[] getItemNames()
	{
		String[] result = new String[itemList.size()];
		
		for(int i = 0; i < itemList.size(); i++)
		{
//			result[i] = itemList.get(i).getName();
		}
		
		return result;
	}
	
	public void exit()
	{
	}
	
	public boolean isEditingLocked()
	{
		return isEditingBlocked;
	}
	
	public void setEditingLocked()
	{
		isEditingBlocked = !isEditingBlocked;
	}
	
	public void setShowConfigBlocks(String text)
	{
		
	}
	
	public void setFocusedElement(int element)
	{
		
	}
	
	public void removeBlock(String name)
	{
		
	}
	
	public void copyBlock(String name)
	{
		
	}

	public Record getCopyBlock()
	{
		return null;
	}

	public void addConfigBlock(Record newBlock)
	{
		// TODO Auto-generated method stub
		
	}

	public Record getSelectedItem()
	{
		if(null == selectedItem || selectedItem.isEmpty())
		{
			return null;
		}
		
		return findEntity(selectedItem);
	}
	
	private Record findEntity(String item)
	{
		Iterator<Record> it = itemList.iterator();
		
		while(it.hasNext())
		{
			Record entity = it.next();
			
//			if(entity.getName().equals(item))
//			{
//				return entity;
//			}
		}
		
		return null;
	}

	public int getFocusedElement()
	{
		// TODO Auto-generated method stub
		return 0;
	}

	public void setInputConfigFile(String currFilePath)
	{
		// TODO Auto-generated method stub
		
	}

	public void setSelectedItem(String item)
	{
		selectedItem = item;
	}

	public void deleteParameters(int[] selectedItems, String text)
	{
		// TODO Auto-generated method stub
		
	}

	public void saveFile(String targetFilePath)
	{
		// TODO Auto-generated method stub
		
	}

	public String[] getAllBlocks()
	{
		// TODO Auto-generated method stub
		return null;
	}

	public void copyParameters(int[] selectedItems)
	{
		// TODO Auto-generated method stub
		
	}

	public void parameterChanged(CellIndex cell, String newValue)
	{
		// TODO Auto-generated method stub
		
	}

	public void updateBlockName(String oldValue, String newValue)
	{
		// TODO Auto-generated method stub
		
	}
	
}
