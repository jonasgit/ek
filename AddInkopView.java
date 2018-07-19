import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * AddInkopView, add a buy-transaction.
 */
final class AddInkopView extends JPanel implements ActionListener {
    private static final long serialVersionUID = 1L;
    private final JButton buyb;
    private final JButton transb;
    private final JButton incomeb;
    private final AddInkopDialogue addinkd;
    private final AddTransferDialogue addtd;
    private final AddIncomeDialogue addid;
    
    AddInkopView(final WorkingStorage _ws) {
	addinkd = new AddInkopDialogue(_ws);
	addtd = new AddTransferDialogue(_ws);
	addid = new AddIncomeDialogue(_ws);
	
	setLayout(new BorderLayout());

	final JPanel jp = new JPanel();

	buyb = new JButton("Press to add Inköp-transaction");
	buyb.addActionListener(this);
	jp.add(buyb);

	transb = new JButton("Press to add Överföring-transaction");
	transb.addActionListener(this);
	jp.add(transb);

	incomeb = new JButton("Press to add Insättning-transaction");
	incomeb.addActionListener(this);
	jp.add(incomeb);

	add(jp, BorderLayout.SOUTH);
    }

    public void actionPerformed(final ActionEvent e) {
	if(e.getSource().equals(buyb)) {
	    addinkd.setVisible(true);
	} else if(e.getSource().equals(transb)) {
	    addtd.setVisible(true);
	} else if(e.getSource().equals(incomeb)) {
	    addid.setVisible(true);
	} else {
	    System.out.println("Unhandled item: "+e);
	}
    }
}
