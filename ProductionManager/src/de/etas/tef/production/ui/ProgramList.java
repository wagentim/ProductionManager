package de.etas.tef.production.ui;

import java.text.SimpleDateFormat;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Monitor;
import org.eclipse.swt.widgets.Shell;

import de.etas.tef.generator.core.Controller;
import de.etas.tef.generator.ui.MainWindow;
import de.etas.tef.prodution.core.CoreService;
import de.wagentim.common.IImageConstants;

public class ProgramList
{
	private static final SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
	private static final int WIDTH = 300;
	private static final int HEIGHT = 450;
	
	public ProgramList()
	{
		Display display = CoreService.getCoreService().getDisplay();
		
		Shell shell = new Shell(display);
		shell.setText("TEF Tools Starter");
		
		shell.addShellListener(new ShellAdapter()
		{

			@Override
			public void shellClosed(ShellEvent arg0)
			{
				System.exit(0);
			}

		});
		
		initMainScreen(shell);
		initTitle(shell);
		initPrograms(shell);
		
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
	
	private void initPrograms(Shell shell)
	{
		Composite composite = new Composite(shell, SWT.NONE);
		composite.setBackground(CoreService.getCoreService().getColorFactory().getColorWhite());
		
		GridLayout layout = new GridLayout(1, false);
		layout.marginHeight = layout.marginWidth = layout.marginTop = layout.marginLeft = layout.marginRight = layout.marginBottom = 0;
		composite.setLayout(layout);
		
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.heightHint = 80;
		composite.setLayoutData(gd);
		
		Button btnXmlEditor = new Button(composite, SWT.PUSH);
		btnXmlEditor.setBackground(CoreService.getCoreService().getColorFactory().getColorWhite());
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.heightHint = 50;
		btnXmlEditor.setLayoutData(gd);
		FontData[] fD = btnXmlEditor.getFont().getFontData();
		fD[0].setHeight(14);
		btnXmlEditor.setFont( new Font(CoreService.getCoreService().getDisplay(),fD[0]));
		btnXmlEditor.setText("TEF XML Editor");
		btnXmlEditor.addSelectionListener(new SelectionListener()
		{
			
			@Override
			public void widgetSelected(SelectionEvent event)
			{
				int currStatus = CoreService.getCoreService().getAppStatusXMLEditor();
				
				if(currStatus == CoreService.APP_STATUS_OFF)
				{
					CoreService.getCoreService().getDisplay().asyncExec(new Runnable()
					{
						
						@Override
						public void run()
						{
							new MainWindow(new Controller(), CoreService.getCoreService().getDisplay());
						}
					});
				}
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent event)
			{
				
			}
		});
	}

	private void initTitle(Shell shell)
	{
		Composite composite = new Composite(shell, SWT.NONE);
		composite.setBackground(CoreService.getCoreService().getColorFactory().getColorWhite());
		
		GridLayout layout = new GridLayout(2, false);
		layout.marginHeight = layout.marginWidth = layout.marginTop = layout.marginLeft = layout.marginRight = layout.marginBottom = 0;
		composite.setLayout(layout);
		
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.heightHint = 80;
		composite.setLayoutData(gd);
		
		Label etasLogo = new Label(composite, SWT.NONE);
		etasLogo.setImage(CoreService.getCoreService().getImageRegister().getImage(IImageConstants.IMAGE_ETAS));
		
//		Label tefVersion = new Label(composite, SWT.NONE);
//		tefVersion.setBackground(CoreService.getCoreService().getColorFactory().getColorWhite());
//		FontData[] fD = tefVersion.getFont().getFontData();
//		fD[0].setHeight(12);
//		tefVersion.setFont( new Font(CoreService.getCoreService().getDisplay(),fD[0]));
//		gd = new GridData(GridData.VERTICAL_ALIGN_BEGINNING);
//		tefVersion.setLayoutData(gd);
//		tefVersion.setText("TEF Tool Kits");
		
	}

	private void initMainScreen(Composite shell)
	{
		Monitor primary = shell.getDisplay().getPrimaryMonitor();
		Rectangle area = primary.getClientArea();
		shell.setBounds((Math.abs(area.width - WIDTH)) / 2, Math.abs((area.height - HEIGHT)) / 2, WIDTH, HEIGHT);

		GridLayout layout = new GridLayout(1, false);
		layout.marginHeight = layout.marginWidth = layout.marginTop = layout.marginLeft = layout.marginRight = layout.marginBottom = 0;
		shell.setLayout(layout);
		shell.setBackground(CoreService.getCoreService().getColorFactory().getColorWhite());
	}
}
