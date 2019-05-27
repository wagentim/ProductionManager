package de.etas.tef.config.ui.core;

import org.eclipse.swt.widgets.Composite;

import de.etas.tef.config.action.ActionManager;
import de.etas.tef.config.controller.IController;
import de.etas.tef.config.controller.MainController;
import de.etas.tef.config.listener.IActionListener;

public class AbstractComposite extends Composite implements IActionListener
{
	
	private final MainController controller;
	private final int compositeID;

	public AbstractComposite(Composite parent, int style, MainController controller, int compositeID)
	{
		super(parent, style);
		this.controller = controller;
		this.compositeID = compositeID;
		ActionManager.INSTANCE.addActionListener(this);
	}
	
	protected IController getController()
	{
		return controller.getController(getCompositeID());
	}
	
	protected int getCompositeID()
	{
		return this.compositeID;
	}

	public void receivedAction(int type, int compositeID, Object content)
	{
	}

}
