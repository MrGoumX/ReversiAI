import java.util.ArrayList;
import java.util.Comparator;
import java.util.Optional;

public class Player {
    private int depth;
    private Character pawn;
    private int count;

    public Player(){
        this.depth = 3;
        this.pawn = GameBoard.B;
        this.count = 0;
    }

    public Player(int depth, Character pawn){
        this.depth = depth;
        this.pawn = pawn;
        this.count = 0;
    }

    public Player(Character pawn){
        this.pawn = pawn;
        this.depth = 0;
    }

    public void setCount(int count){
        this.count = count;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public int getCount(){
        return count;
    }

    public int getDepth(){
        return depth;
    }

    public Character getPawn(){
        return pawn;
    }

    public Move miniMaxAlphaBeta(GameBoard gameBoard){
        Move res = getMax(new GameBoard(gameBoard), Integer.MIN_VALUE, Integer.MAX_VALUE, 0);
        return res;
    }

    public Move getMax(GameBoard board, double alpha, double beta, int depth){
        if(board.isTerminal() || depth == this.depth){
            Move last = new Move(board.getLastMove().getRow(), board.getLastMove().getCol(), board.getLastMove().getValue());
            last.setScore(board.evaluate());
            return last;
        }
        ArrayList<GameBoard> children = board.getChildren();
        Move max = new Move(Integer.MIN_VALUE);
        for(GameBoard i : children){
            Move move = getMin(new GameBoard(i), alpha, beta, depth+1);
            if(move.getScore() >= max.getScore()){
                max.setRow(i.getLastMove().getRow());
                max.setCol(i.getLastMove().getCol());
                max.setValue(i.getLastMove().getValue());
                max.setScore(i.evaluate());
            }
            if(move.getScore() >= beta) return move;
            alpha = Math.max(alpha, move.getScore());
        }
        return max;
    }

    public Move getMin(GameBoard board, double alpha, double beta, int depth){
        if(board.isTerminal() || depth == this.depth){
            Move last = new Move(board.getLastMove().getRow(), board.getLastMove().getCol(), board.getLastMove().getValue());
            last.setScore(board.evaluate());
            return last;
        }
        ArrayList<GameBoard> children = board.getChildren();
        Move min = new Move(Integer.MAX_VALUE);
        for(GameBoard i : children){
            Move move = getMax(new GameBoard(i), alpha, beta, depth+1);
            if(move.getScore() <= min.getScore()){
                min.setRow(i.getLastMove().getRow());
                min.setCol(i.getLastMove().getCol());
                min.setValue(i.getLastMove().getValue());
                min.setScore(i.evaluate());
            }
            if(move.getScore() <= alpha) return move;
            beta = Math.min(beta, move.getScore());
        }
        return min;
    }



}
