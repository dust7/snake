import java.awt.Point;
import java.util.Random;
import java.awt.Color;

class Food {
    private Point foodPosition=new Point();
    private Random rand=new Random();
    private Color foodColor=new Color(255,20,147); //default Color    
    
    public Food(Grid grid) {
        jump(grid);        
    }
    
    public Food(Food cloneme) {
        this.foodPosition=new Point(cloneme.foodPosition);
        this.rand=cloneme.rand;
        this.foodColor=new Color(cloneme.foodColor.getRed(), cloneme.foodColor.getGreen(),
                                 cloneme.foodColor.getBlue());
    }
    
    public void jump(Grid grid) {
        int rn=rand.nextInt(grid.getPlayzoneSize());
        int x=(int)grid.getPlayzoneElement(rn).getX();
        int y=(int)grid.getPlayzoneElement(rn).getY();        
        foodPosition.move(x,y);             
    }
    
    public boolean isOnSnake(Snake snake) {
        for(int i=0; i<snake.getSize(); i++) {
            if(foodPosition.equals(snake.getPartsElement(i))) {
                return true;
            }
        }
        return false;
    }    
    
    public Point getFoodPosition() {
        return new Point(foodPosition);
    }

    public void setFoodColor(Color col) {
        foodColor=col;
    }

    public Color getFoodColor() {
        return new Color(foodColor.getRed(), foodColor.getGreen(), foodColor.getBlue());
    }    
}