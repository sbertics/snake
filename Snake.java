/*********************************************************************
 * THE SNAKE PROGRAM
 * by Scott Bertics
 * July 13, 2011
 * 
 * File: Snake.java, SnakeSegment.java, SnakeFunctions.java
 * The basic snake game (snake moves around screen, eating food/ growing)
 * has been enhanced in this program to include:
 *   Five styles of gameplay
 * 	Two sizes of snake
 * 	Single and Multi-player modes
 * 	Four power-ups
 * 	Three types of food
 *  Animations
 *  .png images
 * 	and Photoshop Graphics (for a clean finish)
 ****************************************************************************/

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import acm.graphics.*;
import acm.program.*;

public class Snake extends GraphicsProgram{
	
	
	/**
	 * Instance variables and constants
	 */
	//I have no clue what serial version UID is...
	private static final long serialVersionUID = 1L;
	//The screen size
	public static final int APPLICATION_WIDTH = 800;
	public static final int APPLICATION_HEIGHT = 600;
	//pause for this long between increments
	private static final int PAUSE_TIME = 50;
	//Each snake segment is a square with length SIZE
	private static final int SIZE = 10;
	//begin the game with these many lives
	private static final int BEGIN_LIVES = 3;
	//The snake can grow by these values after eating a food dot
	private static final int GROW_MAX = 3; //MUST BE EQUAL TO NUMBER OF AVAILABLE FOOD IMAGES
	private static final int GROW_MIN = 1;
	//The option buttons on the main screen
	public static final int BUTTON_WIDTH = 100;
	public static final int BUTTON_HEIGHT = 60;
	//The image offset distance
	public static final int FOOD_OFFSET = 5;
	
	
	
	//pause for this long between increments
	private static int time_delay;
	
	//Screen height and screen width variables
	private static int width, height;
	
	//Each snake segment is inscribed in a square with dimensions size x size
	private static int size;
	
	//The arraylist that holds the snake segments
	private static ArrayList<SnakeSegment> snake, multiplayer_snake;
	
	//The arraylist that holds the heart images
	private static ArrayList<GImage> heart, multiplayer_heart;
	
	//The arraylist of GImages to represent the food
	private static ArrayList<String> food_img, power_up_img, uh_oh_img;
	
	//Maintains what powerup is currently functioning and displays help text on the bottom of the screen
	private static String powerup;
	private static GLabel powerup_label;
	
	//Number of lives left
	private static int lives, multiplayer_lives;
	
	//Has the snake lost a life?
	private static boolean life_lost, multiplayer_life_lost;
	
	//The length of the snake in snake segments
	private static int snake_length, multiplayer_snake_length;
	
	//The coordinates of the food dot, and the value by which the snake will grow when it eats the food dot
	private static int foodX, foodY, food_value;
	
	//The food graphics
	private static GImage food;
	
	//How far the snake will move in pixels
	private static int incrementX, incrementY, multiplayer_incrementX, multiplayer_incrementY;
	
	//The score is directly related to the snake_length, however it is not reset to zero when the snake loses a life
	private static int score, multiplayer_score;
	private static GLabel score_label, multiplayer_score_label;
	
	private static GImage play, options, help, exit, return_button;
	
	private static GLabel credit;
	
	private static GImage random_uh_oh;
	
	private static int uh_oh_count;
	
	private static GImage play_highlight, options_highlight, help_highlight, exit_highlight, return_button_highlight;
	
	private static boolean play_boolean, options_boolean, help_boolean, pause_boolean, credit_boolean;
	
	private static GLabel normal_label, inverted_label, color_label, epilepsy_label, uh_oh_label, small_label, large_label, single_player, multi_player;
	
	//The borders of the screen
	private static int left_border, right_border, top_border, bottom_border;
	
	private GRect whitespace;
	
	//The snake can turn at most once per iteration of the main loop
	private static boolean turn, multiplayer_turn;
	
	//The game mode can be: "normal", "inverted", "color", and "epilepsy"
	private static String style;
	
	//A single player or multiplayer mode?
	private static boolean multiplayer;
	
	//A multiplier used to triple the number of segments that the snake will add per food dot
	private static int three_times, multiplayer_three_times;
	
	//Controls and displays the pause command during gameplay
	private static GLabel pause_label;
	private static boolean wait_boolean;
	
	//The snake game window stays open until continuePlay is set to false by clicking on the exit button
	private static boolean continuePlay;
	
	
	
	
	
	public void run(){
		
		initializeConstants();
		
		while(continuePlay){
			
			mainScreen();
			
			if(play_boolean){
				playScreen();
				endGame();
			}else if(options_boolean){
				optionsScreen();
			}else if(help_boolean){
				helpScreen();
			}else if(credit_boolean){
				creditScreen();
			}
		}
		System.exit(0); //Goodbye, world!
	}
	
	
	
	
	
