import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

class SnakeFrame extends JFrame implements WindowListener, KeyListener {
    private static final int EXTRA_X=10; //because JFrame is a bitch   
    private static final int EXTRA_Y=32;
    
    private Color bg=new Color(215, 215, 215);
    private int lastListened=-1; //last user input, 0: up, 1: down, 2:left, 3: right    
    
    public SnakeFrame(int userx, int usery) {
        super(userx+"x"+usery);        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);        
        addWindowListener(this);            
        addKeyListener(this);            
        setSize((userx+2)*Grid.SIDE+EXTRA_X,(usery+2)*Grid.SIDE+EXTRA_Y);        
        setVisible(true);
        repaint();        
    }
    
    public void paint(Graphics g) {} //override default paint method

    public void setBg(Color col) {
        bg=col;
    }

    public Color getBg() {
        return new Color(bg.getRed(), bg.getGreen(), bg.getBlue());
    }

    public void refresh() {
        SnakePainter.paintDeadzone(this,SnakeGame.getGrid());
        SnakePainter.paintPlayzone(this,SnakeGame.getGrid());
        SnakePainter.repaintWholeSnake(this,SnakeGame.getTroll());
        SnakePainter.repaintWholeSnake(this,SnakeGame.getSnake());        
        SnakePainter.repaintFood(this,SnakeGame.getFood());
    }    
    
    public void windowDeiconified(WindowEvent evt) {             
        refresh();       
    }
    
    public void windowActivated(WindowEvent evt) {         
        refresh();   
    }    
    
    public void windowDeactivated(WindowEvent evt) {
        refresh();
    }

    public void keyPressed(KeyEvent evt) {
        int keyCode=evt.getKeyCode();
        if(keyCode==37) { //left arrow
            lastListened=2;
        } else if(keyCode==38) { //up arrow
            lastListened=0;
        } else if(keyCode==39) { //right arrow
            lastListened=3;
        } else if(keyCode==40) { //down arrow
            lastListened=1;
        }            
    }

    public int getLastListened() {
        return lastListened;
    }    

    //override unused abstract methods of WindowListener and KeyListener
    public void windowClosed(WindowEvent evt) {}
    
    public void windowClosing(WindowEvent evt) {}
    
    public void windowIconified(WindowEvent evt) {}
    
    public void windowOpened(WindowEvent evt) {}
    
    public void keyReleased(KeyEvent evt) {}
    
    public void keyTyped(KeyEvent evt) {}
}