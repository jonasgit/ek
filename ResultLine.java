import org.w3c.dom.Document;
import org.w3c.dom.Node;

final class ResultLine extends DataStorage {
    private long id;
    private long category;
    private long frequency;
    private long startMonth;
    private long verNumb;
    private final long[] values=new long[12];
	
    ResultLine() {
	//System.out.println("ResultLine constructor");
    }
    
    /** 
     * This method.
     *
     * @param node some node
     */
    ResultLine(final Node node) {
	Node TextNode;
 
        for (Node child = node.getFirstChild(); child != null;
             child = child.getNextSibling()) {
	    if( child.getNodeType() == Node.ELEMENT_NODE) {
		TextNode = child.getFirstChild();
		if(TextNode.getNodeType() == Node.TEXT_NODE) {
		    if(child.getNodeName().compareToIgnoreCase("Id") == 0)
			id = Long.parseLong(TextNode.getNodeValue(), 10);
		    if(child.getNodeName().compareToIgnoreCase("Category") == 0)
			category = Long.parseLong(TextNode.getNodeValue(), 10);
		    if(child.getNodeName().compareToIgnoreCase("Frequency") == 0)
			frequency = Long.parseLong(TextNode.getNodeValue(), 10);
		    if(child.getNodeName().compareToIgnoreCase("StartMonth") == 0)
			startMonth = Long.parseLong(TextNode.getNodeValue(), 10);
		    if(child.getNodeName().compareToIgnoreCase("verNumb") == 0)
			verNumb = Long.parseLong(TextNode.getNodeValue(), 10);

		    if(child.getNodeName().compareToIgnoreCase("Month0") == 0)
			values[0] = Long.parseLong(TextNode.getNodeValue(), 10);
		    if(child.getNodeName().compareToIgnoreCase("Month1") == 0)
			values[1] = Long.parseLong(TextNode.getNodeValue(), 10);
		    if(child.getNodeName().compareToIgnoreCase("Month2") == 0)
			values[2] = Long.parseLong(TextNode.getNodeValue(), 10);
		    if(child.getNodeName().compareToIgnoreCase("Month3") == 0)
			values[3] = Long.parseLong(TextNode.getNodeValue(), 10);
		    if(child.getNodeName().compareToIgnoreCase("Month4") == 0)
			values[4] = Long.parseLong(TextNode.getNodeValue(), 10);
		    if(child.getNodeName().compareToIgnoreCase("Month5") == 0)
			values[5] = Long.parseLong(TextNode.getNodeValue(), 10);
		    if(child.getNodeName().compareToIgnoreCase("Month6") == 0)
			values[6] = Long.parseLong(TextNode.getNodeValue(), 10);
		    if(child.getNodeName().compareToIgnoreCase("Month7") == 0)
			values[7] = Long.parseLong(TextNode.getNodeValue(), 10);
		    if(child.getNodeName().compareToIgnoreCase("Month8") == 0)
			values[8] = Long.parseLong(TextNode.getNodeValue(), 10);
		    if(child.getNodeName().compareToIgnoreCase("Month9") == 0)
			values[9] = Long.parseLong(TextNode.getNodeValue(), 10);
		    if(child.getNodeName().compareToIgnoreCase("Month10") == 0)
			values[10] = Long.parseLong(TextNode.getNodeValue(), 10);
		    if(child.getNodeName().compareToIgnoreCase("Month11") == 0)
			values[11] = Long.parseLong(TextNode.getNodeValue(), 10);
		}
	    }
        }
	//System.out.println("New ResultLine created:");
	//System.out.println("id = "+id);

	//System.out.println("Category= "+ category);
	//System.out.println("Frequency= "+ frequency);
	//System.out.println("StartMonth= "+ startMonth);
	//System.out.println("verNumb= "+ verNumb);

	//for(int i=0; i<12; i++) {
	//    System.out.println("Month"+i+"= "+ values[i]);
	//}
	//System.out.println("--");
    }

// --Recycle Bin START (2003-12-27 18:33):
//    public static final void Debugdump() {
//	System.out.println("ResultLine Userdump");
//    }
// --Recycle Bin STOP (2003-12-27 18:33)

    public void setId(final long _id) {
	id = _id;
    }

    public void setCategory(final long _category) {
	category = _category;
    }

    public void setFrequency(final long _frequency) {
	frequency = _frequency;
    }

    public void setStartMonth(final long _startMonth) {
	startMonth = _startMonth;
    }

    public void setVerNumb(final long _verNumb) {
	verNumb = _verNumb;
    }

    public void setMonthValue(final int month, final long _value) {
	values[month] = _value;
    }



    public long getId() {
	return id;
    }

    public long getCategory() {
	return category;
    }

// --Recycle Bin START (2003-12-27 18:33):
//    public final long getFrequency() {
//	return frequency;
//    }
// --Recycle Bin STOP (2003-12-27 18:33)

// --Recycle Bin START (2003-12-27 18:33):
//    public final long getStartMonth() {
//	return startMonth;
//    }
// --Recycle Bin STOP (2003-12-27 18:33)

// --Recycle Bin START (2003-12-27 18:33):
//    public final long getVerNumb() {
//	return verNumb;
//    }
// --Recycle Bin STOP (2003-12-27 18:33)

    public long getMonthValue(final int month) {
	return values[month];
    }
    
    public void DOMexport(final Document document, final Node root) {

        final Node resultLine;
	
	resultLine = document.createElement("resultLine");

	addNodeDOM(document, resultLine, "Id", id);
	addNodeDOM(document, resultLine, "Category", category);
	addNodeDOM(document, resultLine, "Frequency", frequency);
	addNodeDOM(document, resultLine, "StartMonth", startMonth);
	addNodeDOM(document, resultLine, "verNumb", verNumb);

	for(int i=0; i<12; i++) {
	    addNodeDOM(document, resultLine, "Month"+i, values[i]);
	}

	root.appendChild(resultLine);
    }
}

