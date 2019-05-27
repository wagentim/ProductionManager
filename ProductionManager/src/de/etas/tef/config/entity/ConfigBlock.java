package de.etas.tef.config.entity;

import java.util.ArrayList;
import java.util.List;

import de.etas.tef.config.helper.Constants;

public final class ConfigBlock
{
	private String blockName = Constants.EMPTY_STRING;
	private int index = -1;
	private List<KeyValuePair> parameters = null;
	private String comments = Constants.EMPTY_STRING;
	
	public ConfigBlock()
	{
		parameters = new ArrayList<KeyValuePair>();
	}
	
	public KeyValuePair getParameter(int index)
	{
		if( index < 0 )
		{
			return null;
		}
		
		return parameters.get(index);
	}

	public String getBlockName()
	{
		return blockName;
	}

	public void setBlockName(String blockName)
	{
		this.blockName = blockName;
	}
	
	public boolean addParameterInLast(KeyValuePair para)
	{
		if( null == para )
		{
			return false;
		}
		
		parameters.add(para);
		return true;
	}
	
	public List<KeyValuePair> getAllParameters()
	{
		return parameters;
	}

	public int getIndex()
	{
		return index;
	}

	public void setIndex(int index)
	{
		this.index = index;
	}
	
	public ConfigBlock clone() throws CloneNotSupportedException
	{
		ConfigBlock newObject = new ConfigBlock();
		newObject.setBlockName(new String(this.getBlockName()));
		newObject.setIndex(this.getIndex());
		
		List<KeyValuePair> newList = new ArrayList<KeyValuePair>();
		for(int i = 0; i < parameters.size(); i++)
		{
			newList.add(parameters.get(i).clone());
		}
		
		newObject.setParameters(newList);
		
		return newObject;
	}

	public String getComments()
	{
		return comments;
	}

	public void setComments(String comments)
	{
		this.comments = comments;
	}

	public List<KeyValuePair> getParameters()
	{
		return parameters;
	}

	public void setParameters(List<KeyValuePair> parameters)
	{
		this.parameters = parameters;
	}
	
	public String getDisplayName()
	{
		StringBuffer sb = new StringBuffer();
		
		sb.append(getBlockName());
		sb.append(Constants.SYMBOL_SPACE);
		sb.append(Constants.SYMBOL_LEFT_PARENTHESES_);
		sb.append(getParameters().size());
		sb.append(Constants.SYMBOL_RIGHT_PARENTHESES_);
		
		return sb.toString();
	}
}
