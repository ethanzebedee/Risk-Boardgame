
//This is a class for managing turns
//Class written by Cathal � Meall�in � Faol�in, but methods written by Cathal and Ethan

public class Turn {
	public static void turn(Player[] players, Board board, UI ui, Deck deck){

		boolean boardConquered = false;
		int turnCounter = 0;

		while(boardConquered == false){
			Player currentPlayer = players[turnCounter % 2];
			
			//Check if the current player has been eliminated
			if(currentPlayer.isEliminated()) {
				//If they have, give this turn to the other player
				currentPlayer = players[(turnCounter + 1) % 2];
			}
			
			ui.displayString("It is "+ currentPlayer.getName() + "'s turn");
			Reinforcements.reinforcementCalc(currentPlayer, board, ui);
			
			if(currentPlayer.getHand().getCards().size() >= 3){

				cardPhase(currentPlayer, deck, board, ui);

			} else {

				ui.displayString(currentPlayer.getName() + " cannot trade in cards as they have less than 3 in their hand");

			}

			reinforcePhase(currentPlayer, board, ui);
			attackPhase(currentPlayer, board, ui, players);

			if(Attacking.getDrawCondition()){

				currentPlayer.getHand().add(deck.draw());
				ui.displayString(currentPlayer.getName() + " has drawn a card for conquering territories");
			}

			boolean validAnswer = false;
			String answer;

			while(validAnswer == false){

				ui.displayString("Would you like to fortify?(Y/N/Skip)");
				answer = ui.getCommand();

				if(answer.toLowerCase().equals("y") || answer.toLowerCase().equals("yes")){

					validAnswer = true;
					redeploymentPhase(currentPlayer, board, ui);

				} else if(!(answer.toLowerCase().equals("y") ||answer.toLowerCase().equals("yes") || answer.toLowerCase().equals("n") || answer.toLowerCase().equals("no") || answer.toLowerCase().equals("skip"))){

					ui.displayString("Invalid answer, would you like to fortify?(Y/N/Skip)");

				} else {

					ui.displayString("Player " + currentPlayer.getPlayerId() + " has chosen to skip the fortify phase");
					validAnswer = true;

				}
			}
			
			//Check how many players are left
			if(ui.playersLeft(players) == 1) {
				//If there is only one player left (the current player), then they have won!
				
				//Set boardConquered to true and end the loop
				boardConquered = true;
				ui.displayString(currentPlayer.getName() + " has won after " + turnCounter + " turns!");
			}
			
			turnCounter++;
			deck.shuffle();
		}

	}
	
	
	//This method handles the Reinforcement phase
	public static void reinforcePhase(Player player, Board board, UI ui){

		int reinforce = 0;

		ui.displayString("\nYou have " + player.getDraftableTroops() + " reinforcements.");

		ui.displayString("How many troops would you like to place?");
		reinforce = CommandPanel.askNum(ui);
		ui.displayString("> " + reinforce);

		while(reinforce > player.getDraftableTroops() || reinforce < 1){

			ui.displayString("You have inputted an invalid number of troops, please try again");
			reinforce = CommandPanel.askNum(ui);
			ui.displayString("> " + reinforce);

		}

		Reinforcements.place(reinforce, player, ui, board);
		ui.displayMap();

		if(player.getDraftableTroops() > 0){

			reinforcePhase(player, board, ui);

		}

	}
	

	//This method handles the attack phase
	public static void attackPhase(Player player, Board board, UI ui, Player[] players){
		ui.displayString("Do you wish to attack? (Enter Yes or Skip)");
		String wish=ui.getCommand().toLowerCase();
		ui.displayString("> " + wish);
		
		String skip="skip";
		//If skip was entered, end the attack phase
		if(skip.contains(wish)){
			return ;
		}
		else {
			int attacker=-1;
			int defender=-1;
			
			boolean adjacent=false;
			
			//Get the territory that they want to attack with and make sure its theirs
			ui.displayString("Which territory do you want to attack with?");
			attacker=Territory.checkTerritory(player, board, ui);
			
			
			//Get which territory they want to attack
			ui.displayString("Which territory do you want to attack?");
			defender=Territory.checkTerritoryAttacked(player, board, ui);
			
			//Get the playerId of the owner of the defending territory
			int defenderOwner = board.getOccupier(defender);
			
			//Check that the territories are adjacent
			adjacent= Board.isAdjacent(attacker, defender);
			
				if(adjacent==false  || board.getNumUnits(attacker) < 2) {
				ui.displayString("The territories you entered aren't next to one another or the territory attacking has less then 2 troops on it, please try again");
				}
				else {
			
					//Attack
					Attacking.rollingMechanics(attacker, defender, player, board, ui);
				}
			
			//Check if the owner of the defending country has any territories left
			if(ui.eliminationCheck(players[defenderOwner], board)) {
				//If the owner owns no more territories, they are eliminated
				players[defenderOwner].eliminate();
				ui.displayString(players[defenderOwner].getName() + " has been eliminated!");
			}
				
			//Update the map
			ui.displayMap();
			//Let them attack again if they want
			attackPhase(player, board, ui, players);
		}
	}
	
