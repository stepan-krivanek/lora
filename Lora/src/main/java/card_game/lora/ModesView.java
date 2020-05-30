/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package card_game.lora;

import card_game.lora.game_modes.GameModes;
import java.util.ArrayList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

/**
 *
 * @author stepa
 */
public class ModesView extends VBox{
    
    private final ArrayList<VBox> modeBoxes = new ArrayList<>();
    
    public ModesView(double height){
        final int numOfModes = GameModes.values().length;
        final int topModes = numOfModes / 2;

        final double windowHeight = height / 3;
        final double windowWidth = windowHeight * 2 / 3;
        final double vSpacing = windowHeight / 3;
        final double hSpacing = windowWidth / 2;
        
        HBox topBox = new HBox();
        topBox.setSpacing(hSpacing);
        topBox.setAlignment(Pos.CENTER);
        
        HBox botBox = new HBox();
        botBox.setSpacing(hSpacing);
        botBox.setAlignment(Pos.CENTER);
        
        BackgroundFill onFill = new BackgroundFill(
                    Design.YELLOW, new CornerRadii(15), Insets.EMPTY
        );
        BackgroundFill offFill = new BackgroundFill(
                Design.GREY, new CornerRadii(15), Insets.EMPTY
        );
        
        for (int i = 0; i < numOfModes; i++){
            GameModes gameMode = GameModes.values()[i];
            //-----------------Icon--------------------
            ImageView modeIcon = new ImageView(
                    GameUtils.getModeImage(gameMode)
            );
            modeIcon.setFitWidth(windowWidth * 2 / 3);
            modeIcon.setFitHeight(windowHeight * 2 / 3);
            modeIcon.setPreserveRatio(true);
            
            //-----------------Text---------------------
            Text modeText = new Text(gameMode.toString());
            modeText.setFont(Design.Font(windowHeight / 10));
            
            //----------------Mode box------------------
            VBox modeBox = new VBox(modeIcon, modeText);
            modeBox.setSpacing(windowHeight / 8);
            modeBox.setAlignment(Pos.CENTER);
            modeBox.setMinSize(windowWidth, windowHeight);
            modeBox.setMaxSize(windowWidth, windowHeight);
            
            modeBox.setBackground(new Background(offFill));
            modeBox.setOnMouseEntered(e -> {
                modeBox.setBackground(new Background(onFill));
            });
            modeBox.setOnMouseExited(e -> {
                modeBox.setBackground(new Background(offFill));
            });
            
            if (i < topModes){
                topBox.getChildren().add(modeBox);
            } else {
                botBox.getChildren().add(modeBox);
            }
            
            modeBoxes.add(i, modeBox);
        }
        
        this.getChildren().addAll(topBox, botBox);
        this.setSpacing(vSpacing);
        this.setAlignment(Pos.CENTER);
    }
    
    public VBox getModeBox(GameModes mode){
        return modeBoxes.get(mode.ordinal());
    }
}
