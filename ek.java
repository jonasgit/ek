import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.SecureRandom;

/**
 * An application ...
 */
public final class ek extends JPanel implements ActionListener {
    private static final long serialVersionUID = 1L;
    private static JFrame frame;
    
    private JMenuItem stuffMI;
    private JMenuItem aboutMI;
    private JMenuItem importMI;
    private JMenuItem newMI;
    private JMenuItem loadMI;
    private JMenuItem saveAsMI;
    private JMenuItem saveAsEncryptedMI;
    private JMenuItem exitMI;
    private JMenuItem debug1MI;
    private JMenuItem debug2MI;
    private JMenuItem accountViewMI;
    private JMenuItem transactionsViewMI;
    private JMenuItem graphViewMI;
    private JMenuItem graph2ViewMI;
    private JMenuItem searchViewMI;
    private JMenuItem accountMonthViewMI;
    private JMenuItem accountsViewMI;
    private JMenuItem budgetViewMI;
    private JMenuItem resultViewMI;
    private JMenuItem transferViewMI;
    private JMenuItem addInkopViewMI;
    private JMenuItem addObjectsViewMI;
    private JMenuItem addGeneralTransactionsMI;
    private JMenuItem summaryViewMI;

    private JMenuBar mainMenu;
    
    private JMenu accmenu;
    private JPanel midPanel;
    
    private WorkingStorage ws;
    
    /** 
     * This method.
     *
     */
    public ek(String[] args) {
	setLayout(new BorderLayout());
	setBorder(new EtchedBorder());

	mainMenu = createMenuBar(1);
	add(mainMenu, BorderLayout.NORTH);

	// Create middle panel
	midPanel = new JPanel();
	midPanel.setLayout(new BorderLayout());

	add(midPanel, BorderLayout.CENTER);

	// Create bottom panel

	final JPanel aPanel2 = new JPanel();
	aPanel2.setLayout(new BorderLayout());

	//	aPanel2.add(metalButton, BorderLayout.WEST);
	add(aPanel2, BorderLayout.SOUTH);

	if(args.length>1) {
// 	    if (args.length > 0) {
// 		for(int i=0; i<args.length; i++) {
// 		    System.out.println("Arg "+i+": "+args[i]);
// 		}
// 	    }
	    
	    System.out.println("Trying to import file >"+args[1]+"<");
	    ws = new WorkingStorage();
	    ws.importHEK(args[1]);
	    
	    // Update GUI
	    remove(mainMenu);
	    mainMenu = createMenuBar(2);
	    add(mainMenu, BorderLayout.NORTH);
	    
	    remove(midPanel);
	    midPanel = new SummaryView(ws);
	    add(midPanel, BorderLayout.CENTER);
	}
    }
    