	//This method handles the fortifying phase
	public static void redeploymentPhase(Player player, Board board, UI ui){

		int fromTerritory;
		int toTerritory;
		int movers;

		//Make sure that the number of troops in the territory is greater than 1
		ui.displayString("Name a territory where you would like to move troops from.");
		fromTerritory = Territory.checkTerritory(player, board, ui);

		//If its not, enter in a new territory
		while(!(board.getNumUnits(fromTerritory)>= 2)) {
			ui.displayString("The territory must have 2 or more troops on it, please pick another");
			fromTerritory = Territory.checkTerritory(player, board, ui);
		}
		
		ui.displayString("how many troops would you like to move?");
		movers = CommandPanel.askNum(ui);
		ui.displayString("> " + movers);

		while(!(1 <= movers && movers < board.getNumUnits(fromTerritory))){

			ui.displayString("You have inputted an invalid number of troops, please try again");
			movers = CommandPanel.askNum(ui);
			ui.displayString("> " + movers);
		}

		ui.displayString("Name a territory where you would like to move troops to.");
		toTerritory = Territory.checkTerritory(player, board, ui);
		
		while(!Territory.areConnected(fromTerritory, toTerritory, player, board, ui))
		{
			ui.displayString("The territories you have selected are not connected, name a new territory where you would like to move troops to");
			toTerritory = Territory.checkTerritory(player, board, ui);
		}

		Territory.takeTroops(movers, fromTerritory, toTerritory, player, board, ui);
		ui.displayMap();
	}

	public static void cardPhase(Player currentPlayer, Deck deck, Board board, UI ui){

		boolean validAnswer = false;
		String answer;
		
		if(currentPlayer.getHand().getCards().size() > 5){

			ui.displayString(currentPlayer.getName() + "'s hand exceeds 5 cards, they must trade in 3 cards or they will lose cards");

		}

		currentPlayer.getHand().printHand(currentPlayer, ui);

			while(validAnswer == false){

				ui.displayString("Would you like to trade in cards?(Y/N/Skip)");
				answer = ui.getCommand();

				if(answer.toLowerCase().equals("y") || answer.toLowerCase().equals("yes")){

				
					int index1, index2, index3;
					validAnswer = true;
					ui.displayString("Please type in the index of the first card you would like to trade in");
					index1 =  CommandPanel.askNum(ui);

					ui.displayString("Please type in the index of the second card you would like to trade in");
					index2 =  CommandPanel.askNum(ui);

					ui.displayString("Please type in the index of the third card you would like to trade in");
					index3 =  CommandPanel.askNum(ui);

					if(currentPlayer.getHand().canTurnInCards(index1, index2, index3)){
						int troops=0;
						//Get the cardBonus
						troops=currentPlayer.getHand().cardBonus(index1, index2, index3);
						
						//Add two extra troops if the player controls a territory
						if(currentPlayer.getHand().checkControlsTerritory(index1, index2, index3, currentPlayer, board)) {
							troops+=2;
						}
						
						//Add the troops to the players draftable troops
						currentPlayer.addDraftableTroops(troops);
						ui.displayString(currentPlayer.getName() + "has got" + troops + " troops from trading in cards");
						
						//Remove the cards 
						currentPlayer.getHand().remove(deck, index3);
						currentPlayer.getHand().remove(deck, index2);
						currentPlayer.getHand().remove(deck, index1);
						validAnswer = true;

					} else {

						ui.displayString("These cards are not able to be traded in");
							
					}
					

				} else if(!(answer.toLowerCase().equals("y") ||answer.toLowerCase().equals("yes") || answer.toLowerCase().equals("n") || answer.toLowerCase().equals("no") || answer.toLowerCase().equals("skip"))){

					ui.displayString("Invalid answer, would you like to trade in cards?(Y/N/Skip)");

				} else {

					ui.displayString("Player " + currentPlayer.getPlayerId() + " has chosen not to trade in any cards");
					validAnswer = true;

				}
			}

		currentPlayer.getHand().exceedHandSize();

	}
	
	
}
