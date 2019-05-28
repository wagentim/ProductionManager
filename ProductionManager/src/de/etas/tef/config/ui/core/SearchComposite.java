package de.etas.tef.config.ui.core;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import de.etas.tef.config.action.ActionManager;
import de.etas.tef.config.controller.IController;
import de.etas.tef.config.controller.MainController;
import de.etas.tef.config.helper.CompositeID;
import de.etas.tef.config.helper.Constants;

public class SearchComposite extends AbstractComposite
{
	
	protected IController controller;
	private Text searchText;
	private final Color WHITE;
	
	public SearchComposite(Composite parent, int style, MainController controller, int compositeID)
	{
		super(parent, style, controller, compositeID);
		WHITE = parent.getDisplay().getSystemColor(SWT.COLOR_WHITE);
		
		GridLayout layout = new GridLayout(2, false);
		layout.marginTop = layout.marginBottom = layout.marginLeft = layout.marginRight = 0; 
		layout.marginHeight = layout.marginWidth = 3;
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.minimumHeight = 30;
		this.setLayout(layout);
		this.setLayoutData(gd);
		this.setBackground(WHITE);
		
		Image image = new Image(this.getDisplay(), "icons/search.png");
		Label label = new Label(this, SWT.NONE);
		label.setImage(image);
		gd = new GridData();
		label.setLayoutData(gd);
		label.setBackground(WHITE);
		
		searchText = new Text(this, SWT.NONE);
		gd = new GridData(GridData.FILL_BOTH);
		gd.verticalSpan = gd.horizontalSpan = 0;
		searchText.setLayoutData(gd);
		searchText.setMessage("Search");
		searchText.setBackground(WHITE);
		
		searchText.addModifyListener(new ModifyListener()
		{
			
			@Override
			public void modifyText(ModifyEvent event)
			{
				String text = searchText.getText();
				getController().setShowConfigBlocks(text);
				ActionManager.INSTANCE.sendAction(Constants.ACTION_SET_SHOW_CONFIG_BLOCKS, compositeID, null);
			}
		});
		
		this.controller = controller;
	}
	
	@Override
	public void receivedAction(int type, int compositeID, Object content)
	{
		if( compositeID != getCompositeID() && compositeID != CompositeID.COMPOSITE_ALONE)
		{
			return;
		}
		
		if( type == Constants.ACTION_NEW_FILE_SELECTED || type == Constants.ACTION_DROP_NEW_FILE_SELECTED)
		{
			searchText.setText(Constants.EMPTY_STRING);
		}
	}
}
