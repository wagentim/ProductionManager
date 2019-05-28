package de.wagentim.protector.ui;

import org.eclipse.swt.widgets.Composite;

import de.etas.tef.production.help.IActionListener;
import de.wagentim.common.ImageRegister;
import de.wagentim.protector.common.ActionManager;
import de.wagentim.protector.controller.ProtectorController;

public abstract class AbstractComposite extends Composite implements IActionListener
{
	
	protected final ProtectorController controller;
	protected final ImageRegister imageRegister;

	public AbstractComposite(Composite parent, int style, ProtectorController controller, ImageRegister imageRegister)
	{
		super(parent, style);
		this.controller = controller;
		this.imageRegister = imageRegister;
		ActionManager.INSTANCE.addActionListener(this);
	}
	
	public void receivedAction(int type, Object content){}

}
