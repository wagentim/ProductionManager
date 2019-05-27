package de.wagentim.protector.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ControlEditor;
import org.eclipse.swt.custom.TreeEditor;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.TypedEvent;
import org.eclipse.swt.widgets.Item;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import de.etas.tef.production.help.ActionManager;
import de.wagentim.protector.controller.MainController;
import de.wagentim.protector.entity.CellIndex;
import de.wagentim.protector.entity.Record;
import de.wagentim.protector.helper.Constants;

public class TreeListener extends CellEditingListener
{

	public TreeListener(Tree tree, MainController controller)
	{
		super(tree, controller);
	}
	
	protected ControlEditor getNewEditor()
	{
		TreeEditor editor = new TreeEditor(getTree());
		editor.horizontalAlignment = SWT.LEFT;
	    editor.grabHorizontal = true;
	    editor.minimumWidth = 50;
		return editor;
	}
	
	private Tree getTree()
	{
		return (Tree)getComposite();
	}

	@Override
	protected void updateWithNewValue()
	{
		controller.updateBlockName(oldValue, newValue);
	}

	@Override
	protected Item getSelectedItem(TypedEvent event)
	{
		return getSelectedTreeItem();
	}

	@Override
	protected void setNewEditor(Text newEditor, Item item)
	{
		((TreeEditor)editor).setEditor(newEditor, (TreeItem)item);
	}

	@Override
	protected void setNewValueByModify()
	{
		getSelectedTreeItem().setText(newValue);
	}

	@Override
	public void widgetDefaultSelected(SelectionEvent arg0)
	{
		
	}

	@Override
	public void widgetSelected(SelectionEvent event)
	{
		if(event.getSource() instanceof MenuItem)
		{
			String text = ((MenuItem)event.getSource()).getText();
			
			if( text.contentEquals(Constants.TXT_BTN_ADD) )
			{
				Record cb = new Record(1, "");
//				cb.setName(Constants.TXT_TEMP);
				controller.addConfigBlock(cb);
				ActionManager.INSTANCE.sendAction(Constants.ACTION_ADD_NEW_BLOCK, cb);
			}
			else if( text.contentEquals(Constants.TXT_BTN_DELETE) )
			{
				ActionManager.INSTANCE.sendAction(Constants.ACTION_DELETE_BLOCK, getTree().getSelection()[0].getText());
			}
			else if( text.contentEquals(Constants.TXT_COPY) )
			{
				sendCopyMessage();
			}
			else if( text.contentEquals(Constants.TXT_PASTE) )
			{
				sendPasteMessage();
			}
		}
		else if(event.getSource() instanceof Tree)
		{
			String s = getSelectedTreeItem().getText();
			controller.setFocusedElement(Constants.FOCUS_BLOCK);
			controller.setSelectedItem(s);
			ActionManager.INSTANCE.sendAction(Constants.ACTION_BLOCK_SELECTED, null);
		}
	}
	
	private TreeItem getSelectedTreeItem()
	{
		TreeItem ti = getTree().getSelection()[0];
		return ti;
	}
	
	private void sendCopyMessage()
	{
		ActionManager.INSTANCE.sendAction(Constants.ACTION_COPY_BLOCK, null);
	}
	
	private void sendPasteMessage()
	{
		ActionManager.INSTANCE.sendAction(Constants.ACTION_PASTE_BLOCK, null);
	}

	@Override
	protected CellIndex getCell()
	{
		return null;
	}

	@Override
	protected void keyCopyPressed()
	{
		sendCopyMessage();
	}

	@Override
	protected void keyPastePressed()
	{
		sendPasteMessage();
	}

	@Override
	public void receivedAction(int type, Object content)
	{
		// TODO Auto-generated method stub
		
	}
}
