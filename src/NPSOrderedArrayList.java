import java.io.Serializable;

public class NPSOrderedArrayList <E> implements Serializable{

	    
	    public static final int DEFAULT_SIZE = 25; 
	         
	    public static final int NOT_FOUND = -1; 
	    
	    private E[] element;  
	   
	    private int count;
	 
	    public NPSOrderedArrayList( ) {
	        this(DEFAULT_SIZE);      
	    }
	      
	    @SuppressWarnings("unchecked")
		public NPSOrderedArrayList(int size) {
	        
	        if (size <= 0) {
	            throw new IllegalArgumentException( 
	                            "Initial capacity must be positive" );
	        }
	        
	        element = (E[]) new Object[ size ];
	        count   = 0;
	    }

	    
	    //wrote method myself
	    

	                        
	  /*	public void union(NPSOrderedList <E> list1, NPSOrderedList<E>list2){
	  		
	  		 //remove any duplicates 
	        for(int i=0;i<=list2.size();i++){
	      	  if(list2.indexOf(list1.get(i)) > -1){
	      		  list2.remove(list2.indexOf(list1.get(i)));
	      	  }  
	        }
	        
	      //add to current list
	        for(int j =0;j<list2.size();j++){
	      	  list1.add(list2.get(j));
	        }
	  		
	  	}
	  */
	  
	    public void add(E item) {
	        
	        if (count == element.length) {        
	            expand( );
	        }

	        element[count] = item;
	        count++;
	        
	        //this method can be implemented by simply calling
	        //the second 'add' method as follows:
	        //
	        //add(count, item);
	    }
	    
	    
	 
	    public void add(int index, E item) throws IndexOutOfBoundsException {
	        
	        checkInsertPosition(index);
	        
	        if (count == element.length) {
	            expand( );
	        }

	        // shift one position to the right
	        for (int i = count; i > index; i--) {
	            element[i] = element[i-1];
	        }
	 
	        element[index] = item;
	        count++;
	    }
	    
	    public void clear(  ) {
	        
	        for (int i = 0; i < count; i++) {
	            element[i] = null;
	        }
	     
	        count = 0;
	    }
	   
	    public boolean contains(E item ) {
	        
	        boolean result = true;
	    
	        int loc = indexOf( item );
	    
	        if ( loc == NOT_FOUND ) {
	            result = false;
	        }
	    
	        return result;
	    }
	    
	     public E get(int index) throws IndexOutOfBoundsException {
	        
	        checkAccessPosition(index);
	    
	        return element[index];
	    }
	    
	    
	    public int indexOf(E item) {
	        
	        int loc = 0;
	    
	        while (loc < count && !element[loc].equals(item)) {
	            loc++;
	        }
	    
	        if (loc == count) {
	            loc = NOT_FOUND;
	        }
	    
	        return loc;
	    }
	    
	    
	    public NPSIterator<E> iterator() {
	        
	        return new MyIterator();
	    }
	    
	    public boolean isEmpty(  ) {
	        
	        return (count == 0);
	    }
	    
	    public E remove(int index) throws IndexOutOfBoundsException {
	        
	        checkAccessPosition(index);
	        
	        E item = element[index];
	            
	        // shift one position to the left
	        for (int i = index; i < count; i++) {
	            element[i] = element[i+1];
	        }
	        
	        element[count] = null;
	        
	        count--;
	        
	        return item;
	    }
	    
	    public boolean remove(E item) {
	        
	        int loc = indexOf(item);
	        
	        if (loc == NOT_FOUND) {
	            
	            return false;
	            
	        } else {
	            
	            remove(loc);
	            
	            return true;
	        }
	    }
	    

	    
	    public int size(   ) {
	        
	        return count;
	    }

	    
	    /**
	     * Checks the passed index position is a valid index that refers
	     * to a non-empty position
	     *
	     * @param index  value to check for valid position
	     * 
	     * @exception IndexOutOfBoundsException if the passed index
	     *              is outside of the range of valid access positions
	     */
	    private void checkAccessPosition(int index) {

	        if (size() == 0) {
	            
	            throw new IndexOutOfBoundsException(
	                        "Index " + index + " is invalid. List is empty.");
	                
	        } else if (index < 0) {
	            
	            throw new IndexOutOfBoundsException(
	                        "Negative index of " + index + " is invalid");    
	                                                              
	        } else if (index > size()-1) {
	            
	            throw new IndexOutOfBoundsException(index +  
	                        " is larger than valid upper bound" + (size()-1));
	        }
	    }
	    
	    
	    /**
	     * Checks the passed index position is a valid insertion point in 
	     * the array.
	     *
	     * @param index   value to check for insertion position
	     *
	     * @exception IndexOutOfBoundsException if the passed index
	     *              is outside of the range of valid insertion positions
	     */
	    private void checkInsertPosition(int index) {

	        if (index < 0) {
	            
	            throw new IndexOutOfBoundsException(
	                        "Negative index of " + index + " is invalid");    
	                                                              
	        } else if (index > size()) {
	            
	            throw new IndexOutOfBoundsException(index +  
	                        " is larger than valid upper bound" + size());
	        }
	    }
	    
	                                
	    private void expand( ) {
	        
	        // create a new array whose size is 150% of
	        // the current array
	        int newLength = (int) (1.5 * element.length);
	        @SuppressWarnings("unchecked")
			E[] temp = (E[]) new Object[newLength];
	    
	        // now copy the data to the new array
	        for (int i = 0; i < element.length; i++) {
	            temp[i] = element[i];
	        }
	    
	        // finally set the variable entry to point to the new array
	        element = temp;    
	    }   

	   
	    
	    ////---------- Inner Class :  MyIterator -------------////
	    private class MyIterator implements NPSIterator<E> {
	        
	        private int current;
	        
	        public MyIterator( ) {
	            current = 0;           
	        }
	        
	        public boolean hasNext( ) {
	            
	            if (current < size()-1) {
	                return true;
	            } else {
	                return false;
	            }
	        }
	        
	        public E next( ) throws NPSNoSuchElementException {
	            
	            if (current >= size()) {
	                throw new NPSNoSuchElementException();
	                
	            } else {
	                
	                return element[current++];
	            }           
	        }
	    }

	}

