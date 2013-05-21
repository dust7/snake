import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

class ScoreBoard extends JDialog implements ActionListener, KeyListener {
    public ScoreBoard(JFrame frame, int score) {
        super(frame,true);
        setSize(110,95);            
        setUndecorated(true);
        getContentPane().setBackground(new Color(202, 213, 251));
        setLayout(new FlowLayout());
        setLocationRelativeTo(frame);
        add(new JLabel("GAME OVER"));
        add(new JLabel("Snake Size: "+score));
        JButton ok=new JButton("OK");
        add(Box.createRigidArea(new Dimension(0,27)));        
        add(ok);
        ok.addActionListener(this);
        ok.addKeyListener(this);
        setVisible(true);
    }
    
    public void actionPerformed(ActionEvent evt) {
        setVisible(false);
        dispose();
    }
    
    public void keyPressed(KeyEvent evt) {        
        if(evt.getKeyCode()==10 || evt.getKeyCode()==27) {//enter, escape
            setVisible(false);
            dispose();
        }
    }
    
    //override unused KeyListener methods
    public void keyReleased(KeyEvent evt) {}
    
    public void keyTyped(KeyEvent evt) {}
}