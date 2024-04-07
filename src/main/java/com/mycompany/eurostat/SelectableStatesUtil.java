package com.mycompany.eurostat;

import java.util.HashMap;
import java.util.Map;

/**
 * Utility class for working with country selection functionality.
 * @author Vidal Spierings
 */

public class SelectableStatesUtil {

    public static HashMap<String, String> countriesHashMap = new HashMap<>();
        
           /**
     * Custom method that enables bi-directional mapping within an HashMap containing causes.
     *
     * @see <a href="https://en.wikipedia.org/wiki/Bidirectional_map">Bidirectional map</a>
     * @param value the country for which the key ought te be retrieved      
     * @return returns null if no key was found for value, and returns the value in question when a value for a key was found
     */
	
	public static String getKeyByValueForCountriesHashMap(String value) {
        for (Map.Entry<String, String> entry : countriesHashMap.entrySet()) {
            if (value.equals(entry.getValue())) {
                return entry.getKey();
            }
        }
        return null;
        
        // returns null if there is no key for the given value
        
    }
        
        /**
     * Custom method that enables bi-directional mapping within an HashMap containing causes.
     *
     * @see <a href="https://en.wikipedia.org/wiki/Bidirectional_map">Bidirectional mapping</a>
     * @param value the cause for which the key ought te be retrieved      
     * @param causesHashMap the HashMap thourgh which needs to be searched to find a 
     * @return returns null if no key was found for value, and returns the value in question when a value for a key was found
     */
        
        public static Integer getKeyByValueForCauses(StringBuilder value, HashMap<Integer, StringBuilder> causesHashMap) {

            for (Map.Entry<Integer, StringBuilder> entry : causesHashMap.entrySet()) {
                                
            if (value.toString().equals(entry.getValue().toString())) {
                
                return entry.getKey();
                
            }
        }
            
        return null;
        
        // returns null if there is no key for the given value
        
    }

}
