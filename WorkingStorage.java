import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.BufferedInputStream;
import java.text.FieldPosition;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.TreeSet;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.sql.*;

import javax.crypto.CipherOutputStream;
import javax.crypto.CipherInputStream;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

final class WorkingStorage {
    private final ArrayList<Account> accounts = new ArrayList<Account>();
    private long maxIdAccounts=0;

    private final ArrayList<User> users = new ArrayList<User>();
    private long maxIdUsers=0;

    private final ArrayList<String> types = new ArrayList<String>();

    private final ArrayList<Category> categories = new ArrayList<Category>();
    private long maxIdCategories=0;

    private final ArrayList<Transaction> transactions = new ArrayList<Transaction>();
    private long maxIdTransactions=0;

    private final ArrayList<Transfer> transfers = new ArrayList<Transfer>();
    private long maxIdTransfers=0;

    private final ArrayList<BudgetLine> budgetLines = new ArrayList<BudgetLine>();
    private long maxIdBudgetLines=0;

    private final ArrayList<Place> places = new ArrayList<Place>();
    private long maxIdPlaces=0;

    private final String[] months = {"Jan", "Feb", "Mar", "Apr", "Maj", "Jun", "Jul", "Aug", "Sep", "Okt", "Nov", "Dec"};
    private final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

    private boolean dirty=false;

    WorkingStorage() {
	//System.out.println("WorkingStorage constructor");
    }

    /** 
     * This method tells if the database is saved or not (dirty).
     *
     @return boolean
     */
    public boolean isDirty() {
	return dirty;
    }

    /** 
     * This method.
     *
     */
    private void dumpAllButTransactions() {
	System.out.println("Användare");
	User u;
	for(int i=0; i<users.size(); i++) {
	    u=users.get(i);
	    System.out.print("User#"+i+": ");
	    u.debugDump();
	}
	
	System.out.println("Platser");
	Place p;
	for(int i=0; i<places.size(); i++) {
	    p=places.get(i);
	    System.out.print("Place#"+i+": ");
	    p.debugDump();
	}
	System.out.println("Konton");
	Account a;
	for(int i=0; i<accounts.size(); i++) {
	    a=accounts.get(i);
	    System.out.print("Account#"+i+": ");
	    a.debugDump();
	}
	System.out.println("Types");
	for(int i=0; i<types.size(); i++) {
	    System.out.println("Type#"+i+": "+types.get(i));
	}
	System.out.println("Categories");
	Category c;
	for(int i=0; i<categories.size(); i++) {
	    c=categories.get(i);
	    System.out.print("Category#"+i+": ");
	    c.debugDump();
	}
	System.out.println("Transfers");
	Transfer t;
	for(int i=0; i<transfers.size(); i++) {
	    t=transfers.get(i);
	    System.out.print("Transfer#"+i+": ");
	    t.debugDump();
	}
    }
    
    /** 
     * This method.
     *
     */
    private void dumpTransactions() {
	Transaction t;
	for(int i=0; i<transactions.size(); i++) {
	    t=transactions.get(i);
	    System.out.print("Transaction#"+i+": ");
	    t.debugDump();
	}
    }
    
    /** 
     * This method.
     *
     * @param val int
     */
    public void debugDump(final int val) {
	System.out.println("--->>  BEGIN DUMP  <<---");

        switch(val) {
	case 1:
	    dumpAllButTransactions();
	    break;
	case 2:
	    dumpTransactions();
	    break;
	default:
	    System.err.println("debugDump called with unknown value");
	    break;
	}
	System.out.println("--->>  END DUMP  <<---");
    }
    
    /** 
     * This method
     * This method must not be called unless this object is fresh and new.
     * (ie there should be no entries in types)
     *
     */
    public void initNew() {
	types.add("Inköp");
	types.add("Insättning");
	types.add("Uttag");
	types.add("Överföring");

	maxIdUsers=(long) -1;
	addPerson("Gemensamt", "1900", "-");

	dirty=true;
    }
    

    /** 
     * This method.
     *
     * @param file File
     */
    public void load(final File file) {
	try {
	    FileInputStream data = new FileInputStream(file);
	    byte[] magicnumber = new byte[5];
	    data.read(magicnumber);
	    byte[] id_v2 = "EK002".getBytes();
	    if (Arrays.equals(magicnumber, id_v2)) {
		load_v2(data);
	    } else {
		System.out.println("v1 file. Loading...");
		load_v1(new FileInputStream(file));
	    }
	} catch (java.io.IOException e) {
	    System.err.println("ERROR:"+e);
	}
    }

