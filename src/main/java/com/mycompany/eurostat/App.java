package com.mycompany.eurostat;

import com.mycompany.eurostat.Views.AbstractAppScreenPaneView;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.sql.SQLException;

import com.mycompany.eurostat.Models.AbstractAppScreenPane;
import com.mycompany.eurostat.Views.GevolgVoorStaatView;
import com.mycompany.eurostat.Views.OorzaakVoorStaatView;
import com.mycompany.eurostat.Views.UitslagVoorOnderzoekStaatView;
import com.mycompany.eurostat.Views.VerbeteringVoorStaatView;
import java.util.ArrayList;
import javafx.scene.control.Alert;


/**
 * Eurostat App, entry point of this application.
 * 
 * This applications UI paradigms take inspiration from Android development.
 * Within Android, the most top level-view is called a 'Window' Which can contain
 * 'Activities' (these activities are somewhat similair to JavaFX's scenes). In order to achieve similar
 * functionality within this app, it is decided that there is one top-level view,
 * in the case of this application this is 'root', which is an HBox object that can contain
 * a specific 'Activity' in the form of a BorderPane object I.E. OorzakenVoorStaten, 
 * GevolgenVoorStaten, etc. As well as a 'Navigation Drawer' which is a menu with which 
 * the user can navigate between screens
 * @author Vidal Spierings
 */
public class App extends Application {
	
private HBox 
			root,
			onderzoeksResultatenHBox,
			oorzakenHBox,
			gevolgenHBox,
			verbeteringsnotieHBox;
	
private Label
			onderzoeksresultatenLabel,
			oorzakenLabel,
			gevolgenLabel,
			verbeteringsnotieLabel;

private String screenTitle;
		
    /**
     *
     * @param args
     */
    public static void main(String[] args) {
        launch();
    }

