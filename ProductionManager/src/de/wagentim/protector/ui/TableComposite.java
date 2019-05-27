package de.wagentim.protector.ui;


import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.MenuAdapter;
import org.eclipse.swt.events.MenuEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import de.etas.tef.config.entity.KeyValuePair;
import de.etas.tef.production.help.ActionManager;
import de.wagentim.protector.controller.MainController;
import de.wagentim.protector.entity.Record;
import de.wagentim.protector.helper.Constants;

public class TableComposite extends AbstractComposite
{
	private Table table;
//	private Button btnAdd;
//	private Button btnDelete;
//	private Button btnSave;
//	private Button btnLock;
	protected Color tableBackgroudColor;
	private Menu rightClickMenu;
	
	public final Image IMAGE_ADD;
	public final Image IMAGE_REMOVE;
	public final Image IMAGE_COPY;
	public final Image IMAGE_PASTE;
	
	private SearchTreeComponent searchTree;
	
	public TableComposite(Composite parent, int style, MainController controller)
	{
		super(parent, style, controller);

		this.setLayout(new GridLayout(2, false));
		this.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		initMainComposite(this, controller);
		
		IMAGE_ADD = new Image(parent.getDisplay(), "icons/add.png");
		IMAGE_REMOVE = new Image(parent.getDisplay(), "icons/remove.png");
		IMAGE_COPY = new Image(parent.getDisplay(), "icons/copy.png");
		IMAGE_PASTE = new Image(parent.getDisplay(), "icons/paste.png");
		
		tableBackgroudColor = parent.getDisplay().getSystemColor(SWT.COLOR_INFO_BACKGROUND);
	}
	
	public Table getTable()
	{
		return table;
	}

	protected void initMainComposite(Composite comp, MainController controller)
	{
		SashForm sf = new SashForm(comp, SWT.HORIZONTAL);
		GridData gd = new GridData(GridData.FILL_BOTH);
		sf.setLayoutData(gd);
		
		searchTree = new SearchTreeComponent(sf, SWT.NONE, controller);
		searchTree.setTableComposite(this);
		
		Composite tableComposite = new Composite(sf, SWT.NONE);
		GridLayout layout = new GridLayout(1, false);
		layout.marginTop = layout.marginBottom = layout.marginLeft = layout.marginRight = 0; 
		tableComposite.setLayout(layout);
		tableComposite.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		table = new Table(tableComposite, SWT.MULTI | SWT.BORDER | SWT.FULL_SELECTION);
		table.setLinesVisible(true);
		table.setHeaderVisible(false);
		
		gd = new GridData(GridData.FILL_BOTH);
		gd.heightHint = Constants.HEIGHT_HINT;
		table.setLayoutData(gd);
		
		TableListener tl = new TableListener(getTable(), controller);
	    
		for (int i = 0; i < 2; i++) 
		{
			TableColumn column = new TableColumn(table, SWT.LEFT);
			column.setResizable(true);
			column.setWidth(150);
		}
		
		table.addMouseListener(tl);
		table.addKeyListener(tl);
		addTableSelectedListener();
		sf.setWeights(new int[]{1, 2});
	}
	
	private void btnAddAction()
	{
		if ( null == controller.getSelectedItem())
		{
			return;
		}
	}
	
	private void btnDeleteAction()
	{
		deleteSelectedItems();
//		treeItemSelected(searchTree.getSelectedTreeItem().getText().trim());
	}

	protected void addTableSelectedListener()
	{
		getTable().addSelectionListener(new SelectionAdapter()
		{
			
			@Override
			public void widgetSelected(SelectionEvent event)
			{
				controller.setFocusedElement(Constants.FOCUS_PARAMETER);
				ActionManager.INSTANCE.sendAction(Constants.ACTION_PARAMETER_SELECTED, getTable().getSelectionIndex());

				String text = getTable().getItem(getTable().getSelectionIndex()).getText(1);
					
				ActionManager.INSTANCE.sendAction(Constants.ACTION_SOURCE_PARAMETER_SELECTED, text);
			}
			
		});
	}

	protected void setTreeSelectedBlock(String blockName)
	{
		searchTree.setTreeSelectedBlock(blockName);
	}
	
