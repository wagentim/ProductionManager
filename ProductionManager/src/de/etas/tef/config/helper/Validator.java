package de.etas.tef.config.helper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;

public final class Validator
{
	private static Validator validator = new Validator();
	
	public static Validator INSTANCE()
	{
		return validator;
	}
	
	/**
	 * 
	 * @param filePath
	 * @param createNewFile
	 * @return
	 */
	public boolean validFile(String filePath, boolean createNewFile)
	{
		if( null == filePath || filePath.isEmpty() )
		{
			return false;
		}
		
		Path path = Paths.get(filePath, Constants.EMPTY_STRING_ARRAY);
		LinkOption[] options = {LinkOption.NOFOLLOW_LINKS};
		
		if(Files.notExists(path, options) && createNewFile)
		{
			try
			{
				Files.createFile(path);
			} 
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		
		return true;
	}
	
}
