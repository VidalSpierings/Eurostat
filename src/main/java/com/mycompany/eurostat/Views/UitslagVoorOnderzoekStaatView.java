package com.mycompany.eurostat.Views;


import com.mycompany.eurostat.Models.UitslagVoorOnderzoekStaat;
import com.mycompany.eurostat.SelectableStatesUtil;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.sql.SQLException;
import java.util.HashMap;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Window;

/**
 * The View for the 'research results' screen.
 * @author Vidal Spierings
 */

public class UitslagVoorOnderzoekStaatView extends AbstractAppScreenPaneView {
	
	private BorderPane topLevelLayout = null;
	
	private ComboBox<String> firstCountryComboBox = null;
	
	private ComboBox<String> secondCountryComboBox = null;
	
	private HBox buttonsLayout = null;
	
	private Pane spacer = null;
	
	private Dialog dialog = null;
	
	private Button okButton = null;
	
	private Button cancelButton = null;
	
	private Window window = null;
	
	private XYChart.Series firstCountrySeries = null;
	
	private XYChart.Series secondCountrySeries = null;
	
	private BarChart<String, Number> researchResultsBarChart = null;
	
	private CategoryAxis xAxis = null;
	
	private NumberAxis yAxis = null;
        
        private UitslagVoorOnderzoekStaat uitslagVoorOnderzoekStaatModel = null;
        
        private HashMap<String, BigDecimal> researchResults = null;

    /**
     *
     * @param screenTitle
     */
    public UitslagVoorOnderzoekStaatView(String screenTitle) {
				
	super(screenTitle);
        
        String currentlySelectedState = countriesComboBox.getSelectionModel().getSelectedItem();
		
	String stateCountryCode = SelectableStatesUtil.getKeyByValueForCountriesHashMap(currentlySelectedState);
        
        uitslagVoorOnderzoekStaatModel = new UitslagVoorOnderzoekStaat(stateCountryCode);
        
        initialiseBarChart();
                        
        /*
        
        * initialise a single new series of data for the barchart and put the single series of data into the barchart,
        * resulting in only the data for one country being shown
        
        */
                						
	}
	
	private void intialiseStateComparisonGraph(
                String firstCountryName,
                String secondCountryName,
                HashMap<String, BigDecimal> firstCountryResearchResults,
                HashMap<String, BigDecimal> secondCountryResearchResults) {
            
            researchResultsBarChart.getData().clear();
            		
	// clear all currently active data in the barchart
                
        firstCountrySeries = new XYChart.Series();
        firstCountrySeries.setName(firstCountryName);

        secondCountrySeries = new XYChart.Series();
        secondCountrySeries.setName(secondCountryName);
                
        for (String key : firstCountryResearchResults.keySet()) {
                    
            firstCountrySeries.getData().add(new XYChart.Data(key, firstCountryResearchResults.get(key)));
        
        }
        
        for (String key : secondCountryResearchResults.keySet()) {
                    
            secondCountrySeries.getData().add(new XYChart.Data(key, secondCountryResearchResults.get(key)));
        
        }
        
        

        /*
            
            * Not done in one loop instead of two different ones,
            * because of the fact that loop order might differ (other than with, for example, an arraylist)
            
            */
        researchResultsBarChart.getData().addAll(firstCountrySeries, secondCountrySeries);
        
        /*
        
         * initialise two new series of data for the barchart and put the two series of data into the barchart,
         * resulting in the data for two countries being shown (two bars for each research question),
         * allowing the user to visually compare research results between the two selected countries
         
         */
		
	}

    /**
     * initialises the functionality of the floatingActionButton (in the form of a {@code Rectangle}) for this screen.
     */
    @Override
	protected void initialiseButtonFunctionality() {
		
		floatingActionButton.setOnMouseClicked(e -> showCountrySelectionDialog());
		
		/*
		
		* class-specific implementation of the functionality of the floating action button in this specific screen.
		* Pressing the floating action button on this screen prompts the country selection dialog to appear, allowing
		* the user to select two countries to visually compare all research results between
		
		*/
				
	}
	
	private void showCountrySelectionDialog() {
		
		topLevelLayout = new BorderPane();
		
		intialiseSpacer();
		
		initialiseOkButton();
		
		cancelButton = new Button("Annuleren");
		
		cancelButton.setOnAction(e -> window.hide());
		
		initialiseButtonsLayout();
		
		intialiseComboBoxes();
                		
		initialiseComboBoxesLayout();
		
		initialiseDialog();
		
		// intialise all layout and funtionality of the dialog
		
	}
        
