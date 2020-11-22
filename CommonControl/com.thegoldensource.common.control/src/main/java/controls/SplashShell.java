package controls;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Shell;

public class SplashShell
{
    private Display display;
    private Image imgSplash;
    private int waitTime = 5;

    public SplashShell(Display display, Image imgSplash)
    {
        super();
        this.display = display;
        this.imgSplash = imgSplash;
    }
    
    public SplashShell(Display display, Image imgSplash,int waitTime)
    {
        super();
        this.display = display;
        this.imgSplash = imgSplash;
        this.waitTime = waitTime;
    }

    public void open()
    {
        final Shell splash = new Shell(SWT.ON_TOP);
        final ProgressBar progressBar = new ProgressBar(splash, SWT.NONE);

        // Splash Imaage Label
        Label label = new Label(splash, SWT.NONE);
        label.setImage(imgSplash);
        FormLayout layout = new FormLayout();
        splash.setLayout(layout);
        FormData labelData = new FormData();
        labelData.right = new FormAttachment(100, 0);
        labelData.bottom = new FormAttachment(100, 0);
        label.setLayoutData(labelData);

        // Splash Progress Bar
        progressBar.setMaximum(500);
        FormData progressData = new FormData();
        progressData.left = new FormAttachment(0, 0);
        progressData.right = new FormAttachment(100, 0);
        progressData.bottom = new FormAttachment(100, 0);
        progressBar.setLayoutData(progressData);
        splash.pack();

        // Set the splash at the center of the screen
        Rectangle splashRect = splash.getBounds();
        Rectangle displayRect = display.getBounds();
        int x = (displayRect.width - splashRect.width) / 2;
        int y = (displayRect.height - splashRect.height) / 2;
        splash.setLocation(x, y);
        splash.open();

        display.asyncExec(new Runnable()
        {
            public void run()
            {
                for (int i = 0; i < 500; i++)
                {
                    progressBar.setSelection(i + 1);
                    try
                    {
                        Thread.sleep(waitTime);
                    }
                    catch (Throwable e)
                    {
                    }
                }
                splash.close();
                imgSplash.dispose();
            }
        });

        while (!splash.isDisposed())
        {
            if (!display.readAndDispatch())
                display.sleep();
        }
    }
}
