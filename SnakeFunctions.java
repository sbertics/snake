import java.awt.Color;

/*
 * My own random generator class (I started the program the day
 * before I learned about the RandomGenerator class)
 */
public class SnakeFunctions {
  
	/**
	 * A random number generator that returns an integer
	 * between the min and max parameters inclusively
	 */
	public static int randomInt(int min, int max){
		double random = (max - min + 1)*Math.random() + min;
		return (int)random;
	}
	
	/**
	 * A random color is generated.
	 */
	public static Color randomColor(int beginColor, int endColor){
		int temp = randomInt(beginColor, endColor);
		switch(temp){
			case 1: return Color.PINK;
			case 2: return Color.MAGENTA;
			case 3: return Color.RED;
			case 4: return Color.ORANGE;
			case 5: return Color.YELLOW;
			case 6: return Color.GREEN;
			case 7: return Color.CYAN;
			case 8: return Color.BLUE;
			case 9: return Color.GRAY;
			default: return Color.BLACK;
		}
	}
}
