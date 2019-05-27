package de.etas.tef.config.controller;

import java.util.Collections;
import java.util.List;

import de.etas.tef.config.action.ActionManager;
import de.etas.tef.config.entity.ConfigBlock;
import de.etas.tef.config.entity.KeyValuePair;
import de.etas.tef.config.helper.CompositeID;
import de.etas.tef.config.helper.Constants;

public class MainController extends AbstractController
{
	private final IController left;
	private final IController right;
	private ConfigBlock copyBlock = null;
	private List<KeyValuePair> copyParameters = null;
	
	private boolean connected = false;
	
	public MainController()
	{
		this.left = new ConfigFileController(this);
		this.left.setParent(this);
		this.right = new ConfigFileController(this);
		this.right.setParent(this);
	}
	
	public IController getController(int compositeID)
	{
		switch (compositeID)
		{
			case CompositeID.COMPOSITE_LEFT:
				return left;
			case CompositeID.COMPOSITE_RIGHT:
				return right;
			case CompositeID.COMPOSITE_ALONE:
			case CompositeID.COMPOSITE_ROOT:
				return this;
		}
		
		return null;
	}
	
	public void setConnected(boolean connected)
	{
		this.connected = connected;
	}
	
	public boolean isConnected()
	{
		return connected;
	}

	public void setCopyBlock(ConfigBlock selectedConfigBlock)
	{
		try
		{
			this.copyBlock = selectedConfigBlock.clone();
		}
		catch (CloneNotSupportedException e)
		{
			ActionManager.INSTANCE.sendAction(Constants.ACTION_LOG_WRITE_ERROR, CompositeID.COMPOSITE_ALONE, "Cannot Clone Block: " + selectedConfigBlock.getBlockName());
			e.printStackTrace();
		}
	}
	
	public ConfigBlock getCopyBlock()
	{
		ConfigBlock result = copyBlock;
		copyBlock = null;
		return result;
	}

	public void copyParameters(List<KeyValuePair> result)
	{
		this.copyParameters = result;
	}
	
	public List<KeyValuePair> getCopyParameters()
	{
		List<KeyValuePair> result = this.copyParameters;
		this.copyParameters = Collections.emptyList();
		return result;
	}
}
