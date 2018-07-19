import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;
import javax.swing.table.TableColumn;

/**
 * ResultView show resultat.
 */
final class ResultView extends JPanel implements ActionListener {
    private static final long serialVersionUID = 1L;
    private final JComboBox yearsList;
    private final WorkingStorage ws;
    private JTable table;
    private TableModel dataModel;
    private JScrollPane scrollPane;
    private JButton helpb;
    private JButton printb;
    
    ResultView(final WorkingStorage _ws) {
	ws = _ws;
	
	setLayout(new BorderLayout());

	dataModel = new AbstractTableModel() {
		private static final long serialVersionUID = 1L;
		final ArrayList2D tableData = new ArrayList2D();
		public int getColumnCount() { return tableData.getSizeX(); }
		public int getRowCount() { return tableData.getSizeY(); }
		public boolean isCellEditable(final int row, final int col) {
		    return false;
		}
		public Object getValueAt(final int row, final int col) {
		    return tableData.get(col, row);
		}
		//public void setValueAt(Object value, int row, int col) {
		//  rowData[row][col] = value;
		//  fireTableCellUpdated(row, col);
		//}
	    };
	table = new JTable(dataModel);
	scrollPane = new JScrollPane(table);
	add(scrollPane, BorderLayout.CENTER);
 
	final JPanel jp = new JPanel();
	//jp.setLayout use default flowlayout

	final JLabel yearsLabel = new JLabel("Resultatår ");
	final Calendar rightNow = Calendar.getInstance();
	final int CurrentYear = rightNow.get(Calendar.YEAR);
	final String[] yearsStrings = new String[10];
	for(int i=0; i<10; i++) {
	    yearsStrings[i] = java.lang.Integer.toString(CurrentYear - i);
	}
	yearsList = new JComboBox(yearsStrings);
	yearsList.addActionListener(this);
	jp.add(yearsLabel);
	jp.add(yearsList);

        printb = new JButton("Print");
        printb.addActionListener(this);
        jp.add(printb);
	
        helpb = new JButton("Help");
        helpb.addActionListener(this);
        jp.add(helpb);
	
	add(jp, BorderLayout.SOUTH);
    }

    public void actionPerformed(final ActionEvent e) {
	if (e.getSource().equals(printb)) {
	    try {
		if (! table.print()) {
		    System.err.println("User cancelled printing");
		}
	    } catch (java.awt.print.PrinterException pe) {
		System.err.format("Cannot print %s%n", pe.getMessage());
	    }
	} else if (e.getSource().equals(helpb)) {
	    MessageDialogue helpd = new MessageDialogue("ResultView\nShows the result.");
	    helpd.setVisible(true);
	} else if(e.getSource().equals(yearsList)) {
	    // Note: This method is called even if the same item is selected
	    // again in the combobox
	    
	    remove(scrollPane);
	    
	    dataModel = new AbstractTableModel() {
		    private static final long serialVersionUID = 1L;
		    final String[] columnNames = { "Vad",
						   "Årstotal",
						   "Genomsnitt",
						   "Jan",
						   "Feb",
						   "Mar",
						   "Apr",
						   "Maj",
						   "Jun",
						   "Jul",
						   "Aug",
						   "Sep",
						   "Okt",
						   "Nov",
						   "Dec" };
		    final ArrayList2D tableData =
			ws.dumpResult(
				      Integer.parseInt((String)yearsList.getSelectedItem()));
		    public int getColumnCount() { return tableData.getSizeX(); }
		    public int getRowCount() { return tableData.getSizeY(); }
		    public boolean isCellEditable(final int row, final int col) {
			return false;
		    }
		    public Object getValueAt(final int row, final int col) {
			return tableData.get(col, row);
		    }
		    public String getColumnName(final int col) {
			return columnNames[col];
		    }
		};
	    table = new JTable(dataModel);
	    for (int vColIndex=1; vColIndex<15; vColIndex++) {
		TableColumn col = table.getColumnModel().getColumn(vColIndex);
		col.setCellRenderer(new FloatRenderer());
	    }
	    scrollPane = new JScrollPane(table);
	    add(scrollPane, BorderLayout.CENTER);
	    revalidate();
	} else {
	    System.out.println("Unhandled item: "+e);
	}
    }
}
