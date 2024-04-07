package com.mycompany.eurostat.Views;

import com.mycompany.eurostat.Colors;

import com.mycompany.eurostat.SelectableStatesUtil;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;

/**
 * The AbstractAppScreenPaneView class is the View class for the UI template for any screen that shows up to the right of the Navigation drawer menu.
 * The AbstractAppScreenPaneView class serves as a template, or in other words, a blueprint of sorts,
 * of all layout components and some of their functionality for a given 'View' class within this project
 * @author Vidal Spierings
 */

public abstract class AbstractAppScreenPaneView extends BorderPane {
		
    
    protected HBox bottomLayoutComponentsLayout = null;
	
    
    protected Rectangle floatingActionButton = null;
	
    
    protected ComboBox<String> countriesComboBox = null;
	
    protected Pane spacer = null;
	
	private String screenTitle;
	
    /**
     * initialises the functionality of the floatingActionButton (in the form of a {@code Rectangle}) for a screen.
     */
    protected abstract void initialiseButtonFunctionality();
	
    /**
     * Initialises the functionality for when the user changes the selected country for a screen.
     */
    protected abstract void initialiseOnStateChangedFunctionality();

		
	/*
	
	* explicitly require all delegations of this class to declare their own implementation
	* of the functionality of the floating action button within the template
	
	*/

    /**
     *
     * @param screenTitle
     */


	public AbstractAppScreenPaneView(String screenTitle) {
		
		this.screenTitle = screenTitle;
				
		initialiseScreenIndicatorLabel();
		
		intialiseCountriesComboBox();
		
		initialiseSpacer();
		
		initialiseFloatingActionButton();
		
		initialiseCountrySelectorAndButtonLayout();
		
		initialiseOnStateChangedFunctionality();
		
		initialiseButtonFunctionality();
		
		HBox.setHgrow(this, javafx.scene.layout.Priority.ALWAYS);
		
		// ensures screen scales property as window size changes
		
		/*
		
		 *  creates the template layout. The code in this constructor is not called when this abstract class is instantiated,
		 *  because this class is abstract and cannot be instantiated, but,
		 *  it is run when a super() method is called in the constructor of a non-abstract delegation class of this class
		
		*/
		
	}
	
    /** 
     * Initialises the layout that contains both the country selection {@code ComboBox} as well as the floatingActionbutton (in the form of a {@code Rectangle}).
     */
    protected void initialiseCountrySelectorAndButtonLayout() {
		
		bottomLayoutComponentsLayout = new HBox();
		
		BorderPane.setMargin(bottomLayoutComponentsLayout, new Insets(10));
				
		bottomLayoutComponentsLayout.getChildren().addAll(countriesComboBox, spacer, floatingActionButton);
		
		setBottom(bottomLayoutComponentsLayout);	
		
		/*
		
		* initialises the HBox layout, on the bottom of the screen,
		* that contains the country selector combobox and floating action button
		
		*/
		
	}
	
	private void initialiseScreenIndicatorLabel() {
		
		Label screenName = new Label(screenTitle);
		
		screenName.setFont(new Font(20));
		
		BorderPane.setAlignment(screenName, Pos.CENTER);
		
		BorderPane.setMargin(screenName, new Insets(18, 0, 0, 0));
		
		setTop(screenName);
		
		/*
		
		* sets the label of the screen name at the top of the screen,
		* serving as an extra indication of what screen the user is currently viewing
		
		*/
		
	}
	
	private void initialiseSpacer() {
		
		spacer = new Pane();
		
		HBox.setHgrow(spacer, Priority.ALWAYS);
		
		/*
				
		 *  initialise a spacer / whitespace with 'always grow' priority.
		 *  This ensures that the country selection combobox is always at the left bottom corner of the screen,
		 *  and the floating action is always at the bottom right corner of the screen
		
		*/
		
	}
	
	private void intialiseCountriesComboBox() {
		
		countriesComboBox = new ComboBox<>();
		
		for (Object key: SelectableStatesUtil.countriesHashMap.keySet()) {
			
			countriesComboBox.getItems().add(SelectableStatesUtil.countriesHashMap.get(key));
			
			// intialise the countries ComboBox and add all known countries from the API to it
			
		}
		
		countriesComboBox.getSelectionModel().selectFirst();
                                
                // ensures a standard country is automatically selected for the user upon entering an AbstractAppScreenPaneView
                		
	}
	
	private void initialiseFloatingActionButton() {
		
		floatingActionButton = new Rectangle(25, 25, Color.web(Colors.primaryColor));
		
		floatingActionButton.setCursor(Cursor.HAND);
		
	}
		
	}
