package de.etas.tef.production.system;

import java.util.Map;

public class SystemManager implements ISystemManager
{
	private static final Map<String,String> info = System.getenv();

	@Override
	public String getOperationSystemInfo()
	{

		return null;
	}

}