    /** 
     * This method.
     *
     * @param file File
     */
    public void load_v2(final FileInputStream file) {
	// Generate a secret key
	try {
	    String password = "tjolahopp";
	    
	    char[] psw_chars = password.toCharArray();
	    byte salt_bytes[] = new byte[16];
	    file.read(salt_bytes);

	    //System.err.println("Read salt: "+asHex(salt_bytes));

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
	    cipher.init(Cipher.DECRYPT_MODE, skeySpec);
	    
	    CipherInputStream ef = new CipherInputStream(file,
							 cipher);
	    load_v1(new BufferedInputStream(ef));
	    //debug_dump_file(ef, file.toString());
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

    /** 
     * This method.
     *
     * @param file File
     */
    public void debug_dump_file(final InputStream infile, final String filename) {
	byte[] buff = new byte[100];
	String tecken;
	int readbytes=1;

	try {
	    FileOutputStream fos = new FileOutputStream(new File(filename+".dump"));
	    readbytes = infile.read(buff);
	    while(readbytes > 0) {
		System.out.println("Readbytes:"+readbytes);
		//System.out.println(asHex(buff));
		fos.write(buff, 0, readbytes);
		//tecken = new String(buff);
		//System.out.print(tecken);
		readbytes = infile.read(buff);
	    }
	    fos.close();
	} catch(java.io.IOException e) {
	    System.err.println(e);
	}
	System.out.println("END OF DUMP");
    }

    /** 
     * This method.
     *
     * @param file File
     */
    public void load_v1(final InputStream file) {
        // Step 1: create a DocumentBuilderFactory and configure it
        final DocumentBuilderFactory dbf =
            DocumentBuilderFactory.newInstance();
	
        // Optional: set various configuration options
        //dbf.setValidating(validation);
        //dbf.setIgnoringComments(ignoreComments);
        //dbf.setIgnoringElementContentWhitespace(true);
        //dbf.setCoalescing(putCDATAIntoText);
        // The opposite of creating entity ref nodes is expanding them inline
        //dbf.setExpandEntityReferences(!createEntityRefs);

        // At this point the DocumentBuilderFactory instance can be saved
        // and reused to create any number of DocumentBuilder instances
        // with the same configuration options.

        // Step 2: create a DocumentBuilder that satisfies the constraints
        // specified by the DocumentBuilderFactory
        DocumentBuilder db = null;
        try {
            db = dbf.newDocumentBuilder();
        } catch (ParserConfigurationException pce) {
            System.err.println(pce);
            System.exit(1);
        }

        // Set an ErrorHandler before parsing
        //OutputStreamWriter errorWriter =
        //    new OutputStreamWriter(System.err, outputEncoding);
        //db.setErrorHandler(
        //    new MyErrorHandler(new PrintWriter(errorWriter, true)));

        // Step 3: parse the input file
        Document doc = null;
        try {
            doc = db.parse(file);
        } catch (SAXException se) {
            System.err.println(se.getMessage());
            System.exit(1);
        } catch (IOException ioe) {
            System.err.println(ioe);
            System.exit(1);
        }

	final Node root = doc.getFirstChild();
	for (Node child = root.getFirstChild(); child != null;
             child = child.getNextSibling()) {
	    if(child.getNodeName().compareToIgnoreCase("Accounts") == 0)
		loadAccounts(child);
	    if(child.getNodeName().compareToIgnoreCase("Users") == 0)
		loadUsers(child);
	    if(child.getNodeName().compareToIgnoreCase("Types") == 0)
		loadTypes(child);
	    if(child.getNodeName().compareToIgnoreCase("Categories") == 0)
		loadCategories(child);
	    if(child.getNodeName().compareToIgnoreCase("Places") == 0)
		loadPlaces(child);
	    if(child.getNodeName().compareToIgnoreCase("Transactions") == 0)
		loadTransactions(child);
	    if(child.getNodeName().compareToIgnoreCase("Budgets") == 0)
		loadBudgets(child);
	    if(child.getNodeName().compareToIgnoreCase("Transfers") == 0)
		loadTransfers(child);
	}
	dirty=false;
    }

    /** 
     * This method.
     *
     * @param root Node
     */
    private void loadAccounts(final Node root) {
	Account account;
	
	for (Node child = root.getFirstChild(); child != null;
             child = child.getNextSibling()) {
	    if(child.getNodeName().compareToIgnoreCase("Account") == 0) {
		account = new Account(child);
		if(account.getId() > maxIdAccounts) {
		    maxIdAccounts = account.getId();
		}
		accounts.add(account);
	    }		
	}
    }
    
    /** 
     * This method.
     *
     * @param root Node
     */
    private void loadUsers(final Node root) {
	User user;
	
	for (Node child = root.getFirstChild(); child != null;
             child = child.getNextSibling()) {
	    if(child.getNodeName().compareToIgnoreCase("User") == 0) {
		user = new User(child);
		if(user.getId() > maxIdUsers) {
		    maxIdUsers = user.getId();
		}
		users.add(user);
	    }
	}
    }
    
    /** 
     * This method.
     *
     * @param root Node
     */
    private void loadTypes(final Node root) {
	types.add("Inköp");
	types.add("Insättning");
	types.add("Uttag");
	types.add("Överföring");
    }
    
    /** 
     * This method.
     *
     * @param root Node
     */
    private void loadCategories(final Node root) {
	Category category;
	
	for (Node child = root.getFirstChild(); child != null;
             child = child.getNextSibling()) {
	    if(child.getNodeName().compareToIgnoreCase("Category") == 0) {
		category = new Category(child);
		if(category.getId() > maxIdCategories) {
		    maxIdCategories = category.getId();
		}
		categories.add(category);
	    }		
	}
    }
    
    /** 
     * This method.
     *
     * @param root Node
     */
    private void loadPlaces(final Node root) {
	Place place;
	
	for (Node child = root.getFirstChild(); child != null;
             child = child.getNextSibling()) {
	    if(child.getNodeName().compareToIgnoreCase("Place") == 0) {
		place = new Place(child);
		if(place.getId() > maxIdPlaces) {
		    maxIdPlaces = place.getId();
		}
		places.add(place);
	    }		
	}
    }
    
    /** 
     * This method.
     *
     * @param root Node
     */
    private void loadTransactions(final Node root) {
	Transaction transaction;
	
	for (Node child = root.getFirstChild(); child != null;
             child = child.getNextSibling()) {
	    if(child.getNodeName().compareToIgnoreCase("Transaction") == 0) {
		transaction = new Transaction(child);
		if(transaction.getId() > maxIdTransactions) {
		    maxIdTransactions = transaction.getId();
		}
		transactions.add(transaction);
	    }		
	}
    }
    
    /** 
     * This method.
     *
     * @param root Node
     */
    private void loadBudgets(final Node root) {
	BudgetLine budgetLine;
	
	for (Node child = root.getFirstChild(); child != null;
             child = child.getNextSibling()) {
	    if(child.getNodeName().compareToIgnoreCase("BudgetLine") == 0) {
		budgetLine = new BudgetLine(child);
		if(budgetLine.getId() > maxIdBudgetLines) {
		    maxIdBudgetLines = budgetLine.getId();
		}
		budgetLines.add(budgetLine);
	    }		
	}
    }
    
    /** 
     * This method.
     *
     * @param root Node
     */
    private void loadTransfers(final Node root) {
	Transfer transfer;
	
	for (Node child = root.getFirstChild(); child != null;
             child = child.getNextSibling()) {
	    if(child.getNodeName().compareToIgnoreCase("Transfer") == 0) {
		transfer = new Transfer(child);
		if(transfer.getId() > maxIdTransfers) {
		    maxIdTransfers = transfer.getId();
		}
		transfers.add(transfer);
	    }		
	}
    }
    
// --Recycle Bin START (2003-12-27 18:04):
//    /**
//     * This method
//     *
//     * @tag    Comment for the tag
//     */
//    public final void exportXML(String filename) {
//	File file = new File(filename);
//	exportXML(file);
//    }
// --Recycle Bin STOP (2003-12-27 18:04)

    /** 
     * This method.
     *
     * @param file File
     */
    public void exportXML(final OutputStream file) {
	// Build DOM-tree
	final Document document;

	final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

	factory.setValidating(false);
	//factory.setNamespaceAware();
        factory.setIgnoringElementContentWhitespace(false);
        factory.setIgnoringComments(false);
        factory.setCoalescing(false);
        // The opposite of creating entity ref nodes is expanding them inline
        factory.setExpandEntityReferences(true);

	try {
	    final DocumentBuilder builder = factory.newDocumentBuilder();
	    document = builder.newDocument();
	    
	    myBuildDocument(document);
	    
	    final Result result = new StreamResult(file);   // Where to put result document
	    // Print out the DOM tree as (more or less)
	    // xml-style.
	    
	    // Start off with a factory object
	    final TransformerFactory tf = TransformerFactory.newInstance();
	    // Create a Transformer object
	    final Transformer transformer = tf.newTransformer();
	    // Create a source from the DOM document
	    final DOMSource source = new DOMSource(document);
	    // Finally, perform the transformation
	    transformer.setOutputProperty("indent", "yes");
	    transformer.setOutputProperty(javax.xml.transform.OutputKeys.INDENT, "yes");
	    transformer.transform(source, result);
	    file.flush();
	} catch(ParserConfigurationException pce) {
	    pce.printStackTrace();
	} catch(TransformerConfigurationException tce) {
	    tce.printStackTrace();
	} catch(TransformerException te) {
	    te.printStackTrace();
	} catch(java.io.IOException e) {
	    System.err.println(e);
	    e.printStackTrace();
	}
	dirty=false;
    }
    
    private void myBuildDocument(final Document document) {
	final Element root = document.createElement("Ek");
	root.setAttribute("Version", "1");
 	document.appendChild(root);

	DOMaccounts(document, root);
	DOMusers(document, root);
	DOMtypes(document, root);
	DOMcategories(document, root); // must be before transactions
	DOMplaces(document, root); // must be before transactions
	DOMtransactions(document, root);
	DOMbudgets(document, root);
	DOMtransfers(document, root);
    }

    /** 
     * This method.
     *
     * @param document some Document
     * @param root some Node
     */
    private void DOMaccounts(final Document document, final Node root) {
	final Node accountsNode = document.createElement("Accounts");
	Account account;
	
	for(int i=0; i<accounts.size(); i++) {
	    account = accounts.get(i);
	    account.DOMexport(document, accountsNode);
	}

	root.appendChild(accountsNode);
    }

    /** 
     * This method.
     *
     * @param document Document
     * @param root Node
     */
    private void DOMusers(final Document document, final Node root) {
	final Node usersNode = document.createElement("Users");
	User user;
	
	for(int i=0; i<users.size(); i++) {
	    user = users.get(i);
	    user.DOMexport(document, usersNode);
	}

	root.appendChild(usersNode);
    }

    /** 
     * This method.
     *
     * @param document Document
     * @param root Node
     */
    private void DOMtypes(final Document document, final Node root) {
	final Node typesNode = document.createElement("Types");
	Node idNode;
        Node typeNode;
        Node nameNode;

        for(int i=0; i<types.size(); i++) {
	    typeNode = document.createElement("Type");
	    idNode = document.createElement("Id");
	    idNode.appendChild(document.createTextNode(""+i));
	    nameNode = document.createElement("Name");
	    nameNode.appendChild(document.createTextNode(types.get(i)));

	    typeNode.appendChild(idNode);
	    typeNode.appendChild(nameNode);
	    typesNode.appendChild(typeNode);
	}

	root.appendChild(typesNode);
    }

    /** 
     * This method.
     *
     * @param document Document
     * @param root Node
     */
    private void DOMcategories(final Document document, final Node root) {
	final Node categoriesNode = document.createElement("Categories");
	Category category;
	
	for(int i=0; i<categories.size(); i++) {
	    category = categories.get(i);
	    category.DOMexport(document, categoriesNode);
	}

	root.appendChild(categoriesNode);
    }

    /** 
     * This method.
     *
     * @param document Document
     * @param root Node
     */
    private void DOMplaces(final Document document, final Node root) {
	final Node placesNode = document.createElement("Places");
	Place place;
	
	for(int i=0; i<places.size(); i++) {
	    place = places.get(i);
	    place.DOMexport(document, placesNode);
	}

	root.appendChild(placesNode);
    }

    /** 
     * This method.
     *
     * @param document Document
     * @param root Node
     */
    private void DOMtransactions(final Document document, final Node root) {
	final Node transactionsNode = document.createElement("Transactions");
	Transaction transaction;
	
	for(int i=0; i<transactions.size(); i++) {
	    transaction = transactions.get(i);
	    transaction.DOMexport(document, transactionsNode);
	}

	root.appendChild(transactionsNode);
    }

    /** 
     * This method.
     *
     * @param document Document
     * @param root Node
     */
    private void DOMbudgets(final Document document, final Node root) {
	final Node budgetsNode = document.createElement("Budgets");
	BudgetLine budgetLine;
	
	for(int i=0; i<budgetLines.size(); i++) {
	    budgetLine = budgetLines.get(i);
	    budgetLine.DOMexport(document, budgetsNode);
	}

	root.appendChild(budgetsNode);
    }

    /** 
     * This method.
     *
     * @param document Document
     * @param root Node
     */
    private void DOMtransfers(final Document document, final Node root) {
	final Node transfersNode = document.createElement("Transfers");
	Transfer transfer;
	
	for(int i=0; i<transfers.size(); i++) {
	    transfer = transfers.get(i);
	    transfer.DOMexport(document, transfersNode);
	}

	root.appendChild(transfersNode);
    }

    /** 
     * This method.
     *
     * @return ArrayList
     */
    public ArrayList<String> getAccountNames() {
	final ArrayList<String> names = new ArrayList<String>();

	for(int i=0; i<accounts.size(); i++) {
	    names.add( ( accounts.get(i)).getTitle());
	}
	return names;
    }

    /** 
     * This method.
     *
     * @return ArrayList
     */
    public ArrayList<String> getPlaces() {
	final ArrayList<String> names = new ArrayList<String>();

	for(int i=0; i<places.size(); i++) {
	    names.add( ( places.get(i)).getName());
	}
	return names;
    }

    /** 
     * This method gives the names of all categories.
     * Categories like "lön", "barnbidrag", "amortering", "livsmedel".
     *
     * @return ArrayList
     */
    public ArrayList<String> getCategories() {
	final ArrayList<String> names = new ArrayList<String>();

	for(int i=0; i<categories.size(); i++) {
	    names.add( ( categories.get(i)).getName());
	}
	return names;
    }

    /** 
     * This method gives the summaries for a specific month,
     *
     * @param int year
     * @param int month
     * @param long incometype = findTypeId("Insättning");
     * @return long
     */
    private long getSumByMonthLong(int year, int month, long incometype) {
	Transaction t;
	long total = 0;

	final ArrayList transactions = getTransactionsByMonth(year, month);
	for(int i=0; i<transactions.size(); i++) {
	    t =  (Transaction)transactions.get(i);
	    if( t.getType() == incometype) {
		total += t.getAmount();
	    }
	}
	return total;
    }

    /** 
     * This method gives the income summaries for a specific month,
     *
     * @return long
     */
    public long getIncomesByMonthLong(int year, int month) {
	long incometype = findTypeId("Insättning");
	return getSumByMonthLong(year, month, incometype);
    }

    /** 
     * This method gives the income summaries for a specific month,
     *
     * @return double
     */
    public double getIncomesByMonth(int year, int month) {
	long total = 0;
	total = getIncomesByMonthLong(year, month);
	return ((double) total) / 100.0;
    }

    /** 
     * This method gives the outgo summaries for a specific month,
     *
     * @return double
     */
    public double getOutgoByMonth(int year, int month) {
	long total = 0;
	total = getOutgoByMonthLong(year, month);
	return ((double) total) / 100.0;
    }

    /** 
     * This method gives the outgo summaries for a specific month,
     *
     * @return long
     */
    public long getOutgoByMonthLong(int year, int month) {
	long incometype = findTypeId("Inköp");
	return getSumByMonthLong(year, month, incometype);
    }

    /** 
     * This method gives the transactions for a specific month,
     *
     * @return ArrayList
     */
    public ArrayList getTransactionsByMonth(int year, int month) {
	final ArrayList<Transaction> trans = new ArrayList<Transaction>();
	Transaction t;
	Date d;
	GregorianCalendar c=new GregorianCalendar();

	for(int i=0; i<transactions.size(); i++) {
	    t = transactions.get(i);
	    d = t.getDate();
	    c.setTime(d);
	    if((c.get(Calendar.YEAR) == year) &&
	       (c.get(Calendar.MONTH) == month)) {
		trans.add(t);
	    }
	}
	return trans;
    }

    /** 
     * This method gives the income summaries for a specific year,
     * ie a summery for January, one for February and so on.
     * Resulting in a 12 item long list.
     *
     * @return ArrayList
     */
    public ArrayList getIncomesByYear(int year) {
	final ArrayList<Double> sums = new ArrayList<Double>();

	for(int i=0; i<12; i++) {
	    sums.add(new Double(getIncomesByMonth(year, i)));
	}
	return sums;
    }

    /** 
     * This method gives the income summaries for a specific year,
     * ie a summery for January, one for February and so on.
     * Resulting in a 12 item long list.
     *
     * @return ArrayList
     */
    public ArrayList getIncomesByYearLong(int year) {
	final ArrayList<Long> sums = new ArrayList<Long>();

	for(int i=0; i<12; i++) {
	    sums.add(new Long(getIncomesByMonthLong(year, i)));
	}
	return sums;
    }

    /** 
     * This method gives the outgo summaries for a specific year,
     * ie a summery for January, one for February and so on.
     * Resulting in a 12 item long list.
     *
     * @return ArrayList
     */
    public ArrayList getOutgoByYear(int year) {
	final ArrayList<Double> sums = new ArrayList<Double>();

	for(int i=0; i<12; i++) {
	    sums.add(new Double(getOutgoByMonth(year, i)));
	}
	return sums;
    }

    /** 
     * This method gives the outgo summaries for a specific year,
     * ie a summery for January, one for February and so on.
     * Resulting in a 12 item long list.
     *
     * @return ArrayList
     */
    public ArrayList getOutgoByYearLong(int year) {
	final ArrayList<Long> sums = new ArrayList<Long>();

	for(int i=0; i<12; i++) {
	    sums.add(new Long(getOutgoByMonthLong(year, i)));
	}
	return sums;
    }

    /** 
     * This method gives the result summaries for a specific year,
     * ie a summery for January, one for February and so on.
     * Resulting in a 12 item long list.
     *
     * @return ArrayList
     */
    public ArrayList getResultByYear(int year) {
	ArrayList sumsi = new ArrayList();
	ArrayList sumso = new ArrayList();
	ArrayList<Double> sums = new ArrayList<Double>();

	sumsi = getIncomesByYearLong(year);
	sumso = getOutgoByYearLong(year);
	for(int i=0; i<12; i++) {
	    long in, out, res;
	    double dres;
	    in = ((Long)sumsi.get(i)).longValue();
	    out = ((Long)sumso.get(i)).longValue();
	    res = in - out;
	    dres = res;
	    sums.add( new Double(dres/100.0));
	}
	return sums;
    }

    /** 
     * This method gives the names of all income categories.
     * Categories like "lön", "barnbidrag".
     *
     * @return ArrayList
     */
    public ArrayList getIncomeCategories() {
	final ArrayList<String> names = new ArrayList<String>();
	Category c;

	for(int i=0; i<categories.size(); i++) {
	    c = categories.get(i);
	    if(c.isIncome()) {
		names.add(c.getName());
	    }
	}
	return names;
    }

    /** 
     * This method gives the names of all outgo categories.
     * Categories like "amortering", "livsmedel".
     *
     * @return ArrayList
     */
    public ArrayList getOutgoCategories() {
	final ArrayList<String> names = new ArrayList<String>();
	Category c;

	for(int i=0; i<categories.size(); i++) {
	    c = categories.get(i);
	    if(!c.isIncome()) {
		names.add(c.getName());
	    }
	}
	return names;
    }

    /** 
     * This method.
     *
     * @return ArrayList
     */
    public ArrayList<String> getUsers() {
	final ArrayList<String> names = new ArrayList<String>();

	for(int i=0; i<users.size(); i++) {
	    names.add( ( users.get(i)).getName());
	}
	return names;
    }

    /** 
     * This method.
     *
     * @return ArrayList
     */
    public ArrayList getTypes() {
	return types;
    }

    /** 
     * This method.
     *
     * @param accountName
     * @return double
     */
    public double getInitialBalance(final String accountName) {
	final int i = findAccountIndex(accountName);
	return (double) ((accounts.get(i)).getInitialBalance() / 100);
    }

    /** 
     * This method.
     *
     * @param accountName String
     * @return long
     */
    private long getInitialBalanceLong(final String accountName) {
	final int i = findAccountIndex(accountName);
	return (accounts.get(i)).getInitialBalance();
    }

    /** 
     * This method.
     *
     * @param accountName String
     * @return int
     */
    private int findAccountIndex(final String accountName) {
	int val=-1;
	
	for(int i=0; i<accounts.size(); i++) {
	    if(accountName.equals( ( accounts.get(i)).getTitle())) {
		val = i;
		break;
	    }
	}
	if(val == -1) {
	    System.out.println("findAccountIndex error: could not find id for >"+accountName+'<');
	}
	return val;
    }
    
    /** 
     * This method.
     *
     * @param accountName String
     * @return long
     */
    private long findAccountId(final String accountName) {
	long val=(long) -1;
	
	for(int i=0; i<accounts.size(); i++) {
	    if(accountName.equals((accounts.get(i)).getTitle())) {
		val = (accounts.get(i)).getId();
		break;
	    }
	}
	if(val == -1) {
	    if(!accountName.equals("---")) {
		if(!accountName.equals("")) {
		    System.out.println("findAccountID error: could not find id for >"+accountName+'<');
		    //Thread.dumpStack();
		}
	    }
	}
	return val;
    }

    /** 
     * This method adds an account to the accountlist if no such account
     * already exists.
     *
     * @param accountName String
     * @return long
     */
    private long makeAccountId(final String accountName) {
	long id;

	id = findAccountId(accountName);
	if(id == -1) {
	    if((!accountName.equals("---")) &&
		(!accountName.equals(""))) {
		System.out.println("Creating ID for account >"+accountName+'<');
		addAccount(accountName, "", (long) 0);
		id = findAccountId(accountName);
	    }
	    dirty=true;
	}
	return id;
    }

    /** 
     * This method.
     *
     * @param placeName String
     * @return long
     */
    private long findPlaceId(final String placeName) {
	long val=(long) -1;
	
	for(int i=0; i<places.size(); i++) {
	    if(placeName.equals((places.get(i)).getName())) {
		val = (places.get(i)).getId();
		break;
	    }
	}
	if(val == -1) {
	    if(!placeName.equals("---")) {
		System.out.println("findPlaceID error: could not find id for >"+placeName+'<');
		// 	    System.out.println("Existing places:");
		// 	    for(int i=0; i<places.size(); i++) {
		// 		System.out.println("--"+( (Place)places.get(i)).getName()+"--");
		// 	    }
		// 	    System.out.println("No more places");
		//		Thread.dumpStack();
	    }
	}
	return val;
    }

    /** 
     * This method.
     *
     * @param placeName String
     * @return long
     */
    private long makePlaceId(final String placeName) {
	long id = findPlaceId(placeName);
	if(id == -1) {
	    if(!placeName.equals("---")) {
		System.out.println("Creating ID for place >"+placeName+'<');
		addPlace(placeName, "", false);
		id = findPlaceId(placeName);
	    }
	    dirty=true;
	}
	return id;
    }
    
    /** 
     * This method.
     *
     * @param accountId long
     * @return String
     */
    private String findAccountName(final long accountId) {
	String val=null;
	
	if(accountId == -1) {
	    val = "---";
	} else {
	    for(int i=0; i<accounts.size(); i++) {
		if(((accounts.get(i)).getId()) == accountId) {
		    val = (accounts.get(i)).getTitle();
		    break;
		}
	    }
	}
	if(val == null) {
	    System.out.println("findAccountName error: could not find id for >"+accountId+'<');
	}
	return val;
    }
    
    /** 
     * This method.
     *
     * @param placeId long
     * @return String
     */
    private String findPlaceName(final long placeId) {
	String val=null;
	
	if(placeId == -1) {
	    val = "---";
	} else {
	    for(int i=0; i<places.size(); i++) {
		if(((places.get(i)).getId()) == placeId) {
		    val = (places.get(i)).getName();
		    break;
		}
	    }
	}
	if(val == null) {
	    System.out.println("findPlaceName error: could not find name for >"+placeId+'<');
	}
	return val;
    }
    
    /** 
     * This method.
     *
     * @param categoryId long
     * @return String
     */
    private String findCategoryName(final long categoryId) {
	String val=null;
	
	if(categoryId == -1) {
	    val = "---";
	} else {
	    for(int i=0; i<categories.size(); i++) {
		if(((categories.get(i)).getId()) == categoryId) {
		    val = (categories.get(i)).getName();
		    break;
		}
	    }
	}
	if(val == null) {
	    System.out.println("findCategoryName error: could not find name for >"+categoryId+'<');
	}
	return val;
    }
    
    /** 
     * This method.
     *
     * @param categoryId long
     * @return boolean - true is income, false is outgo
     */
    private boolean findCategoryType(final long categoryId) {
	boolean val=false;
	
	if(categoryId == -1) {
	    val = false;
	} else {
	    for(int i=0; i<categories.size(); i++) {
		if(((categories.get(i)).getId()) == categoryId) {
		    val = (categories.get(i)).isIncome();
		    break;
		}
	    }
	}
	return val;
    }
    
    /** 
     * This method.
     *
     * @param typeId long
     * @return String
     */
    private String findTypeName(final long typeId) {
	final String val;
	
	if(typeId == -1) {
	    val = "---";
	} else {
	    // WARNING: Not good using a long to int cast
	    // TBD: get rid of that cast
	    val = types.get((int)typeId);
	}
	return val;
    }
    
    /** 
     * This method.
     *
     * @param userId long
     * @return String
     */
    private String findUserName(final long userId) {
	String val=null;
	
	if(userId == -1) {
	    val = "---";
	} else {
	    for(int i=0; i<users.size(); i++) {
		if(((users.get(i)).getId()) == userId) {
		    val = (users.get(i)).getName();
		    break;
		}
	    }
	}
	if(val == null) {
	    System.out.println("findUserName error: could not find name for >"+userId+'<');
	}
	return val;
    }
    
    /** 
     * This method.
     *
     * @param typeName String
     * @return long
     */
    private long findTypeId(final String typeName) {
	long val=(long) -1;
	
	for(int i=0; i<types.size(); i++) {
	    if(typeName.equals( types.get(i))) {
		val = (long) i;
		break;
	    }
	}
	if(val == -1) {
	    if(!typeName.equals("---")) {
		System.out.println("findTypeID error: could not find id for >"+typeName+'<');
		//		Thread.dumpStack();
	    }
	}
	return val;
    }
    
    /** 
     * This method.
     *
     * @param categoryName String
     * @return long
     */
    private long findCategoryId(final String categoryName) {
	long val=(long) -1;
	
	for(int i=0; i<categories.size(); i++) {
	    if(categoryName.equals((categories.get(i)).getName())) {
		val = (categories.get(i)).getId();
		break;
	    }
	}
	if(val == -1) {
	    if(!categoryName.equals("---")) {
		System.out.println("findCategoryId error: could not find ID for >"+categoryName+'<');
		//		Thread.dumpStack();
	    }
	}
	//System.out.println("For category >"+categoryName+"< I found id#"+val);
	return val;
    }
    
    /** 
     * This method tries to find the id for a category. If not found
     * a new one is created.
     *
     * @param categoryName String
     * @return long
     */
    private long makeCategoryId(final String categoryName) {
	long val=findCategoryId(categoryName);
	if((val == -1) && (!categoryName.equals("---"))){
	    System.out.println("Creating ID for category >"+categoryName+'<');
	    addCategory(categoryName, "Inkomst");
	    val=findCategoryId(categoryName);
	    dirty=true;
	}
	return val;
    }
    
    /** 
     * This method.
     *
     * @param userName String
     * @return long
     */
    private long findUserId(final String userName) {
	long val=(long) -1;
	
	for(int i=0; i<users.size(); i++) {
	    if(userName.equals((users.get(i)).getName())) {
		val = (users.get(i)).getId();
		break;
	    }
	}
	if(val == -1) {
	    if(!userName.equals("---")) {
		System.out.println("findUserId error: could not find ID for >"+userName+'<');
		//		Thread.dumpStack();
	    }
	}
	return val;
    }
    
    /** 
     * This method.
     *
     * @param userName String
     * @return long
     */
    private long makeUserId(final String userName) {
	long id;
	
	id = findUserId(userName);
	if(id == -1) {
	    if(!userName.equals("---")) {
		addPerson(userName, "1900", "-");
		id = findUserId(userName);
	    }
	    dirty=true;
	}
	return id;
    }
    
    /** 
     * This method.
     *
     * @param accountName String
     * @return double
     */
    public double getTotalBalance(final String accountName) {
	long balance;
        final long accountId;
        Transaction t;
	
	balance=getInitialBalanceLong(accountName);
	accountId=findAccountId(accountName);
	
	for(int i=0; i<transactions.size(); i++) {
	    t = transactions.get(i);
	    if(t.getSource() == accountId) {
		balance-=t.getAmount();
	    } else if (t.getDestination() == accountId) {
		balance+=t.getAmount();
	    }
	}

	return ( (double) balance)/(double) 100;
    }
    
    /** 
     * This method.
     *
     * @param accountName String
     * @return long
     */
    public long getNumberOfTransactions(final String accountName) {
	final long accountId;
        long numtrans;
        Transaction t;
	
	accountId=findAccountId(accountName);

	numtrans = (long) 0;
	for(int i=0; i<transactions.size(); i++) {
	    t = transactions.get(i);
	    if ( (t.getSource() == accountId) ||
		 (t.getDestination() == accountId) ) {
		numtrans++;
	    }
	}
	
	return numtrans;
    }
    
    /** 
     * This method.
     *
     * @return long
     */
    public long getNumberOfTransactions() {
	return (long) transactions.size();
    }
    
    /** 
     * This method.
     *
     * @return long
     */
    public long getNumberOfPlaces() {
	return (long) places.size();
    }
    
    /** 
     * This method.
     *
     * @return long
     */
    public long getNumberOfUsers() {
	return (long) users.size();
    }
    
    /** 
     * This method.
     *
     * @return long
     */
    public long getNumberOfCategories() {
	return (long) categories.size();
    }
    
    /** 
     * This method.
     *
     * @return long
     */
    public long getNumberOfAccounts() {
	return (long) accounts.size();
    }
    
    /** 
     * This method.
     *
     * @return long
     */
    public long getNumberOfTypes() {
	return (long) types.size();
    }
    
    /** 
     * This method.
     *
     * @return long
     */
    public long getNumberOfTransfers() {
	return (long) transfers.size();
    }
    
    /** 
     * This method.
     *
     * @return long
     */
    public long getNumberOfBudgetLines() {
	return (long) budgetLines.size();
    }
    
    /** 
     * This method.
     *
     * @param accountName
     * @return Date
     */
    public Date getDateOfOldestTransaction(final String accountName) {
	final long accountId;
	Date date=null;
	Transaction t;
	
	accountId=findAccountId(accountName);

	for(int i=0; i<transactions.size(); i++) {
	    t = transactions.get(i);
	    if ( (t.getSource() == accountId) ||
		 (t.getDestination() == accountId) ) {
		if (date == null) {
		    date = t.getDate();
		} else if ( (t.getDate().before(date)) ) {
		    date = t.getDate();
		}
	    }
	}
	
	return date;
    }
    
    /** 
     * This method.
     *
     * @return Date
     */
    public Date getDateOfOldestTransaction() {
	Date date=null;
	Transaction t;
	
	for(int i=0; i<transactions.size(); i++) {
	    t = transactions.get(i);
	    if (date == null) {
		date = t.getDate();
	    } else if ( (t.getDate().before(date)) ) {
		date = t.getDate();
	    }
	}
	
	return date;
    }
    
    /** 
     * This method.
     *
     * @param accountName String
     * @return Date
     */
    public Date getDateOfNewestTransaction(final String accountName) {
	final long accountId;
	Date date=null;
	Transaction t;
	
	accountId=findAccountId(accountName);
	for(int i=0; i<transactions.size(); i++) {
	    t = transactions.get(i);
	    if ( (t.getSource() == accountId) ||
		 (t.getDestination() == accountId) ) {
		if(date == null) {
		    date = t.getDate();
		} else if ( (t.getDate().after(date)) ) {
		    date = t.getDate();
		}
	    }
	}
	
	return date;
    }
    
    /** 
     * This method.
     *
     * @return Date
     */
    public Date getDateOfNewestTransaction() {
	Date date=null;
	Transaction t;
	
	for(int i=0; i<transactions.size(); i++) {
	    t = transactions.get(i);
	    if(date == null) {
		date = t.getDate();
	    } else if ( (t.getDate().after(date)) ) {
		date = t.getDate();
	    }
	}
	
	return date;
    }
    
    /** 
     * This method.
     *
     */
    private void sortTransactionsByDate() {
	int i;
        int j;
        Transaction t1;
        Transaction t2;
        boolean finished;
	
	for(i=0; i<transactions.size(); i++) {
	    finished = true;
	    for(j=1; j<transactions.size(); j++) {
		t1 = transactions.get(j-1);
		t2 = transactions.get(j);
	    
		if (t2.getDate().before(t1.getDate())) {
		    transactions.set(j-1, t2);
		    transactions.set(j, t1);
		    finished = false;
		}
	    }
	    if (finished) {
		i = transactions.size(); /* break out of loop */
	    }
	}
    }
    
    /** 
     * This method.
     *
     */
    private void sortBudgetlinesByName() {
	int i;
        int j;
        BudgetLine bl1;
        BudgetLine bl2;
        boolean finished;
	String name1;
	String name2;

	for(i=0; i<budgetLines.size(); i++) {
	    finished = true;
	    for(j=1; j<budgetLines.size(); j++) {
		bl1 = budgetLines.get(j-1);
		name1 = findCategoryName(bl1.getCategory());
		bl2 = budgetLines.get(j);
		name2 = findCategoryName(bl2.getCategory());
	    
		if (name1.compareToIgnoreCase(name2)>0) {
		    budgetLines.set(j-1, bl2);
		    budgetLines.set(j, bl1);
		    finished = false;
		}
	    }
	    if (finished) {
		i = budgetLines.size(); /* break out of loop */
	    }
	}
    }
    
    /** 
     * This method.
     *
     * @param accountName String
     * @param startDate Date
     * @param endDate Date
     * @return ArrayList2D
     */
    public ArrayList2D dumpAccount(final String accountName, final Date startDate, final Date endDate) {
	if(accountName == null) {
	    return null;
	} else {
	    //System.out.println("Dumpar konto "+accountName+" från "+startDate+" till "+endDate);
	    
	    long balance;
	    final long accountId;
	    Transaction t;
	    final ArrayList2D data = new ArrayList2D();
	    int line=0;
	    StringBuffer dateBuffer;
	    
	    sortTransactionsByDate();
	    balance=getInitialBalanceLong(accountName);
	    accountId=findAccountId(accountName);
	    
	    for(int i=0; i<transactions.size(); i++) {
		t = transactions.get(i);
		if(t.getSource() == accountId) {
		    balance-=t.getAmount();
		} else if (t.getDestination() == accountId) {
		    balance+=t.getAmount();
		}
		if( (startDate == null) ||
		    (startDate.before(t.getDate()))) {
		    if( (endDate == null) ||
			(endDate.after(t.getDate()))) {
			if( (t.getSource() == accountId) ||
			    (t.getDestination() == accountId) ) {
			    data.set(0, line, findAccountName(t.getSource()));
			    data.set(1, line, findAccountName(t.getDestination()));
			    data.set(2, line, findTypeName(t.getType()));
			    data.set(3, line, findPlaceName(t.getPlace()));
			    dateBuffer = new StringBuffer();
			    df.format(t.getDate(), dateBuffer, new FieldPosition(0));
			    data.set(4, line, new String(dateBuffer));
			    data.set(5, line, findCategoryName(t.getCategory()));
			    data.set(6, line, findUserName(t.getUser()));
			    data.set(7, line, new Double( ((double)t.getAmount())/(double) 100));
			    data.set(8, line, new Double( ((double)balance)/(double) 100));
			    data.set(9, line, t.getComment());
			    line++;
			}
		    }
		}
	    }
	    return data;
	}
    }
    
    /** 
     * This method.
     *
     * @param accountNames Object[]
     * @param startDate Date
     * @param endDate Date
     * @return ArrayList2D
     */
    public ArrayList2D getTransactions(
				       final Object[] accountNames,
				       final boolean allAccounts,
				       final Object[] catNames,
				       final boolean allCategories,
				       final Object[] userNames,
				       final boolean allUsers,
				       final Object[] placeNames,
				       final boolean allPlaces,
				       final Date startDate,
				       final Date endDate) {
	Transaction t;
	final ArrayList2D data = new ArrayList2D();
	int line=0;
	StringBuffer dateBuffer;
	
	sortTransactionsByDate();

	TreeSet<Long> accountIds = new TreeSet<Long>();
	for(int i=0; i<accountNames.length; i++) {
	    accountIds.add(findAccountId((String) accountNames[i]));
	}
	TreeSet<Long> catIds = new TreeSet<Long>();
	for(int i=0; i<catNames.length; i++) {
	    catIds.add(findCategoryId((String) catNames[i]));
	}
	TreeSet<Long> userIds = new TreeSet<Long>();
	for(int i=0; i<userNames.length; i++) {
	    userIds.add(findUserId((String) userNames[i]));
	}
	TreeSet<Long> placeIds = new TreeSet<Long>();
	for(int i=0; i<placeNames.length; i++) {
	    placeIds.add(findPlaceId((String) placeNames[i]));
	}
	
	for(int i=0; i<transactions.size(); i++) {
	    t = transactions.get(i);
	    if( (startDate == null) ||
		(startDate.before(t.getDate()))) {
		if( (endDate == null) ||
		    (endDate.after(t.getDate()))) {
		    if( allAccounts ||
			(accountIds.contains(t.getSource())) ||
			(accountIds.contains(t.getDestination()))) {
			if(allCategories || catIds.contains(t.getCategory())) {
			    if(allUsers || userIds.contains(t.getUser())) {
				if(allPlaces || placeIds.contains(t.getPlace())) {
				    data.set(0, line, findAccountName(t.getSource()));
				    data.set(1, line, findAccountName(t.getDestination()));
				    data.set(2, line, findTypeName(t.getType()));
				    data.set(3, line, findPlaceName(t.getPlace()));
				    dateBuffer = new StringBuffer();
				    df.format(t.getDate(), dateBuffer, new FieldPosition(0));
				    data.set(4, line, new String(dateBuffer));
				    data.set(5, line, findCategoryName(t.getCategory()));
				    data.set(6, line, findUserName(t.getUser()));
				    data.set(7, line, new Double( ((double)t.getAmount())/(double) 100));
				    data.set(8, line, t.getComment());
				    line++;
				}
			    }
			}
		    }
		}
	    }
	}
	return data;
    }
    
    /** 
     * This method give the balance summary for a year (day by day) and a
     * number of accounts
     *
     * @param accountNames String[]
     * @param year int
     * @return ArrayList
     */
    public ArrayList dumpAccountBalance(final String accountNames[], final int year) {
	StringBuffer dateBuffer;
	final GregorianCalendar monthDate;

	//System.out.println("Dumpar ... "+accountName()+"..."+year);
	
	final ArrayList<Double> data = new ArrayList<Double>();
	Date datum;

	monthDate = new GregorianCalendar();
	monthDate.setTime (new Date());	/* Initialize to todays date */
	monthDate.set(Calendar.YEAR, year);
	
	for(int j=0; j<12; j++) {
	    monthDate.set(Calendar.MONTH, j);
	    
	    for(int i=1; i<32; i++) {
		monthDate.set(Calendar.DAY_OF_MONTH, i);
		datum = monthDate.getTime();
		//System.out.println("Konto:>"+konto+"< "+datum+" saldo: "+ getBalance(konto, datum));
		dateBuffer = new StringBuffer();
		df.format(datum, dateBuffer, new FieldPosition(0));
		//System.out.println("account:\""+accountNames[0]+"\"");
		double sum=0;
		for(int k=0; k<accountNames.length; k++) {
		    sum+=getBalance(accountNames[k], datum);
		}
		data.add(new Double(sum));
	    }
	}
	return data;
    }
    
    /** 
     * This method.
     *
     * @param accountName String
     * @param monthDate G.Calendar
     * @return ArrayList2D
     */
    public ArrayList2D dumpAccountBalance(final String accountName, final GregorianCalendar monthDate) {
	StringBuffer dateBuffer;

	if(accountName == null) {
	    return null;
	} else {
	    //System.out.println("Dumpar konto "+accountName+" för månad med datum "+monthDate.getTime());

	    final ArrayList2D data = new ArrayList2D();
	    Date datum;
	    for(int i=1; i<32; i++) {
		monthDate.set(Calendar.DAY_OF_MONTH, i);
		datum = monthDate.getTime();
		//System.out.println("Konto:>"+konto+"< "+datum+" saldo: "+ getBalance(konto, datum));
		dateBuffer = new StringBuffer();
		df.format(datum, dateBuffer, new FieldPosition(0));

		data.set(0, i-1, dateBuffer);
		data.set(1, i-1, ""+getBalance(accountName, datum));
	    }
	    return data;
	}
    }
    
    /** 
     * This method.
     *
     * @param line int
     * @param data ArrayList2D
     * @param budgetLine BudgetLine
     */
    private void dumpBudgetLine(final ArrayList2D data, final BudgetLine budgetLine, final int line) {
	final long cid;
	int summary=0;
	cid = budgetLine.getCategory();

	data.set(0, line, findCategoryName(cid));
	for(int j=0; j<12; j++) {
	    summary+=(int) budgetLine.getMonthValue(j);
	}
	data.set(1, line, new Double( ((double)summary)/(double) 100));
	for(int j=0; j<12; j++) {
	    data.set(2+j, line,
		     new Double(
				((double)budgetLine.getMonthValue(j))/(double) 100));
	}
    }

    /** 
     * This method.
     *
     * @param searchstring string
     * @return ArrayList2D
     */
    public ArrayList2D stringSearch(final String value) {
	final ArrayList2D data = new ArrayList2D();
	Transaction t;
	String txt;
	String match = value.toLowerCase();
	int co=0;
	StringBuffer dateBuffer;

	//	System.out.println("Söker efter "+value);
	for(int i=0; i<transactions.size(); i++) {
	    t = transactions.get(i);
	    txt = t.getComment().toLowerCase();
	    if(txt.indexOf(match) != -1) {
		//		System.out.println("==>"+txt);
		data.set(0, co, new Integer(co));
		dateBuffer = new StringBuffer();
		df.format(t.getDate(), dateBuffer, new FieldPosition(0));
		data.set(1, co, new String(dateBuffer));
		data.set(2, co, new Double( ((double)t.getAmount())/(double) 100));
		data.set(3, co, t.getComment());
		co++;
	    }
	}
	//System.out.println("Found " + co + "items");
	return data;
    }
    
    /** 
     * This method.
     *
     * @param year int
     * @return ArrayList2D
     */
    public ArrayList2D dumpBudget(final int year) {
	System.out.println("Dumpar budget för år "+year+" dvs innevarande...");
	
	sortBudgetlinesByName();

	final ArrayList2D data = new ArrayList2D();
	int line=0;
	BudgetLine budgetLine;
	boolean income=false;
	long cid;

	for(int i=0; i<budgetLines.size(); i++) {
	    budgetLine = budgetLines.get(i);
	    cid = budgetLine.getCategory();
	    income = findCategoryType(cid);
	    if(income) {
		dumpBudgetLine(data, budgetLine, line);
		line++;
	    }
	}
	for(int j=0; j<14; j++) {
	    data.set(j, line, " ");
	}
	line++;
	for(int i=0; i<budgetLines.size(); i++) {
	    budgetLine = budgetLines.get(i);
	    cid = budgetLine.getCategory();
	    income = findCategoryType(cid);
	    if(!income) {
		dumpBudgetLine(data, budgetLine, line);
		line++;
	    }
	}
	return data;
    }
    
    /** 
     * This method.
     *
     * @param line int
     * @param data ArrayList2D
     * @param budgetLine BudgetLine
     */
    private void clearResultLine(ArrayList2D data, final BudgetLine budgetLine, final int line) {
	final long cid;
	int summary=0;
	cid = budgetLine.getCategory();

	data.set(0, line, findCategoryName(cid));
	data.set(1, line, new Double( ((double)0)));
	for(int j=0; j<12; j++) {
	    data.set(3+j, line,
		     new Double(
				((double)0)));
	}
    }

    /** 
     * This method add upp all transactions for this category and month
     *
     * @param line int
     * @param data ArrayList2D
     * @param budgetLine BudgetLine
     */
    private void dumpResultLine(ArrayList2D data, final BudgetLine budgetLine, final int line, final int month, final ArrayList transactions) {
	final long cid;
	long summary=0;
	cid = budgetLine.getCategory();
        Transaction t;

	// Add upp all transactions for this category and month
	for (int i=0; i<transactions.size(); i++) {
	    t =  (Transaction)transactions.get(i);
	    if(t.getCategory() == cid)
		summary += t.getAmount();
	}
	data.set(3+month, line, new Double(((double)summary)/(double)100));
    }

    /** 
     * This method.
     *
     * @param year int
     * @return ArrayList2D
     */
    public ArrayList2D dumpResult(final int year) {
	System.out.println("Dumpar resultat för år "+year);
	
	sortBudgetlinesByName();

	ArrayList2D data = new ArrayList2D();
	int line=0;
	BudgetLine budgetLine;
	boolean income=false;
	long cid;
	double summary;
	double value;

	// create lines for incomes
	for(int i=0; i<budgetLines.size(); i++) {
	    budgetLine = budgetLines.get(i);
	    cid = budgetLine.getCategory();
	    income = findCategoryType(cid);
	    if(income) {
		clearResultLine(data, budgetLine, line);
		line++;
	    }
	}
	// create empty spacer line in table
	for(int j=0; j<15; j++) {
	    data.set(j, line, " ");
	}
	// create lines for expenses
	line++;
	for(int i=0; i<budgetLines.size(); i++) {
	    budgetLine = budgetLines.get(i);
	    cid = budgetLine.getCategory();
	    income = findCategoryType(cid);
	    if(!income) {
		clearResultLine(data, budgetLine, line);
		line++;
	    }
	}
	// fill table with data
	for(int month=0; month<12; month++) {
	    final ArrayList transactions = getTransactionsByMonth(year, month);
	    line=0;
	    
	    if(transactions.size() > 0) {
		// create lines for incomes
		for(int i=0; i<budgetLines.size(); i++) {
		    budgetLine = budgetLines.get(i);
		    cid = budgetLine.getCategory();
		    income = findCategoryType(cid);
		    if(income) {
			dumpResultLine(data, budgetLine, line, month, transactions);
			line++;
		    }
		}
		// create lines for expenses
		line++;
		for(int i=0; i<budgetLines.size(); i++) {
		    budgetLine = budgetLines.get(i);
		    cid = budgetLine.getCategory();
		    income = findCategoryType(cid);
		    if(!income) {
			dumpResultLine(data, budgetLine, line, month, transactions);
			line++;
		    }
		}
	    }
	}
	// Calculate totals and avarages
	line=0;
	for(int i=0; i<budgetLines.size(); i++) {
	    budgetLine = budgetLines.get(i);
	    cid = budgetLine.getCategory();
	    income = findCategoryType(cid);
	    if(income) {
		summary = 0;
		for (int month=0; month<12; month++) {
		    try {
			value = (double) ((Double)data.get(3+month, line));
		    } catch(ClassCastException e) {
			System.out.println("Not a double:"+data.get(3+month, line));
			value=0;
		    }
		    summary += value;
		}
		data.set(1, line, new Double(summary));
		data.set(2, line, new Double(summary/12));
		line++;
	    }
	}
	// create lines for expenses
	line++;
	for(int i=0; i<budgetLines.size(); i++) {
	    budgetLine = budgetLines.get(i);
	    cid = budgetLine.getCategory();
	    income = findCategoryType(cid);
	    if(!income) {
		summary = 0;
		for (int month=0; month<12; month++) {
		    try {
			value = (double) ((Double)data.get(3+month, line));
		    } catch(ClassCastException e) {
			System.out.println("Not a double:"+data.get(3+month, line));
			value=0;
		    }
		    summary += value;
		}
		data.set(1, line, new Double(summary));
		data.set(2, line, new Double(summary/12));
		line++;
	    }
	}

	return data;
    }
    
    /** 
     * This method.
     *
     * @return ArrayList2D
     */
    public ArrayList2D dumpTransfers() {
	System.out.println("Dumpar transfers");
	
	final ArrayList2D data = new ArrayList2D();
	int line=0;
	Transfer t;
	StringBuffer dateBuffer;
	
	for(int i=0; i<transfers.size(); i++) {
	    t = transfers.get(i);
	    
	    data.set(0, line, findAccountName(t.getSource()));
	    data.set(1, line, findAccountName(t.getDestination()));
	    data.set(2, line, new Double( ((double)t.getAmount())/(double) 100));

	    dateBuffer = new StringBuffer();
	    df.format(t.getDate(), dateBuffer, new FieldPosition(0));
	    data.set(3, line, new String(dateBuffer));

	    if(t.getEndDate() != null) {
		dateBuffer = new StringBuffer();
		df.format(t.getEndDate(), dateBuffer, new FieldPosition(0));
		data.set(4, line, new String(dateBuffer));
	    } else {
		data.set(4, line, "<none>");
	    }

	    data.set(5, line, new Integer(t.getFrequency()));
	    data.set(6, line, findCategoryName(t.getCategory()));
	    data.set(7, line, findUserName(t.getUser()));
	    
	    line++;
	}
	return data;
    }
    
    /** 
     * This method.
     *
     * @param accountName String
     * @param day Date
     * @return double
     */
    private double getBalance(final String accountName, final Date day) {
	long balance;
	balance = getBalanceLong(accountName, day);
	return ( (double) balance)/(double) 100;
    }
    
    /** 
     * This method.
     *
     * @param AccountName String
     * @return double
     */
    public double getTodayBalance(final String AccountName) {
	final Date today=new Date();

	return getBalance(AccountName, today);
    }
    
    /** 
     * This method.
     *
     * @param accountName String
     * @param day Date
     * @return long
     */
    private long getBalanceLong(final String accountName, final Date day) {
	long balance;
        final long accountId;
        Transaction t;
	
	balance=getInitialBalanceLong(accountName);
	accountId=findAccountId(accountName);
	
	for(int i=0; i<transactions.size(); i++) {
	    t = transactions.get(i);
	    if( t.getDate().before(day)) {
		if(t.getSource() == accountId) {
		    balance-=t.getAmount();
		} else if (t.getDestination() == accountId) {
		    balance+=t.getAmount();
		}
	    }
	}

	return balance;
    }
    
    /** 
     * This method.
     *
     * @param AccountName
     * @return long
     */
    public long getTodayBalanceLong(final String AccountName) {
	final Date today=new Date();

	return getBalanceLong(AccountName, today);
    }
    
    /** 
     * This method.
     *
     * @param filename String
     */
    public void importHEK(final String filename) {
	final File file = new File(filename);
	importHEK(file);
	dirty=false;
    }
    
    /** 
     * This method.
     *
     * @param file File
     */
    public void importHEK(final File file) {
	try{
	    Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
	    System.out.println("Opening file: "+file.getPath());
	    Connection con=null;
	    Boolean DB_found = false;
	    // Note: name of driver is found at: Control Panel -> Administrative tools -> Data Sources (ODBC) -> Drivers
	    // (at least in XP and Windows 7)
	    try {
		//standard XP SP3
		con = DriverManager.getConnection("jdbc:odbc:Driver={Microsoft Access Driver (*.mdb)};dbq="+file.getPath());
		DB_found = true;
	    }catch(SQLException sqle){
		System.out.println("No driver: Microsoft Access Driver (*.mdb)");
	    }
	    if (!DB_found) {
		try {
		    // when Access is installed?
		    con = DriverManager.getConnection("jdbc:odbc:MS Access Database;DBQ="+file.getPath());
		    DB_found = true;
		}catch(SQLException sqle){
		    System.out.println("No driver: MS Access Database");
		    //sqle.printStackTrace();
		}
	    }

	    if (!DB_found) {
		try {
		    // MDAC25 ?
		    con = DriverManager.getConnection("jdbc:odbc:Driver={MS Access Driver (*.mdb)};dbq="+file.getPath());
		    DB_found = true;
		}catch(SQLException sqle){
		    System.out.println("No driver: MS Access Driver (*.mdb)");
		    //sqle.printStackTrace();
		}
	    }

	    if (DB_found) {
		final Statement stmt = con.createStatement();
		
		System.out.println("Importing Accounts");
		importHEKaccounts(stmt);
		System.out.println("Importing Users");
		importHEKusers(stmt);
		System.out.println("Importing Types");
		importHEKtypes(stmt);
		System.out.println("Importing Categories");
		importHEKcategories(stmt); // must be before transactions
		System.out.println("Importing Places");
		importHEKplaces(stmt); // must be before transactions
		System.out.println("Importing Transactions");
		importHEKtransactions(stmt);
		System.out.println("Importing Budget");
		importHEKbudgets(stmt);
		System.out.println("Importing Transfers");
		importHEKtransfers(stmt);
		
		dirty=true;
	    }
	}catch(SQLException sqle){
	    JOptionPane.showMessageDialog(null,
					  "Drivrutin för Access-databas saknas troligen. Eller möjligen en trasig fil.");
	    sqle.printStackTrace();
	}catch(ClassNotFoundException cnfe){
	    JOptionPane.showMessageDialog(null,
					  "Saknar ODBC eller JDBC eller Access Driver.");
	    cnfe.printStackTrace();
	}
	dirty=true;
    }

    /** 
     * This method.
     *
     * @param stmt Statement
     */
    private void importHEKaccounts(final Statement stmt) {
	try {
	    // Get the Accounts-table
	    final String sq = "SELECT * FROM Konton";
	    final ResultSet rst = stmt.executeQuery(sq);
	    
	    while(rst.next()){
		final Account account = new Account();
		account.setId(rst.getLong("Löpnr"));
		account.setAccountNumber(rst.getString("KontoNummer"));
		account.setTitle(rst.getString("Benämning"));
		account.setBalance(double2Long(rst.getDouble("Saldo")));
		account.setInitialBalance(double2Long(rst.getDouble("StartSaldo")));
		account.setStartMonth(month2int(rst.getString("StartManad")));
		account.setYearBalance(double2Long(rst.getDouble("SaldoArsskifte")));
		account.setYearChange(month2int(rst.getString("ArsskifteManad")));
		if(account.getId() > maxIdAccounts) {
		    maxIdAccounts = account.getId();
		}
		accounts.add(account);
	    }
	}catch(SQLException sqle){
	    sqle.printStackTrace();
	}
    }
    
    /** 
     * This method.
     *
     * @param stmt Statement
     */
    private void importHEKusers(final Statement stmt) {
	try {
	    // Get the Users-table
	    final String sq = "SELECT * FROM Personer";
	    final ResultSet rst = stmt.executeQuery(sq);
	    
	    while(rst.next()){
		final User user = new User();
		user.setId(rst.getLong("Löpnr"));
		user.setName(rst.getString("Namn"));
		user.setBorn(rst.getString("Född"));
		user.setSex(rst.getString("Kön"));

		if(user.getId() > maxIdUsers) {
		    maxIdUsers = user.getId();
		}
		users.add(user);
	    }
	}catch(SQLException sqle){
	    sqle.printStackTrace();
	}
    }

    /** 
     * This method creates the table "categories".
     * The categories table is used to classify transactions whether it
     * wages, taxes, childcare, car and such.
     *
     * @param stmt Statement
     */
    private void importHEKcategories(final Statement stmt) {
	int i=1;
	
	try {
	    // Get the categories
	    final String sq = "SELECT Typ, Inkomst FROM Budget";
	    final ResultSet rst = stmt.executeQuery(sq);
	    
	    while(rst.next()){
		final Category category = new Category();
		category.setId((long) i);
		category.setName(rst.getString("Typ"));
		category.setIncome(rst.getString("Inkomst"));

		if(category.getId() > maxIdCategories) {
		    maxIdCategories = category.getId();
		}
		categories.add(category);
		i++;
	    }
	}catch(SQLException sqle){
	    sqle.printStackTrace();
	}
    }

     /** 
     * This method creates the table "types".
     * The types table is used to classify transactions regarding whether
     * is is a buy, sell, deposit, withdrawal or transfer of funds between
     * accounts.
     *
     * @param stmt Statement
     */
    private void importHEKtypes(final Statement stmt) {
	types.add("Inköp");
	types.add("Insättning");
	types.add("Uttag");
	types.add("Överföring");
    }
    
    /** 
     * This method creates the table "transactions" from HEK-file.
     *
     * @param stmt Statement
     */
    private void importHEKtransactions(final Statement stmt) {
	String name;

	try {
	    // Get the transactions-table
	    final String sq = "SELECT * FROM Transaktioner";
	    final ResultSet rst = stmt.executeQuery(sq);
	    
	    while(rst.next()){
		final Transaction transaction = new Transaction();
		transaction.setId(rst.getLong("Löpnr"));
		transaction.setSource(makeAccountId(rst.getString("FrånKonto")));
		
		name = rst.getString("Typ");
		if(name.equals("Fast Inkomst")) {
		    name = "Insättning";
		}
		if(name.equals("Fast Utgift")) {
		    name = "Inköp";
		}
		transaction.setType(findTypeId(name));
		if(transaction.getType() == 0 ) {  //Inköp
		    transaction.setPlace(makePlaceId(rst.getString("TillKonto")));
		} else {
		    transaction.setDestination(makeAccountId(rst.getString("TillKonto")));
		}
		transaction.setDate(str2Date(rst.getString("Datum")));
		transaction.setCategory(makeCategoryId(rst.getString("Vad")));
		transaction.setUser(makeUserId(rst.getString("Vem")));

		transaction.setAmount(double2Long(rst.getDouble("Belopp")));
		transaction.setBalance(double2Long(rst.getDouble("Saldo")));
		transaction.setFixedTransfer(rst.getBoolean("FastÖverföring"));
		transaction.setComment(rst.getString("Text"));

		if(transaction.getId() > maxIdTransactions) {
		    maxIdTransactions = transaction.getId();
		}
		transactions.add(transaction);
	    }
	}catch(SQLException sqle){
	    sqle.printStackTrace();
	}
    }

    /** 
     * This method translated from strings to integer representing frequency
     * in transfers. Ie "Varje månad" => 1, "1" => 1.
     *
     * @param ofta String
     * @return int
     */
    private static int frequency2int(final String ofta) {
	int val=0;

	if(ofta.equals("Varje månad")) {
	    val = 1;
	}

	if(ofta.equals("Varannan månad")) {
	    val = 2;
	}

	if(ofta.equals("Varje kvartal")) {
	    val = 3;
	}

	if(ofta.equals("Varje halvår")) {
	    val = 6;
	}

	if(ofta.equals("Varje år")) {
	    val = 12;
	}

	if(val==0) {
	    val = Integer.parseInt(ofta.trim());
	}
	return val;
    }

    /** 
     * This method creates the table "transfers" from HEK-file.
     *
     * @param stmt Statement
     */
    private void importHEKtransfers(final Statement stmt) {
	long TransId;
	try {
	    // Get the transactions-table
	    final String sq = "SELECT * FROM Överföringar";
	    final ResultSet rst = stmt.executeQuery(sq);


        while(rst.next()){
		final Transfer transfer = new Transfer();
		TransId = rst.getLong("Löpnr");
		transfer.setId(TransId);
		transfer.setSource(findAccountId(rst.getString("FrånKonto")));
		transfer.setCategory(findCategoryId(rst.getString("Vad")));
		if(transfer.getCategory() == -1 ) {  // Mellankonto
		    transfer.setDestination(findAccountId(rst.getString("TillKonto")));
		} else {
		    if(transfer.getSource() == -1) {
			transfer.setDestination(findAccountId(rst.getString("TillKonto")));
		    } else {
			transfer.setPlace(findPlaceId(rst.getString("TillKonto")));
		    }
		}
		transfer.setAmount(double2Long(rst.getDouble("Belopp")));
		transfer.setDate(str2Date(rst.getString("Datum")));
		transfer.setFrequency(frequency2int(rst.getString("HurOfta")));
		transfer.setUser(findUserId(rst.getString("Vem")));
		transfer.setVerNumb(rst.getLong("Kontrollnr"));
		transfer.setEndDate(str2Date(rst.getString("TillDatum")));
		//There is also a field "Rakning". I ignore that.

		if(transfer.getId() > maxIdTransfers) {
		    maxIdTransfers = transfer.getId();
		}
		transfers.add(transfer);
	    }
	} catch(SQLException sqle) {
	    sqle.printStackTrace();
	}
    }
    
    /** 
     * This method creates the table "budgets" from HEK-file.
     *
     * @param stmt Statement
     *
     */
    private void importHEKbudgets(final Statement stmt) {
	try {
	    // Get the transactions-table
	    final String sq = "SELECT * FROM Budget";
	    final ResultSet rst = stmt.executeQuery(sq);
	    
	    while(rst.next()){
		final BudgetLine budgetLine = new BudgetLine();
		budgetLine.setId(rst.getLong("Löpnr"));
		budgetLine.setCategory(findCategoryId(rst.getString("Typ")));
		budgetLine.setFrequency(rst.getLong("HurOfta"));
		budgetLine.setStartMonth(rst.getLong("StartMånad"));
		budgetLine.setMonthValue(0, double2Long(rst.getDouble("Jan")));
		budgetLine.setMonthValue(1, double2Long(rst.getDouble("Feb")));
		budgetLine.setMonthValue(2, double2Long(rst.getDouble("Mar")));
		budgetLine.setMonthValue(3, double2Long(rst.getDouble("Apr")));
		budgetLine.setMonthValue(4, double2Long(rst.getDouble("Maj")));
		budgetLine.setMonthValue(5, double2Long(rst.getDouble("Jun")));
		budgetLine.setMonthValue(6, double2Long(rst.getDouble("Jul")));
		budgetLine.setMonthValue(7, double2Long(rst.getDouble("Aug")));
		budgetLine.setMonthValue(8, double2Long(rst.getDouble("Sep")));
		budgetLine.setMonthValue(9, double2Long(rst.getDouble("Okt")));
		budgetLine.setMonthValue(10, double2Long(rst.getDouble("Nov")));
		budgetLine.setMonthValue(11, double2Long(rst.getDouble("Dec")));
		budgetLine.setVerNumb(rst.getLong("Kontrollnr"));


		if(budgetLine.getId() > maxIdBudgetLines) {
		    maxIdBudgetLines = budgetLine.getId();
		}
		budgetLines.add(budgetLine);
	    }
	}catch(SQLException sqle){
	    sqle.printStackTrace();
	}
    }
    
    /** 
     * This method creates the table "places" from HEK-file.
     *
     * @param stmt Statement
     */
    private void importHEKplaces(final Statement stmt) {
	try {
	    // Get the transactions-table
	    final String sq = "SELECT * FROM Platser";
	    final ResultSet rst = stmt.executeQuery(sq);
	    
	    while(rst.next()){
		final Place place = new Place();
		place.setId(rst.getLong("Löpnr"));
		place.setName(rst.getString("Namn"));
		place.setTransferAccount(rst.getString("Gironummer"));
		place.setType(rst.getString("Typ"));
		place.setReAccount(makeAccountId(rst.getString("RefKonto").trim()));

		if(place.getId() > maxIdPlaces) {
		    maxIdPlaces = place.getId();
		}
		places.add(place);
	    }
	}catch(SQLException sqle){
	    sqle.printStackTrace();
	}
    }
    
    /** 
     * This method.
     *
     * @param filename String
     * @param rec TRec
     *
    public static void addTransaction(final String filename, final TRec rec) {
	final File file = new File(filename);
	addTransaction(file, rec);
	}*/
    
    /** 
     * This method.
     *
     * @param file File
     * @param rec TRec
     *
    private static void addTransaction(final File file, final TRec rec) {
	try{
	    Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
	    System.out.println("Opening file: "+file.getPath());
	    final Connection con = DriverManager.getConnection("jdbc:odbc:MS Access Database;DBQ="+file.getPath());
	    
	    final Statement stmt = con.createStatement();

	    addTransaction(stmt, rec);
	}catch(SQLException sqle){
	    sqle.printStackTrace();
	}catch(ClassNotFoundException cnfe){
	    cnfe.printStackTrace();
	}
	}*/

    /** 
     * This method.
     *
     * @param stmt Statement
     * @param rec TRec
     *
    private static void addTransaction(final Statement stmt, final TRec rec) {
	String sq="";
	
	if(rec.getTypeAsString().equals("Inköp")) {
	    sq =
		"INSERT INTO Transaktioner ( [FrånKonto], [TillKonto], [Typ], [Datum], [Vad], [Vem], [Belopp], [FastÖverföring], [Text] ) VALUES ('"+
		rec.getSrcAsString() + "','"+
		rec.getPlaceAsString() + "','"+
		rec.getTypeAsString() + "','"+
		rec.getDateAsString() + "','"+
		rec.getCategoryAsString() + "','"+
		rec.getUserAsString() + "',"+
		rec.getAmountAsString() + ','+
                '0' + ",'"+
		rec.getCommentAsString() + "')";
	} else {
	    if(rec.getTypeAsString().equals("Insättning")) {
		sq =
		    "INSERT INTO Transaktioner ( [FrånKonto], [TillKonto], [Typ], [Datum], [Vad], [Vem], [Belopp], [FastÖverföring], [Text] ) VALUES ('"+
		    "---" + "','"+
		    rec.getDstAsString() + "','"+ 
		    rec.getTypeAsString() + "','"+
		    rec.getDateAsString() + "','"+
		    rec.getCategoryAsString() + "','"+
		    rec.getUserAsString() + "',"+
		    rec.getAmountAsString() + ','+
                '0' + ",'"+
		    rec.getCommentAsString() + "')";
	    } else {
		if(rec.getTypeAsString().equals("Uttag")) {
		    sq =
			"INSERT INTO Transaktioner ( [FrånKonto], [TillKonto], [Typ], [Datum], [Vad], [Vem], [Belopp], [FastÖverföring], [Text] ) VALUES ('"+
			rec.getSrcAsString() + "','"+
			"Plånboken" + "','"+  //Depending on type...
			rec.getTypeAsString() + "','"+
			rec.getDateAsString() + "','"+
			rec.getCategoryAsString() + "','"+
			rec.getUserAsString() + "',"+
			rec.getAmountAsString() + ','+
                    '0' + ",'"+
			rec.getCommentAsString() + "')";
		} else {
		    if(rec.getTypeAsString().equals("Överföring")) {
			sq =
			    "INSERT INTO Transaktioner ( [FrånKonto], [TillKonto], [Typ], [Datum], [Vad], [Vem], [Belopp], [FastÖverföring], [Text] ) VALUES ('"+
			    rec.getSrcAsString() + "','"+
			    rec.getDstAsString() + "','"+
			    rec.getTypeAsString() + "','"+
			    rec.getDateAsString() + "','"+
			    rec.getCategoryAsString() + "','"+
			    rec.getUserAsString() + "',"+
			    rec.getAmountAsString() + ','+
                    '0' + ",'"+
			    rec.getCommentAsString() + "')";
		    } else {
			System.err.println("Unhandled type");
		    }
		}
	    }
	}
	
	try {
	    //ResultSet rst = stmt.executeQuery(sq);
	    stmt.executeUpdate(sq);
    }catch(SQLException sqle){
	    sqle.printStackTrace();
	}
    }*/
    
    /** 
     * This method creates creates and adds an transaction.
     *
     * @param type String
     * @param sourceaccount String
     * @param destaccount String
     * @param place String
     * @param tdate String
     * @param category String
     * @param user String
     * @param amount String
     * @param comment String
     */
    public void addTransaction(final String type,
			       String sourceaccount,
			       final String destaccount,
			       final String place,
			       final String tdate,
			       String category,
			       final String user,
			       final String amount,
			       final String comment) {

	if(type.equals("Insättning")) {
	    sourceaccount = "---";
	}
	if(type.equals("Uttag")) {
	    category = "---";
	}
	if(type.equals("Överföring")) {
	    category = "---";
	}

	final Transaction transaction = new Transaction();
	transaction.setId(maxIdTransactions+(long) 1);
	transaction.setSource(findAccountId(sourceaccount));
	transaction.setType(findTypeId(type));
	transaction.setPlace(findPlaceId(place));
	transaction.setDestination(findAccountId(destaccount));
	transaction.setDate(str2Date(tdate));
	transaction.setCategory(findCategoryId(category));
	transaction.setUser(findUserId(user));
	transaction.setAmount(double2Long(Double.parseDouble(amount)));
	transaction.setComment(comment);
	
	if(transaction.getId() > maxIdTransactions) {
	    maxIdTransactions = transaction.getId();
	}
	transactions.add(transaction);
	transaction.debugDump();
	dirty=true;
    }

    /** 
     * This method creates and adds a person.
     *
     * @param name String
     * @param year String
     * @param sex String
     */
    public void addPerson(final String name,
			       final String year,
			       final String sex) {
	final User user = new User(name, year, sex, maxIdUsers+(long) 1);
	if(user.getId() > maxIdUsers) {
	    maxIdUsers = user.getId();
	}
	users.add(user);
	dirty=true;
    }

    /** 
     * This method creates and adds an account.
     *
     * @param name String
     * @param number String
     * @param amount long
     */
    public void addAccount(final String name,
			       final String number,
			       final long amount) {
	final Account acc = new Account(name, number, amount, maxIdAccounts+(long) 1);
	if(acc.getId() > maxIdAccounts) {
	    maxIdAccounts = acc.getId();
	}
	accounts.add(acc);
	dirty=true;
    }

    /** 
     * This method creates and adds a category.
     *
     * @param name String
     * @param type String
     */
    public void addCategory(final String name,
			    final String type) {
	final Category cat = new Category(name, type, maxIdCategories+(long) 1);
	if(cat.getId() > maxIdCategories) {
	    maxIdCategories = cat.getId();
	}
	categories.add(cat);
	dirty=true;
    }

    /** 
     * This method creates and adds a place.
     *
     * @param name String
     * @param number String
     * @param cred boolean
     */
    public void addPlace(final String name,
			 final String number,
			 final boolean cred) {
	final Place place = new Place(name, number, cred, maxIdPlaces+(long) 1);
	if(place.getId() > maxIdPlaces) {
	    maxIdPlaces = place.getId();
	}
	places.add(place);
	dirty=true;
    }

    /** 
     * This method casts from double to long by multiplying by 100.
     *
     * @param in double
     * @return long
     */
    private static long double2Long(final double in) {
	return ( (long) ( in*(double) 100  ));
    }

    /** 
     * This method parses a string as a monthname and returns the month index.
     *
     * @param in String
     * @return short (0..11 inclusive)
     */
    private short month2int(final String in) {
	for(short i=(short) 0; i<12; i++) {
	    if(in.compareToIgnoreCase(months[i]) == 0) {
		return i;
	    }
	}
	return (short) -1;
    }

    /** 
     * This method.
     *
     * @param in String
     * @return Date
     */
    private Date str2Date(final String in) {
	Date foo;
	try {
	    foo=df.parse(in.trim());
	} catch (ParseException e) {
	    //System.out.println("Datefailed: "+e);
	    //e.printStackTrace();
	    foo = null;
	}
	return foo;
    }

    /**
     * Turns array of bytes into string
     *
     * @param buf	Array of bytes to convert to hex string
     * @return	Generated hex string
     */
    public static String asHex (byte buf[]) {
	StringBuffer strbuf = new StringBuffer(buf.length * 2);
	int i;
	
	for (i = 0; i < buf.length; i++) {
	    if (((int) buf[i] & 0xff) < 0x10)
		strbuf.append("0");
	    
	    strbuf.append(Long.toString((int) buf[i] & 0xff, 16));
	}
	
	return strbuf.toString();
    }
}
