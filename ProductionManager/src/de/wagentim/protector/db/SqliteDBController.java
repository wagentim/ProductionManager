package de.wagentim.protector.db;

import java.sql.SQLException;

public class SqliteDBController implements IDBController
{
	private final SqliteDBHandler handler;
	private final StringBuffer sb;
	
	public SqliteDBController()
	{
		handler = new SqliteDBHandler();
		sb = new StringBuffer();
	}

	public boolean createRecordTable()
	{
		String state = "CREATE TABLE record (record_id INTEGER PRIMARY KEY, record_name STRING NOT NULL);";
		
		handler.openDB();
		
		try
		{
			return handler.executeUpdate(state) == 0 ? true : false;
		}
		catch (ClassNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		
		return false;
	}
	
	public boolean createItemTable()
	{
		String state = "CREATE TABLE item (record_id INTEGER, item_id INTEGER, item_key STRING NOT NULL, item_value STRING NOT NULL);";
		
		handler.openDB();
		
		try
		{
			return handler.executeUpdate(state) == 0 ? true : false;
		}
		catch (ClassNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		
		return false;
	}
	
	public boolean insertNewRecord(int id, String name)
	{
		sb.delete(0, sb.length());
		
		sb.append("INSERT INTO record (").append("record_id, ").append("record_name) ")
			.append("VALUES (").append(id).append(", '").append(name).append("')");
		
//		System.out.println(sb.toString());
		
		handler.openDB();
		
		try
		{
			return handler.executeUpdate(sb.toString()) == 0 ? true : false;
		}
		catch (ClassNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		
		return false;
	}
	
	public boolean insertNewItem(int recordId, int itemId, String key, String value)
	{
		sb.delete(0, sb.length());
		
		sb.append("INSERT INTO item (").append("record_id, ").append("item_id, ").append("item_key, ").append("item_value) ")
			.append("VALUES (").append(recordId).append(", ").append(itemId).append(", '").append(key).append("', '").append(value).append("')");
		
//		System.out.println(sb.toString());
		
		handler.openDB();
		
		try
		{
			return handler.executeUpdate(sb.toString()) == 0 ? true : false;
		}
		catch (ClassNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		
		return false;
	}
}