        private void intialiseComboBoxes(){
        
            firstCountryComboBox = new ComboBox();
		
            secondCountryComboBox = new ComboBox();
                            
                for (String country : SelectableStatesUtil.countriesHashMap.keySet()) {
                
                    firstCountryComboBox.getItems().add(SelectableStatesUtil.countriesHashMap.get(country));
                
                    secondCountryComboBox.getItems().add(SelectableStatesUtil.countriesHashMap.get(country));
                    
                }
                
                // populate both ComboBoxes with all possible countries
        
        }
	
	private void initialiseComboBoxesLayout() {
		
		HBox comboBoxesLayoutHBox = new HBox();
		
		comboBoxesLayoutHBox.getChildren().addAll(firstCountryComboBox, secondCountryComboBox);
		
		comboBoxesLayoutHBox.setSpacing(10);
		
		topLevelLayout.setCenter(comboBoxesLayoutHBox);
		
		// intialise the layout containing the two country seletion comboboxes

		
	}
	
	private void initialiseButtonsLayout() {
		
		buttonsLayout = new HBox();
		
		buttonsLayout.setAlignment(Pos.CENTER_RIGHT);
		
		BorderPane.setMargin(buttonsLayout, new Insets(30, 0, 0, 0));
		
		buttonsLayout.setSpacing(7.5);
		
		buttonsLayout.getChildren().addAll(okButton, cancelButton);
		
		topLevelLayout.setBottom(buttonsLayout);
		
		// intialise the layout containg the 'Annuleren' and 'Ok' buttons
						
	}
	
	private void intialiseSpacer() {
		
		spacer = new Pane();
		
		spacer.setMinHeight(30);
		
		topLevelLayout.setTop(spacer);
		
		/*
		
		* initialise the spacer/whitespace, preventing the layout containing the country selection comboboxes
		* from always being aligned to the top border of the screen
		
		*/
		
	}
	
	private void initialiseDialog() {
		
		dialog = new Dialog();
		
		window = dialog.getDialogPane().getScene().getWindow();
		window.setOnCloseRequest(e -> window.hide());
		
		// enable the dialog to be closed when user presses the close button at the top right corner of the dialog window
		
		dialog.getDialogPane().setContent(topLevelLayout);
		
		dialog.showAndWait();

		// create and show the dialog
		
	}
	
	private void initialiseOkButton() {
		
		okButton = new Button("Ok");

		okButton.setOnAction(e -> {
                                            
                        if (firstCountryComboBox.getSelectionModel().getSelectedItem() != null
					&& secondCountryComboBox.getSelectionModel().getSelectedItem() != null){
                            
                            if (!firstCountryComboBox.getSelectionModel().getSelectedItem().equals(
                                secondCountryComboBox.getSelectionModel().getSelectedItem())) {
                                
                                // if not two identical countries selected
                                
                                try {
                                    
                                    String firstCountryStateCode = SelectableStatesUtil.getKeyByValueForCountriesHashMap(
                                            firstCountryComboBox.getSelectionModel().getSelectedItem());
                                    
                                    String secondCountryStateCode = SelectableStatesUtil.getKeyByValueForCountriesHashMap(
                                            secondCountryComboBox.getSelectionModel().getSelectedItem());
                                    
                                    uitslagVoorOnderzoekStaatModel = new UitslagVoorOnderzoekStaat(firstCountryStateCode);
                                    
                                    int firstCountryStateIndex = uitslagVoorOnderzoekStaatModel.getCurrentStateIndex();
                                    
                                    uitslagVoorOnderzoekStaatModel = new UitslagVoorOnderzoekStaat(secondCountryStateCode);
                                    
                                    int secondCountryStateIndex = uitslagVoorOnderzoekStaatModel.getCurrentStateIndex();
                                    
                                    /*
                                    
                                    * do converions in order to correctly retrieve indexes (as per saved from in API)
                                    * for selected countries
                                    
                                    */
                                    
                                    HashMap<String, BigDecimal> firstCountryResearchResult =
                                            uitslagVoorOnderzoekStaatModel.getUitslagenVoorStaat(
                                                    firstCountryStateIndex);
                                    
                                    HashMap<String, BigDecimal> secondCountryResearchResult =
                                            uitslagVoorOnderzoekStaatModel.getUitslagenVoorStaat(
                                                    secondCountryStateIndex);
                                    
                                    countriesComboBox.getItems().add(countriesComboBox.getSelectionModel().getSelectedItem());
                                    
                                    countriesComboBox.getSelectionModel().clearSelection(countriesComboBox.getSelectionModel().getSelectedIndex());
                                    
                                    intialiseStateComparisonGraph(
                                            firstCountryComboBox.getSelectionModel().getSelectedItem(),
                                            secondCountryComboBox.getSelectionModel().getSelectedItem(),
                                            firstCountryResearchResult,
                                            secondCountryResearchResult
                                    );
                                    
                                    // intialise new graph containing the research results of the two selected countries
                                    
                                    window.hide();
                                    
                                    /*
                                    
                                    * if a country is selected in both fields, hide the dialog and initialise a new bar chart
                                    * showing the different results of the same research questions per country
                                    
                                    */
                                }
                                
                                catch (SQLException se) {
                                
                                Alert alert = new Alert(AlertType.ERROR);
                                alert.setTitle("Error");
                                alert.setContentText("cannot fulfill action, because a SQL related error has occured: " + se.getMessage());
		        
                                alert.showAndWait();
                                
                                }
                                
                                catch (Exception ex) {
                                    
                            Alert alert = new Alert(AlertType.ERROR);
                            alert.setTitle("Error");
                            alert.setContentText("cannot fulfill action, because an error has occured: " + ex.getMessage());
		        
                            alert.showAndWait();
                                                                        
                                }
				
			}
                                                    
                        else {
                                				
			Alert alert = new Alert(AlertType.ERROR);
		        alert.setTitle("Error");
		        alert.setContentText("Please select two different countries");
		        
		        alert.showAndWait();
		        
		        // if either one or neither of the countries in the dialog are selected, inform the user by showing an error dialog
				
			}
                        
                        } else {
                        
                            	Alert alert = new Alert(AlertType.ERROR);
		        alert.setTitle("Error");
		        alert.setContentText("either or neither one of the fields are selected");
		        
		        alert.showAndWait();
		        
		        // if either one or neither of the countries in the dialog are selected, inform the user by showing an error dialog
                        
                        }
						
		});
		
	}