	/**
	 * intializeConstants: Important instance variables are initialized.
	 * The other instance variables will be initialized right before
	 * the game starts in the playScreen method.
	 */
	private void initializeConstants(){
		
		continuePlay = true;
		
		time_delay = PAUSE_TIME;
		
		width = getWidth();
		height = getHeight();
		
		size = SIZE;
		
		multiplayer = false;
		
		life_lost = true;
		
		multiplayer_life_lost = true;
		
		style = "normal";
		
		snake = new ArrayList<SnakeSegment>(snake_length);
		
		multiplayer_snake = new ArrayList<SnakeSegment>(multiplayer_snake_length);
		
		heart = new ArrayList<GImage>(BEGIN_LIVES);
		
		multiplayer_heart = new ArrayList<GImage>(BEGIN_LIVES);
		
		uh_oh_img = new ArrayList<String>(11);
		uh_oh_img.add(0, "./img/focus.png");
		uh_oh_img.add(1, "./img/java.png");
		uh_oh_img.add(2, "./img/osvaldo.png");
		uh_oh_img.add(3, "./img/happy-chicken.png");
		uh_oh_img.add(4, "./img/pointers.png");
		uh_oh_img.add(5, "./img/turing.png");
		uh_oh_img.add(6, "./img/ubuntu.png");
		uh_oh_img.add(7, "./img/hi.png");
		uh_oh_img.add(8, "./img/dennis.png");
		uh_oh_img.add(9, "./img/evolution.png");
		uh_oh_img.add(10, "./img/computer_programming_101.png");

		uh_oh_count = 9;
		random_uh_oh = new GImage(uh_oh_img.get(uh_oh_count));
		
		food_img = new ArrayList<String>(GROW_MAX);
		food_img.add(0, "./img/apple_icon.png");
		food_img.add(1, "./img/banana_icon.png");
		food_img.add(2, "./img/strawberry_icon.png");
		
		power_up_img = new ArrayList<String>(4);
		power_up_img.add(0, "./img/extra_life.png");
		power_up_img.add(1, "./img/coin.png");
		power_up_img.add(2, "./img/three_times.png");
		power_up_img.add(3, "./img/accelerate.png");
		
		powerup_label = new GLabel("");
		three_times = 1;
		if(multiplayer){
			multiplayer_three_times = 1;
		}
		
		play = new GImage("./img/button3.png");
		play_highlight = new GImage("./img/button3-2.png");
		options = new GImage("./img/button5.png");
		options_highlight = new GImage("./img/button5-2.png");
		help = new GImage("./img/button4.png");
		help_highlight = new GImage("./img/button4-2.png");
		exit = new GImage("./img/button2.png");
		exit_highlight = new GImage("./img/button2-2.png");
		return_button = new GImage("./img/button2.png");
		return_button_highlight = new GImage("./img/button2-2.png");
		
		credit = new GLabel("A Game by Scott Bertics");
		
		play_boolean = false;
		options_boolean = false;
		help_boolean = false;
		credit_boolean = false;
		wait_boolean = false;
		pause_boolean = true;
		
		normal_label = new GLabel("Normal");
		inverted_label = new GLabel("Inverted");
		color_label = new GLabel("Color");
		epilepsy_label = new GLabel("Epilepsy");
		uh_oh_label = new GLabel("Uh-Oh!");
		small_label = new GLabel("Small");
		large_label = new GLabel("Large");
		single_player = new GLabel("Single Player");
		multi_player = new GLabel("Multi-Player");
		
		addKeyListeners();
		
		addMouseListeners();
	}
	
	/**
	 * mainScreen: The beginning screen is loaded in this method
	 * and it includes a title jpeg, four buttons (play, options, help, and exit),
	 * and a credit link at the bottom.  This method also processes the
	 * animation that brings these images and buttons onto the screen.
	 */
	private void mainScreen(){
		
		GImage title = new GImage("./img/snake.png");
		title.setLocation(width/2 - title.getWidth()/2, 0);
		add(title);
		
		play.setLocation(width/5 - play.getWidth()/2, 3*height/4);
		add(play);
		
		play_highlight.setLocation(play.getX(), play.getY());
		play_highlight.setVisible(false);
		add(play_highlight);
		
		options.setLocation(2*width/5 - options.getWidth()/2, 3*height/4);
		add(options);
		
		options_highlight.setLocation(options.getX(), options.getY());
		options_highlight.setVisible(false);
		add(options_highlight);

		help.setLocation(3*width/5 - help.getWidth()/2, 3*height/4);
		add(help);

		help_highlight.setLocation(help.getX(), help.getY());
		help_highlight.setVisible(false);
		add(help_highlight);

		exit.setLocation(4*width/5 - exit.getWidth()/2, 3*height/4);
		add(exit);

		exit_highlight.setLocation(exit.getX(), exit.getY());
		exit_highlight.setVisible(false);
		add(exit_highlight);

		credit.setFont("*-BOLD-14");
		credit.setLocation(width/2 - credit.getWidth()/2, height/2 + credit.getAscent());
		add(credit);
		
		play.sendToBack();
		options.sendToBack();
		help.sendToBack();
		exit.sendToBack();
		
		//The system waits here until a button is pressed, and pause_boolean is set to false
		while(pause_boolean){
			pause(100);
		}
		//Pause is reset for the next pause point
		pause_boolean = true;
		
	}
	
