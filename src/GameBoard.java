//The Reversi Game Board Class

import java.util.ArrayList;
import java.util.Arrays;

public class GameBoard {
    //Reversi Board States
    public static final Character B = 'B';
    public static final Character W = 'W';
    public static final Character E = null;

    //GRID SIZE
    public static final int GRID_SIZE = 8;

    //Number of combinations that we can traverse the game board
    private final int[][] combinations = new int[][] {{-1, -1}, {-1, 0}, {-1, 1}, {0, -1}, {0, 1}, {1, -1}, {1, 0}, {1, 1}};

    //The game board
    private Character[][] board;

    //The players
    private Player playsNow, playsNext;

    //The last move played
    private Move lastMove;

    //Number of squares populated
    private int populated, changed = 0;

    //Constructors
    public GameBoard(Player playsNow, Player playsNext){
        this.lastMove = new Move();
        this.playsNow = playsNow;
        this.playsNext = playsNext;
        this.board = new Character[GRID_SIZE][GRID_SIZE];
        board[3][3] = W;
        board[3][4] = B;
        board[4][4] = W;
        board[4][3] = B;
        populated = 4;
        count();
    }

    public GameBoard(GameBoard board){
        this.lastMove = new Move(board.getLastMove());
        this.playsNow = board.playsNow;
        this.playsNext = board.playsNext;
        this.board = new Character[GRID_SIZE][GRID_SIZE];
        for(int i = 0; i < GRID_SIZE; i++){
            for(int j = 0; j < GRID_SIZE; j++){
                this.board[i][j] = board.board[i][j];
            }
        }
        this.populated = board.populated;
        count();
    }

    //Method that gets all the available moves that a player can make
    public ArrayList<Move> getValidMoves(){
        ArrayList<Move> validMoves = new ArrayList<>();
        for(int i = 0; i < GRID_SIZE; i++){
            for(int j = 0; j < GRID_SIZE; j++){
                if(board[i][j]==null){
                    for(int k = 0; k < GRID_SIZE; k++){
                        //For every block of the board and every direction check if a square is a valid move for the current player
                        boolean res = isValid(i, j, combinations[k][0], combinations[k][1]);
                        if(res){
                            //If so, add it to the valid moves list
                            validMoves.add(new Move(i, j, playsNow.getPawn()));
                            break;
                        }
                    }
                }
            }
        }
        return validMoves;
    }

    //Checks if a square is valid for a move
    public boolean isValid(int row, int col, int x, int y){
        boolean same = true;
        while(true){
            row += x;
            col += y;
            if(row >= 0 && col >= 0 && row < GRID_SIZE && col < GRID_SIZE){
                if(board[row][col] == null) return false;
                if(playsNow.getPawn() == W){
                    if(board[row][col] == B){
                        if(same) same = false;
                    }
                    else if(same){
                        return false;
                    }
                    else{
                        return true;
                    }
                }
                else{
                    if(board[row][col] == W){
                        if(same) same = false;
                    }
                    else if(same){
                        return false;
                    }
                    else{
                        return true;
                    }
                }
            }
            else{
                return false;
            }
        }
    }

    //Update the board according to the new pawn that was placed on the board
    public void update(Move move){
        //Get the character of the move
        Character value = move.getValue();
        for(int k = 0; k < GRID_SIZE; k++){
            int row = move.getRow();
            int col = move.getCol();
            if(isValid(move.getRow(), move.getCol(), combinations[k][0], combinations[k][1])){
                //Update to all the directions that the move has access to
                while(true) {
                    row += combinations[k][0];
                    col += combinations[k][1];
                    if (board[row][col] == null) break;
                    if (row >= 0 && col >= 0 && row < GRID_SIZE && col < GRID_SIZE) {
                        if (playsNow.getPawn() == W) {
                            if (board[row][col] == B) {// if pawn is black
                                board[row][col] = value;
                                changed++;
                            } else { // its the end of move, so exit.
                                break;
                            }
                        } else {// black turn
                            if (board[row][col] == W) {// if pawn is black
                                board[row][col] = value;
                                changed++;
                            } else { // its the end of move, so exit.
                                break;
                            }
                        }
                    } else {
                        break;
                    }
                }
            }
        }
        count();
    }

    //Perform player move
    public void makeMove(Player player, Move move){
        //Add the move to the board
        board[move.getRow()][move.getCol()] = move.getValue();
        //Update the last move
        lastMove = new Move(move.getRow(), move.getCol(), move.getValue());
        //Increase the population
        populated++;
        //Get how many pawns changed
        changed = 1;
        //Update the rest of the board
        update(move);
    }

    //Count the pawns
    public void count(){
        int p1 = 0, p2 = 0;
        for(int i = 0; i < GRID_SIZE; i++){
            for(int j = 0; j < GRID_SIZE; j++){
                if(board[i][j] == B){
                    p1++;
                }
                else if(board[i][j] == W){
                    p2++;
                }
            }
        }
        if(playsNow.getPawn() == B){
            playsNow.setCount(p1);
            playsNext.setCount(p2);
        }
        else{
            playsNow.setCount(p2);
            playsNext.setCount(p1);
        }
    }

    //Helper method so as the bot is able to play
    public void botPlay(){
        //Get all the valid moves
        ArrayList<Move> validMoves = getValidMoves();
        //Get the best move according to MiniMax Algorithm
        Move best = playsNow.miniMaxAlphaBeta(this);
        //Make the move
        makeMove(playsNow, best);
        //Clear the list
        validMoves.clear();
    }