    /** 
     * This method.
     *
     * @param state int
     * @return JMenuBar
     */
    private JMenuBar createMenuBar(final int state) {
	final JMenuBar menuBar;

	JPopupMenu.setDefaultLightWeightPopupEnabled(false);
	menuBar = new JMenuBar();
	
	final JMenu file = menuBar.add(new JMenu("File"));
	
	importMI = file.add(new JMenuItem("Import HemEkonomi"));
	importMI.addActionListener(this);

	newMI = file.add(new JMenuItem("New"));
	newMI.addActionListener(this);

	loadMI = file.add(new JMenuItem("Load"));
	loadMI.addActionListener(this);

	if(state == 2) {
	    saveAsMI = file.add(new JMenuItem("Save as..."));
	    saveAsMI.addActionListener(this);

	    saveAsEncryptedMI = file.add(new JMenuItem("Save Encrypted as..."));
	    saveAsEncryptedMI.addActionListener(this);
	}

	file.add(new JSeparator());
	
	exitMI = file.add(new JMenuItem("Exit"));
	exitMI.addActionListener(this);

	if(state == 2) {
	    accmenu = menuBar.add(new JMenu("Views"));
	    
	    summaryViewMI = accmenu.add(new JMenuItem("Databasöversikt"));
	    summaryViewMI.addActionListener(this);

	    accountViewMI = accmenu.add(new JMenuItem("Kontoutdrag"));
	    accountViewMI.addActionListener(this);

	    transactionsViewMI = accmenu.add(new JMenuItem("Transaktioner"));
	    transactionsViewMI.addActionListener(this);

	    accountsViewMI = accmenu.add(new JMenuItem("Kontoöversikt"));
	    accountsViewMI.addActionListener(this);

	    budgetViewMI = accmenu.add(new JMenuItem("Visa budget"));
	    budgetViewMI.addActionListener(this);

	    resultViewMI = accmenu.add(new JMenuItem("Visa resultat-tabell"));
	    resultViewMI.addActionListener(this);

	    transferViewMI = accmenu.add(new JMenuItem("Visa överföringar"));
	    transferViewMI.addActionListener(this);
	    
	    addObjectsViewMI = accmenu.add(new JMenuItem("Lägg till konton m.m."));
	    addObjectsViewMI.addActionListener(this);
	    addGeneralTransactionsMI = accmenu.add(new JMenuItem("Lägg till transaktion(er)"));
	    addGeneralTransactionsMI.addActionListener(this);
	    graphViewMI = accmenu.add(new JMenuItem("Graph Result"));
	    graphViewMI.addActionListener(this);
	    graph2ViewMI = accmenu.add(new JMenuItem("Graph Likviditet"));
	    graph2ViewMI.addActionListener(this);
	    searchViewMI = accmenu.add(new JMenuItem("Sök transaktioner"));
	    searchViewMI.addActionListener(this);

	    /**   DEBUG-MENU  **/
	    final JMenu debugMenu = menuBar.add(new JMenu("Debug"));
	    
	    debug1MI = debugMenu.add(new JMenuItem("Dump tables"));
	    debug1MI.addActionListener(this);
	    debug2MI = debugMenu.add(new JMenuItem("Dump transactions"));
	    debug2MI.addActionListener(this);
	}
	    
	final JMenu options = menuBar.add(new JMenu("Options"));
	
	stuffMI = options.add(new JMenuItem("Stuff"));
	stuffMI.addActionListener(this);
	options.add(new JSeparator());
	aboutMI = options.add(new JMenuItem("About"));
	aboutMI.addActionListener(this);
	
	return menuBar;
    }

    /** 
     * This method.
     *
     */
    private static void testQuit() {
	final int svar = JOptionPane.showConfirmDialog(frame,
						 "Bekräfta att du vill avsluta",
						 "Avsluta",
						 JOptionPane.OK_CANCEL_OPTION,
						 JOptionPane.QUESTION_MESSAGE);
	if(svar == 0) {
	    System.exit(0);
	}
    }
    /** 
     * This method.
     *
     */
    private void testDirty() {
	if(ws!=null) {
	    if(ws.isDirty()) {
		final int svar = JOptionPane.showConfirmDialog(frame,
							 "Spara innan avsluta?",
							 "Avsluta",
							 JOptionPane.YES_NO_OPTION,
							 JOptionPane.QUESTION_MESSAGE);
		if(svar == 0) {
		    saveAs();
		}
		System.exit(0);
	    } else {
		System.exit(0);
	    }
	} else {
	    System.exit(0);
	}
    }

