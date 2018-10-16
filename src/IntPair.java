public class IntPair {
    private final int x;
    private final int y;

    IntPair(int x, int y){
        this.x = x;
        this.y = y;
    }

    public int getX(){
        return x;
    }

    public int getY() {
        return y;
    }

    public String toString(){
        return "x: " + this.x + " y: " + this.y;
    }
}
