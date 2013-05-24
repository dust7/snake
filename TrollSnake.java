import java.util.Random;
import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;

class TrollSnake extends Snake {
    Random rand=new Random();
    private int prevDir=-1;
    private int prevPrevDir=-1;
    private int prevPrevPrevDir=-1;
    
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
            prevPrevDir=2;
        } else {
            for(int i=1; i<=rand.nextInt(quartsqx)+2; i++) {
                move(3); //right
            }
            prevPrevDir=3;
        }        
        rb=rand.nextBoolean();           
        if(rb) {
            for(int i=1; i<rand.nextInt(quartsqy)+2; i++) {
                move(0); //up
            }
            prevDir=0;
        } else {
            for(int i=1; i<rand.nextInt(quartsqy)+2; i++) {
                move(1); //down
            }
            prevDir=1;
        }
    }
    
    private void setPrevDirs(int nextmove) {
        if(nextmove!=getLastMoved()) { //if next move will change direction
            prevPrevPrevDir=prevPrevDir;
            prevPrevDir=prevDir;
            prevDir=getLastMoved(); 
        }       
    }
    
    private boolean isCircleMove(int nextmove) { //0:up, 1:down, 2:left, 3:right
        if(nextmove==0) {
            return     (prevDir==2 && prevPrevDir==1 && prevPrevPrevDir==3)
                    || (prevDir==3 && prevPrevDir==1 && prevPrevPrevDir==2);
        } else if(nextmove==1) {
            return     (prevDir==3 && prevPrevDir==0 && prevPrevPrevDir==2) 
                    || (prevDir==2 && prevPrevDir==0 && prevPrevPrevDir==3);
        } else if(nextmove==2) {
            return     (prevDir==1 && prevPrevDir==3 && prevPrevPrevDir==0)
                    || (prevDir==0 && prevPrevDir==3 && prevPrevPrevDir==1);
        } else if(nextmove==3) {
            return     (prevDir==0 && prevPrevDir==2 && prevPrevPrevDir==1) 
                    || (prevDir==1 && prevPrevDir==3 && prevPrevPrevDir==0);
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
        boolean[] eval=new boolean[3];
        eval[0]=pointBites(dummy) && !dummy.equals(getPartsElement(getSize()-1));        
        eval[1]=grid.deadzonePoint(dummy);
        eval[2]=isCircleMove(where);    
        return eval;
    }
    
    public void randMove(Grid grid) {
        boolean moved=false;
        boolean testedLast=false;              
        int chosen;
        int rn=rand.nextInt(20);
        
        ArrayList<Integer> directions=new ArrayList<Integer>(4);
        ArrayList<Integer> circleDir=new ArrayList<Integer>(4);        
        ArrayList<Integer> biteDir=new ArrayList<Integer>(4);
        for(int i=0; i<=3; i++) {
            directions.add(i);
        }        
        
        while(directions.size()>=1 && !moved) {            
            if(!testedLast && rn!=0 && rn!=1 && rn!=2) { //85% chance to try keeping direction
                chosen=getLastMoved();
                testedLast=true;                
            } else {            
                chosen=directions.get(rand.nextInt(directions.size()));
            }        
            boolean[] evalChosen=evalMove(grid,chosen);
            if(!evalChosen[0] && !evalChosen[1] && !evalChosen[2]) {
                setPrevDirs(chosen);
                move(chosen);                
                moved=true;                
            } else {                
                directions.remove(directions.indexOf(chosen));
                if(evalChosen[0] && !evalChosen[1]) {
                    biteDir.add(chosen);
                } else if(!evalChosen[0] && !evalChosen[1] && evalChosen[2]) {
                    circleDir.add(chosen);
                }
            }            
        }
        if(!moved && circleDir.size()!=0) {
            chosen=circleDir.get(rand.nextInt(circleDir.size()));
            setPrevDirs(chosen);
            move(chosen);
            moved=true;    
        }
        if(!moved) {
            chosen=biteDir.get(rand.nextInt(biteDir.size()));
            setPrevDirs(chosen);
            move(chosen);  
        }
    }
}