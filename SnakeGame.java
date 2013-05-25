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
    private static boolean trolled=false;
    private static Random rand=new Random(); 
    //restart relevant:
    private static AskFrame askuser;
    private static boolean sameRestart=false; //game restarted with same settings?
    private static boolean playAgain=true;    

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

    public static void setPlayAgain(boolean set) {
        playAgain=set;
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
        return snake.bites(troll) || snake.bites() || snake.isInDeadzone(grid);
    }
    
    public static boolean win() { //grid full?
        return (grid.getSQX()-2)*(grid.getSQY()-2)-troll.getSize()
                                                 ==snake.getSize();
    }
    
    private static void playerMove(int command) {
        if(!trolled) {
            snake.move(command);
            return;
        } else if(command==0) {
            snake.move(1);            
        } else if(command==1) {
            snake.move(0);
        } else if(command==2) {
            snake.move(3);
        } else if(command==3) {
            snake.move(2);
        }
        trolled=false;
        snake.setHeadColor(new Color(2,157,16));
    }
    
    public static void setSameRestart(boolean set) {
        sameRestart=set;
    }    
    
    //debugging methods
    private static String normLocOut(Point pt) {
        int x=(int)pt.getX();
        int y=(int)pt.getY();
        x=x-Grid.SIDE-Grid.SIDE/2-Grid.OFFSET_X;
        x=x/Grid.SIDE;
        y=y-Grid.SIDE-Grid.SIDE/2-Grid.OFFSET_Y;
        y=y/Grid.SIDE;
        return "("+x+","+y+")";
    }
    
    public static void inform() {
        System.out.println("\nsnake size: "+snake.getSize());
        System.out.println("head at: "+normLocOut(snake.getPartsElement(0)));    
        System.out.println("food at: "+normLocOut(food.getFoodPosition()));  
        System.out.println("time: "+time+" ms");        
    }
    
    public static void main(String[] args) {
        while(playAgain) { //ScoreBoard handles restart/exit (user input)
            //ask user for playzone dimensions and game mode
            if(!sameRestart) {
                askuser=new AskFrame();
                while(!askuser.isInitialized()) {
                    try {
                        Thread.sleep(100);                
                    } catch(InterruptedException ex){}
                }            
            askuser.setVisible(false);
            askuser.dispose();
            }        
            //init grid, snake, food, frame                           
            grid=new Grid(askuser.getUserX(), askuser.getUserY());        
            snake=new Snake(grid);        
            troll=new TrollSnake(grid);            
            food=new Food(grid);
            while(food.isOnSnake(snake) || food.isOnSnake(troll)) {
                food.jump(grid);
            }       
            SnakeFrame frame=new SnakeFrame(askuser.getUserX(), askuser.getUserY());
            frame.setLocationRelativeTo(askuser);     
            if(askuser.getTurbo()) {
                time=25;
                frame.setTitle(askuser.getUserX()+"x"+askuser.getUserX()+" T");    
            }
            //prevent painting bugs on startup, give frame time to load
            try {
                Thread.sleep(100);
            } catch(InterruptedException ex) {}
            frame.refresh();    
            //start game                           
            while(!gameOver() && !win()) {
                //move
                int command=frame.getLastListened(); //0: up, 1: down, 2:left, 3: right
                int lastMoved=snake.getLastMoved();
                if(command==0 && lastMoved!=1 && command!=lastMoved) { 
                    playerMove(0);                            
                } else if(command==1 && lastMoved!=0 && command!=lastMoved) {
                    playerMove(1);                
                } else if(command==2 && lastMoved!=3 && command!=lastMoved) {
                    playerMove(2);                   
                } else if(command==3 && lastMoved!=2 && command!=lastMoved) {
                    playerMove(3);                    
                } else if(lastMoved!=-1) {                               
                    snake.move(lastMoved);
                }                  
                if(lastMoved!=-1) {
                    ((TrollSnake)troll).randMove(grid);                
                }
                //trolled?
                if((troll.eats(food) || troll.bites(snake)) && !snake.bites(troll)) {
                    trolled=true;                    
                    snake.setHeadColor(new Color(106,90,205));
                }
                //food eaten?    
                if(snake.eats(food) || troll.eats(food)) {
                    adjustTime();
                    troll.grow();
                    snake.grow();                
                    SnakePainter.repaintWholeSnake(frame,troll);                                 
                    SnakePainter.repaintSnake(frame,troll,food);                        
                    SnakePainter.repaintSnake(frame,snake,food);                      
                    while(!win() && (food.isOnSnake(snake) || food.isOnSnake(troll))) {
                        food.jump(grid);
                    }
                    SnakePainter.repaintFood(frame,food);
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
            //prepare restart
            time=150;
            trolled=false;            
            frame.setVisible(false);
            frame.dispose();
            while(!score.isFinished()) { //wait for decision to exit or restart
                try {
                    Thread.sleep(100);                
                } catch(InterruptedException ex){}
            }
            score=null;    
        }        
        System.exit(0);    
    }
}