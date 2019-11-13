package de.etas.tef.production.ui;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Monitor;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;

import de.etas.tef.app.IGlobalConstants;
import de.etas.tef.production.zipsftp.gui.FTPMainScreen;
import de.wagentim.common.IImageConstants;
import de.wagentim.common.ImageRegister;
import de.wagentim.common.ui.IStatusBarUpdate;
import de.wagentim.protector.ui.ProtectorMainScreen;

public class MainScreen implements IStatusBarUpdate
{
	private static final SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
	
	private Label dateLabel;
	private Label textLabel;
	private ImageRegister imageRegister;
	private final Color WHITE;
	private final Color BLUE;
	private Composite statusbar;

	public MainScreen()
	{
		Display display = new Display();
		imageRegister = new ImageRegister(display);
		WHITE = display.getSystemColor(SWT.COLOR_WHITE);
		BLUE = display.getSystemColor(SWT.COLOR_BLUE);
		Shell shell = new Shell(display);
		shell.setBackground(WHITE);
		shell.setImage(imageRegister.getImage(IImageConstants.IMAGE_HOME));
		shell.setText(IGlobalConstants.TXT_APP);
		shell.addShellListener(new ShellAdapter()
		{

			@Override
			public void shellClosed(ShellEvent arg0)
			{
				System.exit(0);
			}

		});
		
		initMainScreen(shell);
		initFolders(shell, imageRegister);
		initMenu(shell);
		initStatusBar(shell);
		
		Runnable timer = new Runnable()
		{
			public void run()
			{
				dateLabel.setText(" " + sdf.format(new Date()) + " ");
				display.timerExec(1000, this);
			}
		};
		display.timerExec(1000, timer);
		
		shell.open();
		while (!shell.isDisposed())
		{
			if (!display.readAndDispatch())
			{
				display.sleep();
			}
		}
		display.dispose();
	}
	
	private void initFolders(Shell shell, ImageRegister imageRegister)
	{
		final TabFolder tabFolder = new TabFolder (shell, SWT.NONE);
		GridLayout layout = new GridLayout(1, false);
		layout.marginTop = layout.marginLeft = layout.marginRight = layout.marginBottom = 0;
		tabFolder.setLayout(layout);
		GridData gd = new GridData(GridData.FILL_BOTH);
		tabFolder.setLayoutData(gd);
		
		TabItem protectorItem = new TabItem (tabFolder, SWT.NONE);
		protectorItem.setText (IGlobalConstants.TXT_FOLDER_PASSWORD_PROTECTOR);
		protectorItem.setImage(imageRegister.getImage(IImageConstants.IMAGE_PASSWORD_TITLE));
		ProtectorMainScreen protectorEditor = new ProtectorMainScreen (tabFolder, SWT.NONE, imageRegister, this);
		protectorItem.setControl (protectorEditor);
		
		TabItem systemItem = new TabItem (tabFolder, SWT.NONE);
		systemItem.setText (IGlobalConstants.TXT_FOLDER_SYSTEM);
		systemItem.setImage(imageRegister.getImage(IImageConstants.IMAGE_PASSWORD_TITLE));
		SystemMainScreen systemEditor = new SystemMainScreen (tabFolder, SWT.NONE, imageRegister, this);
		systemItem.setControl (systemEditor);
		
		TabItem ftpItem = new TabItem (tabFolder, SWT.NONE);
		ftpItem.setText (IGlobalConstants.TXT_FOLDER_SFTP);
		ftpItem.setImage(imageRegister.getImage(IImageConstants.IMAGE_PASSWORD_TITLE));
		FTPMainScreen ftpEditor = new FTPMainScreen (tabFolder, SWT.NONE, imageRegister);
		ftpItem.setControl (ftpEditor);
		
	}

