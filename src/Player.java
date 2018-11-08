import com.sun.org.apache.xpath.internal.SourceTree;

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
        Move res = getMax(new GameBoard(gameBoard), new Move(Integer.MIN_VALUE), new Move(Integer.MAX_VALUE), 0);
        return res;
    }

    public Move getMax(GameBoard board, Move alpha, Move beta, int depth){
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
                Move lastM = i.getLastMove();
                max = new Move(lastM.getRow(), lastM.getCol(), lastM.getValue());
                max.setScore(lastM.getScore());
            }
            if(move.getScore() >= beta.getScore()){
                return move;
            }
            alpha.setScore(Math.max(alpha.getScore(), move.getScore()));
        }
        return max;
    }

    public Move getMin(GameBoard board, Move alpha, Move beta, int depth) {
        if (board.isTerminal() || depth == this.depth) {
            Move last = new Move(board.getLastMove().getRow(), board.getLastMove().getCol(), board.getLastMove().getValue());
            last.setScore(board.evaluate());
            return last;
        }
        ArrayList<GameBoard> children = board.getChildren();
        Move min = new Move(Integer.MAX_VALUE);
        for (GameBoard i : children) {
            Move move = getMax(new GameBoard(i), alpha, beta, depth + 1);
            if (move.getScore() <= min.getScore()) {
                Move lastM = i.getLastMove();
                min = new Move(lastM.getRow(), lastM.getCol(), lastM.getValue());
                min.setScore(lastM.getScore());
            }
            if (move.getScore() <= alpha.getScore()) return move;
            beta.setScore(Math.min(beta.getScore(), move.getScore()));
        }
        return min;
    }

    @Override
    public boolean equals(Object obj) {
        if(this.getPawn() == ((Player)obj).getPawn()) return true;
        return false;
    }
}
