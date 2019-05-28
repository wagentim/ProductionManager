package de.wagentim.protector.controller;

import javax.management.RuntimeErrorException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Color;

import de.etas.tef.production.help.ActionManager;
import de.wagentim.protector.helper.Constants;
import de.wagentim.protector.listener.IActionListener;

public class InfoBlockWriter implements IActionListener
{
	private final StyledText infoBlock;
	private final Color error;
	private final Color info;
	private final Color warning;
	
	private String txt = Constants.EMPTY_STRING;

	public InfoBlockWriter(final StyledText infoBlock, MainController controller)
	{
		if (null == infoBlock)
		{
			throw new RuntimeErrorException(null, "InfoBlock is null");
		}
		this.infoBlock = infoBlock;
		this.error = infoBlock.getDisplay().getSystemColor(SWT.COLOR_RED);
		this.info = infoBlock.getDisplay().getSystemColor(SWT.COLOR_BLACK);
		this.warning = infoBlock.getDisplay().getSystemColor(SWT.COLOR_BLUE);
		ActionManager.INSTANCE.addActionListener(this);
	}
	
	private void moveToLastLine()
	{
		infoBlock.setTopIndex(infoBlock.getLineCount());
	}

	private void logError(String text)
	{
		txt = "[ERROR] " + text + "\n";

		StyleRange sr = new StyleRange();
		sr.start = infoBlock.getText().length();
		sr.length = txt.length();
		sr.foreground = error;
		sr.fontStyle = SWT.ITALIC;
		infoBlock.append(txt);
		infoBlock.setStyleRange(sr);
		moveToLastLine();
	}

	private void logInfo(String text)
	{
		txt = "[INFO] " + text + "\n";
		
		StyleRange sr = new StyleRange();
		sr.start = infoBlock.getText().length();
		sr.length = txt.length();
		sr.foreground = info;
		sr.fontStyle = SWT.ITALIC;
		infoBlock.append(txt);
		infoBlock.setStyleRange(sr);
		moveToLastLine();
	}
	
	private void logWarning(String text)
	{
		txt = "[WARN] " + text + "\n";
		
		StyleRange sr = new StyleRange();
		sr.start = infoBlock.getText().length();
		sr.length = txt.length();
		sr.foreground = warning;
		sr.fontStyle = SWT.ITALIC;
		infoBlock.append(txt);
		infoBlock.setStyleRange(sr);
		moveToLastLine();
	}


	@Override
	public void receivedAction(int type, Object content)
	{
		if ((type == Constants.ACTION_LOG_WRITE_INFO))
		{
			logInfo(content.toString());
		} 
		else if (type == Constants.ACTION_LOG_WRITE_ERROR)
		{
			logError(content.toString());
		}
		else if (type == Constants.ACTION_LOG_WRITE_WARNING)
		{
			logWarning(content.toString());
		}
	}
}