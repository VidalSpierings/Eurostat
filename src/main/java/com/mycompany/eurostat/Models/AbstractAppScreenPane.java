package com.mycompany.eurostat.Models;

import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import org.json.simple.JSONObject;
import com.mycompany.eurostat.DAO;
import com.mycompany.eurostat.SelectableStatesUtil;

/**
 * The AbstractAppScreenPane class is the Model class for the UI template for any screen that shows up to the right of the Navigation drawer menu.
 * @author Vidal Spierings
 */

public class AbstractAppScreenPane {
    
    /**
     * Synchronizes the most recently present countries in the API with the countries present in the database table that also contains the basale veiligheid for set countries
     *
     * @throws MalformedURLException
     * @throws UnknownHostException
     * @throws SQLException
     * @throws Exception
     */
	
public void synchroniseAllCountriesWithImprovementNotionTable() throws MalformedURLException, UnknownHostException, SQLException, Exception {
		
		try {
			
			JSONObject apiJson = new DAO().fetchDataFromApi();
			
			JSONObject dimensionObject = (JSONObject) apiJson.get("dimension");
			
			JSONObject geoObject = (JSONObject) dimensionObject.get("geo");
			
			JSONObject categoryObject = (JSONObject) geoObject.get("category");
			
			JSONObject labelObject = (JSONObject) categoryObject.get("label");
			
			SelectableStatesUtil.countriesHashMap.clear();
                        
                        // clear (possible) previous population
			
			for (Object key: labelObject.keySet()) {
				
				SelectableStatesUtil.countriesHashMap.put(key.toString(), labelObject.get(key).toString());
										
			}
                        
                        // repopulate with most recent data from the API
						
			synchroniseJSONSetWithDatabase();
												
		}
		
		catch (MalformedURLException e) {
			
			System.out.println(e.getMessage());
			
			throw new MalformedURLException();
			
		}
		
		catch (UnknownHostException uhe) {
			
			System.out.println(uhe.getMessage());
			
			throw new UnknownHostException();
			
		}
		
		catch (SQLException se) {
                    
                        System.out.println(se.getMessage());
			
			throw new SQLException();
			
		}
		
		catch (Exception e) {
			
			System.out.println(e.getMessage());
			
			throw new Exception();
			
		}
		
		
		
	}

        private void synchroniseJSONSetWithDatabase() throws SQLException, Exception {
		
		Connection con = new DAO().createDatabaseConnection();
		
		for (Object key : SelectableStatesUtil.countriesHashMap.keySet()) {
						
			try{
				
				String sqlInsertQueryString = String.format(
		                "INSERT IGNORE INTO verbeteringsnotiesvoorstaten VALUES ('%s', null)",
		           key.toString());
                                
                    /*
                    * Insert standard value for previously non-existent country. 
                    * If country is already in database table, skip (IGNORE) insertion
                    */
				
	            Statement stat = con.createStatement();
	            int result = stat.executeUpdate(sqlInsertQueryString);
	            
	            if(result == 1 || result == 0){
	            	
	                System.out.println("Operatie uitgevoerd");  
	                
	            }
                    
                    // inform console when database request was succesful
	                        
	        }catch(SQLException se){
	            
	            System.out.println(se.getMessage());     
	            
	            throw new SQLException();
	            
	        }catch (Exception e) {
	        	
	            System.out.println(e.getMessage());     

	            throw new Exception();
	        	
	        }
			
		}
		
		try {
			
	        String sqlSelectQueryString = "SELECT * FROM verbeteringsnotiesvoorstaten";
	        
            Statement stat = con.createStatement();
	        
	        ResultSet resultSet = stat.executeQuery(sqlSelectQueryString);
	        
	        ArrayList<String> allCountriesInDatabase = new ArrayList<>();
	        
	        while (resultSet.next()) {
	        	
	        	allCountriesInDatabase.add(resultSet.getString("state"));
	        	
	        }
	        			
	        for (String country : allCountriesInDatabase) {
                    
                    // for every country that is registered in the database
                    
                    // Check how countries from API line up with countries in database
	        	
	        	if (!SelectableStatesUtil.countriesHashMap.containsKey(country)) {
                            		        	
		        	String sqlDeleteQueryString = String.format(
			                "DELETE FROM verbeteringsnotiesvoorstaten WHERE state = '%s'",
			                country);
		        	
			        int result = stat.executeUpdate(sqlDeleteQueryString);
			        
			        if(result == 1 || result == 0){
		            	
		                System.out.println("Operatie uitgevoerd, land verwijderd"); 
                                
                                // inform console when database request was succesful
		                
		            }
		        	
		        }
	        	
	        }
			
		} catch (SQLException se) {
			
			System.out.println(se.getMessage());
			
			throw new SQLException();
			
		} catch (Exception e) {
			
			System.out.println(e.getMessage());
			
			throw new Exception();
			
		}
		
		try{
			
            con.close();
            
        } catch(SQLException se){
        	
            System.out.println(se.getMessage());
            
            throw new SQLException();
            
        } catch (Exception e) {
        	
            System.out.println(e.getMessage());
        	
            throw new Exception();
            
        }
		
	}

}
