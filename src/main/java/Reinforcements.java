//Author: Cathal � Meall�in � Faol�in and Ethan Hammond

import java.util.Random;

public class Reinforcements {
	
	//This sets up the armies for the beginning of the game
	public static void setUpArmies(Player[] players, UI ui, Board board) {

		int playerId;
		//give the players 36 new troops
		for(playerId=0;playerId<GameData.NUM_PLAYERS;playerId++) {
			players[playerId].addDraftableTroops(36);
			ui.displayString(players[playerId].getName()+" has been given 36 draftable troops");
		}
		
		//Give the neutrals 24 new troops
		for(;playerId<GameData.NUM_PLAYERS_PLUS_NEUTRALS; playerId++) {
			players[playerId].addDraftableTroops(24);
			ui.displayString(players[playerId].getName()+" has been given 24 draftable troops");
		}
		
		int playerRoll[]= new int[2];
		
		//Roll for both the players
		for(int i=0;i<GameData.NUM_PLAYERS;i++) {
			playerRoll[i]= (int)( Math.random()*6)+1;
			ui.displayString(players[i].getName()+" has rolled "+ playerRoll[i]);
		}
	
		int goesFirst;
		//Figure out who goes first
		if(playerRoll[0]>=playerRoll[1]) {
			goesFirst=0;
		}
		else {
			goesFirst=1;
		}
	
		//Figure out who goes second
		int goesSecond=(goesFirst+1)%2;
		
		ui.displayString("\n" + players[goesFirst].getName() + " goes first");
		
		boolean alreadyRandomised = false;

		while(players[goesSecond].getDraftableTroops()>0) {
			
			ui.displayString("\n" + players[goesFirst].getName() + " it is your turn to place reinforcements");
			//Place 3 per Player
			place( 3, players[goesFirst], ui, board);
		
			String reinforcementAnswer;

			if(alreadyRandomised == false) {

				boolean reinforcementvalidAnswer = false;

				while(reinforcementvalidAnswer == false){

					ui.displayString("Would you like to randomly allocate neutral troops?(Y/N)");
					reinforcementAnswer = ui.getCommand();

					if(reinforcementAnswer.toLowerCase().equals("y") || reinforcementAnswer.toLowerCase().equals("yes")){

						ui.displayString("Player " + players[goesFirst].getName() + " has chosen to skip neutral allocation");
						for(int i=0;i<GameData.NUM_NEUTRALS;i++) {
							
						}
						randomNeutralallocation(players, board, ui);
						alreadyRandomised = true;
						reinforcementvalidAnswer = true;

					} else if(!(reinforcementAnswer.toLowerCase().equals("y") ||reinforcementAnswer.toLowerCase().equals("yes") || reinforcementAnswer.toLowerCase().equals("n") || reinforcementAnswer.toLowerCase().equals("no"))){

						ui.displayString("Invalid answer, would you like to randomly allocate neutral troops?(Y/N)");

					} else {

						//Let player place 1 troop per neutral
						for(int i=0;i<GameData.NUM_NEUTRALS;i++) {
							ui.displayString(players[goesFirst].getName()+ " may now place troops in "+ players[i+GameData.NUM_PLAYERS].getName() + "'s territory ");
							place(1, players[i+GameData.NUM_PLAYERS] , ui, board);
						}
						reinforcementvalidAnswer = true;

					}
				}
			}

			//Change who's turn it is to go
			goesFirst=(goesFirst+1)%2;
		}
		
		ui.displayString("All your troops have been placed. Good luck!!!\n");
	}
	
	//This places troops on the board
	public static void place(int troop_num, Player player, UI ui, Board board) {
		//If there is no issues this loop runs only once
		for(int i=0;i<1;i++) {
			//Tells the player to place the reinforcements
			ui.displayString("Enter the territory name you want to place your reinforcements");
		
			//Check the territory to ensure that they own it and it is a valid territory
			int toReinforce= Territory.checkTerritory(player, board, ui);
			
			//Let the player place the troops
			board.addUnits(toReinforce, player.getPlayerId(), troop_num);
			//Reduce the number of units draftable by the number of units added
			player.draft(troop_num);
			
			ui.displayString(troop_num + " troops placed in "+ GameData.COUNTRY_NAMES[toReinforce] + "\n");
			//Update the map
			ui.displayMap();
		}
	}

	//This randomly places neutral troops on the board
	public static void randomNeutralallocation(Player[] players, Board board, UI ui) {

		Random rand = new Random();
		int randTroops;

		for(int i=0;i<GameData.NUM_NEUTRALS;i++) {
			
			ui.displayString("\n" + players[i + GameData.NUM_PLAYERS].getName() + " is placing territories" );
		
			for(int j = 0; j<GameData.INIT_COUNTRIES_NEUTRAL; j++){

				//randomly generate a number between 1 and 4
				randTroops = rand.nextInt(4) + 1;
				
				//if random troops is greater than the number of draftable troops or this is the last territory left, place all the remaining troops
				if(randTroops > players[i + GameData.NUM_PLAYERS].getDraftableTroops() || j == players[i + GameData.NUM_PLAYERS].getDraftableTroops() - 1){

					randTroops =  players[i + GameData.NUM_PLAYERS].getDraftableTroops();

				}

				//Place the troops in a territory occupied by the neutral
				board.addUnits(players[i + GameData.NUM_PLAYERS].getOwnedTerritories(j), players[i + GameData.NUM_PLAYERS].getPlayerId(), randTroops);
				
				//Reduce the number of units draftable by the number of units added
				players[i + GameData.NUM_PLAYERS].draft(randTroops);
				
				//Tell the player how many troops were placed
				ui.displayString(randTroops + " troops placed in "+ GameData.COUNTRY_NAMES[players[i + GameData.NUM_PLAYERS].getOwnedTerritories(j)]);
				//Update the map
				ui.displayMap();

			}

		}
	}
	
	public static void reinforcementCalc(Player player, Board board, UI ui){

		//Keeps the overall number of controlled countries
		int controlledCountries = 0;
		int troops;
		int countries_checked=0;
		
		
		//Runs through all the countries and continents
		for(int continent=0;continent<GameData.NUM_CONTINENTS;continent++)
		{
			//Keeps the number of countries controlled within a continent
			int controlled=0;
			int country=0;
			
			for(; country < GameData.CONTINENT_SIZES[continent]; country++){

				//Increment if a country is controlled
				if (board.getOccupier(country+countries_checked) == player.getPlayerId()){

						controlled++;

				}

			}
			
			//If an entire continent is controlled allocate the extra reinforcements
			if(controlled==GameData.CONTINENT_SIZES[continent]) {
				player.addDraftableTroops(GameData.CONTINENT_REINFORCEMENTS[continent]);
				ui.displayString(player.getName()+ " got an extra " + GameData.CONTINENT_REINFORCEMENTS[continent] + 
						" troops for controlling the continent of" + GameData.CONTINENT_NAMES[continent]);
			}
			
			//Update the overall number of controlled countries
			controlledCountries+=controlled;
			countries_checked+=country;
		}

		troops = (int)Math.floor(controlledCountries / 3);

		if(troops < 3){

			troops = 3;

		}

		player.addDraftableTroops(troops);

	}

}
	
	