    /** 
     * This method.
     *
     * @param e ActionEvent
     */
    public void actionPerformed(final ActionEvent e) {
	if (e.getSource().equals(exitMI)) {
	    testDirty();
	} else if (e.getSource().equals(importMI)) {
	    importOldFile();
	} else if (e.getSource().equals(newMI)) {
	    newWS();
	} else if (e.getSource().equals(loadMI)) {
	    load();
	} else if (e.getSource().equals(saveAsMI)) {
	    saveAs();
	} else if (e.getSource().equals(saveAsEncryptedMI)) {
	    saveAsEncrypted();
	} else if (e.getSource().equals(debug1MI)) {
	    debugDump(1);
	} else if (e.getSource().equals(debug2MI)) {
	    debugDump(2);
	} else if (e.getSource().equals(summaryViewMI)) {
	    remove(midPanel);
	    midPanel = new SummaryView(ws);
	    add(midPanel, BorderLayout.CENTER);
	    frame.pack();
	} else if (e.getSource().equals(accountViewMI)) {
	    remove(midPanel);
	    midPanel = new AccountView(ws);
	    add(midPanel, BorderLayout.CENTER);
	    frame.pack();
	} else if (e.getSource().equals(transactionsViewMI)) {
	    remove(midPanel);
	    midPanel = new TransactionsView(ws);
	    add(midPanel, BorderLayout.CENTER);
	    frame.pack();
	} else if (e.getSource().equals(graphViewMI)) {
	    remove(midPanel);
	    midPanel = new GraphView(ws);
	    add(midPanel, BorderLayout.CENTER);
	    frame.pack();
	} else if (e.getSource().equals(graph2ViewMI)) {
	    remove(midPanel);
	    midPanel = new Graph2View(ws);
	    add(midPanel, BorderLayout.CENTER);
	    frame.pack();
	} else if (e.getSource().equals(searchViewMI)) {
	    remove(midPanel);
	    midPanel = new SearchView(ws);
	    add(midPanel, BorderLayout.CENTER);
	    frame.pack();
	} else if (e.getSource().equals(accountMonthViewMI)) {
	    remove(midPanel);
	    midPanel = new AccountMonthView(ws);
	    add(midPanel, BorderLayout.CENTER);
	    frame.pack();
	} else if (e.getSource().equals(accountsViewMI)) {
	    remove(midPanel);
	    midPanel = new AccountsView(ws);
	    add(midPanel, BorderLayout.CENTER);
	    frame.pack();
	} else if (e.getSource().equals(budgetViewMI)) {
	    remove(midPanel);
	    midPanel = new BudgetView(ws);
	    add(midPanel, BorderLayout.CENTER);
	    frame.pack();
	} else if (e.getSource().equals(resultViewMI)) {
	    remove(midPanel);
	    midPanel = new ResultView(ws);
	    add(midPanel, BorderLayout.CENTER);
	    frame.pack();
	} else if (e.getSource().equals(transferViewMI)) {
	    remove(midPanel);
	    midPanel = new TransferView(ws);
	    add(midPanel, BorderLayout.CENTER);
	    frame.pack();
	} else if (e.getSource().equals(addInkopViewMI)) {
	    remove(midPanel);
	    midPanel = new AddInkopView(ws);
	    add(midPanel, BorderLayout.CENTER);
	    frame.pack();
	} else if (e.getSource().equals(addObjectsViewMI)) {
	    remove(midPanel);
	    midPanel = new AddObjectsView(ws);
	    add(midPanel, BorderLayout.CENTER);
	    frame.pack();
	} else if (e.getSource().equals(addGeneralTransactionsMI)) {
	    remove(midPanel);
	    midPanel = new AddGeneralTransactionsView(ws);
	    add(midPanel, BorderLayout.CENTER);
	    frame.pack();
	} else if (e.getSource().equals(stuffMI)) {
	    MessageDialogue helpd = new MessageDialogue("General odds and ends\n* There is a commandline option to read an Hogia Hemekonomi-database directly. This is the same as starting the program and do a manual import using File-menu and Import Hemekonomi. Usage:\njava -jar ek.jar -import h:\\home\\hogia\\JONAS.MDB");
	    helpd.setVisible(true);
	} else if (e.getSource().equals(aboutMI)) {
	    final Runtime r = Runtime.getRuntime();

	    JOptionPane.showMessageDialog(frame,
					  "Ek v0.12 by Jonas Svensson\n\n" +
					  "Free memory: "+r.freeMemory()/1024+"kB\n"+
					  "Total memory: "+r.totalMemory()/1024+"kB\n"+
					  "Max memory: "+r.maxMemory()/1024+"kB\n"+
					  "Processors: "+r.availableProcessors()+'\n'+
					  "java.version: "+System.getProperty("java.version")+'\n'+
					  "java.vendor: "+System.getProperty("java.vendor")+'\n'+
					  "os.name: "+System.getProperty("os.name")+'\n'+
					  "os.arch: "+System.getProperty("os.arch")+'\n'+
					  "os.version: "+System.getProperty("os.version")+'\n'+
					  "java.vm.specification.version: "+System.getProperty("java.vm.specification.version")+'\n'+
					  "java.vm.version: "+System.getProperty("java.vm.version")+'\n'+
					  "java.specification.version: "+System.getProperty("java.specification.version")+'\n'
					  );
	} else {
	    System.out.println("Unhandled menuitem: "+e);
	}
    }
    
