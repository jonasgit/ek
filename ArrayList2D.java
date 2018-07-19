import java.util.ArrayList;

/**
 * ArrayList2D is a 2D-ArrayList
 * Indexes are zero-based, sizes are 1..n
 */
public final class ArrayList2D {
    private final ArrayList<ArrayList<Object>> data = new ArrayList<ArrayList<Object>>();
    private int sizeX=0;
    private int sizeY=0;

    /**
     * Constructor.
     *
     */
    public ArrayList2D() {
    }

    /** 
     * Return size.x.
     *
     * @return sizeX
     */
    public int getSizeX() {
	return sizeX;
    }

    /** 
     * Return size.y.
     *
     * @return sizeY
     */
    public int getSizeY() {
	return sizeY;
    }

    /** 
     * Makes sure the array can contain so many elements.
     * Specified sizes should be in range 1..n.
     *
     * @param minCapacityX size X
     * @param minCapacityY size Y
     */
    private void ensureCapacity(final int minCapacityX, final int minCapacityY) {
	ArrayList<Object> x;
	if(minCapacityY > sizeY) {
	    data.ensureCapacity(minCapacityY);
	    sizeY=minCapacityY;
	    for(int i=data.size(); i<minCapacityY; i++) {
		x = new ArrayList<Object>();
		data.add(x);
	    }
	}
	for(int i=0; i<sizeY; i++) {
	    x=data.get(i);
	    if(minCapacityX > x.size()) {
		x.ensureCapacity(minCapacityX);
		for(int j=x.size(); j<minCapacityX; j++) {
		    x.add(new Object());
		}
	    }
	}
	if(minCapacityX > sizeX) 
	    sizeX=minCapacityX;
    }

    /** 
     * Makes sure the array can contain so many elements.
     * Specified indexes should be in range 0..n.
     *
     * @param indexX X
     * @param indexY Y
     * @return object at X,Y
     */
    public Object get(final int indexX, final int indexY) {
	if( (indexX >= sizeX) ||
	    (indexY >= sizeY) ) {
	    System.err.println("ArrayList2D.get out of bounds");
	    System.exit(1);
	}
	return ((ArrayList)data.get(indexY)).get(indexX);
    }

    /** 
     * Makes sure the array can contain so many elements.
     * Specified indexes should be in range 0..n.
     *
     * @param indexX X
     * @param indexY Y
     * @param element element
     */
    public void set(final int indexX, final int indexY, final Object element) {
	ensureCapacity(indexX+1, indexY+1);
	(data.get(indexY)).set(indexX, element);
    }

// --Recycle Bin START (2003-12-27 18:33):
//    /**
//     * Removes all elements and sets size to 0:0
//     *
//     * @tag    Comment for the tag
//     */
//    public final void clear() {
//	data = new ArrayList();
//
//	sizeX=0;
//	sizeY=0;
//    }
// --Recycle Bin STOP (2003-12-27 18:33)
}
