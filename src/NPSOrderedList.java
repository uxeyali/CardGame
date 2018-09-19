	
	public interface NPSOrderedList <E>{
	    
	    public void add(E item);
	    
	    public void clear(  ); //done
	    
	    public boolean contains(E item);//done
	    
	    public E get(int index) throws IndexOutOfBoundsException; // done
	    
	    public int indexOf(E item); //done
	    
	    public NPSIterator<E> iterator();
	    
	    public boolean isEmpty(  ); //done
	    
	    public E remove(int index) throws IndexOutOfBoundsException; //done

	    public boolean remove(E item); //done
	    
	    public int size(   ); //done
	    
	    public void union(NPSOrderedList <E> list1, NPSOrderedList<E>list2);

}
