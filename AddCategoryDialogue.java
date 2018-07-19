import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * dialog to add Category (vad dvs lön, bostadsbidrag etc).
 * @author Jonas Svensson
 */
final class AddCategoryDialogue extends JDialog implements ActionListener {
    //TODO: submit to resource bundle for internationalization
    private static final long serialVersionUID = 1L;
    private JPanel mainPanel = null;
    private final WorkingStorage ws;
    private JButton okb;
    private JButton cancelb;
    private JComboBox typeList;
    private JTextField namnField;
    
    /**
     * AddCategoryDialogue constructor comment.
     *
     * @param _ws WS
     */
    AddCategoryDialogue(final WorkingStorage _ws) {
	super();
	ws = _ws;
	setResizable(false);
	setModal(true);
	
	initialize();
    }
    /**
     * deal with all the buttons that may be pressed.
     * @param e java.awt.event.ActionEvent
     */
    public void actionPerformed(final ActionEvent e) {

        if (e.getSource().equals(cancelb)){
	    /* handle cancel-button */
	    dispose();
	} else if(e.getSource().equals(okb)){
	    /* handle ok-button */
	    //	    System.out.println("OK-button pressed. Will add category...");
	    final String type = "" + typeList.getSelectedItem();
	    // System.out.println("Type: "+type);
	    // System.out.println("Namn: "+namnField.getText());
	    ws.addCategory(namnField.getText(),
			      type);
	    dispose();
	}
    }
    
    /**
     * set up the layout and look of the dialog
     * but don't do any data yet.
     */
    private void initialize() {
	setTitle("AddCategoryDialogue");
	mainPanel = new JPanel();
	final JPanel northPanel = new JPanel();
	final JPanel centerPanel = new JPanel();
	final JPanel southPanel = new JPanel();

	/* BUILD CENTERPANEL */
	centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));

	/* Type-dropdown */
	final JLabel nameLabel = new JLabel(" Typ ");
	final String[] typeStrings = {"Inkomst", "Utgift"};
	
	typeList = new JComboBox(typeStrings);
	typeList.addActionListener(this);
	JPanel jp = new JPanel();
	jp.add(nameLabel);
	jp.add(typeList);
	centerPanel.add(jp);

	/* KOMMENTAR */
	final JLabel jff = new JLabel("Namn");

	namnField = new JTextField(40);
	
	jp = new JPanel();
	jp.add(jff);
	jp.add(namnField);
	centerPanel.add(jp);
	/* END KOMMENTAR */

	/*  BUILD SOUTHPANEL */
	okb = new JButton("OK");
	okb.addActionListener(this);
	southPanel.add(okb);

	cancelb = new JButton("Cancel");
	cancelb.addActionListener(this);
	southPanel.add(cancelb);
	
	mainPanel.setLayout(new BorderLayout());
	mainPanel.add(northPanel, "North");
	mainPanel.add(centerPanel,"Center");
	mainPanel.add(southPanel, "South");
	
	this.getContentPane().add(mainPanel);
	setSize(500,450);
    }
}
