import java.awt.Color;
import acm.graphics.*;

public class SnakeSegment {
  
	public int x;
	public int y;
	public GOval segment;
	
	/**
	 * SnakeSegment: A constructor that creates a SnakeSegment object
	 */
	public SnakeSegment(int x_displacement, int y_displacement, int size){
		x = x_displacement;
		y = y_displacement;
		segment = new GOval(x, y, size, size);
		segment.setFilled(true);
		segment.setColor(Color.RED);
	}
	
	/**
	 * SnakeSegment: The same constructor as above,
	 * except that it takes a color parameter
	 */
	public SnakeSegment(int x_displacement, int y_displacement, int size, Color color){
		x = x_displacement;
		y = y_displacement;
		segment = new GOval(x, y, size, size);
		segment.setFilled(true);
		segment.setColor(Color.RED);
	}
	
	//returns the x coordinate of the snake segment
	public int getX(){
		return x;
	}
	
	//sets the x coordinate of the snake segment
	public void setX(int temp){
		x = temp;
	}
	
	//returns the y coordinate of the snake segment
	public int getY(){
		return y;
	}
	
	//sets the y coordinate of the snake segment
	public void setY(int temp){
		y = temp;
	}
	
	//set the segment color
	public void setColor(Color color){
		segment.setColor(color);
	}
	
	//returns the GOval segment
	public GOval getSegment(){
		return segment;
	}
	
}