    /**
     * Initialises the functionality for when the user changes the selected country for this screen.
     */
    @Override
	protected void initialiseOnStateChangedFunctionality() {

            countriesComboBox.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
            	
                if (newValue != null) {
                
                String currentlySelectedState = newValue;
		
                String stateCountryCode = SelectableStatesUtil.getKeyByValueForCountriesHashMap(currentlySelectedState);
        
                uitslagVoorOnderzoekStaatModel = new UitslagVoorOnderzoekStaat(stateCountryCode);
                
                initialiseBarChart();
                
                // if user selects a different (singular) country, change UI accordingly
                
                }
            	                
            }
        });
            
	}
        
        private void initialiseBarChart(){
        
        researchResults = new HashMap<>();
        
        xAxis = new CategoryAxis();
        yAxis = new NumberAxis();
                        
            try {
                
                int currentStateIndex = uitslagVoorOnderzoekStaatModel.getCurrentStateIndex();
                
                researchResults = uitslagVoorOnderzoekStaatModel.getUitslagenVoorStaat(currentStateIndex);
                
                // retrieve research results from API
                                
            } catch (MalformedURLException me) {
            
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error");
                alert.setContentText("cannot fulfill action, because an error related to a malformed url has occurred: " + me.getMessage());
		        
                alert.showAndWait();
            
            }
            
            
            catch (Exception e) {
                
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error");
                alert.setContentText("cannot fulfill action, because an error has occurred: " + e.getMessage());
		        
                alert.showAndWait();
                
            }
		
        
        researchResultsBarChart = new BarChart<String,Number>(xAxis,yAxis);
        xAxis.setLabel("Indicator");       
        yAxis.setLabel("Percentage");
        
        yAxis.setUpperBound(100);
        yAxis.setAutoRanging(false);
        
        // initialise the frame of the barchart, intialise the type of the axes and their names
         
        firstCountrySeries = new XYChart.Series();
        firstCountrySeries.setName(countriesComboBox.getSelectionModel().getSelectedItem().toString());
        
        // set bar legend name equal to currently selected country
        
        for (String key : researchResults.keySet()) {
        
            firstCountrySeries.getData().add(new XYChart.Data(key, researchResults.get(key)));
        
        }
        
        // add all data to series
                
        researchResultsBarChart.getData().addAll(firstCountrySeries);
        
        /*
        
        * add the series in the barchart, resulting in the desired barchart
        * with a comprehensive view of all research results for a given country
        
        */
        
        researchResultsBarChart.setPrefHeight(1750);
        
        /*
        
        * set height very high, so user can always scroll to see the entire contents of the barcharts,
        * and not just the questions with collapsed, invisible bar indicators
        
        */
        
        VBox vbox = new VBox(researchResultsBarChart);
        
        ScrollPane scrollPane = new ScrollPane(vbox);
                        
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
			
	scrollPane.setFitToWidth(true);
		
        setCenter(scrollPane);
        
        }

}
