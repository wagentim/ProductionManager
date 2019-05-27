package de.etas.tef.production.shell;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ShellExecutor implements Runnable
{
	private static final String SHELL_POWER_SHELL ="powershell.exe -file ";
	private final Object lock;
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
		
		queue.offer(handler);
		lock.notify();
	}
	
	private void exec(String shellCommand, String shellFile, IShellContentHandler handler)
	{
		String command = shellCommand + shellFile;
		Process shellProcess;
		int count = 0;
		try
		{
			shellProcess = Runtime.getRuntime().exec(command);
			shellProcess.getOutputStream().close();
			
			String line;

			BufferedReader stdout = new BufferedReader(new InputStreamReader(
					shellProcess.getInputStream(), "utf-8"));
			while ((line = stdout.readLine()) != null)
			{
				System.out.println(line);
				count++;
			}
			stdout.close();
			BufferedReader stderr = new BufferedReader(new InputStreamReader(
					shellProcess.getErrorStream(), "utf-8"));
			while ((line = stderr.readLine()) != null)
			{
				System.out.println(line);
			}
			stderr.close();
			System.out.println("Number: " + count);
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args)
	{
//		ShellExecutor se = new ShellExecutor();
//		se.exec(SHELL_POWER_SHELL, sw, null);
	}

	@Override
	public void run()
	{
		while(handlers.size() <= 0)
		{
			try
			{
				lock.wait();
			} catch (InterruptedException e)
			{
				e.printStackTrace();
			}
			
			
		}
	}
}
