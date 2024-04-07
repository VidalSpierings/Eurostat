package com.mycompany.eurostat.Models;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

/**
 * Model class for the 'causes' screen.
 */

import com.mycompany.eurostat.DAO;

/**
 *
 * @author Vidal Spierings
 */
public class GevolgVoorStaat implements StaatParadigma {

	String state;
        HashMap<Integer, StringBuilder> currentEffectsForState = new HashMap<>();
	
    /**
     *
     * @param state
     */
    public GevolgVoorStaat(String state) {
		
		this.state = state;
		
	}

	@Override
	public String myToString() {
            
            System.out.println(currentEffectsForState.toString());
		
            return currentEffectsForState.toString();
            
	}
        
        /**
     * Retrieves effects for the current state in the form of an id-effect key-value pair in the form of a HashMap
     * 
     * @return A HashMap containing id-effect key-value pair in the form of a HashMap.
     * @throws SQLException
     * @throws Exception
     */
	
	public HashMap<Integer, StringBuilder> getEffectsForState() throws SQLException, Exception {
		
		HashMap<Integer, StringBuilder> listOfEffects = new HashMap<>();
				
		try{
			
			DAO dao = new DAO();
			
            Statement stat = dao.createDatabaseConnection().createStatement();
            
            // prepare SQL statement to be executed to correct connection and database schema
            
            ResultSet result = stat.executeQuery(String.format("SELECT * FROM gevolgenvoorstaten WHERE state = '%s'", state));
            
            while(result.next()){
            	
                int retrievedID = result.getInt("id");
                
                StringBuilder retrievedEffect = new StringBuilder(result.getString("effect"));
                
                listOfEffects.put(retrievedID, retrievedEffect);
                
            }
            
            currentEffectsForState = listOfEffects;
            
            myToString();
            
            /*
            
             * retrieve all the id's for the effects of the given state and the effects themselves, and organise these into a key-value list using a HashMap list.
             * 
             * This approach ensures that the correct effects can be deleted by the application, if so desired by the user,
             * keeping into account the primary key paradigm of the gevolgenvoorstaten column in the database
             
             */
            
        } catch(SQLException se){
        	
            System.out.println(se.getMessage());
            
            throw new SQLException();
            
         // If SQLException is caught, show error message in console, than throw exception to higher level (all the way up to View layer).
            
        }
		
		catch (Exception e) {
			
		System.out.println(e.getMessage());
            
		throw new Exception();
				
		// If any other Exception is caught, show error message in console, than throw exception to higher level (all the way up to View layer)
			
		}
		
		return listOfEffects;
		
	}
	
        /**
     * Deletes an effect for a specified ID of a country
     * 
     * @param id     The ID of the effect
     * @param effect The effect to be deleted
     * @throws SQLException
     * @throws Exception
     */
        
	public void deleteEffectForState(int id, StringBuilder effect) throws SQLException, Exception {
            
            myToString();
		
		Connection con = null;
            
		try{
						
                        con = new DAO().createDatabaseConnection();
			
			Statement stat = con.createStatement();            
                        // prepare SQL statement to be executed to correct connection and database schema
            
            String sqlDeleteCauseString = String.format(
                    "DELETE FROM gevolgenvoorstaten WHERE (id, state) = (%d, '%s')", id, state);
            
            int result = stat.executeUpdate(sqlDeleteCauseString);
                                    
            if(result == 1 || result == 0){
	        	
	            System.out.println("effect deleted for: " + state);  
	            
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
     * Adds an effect for a state
     * 
     * @param effect The effect that ought to be added
     * @throws SQLException
     * @throws Exception
     */
	
	public void addEffectForState(StringBuilder effect) throws SQLException, Exception {
            
            myToString();
		
		String addEffectSQLMethodQuery = String.format("SET @next_id = (SELECT IFNULL(MAX(id), 0) + 1 FROM gevolgenvoorstaten); \n" +
    "/* create variable that stores a method, and call it later. this prevents MySQL issues with updating target table in from clause */\n",
                    state, effect);
                
                /*
                
                * create variable that stores a method, and call it later.
                * this prevents MySQL issues with updating target table in from clause
                        
                */
            
            String insertEffectSQLQuery = String.format("INSERT INTO gevolgenvoorstaten VALUES (@next_id, '%s', '%s');", state, effect);
            
            Connection con = null;
            
            try{
                
                con = new DAO().createDatabaseConnection();
                Statement stat = con.createStatement();
                int result1 = stat.executeUpdate(addEffectSQLMethodQuery);
                int result2 = stat.executeUpdate(insertEffectSQLQuery);
                
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
                    
                }
                catch (Exception e) {
                
                    System.out.println(e.getMessage());
                    
                    throw new Exception();
                
                }
            }
            
	}

}
