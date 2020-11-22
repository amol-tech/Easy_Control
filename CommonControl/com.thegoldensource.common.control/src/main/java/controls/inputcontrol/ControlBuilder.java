package controls.inputcontrol;

import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;

public class ControlBuilder
{
    private Composite parent;
    private int style = SWT.NONE;
    private String label = "";
    private boolean required;
    private Object defaultValue;
    private GridData gridData = new GridData(GridData.FILL_HORIZONTAL);

    private ControlBuilder(Composite parent)
    {
        super();
        this.parent = parent;
    }

    public ControlBuilder style(int style)
    {
        this.style = style;
        return this;
    }

    public ControlBuilder required()
    {
        this.required = true;
        return this;
    }

    public ControlBuilder label(String label)
    {
        this.label = label;
        return this;
    }

    public ControlBuilder defaultValue(Object defaultValue)
    {
        this.defaultValue = defaultValue;
        return this;
    }

    public ControlBuilder gridData(GridData gridData)
    {
        this.gridData = gridData;
        return this;
    }

    public TextInputControl buildTextControl()
    {
        TextInputControl textInputControl = new TextInputControl(parent);
        style = style | SWT.BORDER;
        configureControl(textInputControl);
        return textInputControl;
    }

    public TextInputControl buildDecimalTextControl(boolean allowNegative)
    {
        DecimalTextInputControl textInputControl = new DecimalTextInputControl(parent);
        style = style | SWT.BORDER | SWT.RIGHT;
        defaultValue = defaultValue != null ? defaultValue : "0.00";
        textInputControl.setAllowNegative(allowNegative);
        configureControl(textInputControl);
        return textInputControl;
    }

    public TextInputControl buildIntegerTextControl(boolean allowNegative)
    {
        IntegerTextInputControl textInputControl = new IntegerTextInputControl(parent);
        style = style | SWT.BORDER | SWT.RIGHT;
        defaultValue = defaultValue != null ? defaultValue : "0";
        textInputControl.setAllowNegative(allowNegative);
        configureControl(textInputControl);
        return textInputControl;
    }

    public ComboInputControl buildComboControl(Map<Object, String> sourceMap)
    {
        ComboInputControl comboInputControl = new ComboInputControl(parent);
        style = style | SWT.READ_ONLY;
        comboInputControl.setSourceMap(sourceMap);
        configureControl(comboInputControl);
        return comboInputControl;
    }

    public ComboInputControl buildComboControl(String[] source)
    {
        ComboInputControl comboInputControl = new ComboInputControl(parent);
        style = style | SWT.READ_ONLY;
        comboInputControl.setSource(source);
        configureControl(comboInputControl);
        return comboInputControl;
    }

    public CheckBoxInputControl buildCheckBoxControl()
    {
        CheckBoxInputControl checkBoxInputControl = new CheckBoxInputControl(parent);
        style = style | SWT.CHECK;
        configureControl(checkBoxInputControl);
        return checkBoxInputControl;
    }

    public ConnectionInputControl buildConnectionInputControl()
    {
        ConnectionInputControl connectionInputControl = new ConnectionInputControl(parent);
        style = style | SWT.BORDER;
        configureControl(connectionInputControl);
        return connectionInputControl;
    }

    public FileDialogInputControl buildFileDialogInputControl(String[] filterExtensions)
    {
        FileDialogInputControl fileDialogInputControl = new FileDialogInputControl(parent);
        style = style | SWT.BORDER;
        fileDialogInputControl.setFilterExtension(filterExtensions);
        configureControl(fileDialogInputControl);
        return fileDialogInputControl;
    }

    public FileDialogInputControl buildFileDialogInputControl()
    {
        return buildFileDialogInputControl(null);
    }

    public FolderDialogInputControl buildFolderDialogInputControl()
    {
        FolderDialogInputControl folderDialogInputControl = new FolderDialogInputControl(parent);
        style = style | SWT.BORDER;
        configureControl(folderDialogInputControl);
        return folderDialogInputControl;
    }

    public void configureControl(IInputControl inputControl)
    {
        inputControl.setLabel(label);
        inputControl.setGridData(gridData);
        inputControl.setStyle(style);
        inputControl.setRequired(required);
        inputControl.setDefaultValue(defaultValue);
        inputControl.createControl();
    }

    public static ControlBuilder newContolBuilder(Composite parent)
    {
        return new ControlBuilder(parent);
    }

    public Composite getParent()
    {
        return parent;
    }
}