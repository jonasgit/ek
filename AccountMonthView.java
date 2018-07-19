import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

/**
 * AccountMonthView show balance every day for a specific account.
 */
final class AccountMonthView extends JPanel implements ActionListener {
    private static final long serialVersionUID = 1L;
    private final JComboBox accountList;
    private final JComboBox monthsList;
    private JButton helpb;
    private final JComboBox yearsList;
    private final WorkingStorage ws;
    private JTable table;
    private TableModel dataModel;
    private JScrollPane scrollPane;
    private final GregorianCalendar monthDate;
    private String AccountName;
    
    AccountMonthView(final WorkingStorage _ws) {
	ws = _ws;

	monthDate = new GregorianCalendar();
	monthDate.setTime (new Date());	/* Initialize to todays date */

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

	final JLabel kontoLabel = new JLabel("Konto ");
	final ArrayList accountNames = ws.getAccountNames();
	final String[] accountStrings = new String[accountNames.size()];
	
	int i=0;
	while (i < accountNames.size()) {
	    accountStrings[i] = (String)accountNames.get(i);
	    i++;
	}

	if(accountNames.size() > 0) {
	    AccountName = accountStrings[0];
	} else {
	    AccountName = "Inga konton";
	}
	
	accountList = new JComboBox(accountStrings);
	accountList.addActionListener(this);
	jp.add(kontoLabel);
	jp.add(accountList);
	
	final JLabel monthsLabel = new JLabel("Månad");
	final String[] monthsStrings = {"Januari", "Februari", "Mars", "April", "Maj", "Juni", "Juli", "Augusti", "September", "Oktober", "November", "December"};
	monthsList = new JComboBox(monthsStrings);
	monthsList.addActionListener(this);
	jp.add(monthsLabel);
	jp.add(monthsList);

	final JLabel yearsLabel = new JLabel("År ");
	final Calendar rightNow = Calendar.getInstance();
	final int CurrentYear = rightNow.get(Calendar.YEAR);
	final String[] yearsStrings = new String[10];
	for(i=0; i<10; i++) {
	    yearsStrings[i] = java.lang.Integer.toString(CurrentYear - i);
	}
	yearsList = new JComboBox(yearsStrings);
	yearsList.addActionListener(this);
	jp.add(yearsLabel);
	jp.add(yearsList);

        helpb = new JButton("Help");
        helpb.addActionListener(this);
        jp.add(helpb);

	add(jp, BorderLayout.SOUTH);
    }

    private void updateView() {
	final ArrayList2D tData;
	tData = ws.dumpAccountBalance(AccountName, monthDate);
	if(tData != null) {
	    remove(scrollPane);
	    
	    dataModel = new AbstractTableModel() {
		    private static final long serialVersionUID = 1L;
		    final String[] columnNames = { "Datum",
						   "Saldo" };
		    final ArrayList2D tableData = tData;
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
	    scrollPane = new JScrollPane(table);
	    add(scrollPane, BorderLayout.CENTER);
	    revalidate();
	}
    }
    
    public void actionPerformed(final ActionEvent e) {
	if (e.getSource().equals(helpb)) {
	    MessageDialogue helpd = new MessageDialogue("AccountMonthView\nThis view will produce an account statement with no transactions. Only the balance at the end of each day will be shown. Press buttons to select month and account.");
	    helpd.setVisible(true);
	} else if(e.getSource().equals(monthsList)) {
	    //month = (String)monthsList.getSelectedItem();
	    if( monthsList.getSelectedItem().equals("Januari")) {
		monthDate.set(Calendar.MONTH, Calendar.JANUARY);
	    } else if( monthsList.getSelectedItem().equals("Februari")) {
		monthDate.set(Calendar.MONTH, Calendar.FEBRUARY);
	    } else if( monthsList.getSelectedItem().equals("Mars")) {
		monthDate.set(Calendar.MONTH, Calendar.MARCH);
	    } else if( monthsList.getSelectedItem().equals("April")) {
		monthDate.set(Calendar.MONTH, Calendar.APRIL);
	    } else if( monthsList.getSelectedItem().equals("Maj")) {
		monthDate.set(Calendar.MONTH, Calendar.MAY);
	    } else if( monthsList.getSelectedItem().equals("Juni")) {
		monthDate.set(Calendar.MONTH, Calendar.JUNE);
	    } else if( monthsList.getSelectedItem().equals("Juli")) {
		monthDate.set(Calendar.MONTH, Calendar.JULY);
	    } else if( monthsList.getSelectedItem().equals("Augusti")) {
		monthDate.set(Calendar.MONTH, Calendar.AUGUST);
	    } else if( monthsList.getSelectedItem().equals("September")) {
		monthDate.set(Calendar.MONTH, Calendar.SEPTEMBER);
	    } else if( monthsList.getSelectedItem().equals("Oktober")) {
		monthDate.set(Calendar.MONTH, Calendar.OCTOBER);
	    } else if( monthsList.getSelectedItem().equals("November")) {
		monthDate.set(Calendar.MONTH, Calendar.NOVEMBER);
	    } else if( monthsList.getSelectedItem().equals("December")) {
		monthDate.set(Calendar.MONTH, Calendar.DECEMBER);
	    }
	    updateView();
	} else if(e.getSource().equals(yearsList)) {
	    String year = (String)yearsList.getSelectedItem();
	    monthDate.set(Calendar.YEAR, Integer.parseInt(year));
	    updateView();
	} else if(e.getSource().equals(accountList)) {
	    // Note: This method is called even if the same item is selected
	    // again in the combobox
	    AccountName = (String)accountList.getSelectedItem();
	    updateView();
	} else {
	    System.out.println("Unhandled item: "+e);
	}
    }
}
