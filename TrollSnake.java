import java.util.Random;
import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;

class TrollSnake extends Snake {
    Random rand=new Random();
    
    public TrollSnake(Grid grid) {
        super(grid);
        this.setHeadColor(new Color(255,96,0));
        this.setBodyColor(new Color(255,215,0));        
        replace(grid);        
    }
    
    private void replace(Grid grid) {
        int quartsqx=grid.getSQX()/4;
        int quartsqy=grid.getSQY()/4;
        boolean rb=rand.nextBoolean();
        if(rb) {
            for(int i=1; i<=rand.nextInt(quartsqx)+2; i++) { 
                move(2); //left
            }          
        } else {
            for(int i=1; i<=rand.nextInt(quartsqx)+2; i++) {
                move(3); //right
            }
        }        
        rb=rand.nextBoolean();           
        if(rb) {
            for(int i=1; i<rand.nextInt(quartsqy)+2; i++) {
                move(0); //up
            }
        } else {
            for(int i=1; i<rand.nextInt(quartsqy)+2; i++) {
                move(1); //down
            }
        }
    }
    
    public boolean bites(Snake snake) {        
        for(int i=0; i<snake.getSize(); i++) {
            if(getPartsElement(0).equals(snake.getPartsElement(i))) {
                return true;
            }
        }
        return false;
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
            if(!testedLast && rn!=0) { //90% chance to first try last move again
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