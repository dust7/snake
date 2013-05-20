import java.awt.Graphics;
import java.awt.Point;
import java.awt.Color;
import javax.swing.JFrame;

class SnakePainter {
    public static void fillSq(JFrame frame, Point pt, Color col, int resize) { //Point is center
        Graphics gr=frame.getGraphics();
        gr.setColor(col);    
        int centerx=(int)pt.getX();
        int centery=(int)pt.getY();
        gr.fillRect(centerx-Grid.SIDE/2-resize, centery-Grid.SIDE/2-resize, 
                                       Grid.SIDE+resize, Grid.SIDE+resize);
    }  

    public static void drawSq(JFrame frame, Point pt) {
        Graphics gr=frame.getGraphics();        
        int centerx=(int)pt.getX();
        int centery=(int)pt.getY();
        gr.drawRect(centerx-Grid.SIDE/2, centery-Grid.SIDE/2, Grid.SIDE, Grid.SIDE);
    }
    
    public static void fillOv(JFrame frame, Point pt, Color col, int resize) { 
        Graphics gr=frame.getGraphics();
        gr.setColor(col);    
        int centerx=(int)pt.getX();
        int centery=(int)pt.getY();
        gr.fillOval(centerx-Grid.SIDE/2-resize, centery-Grid.SIDE/2-resize, 
                                       Grid.SIDE+resize, Grid.SIDE+resize);
    }
    
    public static void drawOv(JFrame frame, Point pt, int resize) { 
        Graphics gr=frame.getGraphics();          
        int centerx=(int)pt.getX();
        int centery=(int)pt.getY();
        gr.drawOval(centerx-Grid.SIDE/2-resize, centery-Grid.SIDE/2-resize, 
                                       Grid.SIDE+resize, Grid.SIDE+resize);
    }
    
    public static void paintDeadzone(JFrame frame, Grid grid) {
        //asas
        for(int i=0; i<grid.getDeadzoneSize(); i++) {
            fillSq(frame, grid.getDeadzoneElement(i), Color.black, 0);
        }
    frame.repaint();
    }
    
    public static void paintPlayzone(JFrame frame, Grid grid) {               
        for(int i=0; i<grid.getPlayzoneSize(); i++) {
            drawSq(frame, grid.getPlayzoneElement(i));
            fillSq(frame, grid.getPlayzoneElement(i), ((SnakeFrame)frame).getBg(),-1);
        }
    frame.repaint();
    }   
 
    public static void repaintSnake(JFrame frame, Snake snake, Food food) {
        if(!snake.eats(food) && !food.getFoodPosition().equals(snake.getPrevTailPos())) {
            fillSq(frame, snake.getPrevTailPos(), ((SnakeFrame)frame).getBg(), -1);           
        }
        if(snake.getSize()>=2) { //repaint previous head to body color            
            fillOv(frame,snake.getPartsElement(1),snake.getBodyColor(),0);
            drawOv(frame,snake.getPartsElement(1),0);
        }        
        fillOv(frame,snake.getPartsElement(0),snake.getHeadColor(), 0);
        drawOv(frame,snake.getPartsElement(0), 0);
        frame.repaint();
    }
    
    public static void repaintWholeSnake(JFrame frame, Snake snake) {      
        for(int i=1; i<snake.getSize(); i++) {
            fillOv(frame,snake.getPartsElement(i),snake.getBodyColor(),0);
            drawOv(frame, snake.getPartsElement(i), 0);
        }
        //paint head last, else is overpainted if snake.bites()
        fillOv(frame,snake.getPartsElement(0),snake.getHeadColor(), 0);
        drawOv(frame,snake.getPartsElement(0), 0);    
    }
    
    public static void repaintFood(JFrame frame, Food food) {       
        fillOv(frame, food.getFoodPosition(), food.getFoodColor(), 0);        
        drawOv(frame, food.getFoodPosition(), 0);        
    }
}