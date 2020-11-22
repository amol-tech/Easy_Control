package example;

import model.ProgressMonitor;

public class DemoTaskExecutor implements Runnable
{
    private ProgressMonitor progressMonitor;

    public DemoTaskExecutor(ProgressMonitor progressMonitor)
    {
        this.progressMonitor = progressMonitor;
    }

    @Override
    public void run()
    {
        for (int i = 0; i < 20; i++)
        {
            try
            {
                progressMonitor.addProgressCount(1);
                progressMonitor.setMessage("Executing Task : " + i);
                Thread.sleep(2000);
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }            
        }
        progressMonitor.setComplete(true);
        progressMonitor.setMessage("Task Completed");
    }
}
