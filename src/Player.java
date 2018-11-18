//The Reversi Player Class

import java.util.ArrayList;

public class Player {
    private int depth;
    private Character pawn;
    private int count;

    //Constructors
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

    //Setters
    public void setCount(int count){
        this.count = count;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    //Getters
    public int getCount(){
        return count;
    }

    public int getDepth(){
        return depth;
    }

    public Character getPawn(){
        return pawn;
    }

    //MiniMax Algorithm with Alpha Beta Pruning.
    public Move miniMaxAlphaBeta(GameBoard gameBoard){
        //Initiate MiniMax with the current game board, alpha = -INF, beta = +INF & depth = 0.
        Move res = getMax(new GameBoard(gameBoard), Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, 0);
        //Return the best move
        return res;
    }

    //Max of MiniMax, that is our bot.
    public Move getMax(GameBoard board, double alpha, double beta, int depth){
        //Check if game is in terminal condition or if we reached max depth & return the best move.
        if(board.isTerminal() || depth == this.depth){
            Move last = new Move(board.getLastMove().getRow(), board.getLastMove().getCol(), board.getLastMove().getValue());
            last.setScore(board.evaluate());
            return last;
        }
        //Get the children of current board and simulate all the possible moves.
        ArrayList<GameBoard> children = board.getChildren();
        //If the human has no moves to play, return the last played move.
        if(children.size() == 0) return board.getLastMove();
        //Initiate max move with -INF score.
        Move max = new Move(Integer.MIN_VALUE);
        for(GameBoard i : children){
            //Get the min move (human) for every possible child board.
            Move move = getMin(new GameBoard(i), alpha, beta, depth+1);
            if(move.getScore() >= max.getScore()){
                //If the move is better than current max then make max the current move.
                Move lastM = i.getLastMove();
                max = new Move(lastM.getRow(), lastM.getCol(), lastM.getValue());
                max.setScore(move.getScore());
            }
            //Change the lower limit (alpha) according to current move score and the current alpha.
            alpha = Math.max(alpha, move.getScore());
            //If the score of our move is bigger than the upper limit (beta) then prune.
            if (alpha >= beta) break;
        }
        //Return max move.
        return max;
    }

    //Max of MiniMax, that is the Homo Sapiens.
    public Move getMin(GameBoard board, double alpha, double beta, int depth) {
        //Check if game is in terminal condition or if we reached max depth & return the worst move.
        if (board.isTerminal() || depth == this.depth) {
            Move last = new Move(board.getLastMove().getRow(), board.getLastMove().getCol(), board.getLastMove().getValue());
            last.setScore(board.evaluate());
            return last;
        }
        //Get the children of current board and simulate all the possible moves.
        ArrayList<GameBoard> children = board.getChildren();
        //If the bot has no moves to play, return the last played move.
        if(children.size() == 0) return board.getLastMove();
        //Initiate min move with +INF score.
        Move min = new Move(Integer.MAX_VALUE);
        for (GameBoard i : children) {
            //Get the max move (bot) for every possible child board.
            Move move = getMax(new GameBoard(i), alpha, beta, depth + 1);
            if (move.getScore() <= min.getScore()) {
                //If the move is worse than current min then make min the current move.
                Move lastM = i.getLastMove();
                min = new Move(lastM.getRow(), lastM.getCol(), lastM.getValue());
                min.setScore(move.getScore());
            }
            //If the score of our move is lower than the lower limit (alpha) then prune.
            //Change the upper limit (beta) according to current move score and the current beta.
            beta = Math.min(beta, move.getScore());

            if (alpha >= beta) break;
        }
        //Return min move.
        return min;
    }

    //Overridden equals for specific use
    @Override
    public boolean equals(Object obj) {
        if(this.getPawn() == ((Player)obj).getPawn()) return true;
        return false;
    }
}
