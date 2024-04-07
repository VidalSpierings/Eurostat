package com.mycompany.eurostat.Views;

import java.sql.SQLException;

import com.mycompany.eurostat.Colors;
import com.mycompany.eurostat.SelectableStatesUtil;
import com.mycompany.eurostat.Models.VerbeteringVoorStaat;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.TextArea;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

/**
 * The View for the 'research results' screen.
 * @author Vidal Spierings
 */

public class VerbeteringVoorStaatView extends AbstractAppScreenPaneView {

	private ScrollPane scrollPane = null;
	private TextArea improvementNotionTextArea = null;
	private StringBuilder improvementNotionText = null;
	private AnchorPane improvementNotionTextFieldAnchorPane = null;
	private Rectangle deleteMiniFloatingActionButton = null;
	private VerbeteringVoorStaat verbeteringVoorStaatModel = null;
	private TextFlow improvementNotionTextFlow = null;

    /**
     *
     * @param screenTitle
     */
    public VerbeteringVoorStaatView(String screenTitle) {
						
		super(screenTitle);
                
                String currentlySelectedState = countriesComboBox.getSelectionModel().getSelectedItem();
		
		String stateCountryCode = SelectableStatesUtil.getKeyByValueForCountriesHashMap(currentlySelectedState);
						
		verbeteringVoorStaatModel = new VerbeteringVoorStaat(stateCountryCode);
		
		try {
			    		
    		improvementNotionText = verbeteringVoorStaatModel.getImprovementNotion();
                								
		} 
		
		/*
		
		* Please read comments in 'VerbeteringVoorStaat' on why stateCountryCode
		* has to be given up as a parameter twice in the constructor
		
		*/
    	
    	catch (SQLException se) {
    		            		
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error");
                alert.setContentText("cannot fulfill action, because a SQL related error has occurred: " + se.getMessage());
		        
                alert.showAndWait();
                
    	}
    	
    	catch (Exception e) {
    		            		
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error");
                alert.setContentText("cannot fulfill action, because an error has occurred: " + e.getMessage());
		        
                alert.showAndWait();    
                
	}
    	    	
	intialiseImprovementNotion();
				
        // initialise the UI and add appropriate OnMouseClicked functionality for the 'delete' button on the screen
							
	}

    /**
     * initialises the functionality of the floatingActionButton (in the form of a {@code Rectangle}) for this screen.
     */
    @Override
	protected void initialiseButtonFunctionality() {
		
		floatingActionButton.setOnMouseClicked(e -> editImprovementNotion());
		
		// enable functionality to edit, or at the very least de-nullify, the existing improvement notion
		
	}
	
    /**
     * Overrides the method from the inherited abstract class to allow for an extra 'delete' button to be added on top of the existing floatingActionButton.
     */
    @Override
	protected void initialiseCountrySelectorAndButtonLayout() {
		
		initialiseDeleteButton();
		
		VBox bottomLayoutComponentsLayoutVBoxLayer = new VBox();
		
		bottomLayoutComponentsLayout = new HBox();
		
		BorderPane.setMargin(bottomLayoutComponentsLayoutVBoxLayer, new Insets(10));
		
		countriesComboBox.getSelectionModel().selectFirst();
		
		bottomLayoutComponentsLayout.getChildren().addAll(countriesComboBox, spacer, floatingActionButton);
				
		bottomLayoutComponentsLayoutVBoxLayer.getChildren().addAll(deleteMiniFloatingActionButton, bottomLayoutComponentsLayout);
		
		bottomLayoutComponentsLayoutVBoxLayer.setAlignment(Pos.CENTER_RIGHT);
		
		setBottom(bottomLayoutComponentsLayoutVBoxLayer);
		
		/*
		
		* overrides the functionality for initialising the set of bottom layout components in order to add a 'delete'
		* button above the existing floating action button from the super class' layout template
		
		*/
		
		
	}
	
