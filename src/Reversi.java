import javafx.application.Application;
import javafx.collections.ObservableList;
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

public class Reversi extends Application {

    private static String turn; // points out who player plays right now, black or white.

    @Override
    public void start(Stage primaryStage) {

        turn = "black"; // player with black paws begins first.

        // -------- ControlPane starts -------------
        // controlPane is the pane at the bottom of the window.

        // blackbox is the left part of controlPane for black pawn player
        VBox blackBox = new VBox();
        blackBox.setSpacing(5);
        blackBox.setAlignment(Pos.CENTER);
        TextField blackPawnField = new TextField("2");
        blackPawnField.setMaxWidth(30);
        blackPawnField.setAlignment(Pos.CENTER);
        blackPawnField.setEditable(false);
        StackPane temp = new StackPane();
        temp.getChildren().addAll(new Rectangle(60,60,Color.DARKGRAY),new Circle(0,0,20, Color.BLACK));
        blackBox.getChildren().addAll(new Text("You"), temp, blackPawnField);
        HBox.setHgrow(blackBox, Priority.ALWAYS);

        // white box is the right part of controlPane for white pawn player.
        VBox whiteBox = new VBox();
        whiteBox.setSpacing(5);
        whiteBox.setAlignment(Pos.CENTER);
        TextField whitePawnField = new TextField("2");
        whitePawnField.setMaxWidth(30);
        whitePawnField.setAlignment(Pos.CENTER);
        whitePawnField.setEditable(false);
        whiteBox.getChildren().addAll(new Text("Computer"), new Circle(0,0,20, Color.WHITE), whitePawnField);
        HBox.setHgrow(whiteBox, Priority.ALWAYS);

        // middlebox placed at the middle of controlPane and it contains "start new game" and "give your turn" buttons.
        VBox middleBox = new VBox();
        middleBox.setSpacing(5);
        middleBox.setAlignment(Pos.CENTER);

        Button giveTurnButton = new Button("Give Your Turn");
        giveTurnButton.setOnMouseClicked((MouseEvent)->{ // if player press this button, he give his turn to the other player.
            if (turn.equals("black")){
                turn = "white";
                StackPane tempWhitePane = new StackPane();
                tempWhitePane.getChildren().addAll(new Rectangle(60,60,Color.DARKGRAY),new Circle(0,0,20, Color.WHITE));
                whiteBox.getChildren().set(1, tempWhitePane);
                blackBox.getChildren().set(1,new Circle(0,0,20, Color.BLACK));
            }else{
                turn = "black";
                StackPane tempBlackPane = new StackPane();
                tempBlackPane.getChildren().addAll(new Rectangle(60,60,Color.DARKGRAY),new Circle(0,0,20, Color.BLACK));
                blackBox.getChildren().set(1, tempBlackPane);
                whiteBox.getChildren().set(1,new Circle(0,0,20, Color.WHITE));
            }
        });

        Button startGameButton = new Button("Start New Game");
        startGameButton.setOnMouseClicked((MouseEvent)->{// start new game button, restart the game(it open again window)
            start(primaryStage);
        });

        middleBox.getChildren().addAll(giveTurnButton, startGameButton);
        HBox.setHgrow(middleBox, Priority.ALWAYS);

        // HBox is the whole controlPane
        HBox controlPane = new HBox();
        controlPane.setSpacing(5);
        controlPane.getChildren().addAll(blackBox, middleBox, whiteBox);
        controlPane.setStyle("-fx-background-color: gray;");

        // -------- ControlPane finished -------------

        // -------- PlayBoard starts -------------
        // Board Creation:
        GridPane gamePane = new GridPane(); // gamePane contains the game board.
        gamePane.setPrefSize(600,600);
        gamePane.setHgap(5);
        gamePane.setVgap(5);
        final int size = 8 ; // size represents number of rows and columns (8*8=64)
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col ++) {
                Rectangle square = new Rectangle(0,0,75,75);
                square.setFill(Color.FORESTGREEN);
                gamePane.add(square, col, row);

                // functionality with listeners:

                // mouseclicked listener
                square.setOnMouseClicked((MouseEvent t) -> {

                    // take column and row of current square, which the player, try to add a pawn.
                    int colTemp = gamePane.getColumnIndex(square);
                    int rowTemp = gamePane.getRowIndex(square);

                    // if its not empty, it has already a pawn so player cant add new one.
                    if(isEmpty(gamePane, colTemp, rowTemp )){

                        // check all directions and if exists any valid move store true at relevant variable.
                        boolean upleft = isValidMove(gamePane, colTemp, rowTemp, -1, -1); // checks up-left
                        boolean upmiddle = isValidMove(gamePane, colTemp, rowTemp, 0, -1); // checks up-middle
                        boolean upright = isValidMove(gamePane, colTemp, rowTemp, 1, -1); // checks up-right
                        boolean left = isValidMove(gamePane, colTemp, rowTemp, -1, 0); // checks left
                        boolean right = isValidMove(gamePane, colTemp, rowTemp, 1, 0); // checks right
                        boolean downleft = isValidMove(gamePane, colTemp, rowTemp, -1, 1); // checks down-left
                        boolean downmiddle = isValidMove(gamePane, colTemp, rowTemp, 0, 1); // checks down-middle
                        boolean downright = isValidMove(gamePane, colTemp, rowTemp, 1, 1); // checks down-right

                        if(!(upleft || upmiddle || upright || left || right || downleft || downmiddle || downright)) return; // if there isn't any valid move, just ignore player command.

                        // if move is valid, update properly the play board.
                        if (upleft) updatePlayBoard(gamePane, colTemp, rowTemp, -1, -1); // checks up-left
                        if (upmiddle)updatePlayBoard(gamePane, colTemp, rowTemp, 0, -1); // checks up-middle
                        if (upright) updatePlayBoard(gamePane, colTemp, rowTemp, 1, -1); // checks up-right
                        if (left) updatePlayBoard(gamePane, colTemp, rowTemp, -1, 0); // checks left
                        if (right) updatePlayBoard(gamePane, colTemp, rowTemp, 1, 0); // checks right
                        if (downleft) updatePlayBoard(gamePane, colTemp, rowTemp, -1, 1); // checks down-left
                        if (downmiddle) updatePlayBoard(gamePane, colTemp, rowTemp, 0, 1); // checks down-middle
                        if (downright) updatePlayBoard(gamePane, colTemp, rowTemp, 1, 1); // checks down-right

                        gamePane.add(new Circle(37.5,37.5, 35, (turn.equals("black")?Color.BLACK:Color.WHITE)), colTemp, rowTemp);// create new pawn, as player mentioned.
                        giveTurnButton.setDisable(true); // disable giveTurnBurron, because it doesn't need after first move.


                        // change turn to the other player.
                        if (turn.equals("black")){
                            turn = "white";
                            StackPane tempWhitePane = new StackPane();
                            tempWhitePane.getChildren().addAll(new Rectangle(60,60,Color.DARKGRAY),new Circle(0,0,20, Color.WHITE));
                            whiteBox.getChildren().set(1, tempWhitePane);
                            blackBox.getChildren().set(1,new Circle(0,0,20, Color.BLACK));
                        }else{
                            turn = "black";
                            StackPane tempBlackPane = new StackPane();
                            tempBlackPane.getChildren().addAll(new Rectangle(60,60,Color.DARKGRAY),new Circle(0,0,20, Color.BLACK));
                            blackBox.getChildren().set(1, tempBlackPane);
                            whiteBox.getChildren().set(1,new Circle(0,0,20, Color.WHITE));
                        }


                        // update number of pawn on the play board for each player.
                        ObservableList<Node> children = gamePane.getChildren();
                        int blackCount = 0;
                        int whiteCount = 0;
                        for (int i=64; i<children.size(); i++) { // first 64 elements are rectangles.

                            if (((Circle)children.get(i)).getFill().toString().equals("0x000000ff")){
                                blackCount++;
                            }else{
                                whiteCount++;
                            }
                        }
                        blackPawnField.setText(blackCount+"");
                        whitePawnField.setText(whiteCount+"");


                        // check if pawn are 64, so the game is over and shows the winner.
                        if(children.size()==128){ // 128 = 64 squares and 64 pawns on gridpane(gamePane).
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("Game Over");
                            alert.setHeaderText(null);
                            String result;
                            if(blackCount>whiteCount){
                                result = "Congratulations! You won this game :) ";
                            }else if(whiteCount>blackCount){
                                result = "Computer won this game.";
                            }else {
                                result = "Its a draw between you and computer.";
                            }
                            alert.setContentText(result);
                            alert.showAndWait();
                            return;
                        }

                        if(existsValidMove(gamePane)) return;

                        // if method doesn't returns from above, then there isn't any possible move for current player, so he looses his turn.
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Game Over");
                        alert.setHeaderText(null);
                        alert.setContentText("There aren't any valid move, so you loose your turn");
                        alert.showAndWait();
                        if (turn.equals("black")){
                            turn = "white";
                            StackPane tempWhitePane = new StackPane();
                            tempWhitePane.getChildren().addAll(new Rectangle(60,60,Color.DARKGRAY),new Circle(0,0,20, Color.WHITE));
                            whiteBox.getChildren().set(1, tempWhitePane);
                            blackBox.getChildren().set(1,new Circle(0,0,20, Color.BLACK));
                        }else{
                            turn = "black";
                            StackPane tempBlackPane = new StackPane();
                            tempBlackPane.getChildren().addAll(new Rectangle(60,60,Color.DARKGRAY),new Circle(0,0,20, Color.BLACK));
                            blackBox.getChildren().set(1, tempBlackPane);
                            whiteBox.getChildren().set(1,new Circle(0,0,20, Color.WHITE));
                        }

                        if(!existsValidMove(gamePane)){
                            Alert gameoverAlert = new Alert(Alert.AlertType.INFORMATION);
                            gameoverAlert.setTitle("Game Over");
                            gameoverAlert.setHeaderText(null);
                            String result = "Neither of players has valid move.\nGame Over.\n";
                            if(blackCount>whiteCount){
                                result = "Congratulations! You won this game :) ";
                            }else if(whiteCount>blackCount){
                                result = "Computer won this game.";
                            }else {
                                result = "Its a draw between you and computer.";
                            }
                            gameoverAlert.setContentText(result);
                            gameoverAlert.showAndWait();
                            return;
                        }
                    }
                });

                // change color when hover to a valid move
                square.setOnMouseExited((MouseEvent t)->{ // when mouse exits from the square, it restore the normal color.
                    square.setFill(Color.FORESTGREEN);
                });
                square.setOnMouseEntered((MouseEvent t)->{ // when the mouse insert into a square, it changes color.

                    int colTemp = gamePane.getColumnIndex(square);
                    int rowTemp = gamePane.getRowIndex(square);
                    if(!isEmpty(gamePane, colTemp, rowTemp)){ // if square contains already pawn, it doesn't change color, but remain the same.
                        return;
                    }

                    // if square contains any valid possible move, then paint it GREENYELLOW, else paint it DARKGREEN
                    boolean upleft = isValidMove(gamePane, colTemp, rowTemp, -1, -1); // checks up-left
                    boolean upmiddle = isValidMove(gamePane, colTemp, rowTemp, 0, -1); // checks up-middle
                    boolean upright = isValidMove(gamePane, colTemp, rowTemp, 1, -1); // checks up-right
                    boolean left = isValidMove(gamePane, colTemp, rowTemp, -1, 0); // checks left
                    boolean right = isValidMove(gamePane, colTemp, rowTemp, 1, 0); // checks right
                    boolean downleft = isValidMove(gamePane, colTemp, rowTemp, -1, 1); // checks down-left
                    boolean downmiddle = isValidMove(gamePane, colTemp, rowTemp, 0, 1); // checks down-middle
                    boolean downright = isValidMove(gamePane, colTemp, rowTemp, 1, 1); // checks down-right
                    if(!(upleft || upmiddle || upright || left || right || downleft || downmiddle || downright)){
                        square.setFill(Color.DARKGREEN);
                    }else{
                        square.setFill(Color.GREENYELLOW);
                    }

                });
            }
        }

        // put at center of play board 4 initial pawns to begin the game.
        gamePane.add(new Circle(37.5,37.5, 35, Color.BLACK), 3,3);
        gamePane.add(new Circle(37.5,37.5, 35, Color.BLACK), 4,4);
        gamePane.add(new Circle(37.5,37.5, 35, Color.WHITE), 3,4);
        gamePane.add(new Circle(37.5,37.5, 35, Color.WHITE), 4,3);

        // -------- playBoard finished -------------

        // seperator to seperate controlPane from gamePane
        Separator sep = new Separator();
        sep.setPrefHeight(10);


        // The whole Pane which contains playboard and controlPane.
        VBox mainPane = new VBox(); // mainPane is the whole GUI, which contains the other components.
        mainPane.setVgrow(controlPane, Priority.ALWAYS); // expand controlPane
        mainPane.getChildren().addAll(gamePane, sep, controlPane);

        primaryStage.setResizable(false);
        primaryStage.setScene(new Scene(mainPane, 635, 750));
        primaryStage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }

    /*
        isValidMove() checks if exist any valid move from a square to a direction.
        gamePane = gridPane, which contains all squares and pawns.
        col, row is col and row of square, which search to make a valid move.
        colOper, rowOper, is a number to add at col and row, to find out the right direction. e.x. -1, -1 -> 1 row up, 1 column left -> up-left direction
     */
    private boolean isValidMove(GridPane gamePane, int col, int row, int colOper, int rowOper){

        int i = 0;
        boolean sameColor = true; // useful to decide if first pawn has same color as initial pawn, so move isn't valid.
        while(true){
            col += colOper; // set direction properly
            row += rowOper;
            if(++i > 65) return false; // just in case of unfinished loop. But normally this command doesn't need.
            Circle tempCircle = getCircleByRowColumnIndex(col, row, gamePane); // gives the next pawn from specific direction.
            if(tempCircle == null) return false; // null means empty square or out of playboard borders. So invalid move.
            if (turn.equals("white")){
                if(tempCircle.getFill().toString().equals("0x000000ff")){// if next pawn is black
                    if(sameColor) sameColor = false; // this means that next pawn could be white without any problem.
                }else if(sameColor){ // = white player turn and first pawn is white, then invalid move.
                    return false;
                }else{ // white player turn and white pawn but not first, then valid move.
                    return true;
                }
            } else{// black turn
                if(!tempCircle.getFill().toString().equals("0x000000ff")){// if pawn is white
                    if(sameColor) sameColor = false;
                }else if(sameColor){ //black turn and black first pawn
                    return false;
                }else{ // black turn and black pawn but not first
                    return true;
                }
            }
        }
    }

    /*
        updatePlayBoard() change color of all pawns of a specific direction, to the color of current turn.
        gamePane = gridPane, which contains all squares and pawns.
        col, row is col and row of square, which makes a valid move.
        colOper, rowOper, is a number to add at col and row, to find out the right direction. e.x. -1, -1 -> 1 row up, 1 column left -> up-left direction
     */
    private void updatePlayBoard(GridPane gamePane, int col, int row, int colOper, int rowOper){

        int i = 0;
        while(true){
            col += colOper;
            row += rowOper;
            if(++i > 65) return; // just in case of unfinished loop. Normally this command doesn't need.
            Circle tempCircle = getCircleByRowColumnIndex(col, row, gamePane);
            if (turn.equals("white")){
                if(tempCircle.getFill().toString().equals("0x000000ff")){// if pawn is black
                    tempCircle.setFill(Color.WHITE);
                }else{ // its the end of move, so exit.
                    return;
                }
            } else{// black turn
                if(!tempCircle.getFill().toString().equals("0x000000ff")){// if pawn is white
                    tempCircle.setFill(Color.BLACK);
                }else{
                    return;
                }
            }
        }
    }

    // isEmpty() method check if a cell is empty or if it contains already a pawn.
    private boolean isEmpty(GridPane gamePane, int column, int row){
        ObservableList<Node> children = gamePane.getChildren();
        for (int i=64; i<children.size(); i++) { // first 64 elements are rectangles.
            if((gamePane.getColumnIndex(children.get(i))==column)&&(gamePane.getRowIndex(children.get(i))==row)){
                return false;
            }
        }
        return true;
    }

    // getCircleByRowColumnIndex returns a Circle from column and row at gridPane.
    private Circle getCircleByRowColumnIndex (final int column, final int row, GridPane gridPane) {
        ObservableList<Node> children = gridPane.getChildren();
        for (int i=64; i<children.size(); i++) {
            if(gridPane.getRowIndex(children.get(i)) == row && gridPane.getColumnIndex(children.get(i)) == column) {
                if (children.get(i) instanceof Circle) return (Circle)children.get(i);
            }
        }
        return null;
    }

    private boolean existsValidMove(GridPane gamePane){
        // for each cell of play board, if it is empty, check if it has any valid move. If there isn't any cell then current player can't make a valid move, so he looses his turn.
        for(int i = 0; i<8; i++){
            for (int j = 0; j<8; j++){
                if (isEmpty(gamePane, j, i)){
                    if (isValidMove(gamePane, j, i, -1, -1)) return true; // checks up-left
                    if (isValidMove(gamePane, j, i, 0, -1)) return true; // checks up-middle
                    if(isValidMove(gamePane, j, i, 1, -1)) return true; // checks up-right
                    if (isValidMove(gamePane, j, i, -1, 0)) return true; // checks left
                    if (isValidMove(gamePane, j, i, 1, 0)) return true; // checks right
                    if (isValidMove(gamePane, j, i, -1, 1)) return true; // checks down-left
                    if (isValidMove(gamePane, j, i, 0, 1)) return true; // checks down-middle
                    if (isValidMove(gamePane, j, i, 1, 1)) return true; // checks down-right
                }
            }
        }
        return false;
    }
}