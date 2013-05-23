import java.util.Scanner;
import java.awt.Color;
import java.awt.Point;
import javax.swing.*;
import java.util.Random;

class SnakeGame {
    private static Snake snake; //snake, troll, food, grid used to refresh() frame
    private static Snake troll;
    private static Food food;
    private static Grid grid;
    private static int time=150; //time/ms between moves
    private static Random rand=new Random();
    private static long startTime=System.currentTimeMillis();
    private static Runtime rt = Runtime.getRuntime();

    public static Snake getSnake() {
        return new Snake(snake);
    }    
    
    public static Snake getTroll() {
        return new Snake(troll);
    }
    
    public static Food getFood() {
        return new Food(food);
    }
    
    public static Grid getGrid() {
        return new Grid(grid);
    }    
    
    private static void adjustTime() {    
        if(time>100) {
            time=time-2;
        } else if(time<=100 && time>60) {
            time=time-1;
        } else if(time<=60 && time>25 && rand.nextBoolean()) {
            time=time-1;
        }    
    }
    
    public static boolean prevTailOnTroll() { // only needed for last painted Snake (for
                                              // now: player snake), prevents painting bug                      
        Point prevTail=snake.getPrevTailPos();
        for(int i=0; i<troll.getSize(); i++) {
            if(prevTail.equals(troll.getPartsElement(i))) {
                return true;
            }
        }
        return false;
    }
    
    private static boolean gameOver() {
        boolean bitestroll=false;
        for(int i=0; i<troll.getSize(); i++) {
            if(snake.getPartsElement(0).equals(troll.getPartsElement(i))) {
                bitestroll=true;
                break;
            }
        }
        return bitestroll || snake.bites() || snake.isInDeadzone(grid);
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
        System.out.println("head at: "+normLocOut(snake.getPartsElement(0)));    
        System.out.println("food at: "+normLocOut(food.getFoodPosition()));  
        System.out.println("time: "+time+" ms");              
        System.out.println("runtime: "+(System.currentTimeMillis()-startTime)/1000+" s");        
        System.out.println("JVM total/free memory: "+rt.totalMemory()+" "+rt.freeMemory()+"\n");
    }
    
    public static void main(String[] args) {
        //ask user for playzone dimensions and game mode
        AskFrame askuser=new AskFrame();
        while(!askuser.isInitialized()) {
            try {
                Thread.sleep(100);                
            } catch(InterruptedException ex){}
        }            
        askuser.setVisible(false);
        askuser.dispose();        
        //init grid, snake, food, frame                           
        grid=new Grid(askuser.getUserX(), askuser.getUserY());        
        snake=new Snake(grid);        
        troll=new TrollSnake(grid);            
        food=new Food(grid);
        while(food.isOnSnake(snake) || food.isOnSnake(troll)) {
            food.jump(grid);
        }       
        SnakeFrame frame=new SnakeFrame(askuser.getUserX(), askuser.getUserY());               
        if(askuser.getTurbo()) {
            time=25;
            frame.setTitle(askuser.getUserX()+"x"+askuser.getUserX()+" T");    
        }
        //prevent painting bugs on startup, give frame time to load0
        try {
            Thread.sleep(100);
        } catch(InterruptedException ex) {}
        frame.refresh();    
        //start game        
        inform();                
        while(!gameOver()) {            
            int command=frame.getLastListened();
            int lastMoved=snake.getLastMoved();
            if(command==0 && lastMoved!=1) { //0: up, 1: down, 2:left, 3: right
                snake.move(0);                            
            } else if(command==1 && lastMoved!=0){
                snake.move(1);                
            } else if(command==2 && lastMoved!=3) {
                snake.move(2);                   
            } else if(command==3 && lastMoved!=2) {
                snake.move(3);                    
            } else if(lastMoved!=-1) {                               
                snake.move(lastMoved);
            } 
            if(lastMoved!=-1) {
                ((TrollSnake)troll).randMove(grid);
            }        
            if(snake.eats(food) || troll.eats(food)) {
                adjustTime();
                troll.grow();
                snake.grow();                
                SnakePainter.repaintWholeSnake(frame,troll);                                 
                SnakePainter.repaintSnake(frame,troll,food);                        
                SnakePainter.repaintSnake(frame,snake,food);                
                boolean win=(askuser.getUserX()*askuser.getUserY()-troll.getSize()
                             <=snake.getSize());    
                while(!win && (food.isOnSnake(snake) || food.isOnSnake(troll))) {
                    food.jump(grid);
                }
                SnakePainter.repaintFood(frame,food);    
                inform();
            } else {
                SnakePainter.repaintWholeSnake(frame,troll);                                
                SnakePainter.repaintSnake(frame,troll,food);
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
        ScoreBoard score=new ScoreBoard(frame, snake.getSize());            
    }
}