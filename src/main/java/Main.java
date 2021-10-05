//import java.awt.Color;
import java.util.ArrayList;

/**
 * 
 * Authors: Cathal Mellon Whelan, Daniel McCarthy, Ethan Hammond 
 * 
 * Main
 * 
 **/

public class Main {

	public static String PlayerColourString(int playerId) {
		switch(playerId) {
		case 0: return "red";
		case 1: return "blue";
		case 2: return "yellow";
		case 3: return "green";
		case 4: return "magenta";
		case 5: return "white";
		default: return null;
		}
	}

	static Player[] players;
	static Board board = new Board();
	static UI ui = new UI(board);
	static Deck deckOfCards = new Deck();
	
	//The main for this class
	public static void main (String args[]) {	   
		int playerId;
		String name;
		players = new Player [GameData.NUM_PLAYERS_PLUS_NEUTRALS];

		
		// display blank board
		ui.displayMap();
		
		
		// get player names
		for (playerId=0; playerId<GameData.NUM_PLAYERS; playerId++) {
			ui.displayString("Enter the name of Player " + (playerId+1));
			name = ui.getCommand();
			ui.displayString("> " + name);
			ui.displayString(name + "'s colour is "+ PlayerColourString(playerId) +  "\n");
			players[playerId]= new Player(playerId, name);
		}
			
		//Set the neutral names
		for(; playerId<GameData.NUM_PLAYERS_PLUS_NEUTRALS; playerId++) {
			players[playerId]= new Player( playerId, PlayerColourString(playerId) + " Neutral");
			ui.displayString(players[playerId].getName() + "'s colour is "+ PlayerColourString(playerId) +  "\n");
		}

		Territory.initialiseTerritories(playerId, board, players, deckOfCards);

		
		// display map
		ui.displayMap();

		
		Reinforcements.setUpArmies(players, ui, board);

		Turn.turn(players, board, ui, deckOfCards);

		return;
	}




}
	
	


