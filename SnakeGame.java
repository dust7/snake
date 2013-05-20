import java.util.Scanner;
import java.awt.Color;
import java.awt.Point;

class SnakeGame {
    private static Snake snake; //statics used by WindowListener methods in SnakeFrame
    private static Food food;
    private static Grid grid;
    private static int time=400; //time between moves

    public static Snake getSnake() {
        return new Snake(snake);
    }    
    
    public static Food getFood() {
        return new Food(food);
    }
    
    public static Grid getGrid() {
        return new Grid(grid);
    }
    
    private static void adjustTime() {
        if(time>300) {
            time=time-20;
        } else if(time<=300 && time>200) {
            time=time-3;
        } else if(time<=200 && time>150) {
            time=time-2;
        } else if(time<=150 && time>20) {
            time=time-1;
        }    
    }
    
    //methods for debugging (format grid locations, print info)
    private static int normXout(int x) {        
        x=x-Grid.SIDE-Grid.SIDE/2-Grid.OFFSET_X;
        x=x/Grid.SIDE;
        return x;
    }
    
    private static int normYout(int y) {        
        y=y-Grid.SIDE-Grid.SIDE/2-Grid.OFFSET_Y;
        y=y/Grid.SIDE;
        return y;
    }
    
    private static String normLocOut(Point pt) {
        int x=(int)pt.getX();
        int y=(int)pt.getY();
        x=normXout(x);
        y=normYout(y);
        return "("+x+","+y+")";
    }
    
    public static void inform() {
        System.out.println("snake size: "+snake.getSize());
        System.out.println("time: "+time+" ms");
        System.out.println("head at: "+normLocOut(snake.getPartsElement(0)));    
        System.out.println("food at: "+normLocOut(food.getFoodPosition())+"\n");
    }
    
    public static void main(String[] args) {
        //ask user for playzone dimensions
        AskFrame askuser=new AskFrame();
        while(!askuser.isInitialized()) {
            try {
                Thread.sleep(100);                
            } catch(InterruptedException ex){}
        }
        askuser.setVisible(false);
        askuser.dispose();
        //init playzone
        SnakeFrame frame=new SnakeFrame(askuser.getUserX(), askuser.getUserY());            
        grid=new Grid(askuser.getUserX(), askuser.getUserY());        
        //init snake, food   
        snake=new Snake(grid);            
        food=new Food(grid);
        while(food.isOnSnake(snake)) {
            food.jump(grid);
        }
        //paint grid, head, food
        try { //give frame time to load, prevent painting bugs
            Thread.sleep(100);
        } catch(InterruptedException ex) {}
        ((SnakeFrame)frame).setBg(new Color(244, 242, 255)); //almost white
        SnakePainter.paintDeadzone(frame,grid);
        SnakePainter.paintPlayzone(frame,grid);
        SnakePainter.repaintSnake(frame,snake,food);    
        SnakePainter.repaintFood(frame,food);          
        //start game        
        inform();
        System.out.println("use arrow keys to move!");        
        while(!snake.isInDeadzone(grid) && !snake.bites()) { //gameover condition            
            String command=frame.getLastListened();
            String lastMoved=snake.getLastMoved();
            if(command.equals("up") && !lastMoved.equals("down")) {
                snake.moveUp();                            
            } else if(command.equals("down") && !lastMoved.equals("up")){
                snake.moveDown();                
            } else if(command.equals("left") && !lastMoved.equals("right")) {
                snake.moveLeft();                   
            } else if(command.equals("right") && !lastMoved.equals("left")) {
                snake.moveRight();                    
            } else if(lastMoved.equals("up")) {                               
                snake.moveUp();
            } else if(lastMoved.equals("down")) {
                snake.moveDown();
            } else if(lastMoved.equals("left")) {
                snake.moveLeft();
            } else if(lastMoved.equals("right")) {
                snake.moveRight();
            }                       
            if(snake.eats(food)) {
                adjustTime();
                snake.grow();
                SnakePainter.repaintSnake(frame,snake,food); //must be called before jump!    
                if(snake.getSize()<askuser.getUserX()*askuser.getUserY()) { //if !full
                    food.jump(grid);
                    while(food.isOnSnake(snake)) {
                        food.jump(grid);
                    }
                SnakePainter.repaintFood(frame,food);    
                inform();
                }    
            } else {
            SnakePainter.repaintSnake(frame,snake,food);
            }
            //limit gamespeed
            try {
                Thread.sleep(time);
            } catch (InterruptedException ex) {}           
        }
        //game over
        snake.setHeadColor(Color.red);
        SnakePainter.repaintSnake(frame,snake,food);
        System.out.println("GAME OVER");
        System.out.println("snake size: "+snake.getSize());        
    }
}