package de.etas.tef.config.helper;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Iterator;
import java.util.SortedMap;

import de.etas.tef.config.action.ActionManager;
import de.etas.tef.config.entity.ConfigBlock;
import de.etas.tef.config.entity.ConfigFile;
import de.etas.tef.config.entity.KeyValuePair;

public class InitFileWorker implements IConfigFileWorker
{
	
	private static final String CHARSET_ISO_8859_1 = "ISO-8859-1";
	private static final String CHARSET_UTF_8 = "UTF-8";
	
	public static void testCharset(String fileName)
	{
		SortedMap<String, Charset> charsets = Charset.availableCharsets();
		for (String k : charsets.keySet())
		{
			int line = 0;
			boolean success = true;
			try (BufferedReader b = Files.newBufferedReader(
					Paths.get(fileName), charsets.get(k)))
			{
				while (b.ready())
				{
					b.readLine();
					line++;
				}
			} catch (IOException e)
			{
				success = false;
				System.out.println(k + " failed on line " + line);
			}
			if (success)
				System.out.println("*************************  Successs " + k);
		}
	}
	
	@Override
	public void readFile(String filePath, Object result) throws IOException
	{
		if( !Validator.INSTANCE().validFile(filePath, true) )
		{
			ActionManager.INSTANCE.sendAction(Constants.ACTION_LOG_WRITE_ERROR, CompositeID.COMPOSITE_ALONE,  "Cannot find file: " + filePath);
			
			return;
		}
		
		if( !(result instanceof ConfigFile ) || null == result)
		{
			throw new RuntimeException("Cannot cast the input object to " + ConfigFile.class.getName());
		}
		
		// start parsing the configure *.ini file
		
		ConfigFile cf = (ConfigFile)result;
		
		Path path = Paths.get(filePath);
		
		BufferedReader br = Files.newBufferedReader(path, Charset.forName(CHARSET_ISO_8859_1));
		
		String currentLine = null;
		
		ConfigBlock cb = null;
		
		int status = TAG_FILE_NEW;
		
		int lineCount = 1;
		
		while ( null != (currentLine = br.readLine()) )
		{
			currentLine = currentLine.trim();	// remove the empty character
			
			if( TAG_FILE_NEW == status )
			{
				if( currentLine.startsWith(Constants.SYMBOL_INIT_FILE_COMMENT_DASH) || 
						currentLine.startsWith(Constants.SYMBOL_INIT_FILE_COMMENT_SEMICOLON)
						)
				{
					status = TAG_FILE_COMMENT_START;
				}
				else if ( currentLine.startsWith(Constants.SYMBOL_LEFT_BRACKET) )
				{
					status = TAG_BLOCK_NAME_START;
					cb = new ConfigBlock();
					cf.getConfigBlocks().add(cb);
				}
				else if (currentLine.isEmpty())
				{
					status = TAG_BLOCK_COMMENT_START;
					cb = new ConfigBlock();
					cf.getConfigBlocks().add(cb);
					continue;
				}
				else
				{
					ActionManager.INSTANCE.sendAction(Constants.ACTION_LOG_WRITE_ERROR, CompositeID.COMPOSITE_ALONE,  "ERROR by Parsing File Comments in line: " + lineCount);
					break;
				}
			}
			
			if( TAG_BLOCK_COMMENT_START == status )
			{
				if( currentLine.startsWith(Constants.SYMBOL_INIT_FILE_COMMENT_DASH) || 
						currentLine.startsWith(Constants.SYMBOL_INIT_FILE_COMMENT_SEMICOLON)  ||
							currentLine.isEmpty())
				{
					String s = cb.getComments();
					
					if(!s.isEmpty())
					{
						s += System.lineSeparator();
					}
					
					s += currentLine;
					
					cb.setComments(s);
				}
				else if ( currentLine.startsWith(Constants.SYMBOL_LEFT_BRACKET) )
				{
					status = TAG_BLOCK_NAME_START;
				}
			}
			
			if( TAG_FILE_COMMENT_START == status )
			{
				
				if (currentLine.isEmpty())
				{
					status = TAG_BLOCK_COMMENT_START;
					cb = new ConfigBlock();
					cf.getConfigBlocks().add(cb);
					continue;
				}
				else if ( currentLine.startsWith(Constants.SYMBOL_LEFT_BRACKET) )
				{
					status = TAG_BLOCK_NAME_START;
				}
				else
				{
					String s = cf.getComments();
					
					if(!s.isEmpty())
					{
						s += System.lineSeparator();
					}
					s += currentLine;
					cf.setComments(s);
				}
			}
			
			if( TAG_BLOCK_NAME_START == status )
			{
				if( null == cb )
				{
					cb = new ConfigBlock();
					cf.getConfigBlocks().add(cb);
				}
				
				parserBlockName(currentLine, lineCount, cb);
				status = TAG_BLOCK_NAME_FINISH;
				continue;
			}
			
			if( TAG_BLOCK_NAME_FINISH == status )
			{
				if( currentLine.isEmpty() )
				{
					continue;
				}
				else if( currentLine.startsWith(Constants.SYMBOL_LEFT_BRACKET) )
				{
					cb = new ConfigBlock();
					cf.getConfigBlocks().add(cb);
					parserBlockName(currentLine, lineCount, cb);
					status = TAG_BLOCK_NAME_FINISH;
					continue;
				}
				else
				{
					int split = currentLine.indexOf(Constants.SYMBOL_EQUAL);
					
					if( split < 0 )
					{
						if( currentLine.startsWith(Constants.SYMBOL_INIT_FILE_COMMENT_DASH) || 
								currentLine.startsWith(Constants.SYMBOL_INIT_FILE_COMMENT_SEMICOLON)
								)
						{
							status = TAG_BLOCK_COMMENT_START;
							cb = new ConfigBlock();
							cf.getConfigBlocks().add(cb);
							String s = cb.getComments();
							if(!s.isEmpty())
							{
								s += System.lineSeparator();
							}
							s += currentLine;
							cb.setComments(s);
						}
						else
						{
							ActionManager.INSTANCE.sendAction(Constants.ACTION_LOG_WRITE_ERROR, CompositeID.COMPOSITE_ALONE,  "ERROR by Parsing Parameter in line: " + lineCount);
						}
					}
					else
					{
						KeyValuePair pair = new KeyValuePair();
						
						pair.setKey(currentLine.substring(0, split));
						pair.setValue(currentLine.substring(split + 1, currentLine.length()));
						
						cb.addParameterInLast(pair);
					}
				}
			}
			
			lineCount++;
		}
	}
	
