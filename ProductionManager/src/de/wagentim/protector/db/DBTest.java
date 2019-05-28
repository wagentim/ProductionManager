package de.wagentim.protector.db;

import java.util.Iterator;

import de.wagentim.protector.entity.Item;
import de.wagentim.protector.entity.Record;

public class DBTest
{
	public static void main(String[] args)
	{
		SqliteDBController c = new SqliteDBController();
		
//		boolean i = c.createRecordTable();
//		
//		System.out.println(i);
//		
//		i = c.createItemTable();
//		
//		System.out.println(i);
		
//		c.insertNewRecord(IDManager.INSTANCE().getRandomInteger(), "Hello");
//		c.insertNewItem(IDManager.INSTANCE().getRandomInteger(), IDManager.INSTANCE().getRandomInteger(), "Hello", "World");
		
		Iterator<Record> iterator = c.getAllRecords().values().iterator();
		
		while(iterator.hasNext())
		{
			System.out.println(iterator.next().toString());
		}
		
		Iterator<Item> it = c.getAllItems().values().iterator();
		
		while(it.hasNext())
		{
			System.out.println(it.next().toString());
		}
	}
}
