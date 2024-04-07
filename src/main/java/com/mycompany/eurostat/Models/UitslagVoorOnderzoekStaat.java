package com.mycompany.eurostat.Models;

import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.util.HashMap;

import org.json.simple.JSONObject;
import com.mycompany.eurostat.ApiHelper;
import com.mycompany.eurostat.DAO;

/**
 * Model class for the 'research results' screen.
 * @author Vidal Spierings
 */

public class UitslagVoorOnderzoekStaat implements StaatParadigma {
	
	private String state;
        private HashMap<String, BigDecimal> currentResultsForState = null;

    /**
     *
     * @param state
     */
    public UitslagVoorOnderzoekStaat(String state) {
            
		this.state = state;
		
	}

	@Override
	public String myToString() {
            
                System.out.println(currentResultsForState.toString());
		
		return currentResultsForState.toString();
                
	}
        
        /**
     * Retrieves the research results for the specified state and converts them into a HashMap.
     *
     * @param currentStateIndex the current index for the state in question
     * @return A |research question  research result| key-value pair in the form of a HashMap
     * @throws MalformedURLException {@code MalformedURLException}
     * @throws Exception             {@code Exception}
     */
	
	public HashMap<String, BigDecimal> getUitslagenVoorStaat(int currentStateIndex) throws MalformedURLException, Exception {
				
		DAO dao = new DAO();
		
		JSONObject fetchedJson = dao.fetchDataFromApi();
                		
		JSONObject researchResultsJsonObject = (JSONObject) fetchedJson.get("value");
		
		JSONObject dimensionObject = (JSONObject) fetchedJson.get("dimension");
		
		JSONObject indicatorObject = (JSONObject) dimensionObject.get("indic_is");
		
		JSONObject categoriesObject = (JSONObject) indicatorObject.get("category");
		
		JSONObject indicatorLabelsList = (JSONObject) categoriesObject.get("label");
		
		HashMap<String, BigDecimal> convertedResearchResultsList = 
				ApiHelper.convertToCorrectList(researchResultsJsonObject, indicatorLabelsList, currentStateIndex);
		
		/*
		 * get the 'value' and 'label' objects from the API, which contains all the research results,
		 * and convert this into a list that contains all the research results for a given state using application-level AI / various algorithms.
		 * 
		 * (the value 'label' is nested inside dimension -> indic_is -> category -> label)
		
		
		*/
                
                currentResultsForState = convertedResearchResultsList;
                
                myToString();
		
		return convertedResearchResultsList;
		
	}
        
        /**
     * Retrieves the index of the current state
     *
     * @return The index for the current state
     * @throws MalformedURLException {@code MalformedURLException}
     * @throws Exception             {@code Exception}
     */
        
        public int getCurrentStateIndex() throws MalformedURLException, Exception {
        
            DAO dao = new DAO();
		
            JSONObject fetchedJson = dao.fetchDataFromApi();
            
            JSONObject dimensionObject = (JSONObject) fetchedJson.get("dimension");
            
            JSONObject geoObject = (JSONObject) dimensionObject.get("geo");
            
            JSONObject categoryObject = (JSONObject) geoObject.get("category");
            
            JSONObject indexObject = (JSONObject) categoryObject.get("index");
            
            Long indexObjectLong = (Long) indexObject.get(state);
            
            // get the indexes corresponding to all states, as per defined in the API itself
                        
            return indexObjectLong.intValue();
        
        }

}
