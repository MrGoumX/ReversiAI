import javafx.scene.layout.GridPane;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.IntStream;

public class GameBoard {
    public static final Character B = 'B';
    public static final Character W = 'W';
    public static final Character E = null;

    public static final int GRID_SIZE = 8;

    private final int[][] combinations = new int[][] {{-1, -1}, {-1, 0}, {-1, 1}, {0, -1}, {0, 1}, {1, -1}, {1, 0}, {1, 1}};

    private Character[][] board;

    private Player playsNow, playsNext;

    private Move lastMove;

    private int populated;

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
        this.lastMove = board.lastMove;
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

    public ArrayList<Move> getValidMoves(){
        ArrayList<Move> validMoves = new ArrayList<>();
        for(int i = 0; i < GRID_SIZE; i++){
            for(int j = 0; j < GRID_SIZE; j++){
                if(board[i][j]==null){
                    for(int k = 0; k < GRID_SIZE; k++){
                        boolean res = isValid(i, j, combinations[k][0], combinations[k][1]);
                        if(res){
                            validMoves.add(new Move(i, j, playsNow.getPawn()));
                            break;
                        }
                    }
                }
            }
        }
        return validMoves;
    }

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

    public void update(Move move){
        Character value = move.getValue();
        for(int k = 0; k < GRID_SIZE; k++){
            int row = move.getRow();
            int col = move.getCol();
            if(isValid(move.getRow(), move.getCol(), combinations[k][0], combinations[k][1])){
                while(true) {
                    row += combinations[k][0];
                    col += combinations[k][1];
                    if (board[row][col] == null) break;
                    if (row >= 0 && col >= 0 && row < GRID_SIZE && col < GRID_SIZE) {
                        //Circle tempCircle = getCircleByRowColumnIndex(col, row, gamePane);
                        if (playsNow.getPawn() == W) {
                            if (board[row][col] == B) {// if pawn is black
                                board[row][col] = value;
                            } else { // its the end of move, so exit.
                                break;
                            }
                        } else {// black turn
                            if (board[row][col] == W) {// if pawn is black
                                board[row][col] = value;
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

    public void makeMove(Player player, Move move){
        board[move.getRow()][move.getCol()] = move.getValue();
        lastMove = new Move(move.getRow(), move.getCol(), move.getValue());
        populated++;
        update(move);
    }

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

    public boolean botPlay(){
        ArrayList<Move> validMoves = getValidMoves();
        if(validMoves.size() > 0){
            Move best = playsNow.miniMaxAlphaBeta(this);
            makeMove(playsNow, best);
            validMoves.clear();
            return true;
        }
        else return false;
    }

    public int evaluate(){
        //TODO OPTIMIZE HEURISTIC EVALUATION
        int score = 0;
        for(int i = 0; i < GRID_SIZE; i++){
            for(int j = 0; j < GRID_SIZE; j++){
                if(board[i][j] == playsNow.getPawn()){
                    score++;
                    if((i == 0 && j == 0) || (i == 0 && j == 7) || (i == 7 && j == 0) || (i == 7 && j == 7)){
                        score += 200;
                    }
                    else if((i == 2 && j == 2) || (i == 2 && j == 3 ) || (i == 2 && j == 4) || (i == 2 && j == 5) ||
                            (i == 3 && j == 2) || (i == 3 && j == 3 ) || (i == 3 && j == 4) || (i == 3 && j == 5) ||
                            (i == 4 && j == 2) || (i == 4 && j == 3 ) || (i == 4 && j == 4) || (i == 4 && j == 5) ||
                            (i == 5 && j == 2) || (i == 5 && j == 3 ) || (i == 5 && j == 4) || (i == 5 && j == 5)){
                        score += 10;
                    }
                    else if((i == 0 && j == 1) || (i == 1 && j == 0) || (i == 1 && j == 1) || (i == 0 && j == 6) ||
                            (i == 1 && j == 6) || (i == 1 && j == 7) || (i == 6 && j == 0) || (i == 6 && j == 1) ||
                            (i == 7 && j == 1) || (i == 6 && j == 6) || (i == 6 && j == 7) || (i == 7 && j == 6)){
                        score -= 10;
                    }
                    else if((i == 0 && j == 2) || (i == 0 && j == 3) || (i == 0 && j == 4) || (i == 0 && j == 5) ||
                            (i == 2 && j == 0) || (i == 3 && j == 0) || (i == 4 && j == 0) || (i == 5 && j == 0) ||
                            (i == 2 && j == 7) || (i == 3 && j == 7) || (i == 4 && j == 7) || (i == 5 && j == 7) ||
                            (i == 7 && j == 2) || (i == 7 && j == 3) || (i == 7 && j == 4) || (i == 7 && j == 5)){
                        score += 50;
                    }
                }
            }
            int black = getPawns(B);
            int white = getPawns(W);
            if(playsNow.getPawn() == B){
                if(black > white){
                    score += 5;
                }
            }
            else{
                if(white > black){
                    score += 5;
                }
            }
        }
        return score;
    }

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

    public ArrayList<GameBoard> getChildren(Player playsNow){
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

    public boolean isTerminal(){
        return (populated == 64 || playsNow.getCount() == 0) ? true : false;
    }

    public void setPlaysNow(Player playsNow) {
        this.playsNext = this.playsNow;
        this.playsNow = playsNow;
    }

    public Move getLastMove() {
        return lastMove;
    }

    public Player getPlaysNow() {
        return playsNow;
    }

    public Character[][] getBoard() {
        return board;
    }

    public void print(){
        for(int i = 0; i < 8; i++){
            System.out.println(Arrays.toString(this.board[i]));
        }
        System.out.println("-----------------------------------------------------------");
    }
}