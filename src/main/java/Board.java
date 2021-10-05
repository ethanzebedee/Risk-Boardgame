
public class Board {
	
	private boolean[] occupied = new boolean [GameData.NUM_COUNTRIES];
	private int[] occupier = new int [GameData.NUM_COUNTRIES];
	private int[] numUnits = new int [GameData.NUM_COUNTRIES];
	
	Board() {
		for (int i=0; i<GameData.NUM_COUNTRIES; i++) {
			occupied[i] = false ;
			numUnits[i] = 0;
		}
		return;
	}
	
	public void addUnits (int country, int player, int addNumUnits) {	
		// prerequisite: country must be unoccupied or already occupied by this player
		if (!occupied[country]) {
			occupied[country] = true;
			occupier[country] = player;
		}
		numUnits[country] = numUnits[country] + addNumUnits;
		return;
	}
	
	public boolean isOccupied(int country) {
		if(getNumUnits(country)==0) {
			occupied[country]=false;
		}
		return occupied[country];
	}
	
	public int getOccupier (int country) {
		return occupier[country];
	}
	
	//Method checks if a player owns a territory
	public boolean checkOccupier (Player player, int countryId) {
		return (occupier[countryId] == player.getPlayerId());
	}
	
	public int getNumUnits (int country) {
		return numUnits[country];
	}

	public static boolean isAdjacent(int attacker, int defender) {
		for(int i=0; i< GameData.ADJACENT[attacker].length;i++)
		{
			//If the attacker is adjacent to the defender, return true
			if(GameData.ADJACENT[attacker][i]==defender) {
				return true;
			}
		}
		//If you get to here, return false
		return false;
	}
}
