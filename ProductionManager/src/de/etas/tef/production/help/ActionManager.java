package de.etas.tef.production.help;

import java.util.ArrayList;
import java.util.List;

public class ActionManager
{
	public static ActionManager INSTANCE = new ActionManager();
	private List<IActionListener> listenerList = null;
	
	public ActionManager()
	{
		listenerList = new ArrayList<IActionListener>();
	}
	
	public void addActionListener(IActionListener listener)
	{
		if( !listenerList.contains(listener) )
		{
			listenerList.add(listener);
		}
	}
	
	public void removeActionListener(IActionListener listener)
	{
		if( !listenerList.isEmpty() )
		{
			listenerList.remove(listener);
		}
	}
	
	public void sendAction(final int type, final Object content)
	{
		if( !listenerList.isEmpty() )
		{
			for( int i = 0; i < listenerList.size(); i++ )
			{
				listenerList.get(i).receivedAction(type, content);
			}
		}
	}
}
