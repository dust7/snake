import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

class AskFrame extends JFrame implements ActionListener, KeyListener {
    private boolean isInitialized=false;    
    private JTextField textx, texty;
    private JButton play;
    private JCheckBox checkBox;
    private JLabel promptx, prompty;
    private int userx, usery;    
    private boolean turbo=false;

    public AskFrame() { //prompts user for playing field dimensions and game mode
        super("Define playing field!");
        setLayout(new FlowLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(205,140);
        setResizable(false); 
        //add components    
        promptx=new JLabel("Squares x (10-150):");
        add(promptx);
        textx=new JTextField(5);
        add(textx);
        prompty=new JLabel("Squares y (10-90):   ");
        add(prompty);
        texty=new JTextField(5);
        add(texty);
        add(Box.createRigidArea(new Dimension(150,7)));    
        checkBox=new JCheckBox("turbo?",false);
        checkBox.setActionCommand("turbo");
        add(checkBox);
        play=new JButton("play");
        add(play);
        //set listeners
        textx.addActionListener(this);
        texty.addActionListener(this);
        checkBox.addActionListener(this);
        checkBox.addKeyListener(this);
        play.addActionListener(this);
        UIManager.put("Button.defaultButtonFollowsFocus", Boolean.TRUE);//buttons resp.
                                                                        //to enter
        setVisible(true);           
    }
    
    public void actionPerformed(ActionEvent evt) { 
        if(evt.getActionCommand().equals("turbo")) {
            if(checkBox.isSelected()) {
                turbo=true;
            } else {
                turbo=false;
            }
        } else {
            start();      
        }       
    }
    
    public void keyPressed(KeyEvent evt) {        
        if(evt.getKeyCode()==10) {
            if(checkBox.isSelected()) {
                checkBox.setSelected(false);
                turbo=false;
            } else {
                checkBox.setSelected(true);
                turbo=true;
            }
        }        
    }    
    
    private void start() {
        try {            
            userx=Integer.parseInt(textx.getText().trim());
            usery=Integer.parseInt(texty.getText().trim());
            if(userx<10) {
                userx=10;
            } else if(userx>150) {
                userx=150;
            }
            if(usery<10) {
                usery=10;
            } else if(usery>90) {
                usery=90;
            }
            isInitialized=true;            
        } catch (Exception ex) {}  
    }

    public boolean isInitialized() {
        return isInitialized;
    }

    public int getUserX() {
        return userx;
    }
    
    public int getUserY() {
        return usery;
    }

    public boolean getTurbo() {
        return turbo;
    }    
    
    //unused KeyListener methods
    public void windowClosed(WindowEvent evt) {}
    
    public void windowClosing(WindowEvent evt) {}
    
    public void windowIconified(WindowEvent evt) {}
    
    public void windowOpened(WindowEvent evt) {}
    
    public void keyReleased(KeyEvent evt) {}
    
    public void keyTyped(KeyEvent evt) {}    
}