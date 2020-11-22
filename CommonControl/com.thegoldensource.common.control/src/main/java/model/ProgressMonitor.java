package model;

public class ProgressMonitor
{
    private int progressCount = 0;
    private boolean complete = false;
    private boolean interrupted = false;
    private boolean eventRaised = false;
    private String message="";

    public synchronized void addProgressCount(int count)
    {
        progressCount = progressCount + count;
    }

    public synchronized int getProgresscount()
    {
        return progressCount;
    }

    public synchronized boolean isComplete()
    {
        return complete;
    }

    public synchronized void setComplete(boolean complete)
    {
        this.complete = complete;
    }

    public synchronized int getProgressCount()
    {
        return progressCount;
    }

    public synchronized void setProgressCount(int progressCount)
    {
        this.progressCount = progressCount;
    }

    public synchronized boolean isInterrupted()
    {
        return interrupted;
    }

    public synchronized void setInterrupted(boolean interrupted)
    {
        this.interrupted = interrupted;
    }

    public synchronized String getMessage()
    {
        return message;
    }

    public synchronized void setMessage(String message)
    {
        this.message = message;
    }

    public synchronized void setEventRaised(boolean eventRaised)
    {
        this.eventRaised = eventRaised;
    }
    
    public synchronized void raiseEvent()
    {
        this.eventRaised = true;
    }

    public synchronized boolean isEventRaised()
    {
        return eventRaised;
    }
    
}
