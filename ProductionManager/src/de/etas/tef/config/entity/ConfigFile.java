package de.etas.tef.config.entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.etas.tef.config.helper.Constants;

public final class ConfigFile
{
	private String filePath = Constants.EMPTY_STRING;
	private List<ConfigBlock> configBlocks = Collections.emptyList();
	private String comments = Constants.EMPTY_STRING;
	
	public ConfigFile()
	{
		configBlocks = new ArrayList<ConfigBlock>();
	}
	
	public String getFilePath()
	{
		return filePath;
	}
	public void setFilePath(String filePath)
	{
		this.filePath = filePath;
	}
	public List<ConfigBlock> getConfigBlocks()
	{
		return configBlocks;
	}
	public void setConfigBlocks(List<ConfigBlock> configBlocks)
	{
		this.configBlocks = configBlocks;
	}
	public String getComments()
	{
		return comments;
	}
	public void setComments(String comments)
	{
		this.comments = comments;
	}
	
	public void addConfigBlock(ConfigBlock cb)
	{
		if( null == cb )
		{
			return;
		}
		
		getConfigBlocks().add(cb);
	}
	
	public ConfigFile clone() throws CloneNotSupportedException
	{
		ConfigFile cf = new ConfigFile();
		
		cf.setFilePath(this.getFilePath());
		
		List<ConfigBlock> newList = new ArrayList<ConfigBlock>();
		
		for(int i = 0; i < configBlocks.size(); i++)
		{
			newList.add(configBlocks.get(i).clone());
		}
		
		cf.setComments(this.getComments());
		
		return cf;
	}
	
}
