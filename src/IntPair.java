public class IntPair {
    private int x;
    private int y;

    IntPair(){

    }

    IntPair(int x, int y){
        this.x = x;
        this.y = y;
    }

    public void setX(int x){
        this.x = x;
    }

    public void setY(int y){
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