    /** 
     * This method.
     *
     */
    private void importOldFile() {
	final JFileChooser chooser = new JFileChooser();
	final ExampleFileFilter filter = new ExampleFileFilter();
	filter.addExtension("mdb");
	filter.setDescription("Hogia Hemekonomi-filer");
	chooser.setFileFilter(filter);
	final int returnVal = chooser.showOpenDialog(this);
	if(returnVal == JFileChooser.APPROVE_OPTION) {
	    remove(midPanel);
	    midPanel = new WaitView();
	    add(midPanel, BorderLayout.CENTER);
	    frame.pack();
	    repaint();
	    SwingUtilities.invokeLater(
				       new Runnable() {
					   public void run() {
					       ws = new WorkingStorage();
					       ws.importHEK(chooser.getSelectedFile());
					       
					       // Update GUI
					       remove(mainMenu);
					       mainMenu = createMenuBar(2);
					       add(mainMenu, BorderLayout.NORTH);
					       
					       remove(midPanel);
					       midPanel = new SummaryView(ws);
					       add(midPanel, BorderLayout.CENTER);
					       frame.pack();
					   }
				       }
				       );
	    
	}
    }
    
    /** 
     * This method.
     *
     * @param val int
     */
    private void debugDump(final int val) {
	ws.debugDump(val);
    }
    
    /** 
     * This method.
     *
     */
    private void newWS() {
	ws = new WorkingStorage();
	ws.initNew();

	// Update GUI
	remove(mainMenu);
	mainMenu = createMenuBar(2);
	add(mainMenu, BorderLayout.NORTH);
	
	remove(midPanel);
	midPanel = new SummaryView(ws);
	add(midPanel, BorderLayout.CENTER);
	frame.pack();
    }
    
    /** 
     * This method.
     *
     */
    private void load() {
	final JFileChooser chooser = new JFileChooser();
	final ExampleFileFilter filter = new ExampleFileFilter();
	filter.addExtension("jek");
	filter.setDescription("Ek-filer");
	chooser.setFileFilter(filter);
	final int returnVal = chooser.showOpenDialog(this);
	if(returnVal == JFileChooser.APPROVE_OPTION) {
	    ws = new WorkingStorage();
	    ws.load(chooser.getSelectedFile());
	    //JOptionPane.showMessageDialog(frame, "Loaded "+chooser.getSelectedFile());
	    // Update GUI
	    remove(mainMenu);
	    mainMenu = createMenuBar(2);
	    add(mainMenu, BorderLayout.NORTH);

	    remove(midPanel);
	    midPanel = new SummaryView(ws);
	    add(midPanel, BorderLayout.CENTER);
	    frame.pack();
	}
    }
    
    /** 
     * Get the extension of a file.
     *
     * @param f File
     * @return String
     */
    private static String getExtension(final File f) {
        String ext = null;
        final String s = f.getName();
        final int i = s.lastIndexOf(".");

        if (i > 0 &&  i < s.length() - 1) {
            ext = s.substring(i+1).toLowerCase();
        }
        return ext;
    }

    /** 
     * This method.
     *
     */
    private void saveAs() {
	final JFileChooser chooser = new JFileChooser();
	final ExampleFileFilter filter = new ExampleFileFilter();
	filter.addExtension("jek");
	filter.setDescription("Ek-filer");
	chooser.setFileFilter(filter);
	chooser.setAcceptAllFileFilterUsed(false);
	final int returnVal = chooser.showSaveDialog(this);
	if(returnVal == JFileChooser.APPROVE_OPTION) {
	    File f = chooser.getSelectedFile();

	    final String extension = getExtension(f);
	    if (extension != null) {
		if (!extension.equals("jek")) {
		    f = new File(f.toString()+".jek");
		}
	    } else {
		f = new File(f.toString()+".jek");
	    }
	    try {
		ws.exportXML(new FileOutputStream(f));
		JOptionPane.showMessageDialog(frame,
					      "Database probably saved as "+f);
	    } catch (java.io.FileNotFoundException e) {
		System.err.println(e);
	    }
	}
    }
    
