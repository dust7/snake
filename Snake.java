import java.awt.Point;
import java.util.ArrayList;
import java.awt.Color;

class Snake {
    private Point head; //must always be at parts(0)
    private Point prevTailPos; //saves tail position before last move
    private ArrayList<Point> parts;
    private String lastMoved="";
    private Color headColor=new Color(2,157,16); //default headcolor
    private Color bodyColor=Color.green; //default bodycolor
    
    public Snake(Grid grid) {
        //place head centrally
        int halfsqx=grid.getSQX()/2;
        int halfsqy=grid.getSQY()/2;
        int headx=halfsqx*Grid.SIDE+Grid.SIDE/2+Grid.OFFSET_X;
        int heady=halfsqy*Grid.SIDE+Grid.SIDE/2+Grid.OFFSET_Y;
        head=new Point(headx,heady);
        prevTailPos=new Point(head); //needs to be initialized to prevent nullpointer 
                                     //exception when first repaintSnake(...) is called
        parts=new ArrayList<Point>(grid.getPlayzoneSize());
        parts.add(head);
    }
    
    public Snake(Snake cloneme) {
        this.head=new Point(cloneme.head);
        this.prevTailPos=new Point(cloneme.prevTailPos);
        this.parts=new ArrayList<Point>(cloneme.parts);
        this.lastMoved=new String(cloneme.lastMoved);
        this.headColor=new Color(cloneme.headColor.getRed(),cloneme.headColor.getGreen(),
                                 cloneme.headColor.getBlue());
        this.bodyColor=new Color(cloneme.bodyColor.getRed(),cloneme.bodyColor.getGreen(),
                                 cloneme.bodyColor.getBlue());
    }
    
    public boolean isInDeadzone(Grid grid) {
        for(int i=0; i<grid.getDeadzoneSize(); i++) {
            if(head.equals(grid.getDeadzoneElement(i))) {
                return true;
            }
        }
        return false;
    }
    
    public void moveUp() {
        prevTailPos=new Point(parts.get(parts.size()-1));        
        parts.add(0,new Point((int)head.getX(),(int)head.getY()-Grid.SIDE));          
        head=parts.get(0);
        parts.remove(parts.size()-1);    
        lastMoved="up";
    }
    
    public void moveDown() {
        prevTailPos=new Point(parts.get(parts.size()-1));               
        parts.add(0,new Point((int)head.getX(),(int)head.getY()+Grid.SIDE));
        head=parts.get(0);
        parts.remove(parts.size()-1); 
        lastMoved="down";
    }    
    
    public void moveLeft() {
        prevTailPos=new Point(parts.get(parts.size()-1));               
        parts.add(0,new Point((int)head.getX()-Grid.SIDE,(int)head.getY()));
        head=parts.get(0);
        parts.remove(parts.size()-1);
        lastMoved="left";
    }
    
    public void moveRight() {
        prevTailPos=new Point(parts.get(parts.size()-1));                
        parts.add(0,new Point((int)head.getX()+Grid.SIDE,(int)head.getY()));
        head=parts.get(0);
        parts.remove(parts.size()-1);
        lastMoved="right";
    }
    
    public String getLastMoved() {
        return lastMoved;
    }
    
    public boolean bites() {
        for(int i=1; i<parts.size(); i++) {
            if(head.equals(parts.get(i))) {
                return true;
            }
        }
        return false;
    }
    
    public boolean eats(Food foo) {        
        return head.equals(foo.getFoodPosition());
    }  

    public void grow() {
        parts.add(new Point(prevTailPos));
    }
    
    public int getSize() {
        return parts.size();
    }
    
    public Point getPartsElement(int i) {
        return new Point(parts.get(i));
    }
    
    public Point getPrevTailPos() {
        return new Point(prevTailPos);       
    }
    
    public void setHeadColor(Color col) {
        headColor=col;
    }
    
    public void setBodyColor(Color col) {
        bodyColor=col;
    }
    
    public Color getHeadColor() {
        return new Color (headColor.getRed(), headColor.getGreen(), headColor.getBlue());
    }
    
    public Color getBodyColor() {
        return new Color (bodyColor.getRed(), bodyColor.getGreen(), bodyColor.getBlue());
    }
}