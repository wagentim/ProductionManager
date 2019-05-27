package de.etas.tef.production.shell;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ShellExecutor implements Runnable
{
	private static final String SHELL_POWER_SHELL ="powershell.exe -file ";
	private final Object lock;
	private boolean exit = false;
//	private static final String device = "shells/get_devices.ps1";
//	private static final String sw = "shells/get_install_sw.ps1";
	
	private final Queue<IShellContentHandler> queue;
	
	public ShellExecutor()
	{
		this.queue = new ConcurrentLinkedQueue<IShellContentHandler>();
		this.lock = new Object();
	}
	
	public synchronized void addHandler(IShellContentHandler handler)
	{
		if( null == handler )
		{
			return;
		}
		
		queue.add(handler);
		lock.notify();
	}
	
	private void exec(IShellContentHandler handler)
	{
		String command = handler.getShellCommand() + handler.getShellFile();
		Process shellProcess;
		try
		{
			shellProcess = Runtime.getRuntime().exec(command);
			shellProcess.getOutputStream().close();
			
			String line;
			StringBuffer sb_content = new StringBuffer();
			StringBuffer sb_error = new StringBuffer();

			BufferedReader stdout = new BufferedReader(new InputStreamReader(
					shellProcess.getInputStream(), "utf-8"));
			while ((line = stdout.readLine()) != null)
			{
				sb_content.append(line);
			}
			stdout.close();

			BufferedReader stderr = new BufferedReader(new InputStreamReader(
					shellProcess.getErrorStream(), "utf-8"));
			
			while ((line = stderr.readLine()) != null)
			{
				sb_error.append(line);
			}
			
			stderr.close();
			
			handler.processContent(sb_content);
			handler.processError(sb_error);
			
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	

	@Override
	public void run()
	{
		while(!exit)
		{
			while(queue.isEmpty())
			{
				try
				{
					lock.wait();
				} catch (InterruptedException e)
				{
					e.printStackTrace();
				}		
			}
			IShellContentHandler handler = queue.poll();
			exec(handler);
		}
	}
}
