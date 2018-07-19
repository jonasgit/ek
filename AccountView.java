import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.awt.print.Printable;
import java.awt.print.PageFormat;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.text.MessageFormat;
import java.util.Collections;

/**
 * AccountView show transactions for an account.
 */
final class AccountView extends JPanel implements ActionListener {
    private static final long serialVersionUID = 1L;
    private final DateButton jb;
    private final DateButton jb2;
    private JButton printb;
    private JButton helpb;
    private final JComboBox accountList;
    private final WorkingStorage ws;
    private JTable table;
    private TableModel dataModel;
    private JScrollPane scrollPane;

    AccountView(final WorkingStorage _ws) {
        ws = _ws;
	
        setLayout(new BorderLayout());
	
        dataModel = new AbstractTableModel() {
	    private static final long serialVersionUID = 1L;
            final ArrayList2D tableData = new ArrayList2D();

            public int getColumnCount() {
                return tableData.getSizeX();
            }

            public int getRowCount() {
                return tableData.getSizeY();
            }

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
        final ArrayList<String> accountNames = ws.getAccountNames();
	Collections.sort(accountNames);
        final String[] accountStrings = new String[accountNames.size()+1];

	accountStrings[0] = "Välj konto";
        int i = 0;
        while (i < accountNames.size()) {
            accountStrings[i+1] = accountNames.get(i);
            i++;
        }

        accountList = new JComboBox(accountStrings);
        accountList.addActionListener(this);
        jp.add(kontoLabel);
        jp.add(accountList);

        final JLabel jff = new JLabel("  Från ");
        jb = new DateButton("Press to select date");
        jp.add(jff, BorderLayout.CENTER);
        jp.add(jb, BorderLayout.SOUTH);

        final JLabel jff2 = new JLabel("  Till ");
        jb2 = new DateButton("Press to select date");
        jp.add(jff2, BorderLayout.CENTER);
        jp.add(jb2, BorderLayout.SOUTH);

        printb = new JButton("Print");
        printb.addActionListener(this);
        jp.add(printb);
	
        helpb = new JButton("Help");
        helpb.addActionListener(this);
        jp.add(helpb);

        add(jp, BorderLayout.SOUTH);
    }

    public void actionPerformed(final ActionEvent e) {
	if (e.getSource().equals(helpb)) {
	    MessageDialogue helpd = new MessageDialogue("AccountView\nThis view will produce an account statement. Press buttons to select start and/or end date. If you do not select a date then today is assumed. After selecting date, select account.");
	    helpd.setVisible(true);
	} else if (e.getSource().equals(accountList)) {
            // Note: This method is called even if the same item is selected
            // again in the combobox
	    
            final ArrayList2D tData;

	    if ( accountList.getSelectedIndex() > 0 ) { 
		tData = ws.dumpAccount((String) accountList.getSelectedItem(), jb.getDate(), jb2.getDate());
		if (tData != null) {
		    remove(scrollPane);
		    
		    dataModel = new AbstractTableModel() {
			    private static final long serialVersionUID = 1L;
			    final String[] columnNames = {"Från",
							  "Till",
							  "Typ",
							  "Plats",
							  "Datum",
							  "Kategori",
							  "Person",
							  "Summa",
							  "Saldo",
							  "Kommentar"};
			    final ArrayList2D tableData = tData;
			    
			    public int getColumnCount() {
				return tableData.getSizeX();
			    }
			    
			    public int getRowCount() {
				return tableData.getSizeY();
			    }
			    
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
	} else if (e.getSource().equals(printb)) {
	    MessageFormat headerFormat = new MessageFormat("Page {0}");
	    MessageFormat footerFormat = new MessageFormat("- {0} -");
	    try {
		table.print(JTable.PrintMode.FIT_WIDTH, headerFormat, footerFormat);
	    } catch (java.awt.print.PrinterException ex) {
		//		    ex.printStackTrace();
		System.err.println("AccountView printer exception:"+ex);
	    }
        } else {
            System.out.println("Unhandled item: " + e);
        }
    }
}
