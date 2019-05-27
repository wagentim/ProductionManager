package de.wagentim.protector.ui;

import java.text.SimpleDateFormat;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Monitor;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

import de.etas.tef.production.help.ActionManager;
import de.wagentim.protector.controller.InfoBlockWriter;
import de.wagentim.protector.controller.MainController;
import de.wagentim.protector.helper.Constants;
import de.wagentim.protector.listener.IActionListener;

public class ProtectorMainScreen extends Composite implements IActionListener
{
	private final MainController controller;
	private MenuItem showInfoPaneItem;
	private StyledText txtInfoBlock;
	private SashForm main;
	private Label dateLabel;
	
	public final Image IMAGE_TITLE;
	public final Image IMAGE_PIN;
	public final Image IMAGE_EXIT;
	public final Image IMAGE_ABOUT;
	public final Image IMAGE_LOCK;
	public final Image IMAGE_UNLOCK;
	public final Image IMAGE_TIME;
	public final Image IMAGE_LOAD;
	
	private static boolean isInfoPaneShow = false;
	
	private static final SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");

	public ProtectorMainScreen(Composite parent, int style)
	{
		super(parent, style);
		
		controller = new MainController();
		ActionManager.INSTANCE.addActionListener(this);
		
		IMAGE_TITLE = new Image(this.getDisplay(), "icons/title.png");
		IMAGE_PIN = new Image(this.getDisplay(), "icons/pin.png");
		IMAGE_EXIT = new Image(this.getDisplay(), "icons/exit.png");
		IMAGE_ABOUT = new Image(this.getDisplay(), "icons/about.png");
		IMAGE_LOCK = new Image(this.getDisplay(), "icons/lock.png");
		IMAGE_UNLOCK = new Image(this.getDisplay(), "icons/unlock.png");
		IMAGE_TIME = new Image(this.getDisplay(), "icons/time.png");
		IMAGE_LOAD = new Image(this.getDisplay(), "icons/load.png");
		
		
		initMainScreen(this);
		initMainComponents(this);
	}
	
	private void initMainComponents(Composite shell)
	{
		main = new SashForm(shell, SWT.VERTICAL);
		GridData gd = new GridData(GridData.FILL_BOTH);
		main.setLayoutData(gd);

		new MainComposite(main, SWT.BORDER, controller);
		
		txtInfoBlock = new StyledText(main, SWT.MULTI | SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
		gd = new GridData(GridData.FILL_BOTH);
		gd.horizontalSpan = 3;
		txtInfoBlock.setLayoutData(gd);
		txtInfoBlock.setEditable(false);
		txtInfoBlock.setVisible(false);
		
		new InfoBlockWriter(txtInfoBlock, controller);

		main.setWeights(new int[]
		{ 1, 0 });
		
	}

	private void initMainScreen(Composite shell)
	{
		GridLayout layout = new GridLayout(1, false);
		layout.marginTop = layout.marginLeft = layout.marginRight = layout.marginBottom = 0;
		shell.setLayout(layout);
		
		ToolBar bar = new ToolBar(shell, SWT.NONE);
		ToolItem lockItem = new ToolItem(bar, SWT.PUSH);
		lockItem.setText("Lock");
		lockItem.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(SelectionEvent arg0)
			{
				// TODO Auto-generated method stub

			}
		});
	}

	@Override
	public void receivedAction(int type, Object content)
	{
	}
}
