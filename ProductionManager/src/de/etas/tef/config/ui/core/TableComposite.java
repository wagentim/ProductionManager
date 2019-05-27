package de.etas.tef.config.ui.core;


import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

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
import org.eclipse.swt.widgets.Button;
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

import de.etas.tef.config.action.ActionManager;
import de.etas.tef.config.controller.MainController;
import de.etas.tef.config.entity.ConfigBlock;
import de.etas.tef.config.entity.ConfigFile;
import de.etas.tef.config.entity.KeyValuePair;
import de.etas.tef.config.helper.CompositeID;
import de.etas.tef.config.helper.Constants;

public class TableComposite extends AbstractComposite
{
	private Table table;
	private Button btnAdd;
	private Button btnDelete;
	private Button btnSave;
	private Button btnLock;
	protected Color tableBackgroudColor;
	private Composite buttonComposite;
	private Menu rightClickMenu;
	
	public final Image IMAGE_ADD;
	public final Image IMAGE_REMOVE;
	public final Image IMAGE_COPY;
	public final Image IMAGE_PASTE;
	
	private SearchTreeComponent searchTree;
	
	public TableComposite(Composite parent, int style, MainController controller, int compositeID)
	{
		super(parent, style, controller, compositeID);

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

	public Button getBtnAdd()
	{
		return btnAdd;
	}

	public Button getBtnDelete()
	{
		return btnDelete;
	}

	public Button getBtnSave()
	{
		return btnSave;
	}
	
	protected void initMainComposite(Composite comp, MainController controller)
	{
		SashForm sf = new SashForm(comp, SWT.HORIZONTAL);
		GridData gd = new GridData(GridData.FILL_BOTH);
		sf.setLayoutData(gd);
		
		searchTree = new SearchTreeComponent(sf, SWT.NONE, controller, getCompositeID());
		searchTree.setTableComposite(this);
		
		Composite tableComposite = new Composite(sf, SWT.NONE);
		GridLayout layout = new GridLayout(1, false);
		layout.marginTop = layout.marginBottom = layout.marginLeft = layout.marginRight = 0; 
		layout.marginHeight = layout.marginWidth = 0;
		gd = new GridData(GridData.FILL_HORIZONTAL);
		tableComposite.setLayout(layout);
		tableComposite.setLayoutData(gd);
		
		table = new Table(tableComposite, SWT.MULTI | SWT.BORDER | SWT.FULL_SELECTION);
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		
		gd = new GridData(GridData.FILL_BOTH);
		gd.heightHint = Constants.HEIGHT_HINT;
		table.setLayoutData(gd);
		
		TableListener tl = new TableListener(getTable(), getController(), getCompositeID());
	    
		for (int i = 0; i < Constants.TABLE_TITLES.length; i++) 
		{
			TableColumn column = new TableColumn(table, SWT.LEFT);
			column.setText(Constants.TABLE_TITLES[i]);
			column.setResizable(true);
			column.setWidth(150);
		}
		
		table.addMouseListener(tl);
		table.addKeyListener(tl);
		addTableSelectedListener();
		sf.setWeights(new int[]{1, 2});
		
		buttonComposite = new Composite(comp, SWT.NONE);
		buttonComposite.setLayout(new GridLayout(1, false));
		
		gd = new GridData();
		gd.widthHint = Constants.BTN_DEFAULT_WIDTH;
		
		btnAdd = new Button(buttonComposite, SWT.PUSH);
		btnAdd.setText(Constants.TXT_BTN_ADD);
		btnAdd.setLayoutData(gd);
		btnAdd.setEnabled(false);
		btnAdd.addSelectionListener(new SelectionListener()
		{
			
			@Override
			public void widgetSelected(SelectionEvent arg0)
			{
				btnAddAction();
			}

			
			
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0)
			{
				// TODO Auto-generated method stub
				
			}
		});
		
		btnDelete = new Button(buttonComposite, SWT.PUSH);
		btnDelete.setText(Constants.TXT_BTN_DELETE);
		btnDelete.setLayoutData(gd);
		btnDelete.setEnabled(false);
		btnDelete.addSelectionListener(new SelectionListener() 
		{
			
			@Override
			public void widgetSelected(SelectionEvent event) 
			{
				btnDeleteAction();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) 
			{
				
			}
		});
		
		
		btnSave = new Button(buttonComposite, SWT.PUSH);
		btnSave.setText(Constants.TXT_BTN_SAVE);
		btnSave.setLayoutData(gd);
		btnSave.setEnabled(false);
		btnSave.addSelectionListener(new SelectionListener()
		{
			
			@Override
			public void widgetSelected(SelectionEvent arg0)
			{
				if( null == getController().getConfigFile() )
				{
					return;
				}
				
				toSave();
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0)
			{
				// TODO Auto-generated method stub
				
			}
		});
		
		btnLock = new Button(buttonComposite, SWT.CHECK);
		btnLock.setText(Constants.TXT_LOCK_EDITING);
		gd = new GridData();
		gd.widthHint = Constants.BTN_DEFAULT_WIDTH;
		gd.heightHint = 80;
		btnLock.setLayoutData(gd);
		btnLock.setSelection(true);
		
		btnLock.addSelectionListener(new SelectionListener()
		{
			
			@Override
			public void widgetSelected(SelectionEvent arg0)
			{
				boolean locked = btnLock.getSelection();
				
				ConfigFile cf = getController().getConfigFile();
				
				if( null == cf )
				{
					cf = new ConfigFile();
					getController().setConfigFile(cf);
				}
				
				getController().setEditingLocked(locked);
				
				if( locked )
				{
					table.setMenu(null);
					
					if(null != rightClickMenu)
					{
						rightClickMenu.dispose();
					}
				}
				else
				{
					createRightMenu(table, tl);
				}
				
				setButtonLocked(locked);
				
				ActionManager.INSTANCE.sendAction(Constants.ACTION_LOCK_SELECTION_CHANGED, getCompositeID(), locked);
			}

			
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0)
			{
				
			}
		});
	}
	
	private void btnAddAction()
	{
		if ( null == getController().getSelectedConfigBlock())
		{
			return;
		}
		
		KeyValuePair kvp = new KeyValuePair();
		
		addTableItem(kvp);
		getController().parameterAdded(kvp);
	}
	
	private void btnDeleteAction()
	{
		deleteSelectedItems();
		treeItemSelected(searchTree.getSelectedTreeItem().getText().trim());
	}

	private void setButtonLocked(boolean locked)
	{
		btnAdd.setEnabled(!locked);
		btnDelete.setEnabled(!locked);
		btnSave.setEnabled(!locked);
	}
	
	protected void addTableSelectedListener()
	{
		getTable().addSelectionListener(new SelectionAdapter()
		{
			
			@Override
			public void widgetSelected(SelectionEvent event)
			{
				getController().setFocusedElement(Constants.FOCUS_PARAMETER);
				ActionManager.INSTANCE.sendAction(Constants.ACTION_PARAMETER_SELECTED, getCompositeID(), getTable().getSelectionIndex());

				String text = getTable().getItem(getTable().getSelectionIndex()).getText(1);
				
				if( !((MainController)(getController().getParent())).isConnected() )
				{
					return;
				}
					
				ActionManager.INSTANCE.sendAction(Constants.ACTION_SOURCE_PARAMETER_SELECTED, getCompositeID(), text);
			}
			
		});
	}

	protected void treeItemSelected(String blockName)
	{
		if( null == blockName || blockName.isEmpty() )
		{
			updateParameters(Collections.emptyList());
			return;
		}
		
		getController().setSelectedBlock(blockName);
		
		ConfigBlock cb = getController().getSelectedConfigBlock();
		
		if( null != cb && cb.getBlockName().contentEquals(blockName))
		{
			updateParameters(cb.getAllParameters());
			ActionManager.INSTANCE.sendAction(Constants.ACTION_BLOCK_SELECTED, getCompositeID(), cb);
		}
	}

	protected void setTreeSelectedBlock(String blockName)
	{
		searchTree.setTreeSelectedBlock(blockName);
	}
	
	protected void setBlockList(String[] blockList)
	{
		searchTree.setBlockList(blockList);
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
				getController().deleteParameters(selectedItems, searchTree.getSelectedTreeItem().getText());
			}
		}
	}


	public void updateParameters(List<KeyValuePair> values)
	{
		clearTable();

		if( null == table || null == values || values.size() < 1 )
		{
			return;
		}
		
		
		for(int i = 0; i < values.size(); i++)
		{
			addTableItem(values.get(i));
			
		}
	}
	
	protected void addTableItem(KeyValuePair kvp)
	{
		TableItem ti = new TableItem(table, SWT.NONE);
		ti.setText(0, kvp.getKey());
		ti.setText(1, kvp.getValue());
		ti.setText(2, kvp.getOther());
		ti.setText(3, kvp.getForthValue());
		ti.setBackground(tableBackgroudColor);
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
		getController().saveFile(targetFilePath);
		ActionManager.INSTANCE.sendAction(Constants.ACTION_LOG_WRITE_INFO, getCompositeID(), "Source Write to: " + targetFilePath + " finished!");
	}
	
	@Override
	public void receivedAction(int type, int compositeID, Object content)
	{
		if( compositeID != getCompositeID() && compositeID != CompositeID.COMPOSITE_ALONE )
		{
			return;
		}
		
		if( type == Constants.ACTION_PARAMETER_UPDATE )
		{
			ConfigBlock cb = getController().getSelectedConfigBlock();
			if( null != cb )
			{
				updateParameters(cb.getAllParameters());
			}
			else
			{
				updateParameters(Collections.emptyList());
			}
		}
		
		if( type == Constants.ACTION_NEW_FILE_SELECTED || type == Constants.ACTION_DROP_NEW_FILE_SELECTED)
		{
			clearTable();
			String[] allBlocks = getController().getAllBlocks();
			setBlockList(allBlocks);
		}
		
		if( type == Constants.ACTION_MENU_ADD )
		{
			btnAddAction();
		}
		
		if( type == Constants.ACTION_MENU_DELETE )
		{
			btnDeleteAction();
		}
		
		if( type == Constants.ACTION_COPY_PARAMETER )
		{
			int[] selectedItems = table.getSelectionIndices();
			
			if( null == selectedItems || selectedItems.length <= 0 )
			{
				return;
			}
			
			getController().copyParameters(selectedItems);
			ActionManager.INSTANCE.sendAction(Constants.ACTION_LOG_WRITE_INFO, CompositeID.COMPOSITE_ALONE, "Parameters are copied");
		}
		
		if( type == Constants.ACTION_PASTE_PARAMETER )
		{
			List<KeyValuePair> copyParameters = getController().getCopyParameters();
			
			if( null == copyParameters || copyParameters.isEmpty() )
			{
				return;
			}
			
			Iterator<KeyValuePair> it = copyParameters.iterator();
			
			while(it.hasNext())
			{
				addTableItem(it.next());
			}
			ActionManager.INSTANCE.sendAction(Constants.ACTION_LOG_WRITE_INFO, CompositeID.COMPOSITE_ALONE, "Parameters are pasted");
		}
		
		if( Constants.ACTION_COMMENT_SAVED == type && getController().getFocusedElement() == Constants.FOCUS_PARAMETER )
		{
			List<KeyValuePair> paras = getController().getSelectedConfigBlock().getAllParameters();
			int index = table.getSelectionIndex();
			
			if(index >= 0)
			{
				paras.get(index).setComment((String) content);
			}
		}
	}
}