    //Heuristic evaluation for the moves played
    public double evaluate() {
        double score = 0;
        double mobility = getValidMoves().size(); // mobility represents every available move
        double pawns = getPawns(playsNow.getPawn()); // pawns represents number of pawns colored by current player
        double positions = 0; // positions represent a position-score-system
        double close = 0; // close represent how close to each other are current player pawns(a strategy tip is to play your pawns close to each other)

        // for each pawn that is colored by current player
        for(int i = 0; i < GRID_SIZE; i++){
            for(int j = 0; j < GRID_SIZE; j++){
                if(board[i][j] == playsNow.getPawn()){
                    // Captured corner +200 points
                    if((i == 0 && j == 0) || (i == 0 && j == 7) || (i == 7 && j == 0) || (i == 7 && j == 7)){
                        positions += 200;
                    }
                    // Captured squares next to corners worth -150 points if corner is empty, - 0 points if corner is captured by the enemy and +50 if the corner is captured by current player
                    else if((i == 0 && j == 1) || (i == 1 && j == 0) || (i == 1 && j == 1)){
                        if(board[0][0] == playsNow.getPawn()){
                            positions += 50;
                        }
                        else if(board[0][0] == playsNext.getPawn()){
                            positions -= 0;
                        }
                        else{
                            positions -= 150;
                        }
                    }
                    else if((i == 1 && j == 6) || (i == 1 && j == 7) || (i == 6 && j == 0)){
                        if(board[0][7] == playsNow.getPawn()){
                            positions += 50;
                        }
                        else if(board[0][7] == playsNext.getPawn()){
                            positions -= 0;
                        }
                        else{
                            positions -= 150;
                        }
                    }
                    else if((i == 6 && j == 0) || (i == 6 && j == 1) || (i == 7 && j == 1)){
                        if(board[7][0] == playsNow.getPawn()){
                            positions += 50;
                        }
                        else if(board[7][0] == playsNext.getPawn()){
                            positions -= 0;
                        }
                        else{
                            positions -= 150;
                        }
                    }
                    else if((i == 6 && j == 6) || (i == 6 && j == 7) || (i == 7 && j == 6)){
                        if(board[7][7] == playsNow.getPawn()){
                            positions += 50;
                        }
                        else if(board[7][7] == playsNext.getPawn()){
                            positions -= 0;
                        }
                        else{
                            positions -= 150;
                        }
                    }
                    // Captured squares at the the first inner square worth -5 points
                    else if((i == 1 && j == 2) || (i == 1 && j == 3) || (i == 1 && j == 4) || (i == 1 && j == 5) ||
                            (i == 2 && j == 1) || (i == 3 && j == 1) || (i == 4 && j == 1) || (i == 5 && j == 1) ||
                            (i == 6 && j == 2) || (i == 6 && j == 3) || (i == 6 && j == 4) || (i == 6 && j == 5) ||
                            (i == 2 && j == 6) || (i == 3 && j == 6) || (i == 4 && j == 6) || (i == 5 && j == 6)){
                        positions -= 5;
                    }
                    // Captured squares at the edges worth +20 points
                    else if((i == 0 && j == 2) || (i == 0 && j == 3) || (i == 0 && j == 4) || (i == 0 && j == 5) ||
                            (i == 2 && j == 0) || (i == 3 && j == 0) || (i == 4 && j == 0) || (i == 5 && j == 0) ||
                            (i == 2 && j == 7) || (i == 3 && j == 7) || (i == 4 && j == 7) || (i == 5 && j == 7) ||
                            (i == 7 && j == 2) || (i == 7 && j == 3) || (i == 7 && j == 4) || (i == 7 && j == 5)){
                        positions += 20;
                    }
                }
            }
        }
        //Play close to your pawns rule. Every pawn in the last move 5x5 is +1 point
        for(int k = lastMove.getRow()-2; k < lastMove.getRow()+2; k++) {
            for (int l = lastMove.getCol() - 2; l < lastMove.getCol() + 2; l++) {
                if (l < 0 || l >= GRID_SIZE || k < 0 || k >= GRID_SIZE) {
                    continue;
                }
                else {
                    if(board[k][l]==playsNow.getPawn()){
                        close+=5;
                    }
                }
            }
        }

        score = positions  + pawns*5 + mobility*3 + close*10; // we tried many combinations and this worked well.

        return score;
    }

    //Get the number of pawns based on the colour
    private int getPawns(Character who){
        int s = 0;
        for(int i = 0; i < GRID_SIZE; i++){
            for(int j = 0; j < GRID_SIZE; j++){
                if(board[i][j] == who){
                    s++;
                }
            }
        }
        return s;
    }

    //Simulate all the moves available as GameBoard
    public ArrayList<GameBoard> getChildren(){
        ArrayList<GameBoard> children = new ArrayList<>();
        ArrayList<Move> validMoves = getValidMoves();
        if(validMoves.size() > 0 ){
            for(int i = 0; i < validMoves.size(); i++){
                GameBoard child = new GameBoard(this);
                child.makeMove(child.getPlaysNow(), validMoves.get(i));
                child.setPlaysNow(child.playsNext);
                children.add(child);
            }
        }
        return children;
    }

    //Check if the game board is in a final condition
    public boolean isTerminal(){
        return (populated == 64 || getPawns(W) == 0 || getPawns(B) == 0);
    }

    //Setters
    public void setPlaysNow(Player playsNow) {
        this.playsNext = this.playsNow;
        this.playsNow = playsNow;
    }

    //Getters
    public Move getLastMove() {
        return lastMove;
    }

    public Player getPlaysNow() {
        return playsNow;
    }

    public Character[][] getBoard() {
        return board;
    }

    //Print
    public void print(){
        for(int i = 0; i < 8; i++){
            System.out.println(Arrays.toString(this.board[i]));
        }
        System.out.println("-----------------------------------------------------------");
    }
}
