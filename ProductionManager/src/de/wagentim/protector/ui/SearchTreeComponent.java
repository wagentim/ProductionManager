package de.wagentim.protector.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MenuAdapter;
import org.eclipse.swt.events.MenuEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import de.etas.tef.production.help.IConstants;
import de.wagentim.common.IImageConstants;
import de.wagentim.common.ImageRegister;
import de.wagentim.common.ui.SearchComposite;
import de.wagentim.protector.common.ActionManager;
import de.wagentim.protector.controller.ProtectorController;
import de.wagentim.protector.entity.Record;

public class SearchTreeComponent extends AbstractComposite
{

	private Tree itemList;
	private TreeItem root;
	private TableComposite tableComposite;

	private TreeListener tl;
	private Menu rightClickMenu;
	
	public SearchTreeComponent(Composite parent, int style, ProtectorController controller, ImageRegister imageRegister)
	{
		super(parent, style, controller, imageRegister);
		
		GridLayout layout = new GridLayout(1, false);
		layout.marginTop = layout.marginBottom = layout.marginLeft = layout.marginRight = 0; 
		this.setLayout(layout);
		this.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		initComponent(controller);
	}

	protected void initComponent(ProtectorController controller)
	{
		new SearchComposite(this, SWT.BORDER, controller, imageRegister);
		
		itemList = new Tree(this, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
		GridData gd = new GridData(GridData.FILL_BOTH);
		gd.heightHint = IConstants.HEIGHT_HINT;
		itemList.setLayoutData(gd);
		
		root = new TreeItem(itemList, SWT.NONE);
		root.setText(IConstants.TXT_RECORD);
		root.setImage(imageRegister.getImage(IImageConstants.IMAGE_ROOT));
		
		tl = new TreeListener(itemList, controller);
		itemList.addMouseListener(tl);
		itemList.addKeyListener(tl);
		itemList.addSelectionListener(tl);
		createRightMenu(itemList, tl);
		
	}
	
	private void createRightMenu(Control control, SelectionListener listener)
	{
		rightClickMenu = new Menu(control);
		control.setMenu(rightClickMenu);
		
		rightClickMenu.addMenuListener(new MenuAdapter()
	    {
	        public void menuShown(MenuEvent e)
	        {
	        	
	            MenuItem[] items = rightClickMenu.getItems();
	            
	            for (int i = 0; i < items.length; i++)
	            {
	                items[i].dispose();
	            }
	            
	            if(itemList.getSelectionCount() <= 0)
	            {
	            	return;
	            }
	            
	            MenuItem copyItem = new MenuItem(rightClickMenu, SWT.NONE);
	            copyItem.setText(IConstants.TXT_COPY);
	            copyItem.setImage(imageRegister.getImage(IImageConstants.IMAGE_COPY));
	            copyItem.addSelectionListener(listener);
	            
	            MenuItem pasteItem = new MenuItem(rightClickMenu, SWT.NONE);
	            pasteItem.setText(IConstants.TXT_PASTE);
	            pasteItem.setImage(imageRegister.getImage(IImageConstants.IMAGE_PASTE));
	            pasteItem.addSelectionListener(listener);
	            
	            new MenuItem(rightClickMenu, SWT.SEPARATOR);
	            
	            MenuItem newItem = new MenuItem(rightClickMenu, SWT.NONE);
	            newItem.setText(IConstants.TXT_BTN_ADD);
	            newItem.setImage(imageRegister.getImage(IImageConstants.IMAGE_ADD));
	            newItem.addSelectionListener(listener);

	            MenuItem deleteItem = new MenuItem(rightClickMenu, SWT.NONE);
	            deleteItem.setText(IConstants.TXT_BTN_DELETE);
	            deleteItem.setImage(imageRegister.getImage(IImageConstants.IMAGE_REMOVE));
	            deleteItem.addSelectionListener(listener);
	            
	            
	        }
	    });
	}
	
	public void setItems(String[] itemList)
	{
		root.removeAll();
		
		for(int i = 0; i < itemList.length; i++)
		{
			String item = itemList[i];
			
			addTreeItem(item, root, -1);
		}
		
		root.setExpanded(true);
	}
	
	private void addTreeItem(String blockName, TreeItem parent, int index)
	{
		TreeItem it;
		
		if( index < 0 )
		{
			it = new TreeItem(parent, SWT.NONE);
		}
		else
		{
			it = new TreeItem(parent, SWT.NONE, index);
		}
		it.setText(blockName);
		it.setImage(imageRegister.getImage(IImageConstants.IMAGE_RECORD));
	}

	
	
	public void setTreeSelectedBlock(String blockName)
	{
		TreeItem[] items = root.getItems();
		
		for( int i = 0 ; i < items.length; i++)
		{
			if( blockName.trim().equals(items[i].getText().trim()))
			{
				itemList.select(items[i]);
			}
		}
	}

	public TableComposite getTableComposite()
	{
		return tableComposite;
	}

	public void setTableComposite(TableComposite tableComposite)
	{
		this.tableComposite = tableComposite;
	}
	
	private TreeItem getTreeItem(String name)
	{
		TreeItem[] items = root.getItems();
		
		for(int i = 0; i < items.length; i++)
		{
			TreeItem ti = items[i];
			
			if(name.equals(ti.getText()))
			{
				return ti;
			}
		}
		
		return null;
	}
	
	public void receivedAction(int type, Object content)
	{
		if( IConstants.ACTION_LOAD_DATA == type)
		{
			String[] items = controller.getItemNames();
			
			setItems(items);
			
			if(items.length > 0)
			{
				String s = items[0];
				setTreeSelectedBlock(s);
			}
			else
			{
			}
			
			ActionManager.INSTANCE.sendAction(IConstants.ACTION_LOG_WRITE_INFO, "Load Items: " + items.length);
			
		}
		
	}
	
	private void addNewBlock(Record content)
	{
//		InfoEntity newBlock = content;
//		
//		TreeItem selectedItem = getSelectedTreeItem();
//		TreeItem root = selectedItem.getParentItem();
//		
//		TreeItem parent;
//		int index;
//		
//		if( null == root )
//		{
//			parent = selectedItem;
//			index = 0;
//		}
//		else
//		{
//			parent = root;
//			index = parent.indexOf(selectedItem);
//		}
//		
//		addTreeItem(newBlock.getName(), parent, index);
	}
}
