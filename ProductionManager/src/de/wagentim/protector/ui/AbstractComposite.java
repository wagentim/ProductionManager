package de.wagentim.protector.ui;

import org.eclipse.swt.widgets.Composite;

import de.etas.tef.production.help.ActionManager;
import de.wagentim.protector.controller.MainController;
import de.wagentim.protector.listener.IActionListener;

public abstract class AbstractComposite extends Composite implements IActionListener
{
	
	protected final MainController controller;

	public AbstractComposite(Composite parent, int style, MainController controller)
	{
		super(parent, style);
		this.controller = controller;
		ActionManager.INSTANCE.addActionListener(this);
	}
	
	public abstract void receivedAction(int type, Object content);

}
