package controls.viewcontrol;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;

import resources.CommonImageProvider;
import controls.CompositeBuilder;

public abstract class EditableTableControl extends TableControl
{
    private AddEditableObjectAction addEditableObjectAction;
    private DeleteEditableObjectAction deleteEditableObjectAction;
    private Collection inputCollection;
    // private List<EditableObject> editableObjects = new
    // ArrayList<EditableObject>();
    private Label lblMessage;
    private boolean enableActions = true;

    public EditableTableControl(Composite parent, int controlHeight)
    {
        super(parent, controlHeight);
    }

    @Override
    public Composite create()
    {
        Composite main = getParentComposite();
        createActionToolBarComposite(main);

        Composite composite = super.create();
        createMessageComposite(main);

        // Selection Listener to enable delete action
        getViewer().addSelectionChangedListener(getEditableObjectSelectionListener());

        // Filter to hide deleted objects
        getViewer().addFilter(new DeletedObjectFilter());
        return composite;
    }

    public void createMessageComposite(Composite parent)
    {
        Composite cmpMessage = CompositeBuilder.newBuilder(parent).toolBarLayout().build();
        lblMessage = new Label(cmpMessage, SWT.LEFT);
        lblMessage.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        lblMessage.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_RED));
    }

    /**
     * This method will provide default tool bar composite which contribute add and delete action with default behavior
     * 
     * @param parent
     */
    public void createActionToolBarComposite(Composite parent)
    {
        Composite comToolBar = CompositeBuilder.newBuilder(parent).toolBarLayout().build();
        ToolBarManager toolBarManager = new ToolBarManager(SWT.FLAT);

        addEditableObjectAction = new AddEditableObjectAction();
        toolBarManager.add(addEditableObjectAction);
        addEditableObjectAction.setEnabled(enableActions);

        deleteEditableObjectAction = new DeleteEditableObjectAction();
        toolBarManager.add(deleteEditableObjectAction);
        deleteEditableObjectAction.setEnabled(false);

        toolBarManager.createControl(comToolBar);

    }

    private ISelectionChangedListener getEditableObjectSelectionListener()
    {
        return new ISelectionChangedListener()
        {

            @Override
            public void selectionChanged(SelectionChangedEvent event)
            {
                if (deleteEditableObjectAction != null)
                {
                    deleteEditableObjectAction.setEnabled(enableActions);
                }
                refreshCompletnessErrorMessage();
            }
        };
    }

    public void refreshCompletnessErrorMessage()
    {
        if (lblMessage != null)
        {
            if (getSelections().size() > 0)
            {
                EditableObject editableObject = getSelections().get(0);
                if (!editableObject.isComplete())
                {
                    lblMessage.setText(editableObject.getCompletenessError());
                }
                else
                {
                    lblMessage.setText("");
                }
            }
            else
            {
                lblMessage.setText("");
            }
        }
    }

    /**
     * This method will returns current selections of editable objects
     * 
     * @return
     */
    public List<EditableObject> getSelections()
    {
        List<EditableObject> editableObjects = new ArrayList<EditableObject>();
        StructuredSelection selection = (StructuredSelection) getViewer().getSelection();
        for (Iterator iterator = selection.iterator(); iterator.hasNext();)
        {
            Object object = (Object) iterator.next();
            if (object instanceof EditableObject)
            {
                editableObjects.add((EditableObject) object);
            }
        }
        return editableObjects;
    }

    /**
     * Local Add Action to add editable object in viewer
     */
    private class AddEditableObjectAction extends Action
    {

        private static final String ERR_INCOMPLETE_ENTRY = "Incomplete Entry";

        public AddEditableObjectAction()
        {
            setImageDescriptor(CommonImageProvider.getImageDescriptor(CommonImageProvider.ICON_ADD));
        }

        @Override
        public void run()
        {
            if (!isComplete())
            {
                Display display = Display.getCurrent() != null ? Display.getCurrent() : Display.getDefault();
                MessageDialog.openError(display.getActiveShell(), ERR_INCOMPLETE_ENTRY,
                        "Please complete already added entry!");
                return;
            }

            EditableObject editableObject = addEditableObject();
            if (editableObject != null)
            {
                inputCollection.add(editableObject);
                getViewer().setInput(inputCollection);
                getViewer().setSelection(new StructuredSelection(editableObject), true);
            }
        }
    }

    /**
     * Check completeness of editable objects
     * 
     * @return
     */
    public boolean isComplete()
    {
        for (Object object : inputCollection)
        {
            EditableObject editableObject = (EditableObject) object;
            if (!editableObject.isComplete() && !editableObject.isMarkForDeletion())
            {
                return false;
            }
        }
        return true;
    }

    /**
     * abstract method need to be implemented to construct editbale object for addition
     * 
     * @return
     */
    public abstract EditableObject addEditableObject();

    /**
     * Local delete action to delete editable object from viewer
     * 
     */
    private class DeleteEditableObjectAction extends Action
    {

        public DeleteEditableObjectAction()
        {
            setImageDescriptor(CommonImageProvider.getImageDescriptor(CommonImageProvider.ICON_DELETE));
        }

        @Override
        public void run()
        {
            performDelete();
        }

    }

    /**
     * Public method to perform default delete operations which can be override by sub-class
     */
    public void performDelete()
    {
        for (EditableObject editableObject : getSelections())
        {
            if (editableObject.isNew())
            {
                inputCollection.remove(editableObject);
            }
            else
            {
                editableObject.markForDeletion();
            }
        }
        getViewer().setInput(inputCollection);
        deleteEditableObjectAction.setEnabled(false);
        refreshCompletnessErrorMessage();
    }

    @Override
    public void setInput(Object input)
    {
        inputCollection = (Collection) input;
        super.setInput(inputCollection);
    }

    /**
     * Filter to hide deleted objects
     */
    private class DeletedObjectFilter extends ViewerFilter
    {

        @Override
        public boolean select(Viewer viewer, Object parentElement, Object element)
        {
            if (element instanceof EditableObject)
            {
                return !((EditableObject) element).isMarkForDeletion();
            }
            return false;
        }
    }

    public void setEnableActions(boolean enableActions)
    {
        this.enableActions = enableActions;
        if (addEditableObjectAction != null)
        {
            addEditableObjectAction.setEnabled(enableActions);
        }
    }

    public void setDeleteActionState(boolean status)
    {
        if (deleteEditableObjectAction != null)
            deleteEditableObjectAction.setEnabled(status);
    }

    public void setAddActionStatus(boolean status)
    {
        if (addEditableObjectAction != null)
            addEditableObjectAction.setEnabled(status);
    }

}