	/**
	 * playScreen:  All of the elements of the play screen are constructed and added,
	 * and then the main loop in which game play occurs is called.
	 */
	private void playScreen(){
		
		//The width of the left border
		left_border = size;
		
		int temp_top = 0;
		while(temp_top < (height / 10)){
			temp_top += size;
		}
		temp_top -= size;
		top_border = temp_top;
		
		//The width of the right border
		int temp_right = 0;
		while(temp_right < width){
			temp_right += size;
		}
		temp_right -= size;
		right_border = width - temp_right;
		
		//The height of the bottom border
		int temp_bottom = 0;
		while(temp_bottom < height){
			temp_bottom += size;
		}
		temp_bottom -= size;
		bottom_border = height - temp_bottom + 2*size; //2*size allows space for a dialog scroll bar
		
		//Construct the borders
		//Note: All of the borders must be shifted off by one pixel to scale properly
		GRect left = new GRect(0, 0, left_border - 1, height);
		GRect right = new GRect(width - right_border + 1, 0, right_border - 1, height);
		GRect top = new GRect(0, 0, width, top_border - 1);
		GRect bottom = new GRect(0, height - bottom_border + 1, width, bottom_border - 1);
		
		//Fill the borders
		left.setFilled(true);
		right.setFilled(true);
		top.setFilled(true);
		bottom.setFilled(true);
		
		//For the inverted style
		if(style == "inverted"){
			left.setColor(Color.WHITE);
			right.setColor(Color.WHITE);
			top.setColor(Color.WHITE);
			bottom.setColor(Color.WHITE);
		}
		
		//Add the borders to the canvas
		add(left);
		add(right);
		add(top);
		add(bottom);
		
		if(style == "uh-oh"){
			uhOh();
		}
		
		//The bottom whitespace for a scroll bar
		GRect whitespace_bottom = new GRect(0, height - 2*size, width, 2*size);
		whitespace_bottom.setFilled(true);
		whitespace_bottom.setColor(Color.WHITE);
		whitespace_bottom.sendToFront();
		add(whitespace_bottom);
		
		//The title whitespace
		int whitespace_height = 3 * top_border / 4;
		whitespace = new GRect(0, 0, width, whitespace_height);
		whitespace.setFilled(true);
		whitespace.setColor(Color.WHITE);
		add(whitespace);
		
		//The title label
		GImage temp_img = new GImage("./img/snake_small.png");
		temp_img.setLocation(width/2 - temp_img.getWidth()/2, whitespace_height/2 - temp_img.getHeight()/2);
		add(temp_img);
		
		//score
		score = 0;
		multiplayer_score = 0;
		
		//lives
		lives = BEGIN_LIVES;
		multiplayer_lives = BEGIN_LIVES;
		
		for(int i = 0; i < lives + 1; i++){
			heart.add(i, new GImage("./img/heart.png"));
			GImage temp = heart.get(i);
			temp.setLocation((i+1) * temp.getWidth(), whitespace.getHeight()/2);
			add(temp);
		}
		if(multiplayer){
			for(int i = 0; i < multiplayer_lives + 1; i++){
				multiplayer_heart.add(i, new GImage("./img/heart.png"));
				GImage temp = multiplayer_heart.get(i);
				temp.setLocation(width - ((i+1) * temp.getWidth()), height - bottom_border + size);
				add(temp);
			}
		}
		
		//add the first food square
		food = new GImage("");
		add(food);
		
		//Construct and add the score label
		score_label = new GLabel("");
		add(score_label);
		updateScore();
		
		if(multiplayer){
			multiplayer_score_label = new GLabel("");
			add(multiplayer_score_label);
			updateMultiplayerScore();
		}
		
		//Add the powerup label
		add(powerup_label);
		
		//Invert the colors on the screen
		if(style == "inverted"){
			setBackground(Color.BLACK);
		}
		
		if(multiplayer){
			while(lives > -1 && multiplayer_lives > -1){
				reset();
				if(lives == -1 || multiplayer_lives == -1){
					break;
				}
				threeTwoOne();
				loop();
			}
		}else{
			while(lives > 0){
				reset();
				threeTwoOne();
				loop();
			}
		}
		play_boolean = false;
	}
	
	
	/**
	 * reset: resets variables to allow play to continue
	 */
	private void reset(){
		
		//reset the powerups
		time_delay = PAUSE_TIME;
		three_times = 1;
		if(multiplayer){
			multiplayer_three_times = 1;
		}
		powerup_label.setLabel("");
		
		//clear the old snake from the screen before restarting
		for(int i = 0; i < snake.size(); i++){
			remove(snake.get(i).getSegment());
		}
		
		if(multiplayer){
			for(int i = 0; i < multiplayer_snake.size(); i++){
				remove(multiplayer_snake.get(i).getSegment());
			}
		}
		
		//clear the old snake
		snake.clear();
		
		if(multiplayer){
			multiplayer_snake.clear();
		}
		
		//remove a heart from the screen
		if(life_lost){
			remove(heart.get(lives));
			lives--;
			life_lost = false;
		}
		
		//remove a multiplayer heart from the screen
		if(multiplayer){
			if(multiplayer_life_lost){
				remove(multiplayer_heart.get(multiplayer_lives));
				multiplayer_lives--;
				multiplayer_life_lost = false;
			}
		}
		
		//only allow one turn per iteration
		turn = false;
		multiplayer_turn = false;
		
		//thus far, the snake is 0 units in length
		snake_length = 0;
		
		if(multiplayer){
			multiplayer_snake_length = 0;
		}
		
		//move by this distance in the x and y axes each turn
		incrementX = size;
		incrementY = 0;
		
		if(multiplayer){
			multiplayer_incrementX = -size;
			multiplayer_incrementY = 0;
		}
		
		//Create the first snake segment
		snake.add(0, new SnakeSegment(left_border, top_border, size));
		add(snake.get(0).getSegment());
		
		if(multiplayer){
			multiplayer_snake.add(0, new SnakeSegment(width - right_border - size, height - bottom_border - size, size));
			add(multiplayer_snake.get(0).getSegment());
		}
		
		//The snake grows from length 0 to length 1, and randomly selects a new food dot
		grow();
		if(multiplayer){
			multiplayerGrow();
		}
		food();
		foodValue();
		
		updateScore();
		if(multiplayer){
			updateMultiplayerScore();
		}
	}
	
	
	/**
	 * threeTwoOne: A countdown sequence that appears before each game.
	 */
	private void threeTwoOne(){
		GLabel num = new GLabel("");
		if(style == "inverted"){
			num.setColor(Color.WHITE);
		}else if(style == "uh-oh"){
			num.setColor(Color.RED);
		}
		String font_string = "*-*-";
		add(num);
		for(int i = 3; i > 0; i--){
			num.setLabel(Integer.toString(i));
			for(int j = 144; j > 32; j-=8){
				String temp = font_string + Integer.toString(j);
				num.setFont(temp);
				num.setLocation(width/2 - num.getWidth()/2, height/2 - num.getAscent()/2);
				pause(10);
			}
			pause(800);
		}
		num.setLabel("GO!");
		num.setFont("*-*-72");
		num.setLocation(width/2 - num.getWidth()/2, height/2 + num.getHeight()/2);
		pause(800);
		remove(num);
		incrementX = size;
		incrementY = 0;
		if(multiplayer){
			multiplayer_incrementX = -size;
			multiplayer_incrementY = 0;
		}
	}
	/**
	 * loop: The main loop in which the game occurs
	 * Order of commands inside the loop method
	 * 1.	The head coordinates are incremented
	 * 2.	Checks are performed to make sure that the head location is legal
	 * 3.	Check to see if the snake has eaten a food dot
	 * 4.	Each segment on the snake is moved to the location of the one in front of it
	 */
	private void loop(){
		
		while(true){
			
			if(style == "epilepsy"){
				setBackground(SnakeFunctions.randomColor(1, 10));
			}
				
			turn = false;
			multiplayer_turn = false;
			
			//increment the head coordinates
			snake.get(0).setX(snake.get(0).getX() + incrementX);
			snake.get(0).setY(snake.get(0).getY() + incrementY);
			
			if(multiplayer){
				multiplayer_snake.get(0).setX(multiplayer_snake.get(0).getX() + multiplayer_incrementX);
				multiplayer_snake.get(0).setY(multiplayer_snake.get(0).getY() + multiplayer_incrementY);
			}
			
			//is the snake off the screen?
			if((snake.get(0).getX() >= width - right_border) || (snake.get(0).getY() >= height - bottom_border) || (snake.get(0).getX() < left_border) || (snake.get(0).getY() < top_border)){
				life_lost = true;
				break;
			}
			
			if(multiplayer){
				if((multiplayer_snake.get(0).getX() >= width - right_border) || (multiplayer_snake.get(0).getY() >= height - bottom_border) || (multiplayer_snake.get(0).getX() < left_border) || (multiplayer_snake.get(0).getY() < top_border)){
					multiplayer_life_lost = true;
					break;
				}
			}
			
			//Did the snake head hit itself?
			//snake.get(0) is the head itself, so the for loop doesn't have to start at 0, it can start at 1
			for(int i = 1; i <= snake_length; i++){
				if((snake.get(0).getX() == snake.get(i).getX()) && (snake.get(0).getY() == snake.get(i).getY())){
					life_lost = true;
				}
			}
			if(life_lost){
				break;
			}
			
			if(multiplayer){
				for(int i = 1; i <= multiplayer_snake_length; i++){
					if((multiplayer_snake.get(0).getX() == multiplayer_snake.get(i).getX()) && (multiplayer_snake.get(0).getY() == multiplayer_snake.get(i).getY())){
						multiplayer_life_lost = true;
					}
				}
				if(multiplayer_life_lost){
					break;
				}
			}
			
			
			
			//did the snake head eat the food square?
			if(snake.get(0).getX()==foodX && snake.get(0).getY()==foodY){
				if(food_value == 0){
					powerUp();
				}
				for(int i = 0; i < three_times; i++){
					for(int j = 0; j < food_value; j++){
						grow();
					}
				}
				updateScore();
				food();
				//You can only get one powerup in a row
				if((food_value > 0) && (SnakeFunctions.randomInt(1, 4) == 1)){
					powerUp();
				}else{
					foodValue();
				}
			}
			
			if(multiplayer){
				if(multiplayer_snake.get(0).getX()==foodX && multiplayer_snake.get(0).getY()==foodY){
					if(food_value == 0){
						powerUp();
					}
					for(int i = 0; i < multiplayer_three_times; i++){
						for(int j = 0; j < food_value; j++){
							multiplayerGrow();
						}
					}
					updateMultiplayerScore();
					food();
					//You can only get one powerup in a row
					if((food_value > 0) && (SnakeFunctions.randomInt(1, 4) == 1)){
						powerUp();
					}else{
						foodValue();
					}
				}
			}
			
			//each segment on the snake, starting from the tail, is moved to the location of the segment in front of it.
			for(int i = snake_length; i > 0; i--){
				snake.get(i).setX(snake.get(i-1).getX());
				snake.get(i).setY(snake.get(i-1).getY());
				snake.get(i).getSegment().setLocation(snake.get(i).getX(), snake.get(i).getY());
			}
			
			//the head is moved
			snake.get(0).getSegment().setLocation(snake.get(0).getX(), snake.get(0).getY());
			
			if(multiplayer){
				//each segment on the snake, starting from the tail, is moved to the location of the segment in front of it.
				for(int i = multiplayer_snake_length; i > 0; i--){
					multiplayer_snake.get(i).setX(multiplayer_snake.get(i-1).getX());
					multiplayer_snake.get(i).setY(multiplayer_snake.get(i-1).getY());
					multiplayer_snake.get(i).getSegment().setLocation(multiplayer_snake.get(i).getX(), multiplayer_snake.get(i).getY());
				}
				
				//the head is moved
				multiplayer_snake.get(0).getSegment().setLocation(multiplayer_snake.get(0).getX(), multiplayer_snake.get(0).getY());
			}
			while(wait_boolean){
				pause(100);
			}
			
			//so as to avoid excessive speed
			pause(time_delay);
		}
	}
	
	
	/**
	 * grow: A snake segment is created, added to the arraylist,
	 * and added to the frame on top of the current tail segment
	 */
	private void grow(){
		snake.add(new SnakeSegment(snake.get(snake_length).getX(), snake.get(snake_length).getY(), size));
		snake_length++;
		if(style == "normal" || style == "epilepsy"){
			snake.get(snake_length).setColor(Color.BLACK);
		}else if (style == "inverted"){
			snake.get(snake_length).setColor(Color.WHITE);
		}else if (style == "colors"){
			snake.get(snake_length).setColor(SnakeFunctions.randomColor(1, 10));
		}
		add(snake.get(snake_length).getSegment());
		score += 10;
	}
	
	
	/**
	 * multiplayerGrow: A snake segment is created, added to the arraylist,
	 * and added to the frame on top of the current tail segment
	 */
	private void multiplayerGrow(){
		multiplayer_snake.add(new SnakeSegment(multiplayer_snake.get(multiplayer_snake_length).getX(), multiplayer_snake.get(multiplayer_snake_length).getY(), size));
		multiplayer_snake_length++;
		if(style == "normal" || style == "epilepsy"){
			multiplayer_snake.get(multiplayer_snake_length).setColor(Color.BLACK);
		}else if (style == "inverted"){
			multiplayer_snake.get(multiplayer_snake_length).setColor(Color.WHITE);
		}else if (style == "colors"){
			multiplayer_snake.get(multiplayer_snake_length).setColor(SnakeFunctions.randomColor(1, 10));
		}
		add(multiplayer_snake.get(multiplayer_snake_length).getSegment());
		multiplayer_score += 10;
	}
	
	
	/**
	 * food: regenerates the food dot location and assigns it a new random value.
	 */
	private void food(){
		foodX = size * SnakeFunctions.randomInt(left_border / size, (width - (left_border + right_border)) / size);
		foodY = size * SnakeFunctions.randomInt(top_border / size, (height - (top_border + bottom_border)) / size);
		food.setLocation(foodX - FOOD_OFFSET, foodY - FOOD_OFFSET);
		for(int i = 0; i<=snake_length; i++){
			if(snake.get(i).getX()==foodX && snake.get(i).getY()==foodY){
				food();
			}
		}
		if(multiplayer){
			for(int i = 0; i <= multiplayer_snake_length; i++){
				if(multiplayer_snake.get(i).getX()==foodX && multiplayer_snake.get(i).getY()==foodY){
					food();
				}
			}
		}
	}
	
	
	/**
	 * powerUp:  This method is called when a power up is created and again when the snake
	 * eats the powerup and it is implemented.  The four powerups are extra life, coins,
	 * growth multiplier, and accelerate.  The odds of these powerups appearing are respectively
	 * 4%, 46%, 25%, and 25%.  The accelerate, and growth multiplier powerups last until the next
	 * powerup is called.
	 */
	private void powerUp(){
		
		if(food_value == 0){
			
			time_delay = PAUSE_TIME;
			three_times = 1;
			
			if(powerup == "extra life"){
				if(multiplayer && multiplayer_snake.get(0).getX() == foodX && multiplayer_snake.get(0).getY() == foodY){
					multiplayer_heart.add(multiplayer_lives, new GImage("./img/heart.png"));
					add(multiplayer_heart.get(multiplayer_lives));
					multiplayer_heart.get(multiplayer_lives).setLocation((multiplayer_lives + 2) * multiplayer_heart.get(multiplayer_lives).getWidth(), whitespace.getHeight()/2);
					multiplayer_lives++;
					powerup_label.setLabel("Extra Life Earned!");
					powerup_label.setLocation(width/2 - powerup_label.getWidth()/2, height - size/2);
				}else{
					heart.add(lives, new GImage("./img/heart.png"));
					add(heart.get(lives));
					heart.get(lives).setLocation((lives + 2) * heart.get(lives).getWidth(), whitespace.getHeight()/2);
					lives++;
					powerup_label.setLabel("Extra Life Earned!");
					powerup_label.setLocation(width/2 - powerup_label.getWidth()/2, height - size/2);
				}
			}else if(powerup == "coin"){
				if(multiplayer && multiplayer_snake.get(0).getX() == foodX && multiplayer_snake.get(0).getY() == foodY){
					multiplayer_score += 100;	
				}else{
					score += 100;
				}
				powerup_label.setLabel("100 Coins Collected!");
				powerup_label.setLocation(width/2 - powerup_label.getWidth()/2, height - size/2);
			}else if(powerup == "three times"){
				if(multiplayer && multiplayer_snake.get(0).getX() == foodX && multiplayer_snake.get(0).getY() == foodY){
					multiplayer_three_times = 3;
					powerup_label.setLabel("Triple Growth: Activated! (Player Two)");
				}else{
					three_times = 3;
					powerup_label.setLabel("Triple Growth: Activated!");
				}
				powerup_label.setLocation(width/2 - powerup_label.getWidth()/2, height - size/2);
			}else if(powerup == "accelerate"){
				time_delay = (int) 3 * time_delay / 4;
				powerup_label.setLabel("Accelerate: Activated!");
				powerup_label.setLocation(width/2 - powerup_label.getWidth()/2, height - size/2);
			}
		}else{
			food_value = 0;
			String old_powerup = powerup;
			while(old_powerup == powerup){
				int random = SnakeFunctions.randomInt(1, 100);
				if(random < 5){
					food.setImage(power_up_img.get(0));
					powerup = "extra life";
					powerup_label.setLabel("Power-Up: Extra Life!");
				}else if(random < 50){
					food.setImage(power_up_img.get(1));
					powerup = "coin";
					powerup_label.setLabel("Power-Up: 100 Coins!");
				}else if(random < 75){
					food.setImage(power_up_img.get(2));
					powerup = "three times";
					powerup_label.setLabel("Power-Up: Triple Growth!");
				}else if(random <= 100){
					food.setImage(power_up_img.get(3));
					powerup = "accelerate";
					powerup_label.setLabel("Power-Up: Accelerate!");
				}
			}
			powerup_label.setLocation(width/2 - powerup_label.getWidth()/2, height - size/2);
		}
	}
	
	
	/**
	 * foodValue: The food_value is regenerated 
	 */
	private void foodValue(){
		food_value = SnakeFunctions.randomInt(GROW_MIN, GROW_MAX);
		food.setImage(food_img.get(food_value - 1));
		food.sendToBack();
		if(style == "uh-oh"){
			uhOh();
		}
	}
	
	
	/**
	 * uhOh: Only for the uh-oh style...
	 * 
	 */
	private void uhOh(){
		remove(random_uh_oh);
		uh_oh_count = (uh_oh_count + 1) % 11;
		random_uh_oh.setImage(uh_oh_img.get((uh_oh_count)));
		random_uh_oh.setLocation(width/2 - random_uh_oh.getWidth()/2, height/2 - random_uh_oh.getHeight()/2);
		add(random_uh_oh);
		random_uh_oh.sendToBack();
	}
	
