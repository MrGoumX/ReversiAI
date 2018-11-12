import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static java.lang.StrictMath.scalb;
import static java.lang.StrictMath.toIntExact;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        //Display Options to Start the Game
        final String[] data = {"Human", "AI Bot"};
        List<String> dialogData = Arrays.asList(data);
        ChoiceDialog first = new ChoiceDialog(dialogData.get(0), dialogData);
        first.setTitle("Who plays first?");
        first.setHeaderText("Select a choice:");

        Optional<String> res = first.showAndWait();
        String selected = "Cancelled";
        if(res.isPresent()){
            selected = res.get();
        }

        Player Human, Bot;

        if(selected.equals("Cancelled")){
            return;
        }
        else if(selected.equals("Human")){
            Human = new Player(GameBoard.B);
            Bot = new Player(GameBoard.W);
        }
        else{
            Human = new Player(GameBoard.W);
            Bot = new Player(GameBoard.B);
        }

        TextInputDialog inputDepth = new TextInputDialog();
        inputDepth.setTitle("Set the depth of MiniMax");
        inputDepth.setHeaderText("Please enter a integer value");
        Optional<String> d = inputDepth.showAndWait();
        String entered = "";
        if(d.isPresent()){
            entered = d.get();
        }
        if(entered.equals("")|| !entered.matches("\\d+")){
            return;
        }
        else{
            Bot.setDepth(Integer.parseInt(entered));
            Human.setDepth(Integer.parseInt(entered));
        }
        GameBoard game;
        String black, white;

        //Set the text on the bottom bar
        if(Human.getPawn() == GameBoard.B){
            game = new GameBoard(Human, Bot);
            black = "You";
            white = "Bot";
        }
        else{
            game = new GameBoard(Bot, Human);
            black = "Bot";
            white = "You";
        }

        //Initiate GUI
        VBox blackBox = new VBox();
        blackBox.setSpacing(5);
        blackBox.setAlignment(Pos.CENTER);
        TextField blackPawnField = new TextField();
        blackPawnField.setMaxWidth(45);
        blackPawnField.setAlignment(Pos.CENTER);
        blackPawnField.setEditable(false);
        StackPane temp = new StackPane();
        temp.getChildren().addAll(new Circle(0,0,20, Color.BLACK));
        blackBox.getChildren().addAll(new Text(black), temp, blackPawnField);
        HBox.setHgrow(blackBox, Priority.ALWAYS);


        VBox whiteBox = new VBox();
        whiteBox.setSpacing(5);
        whiteBox.setAlignment(Pos.CENTER);
        TextField whitePawnField = new TextField();
        whitePawnField.setMaxWidth(45);
        whitePawnField.setAlignment(Pos.CENTER);
        whitePawnField.setEditable(false);
        whiteBox.getChildren().addAll(new Text(white), new Circle(0,0,20, Color.WHITE), whitePawnField);
        HBox.setHgrow(whiteBox, Priority.ALWAYS);

        VBox middleBox = new VBox();
        middleBox.setSpacing(5);
        middleBox.setAlignment(Pos.CENTER);

        Button startGameButton = new Button("Start New Game");
        startGameButton.setOnMouseClicked((MouseEvent)->{// start new game button, restart the game(it open again window)
            try {
                start(primaryStage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        middleBox.getChildren().addAll(startGameButton);
        HBox.setHgrow(middleBox, Priority.ALWAYS);

        // HBox is the whole controlPane
        HBox controlPane = new HBox();
        controlPane.setSpacing(5);
        controlPane.getChildren().addAll(blackBox, middleBox, whiteBox);
        controlPane.setStyle("-fx-background-color: gray;");


        GridPane gamePane = new GridPane(); // gamePane contains the game board.
        gamePane.setPrefSize(600,600);
        gamePane.setHgap(5);
        gamePane.setVgap(5);

        for(int row = 0; row < GameBoard.GRID_SIZE; row++){
            for(int col = 0; col < GameBoard.GRID_SIZE; col++){
                Rectangle square = new Rectangle(row,col,75,75);
                square.setFill(Color.FORESTGREEN);
                gamePane.add(square, col, row);
            }
        }
        Character[][] startingBoard = game.getBoard();
        for(int i = 0; i < GameBoard.GRID_SIZE; i++){
            for(int j = 0; j < GameBoard.GRID_SIZE; j++){
                Character tempC = startingBoard[i][j];
                if(tempC != null){
                    gamePane.add(new Circle(37.5, 37.5, 35, (tempC == GameBoard.B)?Color.BLACK:Color.WHITE), i, j);
                }
            }
        }

        if(Human.getPawn() == GameBoard.B){
            blackPawnField.setText(Human.getCount()+"");
            whitePawnField.setText(Bot.getCount()+"");
        }
        else{
            blackPawnField.setText(Bot.getCount()+"");
            whitePawnField.setText(Human.getCount()+"");
        }

        Separator sep = new Separator();
        sep.setPrefHeight(10);

        // The whole Pane which contains playboard and controlPane.
        VBox mainPane = new VBox(); // mainPane is the whole GUI, which contains the other components.
        mainPane.setVgrow(controlPane, Priority.ALWAYS); // expand controlPane
        mainPane.getChildren().addAll(gamePane, sep, controlPane);

        primaryStage.setResizable(false);
        primaryStage.setScene(new Scene(mainPane, 635, 750));
        primaryStage.show();

        //Start the game
        runGame(gamePane, game, Human, Bot, blackPawnField, whitePawnField);

    }

    public static void main(String[] args) throws Exception{
        launch(args);
    }

    //The method that runs the game
    private void runGame(GridPane gamePane, GameBoard game, Player Human, Player Bot, TextField blackPawnField, TextField whitePawnField){
        //Check if the board is in terminal condition
        if(!game.isTerminal()){
            //If there are no moves available
            if(game.getValidMoves().size() == 0){
                //Of the human, then display message and set the bot to play
                if(game.getPlaysNow().getPawn() == Human.getPawn()){
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Reversi Message");
                    alert.setHeaderText(null);
                    alert.setContentText("You have no valid moves");
                    alert.showAndWait();
                    game.setPlaysNow(Bot);
                }
                //Of the bot, then display message and set the human to play
                else{
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Reversi Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Bot has no valid moves");
                    alert.showAndWait();
                    game.setPlaysNow(Human);
                }
                runGame(gamePane, game, Human, Bot, blackPawnField, whitePawnField);
                return;
            }
            else{
                //If Homo Sapiens is playing, then update the GUI and wait for the Homo Sapiens to play
                if(game.getPlaysNow().getPawn() == Human.getPawn()){
                    ArrayList<Move> validMoves = game.getValidMoves();
                    movesFX(gamePane, validMoves);
                    for(Move i : validMoves){
                        Rectangle sq = getRectangeByRowColumnIndex(i.getCol(), i.getRow(), gamePane);
                        sq.setOnMouseClicked((MouseEvent t) -> {
                            //Once the Homo Sapiens has made a move, update the GUI and set the bot to play
                            i.setValue(Human.getPawn());
                            game.makeMove(Human, i);
                            updateFX(gamePane, game.getBoard());
                            if (Human.getPawn() == GameBoard.B) {
                                blackPawnField.setText(Human.getCount() + "");
                                whitePawnField.setText(Bot.getCount() + "");
                            } else {
                                blackPawnField.setText(Bot.getCount() + "");
                                whitePawnField.setText(Human.getCount() + "");
                            }
                            game.setPlaysNow(Bot);
                            runGame(gamePane, game, Human, Bot, blackPawnField, whitePawnField);
                            return;
                        });
                    }
                }
                //If the bot is playing, then update the GUI and wait for the bot to play
                else{
                    game.botPlay();
                    updateFX(gamePane, game.getBoard());
                    if(Human.getPawn() == GameBoard.B){
                        blackPawnField.setText(Human.getCount()+"");
                        whitePawnField.setText(Bot.getCount()+"");
                    }
                    else{
                        blackPawnField.setText(Bot.getCount()+"");
                        whitePawnField.setText(Human.getCount()+"");
                    }
                    game.setPlaysNow(Human);
                    runGame(gamePane, game, Human, Bot, blackPawnField, whitePawnField);
                    return;
                }
            }
        }
        else{
            //Once the game has game has finished display the correct message
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Game Over");
            alert.setHeaderText(null);
            String result;
            if(Human.getCount() > Bot.getCount()){
                result = "Congratulations! You won this game :) ";
            }
            else if(Human.getCount() < Bot.getCount()){
                result = "Computer won this game.";
            }
            else{
                result = "Its a draw between you and computer.";
            }
            alert.setContentText(result);
            alert.showAndWait();
            return;
        }
    }

    //Update the GUI board according to the changes from the move played
    private void updateFX(GridPane gamePane, Character[][] gameBoard){
        for(int i = 0; i < GameBoard.GRID_SIZE; i++){
            for(int j = 0; j < GameBoard.GRID_SIZE; j++){
                Circle temp = getCircleByRowColumnIndex(j, i, gamePane);
                Character tempC = gameBoard[i][j];
                if(gameBoard[i][j] != null && temp == null){
                    gamePane.add(new Circle(37.5, 37.5, 35, (tempC == GameBoard.B)?Color.BLACK:Color.WHITE), j, i);
                }
                else if(gameBoard[i][j] != null && temp != null){
                    temp.setFill((tempC == GameBoard.B)?Color.BLACK:Color.WHITE);
                }
            }
        }
    }

    //Update the GUI to display the moves available
    private void movesFX(GridPane gamePane, ArrayList<Move> validMoves){
        for(int i = 0; i < GameBoard.GRID_SIZE; i++){
            for(int j = 0; j < GameBoard.GRID_SIZE; j++){
                Rectangle square = getRectangeByRowColumnIndex(j, i, gamePane);
                Move temp = new Move(i, j);
                square.setOnMouseEntered((MouseEvent t) -> {
                    if(validMoves.contains(temp)){
                         square.setFill(Color.YELLOWGREEN);
                    }
                    else{
                        square.setFill(Color.DARKGREEN);
                    }
                });
                square.setOnMouseExited((MouseEvent t) -> {
                    square.setFill(Color.FORESTGREEN);
                });
                square.setOnMouseClicked((MouseEvent t) ->{

                });
            }
        }
    }

    //Get the square in the Board by GUI
    private Rectangle getRectangeByRowColumnIndex (final int column, final int row, GridPane gridPane) {
        ObservableList<Node> children = gridPane.getChildren();
        for (int i=0; i< 64; i++) {
            if(gridPane.getRowIndex(children.get(i)) == row && gridPane.getColumnIndex(children.get(i)) == column) {
                if (children.get(i) instanceof Rectangle) return (Rectangle)children.get(i);
            }
        }
        return null;
    }

    //Get the circle in the Board by GUI
    private Circle getCircleByRowColumnIndex (final int column, final int row, GridPane gridPane) {
        ObservableList<Node> children = gridPane.getChildren();
        for (int i=64; i< children.size(); i++) {
            if(gridPane.getRowIndex(children.get(i)) == row && gridPane.getColumnIndex(children.get(i)) == column) {
                if (children.get(i) instanceof Circle) return (Circle)children.get(i);
            }
        }
        return null;
    }

}