	protected void setBlockList(String[] blockList)
	{
		searchTree.setItems(blockList);
	}
	
	private void createRightMenu(Control control, SelectionListener listener)
	{
		rightClickMenu = new Menu(table);
		table.setMenu(rightClickMenu);
		
		rightClickMenu.addMenuListener(new MenuAdapter()
	    {
	        public void menuShown(MenuEvent e)
	        {
	            MenuItem[] items = rightClickMenu.getItems();
	            
	            for (int i = 0; i < items.length; i++)
	            {
	                items[i].dispose();
	            }
	            
	            if(table.getSelectionCount() <= 0)
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

	protected void deleteSelectedItems()
	{
		int[] selectedItems = table.getSelectionIndices();
		
		if( null != selectedItems && selectedItems.length > 0 )
		{
			
			MessageBox mb = new MessageBox(this.getShell(), SWT.ICON_WARNING | SWT.YES | SWT.NO);

			mb.setMessage("Do you really want to delete?");

			boolean done = mb.open() == SWT.YES;
			
			if( done )
			{
//				controller.deleteParameters(selectedItems, searchTree.getSelectedTreeItem().getText());
			}
		}
	}


	public void updateParameters(Record entity)
	{
		clearTable();

		if( null == table || null == entity )
		{
			return;
		}
		
//		KeyValuePair pair = new KeyValuePair("Name", entity.getName());
//		addTableItem(pair);
//		pair = new KeyValuePair("Content", entity.getContent());
//		addTableItem(pair);
//		pair = new KeyValuePair("Link", entity.getLink());
//		addTableItem(pair);
//		pair = new KeyValuePair("Use Name", entity.getUname());
//		addTableItem(pair);
//		pair = new KeyValuePair("Password", entity.getPassword());
//		addTableItem(pair);
		
		resize();
	}
	
	private void addTableItem(KeyValuePair pair)
	{
		TableItem ti = new TableItem(table, SWT.NONE);
		ti.setText(0, pair.getKey());
		ti.setText(1, pair.getValue());
		ti.setBackground(tableBackgroudColor);
	}
	
	private void resize()
	{
		for (TableColumn tc : table.getColumns())
		{
	        resizeColumn(tc);
		}
	}
	
	private static void resizeColumn(TableColumn tableColumn_)
	{
	    tableColumn_.pack();

	}
	
	protected void clearTable()
	{
		if( null == table )
		{
			return;
		}
			
		TableItem[] items = table.getItems();
		
		if( items.length <= 0 )
		{
			return;
		}
		
		for(int i = items.length - 1; i >= 0; i--)
		{
			TableItem item = items[i];
			table.remove(i);
			item.dispose();
		}
	}

	protected String fileSave(Shell shell)
	{
		FileDialog fd = new FileDialog(shell, SWT.APPLICATION_MODAL | SWT.SAVE);
		fd.setFilterExtensions(Constants.CONFIG_FILE_EXTENSION);
		fd.setFilterNames(Constants.CONFIG_FILE_NAME);
		String result = fd.open();
		if( null != result )
		{
			Path file = Paths.get(result);
	        if (Files.exists(file))
	        {
	          MessageBox mb = new MessageBox(fd.getParent(), SWT.ICON_WARNING | SWT.YES | SWT.NO);

	          mb.setMessage(result + " already exists. Do you want to replace it?");

	          boolean done = mb.open() == SWT.YES;
	          
	          if( !done )
	          {
	        	return null;  
	          }
	        }
		}
		
		return result;
	}
	
	private void toSave() 
	{
		String fileSavePath = fileSave(this.getShell());

		if (null == fileSavePath)
		{
			return;
		}

		saveAction(fileSavePath);
	}
	
	protected void saveAction(String targetFilePath)
	{
		controller.saveFile(targetFilePath);
		ActionManager.INSTANCE.sendAction(Constants.ACTION_LOG_WRITE_INFO, "Source Write to: " + targetFilePath + " finished!");
	}
	
	@Override
	public void receivedAction(int type, Object content)
	{
		if( type == Constants.ACTION_BLOCK_SELECTED )
		{
			updateParameters(controller.getSelectedItem());
		}

		if( type == Constants.ACTION_LOAD_DATA )
		{
			updateParameters(null);
		}
		
	}
}