	private void nullifyImprovementNotion() {
								
		if (improvementNotionText != null && !improvementNotionText.toString().equals("null")) {
						
			try {
																
				verbeteringVoorStaatModel.setImprovementNotionToNull();
				
				improvementNotionTextFlow = new TextFlow(new Text(""));
				
				improvementNotionTextFlow.setVisible(false);
								
				improvementNotionText = new StringBuilder("null");
				
				Label noImprovementNotion = new Label("Er is nog geen verbeteringsnotie voor dit land. Klik rechtsonderin op de toevoegen knop om een verbeteringsnotie toe te voegen");
				
				this.setCenter(noImprovementNotion);
								
				BorderPane.setMargin(noImprovementNotion, new Insets(30));
								
				/*
				
				* clear the improvement notion and set the UI to the same state as when a user first enters the screen and
				* the improvement notion is found to be empty or null
				
				*/
				
			} 
			
			catch (SQLException se) {
				
                        Alert alert = new Alert(AlertType.ERROR);
                        alert.setTitle("Error");
                        alert.setContentText("cannot fulfill action, because a SQL related error has occurred: " + se.getMessage());
		        
                        alert.showAndWait(); 
                        
		}
			
			catch (Exception e) {
				
                        Alert alert = new Alert(AlertType.ERROR);
                        alert.setTitle("Error");
                        alert.setContentText("cannot fulfill action, because an error has occurred: " + e.getMessage());
		        
                        alert.showAndWait(); 
                        
		}
			
		} else {
						
			Alert alert = new Alert(AlertType.ERROR);
	        alert.setTitle("Error");
	        alert.setContentText("cannot delete a non-existing improvement notion");
	        
	        alert.showAndWait();
			
		}
		
	}

	private void editImprovementNotion() {
		
		scrollPane = null;
		
		// perform garbage collection on unused object / memory address
		
		improvementNotionTextFieldAnchorPane = new AnchorPane();
		
		improvementNotionTextArea = new TextArea();
		
		improvementNotionTextArea.setWrapText(true);
				
		if (improvementNotionText.toString().equals("null") || improvementNotionText == null) {
			
			improvementNotionTextArea.setPromptText("Enter improvement notion here");
			
		} else {
			
			improvementNotionTextArea.setText(improvementNotionText.toString());
			
		}
		
		/*
		
		* if improvement notion is null or doesn't exist, show a Label in the center of the screen informing the user about this.
		* 
		* if a non-empty improvement notion does exist, fill the TextArea with the improvement notion in question right away.
		* This results in a visual UX where the user understands that the TextArea that appears, is the exact layout component
		* that allows them to edit the existing improvement notion
		
		*/
		
		improvementNotionTextFieldAnchorPane.getChildren().add(improvementNotionTextArea);
		
		AnchorPane.setBottomAnchor(improvementNotionTextArea, 0.0);
		AnchorPane.setTopAnchor(improvementNotionTextArea, 0.0);
		AnchorPane.setLeftAnchor(improvementNotionTextArea, 0.0);
		AnchorPane.setRightAnchor(improvementNotionTextArea, 0.0);
		
		BorderPane.setMargin(improvementNotionTextFieldAnchorPane, new Insets(30));
		
		// show hint to user as to what to enter into the new textfield
		
		setCenter(improvementNotionTextFieldAnchorPane);
		
		initialiseDeleteButton();
		
		deleteMiniFloatingActionButton.setCursor(Cursor.DEFAULT);
		
		deleteMiniFloatingActionButton.setOnMouseClicked(e -> {});
		
		floatingActionButton.setOnMouseClicked(e -> submitEditedImprovementNotion());
		
	}

