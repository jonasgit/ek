import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * AddObjectsView, add a buy-transaction.
 */
final class AddObjectsView extends JPanel implements ActionListener {
    private static final long serialVersionUID = 1L;
    private final JButton buyb;
    private final JButton placeb;
    private final JButton accb;
    private final JButton catb;
    private final AddPersonDialogue addpersd;
    private final AddPlaceDialogue addplaced;
    private final AddAccountDialogue addaccd;
    private final AddCategoryDialogue addcatd;
    
    AddObjectsView(final WorkingStorage _ws) {
	addpersd = new AddPersonDialogue(_ws);
	addplaced = new AddPlaceDialogue(_ws);
	addaccd = new AddAccountDialogue(_ws);
	addcatd = new AddCategoryDialogue(_ws);
	
	setLayout(new BorderLayout());

	final JPanel jp = new JPanel();

	buyb = new JButton("Press to add Person");
	buyb.addActionListener(this);
	jp.add(buyb);

	placeb = new JButton("Press to add Plats");
	placeb.addActionListener(this);
	jp.add(placeb);

	accb = new JButton("Press to add konto");
	accb.addActionListener(this);
	jp.add(accb);

	catb = new JButton("Press to add Vad");
	catb.addActionListener(this);
	jp.add(catb);

	add(jp, BorderLayout.SOUTH);
    }

    public void actionPerformed(final ActionEvent e) {
	if(e.getSource().equals(buyb)) {
	    addpersd.setVisible(true);
	} else if(e.getSource().equals(placeb)) {
	    addplaced.setVisible(true);
	} else if(e.getSource().equals(accb)) {
	    addaccd.setVisible(true);
	} else if(e.getSource().equals(catb)) {
	    addcatd.setVisible(true);
	} else {
	    System.out.println("Unhandled item: "+e);
	}
    }
}
