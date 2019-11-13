package de.etas.tef.prodution.core;

import org.eclipse.swt.widgets.Display;

import de.wagentim.common.ColorPicker;
import de.wagentim.common.ImageRegister;

public final class CoreService
{
	private static CoreService service = null;
	
	private Display display = null;
	private ImageRegister imageRegister = null;
	private ColorPicker colorFactory = null;
	
	public static final int APP_STATUS_OFF = 0x00;
	public static final int APP_STATUS_ON = 0x01;
	
	private int appStatusXMLEditor = APP_STATUS_OFF;
	
	public static CoreService getCoreService()
	{
		if(null == service)
		{
			service = new CoreService();
			service.start();
		}
		
		return service;
	}
	
	public boolean start()
	{
		
		// initial the components
		display = new Display();
		imageRegister = new ImageRegister(display);
		colorFactory = new ColorPicker(display);
		
		return true;
	}
	
	public Display getDisplay()
	{
		return display;
	}
	
	public ImageRegister getImageRegister()
	{
		return imageRegister;
	}
	
	public ColorPicker getColorFactory()
	{
		return colorFactory;
	}

	public int getAppStatusXMLEditor()
	{
		return appStatusXMLEditor;
	}

	public void setAppStatusXMLEditor(int appStatusXMLEditor)
	{
		this.appStatusXMLEditor = appStatusXMLEditor;
	}
	
	
}
