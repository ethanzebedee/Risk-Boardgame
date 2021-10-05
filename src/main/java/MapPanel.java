import javax.imageio.ImageIO;
import javax.swing.*;

import java.awt.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;

//Author: Dr Pavel Gladyshev and Cathal � Meall�in � Faol�in
class MapPanel extends JPanel {
	
	private BufferedImage boardImage;
	private static final long serialVersionUID = 1L;
	private static final int FRAME_WIDTH = 1000;    // must be even
	private static final int MAP_WIDTH=940;
	private static final int FRAME_HEIGHT = 630;    //630
	private static final int COUNTRY_RADIUS = 12;   // must be even
	private static final int NAME_OFFSET_X = 3;
	private static final int NAME_OFFSET_Y = 13;
	private static final Color TEXT_COLOR = Color.BLACK;
	private static final int ADJACENT_LINE = 1;
	private static final Color ADJACENT_COLOR = Color.MAGENTA;
	private static final Color[] PLAYER_COLORS = {Color.RED,Color.BLUE,Color.YELLOW,Color.GREEN,Color.MAGENTA,Color.WHITE};
	public static final Color[] CONTINENT_COLORS = {Color.BLACK, Color.CYAN, Color.DARK_GRAY, Color.PINK, Color.ORANGE, Color.GRAY};
	private static final int PLAYER_RADIUS = 8;
	public static final int[][] COUNTRY_COORD = {
		{195,150},     // 0
		{255,161},
		{146,86},
		{130,144},
		{314,61},
		{205,235},
		{135,219},
		{160,299},
		{65,89},
		{370,199},
		{398,280},      // 10
		{465,270},
		{547,180},
		{460,200},
		{393,127},
		{463,122},
		{628,227},
		{679,332},
		{572,338},
		{861,213},
		{645,152},      // 20
		{763,70},
		{827,94},
		{751,360},
		{750,140},
		{695,108},
		{760,216},
		{735,277},
		{889,537},
		{850,429},
		{813,526},       // 30
		{771,454},
		{213,352},
		{221,426},
		{289,415},
		{233,523},
		{496,462},
		{440,393},
		{510,532},
		{499,354},
		{547,432},        // 40
		{586,545}
	};
	
	private Board board;	
	
	MapPanel (Board inBoard) {
		board = inBoard;
		setPreferredSize(new Dimension(FRAME_WIDTH, FRAME_HEIGHT));
		setBackground(Color.WHITE);
		//Throws an exception if it cannot find the image file
				try {
					//Get the risk map image and add it to the private variable board image
					this.boardImage = ImageIO.read(getClass().getResource("new-risk-board .png"));
					
				}catch(Exception ex){
					System.out.println("Could not find image file " + ex.toString());
				}
		return;
	}

    public void paintComponent(Graphics g) {
    	int xPos, yPos, xPosBegin, yPosBegin, xPosEnd, yPosEnd;
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        //Draw the image of the board to the map
        g2.drawImage(boardImage, 0, 0, MAP_WIDTH, FRAME_HEIGHT, this);
        
        // Display adjacent lines
        g2.setStroke(new BasicStroke(ADJACENT_LINE));
        g2.setColor(ADJACENT_COLOR);
        for (int i=0; i<GameData.NUM_COUNTRIES; i++) {
        	xPosBegin = COUNTRY_COORD[i][0];
        	yPosBegin = COUNTRY_COORD[i][1];
        	for (int j=0; j<GameData.ADJACENT[i].length; j++) {
            	xPosEnd = COUNTRY_COORD[GameData.ADJACENT[i][j]][0];
            	yPosEnd = COUNTRY_COORD[GameData.ADJACENT[i][j]][1];
            	if (xPosBegin < xPosEnd) {
	       			if (Math.abs(xPosEnd-xPosBegin)<FRAME_WIDTH/2) {
	        			g2.drawLine(xPosBegin, yPosBegin, xPosEnd, yPosEnd);
	        		} else {
	        			g2.drawLine(0,yPosBegin,xPosBegin,yPosBegin);
	        			g2.drawLine(FRAME_WIDTH-1,yPosEnd,xPosEnd,yPosEnd);
	        		}
            	}
        	}
        }
        // Display countries
        for (int i=0; i<GameData.NUM_COUNTRIES; i++) {
            g2.setColor(CONTINENT_COLORS[GameData.CONTINENTS[i]]);
            xPos = COUNTRY_COORD[i][0] - COUNTRY_RADIUS;
            yPos = COUNTRY_COORD[i][1] - COUNTRY_RADIUS;
			Ellipse2D.Double ellipse = new Ellipse2D.Double(xPos,yPos,2*COUNTRY_RADIUS,2*COUNTRY_RADIUS);
			g2.fill(ellipse);
            g2.setColor(TEXT_COLOR);
            xPos = COUNTRY_COORD[i][0] - GameData.COUNTRY_NAMES[i].length()*NAME_OFFSET_X;
            yPos = COUNTRY_COORD[i][1] - NAME_OFFSET_Y;
			g2.drawString(GameData.COUNTRY_NAMES[i],xPos,yPos);
        }
        // Display players units
        for (int i=0; i<GameData.NUM_COUNTRIES; i++) {
        	if (board.isOccupied(i)) {
                g2.setColor(PLAYER_COLORS[board.getOccupier(i)]);
                xPos = COUNTRY_COORD[i][0] - PLAYER_RADIUS;
                yPos = COUNTRY_COORD[i][1] - PLAYER_RADIUS;
    			Ellipse2D.Double ellipse = new Ellipse2D.Double(xPos,yPos,2*PLAYER_RADIUS,2*PLAYER_RADIUS);
    			g2.fill(ellipse);  
                g2.setColor(TEXT_COLOR);   			
                xPos = COUNTRY_COORD[i][0] - NAME_OFFSET_X;
                yPos = COUNTRY_COORD[i][1] + 2*PLAYER_RADIUS + NAME_OFFSET_Y;  
        		g2.drawString(String.valueOf(board.getNumUnits(i)),xPos,yPos);
        	}
        }
        
        //Draw the table of continents
        drawContinentTable(g2);		
		return;
    }
    
  //This draws a table of continents
  	private void drawContinentTable(Graphics2D g2) {
  		//Make a table of the continents in the corner
  		//Get the colours of the continents in filled rectangles
  				g2.setColor(Color.PINK);
  				g2.fillRect(100, 521, 68, 10);
  				
  				g2.setColor(Color.ORANGE);
  				g2.fillRect(100, 536, 68, 10);
  				
  				g2.setColor(Color.LIGHT_GRAY);
  				g2.fillRect(100, 551, 68, 10);
  				
  				g2.setColor(Color.BLACK);
  				g2.fillRect(30, 521, 68, 10);
  				
  				g2.setColor(Color.CYAN);
  				g2.fillRect(30, 536, 68, 10);
  				
  				g2.setColor(Color.DARK_GRAY);
  				g2.fillRect(30, 551, 68, 10);
  				
  				
  				
  				//Draw the continent names and how much it is worth
  				g2.setColor(Color.WHITE);
  				g2.drawString("N. America 5", 30, 530);
  				g2.setColor(Color.BLACK);
  				g2.drawString("Europe 5", 30, 545);
  				g2.drawString("Asia 7", 30, 560);
  				g2.drawString("Australia 2", 100, 530);
  				g2.drawString("S. America 2", 100, 545);
  				g2.drawString("Africa 3", 100, 560);
  	}

    public void refresh () {
		revalidate();
		repaint();
		return;
    }
    
}
