import java.util.Random;
import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;

class TrollSnake extends Snake {
    Random rand=new Random();
    
    public TrollSnake(Grid grid) {
        super(grid);
        int quartsqx=grid.getSQX()/4;
        int quartsqy=grid.getSQY()/4;
        for(int i=1; i<=quartsqx; i++) {
            moveLeft();
        }
        boolean rb=rand.nextBoolean();           
        if(rb) {
            for(int i=1; i<rand.nextInt(quartsqx); i++) {
                moveUp();
            }
        } else {
            for(int i=1; i<rand.nextInt(quartsqx); i++) {
                moveDown();
            }
        }
        this.setHeadColor(new Color(255,96,0));
        this.setBodyColor(new Color(255,215,0));
    }
    
    private boolean[] evalMove(Grid grid, int where) {
        int headx=(int)getPartsElement(0).getX();
        int heady=(int)getPartsElement(0).getY();
        Point dummy=new Point();
        if(where==0) {
            dummy.move(headx,heady-Grid.SIDE);            
        } else if(where==1) {
            dummy.move(headx,heady+Grid.SIDE);            
        } else if(where==2) {
            dummy.move(headx-Grid.SIDE,heady);            
        } else if(where==3) {
            dummy.move(headx+Grid.SIDE,heady);            
        }        
        boolean[] eval=new boolean[2];
        eval[0]=pointBites(dummy) && !dummy.equals(getPartsElement(getSize()-1));        
        eval[1]=grid.deadzonePoint(dummy);        
        return eval;
    }
    
    private void move(int i) {
        if(i==0) {
            moveUp();            
        } else if(i==1) {
            moveDown();            
        } else if(i==2) {
            moveLeft();            
        } else if(i==3) {
            moveRight();            
        }
    }
    
    public void randMove(Grid grid) {
        boolean moved=false;
        boolean testedLast=false;              
        int chosen;
        int rn=rand.nextInt(10);
        
        ArrayList<Integer> directions=new ArrayList<Integer>(4);
        ArrayList<Integer> forcedDir=new ArrayList<Integer>(4);
        for(int i=0; i<=3; i++) {
            directions.add(i);
        }        
        
        while(directions.size()>=1 && !moved) {            
            if(!testedLast && rn!=0) { //90% chance to try last last move first
                chosen=getLastMoved();
                testedLast=true;                
            } else {            
                chosen=directions.get(rand.nextInt(directions.size()));
            }        
            boolean[] evalChosen=evalMove(grid,chosen);
            if(!evalChosen[0] && !evalChosen[1]) {
                move(chosen);                
                moved=true;                
            } else {
                directions.remove(directions.indexOf(chosen));
                if(evalChosen[0] && !evalChosen[1]) {
                    forcedDir.add(chosen);
                }
            }            
        }
        if(!moved) {
            move(forcedDir.get(rand.nextInt(forcedDir.size())));            
        }
    }
}