	private void submitEditedImprovementNotion() {
		                
            try {
                
                improvementNotionText = new StringBuilder(improvementNotionTextArea.getText());
                
                verbeteringVoorStaatModel.editImprovementNotion(improvementNotionText);
                
                intialiseImprovementNotion();
		
		deleteMiniFloatingActionButton.setCursor(Cursor.HAND);
		
		deleteMiniFloatingActionButton.setOnMouseClicked(e -> nullifyImprovementNotion());
		
		floatingActionButton.setOnMouseClicked(e -> editImprovementNotion());
		
		// submit improvement notion and set appriopriate onMouseClik functionality for the context of the current UI
            } 
            
            catch (SQLException se) {
            
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("cannot fulfill action, because a SQL-related error has occurred: " + se.getMessage());
		        
            alert.showAndWait(); 
            
            }
            
            
            catch (Exception ex) {
                
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("cannot fulfill action, because an error has occurred: " + ex.getMessage());
		        
            alert.showAndWait(); 
                
            }
		
	}
	
	private void intialiseImprovementNotion() {
		
		if (scrollPane != null) {
			
			scrollPane.setVisible(false);
			
		}
		
		if (improvementNotionTextFieldAnchorPane != null) {
			
			improvementNotionTextFieldAnchorPane.setVisible(false);
			
		}
		
		// disable irrelevant layout components for the current context
						
		if (improvementNotionText == null || improvementNotionText.toString().equals("null")) {
						
			Label noImprovementNotion = new Label("Er is nog geen verbeteringsnotie voor dit land. Klik rechtsonderin op de toevoegen knop om een verbeteringsnotie toe te voegen");
			
			this.setCenter(noImprovementNotion);
			
			BorderPane.setMargin(noImprovementNotion, new Insets(30));
			
			/*
			
			* if improvement notion is empty or null, inform the user by showing a label containing info in the center of the screen
			
			*/
			
		}
		
		else {
			
			scrollPane = new ScrollPane();
			
			improvementNotionTextFlow = new TextFlow(new Text(improvementNotionText.toString()));
																		
			scrollPane.setContent(improvementNotionTextFlow);
			
	        scrollPane.setStyle("-fx-background: #FFFFFF; -fx-border-color: #FFFFFF;");
									
			scrollPane.setHbarPolicy(ScrollBarPolicy.NEVER);
			
	        scrollPane.setFitToWidth(true);
	        
	        scrollPane.setPadding(new Insets(7.5));
	        	        
	        BorderPane.setMargin(scrollPane, new Insets(30));
	        															
			setCenter(scrollPane);
			
			scrollPane.setVisible(true);
			
			/*
			
			* initialise a text layout showing the improvement notion, which becomes scrollable
			* if not enough screen real estate is available to show the improvement notion in it's entirety
			
			*/
			
		}
		
	}
	
	private void initialiseDeleteButton() {
		
		deleteMiniFloatingActionButton = new Rectangle(15, 15, Color.web(Colors.primaryColor));
		
		deleteMiniFloatingActionButton.setCursor(Cursor.HAND);
		
		VBox.setMargin(deleteMiniFloatingActionButton, new Insets(0, 0, 10, 0));
				
		deleteMiniFloatingActionButton.setOnMouseClicked(e -> nullifyImprovementNotion());
				
	}

    /**
     * Initialises the functionality for when the user changes the selected country for this screen.
     */
    @Override
	protected void initialiseOnStateChangedFunctionality() {
		
		countriesComboBox.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                            	
            	try {
            		
            		verbeteringVoorStaatModel = new VerbeteringVoorStaat(
            				SelectableStatesUtil.getKeyByValueForCountriesHashMap(newValue));
            		
            		improvementNotionText = verbeteringVoorStaatModel.getImprovementNotion();
            		
            		intialiseImprovementNotion();
            												
				} 
            	
            	catch (SQLException se) {
            		            		
		Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error");
                alert.setContentText("cannot fulfill action, because a SQL related error has occurred: " + se.getMessage());
		        
                alert.showAndWait(); 
            		
            	}
            	
            	
            	catch (Exception e) {
            		            		
		Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error");
                alert.setContentText("cannot fulfill action, because an error has occurred: " + e.getMessage());
		        
                alert.showAndWait(); 
            		
	}
                
            // if user selects a different country, change layout accordingly
            	            	                
            }
        });
		
	}

}