	/**
	 * updateScore: The score label is updated to the most current value of the score.
	 */
	public void updateScore(){
		score_label.setLabel("Score: " + Integer.toString(score));
		score_label.setLocation(width - (score_label.getWidth() + size), 3*whitespace.getHeight()/4);
	}
	
	
	/**
	 * updateMultiplayerScore: The score label is updated to the most current value of the score.
	 */
	public void updateMultiplayerScore(){
		multiplayer_score_label.setLabel("Score: " + Integer.toString(multiplayer_score));
		multiplayer_score_label.setLocation(size, height - size/2);
	}
	
	
	/**
	 * endGame: The screen is wiped and final scores are displayed.
	 */
	private void endGame(){
		setBackground(Color.WHITE);
		int increment = 3;
		for(int i = 0; i < width; i+=increment){
			GRect temp = new GRect(i, 0, increment, height);
			temp.setFilled(true);
			add(temp);
			pause(10);
		}
		pause(100);
		removeAll();
		
		if(multiplayer){
			GLabel winner;
			if(score > multiplayer_score){
				winner = new GLabel("Player One Wins!");
			}else if(multiplayer_score > score){
				winner = new GLabel("Player Two Wins!");
			}else{
				winner = new GLabel("TIE!");
			}
			winner.setFont("*-BOLD-48");
			winner.setLocation(width/2 - winner.getWidth()/2, height/2 - winner.getHeight()/2);
			add(winner);
			
			GLabel finalScore = new GLabel("Player 1 Score: " + score);
			finalScore.setFont("SERIF-*-24");
			finalScore.setLocation(width/2 - finalScore.getWidth()/2, 3*height/4);
			add(finalScore);
			
			GLabel multiFinalScore = new GLabel("Player 2 Score: " + multiplayer_score);
			multiFinalScore.setFont("SERIF-*-24");
			multiFinalScore.setLocation(width/2 - multiFinalScore.getWidth()/2, 3*height/4 + 2*finalScore.getAscent());
			add(multiFinalScore);
		}else{
			GLabel tempLbl = new GLabel("GAME OVER");
			tempLbl.setFont("SERIF-BOLD-48");
			tempLbl.setLocation(width/2 - tempLbl.getWidth()/2, height/2);
			add(tempLbl);
			GLabel finalScore = new GLabel("Final Score: " + score);
			finalScore.setFont("SERIF-*-24");
			finalScore.setLocation(width/2 - finalScore.getWidth()/2, 3*height/4);
			add(finalScore);
		}
		pause(500);
		GLabel click = new GLabel("(Click to Continue)");
		click.setLocation(width/2 - click.getWidth()/2, height - click.getAscent());
		add(click);
		waitForClick();
		removeAll();
		continuePlay = true;
	}
	
	
	/**
	 * optionsScreen:
	 */
	private void optionsScreen(){
		//switch to options img
		GImage title = new GImage("./img/options.png");
		title.setLocation(width/2 - title.getWidth()/2, 0);
		add(title);
		
		GLabel style_title = new GLabel("Style");
		style_title.setFont("*-BOLD-32");
		style_title.setLocation(width/8 - style_title.getWidth()/2, 7*height/16);
		add(style_title);
		
		normal_label.setFont("*-BOLD-16");
		normal_label.setColor(Color.BLACK);
		normal_label.setLocation(width/8, 8*height/16);
		add(normal_label);
		
		inverted_label.setFont("*-BOLD-16");
		inverted_label.setColor(Color.BLACK);
		inverted_label.setLocation(width/8, 9*height/16);
		add(inverted_label);
		
		color_label.setFont("*-BOLD-16");
		color_label.setColor(Color.BLACK);
		color_label.setLocation(width/8, 10*height/16);
		add(color_label);
		
		epilepsy_label.setFont("*-BOLD-16");
		epilepsy_label.setColor(Color.BLACK);
		epilepsy_label.setLocation(width/8, 11*height/16);
		add(epilepsy_label);
		
		uh_oh_label.setFont("*-BOLD-16");
		uh_oh_label.setColor(Color.BLACK);
		uh_oh_label.setLocation(width/8, 3*height/4);
		add(uh_oh_label);
		
		GLabel size_label = new GLabel("Size");
		size_label.setFont("*-BOLD-32");
		size_label.setColor(Color.BLACK);
		size_label.setLocation(width/2 - size_label.getWidth() - 2*size, 7*height/16);
		add(size_label);
		
		small_label.setFont("*-BOLD-16");
		small_label.setColor(Color.BLACK);
		small_label.setLocation(width/2 - 2*size, 8*height/16);
		add(small_label);
		
		large_label.setFont("*-BOLD-16");
		large_label.setColor(Color.BLACK);
		large_label.setLocation(width/2 - 2*size, 9*height/16);
		add(large_label);
		
		GLabel player_mode = new GLabel("Player Mode");
		player_mode.setFont("*-BOLD-32");
		player_mode.setColor(Color.BLACK);
		player_mode.setLocation(3*width/4 - player_mode.getWidth()/2, 7*height/16);
		add(player_mode);
		
		single_player.setFont("*-BOLD-16");
		single_player.setColor(Color.BLACK);
		single_player.setLocation(3*width/4, 8*height/16);
		add(single_player);
		
		multi_player.setFont("*-BOLD-16");
		multi_player.setColor(Color.BLACK);
		multi_player.setLocation(3*width/4, 9*height/16);
		add(multi_player);
		
		return_button = new GImage("./img/button2.png");
		return_button.setLocation(width/2 - return_button.getWidth()/2, 3*height/4);
		add(return_button);
		
		return_button_highlight = new GImage("./img/button2-2.png");
		return_button_highlight.setLocation(return_button.getX(), return_button.getY());
		return_button_highlight.setVisible(false);
		add(return_button_highlight);		
		
		return_button.sendToBack();
		
		if(style == "normal"){
			normal_label.setColor(Color.RED);
		}else if(style == "inverted"){
			inverted_label.setColor(Color.RED);
		}else if(style == "color"){
			color_label.setColor(Color.RED);
		}else if(style == "epilepsy"){
			epilepsy_label.setColor(Color.RED);
		}else{
			uh_oh_label.setColor(Color.RED);
		}
		if(multiplayer){
			multi_player.setColor(Color.RED);
		}else{
			single_player.setColor(Color.RED);
		}
		if(size == 10){
			small_label.setColor(Color.RED);
		}else{
			large_label.setColor(Color.RED);
		}
		//The system waits here until a button is pressed, and pause_boolean is set to false
		while(pause_boolean){
			pause(100);
		}
		//Pause is reset for the next pause point
		pause_boolean = true;
		
		options_boolean = false;
	}
	
	
	/**
	 * helpScreen:
	 */
	private void helpScreen(){
		
		GImage title = new GImage("./img/help.png");
		title.setLocation(width/2 - title.getWidth()/2, 0);
		add(title);
		
		GLabel basics = new GLabel("Basics");
		basics.setFont("*-BOLD-32");
		basics.setColor(Color.GREEN);
		basics.setLocation(width/8 - basics.getWidth()/2, height/3);
		add(basics);
		
		add(new GLabel("Eat food pellets to grow, avoid the walls and yourself.  Choose from a selection of options to suit your needs!", width/8, height/3 + basics.getAscent()/2));
		add(new GLabel("For more info, visit:    http://en.wikipedia.org/wiki/Snake_%28video_game%29", width/8, height/3 + basics.getAscent() + 2));
		
		GLabel powerups = new GLabel("Power-Ups");
		powerups.setFont("*-BOLD-32");
		powerups.setColor(Color.YELLOW);
		powerups.setLocation(width/8 - basics.getWidth()/2, height/3 + 2*basics.getAscent());
		add(powerups);
		
		GImage coin = new GImage("./img/coin.png");
		coin.setLocation(width/8, height/3 + 9*basics.getAscent()/4);
		add(coin);
		add(new GLabel("The coin adds 100 points to your score.  There is a 46% chance that it is chosen as a power-up."), width/8 + 30, height/3 + 11*basics.getAscent()/4);
		
		add(new GImage("./img/three_times.png"), width/8, coin.getY() + 25);
		add(new GLabel("The triple growth triples the amount that the snake grows by per food dot.  There is a 25% chance that it is chosen."), width/8 + 30, coin.getY() + 39);
		
		add(new GImage("./img/accelerate.png"), width/8, coin.getY() + 46);
		add(new GLabel("The accelerate power-up quickens gameplay until the next power-" +
				"up is acquired.  There is a 25% chance that it is chosen."), width/8 + 30, coin.getY() + 61);
		
		add(new GImage("./img/extra_life.png"), width/8, coin.getY() + 67);
		add(new GLabel("The extra life power-up gives the snake an additional life.  There is a 4% chance that it is chosen."), width/8 + 30, coin.getY() + 82);
		
		GLabel food = new GLabel("Food");
		food.setFont("*-BOLD-32");
		food.setColor(Color.GREEN);
		food.setLocation(width/8 - basics.getWidth()/2, height/3 + 23*basics.getAscent()/4);
		add(food);
		
		add(new GImage("./img/apple_icon.png"), width/8, coin.getY() + 120);
		add(new GLabel("The snake grows by one segment when it eats the apple."), width/8 + 30, coin.getY() + 135);
		
		add(new GImage("./img/banana_icon.png"), width/8, coin.getY() + 141);
		add(new GLabel("The snake grows by two segments when it eats the banana."), width/8 + 30, coin.getY() + 156);
		
		add(new GImage("./img/strawberry_icon.png"), width/8, coin.getY() + 162);
		add(new GLabel("The snake grows by three segments when it eats the strawberry."), width/8 + 30, coin.getY() + 177);
		
		return_button = new GImage("./img/button2.png");
		return_button.setLocation(width/2 - return_button.getWidth()/2, 3*height/4 + 25);
		add(return_button);
		
		return_button_highlight = new GImage("./img/button2-2.png");
		return_button_highlight.setLocation(return_button.getX(), return_button.getY());
		return_button_highlight.setVisible(false);
		add(return_button_highlight);
		
		return_button.sendToBack();
		
		//The system waits here until a button is pressed, and pause_boolean is set to false
		while(pause_boolean){
			pause(100);
		}
		//Pause is reset for the next pause point
		pause_boolean = true;
		
		help_boolean = false;
	}
	
	
	/**
	 * creditScreen:
	 */
	private void creditScreen(){
		
		GLabel credit = new GLabel("CREDIT!");
		credit.setFont("*-BOLD-144");
		credit.setLocation(width/2 - credit.getWidth()/2, credit.getAscent());
		add(credit);
		
		GLabel by = new GLabel("by");
		by.setLocation(width/2 - by.getWidth(), height/2 - by.getAscent()/2);
		add(by);
		
		GLabel also = new GLabel("...and a special thanks to David Lindenbaum for the Images!");
		also.setLocation(width/2 - also.getWidth()/2, height);
		add(also);
		
		return_button = new GImage("./img/button2.png");
		return_button.setLocation(width/2 - return_button.getWidth()/2, 3*height/4 + 25);
		add(return_button);
		
		return_button_highlight = new GImage("./img/button2-2.png");
		return_button_highlight.setLocation(return_button.getX(), return_button.getY());
		return_button_highlight.setVisible(false);
		add(return_button_highlight);
		
		return_button.sendToBack();
		
		int N_STEPS = 100;
		int PAUSE_ANIMATION = 5;
		GLabel boss_sauce = new GLabel("SCOTT BERTICS", 0, 5*getHeight()/8);
		boss_sauce.setFont("*-BOLD-72");
		add(boss_sauce);
		double increment = (getWidth()-boss_sauce.getWidth())/N_STEPS;
		while(pause_boolean){
			for(int i = 0; i<N_STEPS; i++){
				boss_sauce.move(increment, 0);
				pause(PAUSE_ANIMATION);
			}
			boss_sauce.setColor(SnakeFunctions.randomColor(1, 10));
			for(int i = 0; i<N_STEPS; i++){
				boss_sauce.move(-increment, 0);
				pause(PAUSE_ANIMATION);
			}
			boss_sauce.setColor(SnakeFunctions.randomColor(1, 10));
		}
		
		//Pause is reset for the next pause point
		pause_boolean = true;
		
		credit_boolean = false;
	}
	
	
	/**
	 * keyPressed: the key listener implementation
	 */
	public void keyPressed(KeyEvent e) {
		//only allows the snake to turn once per cycle of the main loop
		if(!turn){
			if(e.getKeyCode()==KeyEvent.VK_UP && incrementY==0){
				incrementY = -size;
				incrementX = 0;
				turn = true;
			}else if(e.getKeyCode()==KeyEvent.VK_DOWN && incrementY==0){
				incrementY = size;
				incrementX = 0;
				turn = true;
			}else if(e.getKeyCode()==KeyEvent.VK_LEFT && incrementX==0){
				incrementY = 0;
				incrementX = -size;
				turn = true;
			}else if(e.getKeyCode()==KeyEvent.VK_RIGHT && incrementX==0){
				incrementY = 0;
				incrementX = size;
				turn = true;
			}else if(e.getKeyCode()==KeyEvent.VK_SPACE){
				if(!wait_boolean){
					wait_boolean = true;
					pause_label = new GLabel("Press Space to Continue");
					pause_label.setFont("*-BOLD-40");
					pause_label.setLocation(width/2 - pause_label.getWidth()/2, height/2 - pause_label.getAscent()/2);
					add(pause_label);
				}else{
					wait_boolean = false;
					remove(pause_label);
				}
			}
		}
		if(multiplayer){
			if(!multiplayer_turn){
				if(e.getKeyCode()==KeyEvent.VK_W && multiplayer_incrementY==0){
					multiplayer_incrementY = -size;
					multiplayer_incrementX = 0;
					multiplayer_turn = true;
				}else if(e.getKeyCode()==KeyEvent.VK_S && multiplayer_incrementY==0){
					multiplayer_incrementY = size;
					multiplayer_incrementX = 0;
					multiplayer_turn = true;
				}else if(e.getKeyCode()==KeyEvent.VK_A && multiplayer_incrementX==0){
					multiplayer_incrementY = 0;
					multiplayer_incrementX = -size;
					multiplayer_turn = true;
				}else if(e.getKeyCode()==KeyEvent.VK_D && multiplayer_incrementX==0){
					multiplayer_incrementY = 0;
					multiplayer_incrementX = size;
					multiplayer_turn = true;
				}
			}
		}
	}
	
	
	/**
	 * mouseMoved: If the mouse moves over the buttons, a shadow appears.
	 */
	public void mouseMoved(MouseEvent e){
		if(getElementAt(e.getX(), e.getY()) != null){
			if(getElementAt(e.getX(), e.getY()) == play_highlight){
				play_highlight.setVisible(true);
			}else if(getElementAt(e.getX(), e.getY()) == options_highlight){
				options_highlight.setVisible(true);
			}else if(getElementAt(e.getX(), e.getY()) == help_highlight){
				help_highlight.setVisible(true);
			}else if(getElementAt(e.getX(), e.getY()) == exit_highlight){
				exit_highlight.setVisible(true);
			}else if(getElementAt(e.getX(), e.getY()) == credit){
				credit.setColor(Color.GRAY);
			}else if(getElementAt(e.getX(), e.getY()) == return_button_highlight){
				return_button_highlight.setVisible(true);
			}else if(getElementAt(e.getX(), e.getY()) == normal_label){
				normal_label.setColor(Color.GRAY);
			}else if(getElementAt(e.getX(), e.getY()) == inverted_label){
				inverted_label.setColor(Color.GRAY);
			}else if(getElementAt(e.getX(), e.getY()) == color_label){
				color_label.setColor(Color.GRAY);
			}else if(getElementAt(e.getX(), e.getY()) == epilepsy_label){
				epilepsy_label.setColor(Color.GRAY);
			}else if(getElementAt(e.getX(), e.getY()) == uh_oh_label){
				uh_oh_label.setColor(Color.GRAY);
			}else if(getElementAt(e.getX(), e.getY()) == small_label){
				small_label.setColor(Color.GRAY);
			}else if(getElementAt(e.getX(), e.getY()) == large_label){
				large_label.setColor(Color.GRAY);
			}else if(getElementAt(e.getX(), e.getY()) == single_player){
				single_player.setColor(Color.GRAY);
			}else if(getElementAt(e.getX(), e.getY()) == multi_player){
				multi_player.setColor(Color.GRAY);
			}
		}else{
			play_highlight.setVisible(false);
			options_highlight.setVisible(false);
			help_highlight.setVisible(false);
			exit_highlight.setVisible(false);
			return_button_highlight.setVisible(false);
			credit.setColor(Color.BLACK);
			normal_label.setColor(Color.BLACK);
			inverted_label.setColor(Color.BLACK);
			color_label.setColor(Color.BLACK);
			epilepsy_label.setColor(Color.BLACK);
			uh_oh_label.setColor(Color.BLACK);
			small_label.setColor(Color.BLACK);
			large_label.setColor(Color.BLACK);
			single_player.setColor(Color.BLACK);
			multi_player.setColor(Color.BLACK);
			if(multiplayer){
				multi_player.setColor(Color.RED);
			}else{
				single_player.setColor(Color.RED);
			}
			if(style == "normal"){
				normal_label.setColor(Color.RED);
			}else if(style == "inverted"){
				inverted_label.setColor(Color.RED);
			}else if(style == "colors"){
				color_label.setColor(Color.RED);
			}else if(style == "epilepsy"){
				epilepsy_label.setColor(Color.RED);
			}else{
				uh_oh_label.setColor(Color.RED);
			}
			if(size == 10){
				small_label.setColor(Color.RED);
			}else{
				large_label.setColor(Color.RED);
			}
		}
	}
	
	
	/**
	 * mouseClicked: Selects a menu when the user clicks the mouse on the home screen.
	 */
	public void mouseClicked(MouseEvent e){
		if(getElementAt(e.getX(), e.getY()) != null){
			if(getElementAt(e.getX(), e.getY()) == play_highlight){
				removeAll();
				play_boolean = true;
				pause_boolean = false;
			}else if(getElementAt(e.getX(), e.getY()) == options_highlight){
				removeAll();
				options_boolean = true;
				pause_boolean = false;
			}else if(getElementAt(e.getX(), e.getY()) == help_highlight){
				removeAll();
				help_boolean = true;
				pause_boolean = false;
			}else if(getElementAt(e.getX(), e.getY()) == exit_highlight){
				removeAll();
				pause_boolean = false;
				continuePlay = false;
			}else if(getElementAt(e.getX(), e.getY()) == credit){
				removeAll();
				credit_boolean = true;
				pause_boolean = false;
			}else if(getElementAt(e.getX(), e.getY()) == return_button_highlight){
				removeAll();
				pause_boolean = false;
			}else if(getElementAt(e.getX(), e.getY()) == normal_label){
				style = "normal";
			}else if(getElementAt(e.getX(), e.getY()) == inverted_label){
				style = "inverted";
			}else if(getElementAt(e.getX(), e.getY()) == color_label){
				style = "colors";
			}else if(getElementAt(e.getX(), e.getY()) == epilepsy_label){
				style = "epilepsy";
			}else if(getElementAt(e.getX(), e.getY()) == uh_oh_label){
				style = "uh-oh";
			}else if(getElementAt(e.getX(), e.getY()) == single_player){
				multiplayer = false;
			}else if(getElementAt(e.getX(), e.getY()) == multi_player){
				multiplayer = true;
			}else if(getElementAt(e.getX(), e.getY()) == small_label){
				size = 10;
			}else if(getElementAt(e.getX(), e.getY()) == large_label){
				size = 15;
			}
		}
	}
}
