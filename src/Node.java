import java.util.ArrayList;

public class Node {

    private IntPair coordinates = new IntPair();
    private Node parent = null;
    private int maxScore = 0;
    private int minScore = 10000000;
    private int score;
    private ArrayList<IntPair> children = null;

    public Node() {

    }

    public void setParent(Node parent){
        this.parent = parent;
    }

    public void setMinScore(int minScore) {
        this.minScore = minScore;
    }

    public void setMaxScore(int maxScore) {
        this.maxScore = maxScore;
    }

    public void setScore(int score){
        this.score = score;
    }

    public void setCoordinates(int x, int y){
        coordinates.setX(x);
        coordinates.setY(y);
    }

    public int getMaxScore() {
        return maxScore;
    }

    public int getMinScore() {
        return minScore;
    }

    public ArrayList<IntPair> getChildren() {
        return children;
    }

    public Node getParent() {
        return parent;
    }

    public int getScore(){
        return score;
    }

    public IntPair getCoordinates(){
        return coordinates;
    }


}
