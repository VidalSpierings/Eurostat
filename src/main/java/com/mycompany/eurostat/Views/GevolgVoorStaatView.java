package com.mycompany.eurostat.Views;

import com.mycompany.eurostat.Models.GevolgVoorStaat;
import com.mycompany.eurostat.SelectableStatesUtil;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Alert;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

/**
 * The View for the 'effects' screen.
 * No Gevolg-Oorzaak generalisation paradigm made, because class already extends AbstractAppScreenPaneView
 * @author Vidal Spierings
 */

public class GevolgVoorStaatView extends AbstractAppScreenPaneView {
        
    private ListView<String> effectsListView = null;
    private TextArea newEffectTextArea = null;
    private String enterNewEffectCode = null;
    private GevolgVoorStaat gevolgVoorStaatModel = null;
    private List<String> effectsList = null;
    HashMap<Integer, StringBuilder> listOfEffectsStringBuilder = null;

    /**
 * The 'enterNewEffectCode' String within this method allows for the app to function properly when the user decides to enter a new effect.
 * The functionality works as follows:
 * 
 * - All effects for the chosen state are entered into the 'effectsList' ArrayList.
 *   As readable in the overriding of the 'updateItem' method, it is pretty much the case
 *   that every item that is not empty (so no text), and every item that is not equal
 *   to the 'enterNewEffectCode' (so not containing the exact order of hieroglyphics that
 *   the 'enterNewEffectCode' consists of) is added as a '(white) effect item' into the
 *   ListView
 *   
 * - The enterNewEffectCode consists of characters from an extinct language. There is absolutely
 *   no scenario where someone would have to exclusively add Egyptian hieroglyphics as an 'effect'
 *   within the context of this application. Even when translated to a modern language, 
 *   according to ChatGPT, this sequence of characters translates simply to gibberish
 *
 * depending on external circumstances, the contents of the string might look like square boxes,
 * but they are in fact unicode characters for Hieroglyhpics
     * @param screenTitle the title of the current screen that ought be shown to the user in the top of the screen
 */
    
	public GevolgVoorStaatView(String screenTitle) {
		
		super(screenTitle);
								
		String currentlySelectedState = countriesComboBox.getSelectionModel().getSelectedItem();
		
		String stateCountryCode = SelectableStatesUtil.getKeyByValueForCountriesHashMap(currentlySelectedState);
                
                effectsList = new ArrayList();
                                
                gevolgVoorStaatModel = new GevolgVoorStaat(stateCountryCode);
		
		enterNewEffectCode = "𓀇𓁄𓂃𓃁𓄂𓅆𓆋𓇑𓈄𓉀𓊇";
				
		initialiseListView(true, false);
						
	}
        
        private void initialiseListView(boolean deleteButtonIsClickable, boolean addTextArea) {
            
            try {
                
                effectsList.clear();
                
                //clear (possible) previous selection
                
                effectsListView = new ListView<>();
                                
                listOfEffectsStringBuilder = new HashMap<>();
            
                listOfEffectsStringBuilder = gevolgVoorStaatModel.getEffectsForState();
                                
                for (StringBuilder value : listOfEffectsStringBuilder.values()) {
                
                    effectsList.add(value.toString());
                    
                }
                
                if (addTextArea) {effectsList.add(enterNewEffectCode);}
                                                                                				
		BackgroundFill bf = new BackgroundFill(Color.rgb(0, 0, 0, 0), CornerRadii.EMPTY, Insets.EMPTY);
		
		// completely opaque background
                				
		effectsListView.setBackground(new Background(bf));
                		
		effectsListView.getItems().addAll(effectsList);
										
		effectsListView.setCellFactory(param -> new ListCell<String>() {

	@Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                                
                setPrefWidth(0);
                
                setBackground(new Background(bf));
                
                /*
                
                * the above line ensures that when the text for an effect is so long that it reaches the horizontal border of it's parent view,
                * the textFlow extends vertically, and not horizontally,
                * also keeping into consideration all different possible window sizes
                
                */
                
                if (item != null && item.equals(enterNewEffectCode)) {
                                    	
                	/*
                	
                	* if item is equal to the code for adding a TextArea
                	* to the ListView where the user can fill in a new cause
                	
                	*/
                	
                	System.out.println("new effect insert detected");
                	
                	newEffectTextArea = new TextArea();
                	
                	newEffectTextArea.setWrapText(true);
                	                	
                	newEffectTextArea.setMaxSize(600, 600);
                	
                	setAlignment(Pos.CENTER);
                	
                	setGraphic(newEffectTextArea);
                	
                	/*
                	
                	* add TextArea with appropriate alignment, so it visibly appears directly underneath the last effect,
                	* and not all the way to the left of the screen
                	
                	*/
                	                	
                }

                else if (item != null && !item.equals(enterNewEffectCode)) {
                	                	
            		Rectangle deleteButton = new Rectangle(10, 10, Color.web("000000"));
            		                	                	                	
                	TextFlow textFlow = new TextFlow(new Text(item));
                	
                	textFlow.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
                	                	                	
                    HBox hbox = new HBox();
                                                                                                                        
                    hbox.getChildren().addAll(textFlow, deleteButton);
                    hbox.setAlignment(Pos.CENTER);

                    setGraphic(hbox);
                    
                    /*
                    
                    * add the effect to the ListCell, along with a white background, and a button to delete set cell
                    * (and it's contents from the database)
                    
                    */
                    
                    if (deleteButtonIsClickable) {
                    	
                    	/*
                    	
                    	* the only time when this boolean is false, is when the listView is reinitialised whilst
                    	* containing a TextArea item to enter a new effect into, ensuring that whilst the user
                    	* is entering a new effect, they cannot delete existing causes. Disabling this functionality
                    	* is the best balance between UX and the programming codes not becoming too big and complex
                    	
                    	*/
                    	
                    	deleteButton.setCursor(Cursor.HAND);
                        
                        deleteButton.setOnMouseClicked(e -> {
                                                        
                            int id_identifier = SelectableStatesUtil.getKeyByValueForCauses(new StringBuilder(item), listOfEffectsStringBuilder);
                                         
                                try {
                                    
                                gevolgVoorStaatModel.deleteEffectForState(id_identifier, new StringBuilder(item));
                                                            	
                            	setCenter(null);
                                                            	
                            	initialiseListView(true, false);
                            	                    	                    	                    	                    	                    	
                                /*
                        	 * Remove the item from the effectsList whose delete button the user just pressed,
                        	 * and reinitialise the listView with set updated effectsList
                    	
                        	*/
                        	                    	          
                                } 
                                
                                catch (SQLException se) {
                                
                                    Alert alert = new Alert(Alert.AlertType.ERROR);
                                    alert.setTitle("Error");
                                    alert.setContentText("cannot fulfill action, because a SQL related error has occured: " + se.getMessage());
		        
                                    alert.showAndWait();                                
                                }
                                
                                catch (Exception ex) {
                                    
                                    Alert alert = new Alert(Alert.AlertType.ERROR);
                                    alert.setTitle("Error");
                                    alert.setContentText("cannot fulfill action, because an error has occured: " + ex.getMessage());
		        
                                    alert.showAndWait();
                                                                        
                                }
                                
                                // IDE won't compile code unless try/catch specified here      
                                
                        	});
                        
                    }
                  
                }
            }
        });
		
		BorderPane.setMargin(effectsListView, new Insets(30));
		
		setCenter(effectsListView);
                                
            } catch (SQLException se) {
                
             Alert alert = new Alert(Alert.AlertType.ERROR);
             alert.setTitle("Error");
             alert.setContentText("cannot fulfill action, because a SQL related error has occured: " + se.getMessage());
		        
             alert.showAndWait();
                        
            } 
            
