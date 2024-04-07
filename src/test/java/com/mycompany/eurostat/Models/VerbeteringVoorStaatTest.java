/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package com.mycompany.eurostat.Models;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import org.junit.jupiter.api.Test;
/**
 * Test class for testing the 'Read' and 'Update' functionality from the CRUD-paradigm for the applications' database.
 * @author Vidal Spierings
 */
public class VerbeteringVoorStaatTest {
    
    @Test
    void testRead() {
        
        var model = new VerbeteringVoorStaat("NL");
        
        assertDoesNotThrow(() -> model.getImprovementNotion());
        
        //IMPORTANT: Before running tests, ensure that the given country name exists in the database table
        
    }
    
    @Test
    void testSetToNull() {
        
        var model = new VerbeteringVoorStaat("NL");
        
        assertDoesNotThrow(() -> model.setImprovementNotionToNull());
        
        //IMPORTANT: Before running tests, ensure that the given country name exists in the database table
        
    }
    
    @Test
    void testUpdate() {
        
        var model = new VerbeteringVoorStaat("NL");
        
        assertDoesNotThrow(() -> model.editImprovementNotion(new StringBuilder("THIS IMPROVEMENT_NOTION WAS CREATED BY A TEST CASE")));
        
        //IMPORTANT: Before running tests, ensure that the given country name exists in the database table
        
    }

    
}
