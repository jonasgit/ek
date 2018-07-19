import org.w3c.dom.Document;
import org.w3c.dom.Node;

import java.text.SimpleDateFormat;
import java.util.Date;

final class Transfer extends DataStorage {
    private long id=-1;
    private long sourceAccount=-1;
    private long destinationAccount=-1;
    private long category=-1;
    private long user=-1;
    private long amount=-1;
    private long place=-1;
    private long verNumb=-1;
    private Date date;
    private Date endDate;
    private int frequency=-1;
	
    Transfer() {
	//	System.out.println("Transfer constructor");
    }
    
    /** 
     * This method.
     *
     * @param node some Node
     */
    Transfer(final Node node) {
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
		    if(child.getNodeName().compareToIgnoreCase("Category") == 0)
			category = Long.parseLong(TextNode.getNodeValue(), 10);
		    if(child.getNodeName().compareToIgnoreCase("User") == 0)
			user = Long.parseLong(TextNode.getNodeValue(), 10);
		    if(child.getNodeName().compareToIgnoreCase("Amount") == 0)
			amount = Long.parseLong(TextNode.getNodeValue(), 10);
		    if(child.getNodeName().compareToIgnoreCase("Place") == 0)
			place = Long.parseLong(TextNode.getNodeValue(), 10);
		    if(child.getNodeName().compareToIgnoreCase("VerNumb") == 0)
			verNumb = Long.parseLong(TextNode.getNodeValue(), 10);
		    if(child.getNodeName().compareToIgnoreCase("Date") == 0) {
			try {
			    date = df.parse(TextNode.getNodeValue());
			} catch (java.text.ParseException pe) {
			    System.out.println("df.parse1 failed");
			}
		    }
		    if(child.getNodeName().compareToIgnoreCase("EndDate") == 0) {
			try {
			    endDate = df.parse(TextNode.getNodeValue());
			} catch (java.text.ParseException pe) {
			    System.out.println("df.parse2 failed");
			}
		    }
		    if(child.getNodeName().compareToIgnoreCase("Frequency") == 0)
			frequency = Integer.parseInt(TextNode.getNodeValue(), 10);
		}
	    }
        }
	//System.out.println("New Transfer created:");
	//System.out.println("id = "+id);
	//System.out.println("sourceAccount = "+sourceAccount);
	//System.out.println("destinationAccount = "+destinationAccount);
	//System.out.println("category = "+category);
	//System.out.println("user = "+user);
	//System.out.println("amount = "+amount);
	//System.out.println("place = "+place);
	//System.out.println("verNumb = "+verNumb);
	//System.out.println("date = "+date);
	//System.out.println("endDate = "+endDate);
	//System.out.println("frequency = "+frequency);
	//System.out.println("--");
    }

    public void debugDump() {
	System.out.println(id+":"+
			   sourceAccount+':'+
			   destinationAccount+':'+
			   category+':'+
			   user+':'+
			   amount+':'+
			   place+':'+
			   verNumb+':'+
			   date+':'+
			   endDate+':'+
			   frequency+':'
			   );
    }

    public void setId(final long _id) {
	id = _id;
    }

    public void setSource(final long _sourceAccount) {
	sourceAccount = _sourceAccount;
    }

    public void setDestination(final long _destinationAccount) {
	destinationAccount = _destinationAccount;
    }

    public void setPlace(final long _place) {
	place = _place;
    }

    public void setDate(final Date _date) {
	date = _date;
    }

    public void setEndDate(final Date _date) {
	endDate = _date;
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

    public void setFrequency(final int _frequency) {
	frequency = _frequency;
    }

    public void setVerNumb(final long _ver) {
	verNumb = _ver;
    }

    
    public long getId() {
	return id;
    }

    public long getSource() {
	return sourceAccount;
    }

    public long getDestination() {
	return destinationAccount;
    }

// --Recycle Bin START (2003-12-27 18:33):
//    public final long getPlace() {
//	return place;
//    }
// --Recycle Bin STOP (2003-12-27 18:33)

    public Date getDate() {
	return date;
    }

    public Date getEndDate() {
	return endDate;
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

    public int getFrequency() {
	return frequency;
    }

    public void DOMexport(final Document document, final Node root) {
	final Node transfer;
	
	transfer = document.createElement("Transfer");

	addNodeDOM(document, transfer, "Id", id);
	addNodeDOM(document, transfer, "SourceAccount", sourceAccount);
	addNodeDOM(document, transfer, "DestinationAccount", destinationAccount);
	addNodeDOM(document, transfer, "Category", category);
	addNodeDOM(document, transfer, "User", user);
	addNodeDOM(document, transfer, "Amount", amount);
	addNodeDOM(document, transfer, "Place", place);
	addNodeDOM(document, transfer, "VerNumb", verNumb);
	addNodeDOM(document, transfer, "Date", date);
	addNodeDOM(document, transfer, "EndDate", endDate);
	addNodeDOM(document, transfer, "Frequency", (long) frequency);

	root.appendChild(transfer);
    }
}

