/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit4TestClass.java to edit this template
 */
package com.mycompany.eurostat.Models;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import org.junit.jupiter.api.Test;

/** 
 * Test class for testing the 'Create' and 'Delete' functionality from the CRUD-paradigm for the applications' database.
 * @author Vidal Spierings
 */
public class GevolgVoorStaatTest {
    
    @Test
    void testCreate(){
        
        var model = new GevolgVoorStaat("NL");
        
        assertDoesNotThrow(() -> model.addEffectForState(new StringBuilder("THIS CAUSE WAS CREATED BY A TEST CASE")));
    
        //IMPORTANT: Before running tests, ensure that the given country name exists in the database table 'gevolgenvoorstaten'
    
    }
    
    @Test
    void testDelete(){
    
        var model = new GevolgVoorStaat("NL");
        
        assertDoesNotThrow(() -> model.deleteEffectForState(1, new StringBuilder("")));
    
        //IMPORTANT: Before running tests, ensure that the correct state-id relation exists in the database table 'gevolgenvoorstaten'
    
    }
    
}
