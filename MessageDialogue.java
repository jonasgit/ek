import javax.swing.Box;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.border.*;
import java.awt.Dimension;

/**
 * Dialog to show a large but simple message.
 * 
 * @author Jonas Svensson
 */
final class MessageDialogue extends JDialog implements ActionListener {
    //TODO: submit to resource bundle for internationalization
    private static final long serialVersionUID = 1L;
    private JPanel mainPanel = null;
    private JButton okb;
    private JTextArea messageField;

    /**
     * AddPlaceDialogue constructor comment.
     * 
     * @param msg Message to be displayed
     */
    MessageDialogue(final String msg) {
        super();
        setResizable(true);
        setModal(true);

        initialize(msg);
    }

    /**
     * deal with all the buttons that may be pressed.
     * 
     * @param e java.awt.event.ActionEvent
     */
    public void actionPerformed(final ActionEvent e) {
	
	if (e.getSource().equals(okb)) {
            /* handle ok-button */
            dispose();
        } else {
	    System.out.println("Unknown event: "+e);
	}
    }
    
    /**
     * set up the layout and look of the dialog
     * but don't do any data yet.
     */
    private void initialize(String msg) {
        setTitle("MessageDialogue");

        mainPanel = new JPanel();
        final Box centerPanel = new Box(BoxLayout.Y_AXIS);
        final JPanel southPanel = new JPanel();

        /* BUILD CENTERPANEL */

        /* Namn */
        messageField = new JTextArea(msg);
	//messageField.setMinimumSize(new Dimension(250,100));
	messageField.setPreferredSize(new Dimension (300, 200));
	messageField.setLineWrap(true);
	messageField.setWrapStyleWord(true);
	JScrollPane scrollPane = new JScrollPane(messageField);
	scrollPane.setBorder(new EtchedBorder());

        Box jp = new Box(BoxLayout.Y_AXIS);
        jp.add(scrollPane);
	//jp.setBorder(new EtchedBorder());

        centerPanel.add(jp);
	//centerPanel.setBorder(new EtchedBorder());
        /* END KOMMENTAR */

        /*  BUILD SOUTHPANEL */
        okb = new JButton("OK");
        okb.addActionListener(this);
        southPanel.add(okb);

        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(centerPanel, "Center");
        mainPanel.add(southPanel, "South");

        this.getContentPane().add(mainPanel);
        setSize(500, 450);
	pack();
    }
}
