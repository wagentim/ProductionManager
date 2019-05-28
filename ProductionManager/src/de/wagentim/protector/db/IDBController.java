package de.wagentim.protector.db;

import java.util.Map;

import de.wagentim.protector.entity.Item;
import de.wagentim.protector.entity.Record;


public interface IDBController
{
	boolean createRecordTable();
	boolean createItemTable();
	boolean insertNewRecord(int id, String name);
	boolean insertNewItem(int recordId, int itemId, String key, String value);
	Map<Integer, Record> getAllRecords();
	Map<Integer, Item> getAllItems();
}
