package de.wagentim.protector.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class SqliteDBHandler
{
	private Connection connection;
	private Statement statement;
	private ResultSet resultSet;

	public boolean openDB()
	{
		try
		{
			Class.forName(ISQLConstants.SQLITE_JDBC);
			connection = DriverManager.getConnection(ISQLConstants.SQLITE_CONNECTION);
			return true;
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

	public <T> T executeQuery(String sql, IResultSetExtractor<T> rse) throws SQLException, ClassNotFoundException
	{
		try
		{
			resultSet = getStatement().executeQuery(sql);
			T rs = rse.extractData(resultSet);
			return rs;
		}
		finally
		{
			destroyed();
		}
	}

	public <T> List<T> executeQuery(String sql, RowMapper<T> rm) throws SQLException, ClassNotFoundException
	{
		List<T> rsList = new ArrayList<T>();
		try
		{
			resultSet = getStatement().executeQuery(sql);
			while (resultSet.next())
			{
				rsList.add(rm.mapRow(resultSet, resultSet.getRow()));
			}
		}
		finally
		{
			destroyed();
		}
		return rsList;
	}

	public int executeUpdate(String sql) throws SQLException, ClassNotFoundException
	{
		try
		{
			int c = getStatement().executeUpdate(sql);
			return c;
		}
		finally
		{
			destroyed();
		}

	}

	public void executeUpdate(String... sqls) throws SQLException, ClassNotFoundException
	{
		try
		{
			for (String sql : sqls)
			{
				getStatement().executeUpdate(sql);
			}
		}
		finally
		{
			destroyed();
		}
	}

	public void executeUpdate(List<String> sqls) throws SQLException, ClassNotFoundException
	{
		try
		{
			for (String sql : sqls)
			{
				getStatement().executeUpdate(sql);
			}
		}
		finally
		{
			destroyed();
		}
	}

	private Connection getConnection() throws ClassNotFoundException, SQLException
	{
		return connection;
	}

	private Statement getStatement() throws SQLException, ClassNotFoundException
	{
		if (null == statement)
		{
			statement = getConnection().createStatement();
		}
		return statement;
	}

	public void destroyed()
	{
		try
		{
			if (null != connection)
			{
				connection.close();
				connection = null;
			}

			if (null != statement)
			{
				statement.close();
				statement = null;
			}

			if (null != resultSet)
			{
				resultSet.close();
				resultSet = null;
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
	}
}
