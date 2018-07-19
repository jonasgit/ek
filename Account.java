
import org.w3c.dom.Document;
import org.w3c.dom.Node;

final class Account extends DataStorage {
    private long id;
    private long balance;
    private long initialBalance;
    private long yearBalance;
    private short startMonth;
    private short yearChangeMonth;
    private String accountNumber = "";
    private String title = "";

    /**
     * This method.
     */
    Account() {
        //	System.out.println("Account constructor");
    }

    /**
     * This method.
     * 
     * @param name   some name
     * @param number some number
     * @param amount some mount
     * @param id     some id
     */
    Account(final String name,
            final String number,
            final long amount,
            final long id) {
        setId(id);
        setAccountNumber(number);
        setTitle(name);
        setInitialBalance(amount);
    }

    /**
     * This method.
     * 
     * @param node some node
     */
    Account(final Node node) {
        Node TextNode;

        for (Node child = node.getFirstChild(); child != null;
             child = child.getNextSibling()) {
            if (child.getNodeType() == Node.ELEMENT_NODE) {
                TextNode = child.getFirstChild();
                if ((TextNode != null) && (TextNode.getNodeType() == Node.TEXT_NODE)) {
                    if (child.getNodeName().compareToIgnoreCase("Id") == 0)
                        id = Long.parseLong(TextNode.getNodeValue(), 10);
                    if (child.getNodeName().compareToIgnoreCase("Balance") == 0)
                        balance = Long.parseLong(TextNode.getNodeValue(), 10);
                    if (child.getNodeName().compareToIgnoreCase("InitialBalance") == 0)
                        initialBalance = Long.parseLong(TextNode.getNodeValue(), 10);
                    if (child.getNodeName().compareToIgnoreCase("YearBalance") == 0)
                        yearBalance = Long.parseLong(TextNode.getNodeValue(), 10);
                    if (child.getNodeName().compareToIgnoreCase("StartMonth") == 0)
                        startMonth = Short.parseShort(TextNode.getNodeValue(), 10);
                    if (child.getNodeName().compareToIgnoreCase("YearChangeMonth") == 0)
                        yearChangeMonth = Short.parseShort(TextNode.getNodeValue(), 10);
                    if (child.getNodeName().compareToIgnoreCase("AccountNumber") == 0)
                        accountNumber = TextNode.getNodeValue();
                    if (child.getNodeName().compareToIgnoreCase("Title") == 0)
                        title = TextNode.getNodeValue();
                }
            }
        }
        //System.out.println("New Account created:");
        //System.out.println("id = "+id);
        //System.out.println("balance = "+balance);
        //System.out.println("initialBalance = "+initialBalance);
        //System.out.println("yearBalance = "+yearBalance);
        //System.out.println("startMonth = "+startMonth);
        //System.out.println("yearChangeMonth = "+yearChangeMonth);
        //System.out.println("accountNumber = "+accountNumber);
        //System.out.println("title = " + title);
        //System.out.println("--");
    }
    
// --Recycle Bin START (2003-12-27 18:33):
//    /**
//     * This method
//     *
//     * @tag    Comment for the tag
//     */
//    public static void Debugdump() {
//	System.out.println("Account Userdump");
//    }
// --Recycle Bin STOP (2003-12-27 18:33)

    /**
     * This method.
     * 
     * @param _id some long id
     */
    public void setId(final long _id) {
        id = _id;
    }

    /**
     * This method.
     * 
     * @param _number the account number
     */
    public void setAccountNumber(final String _number) {
        if (_number == null) {
            accountNumber = "";
        } else {
            accountNumber = _number;
        }
    }

    /**
     * This method.
     * 
     * @param _title some string title
     */
    public void setTitle(final String _title) {
        if (_title == null) {
            title = "";
        } else {
            title = _title;
        }
    }

    /**
     * This method.
     * 
     * @param _balance a long balance
     */
    public void setBalance(final long _balance) {
        balance = _balance;
    }

    /**
     * This method.
     * 
     * @param _initialBalance some long initial balance
     */
    public void setInitialBalance(final long _initialBalance) {
        initialBalance = _initialBalance;
    }

    /**
     * This method.
     * 
     * @param _month some short month
     */
    public void setStartMonth(final short _month) {
        startMonth = _month;
    }

    /**
     * This method.
     * 
     * @param _balance some long balance
     */
    public void setYearBalance(final long _balance) {
        yearBalance = _balance;
    }

    /**
     * This method.
     * 
     * @param _month some short month
     */
    public void setYearChange(final short _month) {
        yearChangeMonth = _month;
    }


    /**
     * This method.
     * 
     * @return some long id
     */
    public long getId() {
        return id;
    }

// --Recycle Bin START (2003-12-27 18:33):
//    /**
//     * This method
//     *
//     * @tag    Comment for the tag
//     */
//    public String getAccountNumber() {
//	return accountNumber;
//    }
// --Recycle Bin STOP (2003-12-27 18:33)

    /**
     * This method.
     * 
     * @return String title
     */
    public String getTitle() {
        return title;
    }

// --Recycle Bin START (2003-12-27 18:33):
//    /**
//     * This method
//     *
//     * @tag    Comment for the tag
//     */
//    public long getBalance() {
//	return balance;
//    }
// --Recycle Bin STOP (2003-12-27 18:33)

    /**
     * This method.
     * 
     * @return long initial balance
     */
    public long getInitialBalance() {
        return initialBalance;
    }

// --Recycle Bin START (2003-12-27 18:33):
//    /**
//     * This method
//     *
//     * @tag    Comment for the tag
//     */
//    public short getStartMonth() {
//	return startMonth;
//    }
// --Recycle Bin STOP (2003-12-27 18:33)

// --Recycle Bin START (2003-12-27 18:33):
//    /**
//     * This method
//     *
//     * @tag    Comment for the tag
//     */
//    public long getYearBalance() {
//	return yearBalance;
//    }
// --Recycle Bin STOP (2003-12-27 18:33)

// --Recycle Bin START (2003-12-27 18:33):
//    /**
//     * This method
//     *
//     * @tag    Comment for the tag
//     */
//    public short getYearChange() {
//	return yearChangeMonth;
//    }
// --Recycle Bin STOP (2003-12-27 18:33)

    /**
     * This method.
     * 
     * @param document some docuemtn
     * @param root     some node
     */
    public void DOMexport(final Document document, final Node root) {
        final Node account;

        account = document.createElement("Account");

        addNodeDOM(document, account, "Id", id);
        addNodeDOM(document, account, "Balance", balance);
        addNodeDOM(document, account, "InitialBalance", initialBalance);
        addNodeDOM(document, account, "YearBalance", yearBalance);
        addNodeDOM(document, account, "StartMonth", (long) startMonth);
        addNodeDOM(document, account, "YearChangeMonth", (long) yearChangeMonth);
        addNodeDOM(document, account, "AccountNumber", accountNumber);
        addNodeDOM(document, account, "Title", title);

        root.appendChild(account);
    }

    public void debugDump() {
        System.out.println(id + ":" +
                balance + ':' +
                initialBalance + ':' +
                yearBalance + ':' +
                startMonth + ':' +
                yearChangeMonth + ':' +
                accountNumber + ':' +
                title + ':');
    }
}

