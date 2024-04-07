package com.mycompany.eurostat;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import org.json.simple.JSONObject;

/**
     * Engine that corrects the broken API of Eurostat, and correctly retrieves categorised research results.
     * see API web page for context <a href="https://ec.europa.eu/eurostat/api/dissemination/statistics/1.0/data/isoc_cisci_sp20?format=JSON&lang=en&freq=A&ind_type=IND_TOTAL&indic_is=I_MPP1&indic_is=I_MPP1_SSA&indic_is=I_MPP1_SSM&indic_is=I_MPP1_SSX&indic_is=I_MPP1_SSZ&indic_is=I_MPP1_LST&indic_is=I_MPP1_RA&indic_is=I_MPP1_RAX&indic_is=I_MPP1_RAKX&indic_is=I_MPP1_RANA&unit=PC_IND&geo=BE&geo=BG&geo=CZ&geo=DK&geo=DE&geo=EE&geo=IE&geo=EL&geo=ES&geo=HR&geo=IT&geo=CY&geo=LV&geo=LT&geo=LU&geo=HU&geo=MT&geo=NL&geo=AT&geo=PL&geo=PT&geo=RO&geo=SI&geo=SK&geo=FI&geo=SE&geo=IS&geo=NO&geo=UK&geo=BA&geo=ME&geo=MK&geo=AL&geo=RS&geo=XK&time=2020">here</a>.
     * the order of research results present in the field 'value' is dictated by the following ranking logic:
     * <code>o = 35i + s</code>
     * Where 'o' represents the research outcome for a specific state in response to a particular research question, 'i' corresponds to the index of the current research question, and 's' represents the index of the current state
     * @author Vidal Spierings
     */

public class ApiHelper {

    /**
     *
     * All unknown research results have the value '0' for the percentage. 
     * when this value is inserted into this programs' {@code BarChart}, it visually represents an unknown research result, instead of a research result of 0%.
     * @param researchResultsApiJson the {@code JSONObject} containing the corrupted list of research results
     * @param indicatorLabelsList the {@code JSONObject} containing a list of all indicators (research questions)
     * @param currentStateIndex the int that represents the index for the current state
     * @return a corrected {@code HashMap} containg all research results, in the form of a 'research question | outcome percentage' key-value pair
     */
    public static HashMap<String, BigDecimal> convertToCorrectList(JSONObject researchResultsApiJson, JSONObject indicatorLabelsList, int currentStateIndex) {
		
		LinkedHashMap<Integer, BigDecimal> researchResultsListWithoutZerosValues = new LinkedHashMap<>();
		ArrayList<String> indicatorNamesList = new ArrayList<>();
		
		int maxIdentifierCounter = 0;
				
			for (Object key: researchResultsApiJson.keySet()) {
			
			researchResultsListWithoutZerosValues.put(Integer.valueOf(key.toString()), new BigDecimal(researchResultsApiJson.get(key).toString()));
			
			maxIdentifierCounter = Math.max(maxIdentifierCounter, Integer.valueOf(key.toString()));
									
		}
						
			/*
			 * 
			 * - convert the research results JSON object to a key-value pair using an HashMap wherein the key is the integer value of a given key
			 * within the JSON and the value of a given key-value pair is the value that belongs to set key within the JSON, converted to a BigDecimal.
			 * 
			 * - if a particular research result is unknown, for example research result 158,
			 *  that index/identifier is not added to the researchResultsListWithoutZerosValues HashMap.
			 * 
			 * -The last line, that sets the value of the variable maxIdentifierCounter, ensures that the real range of identifiers can be known.
			 * 
			 *  For example:
			 * 
			 *  If there are 349 research results, and 4 are missing from the API, the API get request will return a list of 345 research results,
			 *  whilst being unable to be aware of the fact that the intended range of the real list, including those whose research results are unknown,
			 *  is intended to be 349.
			 * 
			 *  The Math.max method checks, troughout the lifecycle of the whole for loop, whether the current identifier is the highest known number as of
			 *  the current number of loops that have taken place. This ensures that at the end of the for loop, the maxIdentifierCounter integer is the value of
			 *  the real range of research results
			 * 
			
			*/
									
			HashMap<Integer, BigDecimal> researchResultsListWithZerosValues = new HashMap<>();
															
			for (int i = 0; i <= maxIdentifierCounter; i++) {
				
				if (researchResultsListWithoutZerosValues.containsKey(i)) {
										
					researchResultsListWithZerosValues.put(i, researchResultsListWithoutZerosValues.get(i));
					
				}
								
				else {
															
					researchResultsListWithZerosValues.put(i, new BigDecimal("0"));
										
				}
				
			}
						
			/*
			 * 
			 * - creates a HashMap that contains the indexes/identifiers of unknown research results, 
			 *   and sets the values of set research results to a BigDecimal of 0 (meaning: A research result of 0%).
			 *   Even though it is theoretically possible that a research result is 0%, and not simply unknown,
			 *   this application will treat all research results with a 0% percentile as unknown research results
			 *   in the current phase of development.
			 * 
			 * This list does not contain the particular research results for the given state index.
			 * 
			*/
									
			for (Object key: indicatorLabelsList.keySet()) {
				
				indicatorNamesList.add((String) indicatorLabelsList.get(key));
				
			}
			
			// append indicatorLabelsList with all the indicators inside the indiatorLabelsList HashMap
					
		HashMap<String, BigDecimal> researchResultsListForState = new HashMap<>();
		
		int numIncrementsCounter = 0;
		
		for (int i = currentStateIndex;  i < researchResultsListWithZerosValues.size(); i += SelectableStatesUtil.countriesHashMap.size()) {
						
			researchResultsListForState.put((String) indicatorNamesList.get(numIncrementsCounter), researchResultsListWithZerosValues.get(i));
						
			numIncrementsCounter++;
			
		}
		
		numIncrementsCounter = 0;
		
		//System.out.println(researchResultsListForState);
						
		/*
		 * Create a list of all research results for the given state. This is achieved by doing the following:
		 * 
		 * 1: create a for loop for an integer (i) that starts at the index of the current state,
		 *    runs for the length of the list that contains all research results, and runs on incrementations of 35 (or more,
		 *    or less, but the conventional number of countries is 35 in the dataset).
		 *    For example: When intending to create a list of research results of Bulgaria, which has the index '1' within the API,
		 *    every 35th value, starting from 1, choronologically represents the research result of Bulgaria for the next indicator (research question)
		 *    
		 * 2: put the indicator, together with the correct associated research result, into a HashMap
		 * 
		 * 3: Increment the 'numIncrementsCounter' variable. This value keeps track of the amount of times an incrementation has taken place.
		 *    it DOES NOT keep track of the current incrementation, only the number of times an incrementation has taken place
		
		*/
		
		return researchResultsListForState;
		
	}
	
}