	private void initMainScreen(Composite shell)
	{
		Monitor primary = shell.getDisplay().getPrimaryMonitor();
		Rectangle area = primary.getClientArea();
		shell.setBounds((Math.abs(area.width - IGlobalConstants.MAIN_SCREEN_WIDTH)) / 2,
				Math.abs((area.height - IGlobalConstants.MAIN_SCREEN_HEIGHT)) / 2, IGlobalConstants.MAIN_SCREEN_WIDTH,
				IGlobalConstants.MAIN_SCREEN_HEIGHT);

		GridLayout layout = new GridLayout(1, false);
		layout.marginHeight = layout.marginWidth = layout.marginTop = layout.marginLeft = layout.marginRight = layout.marginBottom = 0;
		shell.setLayout(layout);
	}
	
	private void initMenu(Shell shell)
	{
		Menu menuBar = new Menu(shell, SWT.BAR);
		
	    MenuItem fileMenuHeader = new MenuItem(menuBar, SWT.CASCADE);
	    fileMenuHeader.setText("&File");

	    Menu fileMenu = new Menu(shell, SWT.DROP_DOWN);
	    fileMenuHeader.setMenu(fileMenu);

	    MenuItem fileExitItem = new MenuItem(fileMenu, SWT.PUSH);
	    fileExitItem.setText("E&xit");
	    fileExitItem.setImage(imageRegister.getImage(IImageConstants.IMAGE_EXIT));
	    fileExitItem.addSelectionListener(new SelectionAdapter()
		{
	    	@Override
			public void widgetSelected(SelectionEvent arg0)
			{
	    		MessageBox mb = new MessageBox(shell, SWT.ICON_WARNING | SWT.YES | SWT.NO);
	    		mb.setText("Exit Confirmation");
				mb.setMessage("Do you really want to Exit?");

				boolean done = mb.open() == SWT.YES;
				
				if( done )
				{
					System.exit(0);
				}
			}
		});
	    
	    MenuItem functionMenuHeader = new MenuItem(menuBar, SWT.CASCADE);
	    functionMenuHeader.setText("F&unction");
	    
	    Menu functionMenu = new Menu(shell, SWT.DROP_DOWN);
	    functionMenuHeader.setMenu(functionMenu);
	    
	    MenuItem windowMenuHeader = new MenuItem(menuBar, SWT.CASCADE);
	    windowMenuHeader.setText("Window");
	    
	    Menu windowMenu = new Menu(shell, SWT.DROP_DOWN);
	    windowMenuHeader.setMenu(windowMenu);
	    
	    MenuItem aboutMenuHeader = new MenuItem(menuBar, SWT.CASCADE);
	    aboutMenuHeader.setText("&?");

	    Menu aboutMenu = new Menu(shell, SWT.DROP_DOWN);
	    aboutMenuHeader.setMenu(aboutMenu);

	    MenuItem aboutItem = new MenuItem(aboutMenu, SWT.PUSH);
	    aboutItem.setText("&About");
	    aboutItem.setImage(shell.getDisplay().getSystemImage(SWT.ICON_QUESTION));
	    
	    shell.setMenuBar(menuBar);
	}
	
	private void initStatusBar(Shell shell)
	{
		
		statusbar = new Composite(shell, SWT.BORDER);

        GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
        gridData.heightHint = 16;
        statusbar.setLayoutData(gridData);
        RowLayout layout = new RowLayout();
        layout.marginLeft = layout.marginTop = 0;
        statusbar.setLayout(layout);
        
        Label image = new Label(statusbar, SWT.NONE);
        image.setImage(imageRegister.getImage(IImageConstants.IMAGE_TIME));
        
        dateLabel = new Label(statusbar, SWT.BOLD);
        dateLabel.setLayoutData(new RowData(115, -1));
        dateLabel.setText(" "+sdf.format(new Date())+" ");
        
        new Label(statusbar, SWT.SEPARATOR | SWT.VERTICAL);
        
        textLabel = new Label(statusbar, SWT.BOLD);
        textLabel.setLayoutData(new RowData(300, -1));
	}

	@Override
	public void statusbarUpdate(String text)
	{
		if( text != null && !text.isEmpty() )
		{
			textLabel.setText(text);
			textLabel.setForeground(BLUE);
		}
	}

}
