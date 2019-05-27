package de.wagentim.protector.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

import de.wagentim.protector.controller.MainController;

public class MainComposite extends AbstractComposite
{
	public MainComposite(Composite composite, int style, MainController controller)
	{
		super(composite, style, controller);

		GridLayout layout = new GridLayout(1, false);
		this.setLayout(layout);
		
		GridData cgd = new GridData(GridData.FILL_HORIZONTAL);
		this.setLayoutData(cgd);
		
		SashForm sf = new SashForm(this, SWT.VERTICAL);
		GridData gd = new GridData(GridData.FILL_BOTH);
		sf.setLayoutData(gd);
		
		new TableComposite(sf, SWT.NONE, controller);
	}

	@Override
	public void receivedAction(int type, Object content)
	{
		// TODO Auto-generated method stub
		
	}
}
