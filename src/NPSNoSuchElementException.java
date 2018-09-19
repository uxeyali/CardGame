/*
     Introduction to OOP with Java (Comprehensive Version 1st Ed), McGraw-Hill
    
     Wu/Otani
    
     Chapter 18 Sample Program: Customized exception class for nonexisting
                                element. This exception is thrown by
                                the inner MyIterator class in the 
                                implementation classes of the List ADT
    
     File: edu/nps/util/NPSNoSuchElementException.java
*/

public class NPSNoSuchElementException extends RuntimeException {
    
    public NPSNoSuchElementException( ) {
        
        this("Requested element does not exist in the data structure");
    }
    
    public NPSNoSuchElementException(String message) {
        
        super(message);
    }

}
