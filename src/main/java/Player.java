//Author: Daniel McCarthy and Ethan Hammond
public class Player {
	private int playerId;
	private String name;
	private int draftableTroops;
	private boolean eliminated;
	private int[] ownedTerritories;
	private Hand hand;
	
	public Player(int playerId, String name) {
		this.playerId=playerId;
		this.name = name;
		this.draftableTroops=0;
		this.hand = new Hand();

		if(playerId <= 2){

			ownedTerritories = new int[GameData.INIT_COUNTRIES_PLAYER];

		} else {

			ownedTerritories = new int[GameData.INIT_COUNTRIES_NEUTRAL];

		}

	}
	
	public int getPlayerId() {
		return this.playerId;
	}
	
	public String getName() { 
		return this.name;
	}

	public int getDraftableTroops() {
		return this.draftableTroops;
	}

	public int getOwnedTerritories(int territory) {

		return this.ownedTerritories[territory];

	}

	public Hand getHand(){

		return this.hand;

	}

	public void addTerritories(int index, int newTerritory) {

		this.ownedTerritories[index] = this.ownedTerritories[index] + newTerritory;

	}
	
	public void addDraftableTroops(int draftableTroops) {
		this.draftableTroops = this.draftableTroops + draftableTroops;
	}
	
	public void draft(int troops) {
		draftableTroops -= troops;
	}
	
	public boolean isEliminated() {
		return eliminated;
	}
	
	public void eliminate() {
		eliminated = true;
	}
}
