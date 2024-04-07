package com.mycompany.eurostat;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javafx.scene.control.Alert;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 * The DAO class is responsible for handling data connections to external API's.
 * 
 * @see <a href="https://en.wikipedia.org/wiki/Data_access_object">Data Access Object (DAO)</a>
 * @author Vidal Spierings
 */

public class DAO {
    
    /**
     * Establishes a connection to the database using the provided constants for URL, username, and password.
     * 
     * @return A {@code Connection}
     * @throws SQLException {@code SQLException}
     * @throws Exception {@code Exception}
     */
	
	public Connection createDatabaseConnection() throws SQLException, Exception {
		
		try {
			
			Connection connection = DriverManager.getConnection(Constants.DATABASE_LINK, Constants.DATABASE_USER_USERNAME, Constants.DATABASE_USER_PASSWORD);
						
			return connection;
			
			// make an attempt to connect to database with correct username and password
			
		} catch (SQLException e) {
			
			System.out.println(e.getMessage());
			
			throw new SQLException();
			
			// If SQLException is caught, show error message in console, than throw exception to higher level (all the way up to View layer).
			
				
		}
		
		catch (Exception e) {
			
			System.out.println(e.getMessage());
			
			throw new Exception();
			
			// If any other Exception is caught, show error message in console, than throw exception to higher level (all the way up to View layer)
			
		}
				
	}
        
        /**
     * Fetches API data
     * 
     * @return A {@code JSONObject} containing the entire API
     * @throws Exception {@code Exception}
     * @throws MalformedURLException {@code MalformedURLException}
     * @throws UnknownHostException  {@code UnknownHostException}
     */

		public JSONObject fetchDataFromApi() throws Exception, MalformedURLException, UnknownHostException {
	
		JSONParser jsonParser = new JSONParser();
		
		// object from JSON.simple (Google) libary that can parse retrieved JSON objects
		
		StringBuilder result = new StringBuilder();
		HttpURLConnection connection = null;
		
		try {
			 
			URL apiUrl = URI.create(Constants.API_LINK).toURL();
			
			connection = (HttpURLConnection) apiUrl.openConnection();
	        connection.setRequestMethod("GET");
	        
	        /*
	        
	        * create connection to the API. The API's relationship to this program is unidirectional, meaning, in this context,
	          that data only needs to be retrieved from the API, and not send to the API. Hence the 'GET' request method
	        
	        */

	        try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
	        	
	            String line;
	            
	            while ((line = reader.readLine()) != null) {
	            	
	                result.append(line);
	                
	            }
	            
	            /*
	            
	            * Attempt to add input stream, which is an abstraction of the API, to a StringBuilder object, line for line.
	            * 
	            * Every time a new line is read from the input stream, add this line to a String object, and append the StringBuilder object with this String object,
	            * over and over again until every line of the input stream is added to the StringBuilder object
	            
	            */
			
		} catch (MalformedURLException e) {
			
			System.out.println(e.getMessage());
			
			throw new MalformedURLException();
			
			/*
			
			* if no legal protocol was found for the String specification, or the string could not be parsed,
			* show error message in console, than throw exception to higher level (all the way up to View layer) 
			
			*/
			
		}
		
		catch (UnknownHostException e) {
			
			System.out.println(e.getMessage());
        				        	
        	throw new UnknownHostException();
        	
        	/*
        	
        	* If no host was found for link (for example: Problems with the API itself that are outside the scope of control of this program), 
        	* show error message in console, than throw exception to higher level (all the way up to View layer)
        	
        	*/
        	
        }
	        
		} catch (Exception e) {
        	
        	System.out.println(e.getMessage());
        	
        	throw new Exception();
        	
        	// if any other exception occurs, show error message in console, than throw exception to higher level (all the way up to View layer)
        	
        }
		
		finally {
			
			try {
				
			connection.disconnect();	
				
			}
                        
                        
                        catch (Exception e) {

			System.out.println(e.getMessage());
        	
                        throw new Exception();	
                            
			}
			
			/*
			
			* Attempt to disconnect from API when all data is correctly read into the program, and an active connection is no longer necessary.
			* If a connection wasn't established in the first place, do nothing
			
			*/
			
		}

        return (JSONObject) jsonParser.parse(result.toString());
        
        // convert StringBuilder object to an Object and cast this Object to a JSONObject, which results in a JSONObject that contains the whole API
		
	}

}
