public class Move{
    private int row;
    private int col;
    private Character value;
    private double score;
    private Move parent;

    public Move(){
        this.row = -1;
        this.col = -1;
        this.value = null;
        this.score = 0;
        this.parent = null;
    }

    public Move(int row, int col){
        this.row = row;
        this.col = col;
        this.value = null;
        this.score = 0;
        this.parent = null;
    }

    public Move(Character value){
        this.row = -1;
        this.col = -1;
        this.value = value;
        this.score = 0;
        this.parent = null;
    }

    public Move(int row, int col, Character value){
        this.row = row;
        this.col = col;
        this.value = value;
        this.score = 0;
        this.parent = null;
    }

    public Move(int row, int col, Character value, int score){
        this.row = row;
        this.col = col;
        this.value = value;
        this.score = score;
        this.parent = null;
    }

    public Move(int row, int col, Character value, int score, Move parent){
        this.row = row;
        this.col = col;
        this.value = value;
        this.score = score;
        this.parent = parent;
    }

    public Move(Move move){
        this.row = move.row;
        this.col = move.col;
        this.value = move.value;
        this.score = move.score;
        this.parent = move.parent;
    }

    public Move(int score){
        this.row = -1;
        this.col = -1;
        this.value = null;
        this.score = score;
        this.parent = null;
    }

    public int getRow(){
        return row;
    }

    public int getCol() {
        return col;
    }

    public Character getValue() {
        return value;
    }

    public double getScore() {
        return score;
    }

    public Move getParent() {
        return parent;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public void setValue(Character value) {
        this.value = value;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public void setParent(Move parent){
        this.parent = parent;
    }

    @Override
    public boolean equals(Object o) {
        if(o == null) return false;
        if(o == this) return true;
        if(!(o instanceof Move)) return false;
        Move other = (Move)o;
        if(this.row == other.row && this.col == other.col) return true;
        if(this.row == other.row && this.col == other.col && this.value == other.getValue()) return true;
        return false;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    public String toString(){
        return "Move at x: " + this.row + ", y: " + col;
    }
}
