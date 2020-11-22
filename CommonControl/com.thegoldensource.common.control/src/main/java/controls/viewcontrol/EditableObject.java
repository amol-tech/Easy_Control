package controls.viewcontrol;

public interface EditableObject
{
    /**
     * This method will return true if object is marked for deletion to filter it in Editable Control
     * @return
     */
    public boolean isMarkForDeletion();

    /**
     * This method will mark the object for deletion
     */
    public void markForDeletion();

    /**
     * This method will return true if the editable object is newly created and not persisted in persistance layer.
     * @return
     */
    public boolean isNew();

    /**
     * This method will return true if editbale object is complete for persist
     * @return
     */
    public boolean isComplete();
    
    /**
     * This method will should return a string message for completeness check which will be display in footer 
     * @return
     */
    public String getCompletenessError();

}
