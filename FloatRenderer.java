import javax.swing.table.DefaultTableCellRenderer;

class FloatRenderer extends DefaultTableCellRenderer {
    private static final long serialVersionUID = 1L;
    //public DateRenderer() { super(); }
    
    public void setValue(Object value) {
	try {
	    Double Dvalue = (Double)value;
	    setText(String.format("%,.2f", Dvalue));
	} catch(ClassCastException e) {
	    setText((value == null) ? "" : (String)value);
	}
    }
}
