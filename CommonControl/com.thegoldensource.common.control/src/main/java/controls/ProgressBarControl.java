package controls;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Text;

import model.ProgressMonitor;

public class ProgressBarControl implements IControl
{
    private ProgressBar progressBar;
    private Composite parent;
    private Label lblMessage;
    private Text txtForListener;

    public ProgressBarControl(Composite parent)
    {
        super();
        this.parent = parent;
    }

    @Override
    public Composite create()
    {
        Composite cmpProgressBar = CompositeBuilder.newBuilder(parent).thinLayout().build();
        progressBar = new ProgressBar(cmpProgressBar, SWT.NULL);
        progressBar.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        Composite cmpMessage = CompositeBuilder.newBuilder(cmpProgressBar).layout(2, false).thinLayout().build();
        lblMessage = new Label(cmpMessage, SWT.LEFT);
        lblMessage.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        txtForListener = new Text(cmpMessage, SWT.BORDER);
        txtForListener.setVisible(false);
        return cmpProgressBar;
    }

    public ProgressMonitor startProgressMonitorThread(int maxCount)
    {
        ProgressMonitor progressMonitor = new ProgressMonitor();
        progressBar.setState(SWT.NONE);
        progressBar.setMaximum(maxCount);
        ProgressBarTimer progressBarTimer = new ProgressBarTimer(progressBar, lblMessage, txtForListener,
                progressMonitor, 500);
        Thread progressBarTimerThread = new Thread(progressBarTimer);
        Display.getDefault().timerExec(progressBarTimer.getTimerValue(), progressBarTimerThread);
        return progressMonitor;
    }

    public void addStateModifyListener(ModifyListener modifyListener)
    {
        txtForListener.addModifyListener(modifyListener);
    }

    public void reset()
    {
        lblMessage.setText("");
        progressBar.setSelection(0);
    }
}