            catch (Exception e) {
                
             Alert alert = new Alert(Alert.AlertType.ERROR);
             alert.setTitle("Error");
             alert.setContentText("cannot fulfill action, because an error has occured: " + e.getMessage());
		        
             alert.showAndWait();
            
            }
		
	}

    /**
     * initialises the functionality of the floatingActionButton (in the form of a {@code Rectangle}) for this screen.
     */
    @Override
	protected void initialiseButtonFunctionality() {

        floatingActionButton.setOnMouseClicked(e -> addNewCause());
            
	}
        
        private void addNewCause() {
            				                
                System.out.println(effectsList);
		
		initialiseListView(false, true);
		
		// adds a new TextArea as a ListView item to the listView. TextArea is located directly underneath the last effect
		
		floatingActionButton.setOnMouseClicked(e -> submitNewCause());
		
		/*
		
		* set floatingAtionButton to now have the functionality where when clicked,
		* it adds the text currently contained in the TextArea as a new effect
		* (change is visible in both the UI and database)
		
		*/
						
	}
        
        private void submitNewCause() {
		
		if (newEffectTextArea != null && !newEffectTextArea.getText().isBlank()) {
                    
                    try {
                        
                        gevolgVoorStaatModel.addEffectForState(new StringBuilder(newEffectTextArea.getText()));
                        
                        floatingActionButton.setOnMouseClicked(e -> addNewCause());
		
                        // set floatingActionButton click action back to it's initial functionality
                        
                    } catch (SQLException se) {
                    
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error");
                        alert.setContentText("cannot fulfill action, because a SQL related error has occured: " + se.getMessage());
		        
                        alert.showAndWait();
                    
                    }
                    
                    
                    catch (Exception ex) {
                        
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error");
                        alert.setContentText("cannot fulfill action, because an error has occured: " + ex.getMessage());
		        
                        alert.showAndWait();
                        
                    }
			
			effectsList.remove(enterNewEffectCode);
			
			initialiseListView(true, false);
			
			/*
			
			* if newEffectTextArea is not null, is not empty, and does not exclusively contain whitespaces,
			* remove the item from the effectsList that is connected to the TextArea item in the ListView,
			* and afterwards, reinitialise the listView
			
			*/
			
		} else {
			
		Alert alert = new Alert(Alert.AlertType.ERROR);
	        alert.setTitle("Error");
	        alert.setContentText("program cannot find layout element, or the effect attempting to be submitted does not contain any text");
	        
	        alert.showAndWait();
	        
	        // if not the case, inform user by showing an error dialog
			
		}
				
	}

    /**
     * Initialises the functionality for when the user changes the selected country for this screen.
     */
    @Override
	protected void initialiseOnStateChangedFunctionality() {

            countriesComboBox.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {

                
            gevolgVoorStaatModel = new GevolgVoorStaat(
            		SelectableStatesUtil.getKeyByValueForCountriesHashMap(newValue));
            		            		
            initialiseListView(true, false);    

            // reinitialise the listview when the user chooses a different country
		
            	                
            }
        });
            
	}

}
