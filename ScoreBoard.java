import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

class ScoreBoard extends JDialog implements ActionListener {
    private boolean isFinished=false;

    public ScoreBoard(JFrame frame, int score) {
        super(frame,true);
        setSize(135,165);            
        setUndecorated(true);
        getContentPane().setBackground(new Color(202, 213, 251));
        setLayout(new FlowLayout());
        setLocationRelativeTo(frame);
        //g/o group
        add(new JLabel("GAME OVER"));
        add(new JLabel("Snake Size: "+score));        
        //exit group
        JButton exit=new JButton("EXIT");
        add(Box.createRigidArea(new Dimension(0,15)));        
        add(exit);
        //restart group
        JPanel restartPanel=new JPanel();
        restartPanel.setLayout(new BoxLayout(restartPanel, BoxLayout.Y_AXIS));
        add(Box.createRigidArea(new Dimension(0,35)));    
        restartPanel.add(new JLabel("            Restart?"));
        JButton oldstart=new JButton("KEEP SETTINGS");
        oldstart.setActionCommand("old");
        restartPanel.add(oldstart);
        JButton newstart=new JButton("NEW SETTINGS ");
        newstart.setActionCommand("new");
        restartPanel.add(newstart);
        add(restartPanel);
        //set listeners
        exit.addActionListener(this);
        oldstart.addActionListener(this);
        newstart.addActionListener(this);        
        UIManager.put("Button.defaultButtonFollowsFocus", Boolean.TRUE);
        setVisible(true);
    }
    
    public void actionPerformed(ActionEvent evt) {
        String command=evt.getActionCommand();
        if(command.equals("EXIT")) {
            System.exit(0);
        } else if(command.equals("old")) {
            isFinished=true;
            SnakeGame.setSameRestart(true);
            setVisible(false);
            dispose();
        } else if(command.equals("new")) {
            isFinished=true;
            SnakeGame.setSameRestart(false);
            setVisible(false);
            dispose();
        }     
    }
    
    public boolean isFinished() {
        return isFinished;
    }       
}