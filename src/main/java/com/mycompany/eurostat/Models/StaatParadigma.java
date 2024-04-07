package com.mycompany.eurostat.Models;

/**
 * The StaatParadigma interface is a paradigm for enforcing every specialisation of the interface to dictate it's own concise functionality for showing the value of an important object within set class.
 * @author Vidal Spierings
 */

@FunctionalInterface
public interface StaatParadigma {

    /**
 * the method that enforces every specialisation of this method to dictate it's own concise functionality for showing the value of an important object within the class.
     * @return important info in the form of a String object
 */
    
	public String myToString();
	
	/*
	 *  every implementation of the 'StaatParadigma' class has to create their own concise version of the 'toString' method or,
	 *  as it is called here, the 'myToString' method
	
	*/
	
}
