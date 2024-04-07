package com.mycompany.eurostat.Models;

import com.mycompany.eurostat.DAO;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Model class for the 'improvement notion' screen.
 * @author Vidal Spierings
 */

public class VerbeteringVoorStaat implements StaatParadigma {
	
	String state;
        String currentImprovementNotion = null;
        String resultImprovementNotion = null;
	
    /**
     *
     * @param state
     */
    public VerbeteringVoorStaat(String state) {
		
		this.state = state;
		
	}

	@Override
	public String myToString() {
		
            System.out.println(currentImprovementNotion);
            
		return currentImprovementNotion;
	}
        
        /**
     * Retrieves the improvement notion for the state.
     *
     * @return the current improvement notion in the form of a {@code StringBuilder} object
     * @throws SQLException {@code SQLException}
     * @throws Exception    {@code Exception}
     */
	
	public StringBuilder getImprovementNotion() throws SQLException, Exception {
                        				
		// this method needs to know state here explicitly, because the selected state can change after this class' initialisation
		
		ResultSet result = null;
		
		try{
			
			DAO dao = new DAO();
			
            Statement stat = dao.createDatabaseConnection().createStatement();
            
            // prepare SQL statement to be executed to correct connection and database schema
            
            result = stat.executeQuery(String.format("SELECT improvement_notion FROM verbeteringsnotiesvoorstaten WHERE state = '%s'", state));
            
            result.next();
                                    
            if (result.getString("improvement_notion") == null) {
                
                currentImprovementNotion = new StringBuilder("null").toString();
                                
                myToString();
            	
        	return new StringBuilder("null");
                
                // if there is no improvement notion for the given state, return a StringBuilder with the content "null"
            	
            } else {
                
                        currentImprovementNotion = result.getString("improvement_notion");
                        
                        myToString();
            	
        		return new StringBuilder(result.getString("improvement_notion"));
            	
                        // if an improvement notion for the given state exists, return it
                        
            }
                                    
            /*
            
             * retrieve all the id's for the effects of the given state and the causes themselves, and organise these into a key-value list using a HashMap list.
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
                                
	}
	
        /**
     * Sets improvement notion to null for the state
     *
     * @throws SQLException {@code SQLException}
     * @throws Exception    {@code Exception}
     */
        
	public void setImprovementNotionToNull() throws SQLException, Exception { 
            
            myToString();
				
		Connection con = null;
		
		try {
			
			con = new DAO().createDatabaseConnection();
			
			Statement stat = con.createStatement();
			
			String sqlSetNotionToNullString = String.format(
	                "UPDATE verbeteringsnotiesvoorstaten SET improvement_notion = null WHERE state = '%s'",
	                state);
	    	
	        int result = stat.executeUpdate(sqlSetNotionToNullString);
	        
	        if(result == 1 || result == 0){
	        	
	            System.out.println("improveent notion set to null for: " + state);  
	            
	        }
                
                // inform console when database request was succesful
	        			
		} catch (SQLException se) {
			
			System.out.println(se.getMessage());
			
			throw new SQLException();

			
		} catch (Exception e) {
			
			System.out.println(e.getMessage());
			
			throw new Exception();
			
		}
		
	}
        
        /**
     * Updates the improvement notion for the state
     *
     * @param newImprovementNotion The value that the new improvement notion ought to be set to
     * @throws SQLException {@code SQLException}
     * @throws Exception    {@code Exception}
     */
	
	public void editImprovementNotion(StringBuilder newImprovementNotion) throws SQLException, Exception {
            
            currentImprovementNotion = newImprovementNotion.toString();
            
                myToString();
		
		Connection con = null;
		
		try {
			
			con = new DAO().createDatabaseConnection();
			
			Statement stat = con.createStatement();
			
			String sqlSetNotionToNullString = String.format(
	                "UPDATE verbeteringsnotiesvoorstaten SET improvement_notion = '%s' WHERE state = '%s'",
	                newImprovementNotion.toString(), state);
	    	
	        int result = stat.executeUpdate(sqlSetNotionToNullString);
	        
	        if(result == 1 || result == 0){
	        	
	            System.out.println("improveent notion updated for: " + state);  
	            
	        }
                
                // inform console when database request was succesful
	        			
		} catch (SQLException se) {
			
			System.out.println(se.getMessage());
			
			throw new SQLException();

			
		} catch (Exception e) {
			
			System.out.println(e.getMessage());
			
			throw new Exception();
			
		}
		
	}
        

}
