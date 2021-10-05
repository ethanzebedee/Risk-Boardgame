//Class written by Cathal � Meall�in � Faol�in and Ethan Hammond
public class Attacking {

	static boolean drawCondition = false;

		//This rolls and compares the dice rolls between the attacker and the defender
		public static void rollingMechanics(int attacker, int defender, Player player, Board board, UI ui) {
			int troops=0;
			int defendingTroops=0;
			
			//Get the number of troops to attack with
			ui.displayString("How many troops do you want to attack with?");
			troops = CommandPanel.askNum(ui);
			ui.displayString("> " + troops);
			
			while(!(board.getNumUnits(attacker) > troops && (troops>0 && troops<=3))) {
				ui.displayString("You have inputted an invalid number of troops, please try again");
				troops = CommandPanel.askNum(ui);
				ui.displayString("> " + troops);
			}
			
			//Get the number of defending troops
			if(board.getNumUnits(defender)>1) {
				defendingTroops=2;
			}
			else {
				defendingTroops=1;
			}
			
			//Roll and sort the dice
			int attackerRolls[]= insertionSort(dice(troops));
			int defenderRolls[]= insertionSort(dice(defendingTroops));
			
			//Compare the dice
			boolean taken=comparesDice(attackerRolls, defenderRolls, attacker, defender, board, ui);
			
			//If a new territory was taken
			if(taken) {
				wonTerritory(attacker, defender, player, board, ui);
				drawCondition = true;
			}
		}
		
		//Allows the player to move their attacking troops into the newly won territory
		private static void wonTerritory(int attacker, int defender, Player player, Board board, UI ui) {
			int troops;
			
			ui.displayString(player.getName() + " has won and taken the new territory of " + GameData.COUNTRY_NAMES[defender]);
			//Get the number of troops to move to the new territory
			ui.displayString("How many troops do you want to move to the new territory?");
			troops = CommandPanel.askNum(ui);
			ui.displayString("> " + troops);
			
			while(!((troops < board.getNumUnits(attacker)) && (troops>0))) {
				ui.displayString("You have inputted an invalid number of troops, please try again "
						+ "\n(must leave at least one in the territory you attacked from and at least one in the new territory)");
				troops = CommandPanel.askNum(ui);
				ui.displayString("> " + troops);
			}
			
			Territory.takeTroops(troops, attacker, defender, player, board, ui);

			
		}
		
		//This compares the results of the dice rolls and updates the board. returns true if the territory was taken
		private static boolean comparesDice(int attackerRolls[], int defenderRolls[], int attacker, int defender, Board board, UI ui){
			for(int i=0;i<defenderRolls.length;i++) {
				ui.displayString("Attacker rolled " + attackerRolls[i] +" and defender rolled " + defenderRolls[i] );
				
				//In case of a draw, the defender wins
				if(attackerRolls[i]>defenderRolls[i]) {
					board.addUnits(defender, board.getOccupier(defender), -1);
					ui.displayString("Attacker won, defender lost a troop" );
					ui.displayMap();
				}
				else {
					board.addUnits(attacker, board.getOccupier(attacker), -1);
					ui.displayString("Defender won, attacker lost a troop" );
					ui.displayMap();
				}
			}
			
			if(board.getNumUnits(defender)==0) {
				return true;
			}
			return false;
		}
		
		//This rolls the dice
		private static int[] dice(int numRolls) throws IndexOutOfBoundsException{
			if(numRolls<0) {
				throw new IndexOutOfBoundsException("The number of rolls has to be greater than 0");
			}
					
			int arr[]= new int [numRolls];
					
			//Roll the dice 
			for(int i=0;i<numRolls;i++) {
				arr[i]=(int)( Math.random()*6)+1;
						
			}
					
			return arr;
		}
				
		//This is used in sorting the dice rolls in order
		private static int[] insertionSort(int arr[]) {
			//Sort through the list one element at a time
			for(int i=0;i<arr.length;i++) {
				
				//Start as far as i has reached, counting back to 0
				for(int j=i;j>0;j--) {
					//swap if the element on the left is less then the one on the right
					if(arr[j]> arr[j-1]) {
						swap(arr, j, j-1);
					}
					//Otherwise they are in order and you can check the next element
					else {
						break;
					}
				}
			}
			return arr;
		}
		
		//A helper function for insertion sort
		private static void swap(int arr[], int i, int min_index) {
			int temp=arr[i];
			arr[i]=arr[min_index];
			arr[min_index]=temp;
			
		}	
		
		public static boolean getDrawCondition(){

			return drawCondition;

		}

}
