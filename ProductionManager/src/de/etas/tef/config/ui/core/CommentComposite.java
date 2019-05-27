package de.etas.tef.config.ui.core;

import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Text;

import de.etas.tef.config.action.ActionManager;
import de.etas.tef.config.controller.MainController;
import de.etas.tef.config.entity.KeyValuePair;
import de.etas.tef.config.helper.CompositeID;
import de.etas.tef.config.helper.Constants;

public class CommentComposite extends AbstractComposite
{
	private Text commentBlock;
	private Button btnCommentSave;
	
	public CommentComposite(Composite parent, int style, MainController controller, int compositeID)
	{
		super(parent, style, controller, compositeID);
		
		this.setLayout(new GridLayout(1, false));
		GridData gd = new GridData(GridData.FILL_BOTH);
		gd.minimumHeight = 100;
		this.setLayoutData(gd);
		
		Group group = new Group(this, SWT.SHADOW_ETCHED_IN);
		group.setLayout(new GridLayout(1, false));
		group.setLayoutData(new GridData(GridData.FILL_BOTH));
		group.setText("Comment");
		
		commentBlock = new Text(group, SWT.MULTI | SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
		commentBlock.setLayoutData(new GridData(GridData.FILL_BOTH));
		commentBlock.setEditable(false);
		
		btnCommentSave = new Button(group, SWT.PUSH);
		gd = new GridData(SWT.RIGHT, SWT.TOP, true, false, 1, 1);
		gd.widthHint = Constants.BTN_DEFAULT_WIDTH;
		btnCommentSave.setLayoutData(gd);
		btnCommentSave.setText("Save");
		btnCommentSave.setEnabled(false);
		
		btnCommentSave.addSelectionListener(new SelectionListener()
		{
			
			@Override
			public void widgetSelected(SelectionEvent event)
			{
				saveAction();
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0)
			{
				
			}
		});
		
		commentBlock.addKeyListener(new KeyListener()
		{
			
			@Override
			public void keyReleased(KeyEvent arg0)
			{
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void keyPressed(KeyEvent keyEvent)
			{
				if(((keyEvent.stateMask & SWT.CTRL) == SWT.CTRL) && ((keyEvent.keyCode == 's') || (keyEvent.keyCode == 'S')) )
				{
					saveAction();
				}
				
			}
		});
	}
	
	private void saveAction()
	{
		ActionManager.INSTANCE.sendAction(Constants.ACTION_COMMENT_SAVED, getCompositeID(), commentBlock.getText());
	}
	
	@Override
	public void receivedAction(int type, int compositeID, Object content)
	{
		if( compositeID != getCompositeID() && compositeID != CompositeID.COMPOSITE_ALONE )
		{
			return;
		}
		
		if( Constants.ACTION_LOCK_SELECTION_CHANGED == type)
		{
			boolean locked = (boolean)content;
			commentBlock.setEditable(!locked);
			btnCommentSave.setEnabled(!locked);
		}
		
		if( Constants.ACTION_BLOCK_SELECTED == type)
		{
			commentBlock.setText(getController().getSelectedConfigBlock().getComments());
		}
		
		if( Constants.ACTION_PARAMETER_SELECTED == type)
		{
			List<KeyValuePair> paras = getController().getSelectedConfigBlock().getAllParameters();
			
			commentBlock.setText(paras.get((int) content).getComment());
		}
		
		if( type == Constants.ACTION_NEW_FILE_SELECTED || type == Constants.ACTION_DROP_NEW_FILE_SELECTED)
		{
			commentBlock.setText(Constants.EMPTY_STRING);
		}
	}
}
