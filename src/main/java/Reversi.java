import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Reversi extends Application {

    @Override
    public void start(Stage primaryStage) {

        // Board Creation:
        GridPane gamePane = new GridPane(); // gamePane contains the game board.
        gamePane.setPrefSize(600,600);
        gamePane.setHgap(5);
        gamePane.setVgap(5);
        final int size = 8 ; // size represents number of rows and columns (8*8=64)
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col ++) {
                Rectangle square = new Rectangle(0,0,75,75);
                if ((row + col) % 2 == 0) { // even squares are white and odds squares are brown.
                    square.setFill(Color.DARKGREEN);
                } else {
                    square.setFill(Color.FORESTGREEN);
                }
                gamePane.add(square, col, row);
            }
        }
        gamePane.add(new Circle(37.5,37.5, 35, Color.BLACK), 3,3);
        gamePane.add(new Circle(37.5,37.5, 35, Color.BLACK), 4,4);
        gamePane.add(new Circle(37.5,37.5, 35, Color.WHITE), 3,4);
        gamePane.add(new Circle(37.5,37.5, 35, Color.WHITE), 4,3);

        // seperator to seperate controlPane from gamePane
        Separator sep = new Separator();
        sep.setPrefHeight(10);

        // controlPane
        VBox blackBox = new VBox();
        blackBox.setSpacing(5);
        blackBox.setAlignment(Pos.CENTER);
        TextField blackPawnField = new TextField("2");
        blackPawnField.setMaxWidth(30);
        blackPawnField.setAlignment(Pos.CENTER);
        blackPawnField.setEditable(false);
        blackBox.getChildren().addAll(new Text("You"), new Circle(0,0,20, Color.BLACK), blackPawnField);
        HBox.setHgrow(blackBox, Priority.ALWAYS);

        VBox whiteBox = new VBox();
        whiteBox.setSpacing(5);
        whiteBox.setAlignment(Pos.CENTER);
        TextField whitePawnField = new TextField("2");
        whitePawnField.setMaxWidth(30);
        whitePawnField.setAlignment(Pos.CENTER);
        whitePawnField.setEditable(false);
        whiteBox.getChildren().addAll(new Text("Computer"), new Circle(0,0,20, Color.WHITE), whitePawnField);
        HBox.setHgrow(whiteBox, Priority.ALWAYS);

        VBox middleBox = new VBox();
        middleBox.setSpacing(5);
        middleBox.setAlignment(Pos.CENTER);
        Button giveTurnButton = new Button("Give Your Turn");
        Button startGameButton = new Button("Start New Game");
        TextField turnText = new TextField();
        turnText.setEditable(false);
        middleBox.getChildren().addAll(turnText, giveTurnButton, startGameButton);
        HBox.setHgrow(middleBox, Priority.ALWAYS);

        HBox controlPane = new HBox(); // controlPane contains control button for game customization.
        controlPane.setSpacing(5);
        controlPane.getChildren().addAll(blackBox, middleBox, whiteBox);
        controlPane.setStyle("-fx-background-color: gray;");

        // The whole Pane
        VBox mainPane = new VBox(); // mainPane is the whole GUI, which contains the other components.
        mainPane.setVgrow(controlPane, Priority.ALWAYS); // expand controlPane
        mainPane.getChildren().addAll(gamePane, sep, controlPane);

        primaryStage.setResizable(false);
        primaryStage.setScene(new Scene(mainPane, 600, 750));
        primaryStage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }
}