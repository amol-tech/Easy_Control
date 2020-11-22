package controls;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Text;

import model.ProgressMonitor;

public class ProgressBarTimer implements Runnable
{
    private Label lblMessage;
    private Text txtListner;
    private ProgressBar progressBar;
    private ProgressMonitor progressMonitor;
    private int timerValue;

    public ProgressBarTimer(ProgressBar progressBar, Label lblMessage, Text txtListner, ProgressMonitor progressMonitor,
            int timerValue)
    {
        this.progressBar = progressBar;
        this.lblMessage = lblMessage;
        this.txtListner = txtListner;
        this.progressMonitor = progressMonitor;
        this.timerValue = timerValue;
    }

    @Override
    public void run()
    {
        progressBar.setSelection(progressMonitor.getProgressCount());
        lblMessage.setText(progressMonitor.getMessage());

        if (progressMonitor.isInterrupted())
        {
            progressBar.setState(SWT.ERROR);
            txtListner.setText("interrupted");
        }
        else if (progressMonitor.isEventRaised())
        {
            progressMonitor.setEventRaised(false);
            txtListner.setText("event raised");
        }
        if (!progressMonitor.isComplete() && !progressMonitor.isInterrupted())
        {
            Display.getDefault().timerExec(timerValue, this);
            // System.out.println("Progress Bar Timer Executed ...");
        }

    }

    public int getTimerValue()
    {
        return timerValue;
    }

    public void setTimerValue(int timerValue)
    {
        this.timerValue = timerValue;
    }

    public static void execute(ProgressBarTimer progressBarTimer)
    {
        Thread progressBarTimerThread = new Thread(progressBarTimer);
        Display.getDefault().timerExec(progressBarTimer.getTimerValue(), progressBarTimerThread);
    }

}
