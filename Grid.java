import java.awt.Point;
import java.util.ArrayList;

class Grid {
    public static final int SIDE=10; //do not set odd values!    
    public static final int OFFSET_Y=25; //because JFrame is a bitch
    public static final int OFFSET_X=5;
    
    private ArrayList<Point> playzone;
    private ArrayList<Point> deadzone;    
    private int squaresx;
    private int squaresy;
    
    public Grid(int userx, int usery) {
        squaresx=userx+2; //extra space for deadzone
        squaresy=usery+2;
        playzone=new ArrayList<Point>(squaresx*squaresy);
        deadzone=new ArrayList<Point>(2*squaresx+2*squaresy-4);
        for(int row=0; row<squaresy; row++) {
            for(int col=0; col<squaresx; col++) {
                Point nextcenter=new Point(SIDE/2+col*SIDE+OFFSET_X,SIDE/2+row*SIDE+OFFSET_Y);                              
                if(row==0 || col==0 || row==squaresy-1 || col==squaresx-1) {
                    deadzone.add(nextcenter);
                } else {
                    playzone.add(nextcenter);
                }                
            }
        }
    }
    
    public Grid(Grid cloneme) {
        this.playzone=new ArrayList<Point>(cloneme.playzone);
        this.deadzone=new ArrayList<Point>(cloneme.deadzone);
        this.squaresx=cloneme.squaresx;
        this.squaresy=cloneme.squaresy;
    }
    
    public int getDeadzoneSize() {
        return deadzone.size();
    }
    
    public Point getDeadzoneElement(int index) {
        return new Point(deadzone.get(index));
    }

    public int getPlayzoneSize() {
        return playzone.size();
    }

    public Point getPlayzoneElement(int index) {
        return new Point(playzone.get(index));
    }    
    
    public int getSQX() {
        return squaresx;
    }
    
    public int getSQY() {
        return squaresy;
    }   
}