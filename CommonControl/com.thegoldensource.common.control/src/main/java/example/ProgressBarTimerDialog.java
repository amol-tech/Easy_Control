package example;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import controls.CompositeBuilder;
import controls.ProgressBarControl;
import model.ProgressMonitor;

public class ProgressBarTimerDialog extends Dialog
{
    private ProgressMonitor progressMonitor;
    private ProgressBarControl progressBarControl;
    private Button btnExcTask;

    protected ProgressBarTimerDialog(Shell parentShell)
    {
        super(parentShell);
    }

    @Override
    protected Control createDialogArea(Composite parent)
    {
        getShell().setText("Progress Bar Timer Dialog");
        Composite cmpProgress = CompositeBuilder.newBuilder(parent).build();
        GridData gridData = new GridData(GridData.FILL_VERTICAL);
        gridData.widthHint = 400;
        cmpProgress.setLayoutData(gridData);

        Composite cmpButton = CompositeBuilder.newBuilder(cmpProgress).thinLayout().layout(2, false).build();
        btnExcTask = new Button(cmpButton, SWT.PUSH);
        btnExcTask.setText("Execute Task");
        btnExcTask.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        btnExcTask.addSelectionListener(getExecuteTaskSelecttionListener());

        Button btnIntruptTask = new Button(cmpButton, SWT.PUSH);
        btnIntruptTask.setText("Intrupt Task");
        btnIntruptTask.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        btnIntruptTask.addSelectionListener(getIntruptTaskSelectionListener());

        progressBarControl = new ProgressBarControl(cmpProgress);
        progressBarControl.create();
        progressBarControl.addStateModifyListener(new ModifyListener()
        {

            @Override
            public void modifyText(ModifyEvent e)
            {
                getShell().setText("Progress Bar Timer Dialog - ****");
                System.out.println("Triggered ...");

            }
        });

        Text txtDesc = new Text(cmpProgress, SWT.BORDER);
        txtDesc.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        return cmpProgress;
    }

    private SelectionListener getIntruptTaskSelectionListener()
    {
        return new SelectionListener()
        {

            @Override
            public void widgetSelected(SelectionEvent e)
            {
                progressMonitor.setInterrupted(true);
            }

            @Override
            public void widgetDefaultSelected(SelectionEvent e)
            {
            }
        };
    }

    private SelectionListener getExecuteTaskSelecttionListener()
    {
        return new SelectionListener()
        {

            @Override
            public void widgetSelected(SelectionEvent event)
            {
                progressMonitor = progressBarControl.startProgressMonitorThread(20);

                Thread taskExecutorThread = new Thread(new DemoTaskExecutor(progressMonitor),
                        "Demo Task Executor Thread");
                taskExecutorThread.start();
                // btnExcTask.setEnabled(false);

            }

            @Override
            public void widgetDefaultSelected(SelectionEvent event)
            {

            }
        };
    }

    @Override
    public boolean close()
    {
        if (progressMonitor == null || progressMonitor.isComplete() || progressMonitor.isInterrupted())
        {
            super.close();
        }
        return false;
    }
}
