package de.etas.tef.config.ui.core;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.DropTargetAdapter;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.FileTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

import de.etas.tef.config.action.ActionManager;
import de.etas.tef.config.controller.MainController;
import de.etas.tef.config.helper.Constants;

public class ConfigComposite extends AbstractComposite
{
	public ConfigComposite(Composite composite, int style, MainController controller, int compositeID)
	{
		super(composite, style, controller, compositeID);

		GridLayout layout = new GridLayout(1, false);
		this.setLayout(layout);
		
		GridData cgd = new GridData(GridData.FILL_HORIZONTAL);
		this.setLayoutData(cgd);
		
		new SelectComposite(this, SWT.NONE, controller, getCompositeID());
		
		SashForm sf = new SashForm(this, SWT.VERTICAL);
		GridData gd = new GridData(GridData.FILL_BOTH);
		sf.setLayoutData(gd);
		
		new TableComposite(sf, SWT.NONE, controller, getCompositeID());
		new CommentComposite(sf, SWT.NONE, controller, getCompositeID());
		
		sf.setWeights(new int[]{2,1});
		
		initDropFunction(this);
	}
	
	protected void initDropFunction(Composite composite)
	{
		DropTarget dt = new DropTarget(composite, DND.DROP_DEFAULT | DND.DROP_MOVE);
		dt.setTransfer(new Transfer[]{ FileTransfer.getInstance() });
		dt.addDropListener(new DropTargetAdapter()
		{
			public void drop(DropTargetEvent event)
			{
				String fileList[] = null;
				
				FileTransfer ft = FileTransfer.getInstance();
				
				if (ft.isSupportedType(event.currentDataType))
				{
					fileList = (String[]) event.data;
				}
				else
				{
					return;
				}
				
				String filePath = fileList[0];
				
				if(!filePath.toLowerCase().trim().endsWith(".ini"))
				{
					return;
				}
				
				ActionManager.INSTANCE.sendAction(Constants.ACTION_DROP_NEW_FILE_SELECTED, getCompositeID(), filePath);
			}
		});
	}
}
