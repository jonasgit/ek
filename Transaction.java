import org.w3c.dom.Document;
import org.w3c.dom.Node;

import java.text.SimpleDateFormat;
import java.util.Date;

final class Transaction extends DataStorage {
    private long id=-1;
    private long sourceAccount=-1;
    private long destinationAccount=-1;
    private long type=-1;
    private long category=-1;
    private long user=-1;
    private long amount=-1;
    private long balance=-1;
    private long place=-1;
    private boolean fixedTransfer;
    private String comment;
    private Date date;
	
    Transaction() {
	//	System.out.println("Transaction constructor");
    }
    
    /** 
     * This method.
     *
     * @param node some Node
     */
    Transaction(final Node node) {
	Node TextNode;
	final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
 
        for (Node child = node.getFirstChild(); child != null;
             child = child.getNextSibling()) {
	    if( child.getNodeType() == Node.ELEMENT_NODE) {
		TextNode = child.getFirstChild();
		if(TextNode.getNodeType() == Node.TEXT_NODE) {
		    if(child.getNodeName().compareToIgnoreCase("Id") == 0)
			id = Long.parseLong(TextNode.getNodeValue(), 10);
		    if(child.getNodeName().compareToIgnoreCase("SourceAccount") == 0)
			sourceAccount = Long.parseLong(TextNode.getNodeValue(), 10);
		    if(child.getNodeName().compareToIgnoreCase("DestinationAccount") == 0)
			destinationAccount = Long.parseLong(TextNode.getNodeValue(), 10);
		    if(child.getNodeName().compareToIgnoreCase("Type") == 0)
			type = Long.parseLong(TextNode.getNodeValue(), 10);
		    if(child.getNodeName().compareToIgnoreCase("Category") == 0)
			category = Long.parseLong(TextNode.getNodeValue(), 10);
		    if(child.getNodeName().compareToIgnoreCase("User") == 0)
			user = Long.parseLong(TextNode.getNodeValue(), 10);
		    if(child.getNodeName().compareToIgnoreCase("Amount") == 0)
			amount = Long.parseLong(TextNode.getNodeValue(), 10);
		    if(child.getNodeName().compareToIgnoreCase("Balance") == 0)
			balance = Long.parseLong(TextNode.getNodeValue(), 10);
		    if(child.getNodeName().compareToIgnoreCase("Place") == 0)
			place = Long.parseLong(TextNode.getNodeValue(), 10);
		    if(child.getNodeName().compareToIgnoreCase("FixedTransfer") == 0)
			fixedTransfer = Boolean.valueOf(TextNode.getNodeValue()).booleanValue();
		    if(child.getNodeName().compareToIgnoreCase("Comment") == 0)
			comment = TextNode.getNodeValue();
		    if(child.getNodeName().compareToIgnoreCase("Date") == 0) {
			try {
			    date = df.parse(TextNode.getNodeValue());
			} catch (java.text.ParseException pe) {
			    System.out.println("df.parse1 failed");
			}
		    }
		}
	    }
        }
    }

    public void debugDump() {
	System.out.println(id+":"+
			   sourceAccount+':'+
			   destinationAccount+':'+
			   type+':'+
			   category+':'+
			   user+':'+
			   amount+':'+
			   balance+':'+
			   place+':'+
			   fixedTransfer+':'+
			   comment+':'+
			   date+':');
    }

    public void setId(final long _id) {
	id = _id;
    }

    public void setComment(final String _comment) {
	comment = _comment;
    }

    public void setSource(final long _sourceAccount) {
	sourceAccount = _sourceAccount;
    }

    public void setDestination(final long _destinationAccount) {
	destinationAccount = _destinationAccount;
    }

    public void setType(final long _type) {
	type = _type;
    }

    public void setPlace(final long _place) {
	place = _place;
    }

    public void setDate(final Date _date) {
	date = _date;
    }

    public void setCategory(final long _category) {
	category = _category;
    }

    public void setUser(final long _user) {
	user = _user;
    }

    public void setAmount(final long _amount) {
	amount = _amount;
    }

    public void setBalance(final long _balance) {
	balance = _balance;
    }

    public void setFixedTransfer(final boolean _fixedTransfer) {
	fixedTransfer = _fixedTransfer;
    }



    
    public long getId() {
	return id;
    }

    public String getComment() {
	return comment;
    }

    public long getSource() {
	return sourceAccount;
    }

    public long getDestination() {
	return destinationAccount;
    }

    public long getType() {
	return type;
    }

    public long getPlace() {
	return place;
    }

    public Date getDate() {
	return date;
    }

    public long getCategory() {
	return category;
    }

    public long getUser() {
	return user;
    }

    public long getAmount() {
	return amount;
    }

// --Recycle Bin START (2003-12-27 18:33):
//    public final long getBalance() {
//	return balance;
//    }
// --Recycle Bin STOP (2003-12-27 18:33)

// --Recycle Bin START (2003-12-27 18:33):
//    public final boolean getFixedTransfer() {
//	return fixedTransfer;
//    }
// --Recycle Bin STOP (2003-12-27 18:33)

    public void DOMexport(final Document document, final Node root) {
	final Node transaction;
	
	transaction = document.createElement("Transaction");

	addNodeDOM(document, transaction, "Id", id);
	addNodeDOM(document, transaction, "SourceAccount", sourceAccount);
	addNodeDOM(document, transaction, "DestinationAccount", destinationAccount);
	addNodeDOM(document, transaction, "Type", type);
	addNodeDOM(document, transaction, "Category", category);
	addNodeDOM(document, transaction, "User", user);
	addNodeDOM(document, transaction, "Amount", amount);
	addNodeDOM(document, transaction, "Balance", balance);
	addNodeDOM(document, transaction, "Place", place);
	addNodeDOM(document, transaction, "FixedTransfer", fixedTransfer);
	addNodeDOM(document, transaction, "Comment", comment);
	addNodeDOM(document, transaction, "Date", date);

	root.appendChild(transaction);
    }
}

