package de.wagentim.protector.db;

import java.sql.ResultSet;

public interface IResultSetExtractor<T>
{
	public abstract T extractData(ResultSet rs);
}
