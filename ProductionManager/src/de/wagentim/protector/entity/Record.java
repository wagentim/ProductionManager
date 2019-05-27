package de.wagentim.protector.entity;

import java.io.Serializable;

import de.wagentim.protector.helper.Constants;


public class Record implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private final double id;
	private String name = Constants.EMPTY_STRING;
	
	public void setName(String name)
	{
		this.name = name;
	}

	public Record(final double id)
	{
		this.id = id;
	}
	
	public Record(final double id, final String name)
	{
		this.id = id;
		this.name = name;
	}

	public double getId()
	{
		return id;
	}

	public String getName()
	{
		return name;
	}

	@Override
	public String toString()
	{
		return "BlockEntity [id=" + id + ", name=" + name + "]";
	}
}
