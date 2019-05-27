package de.etas.tef.config.ui.core;

import java.text.SimpleDateFormat;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MenuItem;

import de.etas.tef.config.action.ActionManager;
import de.etas.tef.config.controller.InfoBlockWriter;
import de.etas.tef.config.controller.MainController;
import de.etas.tef.config.helper.CompositeID;
import de.etas.tef.config.helper.Constants;
import de.etas.tef.config.listener.IActionListener;

public class ConfigEditorMainScreen extends Composite implements IActionListener
{
	private ConfigComposite leftConfigComposite = null;
	private ConfigComposite rightConfigComposite = null;
	private SashForm configCompositeSashForm = null;
	private final MainController controller;
	private MenuItem leftPaneItem;
	private MenuItem rightPaneItem;
	private MenuItem showInfoPaneItem;
	private MenuItem connectItem;
	private StyledText txtInfoBlock;
	private SashForm main;
	private Label dateLabel;
	
	
	public final Image IMAGE_TITLE;
	public final Image IMAGE_PIN;
	public final Image IMAGE_EXIT;
	public final Image IMAGE_ABOUT;
	public final Image IMAGE_CONNECT;
	public final Image IMAGE_DISCONNECT;
	public final Image IMAGE_TIME;

	
	private static boolean isLeftSelected = true;
	private static boolean isRightSelected = false;
	private static boolean isConnected = false;
	private static boolean isInfoPaneShow = false;
	
	private static final int fromLeft = 0x00;
	private static final int fromRight = 0x01;
	private static final int fromConnect = 0x02;
	
	private static final SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
	

	public ConfigEditorMainScreen(Composite parent, int style)
	{
		super(parent, style);
		
		controller = new MainController();
		ActionManager.INSTANCE.addActionListener(this);
		
		
		
		IMAGE_TITLE = new Image(this.getDisplay(), "icons/file_24.png");
		IMAGE_PIN = new Image(this.getDisplay(), "icons/pin.png");
		IMAGE_EXIT = new Image(this.getDisplay(), "icons/exit.png");
		IMAGE_ABOUT = new Image(this.getDisplay(), "icons/about.png");
		IMAGE_CONNECT = new Image(this.getDisplay(), "icons/connect.png");
		IMAGE_DISCONNECT = new Image(this.getDisplay(), "icons/disconnect.png");
		IMAGE_TIME = new Image(this.getDisplay(), "icons/time.png");
		
		initMainScreen(this);
		initMainComponents(this);
	}

	private void initMainComponents(Composite shell)
	{
		main = new SashForm(shell, SWT.VERTICAL);
		GridData gd = new GridData(GridData.FILL_BOTH);
		main.setLayoutData(gd);

		configCompositeSashForm = new SashForm(main, SWT.HORIZONTAL);
		gd = new GridData(GridData.FILL_BOTH);
		configCompositeSashForm.setLayoutData(gd);
		
		leftConfigComposite = new ConfigComposite(configCompositeSashForm, SWT.BORDER, controller, CompositeID.COMPOSITE_LEFT);
		rightConfigComposite = new ConfigComposite(configCompositeSashForm, SWT.BORDER, controller, CompositeID.COMPOSITE_RIGHT);
		
		txtInfoBlock = new StyledText(main, SWT.MULTI | SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
		gd = new GridData(GridData.FILL_BOTH);
		gd.horizontalSpan = 3;
		txtInfoBlock.setLayoutData(gd);
		txtInfoBlock.setEditable(false);
		txtInfoBlock.setVisible(false);
		
		new InfoBlockWriter(txtInfoBlock, controller);

		main.setWeights(new int[]
		{ 1, 0 });
		
		rightConfigComposite.setVisible(false);
		configCompositeSashForm.setWeights(new int[] {1, 0});
	}

	private void initMainScreen(Composite shell)
	{
		GridLayout layout = new GridLayout(1, false);
		layout.marginTop = layout.marginLeft = layout.marginRight = layout.marginBottom = 0;
		shell.setLayout(layout);
	}

	@Override
	public void receivedAction(int type, int compositeID, Object content)
	{
		if (Constants.ACTION_COMPOSITE_CHANGED == type)
		{
			boolean[] value = (boolean[])content;
			
			boolean left = value[0];
			boolean right = value[1];
			
			leftConfigComposite.setVisible(left);
			rightConfigComposite.setVisible(right);
			
			if(left && right)
			{
				configCompositeSashForm.setWeights(new int[]{ 1, 1 });
			}
			else if(!left && right)
			{
				configCompositeSashForm.setWeights(new int[]{ 0, 1 });
			}
			else if(left && !right)
			{
				configCompositeSashForm.setWeights(new int[]{ 1, 0 });
			}
		}
	}
}
