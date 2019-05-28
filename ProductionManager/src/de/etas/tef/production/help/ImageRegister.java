package de.etas.tef.production.help;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

public class ImageRegister
{
	private final Map<String, Image> mapper;
	private final Display display;
	
	public ImageRegister(Display display)
	{
		this.display = display;
		mapper = new HashMap<String, Image>();
		initImages();
	}
	
	private void initImages()
	{
		mapper.put(IImageConstants.IMAGE_PASSWORD_TITLE, createImage("icons/title.png"));
		mapper.put(IImageConstants.IMAGE_LOAD_RECORD_ITEM, createImage("icons/refresh.png"));
		mapper.put(IImageConstants.IMAGE_EXIT, createImage("icons/exit.png"));
		mapper.put(IImageConstants.IMAGE_ABOUT, createImage("icons/about.png"));
		mapper.put(IImageConstants.IMAGE_LOCK, createImage("icons/lock.png"));
		mapper.put(IImageConstants.IMAGE_UNLOCK, createImage("icons/unlock.png"));
		mapper.put(IImageConstants.IMAGE_TIME, createImage("icons/time.png"));
		mapper.put(IImageConstants.IMAGE_LOAD, createImage("icons/load.png"));
		mapper.put(IImageConstants.IMAGE_SEARCH, createImage("icons/search.png"));
		mapper.put(IImageConstants.IMAGE_CANCEL, createImage("icons/cancel.png"));
	}
	
	private Image createImage(String path)
	{
		return new Image(display, ImageRegister.class.getClassLoader().getResourceAsStream(path));
	}

	public Image getImage(String key)
	{
		return mapper.get(key);
	}
}