    /**
     *
     * @param stage
     */
    @Override
    public void start(Stage stage) {
    	
            try {
			
		new AbstractAppScreenPane().synchroniseAllCountriesWithImprovementNotionTable();
			
		} catch (MalformedURLException me) {
			
		Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setContentText("cannot fulfill action, because an error related to a malformed URL has occurred: " + me.getMessage());
			
		} catch (UnknownHostException uhe) {
			
		Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setContentText("cannot fulfill action, because an erro ccurreed related to an unknown host: " + uhe.getMessage());
			
		} catch (SQLException se) {
			
		Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setContentText("cannot fulfill action, because a SQL related error has occurred: " + se.getMessage());
		        
                alert.showAndWait(); 
			
		} catch (Exception e) {
			
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setContentText("cannot fulfill action, because an error has occurred: " + e.getMessage());
		        
                alert.showAndWait(); 
                    
		}
            
            /*
            
            * Add and delete countries from verbeteringsnotiesvoorstaten database according to latest info from the remote API,
            * ensuring proper basale veiligheid
            
            */
    	
        root = new HBox();
    	
    	VBox navigationDrawer = new VBox();
    	    	
    	onderzoeksResultatenHBox = new HBox();
    	oorzakenHBox = new HBox();
    	gevolgenHBox = new HBox();
    	verbeteringsnotieHBox = new HBox();
    	
    	onderzoeksResultatenHBox.setPrefHeight(35);
    	oorzakenHBox.setPrefHeight(35);
    	gevolgenHBox.setPrefHeight(35);
    	verbeteringsnotieHBox.setPrefHeight(35);
    	    	    	
    	onderzoeksresultatenLabel = new Label("Onderzoeksresultaten");
    	oorzakenLabel = new Label("Oorzaken");
    	gevolgenLabel = new Label("Gevolgen");
    	verbeteringsnotieLabel = new Label("Verbeteringsnotie");
    	
    	Insets navigationDrawerItemMargins = new Insets(10, 10, 10, 10);
    	
    	HBox.setMargin(onderzoeksresultatenLabel, navigationDrawerItemMargins);
    	HBox.setMargin(oorzakenLabel, navigationDrawerItemMargins);
    	HBox.setMargin(gevolgenLabel, navigationDrawerItemMargins);
    	HBox.setMargin(verbeteringsnotieLabel, navigationDrawerItemMargins);
    	
    	onderzoeksresultatenLabel.setFont(new Font("Arial", 15));
    	oorzakenLabel.setFont(new Font("Arial", 15));
    	gevolgenLabel.setFont(new Font("Arial", 15));
    	verbeteringsnotieLabel.setFont(new Font("Arial", 15));
    	
    	onderzoeksResultatenHBox.getChildren().add(onderzoeksresultatenLabel);
    	oorzakenHBox.getChildren().add(oorzakenLabel);
    	gevolgenHBox.getChildren().add(gevolgenLabel);
    	verbeteringsnotieHBox.getChildren().add(verbeteringsnotieLabel);
    	
    	onderzoeksresultatenLabel.setTextFill(Color.WHITE);
    	oorzakenLabel.setTextFill(Color.WHITE);
    	gevolgenLabel.setTextFill(Color.WHITE);
    	verbeteringsnotieLabel.setTextFill(Color.WHITE);
    	
    	onderzoeksResultatenHBox.setCursor(Cursor.HAND);
    	oorzakenHBox.setCursor(Cursor.HAND);
    	gevolgenHBox.setCursor(Cursor.HAND);
    	verbeteringsnotieHBox.setCursor(Cursor.HAND);
        
        // visually initialise the 'Navigation Drawer' menu items that the user can select
        
        UitslagVoorOnderzoekStaatView uitslagVoorOnderzoekStaatView = new UitslagVoorOnderzoekStaatView(onderzoeksresultatenLabel.getText());
        OorzaakVoorStaatView oorzaakVoorStaatView = new OorzaakVoorStaatView(oorzakenLabel.getText());
        GevolgVoorStaatView gevolgVoorStaatView = new GevolgVoorStaatView(gevolgenLabel.getText());
        VerbeteringVoorStaatView verbeteringVoorStaatView = new VerbeteringVoorStaatView(verbeteringsnotieLabel.getText());
    	
    	initiliaseOnClick(
    			onderzoeksResultatenHBox,
    			onderzoeksresultatenLabel,
    			uitslagVoorOnderzoekStaatView);
    	
    	initiliaseOnClick(oorzakenHBox, oorzakenLabel, oorzaakVoorStaatView);
    	
    	initiliaseOnClick(gevolgenHBox, gevolgenLabel, gevolgVoorStaatView);
    	
    	initiliaseOnClick(
    			verbeteringsnotieHBox,
    			verbeteringsnotieLabel,
    			verbeteringVoorStaatView);
        
        // initialse onClick functionality for all menu items within the 'Navigation Drawer'
        
        ArrayList<AbstractAppScreenPaneView> allScreenArrayList = new ArrayList();
        
        allScreenArrayList.add(uitslagVoorOnderzoekStaatView);
        allScreenArrayList.add(oorzaakVoorStaatView);
        allScreenArrayList.add(gevolgVoorStaatView);
        allScreenArrayList.add(verbeteringVoorStaatView);

        
        for (int i = 0; i < allScreenArrayList.size(); i++) {
        
            if (allScreenArrayList.get(i) instanceof UitslagVoorOnderzoekStaatView) {
            
                System.out.println("UitslagVoorOnderzoekStaatView is present in this application");
            
            }
            
            if (allScreenArrayList.get(i) instanceof OorzaakVoorStaatView) {
            
                System.out.println("OorzaakVoorStaatView is present in this application");
            
            }
            
            if (allScreenArrayList.get(i) instanceof GevolgVoorStaatView) {
            
                System.out.println("GevolgVoorStaatView is present in this application");
            
            }
            
            if (allScreenArrayList.get(i) instanceof OorzaakVoorStaatView) {
            
                System.out.println("VerbeteringVoorStaatView is present in this application");
            
            }
                        
            // inform programmer about all the Views that are present in the source code when first running app from IDE.
        
        }
    	
    	navigationDrawer.setPrefWidth(200);
    	
    	navigationDrawer.setMinWidth(200);
    	    	    	                
        root.setStyle("-fx-background-color: " + Colors.primaryColorLight + ";");
        
        navigationDrawer.setStyle("-fx-background-color: " + Colors.primaryColor + ";");
        
        navigationDrawer.getChildren().addAll(onderzoeksResultatenHBox, oorzakenHBox, gevolgenHBox, verbeteringsnotieHBox);
        
        // add all the menu items that were just initialised to the 'Navigation Drawer' menu
        
        onderzoeksResultatenHBox.setStyle("-fx-background-color: #FFFFFF;");
    	
        onderzoeksresultatenLabel.setTextFill(Color.web(Colors.tertiaryColor));
        
        //UitslagVoorOnderzoekStaatView uitslagVoorOnderzoekStaatView = new UitslagVoorOnderzoekStaatView(onderzoeksresultatenLabel.getText());
        
        root.getChildren().addAll(navigationDrawer, uitslagVoorOnderzoekStaatView);
    	        
        var scene = new Scene(root, 640, 480);
        stage.setScene(scene);
        stage.show();
        
    }
    
    private void initiliaseOnClick(HBox pressedItem, Label pressedItemAssociatedLabel, AbstractAppScreenPaneView pressedItemAssociatedView) {
    	
    	EventHandler<MouseEvent> buttonClickedEventHandler = new EventHandler<>() {
            @Override
            public void handle(MouseEvent event) {
            	
            	if (root.getChildren().size() != 1) {
            		
                	root.getChildren().remove(1);
            		
            	}
                
                // ensure correct screen being shown
            	                
            	onderzoeksResultatenHBox.setStyle("-fx-background-color: " + Colors.primaryColor + ";");
            	oorzakenHBox.setStyle("-fx-background-color: " + Colors.primaryColor + ";");
            	gevolgenHBox.setStyle("-fx-background-color: " + Colors.primaryColor + ";");
            	verbeteringsnotieHBox.setStyle("-fx-background-color: " + Colors.primaryColor + ";");
            	
            	onderzoeksresultatenLabel.setTextFill(Color.WHITE);
            	oorzakenLabel.setTextFill(Color.WHITE);
            	gevolgenLabel.setTextFill(Color.WHITE);
            	verbeteringsnotieLabel.setTextFill(Color.WHITE);
            	
            	pressedItem.setStyle("-fx-background-color: #FFFFFF;");
            	            	
            	pressedItemAssociatedLabel.setTextFill(Color.web(Colors.tertiaryColor));
                
                // indicate the item that was pressed by the user
            	
            	root.getChildren().add(pressedItemAssociatedView);
            	
            }
                        
        };
    	
        pressedItem.addEventHandler(MouseEvent.MOUSE_CLICKED, buttonClickedEventHandler);
    	
    }

}