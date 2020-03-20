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
 *
 * @author stepa
 */
public class ExitAlertBox {
    
    private static boolean toBeClosed = true;
    
    private ExitAlertBox(){}
    
    public static boolean display(Main program, String message){
        Stage exitStage = new Stage();
        exitStage.setTitle("Exit");
        exitStage.initModality(Modality.APPLICATION_MODAL);
        exitStage.initStyle(StageStyle.UTILITY);
        exitStage.setMinWidth(300);
        exitStage.setMinHeight(120);
        exitStage.setOnCloseRequest(e -> {
            toBeClosed = false;
            exitStage.close();
        });
        
        Label label = new Label();
        label.setText(message);
        
        //Save Button
        Button save = new Button("Yes");
        save.setOnAction(e -> {
            program.setToBeSaved(true);
            toBeClosed = true;
            exitStage.close();
        });
        
        //No Save Button
        Button noSave = new Button("No");
        noSave.setOnAction(e -> {
            program.setToBeSaved(false);
            toBeClosed = true;
            exitStage.close();
        });
        
        //Cancel Button
        Button cancel = new Button("Cancel");
        cancel.setOnAction(e -> {
            toBeClosed = false;
            exitStage.close();
        });
        
        HBox hBox = new HBox();
        hBox.getChildren().addAll(save, noSave, cancel);
        hBox.setSpacing(10);
        hBox.setAlignment(Pos.CENTER);
        
        VBox vBox = new VBox();
        vBox.getChildren().addAll(label, hBox);
        vBox.setSpacing(10);
        vBox.setAlignment(Pos.CENTER);
        
        Scene scene = new Scene(vBox);
        exitStage.setScene(scene);
        exitStage.showAndWait();
        
        return toBeClosed;
    }
    
}
