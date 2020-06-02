/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package card_game.lora;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * Alert box for message confirmation.
 * 
 * @author Štěpán Křivánek
 */
public class ExitAlertBox {
    
    private static final int SPACING = 10;
    private static final int BOX_WIDTH = 300;
    private static final int BOX_HEIGHT = 120;
    private static boolean toBeClosed = true;
    
    private ExitAlertBox(){}
    
    /**
     * Triggers the allert box and displays a message.
     * Freezes a program until this alert box is finished.
     * 
     * @param program Program to be frozen
     * @param message Message to display
     * @return True on "yes" answer, false on "no" answer
     */
    public static boolean display(Main program, String message){
        Stage exitStage = new Stage();
        exitStage.setTitle("Exit");
        exitStage.initModality(Modality.APPLICATION_MODAL);
        exitStage.initStyle(StageStyle.UTILITY);
        exitStage.setWidth(BOX_WIDTH);
        exitStage.setHeight(BOX_HEIGHT);
        exitStage.setResizable(false);
        exitStage.setOnCloseRequest(e -> {
            toBeClosed = false;
            exitStage.close();
        });
        
        Label label = new Label();
        label.setText(message);
        
        //Save Button
        Button yes = new Button("Yes");
        yes.setOnAction(e -> {
            toBeClosed = true;
            exitStage.close();
        });
        
        //No Save Button
        Button no = new Button("No");
        no.setOnAction(e -> {
            toBeClosed = false;
            exitStage.close();
        });
        
        HBox hBox = new HBox();
        hBox.getChildren().addAll(yes, no);
        hBox.setSpacing(SPACING);
        hBox.setAlignment(Pos.CENTER);
        
        VBox vBox = new VBox();
        vBox.getChildren().addAll(label, hBox);
        vBox.setSpacing(SPACING);
        vBox.setAlignment(Pos.CENTER);
        
        Scene scene = new Scene(vBox);
        exitStage.setScene(scene);
        exitStage.showAndWait();
        
        return toBeClosed;
    }
    
}
