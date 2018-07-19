import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.text.FieldPosition;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;


/**
 * DateButton is a button with some date-special handling.
 */
final class DateButton extends JButton implements ActionListener, KeyListener {
    private static final long serialVersionUID = 1L;
    private StringBuffer dateBuffer;
    private final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    private Date startDate = null;
    private final DateChooser mydc = new DateChooser();

    /**
     * Default constructor, sets the default text to the date of today.
     */
    DateButton() {
        super();
        dateBuffer = new StringBuffer();
        final Calendar rightNow;
        rightNow = Calendar.getInstance();
        startDate = rightNow.getTime();
        df.format(startDate, dateBuffer, new FieldPosition(0));
        setText(new String(dateBuffer));
        addActionListener(this);
        addKeyListener(this);
    }

    /**
     * Just an ordinary constructor.
     * 
     * @param s Whatever the string on the button should be
     */
    DateButton(final String s) {
        super(s);
        addActionListener(this);
    }

    private void fireDatechooser() {
                                            mydc.setLocationRelativeTo(this);
        mydc.setVisible(true);
        dateBuffer = new StringBuffer();
        startDate = mydc.getCalendar().getTime();
        df.format(startDate, dateBuffer, new FieldPosition(0));

        setText(new String(dateBuffer));
    }
    /**
     * A function.
     * @param e ActionEvent
     */
    public void actionPerformed(final ActionEvent e) {
        fireDatechooser();
    }

    /**
     * Handle the key typed event from the text field.
     * @param e KeyEvent
     */
    public void keyTyped(final KeyEvent e) {
        // displayInfo(e, "KEY PRESSED: ");
    }

    /**
     * Handle the key pressed event from the text field.
     * @param e KeyEvent
     */
    public void keyPressed(final KeyEvent e) {
        //displayInfo(e, "KEY PRESSED: ");
    }

    /**
     * Handle the key released event from the text field.
     * @param e KeyEvent
     */
    public void keyReleased(final KeyEvent e) {
        //displayInfo(e, "KEY RELEASED: ");
        char c = '-';
        final int id = e.getID();
        if (id == KeyEvent.KEY_TYPED) {
            c = e.getKeyChar();
            //keyString = "key character = '" + c + "'";
        } else {
            final int keyCode = e.getKeyCode();
            /*keyString = "key code = " + keyCode
                        + " ("
                        + KeyEvent.getKeyText(keyCode)
                        + ")";*/
            //    c = KeyEvent.getKeyText(keyCode).charAt(0);
            if (keyCode == KeyEvent.VK_NUMPAD0 || keyCode == KeyEvent.VK_0) {
                c = '0';
            }
            if (keyCode == KeyEvent.VK_NUMPAD1 || keyCode == KeyEvent.VK_1) {
                c = '1';
            }
            if (keyCode == KeyEvent.VK_NUMPAD2 || keyCode == KeyEvent.VK_2) {
                c = '2';
            }
            if (keyCode == KeyEvent.VK_NUMPAD3 || keyCode == KeyEvent.VK_3) {
                c = '3';
            }
            if (keyCode == KeyEvent.VK_NUMPAD4 || keyCode == KeyEvent.VK_4) {
                c = '4';
            }
            if (keyCode == KeyEvent.VK_NUMPAD5 || keyCode == KeyEvent.VK_5) {
                c = '5';
            }
            if (keyCode == KeyEvent.VK_NUMPAD6 || keyCode == KeyEvent.VK_6) {
                c = '6';
            }
            if (keyCode == KeyEvent.VK_NUMPAD7 || keyCode == KeyEvent.VK_7) {
                c = '7';
            }
            if (keyCode == KeyEvent.VK_NUMPAD8 || keyCode == KeyEvent.VK_8) {
                c = '8';
            }
            if (keyCode == KeyEvent.VK_NUMPAD9 || keyCode == KeyEvent.VK_9) {
                c = '9';
            }
            if (keyCode == KeyEvent.VK_ENTER) {
                fireDatechooser();
            }
        }
        if(c != '-') {
            System.out.println("Got key" + c + '\n');
        }
    }

    static void displayInfo(final KeyEvent e, final String s) {
        final String keyString;
        String modString;
        final String tmpString;
        String actionString;
        String locationString;

        //You should only rely on the key char if the event
        //is a key typed event.
        final int id = e.getID();
        if (id == KeyEvent.KEY_TYPED) {
            final char c = e.getKeyChar();
            keyString = "key character = '" + c + '\'';
        } else {
            final int keyCode = e.getKeyCode();
            keyString = "key code = " + keyCode
                    + " ("
                    + KeyEvent.getKeyText(keyCode)
                    + ')';
        }

        final int modifiers = e.getModifiersEx();
        modString = "modifiers = " + modifiers;
        tmpString = KeyEvent.getModifiersExText(modifiers);
        if (tmpString.length() > 0) {
            modString += " (" + tmpString + ')';
        } else {
            modString += " (no modifiers)";
        }

        actionString = "action key? ";
        if (e.isActionKey()) {
            actionString += "YES";
        } else {
            actionString += "NO";
        }

        locationString = "key location: ";
        final int location = e.getKeyLocation();
        if (location == KeyEvent.KEY_LOCATION_STANDARD) {
            locationString += "standard";
        } else if (location == KeyEvent.KEY_LOCATION_LEFT) {
            locationString += "left";
        } else if (location == KeyEvent.KEY_LOCATION_RIGHT) {
            locationString += "right";
        } else if (location == KeyEvent.KEY_LOCATION_NUMPAD) {
            locationString += "numpad";
        } else { // (location == KeyEvent.KEY_LOCATION_UNKNOWN)
            locationString += "unknown";
        }

        System.out.println(s + '\n' +
                "keyString:" + keyString + '\n' +
                "modString:" + modString + '\n' +
                "tmpString:" + tmpString + '\n' +
                "actionString:" + actionString + '\n' +
                "locationString:" + locationString + '\n');
    }

    public Date getDate() {
        final String str = getText();
        Date ret = null;
        try {
            ret = df.parse(str);
        } catch (ParseException e) {
            ret = new Date();
        }
        return ret;  //To change body of created methods use Options | File Templates.
    }
}
