package de.wagentim.protector.ui;

import org.eclipse.swt.widgets.Composite;

import de.etas.tef.production.help.ActionManager;
import de.etas.tef.production.help.IActionListener;
import de.etas.tef.production.help.ImageRegister;
import de.wagentim.protector.controller.MainController;

public abstract class AbstractComposite extends Composite implements IActionListener
{
	
	protected final MainController controller;
	protected final ImageRegister imageRegister;

	public AbstractComposite(Composite parent, int style, MainController controller, ImageRegister imageRegister)
	{
		super(parent, style);
		this.controller = controller;
		this.imageRegister = imageRegister;
		ActionManager.INSTANCE.addActionListener(this);
	}
	
	public void receivedAction(int type, Object content){}

}
