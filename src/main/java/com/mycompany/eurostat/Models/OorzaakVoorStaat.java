package com.mycompany.eurostat.Models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

import com.mycompany.eurostat.DAO;
import java.sql.Connection;

/**
 * Model class for the 'effects' screen.
 * @author Vidal Spierings
 */

public class OorzaakVoorStaat implements StaatParadigma {
	
	private String state;
        
        private HashMap<Integer, StringBuilder> currentListOfKnownCauses = null;
	
    /**
     *
     * @param state
     */
    public OorzaakVoorStaat(String state) {
		
		this.state = state;
		
	}
        
        /**
     * Retrieves causes for the current state in the form of an id-cause key-value pair in the form of a HashMap
     * 
     * @return A HashMap containing id-cause key-value pair in the form of a HashMap.
     * @throws SQLException
     * @throws Exception
     */

        public HashMap<Integer, StringBuilder> getCausesForState() throws SQLException, Exception{
		
		HashMap<Integer, StringBuilder> listOfCauses = new HashMap<>();
				
		try{
			
			DAO dao = new DAO();
			
            Statement stat = dao.createDatabaseConnection().createStatement();
            
            // prepare SQL statement to be executed to correct connection and database schema
            
            ResultSet result = stat.executeQuery(String.format("SELECT * FROM oorzakenvoorstaten WHERE state = '%s'", state));
            
            while(result.next()){
            	
                int retrievedID = result.getInt("id");
                
                StringBuilder retrievedCause = new StringBuilder(result.getString("cause"));
                
                listOfCauses.put(retrievedID, retrievedCause);
                
            }
            
            currentListOfKnownCauses = new HashMap<>();
            
            currentListOfKnownCauses = listOfCauses;
            
            myToString();
            
            /*
            
             * retrieve all the id's for the causes of the given state and the causes themselves, and organise these into a key-value list using a HashMap list.
             * 
             * This approach ensures that the correct causes can be deleted by the application, if so desired by the user,
             * keeping into account the primary key paradigm of the oorzakenvoorstaten column in the database
             
             */
            
        } catch(SQLException se){
        	
            System.out.println(se.getMessage());
            
            throw new SQLException();
            
         // If SQLException is caught, show error message in console, than throw exception to higher level (all the way up to View layer).
			
            /*
            			
            * Please see following link for elaboration:
            * https://docs.oracle.com/javase/8/docs/api/java/sql/SQLException.html
            			
            */
            
        }
		
		catch (Exception e) {
			
				System.out.println(e.getMessage());
            
				throw new Exception();
				
				// If any other Exception is caught, show error message in console, than throw exception to higher level (all the way up to View layer)
			
		}
                
                myToString();
		
		return listOfCauses;
		
	}
        
          /**
     * Deletes a cause for a specified ID of a country
     * 
     * @param id     The ID of the cause
     * @param cause The cause to be deleted
     * @throws SQLException {@code SQLException}
     * @throws Exception    {@code Exception}
     */
	
	public void deleteCauseForState(int id, StringBuilder cause) throws SQLException, Exception {
            
            myToString();
		
                Connection con = null;
            
		try{
						
                        con = new DAO().createDatabaseConnection();
			
			Statement stat = con.createStatement();            
                        // prepare SQL statement to be executed to correct connection and database schema
            
            String sqlDeleteEffectString = String.format(
                    "DELETE FROM oorzakenvoorstaten WHERE (id, state) = (%d, '%s')", id, state);
            
            int result = stat.executeUpdate(sqlDeleteEffectString);
                                    
            if(result == 1 || result == 0){
	        	
	            System.out.println("cause deleted for: " + state);  
	            
	        }
                        
            // inform console when database request was succesful
            
        } catch(SQLException se){
        	
            System.out.println(se.getMessage());
                        
            throw new SQLException();
            
         // If SQLException is caught, show error message in console, than throw exception to higher level (all the way up to View layer).
			
            /*
            			
            * Please see following link for elaboration:
            * https://docs.oracle.com/javase/8/docs/api/java/sql/SQLException.html
            			
            */
            
        }
		
	catch (Exception e) {
			
		System.out.println(e.getMessage());
				            
		throw new Exception();
				
			// If any other Exception is caught, show error message in console, than throw exception to higher level (all the way up to View layer)
			
		}		
	}
        
        /**
     * Adds a cause for a state
     * 
     * @param cause The cause that ought to be added
     * @return 
     * @throws SQLException {@code SQLException}
     * @throws Exception    {@code Exception}
     */
	
	public ArrayList<StringBuilder> addCauseForState(StringBuilder cause) throws SQLException, Exception {
            
            myToString();
            
            String addCauseSQLMethodQuery = String.format("SET @next_id = (SELECT IFNULL(MAX(id), 0) + 1 FROM oorzakenvoorstaten); \n" +
    "/* create variable that stores a method, and call it later. this prevents MySQL issues with updating target table in from clause */\n",
                    state, cause);
            
            /*
                
                * create variable that stores a method, and call it later.
                * this prevents MySQL issues with updating target table in from clause
                        
                */
            
            String insertCauseSQLQuery = String.format(
                    "INSERT INTO oorzakenvoorstaten VALUES ('%s', @next_id, '%s');", state, cause);
            
            Connection con = null;
            
            try{
                
                con = new DAO().createDatabaseConnection();
                Statement stat = con.createStatement();
                int result1 = stat.executeUpdate(addCauseSQLMethodQuery);
                int result2 = stat.executeUpdate(insertCauseSQLQuery);
                
                if((result1 == 1 || result1 == 0) && (result2 == 1 || result2 == 0)){
                    
                    System.out.println("Operatie succesvol uitgevoerd, oorzaak toegevoegd voor: " + state);
                    
                }
                
                // inform console when both database requests were succesful
                
            } catch(SQLException se){
                
                System.out.println(se.getMessage());
                
                throw new SQLException();
                
            } catch (Exception e) {
            
            System.out.println(e.getMessage());
            
            throw new Exception();
            
            } 
            
            finally{
                
                try{
                    con.close();
                }catch(SQLException se){
                    
                    System.out.println(se.getMessage());
                    
                    throw new SQLException();
                    
                } catch (Exception e) {
                
                    System.out.println(e.getMessage());
                    
                    throw new Exception();
                
                }
            }
    
            /*
    
        - Doet een poging om een nieuwe record aan te maken in de klant database tafel. Indien dit niet lukt,
          Geef weer waarom
        
        */
		
		return null;
		
	}

	@Override
	public String myToString() {
            
                System.out.println(currentListOfKnownCauses.toString());
            
		return currentListOfKnownCauses.toString();
	}

}
