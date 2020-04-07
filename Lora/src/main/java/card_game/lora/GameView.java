/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package card_game.lora;

import card_game.card.Card;
import card_game.card.Rank;
import card_game.card.Suit;
import java.util.List;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.effect.PerspectiveTransform;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 *
 * @author stepan
 */
public class GameView extends StackPane{
    
    private final double WIDTH = GameUtils.getScreenWidth();
    private final double HEIGHT = GameUtils.getScreenHeight();
    private final double CARD_WIDTH = WIDTH / 10;
    private final double CARD_HEIGHT = HEIGHT / 5;
    private final StackPane table = new StackPane();
    private HandView primaryHand;
    private Rectangle playZone;
    
    public GameView(Game game){
        //Background bcgr = GameUtils.loadBackground(path, WIDTH, HEIGHT);
        Background bcgr = new Background(new BackgroundFill(Color.LIGHTBLUE, CornerRadii.EMPTY, Insets.EMPTY));
        this.setBackground(bcgr);
        this.setHeight(HEIGHT);
        this.setWidth(WIDTH);

        Background tableBcgr = new Background(new BackgroundImage(
                new Image("/images/table.png", WIDTH / 5, 0, true, true),
                BackgroundRepeat.REPEAT,
                BackgroundRepeat.REPEAT,
                BackgroundPosition.CENTER,
                BackgroundSize.DEFAULT
        ));
        table.setBackground(tableBcgr);
        table.setPrefSize(WIDTH, HEIGHT);
        table.setEffect(perspection(WIDTH, HEIGHT));
        this.setAlignment(table, Pos.CENTER);
        this.getChildren().add(table);
    }
    
    public void showPassButton(Player player){
        Button passButton = new Button("PASS");
        
        this.setAlignment(passButton, Pos.BOTTOM_RIGHT);
        passButton.setOnAction(e -> {
            player.playCard(null);
        });
        
        this.getChildren().add(passButton);
    }
    
    public void showHand(Player player){
        this.getChildren().removeAll(primaryHand, playZone);
        primaryHand = player.getHandView();
        
        playZone = new Rectangle(WIDTH, HEIGHT * 2 / 3);
        this.setAlignment(playZone, Pos.TOP_CENTER);
        playZone.setOpacity(0);
        
        double handShift = CARD_HEIGHT / 2;
        primaryHand.setTranslateY(handShift);
        playZone.setOnMouseEntered(e -> {
            primaryHand.setTranslateY(handShift);
        });
        playZone.setOnMouseExited(e -> {
            primaryHand.setTranslateY(0);
        });
        
        this.getChildren().addAll(primaryHand, playZone);
    }
    
    public void cardDealing(){
        
    }
    
    public void showWinner(Player player){
        Text winner = new Text("The winner is player number " + player.getId());
        Font font = new Font(40);
        winner.setFont(font);
        this.setAlignment(winner, Pos.TOP_CENTER);
        
        Rectangle rect = new Rectangle(WIDTH, HEIGHT);
        rect.setOpacity(0);
        this.setAlignment(rect, Pos.CENTER);
        
        getChildren().addAll(rect, winner);
    }
    
    private PerspectiveTransform perspection(double width, double height){
        final double hSegment = width / 6;
        final double vSegment = height / 5;
        
        PerspectiveTransform perspection = new PerspectiveTransform(
                hSegment, vSegment,
                width - hSegment, vSegment,
                width + (width / 10), height,
                0 - (width / 10), height
        );
        
        return perspection;
    }
    
    public StackPane getTable(){
        return table;
    }
}