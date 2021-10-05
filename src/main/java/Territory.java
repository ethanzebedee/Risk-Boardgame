
//This is a class for manipulating territories
//Class written by Cathal � Meall�in � Faol�in, but methods written by Cathal, Ethan and Daniel

public class Territory {
	
	//Initalise the Territories at the start of the game
	public static void initialiseTerritories(int playerId, Board board, Player[] players, Deck deckOfCards) {

		//Deck deckOfCards = new Deck();

		// add units
		int x = 0;
		for (playerId=0; playerId<GameData.NUM_PLAYERS; playerId++) {
			for (int i=0; i<GameData.INIT_COUNTRIES_PLAYER; i++) {
				board.addUnits(Territory.validTerritory(deckOfCards.get(x).getName()), playerId, 1);
				players[playerId].addTerritories(i, Territory.validTerritory(deckOfCards.get(x).getName()));
				x++;
			}
		}
		for (; playerId<GameData.NUM_PLAYERS_PLUS_NEUTRALS; playerId++) {
			for (int i=0; i<GameData.INIT_COUNTRIES_NEUTRAL; i++) {
				board.addUnits(Territory.validTerritory(deckOfCards.get(x).getName()), playerId, 1);
				players[playerId].addTerritories(i, Territory.validTerritory(deckOfCards.get(x).getName()));
				x++;
			}
		}	

		deckOfCards.addJoker();
		
		deckOfCards.shuffle();
	}
	
	//Check that a territory name is valid and return the corresponding territory number
	public static int validTerritory(String name) {
		boolean valid=false;
		int country_index=-1;
		for(int i=0;i<GameData.NUM_COUNTRIES;i++) {
		//If the String name is contained within only 1 country name, then it is a valid territory
			if(GameData.COUNTRY_NAMES[i].contains(name)) {
			//Changes valid to be true, or valid back to false if the name is ambiguous
			valid= !valid;
			country_index=i;
			//If it has been detected that the String is contained within more then one territory, break the loop 
			//and return false
			if(valid==false) {
					break;
				}
			}
		}

		if(valid==false) {
			return -1;
		}
		else {
			return country_index;
		}
	}
			

	//Checks that the territory belongs to the player
	public static int checkTerritory(Player player, Board board, UI ui){

		
		int territory;
		String name= ui.getCommand();
		ui.displayString("> " + name);
		
		territory = Territory.validTerritory(name);
				
		while(territory == -1 || board.getOccupier(territory) != player.getPlayerId()){
			ui.displayString("You have entered an invalid territory or a territory you do not own, please try again");
			name= ui.getCommand();
			ui.displayString("> " + name);
			
			territory = Territory.validTerritory(name);

		}
		return territory;

	}
	
	//Checks that the territory being attacked doesn't belong to the player
	public static int checkTerritoryAttacked(Player player, Board board, UI ui) {

		
		int territory;
		String name= ui.getCommand();
		ui.displayString("> " + name);
		
		territory = Territory.validTerritory(name);
				
		while(territory == -1 || board.getOccupier(territory) == player.getPlayerId()){
			ui.displayString("You have entered an invalid territory or a territory you already own, please try again");
			name= ui.getCommand();
			ui.displayString("> " + name);
			
			territory = Territory.validTerritory(name);

		}
		return territory;
	}
	
	
	//Takes troops from one territory and moves it to another 
	public static void takeTroops(int movers, int fromTerritory, int toTerritory, Player player, Board board, UI ui) {
		board.addUnits(fromTerritory, player.getPlayerId(), -movers);
		board.addUnits(toTerritory, player.getPlayerId(), movers);
		ui.displayString(movers + " troops moved from " + GameData.COUNTRY_NAMES[fromTerritory] + " to " + GameData.COUNTRY_NAMES[toTerritory]);
		ui.displayMap();
	}
		
	//Check that there is a link between two territories
	public static boolean areConnected(int starting, int destination, Player player, Board board, UI ui){
		boolean visited[]=new boolean [GameData.NUM_COUNTRIES];
		ArrayStack<Integer> stack=new ArrayStack<Integer>(GameData.NUM_COUNTRIES);
		stack.push(starting);
		
		//Call the helper depth-first algorithm to figure out if the territories are connected
		int result=dfs(starting, destination, stack, visited, player, board);
		
		if(result==1) {
			ui.displayString("The territories " + GameData.COUNTRY_NAMES[starting] + " and " + GameData.COUNTRY_NAMES[destination] + " are connected");
			return true;
		}
		
		ui.displayString("The territories " + GameData.COUNTRY_NAMES[starting] + " and " + GameData.COUNTRY_NAMES[destination] + " are not connected");
		return false;
	}
	
	
	//the depth-first algorithm as a helper function for areConnected
	public static int dfs(int at, int destination, ArrayStack<Integer> stack, boolean visited[], Player player, Board board) {
		if(stack.isEmpty()) {
			return 0;
		}
		//If the player doesn't own the territory or the territory has already been visited, skip it
		else if(board.getOccupier(at)!= player.getPlayerId() || visited[at]==true) {
			return  dfs(stack.pop(), destination, stack, visited, player, board);
		}
		
		visited[at]=true;
		
		//If the territory we're at is the same as b, return 1
		if(at==destination) {
			return 1;
		}
		
		//add the adjacent territories of at to the stack
		for(int i=0;i<GameData.ADJACENT[at].length; i++){
			stack.push(GameData.ADJACENT[at][i]);
		}
		
		//Continue recursively down the list
		return dfs(stack.pop(), destination, stack, visited, player, board);
	}

}
