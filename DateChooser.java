import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Calendar;

/**
 * dialog to allow user to select a date and return
 * a java.util.Date object
 * Creation date: (2/5/00 10:57:04 AM)
 * 
 * @author Gervase Gallant, ggallant@bigfoot.com
 */
public final class DateChooser extends JDialog implements ActionListener, KeyListener {
    //TODO: submit to resource bundle for internationalization
    private static final long serialVersionUID = 1L;
    private final String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "June", "July", "Aug", "Sept", "Oct", "Nov", "Dec"};
    private JButton[] days = null;
    private JLabel monthLabel = null;
    private Calendar calendar = null;
    private JPanel mainPanel = null;
    private JButton[] moves = null;

    /**
     * DateChooser constructor comment.
     */
    public DateChooser() {
        super();
        setModal(true);

        initialize();
        addKeyListener(this);
    }

    /**
     * deal with all the buttons that may be pressed.
     * Creation date: (2/5/00 11:36:11 AM)
     * 
     * @param e java.awt.event.ActionEvent
     */
    public void actionPerformed(final ActionEvent e) {
        final String text;
        //System.out.println("ActionEvent:" + e);
        if (e.getActionCommand().equals("D")) {
            //return a date
            text = ((JButton) e.getSource()).getText();
            if (text.length() > 0) {
                returnDate(text);
            } else {
                Toolkit.getDefaultToolkit().beep();
            }
        } else {
            roll(e.getActionCommand());
        }

    }

    /**
     * repaint the window with the supplied calendar date.
     * Creation date: (2/5/00 11:04:03 AM)
     */
    private void caption() {
        final Calendar cal = getCalendar();
        final int startPos;

        //for painting ease, quick display
        mainPanel.setVisible(false);

        //set month	
        monthLabel.setText(months[cal.get(Calendar.MONTH)] + ' ' + cal.get(Calendar.YEAR));

        //set to first day
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), 1);

        //now work the day labels
        startPos = cal.get(Calendar.DAY_OF_WEEK);

        days[startPos - 1].requestFocus();

        for (int i = startPos - 1; i < days.length; i++) {
            days[i].setText(String.valueOf(cal.get(Calendar.DATE)));
            days[i].setEnabled(true);
            cal.roll(Calendar.DATE, true);
            if (cal.get(Calendar.DATE) == 1) {
                //clear remaining labels going forward
                for (int j = i + 1; j < days.length; j++) {
                    days[j].setText("");
                    days[j].setEnabled(false);
                }
                break;
            }
        }

        //work first week
        for (int h = 0; h < startPos - 1; h++) {
            if (cal.get(Calendar.DATE) > 25) {
                days[h].setText(String.valueOf(cal.get(Calendar.DATE)));
                days[h].setEnabled(true);
                cal.roll(Calendar.DATE, true);
            } else {
                days[h].setText("");
                days[h].setEnabled(false);
            }
        }

        setCalendar(cal);
        mainPanel.setVisible(true);
    }

    /**
     * Insert the method's description here.
     * Creation date: (2/5/00 1:12:24 PM)
     * 
     * @return java.util.Calendar
     */
    public java.util.Calendar getCalendar() {
        if (calendar == null) {

            calendar = Calendar.getInstance();
        }

        return calendar;
    }

    /**
     * set up the layout and look of the dialog
     * but don't do any data yet.
     * Creation date: (2/5/00 11:02:37 AM)
     */
    private void initialize() {
        JButton jb;
        /* layout:
        North panel: month + year display
        Center Panel: buttons for the days, 5 by 7 grid
        South Panel: navigation buttons */
        mainPanel = new JPanel();
        final JPanel northPanel = new JPanel();
        final JPanel centerPanel = new JPanel();
        final JPanel southPanel = new JPanel();

        //widgets
        //NORTH
        monthLabel = new JLabel(months[9] + " 1953");
        northPanel.setLayout(new FlowLayout());
        northPanel.add(monthLabel);


        //CENTER
        centerPanel.setLayout(new GridLayout(5, 7));
        days = new JButton[35];
        for (int i = 0; i < 35; i++) {
            jb = new JButton(String.valueOf(i));
            jb.setSize(25, 25);
            jb.setBorder(new EmptyBorder(1, 1, 1, 1));
            //jb.setFocusPainted(false);
            jb.setActionCommand("D");
            jb.addActionListener(this);
            jb.addKeyListener(this);
            days[i] = jb;
            centerPanel.add(jb);
        }
	
        //SOUTH
        moves = new JButton[4];
        southPanel.setLayout(new FlowLayout());
        JButton tb = makeButton("<<");
        southPanel.add(tb);
        moves[0] = tb;
        tb = makeButton("<");
        southPanel.add(tb);
        moves[1] = tb;
        tb = makeButton(">");
        southPanel.add(tb);
        moves[2] = tb;
        tb = makeButton(">>");
        southPanel.add(tb);
        moves[3] = tb;


        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(northPanel, "North");
        mainPanel.add(centerPanel, "Center");
        mainPanel.add(southPanel, "South");

        this.getContentPane().add(mainPanel);
        setSize(150, 150);


        caption();


    }

    /**
     * for testing only.
     * Creation date: (2/5/00 11:19:35 AM)
     * 
     * @param args java.lang.String[]
     */
    public static void main(final String[] args) {
        final DateChooser dc = new DateChooser();
        final JFrame jf = new JFrame();
        jf.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        final JPanel jp = new JPanel();
        final JTextField jff = new JTextField("The date field will hold the result.");
        final JButton jb = new JButton("...");

        jb.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                dc.setVisible(true);
                jff.setText("" + dc.getCalendar().getTime());
            }

        });
        jp.add(jff);
        jp.add(jb);
        jf.getContentPane().add(jp);
        jf.setSize(300, 300);

        jf.setVisible(true);


        //set up location of the dialog
        dc.setLocationRelativeTo(jb);


    }

    /**
     * return a button to the control Panel.
     * Creation date: (2/5/00 10:57:59 AM)
     * 
     * @param caption some string
     * @return java.lang.String
     */
    private JButton makeButton(final String caption) {
        final JButton jb = new JButton(caption);
        jb.setSize(25, 25);
        jb.setBorder(new EmptyBorder(1, 4, 1, 4));
        //jb.setFocusPainted(false);
        jb.setActionCommand(caption);
        jb.addActionListener(this);
        jb.addKeyListener(this);
        return jb;
    }

    /**
     * roll the calendar to the day
     * then hide the dialog.
     * Creation date: (2/5/00 11:40:31 AM)
     * 
     * @param day java.lang.String
     */
    private void returnDate(final String day) {

        this.getCalendar().set(this.getCalendar().get(Calendar.YEAR), this.getCalendar().get(Calendar.MONTH), Integer.parseInt(day));

        setVisible(false);

    }

    /**
     * which way to roll the calendar.
     * Creation date: (2/5/00 11:46:42 AM)
     * 
     * @param direction java.lang.String
     */
    private void roll(final String direction) {

        if (direction.equals(">>")) calendar.roll(Calendar.YEAR, true);
        if (direction.equals(">")) calendar.roll(Calendar.MONTH, true);
        if (direction.equals("<<")) calendar.roll(Calendar.YEAR, false);
        if (direction.equals("<")) calendar.roll(Calendar.MONTH, false);
        caption();
    }

    /**
     * Insert the method's description here.
     * Creation date: (2/5/00 1:12:24 PM)
     * 
     * @param newCalendar java.util.Calendar
     */
    private void setCalendar(final java.util.Calendar newCalendar) {
        calendar = newCalendar;

    }

    public void keyTyped(final KeyEvent e) {
        //To change body of implemented methods use Options | File Templates.
        //System.out.println("KeyTyped"+e);
    }

    public void keyPressed(final KeyEvent e) {
        //To change body of implemented methods use Options | File Templates.
        //System.out.println("Keypressed"+e);
    }

    public void keyReleased(final KeyEvent e) {
        //System.out.println("Keyreleased"+e);
        //To change body of implemented methods use Options | File Templates.
        char c = '-';
        final int id = e.getID();
        if (id == KeyEvent.KEY_TYPED) {
            c = e.getKeyChar();
            //keyString = "key character = '" + c + "'";
        } else {
            final int keyCode = e.getKeyCode();

            if ((keyCode == KeyEvent.VK_DOWN) ||
                    (keyCode == KeyEvent.VK_UP) ||
                    (keyCode == KeyEvent.VK_LEFT) ||
                    (keyCode == KeyEvent.VK_RIGHT)) {
                moveFocus(e, keyCode);
            }

            if (keyCode == KeyEvent.VK_ENTER) {
                // do something intelligent
                //System.out.println("Enter pressed");
                final JButton jb = (JButton) e.getSource(); //Assume this event comes from a button
                //System.out.println("Button title:"+jb.getText());
                final String command = jb.getText();
                try {
                    final int val = Integer.parseInt(command);
                    returnDate(command);
                } catch (NumberFormatException exc) {
                    roll(command);
                }
            }
        }
    }

    private void moveFocus(final KeyEvent e, final int keyCode) {
        //To change body of created methods use Options | File Templates.
        final JButton jb = (JButton) e.getSource(); //Assume this event comes from a button
        //System.out.println("Button title:"+jb.getText());
        final String command = jb.getText();
        try {
            int val = Integer.parseInt(command);
            switch (keyCode) {
                case KeyEvent.VK_DOWN:
                    val += 7;
                    break;
                case KeyEvent.VK_UP:
                    val -= 7;
                    break;
                case KeyEvent.VK_LEFT:
                    val -= 1;
                    break;
                case KeyEvent.VK_RIGHT:
                    val += 1;
                    break;
            }

            if (val > 14) {
                while ((val < days.length) && (!days[val].isEnabled())) {
                    val += 1;
                }
            } else {
                while ((val >= 0) && (!days[val].isEnabled())) {
                    val -= 1;
                    if (val < 0) {
                        val = days.length - 1;
                    }
                }
            }
            if ((val >= 0) && (val < days.length)) {
                days[val].requestFocus();
            } else {
                moves[0].requestFocus();
            }
        } catch (NumberFormatException exc) {
            int val = 0;
            switch (keyCode) {
                case KeyEvent.VK_DOWN:
                    // set focus to day 1
                    val = 0;
                    while (!days[val].isEnabled()) {
                        val += 1;
                    }
                    days[val].requestFocus();
                    break;
                case KeyEvent.VK_UP:
                    // set focus to last # line
                    val = days.length - 1;
                    while (!days[val].isEnabled()) {
                        val -= 1;
                    }
                    days[val].requestFocus();
                    break;
                case KeyEvent.VK_LEFT:
                    // set focus to next button
                    val = findMoveButton(jb);
                    val--;
                    if (val < 0) {
                        val = moves.length - 1;
                    }
                    moves[val].requestFocus();
                    break;
                case KeyEvent.VK_RIGHT:
                    // set focus to previous button
                    val = findMoveButton(jb);
                    val++;
                    if (val >= moves.length) {
                        val = 0;
                    }
                    moves[val].requestFocus();
                    break;
            }
        }
    }

    private int findMoveButton(final JButton jb) {
        int ret = 0;
        for (int i = 0; i < moves.length; i++) {
            if (moves[i] == jb) {
                ret = i;
            }
        }
        return ret;
    }
}
