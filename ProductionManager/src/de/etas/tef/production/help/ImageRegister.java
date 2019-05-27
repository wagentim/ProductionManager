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
		mapper.put(ImageConstants.IMAGE_TITLE, new Image(display, "icons/title.png"));
	}

	public Image getImage(String key)
	{
		return mapper.get(key);
	}
}