    /** 
     * This method.
     *
     */
    private void saveAsEncrypted() {
	//byte[] iv =
	//    { 0x0a, 0x01, 0x02, 0x03, 0x04, 0x0b, 0x0c, 0x0d };

	final JFileChooser chooser = new JFileChooser();
	final ExampleFileFilter filter = new ExampleFileFilter();
	filter.addExtension("jek");
	filter.setDescription("Ek-filer");
	chooser.setFileFilter(filter);
	chooser.setAcceptAllFileFilterUsed(false);
	final int returnVal = chooser.showSaveDialog(this);
	if(returnVal == JFileChooser.APPROVE_OPTION) {
	    File f = chooser.getSelectedFile();

	    final String extension = getExtension(f);
	    if (extension != null) {
		if (!extension.equals("jek")) {
		    f = new File(f.toString()+".jek");
		}
	    } else {
		f = new File(f.toString()+".jek");
	    }

	    // Generate a secret key
	    try {
		String password = "tjolahopp";

		char[] psw_chars = password.toCharArray();
		SecureRandom random = new SecureRandom();
		byte salt_bytes[] = new byte[16];
		random.nextBytes(salt_bytes);

		System.err.println("Using salt:"+ws.asHex(salt_bytes));

		SecretKey skey=null;

		SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
		PBEKeySpec spec = new PBEKeySpec(psw_chars, salt_bytes, 1024, 128);
		try {
		    skey = factory.generateSecret(spec);
		} catch (java.security.spec.InvalidKeySpecException e) {
		    System.err.println("ERROR:"+e);
		}
		SecretKeySpec skeySpec = new SecretKeySpec(skey.getEncoded(), "AES");
		
		Cipher cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
		
		FileOutputStream foo = new FileOutputStream(f);
		foo.write("EK002".getBytes());
		foo.write(salt_bytes);
		OutputStream ef = new CipherOutputStream(foo,
							 cipher);
		//ws.exportXML(ef);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ws.exportXML(baos);
		ef.write(baos.toByteArray());
		FileOutputStream fos = new FileOutputStream(new File(f.toString()+".raw"));
		fos.write(baos.toByteArray());

		fos.close();
		ef.close();
		JOptionPane.showMessageDialog(frame,
					      "Database probably saved as "+f);
	    } catch (java.security.NoSuchAlgorithmException e) {
		System.err.println(e);
	    } catch (javax.crypto.NoSuchPaddingException e) {
		System.err.println(e);
	    } catch (java.security.InvalidKeyException e) {
		System.err.println(e);
	    } catch (java.io.IOException e) {
		System.err.println(e);
	    }
	}
    }
    
// --Recycle Bin START (2003-12-27 18:04):
//    /** An ActionListener that listens to the radio buttons. */
//    private static final class RadioListener implements ActionListener {
//	public final void actionPerformed(ActionEvent e) {
//
//
//        try {
//		SwingUtilities.updateComponentTreeUI(frame);
//		frame.pack();
//	    }
//	    catch (Exception exc) {
//		System.err.println("ek.ActionListener error:: " + exc);
//	    }
//
//	}
//    }
// --Recycle Bin STOP (2003-12-27 18:04)

    private static void setUIFont() {
	javax.swing.plaf.FontUIResource f = new javax.swing.plaf.FontUIResource(new java.awt.Font("Arial", java.awt.Font.PLAIN, 11));
	
	java.util.Enumeration keys = javax.swing.UIManager.getDefaults().keys();
	while (keys.hasMoreElements()) {
	    Object key = keys.nextElement();
	    Object value = javax.swing.UIManager.get(key);
	    if (value instanceof javax.swing.plaf.FontUIResource)
		javax.swing.UIManager.put(key, f);
	}
    }
    
    /** 
     * This method.
     *
     * @param s String[]
     */
    public static void main(final String[] args) {
	setUIFont();

	final ek panel = new ek(args);
	frame = new JFrame("Ek by Jonas Svensson");
	frame.addWindowListener(
				new WindowAdapter() {
				    public void windowClosing(final WindowEvent e) {
					System.exit(0);
				    }
				}
				);
	frame.getContentPane().add("Center", panel);
	frame.pack();
	frame.setVisible(true);
    }
}
