import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * TransferView show definitions of automatic transfers.
 */
final class TransferView extends JPanel implements ActionListener {
    private static final long serialVersionUID = 1L;
    private final WorkingStorage ws;
    private final JTable table;
    private final TableModel dataModel;
    private final JScrollPane scrollPane;
    private JButton helpb;
    
    TransferView(final WorkingStorage _ws) {
	ws = _ws;
	
	setLayout(new BorderLayout());
	
	dataModel = new AbstractTableModel() {
		private static final long serialVersionUID = 1L;
		final String[] columnNames = { "FrånKonto",
					       "TillKonto",
					       "Belopp",
					       "Nästa",
					       "Sista",
					       "Hur ofta",
					       "Vad",
					       "Person" };
		final ArrayList2D tableData = ws.dumpTransfers();
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

        helpb = new JButton("Help");
        helpb.addActionListener(this);
	add(helpb, BorderLayout.SOUTH);
    }

    public void actionPerformed(final ActionEvent e) {
	if (e.getSource().equals(helpb)) {
	    MessageDialogue helpd = new MessageDialogue("TransferView\nThis shows repeating events (\"Stående överföringar/betalningar\").");
	    helpd.setVisible(true);
	} else {
	    System.out.println("Unhandled item: "+e);
	}
    }
}
