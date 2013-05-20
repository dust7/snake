import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

class SnakeFrame extends JFrame implements WindowListener, KeyListener {
    private static final int EXTRA_X=10; //because JFrame is a bitch   
    private static final int EXTRA_Y=32;
    
    private Color bg=Color.white; //default background
    private String lastListened=""; //stores last user input    
    
    public SnakeFrame(int userx, int usery) {
        super("Snake");        
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

    private void refresh() {
        SnakePainter.paintDeadzone(this,SnakeGame.getGrid());
        SnakePainter.paintPlayzone(this,SnakeGame.getGrid());
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
        if(keyCode==37) {
            lastListened="left";
        } else if(keyCode==38) {
            lastListened="up";
        } else if(keyCode==39) {
            lastListened="right";
        } else if(keyCode==40) {
            lastListened="down";
        }            
    }

    public String getLastListened() {
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