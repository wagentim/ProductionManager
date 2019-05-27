package de.wagentim.protector.db;

import de.wagentim.protector.entity.IDManager;

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
		c.insertNewItem(IDManager.INSTANCE().getRandomInteger(), IDManager.INSTANCE().getRandomInteger(), "Hello", "World");
	}
}
