import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

class AskFrame extends JFrame implements ActionListener {
    private static boolean isInitialized=false;
    
    private JTextField textx, texty;
    private JButton play;
    private JCheckBox checkBox;
    private JLabel promptx, prompty;
    private int userx, usery;
    private Box labelBox, fieldBox, selectBox;
    private boolean turbo=false;

    public AskFrame() { //prompt user for playing field dimensions
        super("Define playing field!");
        setLayout(new FlowLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(205,140);
        setResizable(true);        
        promptx=new JLabel("Squares x (10-150):");
        add(promptx);
        textx=new JTextField(5);
        add(textx);
        prompty=new JLabel("Squares y (10-90):   ");
        add(prompty);
        texty=new JTextField(5);
        add(texty);        
        checkBox=new JCheckBox("turbo?",false);
        play=new JButton("play");
        play.addActionListener(this);
        textx.addActionListener(this);
        texty.addActionListener(this);
        checkBox.addActionListener(this);
        checkBox.setActionCommand("turbo");
        add(Box.createRigidArea(new Dimension(150,7)));    
        add(checkBox);
        add(play);
        setVisible(true);           
    }
    
    public void actionPerformed(ActionEvent evt) { //process user input
        if(evt.getActionCommand().equals("turbo")) {
            if(checkBox.isSelected()) {
                turbo=true;
            } else {
                turbo=false;
            }
        } else {
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
}