import java.util.ArrayList;
import java.util.Arrays;

public class State {

    private Character[][] gameBoard;
    private char player;

    public State(char color){
        this.gameBoard = new Character[8][8];
        this.gameBoard[3][3] = 'w';
        this.gameBoard[4][4] = 'w';
        this.gameBoard[3][4] = 'b';
        this.gameBoard[4][3] = 'b';
        this.player = color;
    }

    public void setPoint(int row, int column, char p){
        this.gameBoard[row][column] = p;
    }

    public char getPoint(int row, int column){
        return this.gameBoard[row][column];
    }

    public Character[][] getGameBoard() {
        return gameBoard;
    }

    public void print(){
        for(int i = 0; i < 8; i++){
            System.out.println(Arrays.toString(this.gameBoard[i]));
        }
    }

    public ArrayList<IntPair> getValidMoves(){
        ArrayList<IntPair> validMoves = new ArrayList<>();
        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++){
                if(gameBoard[i][j] == null){
                    boolean upleft = isValidMove(i, j, -1, -1);
                    boolean upmiddle = isValidMove(i, j, -1, 0);
                    boolean upright = isValidMove(i, j, -1, 1);
                    boolean left = isValidMove(i, j, 0, -1);
                    boolean right = isValidMove(i, j, 0, 1);
                    boolean downleft = isValidMove(i, j, 1, -1);
                    boolean downmiddle = isValidMove(i, j, 1, 1);
                    boolean downright = isValidMove(i, j, 1, 1);
                    if(upleft || upmiddle || upright || left || right || downleft || downmiddle || downright) validMoves.add(new IntPair(i, j));
                }
            }
        }
        return validMoves;
    }

    private boolean isValidMove(int row, int col, int rO, int cO){
        int i = 0;
        boolean same = true;
        while(true) {
            row += rO;
            col += cO;
            if(row < 0 || row > 7 || col < 0 || col > 7) return false;
            if(++i > 65) return false;
            if(gameBoard[row][col] == null) return false;
            if(player == 'w'){
                if(gameBoard[row][col]=='w'){
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
                if(gameBoard[row][col] == 'b'){
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
    }

    private void update(int row, int col, int rO, int cO){
        int i = 0;
        while(true){
            row += rO;
            col += cO;
            if(row < 0 || row > 7 || col < 0 || col > 7) return;
            if(++i > 65) return;
            if(player == 'w'){
                if(gameBoard[row][col] == 'b'){
                    gameBoard[row][col] = 'w';
                }
                else return;
            }
            else{
                if(gameBoard[row][col] == 'w'){
                    gameBoard[row][col] = 'b';
                }
                else return;
            }
        }
    }

    public int getMax(boolean getMaxFromMin){
        int max = 0;
        int min = 100000000;
        ArrayList<IntPair> moves = getValidMoves();
        for(int i = 0; i < moves.size(); i++){
            if(getMaxFromMin){

            }
        }
    }

}
