package de.etas.tef.config.ui.core;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MenuAdapter;
import org.eclipse.swt.events.MenuEvent;
import org.eclipse.swt.events.SelectionEvent;
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

import de.etas.tef.config.action.ActionManager;
import de.etas.tef.config.controller.MainController;
import de.etas.tef.config.entity.ConfigBlock;
import de.etas.tef.config.helper.CompositeID;
import de.etas.tef.config.helper.Constants;

public class SearchTreeComponent extends AbstractComposite
{

	private Tree blockList;
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
	
	public SearchTreeComponent(Composite parent, int style, MainController controller, int compositeID)
	{
		super(parent, style, controller, compositeID);
		
		GridLayout layout = new GridLayout(1, false);
		layout.marginTop = 0;
		this.setLayout(layout);
		this.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		IMAGE_ADD = new Image(parent.getDisplay(), "icons/add.png");
		IMAGE_REMOVE = new Image(parent.getDisplay(), "icons/remove.png");
		IMAGE_COPY = new Image(parent.getDisplay(), "icons/copy.png");
		IMAGE_PASTE = new Image(parent.getDisplay(), "icons/paste.png");
		IMAGE_BLOCK = new Image(parent.getDisplay(), "icons/block.png");
		IMAGE_ROOT = new Image(parent.getDisplay(), "icons/root.png");
		
		initComponent(controller);
	}

	protected void initComponent(MainController controller)
	{
		new SearchComposite(this, SWT.BORDER, controller, getCompositeID());
		
		blockList = new Tree(this, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
		GridData gd = new GridData(GridData.FILL_BOTH);
		gd.heightHint = Constants.HEIGHT_HINT;
		blockList.setLayoutData(gd);
		
		root = new TreeItem(blockList, SWT.NONE);
		root.setText(Constants.TXT_CONFIG_FILE);
		root.setImage(IMAGE_ROOT);
		
		blockList.addSelectionListener(new SelectionListener()
		{
			
			@Override
			public void widgetSelected(SelectionEvent event)
			{
				String s = getSelectedTreeItem().getText();
				treeItemSelected(s);
				getController().setFocusedElement(Constants.FOCUS_BLOCK);
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent event)
			{
				
			}
		});
		
		tl = new TreeListener(blockList, getController(), getCompositeID());
		blockList.addMouseListener(tl);
		blockList.addKeyListener(tl);
		
	}
	
	private void treeItemSelected(String name)
	{
		getTableComposite().treeItemSelected(name.trim());
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
	            
	            if(blockList.getSelectionCount() <= 0)
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
	
	public void setBlockList(String[] blockList)
	{
		root.removeAll();
		
		for(int i = 0; i < blockList.length; i++)
		{
			String blockName = blockList[i];
			
			addTreeItem(blockName, root, -1);
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

	public TreeItem getSelectedTreeItem()
	{
		TreeItem ti = blockList.getSelection()[0];
		return ti;
	}
	
	public void setTreeSelectedBlock(String blockName)
	{
		TreeItem[] items = root.getItems();
		
		for( int i = 0 ; i < items.length; i++)
		{
			if( blockName.trim().equals(items[i].getText().trim()))
			{
				blockList.select(items[i]);
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
	
	public void receivedAction(int type, int compositeID, Object content)
	{
		if( type == Constants.ACTION_SOURCE_PARAMETER_SELECTED && compositeID != getCompositeID() && ((MainController)getController().getParent()).isConnected())
		{
			String blockName = (String)content;
			
			TreeItem ti = getTreeItem(blockName);
			
			if( null == ti )
			{
				ActionManager.INSTANCE.sendAction(Constants.ACTION_LOG_WRITE_WARNING, getCompositeID(), "Connected, but cannot find block: " + blockName);
				return;
			}
			
			blockList.setSelection(ti);
			treeItemSelected(blockName);
		}
		
		if( compositeID != getCompositeID() && compositeID != CompositeID.COMPOSITE_ALONE)
		{
			return;
		}
		
		if( Constants.ACTION_SET_SHOW_CONFIG_BLOCKS == type)
		{
			String[] blocks = getController().getShowConfigBlocks();
			setBlockList(blocks);
			
			if(blocks.length > 0)
			{
				String s = blocks[0];
				setTreeSelectedBlock(s);
				getTableComposite().treeItemSelected(s);
			}
			else
			{
				getTableComposite().treeItemSelected(Constants.EMPTY_STRING);
			}
			
		}
		
		if( Constants.ACTION_LOCK_SELECTION_CHANGED == type)
		{
			boolean locked = (boolean)content;
			
			if( locked )
			{
				blockList.setMenu(null);
				
				if(null != rightClickMenu)
				{
					rightClickMenu.dispose();
				}
			}
			else
			{
				createRightMenu(blockList, tl);
			}
		}
		
		if( Constants.ACTION_ADD_NEW_BLOCK == type )
		{
			
			addNewBlock((ConfigBlock)content);
		}
		
		if( Constants.ACTION_DELETE_BLOCK == type )
		{
			String blockName = (String)content;
			
			if( null == blockName || blockName.isEmpty() )
			{
				ActionManager.INSTANCE.sendAction(Constants.ACTION_LOG_WRITE_ERROR, CompositeID.COMPOSITE_ALONE, "Delete node name is empty");
				return;
			}
			
			TreeItem selected = getSelectedTreeItem();
			
			if(null == selected.getParentItem())
			{
				ActionManager.INSTANCE.sendAction(Constants.ACTION_LOG_WRITE_WARNING, CompositeID.COMPOSITE_ALONE, "Cannot remove the root node");
				return;
			}
			getController().removeBlock(blockName);
			
			TreeItem root = selected.getParentItem();
			selected.dispose();
			blockList.setSelection(root.getItem(0));
		}
		
		if( Constants.ACTION_COPY_BLOCK == type )
		{
			TreeItem cb = blockList.getSelection()[0];
			
			if( null == cb || null == cb.getParentItem() )
			{
				return;
			}
			
			String blockName = cb.getText();
			getController().copyBlock(blockName);
			ActionManager.INSTANCE.sendAction(Constants.ACTION_LOG_WRITE_INFO, CompositeID.COMPOSITE_ALONE, "Block is copied: " + blockName);
		}
		
		if( Constants.ACTION_PASTE_BLOCK == type )
		{
			ConfigBlock newBlock = getController().getCopyBlock();
			
			if( null == newBlock )
			{
				return;
			}
			getController().addConfigBlock(newBlock);
			addNewBlock(newBlock);
			ActionManager.INSTANCE.sendAction(Constants.ACTION_LOG_WRITE_INFO, CompositeID.COMPOSITE_ALONE, "Block is pasted: " + newBlock.getBlockName());
		}
		
		if( Constants.ACTION_COMMENT_SAVED == type && getController().getFocusedElement() == Constants.FOCUS_BLOCK )
		{
			getController().getSelectedConfigBlock().setComments((String) content);
		}
	}
	
	private void addNewBlock(ConfigBlock content)
	{
		ConfigBlock newBlock = content;
		
		TreeItem selectedItem = getSelectedTreeItem();
		TreeItem root = selectedItem.getParentItem();
		
		TreeItem parent;
		int index;
		
		if( null == root )
		{
			parent = selectedItem;
			index = 0;
		}
		else
		{
			parent = root;
			index = parent.indexOf(selectedItem);
		}
		
		addTreeItem(newBlock.getBlockName(), parent, index);
	}
}
