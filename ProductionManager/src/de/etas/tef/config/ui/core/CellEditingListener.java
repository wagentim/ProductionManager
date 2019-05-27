package de.etas.tef.config.ui.core;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ControlEditor;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.TypedEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Item;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.TreeItem;

import de.etas.tef.config.action.ActionManager;
import de.etas.tef.config.controller.IController;
import de.etas.tef.config.entity.CellIndex;
import de.etas.tef.config.helper.Constants;
import de.etas.tef.config.listener.IActionListener;

public abstract class CellEditingListener implements MouseListener, IActionListener, KeyListener, SelectionListener
{
	
	private final Composite composite;
	private final IController controller;
	private final int compositeID;

	protected ControlEditor editor = null;
	protected String newValue = Constants.EMPTY_STRING;
	protected String oldValue = Constants.EMPTY_STRING;
	protected boolean isLocked = true;
	
	public CellEditingListener(Composite composite, IController controller, int compositeID)
	{
		this.composite = composite;
		this.controller = controller;
		this.compositeID = compositeID;
		
		editor = getNewEditor();
		
		ActionManager.INSTANCE.addActionListener(this);
	}

	protected Composite getComposite()
	{
		return composite;
	}
	
	protected abstract ControlEditor getNewEditor();
	
	@Override
	public void receivedAction(int type, int compositeID, Object content)
	{

	}
	
	protected void disposeEditor()
	{
		if( null == editor )
		{
			return;
		}
		
		Text oldEditor = (Text)editor.getEditor();
		
		if( null == oldEditor )
		{
			return;
		}

		if (newValue.isEmpty())
		{
			oldEditor.setText(oldValue);
		}

		oldEditor.dispose();

		if (!newValue.equalsIgnoreCase(oldValue))
		{
			updateWithNewValue();
		}
		
		editor.setEditor(null);
	}
	
	protected abstract void updateWithNewValue();
	
	protected abstract Item getSelectedItem(TypedEvent event);

	@Override
	public void mouseDoubleClick(MouseEvent event)
	{
		boolean isEditingLocked = getController().isEditingLocked();
		
		if(isEditingLocked)
		{
			return;
		}
		
		handleModification(event);
	}
	
	protected void handleModification(TypedEvent event)
	{
		disposeEditor();

		final Item item = getSelectedItem(event);
        
        if (item == null)
        {
          return;
        }
        
        Text newEditor = new Text(getComposite(), SWT.NONE);
        
        if( item instanceof TreeItem)
        {
        	newValue = oldValue = item.getText();
        }
        else if (item instanceof TableItem)
        {
        	newValue = oldValue = ((TableItem)item).getText(getCell().getColumn());
        }
        
        newEditor.setText(oldValue);
        
        newEditor.addKeyListener(new KeyListener()
		{
			
			@Override
			public void keyReleased(KeyEvent event)
			{
				if(event.keyCode == SWT.CR)
				{
					disposeEditor();
				}
				else if(event.keyCode == SWT.ESC)
				{
					newValue = Constants.EMPTY_STRING;
					disposeEditor();
				}
			}
			
			@Override
			public void keyPressed(KeyEvent arg0)
			{
				
			}
		});
        
        newEditor.addModifyListener(new ModifyListener()
		{
			
			@Override
			public void modifyText(ModifyEvent event)
			{
				Text text = (Text) editor.getEditor();
				
				if(null == text)
				{
					return;
				}
				
				newValue = text.getText();
				setNewValueByModify();
			}

		});
        
        newEditor.selectAll();
        newEditor.setFocus();
        setNewEditor(newEditor, item);
	}
	
	protected abstract CellIndex getCell();

	protected abstract void setNewValueByModify();
	
	protected abstract void setNewEditor(Text newEditor, Item item);

	@Override
	public void mouseDown(MouseEvent event)
	{
		disposeEditor();
	}

	@Override
	public void mouseUp(MouseEvent event)
	{
	}


	@Override
	public void keyPressed(KeyEvent keyEvent)
	{
		if( keyEvent.keyCode == SWT.SPACE )
		{
			boolean isEditingLocked = getController().isEditingLocked();
			
			if(isEditingLocked)
			{
				return;
			}
			
			handleModification(keyEvent);
		}
		
		if(((keyEvent.stateMask & SWT.CTRL) == SWT.CTRL) && ((keyEvent.keyCode == 'c') || (keyEvent.keyCode == 'C')) )
		{
			keyCopyPressed();
		}
		
		if(((keyEvent.stateMask & SWT.CTRL) == SWT.CTRL) && ((keyEvent.keyCode == 'v') || (keyEvent.keyCode == 'V')) )
		{
			keyPastePressed();
		}
		
	}

	protected abstract void keyPastePressed();

	protected abstract void keyCopyPressed();

	@Override
	public void keyReleased(KeyEvent arg0)
	{
		
	}
	
	protected int getCompositeID()
	{
		return this.compositeID;
	}
	
	protected IController getController()
	{
		return controller;
	}
}