	private void parserBlockName(String currentLine, int lineCount, ConfigBlock configBlock)
	{
		int index = currentLine.indexOf(Constants.SYMBOL_RIGHT_BRACKET);
		
		if( index < 1 )
		{
			ActionManager.INSTANCE.sendAction(Constants.ACTION_LOG_WRITE_ERROR, CompositeID.COMPOSITE_ALONE,  "ERROR by parsing block name in line: " + lineCount);
			configBlock.setBlockName(currentLine.substring(1, currentLine.length()));
		}
		else
		{
			String name = currentLine.substring(1, currentLine.length() - 1);
			
			configBlock.setBlockName(name);
		}
	}
		
	@Override
	public void writeFile(String filePath, Object result) throws IOException
	{
		if( !Validator.INSTANCE().validFile(filePath, true) )
		{
			ActionManager.INSTANCE.sendAction(Constants.ACTION_LOG_WRITE_ERROR, CompositeID.COMPOSITE_ALONE, "Cannot find file: " + filePath);
			
			return;
		}
		
		if( !(result instanceof ConfigFile ) || null == result)
		{
			return;
		}
		
		ConfigFile configFile = (ConfigFile)result;
		
		Path path = Paths.get(filePath);
		
		BufferedWriter bw = Files.newBufferedWriter(path, Charset.forName(CHARSET_UTF_8));
		
		String s = Constants.EMPTY_STRING;

		try
		{
			s = configFile.getComments();
			
			if( null != s && !s.isEmpty())
			{
				bw.write(s);
				bw.write(System.lineSeparator());
				bw.flush();
			}
			Collection<ConfigBlock> values = configFile.getConfigBlocks();
			
			Iterator<ConfigBlock> it = values.iterator();
			
			StringBuffer sb = null;
			
			while (it.hasNext())
			{
				ConfigBlock cb = it.next();
				
				s = cb.getComments();
				
				if( null != s && !s.isEmpty())
				{
					bw.write(s);
					bw.write(System.lineSeparator());
				}
				
				sb = new StringBuffer();
				
				sb.append(Constants.SYMBOL_LEFT_BRACKET);
				sb.append(cb.getBlockName());
				sb.append(Constants.SYMBOL_RIGHT_BRACKET);
				sb.append(System.lineSeparator());
				
				Iterator<KeyValuePair> iter = cb.getAllParameters().iterator();
				
				while (iter.hasNext())
				{
					KeyValuePair pair = iter.next();
					
					sb.append(pair.getKey());
					sb.append(Constants.SYMBOL_EQUAL);
					sb.append(pair.getValue());
					sb.append(System.lineSeparator());
				}
				
				bw.write(sb.toString());
				bw.write(System.lineSeparator());
				bw.flush();
			}
		} 
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}
