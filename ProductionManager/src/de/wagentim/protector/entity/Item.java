package de.wagentim.protector.entity;

import java.io.Serializable;

import de.wagentim.protector.helper.Constants;

public class Item implements Serializable
{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8554133090987357957L;
	
	private String key = Constants.EMPTY_STRING;
	private String value = Constants.EMPTY_STRING;
	private final double blockId;
	private final double itemId;
	
	public Item(final double blockId, final double itemId)
	{
		super();
		this.blockId = blockId;
		this.itemId = itemId;
	}
	
	public double getBlockId()
	{
		return blockId;
	}

	public double getItemId()
	{
		return itemId;
	}

	public Item(final double blockId, final double itemId, String key, String value)
	{
		super();
		this.key = key;
		this.value = value;
		this.blockId = blockId;
		this.itemId = itemId;
	}

	public String getKey()
	{
		return key;
	}

	public void setKey(String key)
	{
		this.key = key;
	}

	public String getValue()
	{
		return value;
	}

	public void setValue(String value)
	{
		this.value = value;
	}

	@Override
	public String toString()
	{
		return "KeyValuePair [key=" + key + ", value=" + value + ", blockId=" + blockId + ", itemId=" + itemId + "]";
	}
}
