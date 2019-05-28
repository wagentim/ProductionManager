package de.wagentim.protector.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MenuAdapter;
import org.eclipse.swt.events.MenuEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import de.etas.tef.production.help.ActionManager;
import de.etas.tef.production.help.Constants;
import de.wagentim.protector.controller.MainController;
import de.wagentim.protector.entity.Record;

public class SearchTreeComponent extends AbstractComposite
{

	private Tree itemList;
	private TreeItem root;
	private TableComposite tableComposite;

	private TreeListener tl;
	private Menu rightClickMenu;
	
	public final Image IMAGE_ADD;
	public final Image IMAGE_REMOVE;
	public final Image IMAGE_COPY;
	public final Image IMAGE_PASTE;
	public final Image IMAGE_BLOCK;
	public final Image IMAGE_ROOT;
	
	public SearchTreeComponent(Composite parent, int style, MainController controller)
	{
		super(parent, style, controller);
		
		GridLayout layout = new GridLayout(1, false);
		layout.marginTop = layout.marginBottom = layout.marginLeft = layout.marginRight = 0; 
		this.setLayout(layout);
		this.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		IMAGE_ADD = new Image(parent.getDisplay(), "icons/add.png");
		IMAGE_REMOVE = new Image(parent.getDisplay(), "icons/remove.png");
		IMAGE_COPY = new Image(parent.getDisplay(), "icons/copy.png");
		IMAGE_PASTE = new Image(parent.getDisplay(), "icons/paste.png");
		IMAGE_BLOCK = new Image(parent.getDisplay(), "icons/record.png");
		IMAGE_ROOT = new Image(parent.getDisplay(), "icons/root.png");
		
		initComponent(controller);
	}

	protected void initComponent(MainController controller)
	{
		new SearchComposite(this, SWT.BORDER, controller);
		
		itemList = new Tree(this, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
		GridData gd = new GridData(GridData.FILL_BOTH);
		gd.heightHint = Constants.HEIGHT_HINT;
		itemList.setLayoutData(gd);
		
		root = new TreeItem(itemList, SWT.NONE);
		root.setText(Constants.TXT_CONFIG_FILE);
		root.setImage(IMAGE_ROOT);
		
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
	            copyItem.setText(Constants.TXT_COPY);
	            copyItem.setImage(IMAGE_COPY);
	            copyItem.addSelectionListener(listener);
	            
	            MenuItem pasteItem = new MenuItem(rightClickMenu, SWT.NONE);
	            pasteItem.setText(Constants.TXT_PASTE);
	            pasteItem.setImage(IMAGE_PASTE);
	            pasteItem.addSelectionListener(listener);
	            
	            new MenuItem(rightClickMenu, SWT.SEPARATOR);
	            
	            MenuItem newItem = new MenuItem(rightClickMenu, SWT.NONE);
	            newItem.setText(Constants.TXT_BTN_ADD);
	            newItem.setImage(IMAGE_ADD);
	            newItem.addSelectionListener(listener);

	            MenuItem deleteItem = new MenuItem(rightClickMenu, SWT.NONE);
	            deleteItem.setText(Constants.TXT_BTN_DELETE);
	            deleteItem.setImage(IMAGE_REMOVE);
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
		it.setImage(IMAGE_BLOCK);
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
		if( Constants.ACTION_LOAD_DATA == type)
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
			
			ActionManager.INSTANCE.sendAction(Constants.ACTION_LOG_WRITE_INFO, "Load Items: " + items.length);
			
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
