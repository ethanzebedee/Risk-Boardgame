import java.util.ArrayList;

public class Hand {

	private boolean condition;
	
	private ArrayList<Card> hand;

    //Instantiates player's hand
	public Hand() {
	
		hand = new ArrayList<Card>();
	}

    //returns the hand
	public ArrayList<Card> getCards() {
		return hand;
	}

	//Adds cards to player's deck
	public void add(Card card) {
	
		hand.add(card);
	}

    public void remove(Deck deckOfCards, int index){

        deckOfCards.add(hand.get(index));
        hand.remove(index);

    }
	

	//Checks if player can turn in cards
	public boolean canTurnInCards(int index1, int index2, int index3) {
	
        condition = false;

        if(!(hand.get(index1).getType().equals("Joker") || hand.get(index2).getType().equals("Joker") || hand.get(index3).getType().equals("Joker"))){
            
            if (hand.get(index1).getType().equals(hand.get(index2).getType()) && hand.get(index1).getType().equals(hand.get(index3).getType())) {
            //If all three cards have the same type
                condition = true;
                    
            } else if (!hand.get(index1).getType().equals(hand.get(index2).getType()) && !hand.get(index1).getType().equals(hand.get(index3).getType()) && !hand.get(index2).getType().equals(hand.get(index3).getType())) {
            //If all three cards have different types
                condition = true;
            }

        } else {

        //if any card is a joker, then all combinations are valid
            condition = true;

        }

        return condition;
	}

    public int cardBonus(int index1, int index2, int index3){

        int infBonus = 4;
        int cavBonus = 6;
        int artBonus = 8;
        int allBonus = 10;
        
        
        if(!(hand.get(index1).getType().equals("Joker") || hand.get(index2).getType().equals("Joker") || hand.get(index3).getType().equals("Joker"))){

            if (hand.get(index1).getType().equals(hand.get(index2).getType()) && hand.get(index1).getType().equals(hand.get(index3).getType())) {
            
                if(hand.get(index1).getType().equals("Infantry")){

                    return infBonus;
        
                }
        
                if(hand.get(index1).getType().equals("Cavalry")){
        
                    return cavBonus;
                    
                }
        
                if(hand.get(index1).getType().equals("Artillery")){
        
                    return artBonus;
                    
                }
                        
            } else if (!hand.get(index1).getType().equals(hand.get(index2).getType()) && !hand.get(index1).getType().equals(hand.get(index3).getType()) && !hand.get(index2).getType().equals(hand.get(index3).getType())) {
            
                return allBonus;

            }

        } else {

            if(hand.get(index1).getType().equals("Joker")){

                if(hand.get(index2).getType().equals(hand.get(index3).getType())){

                    if(hand.get(index2).getType().equals("Infantry")){

                        return infBonus;
            
                    }
            
                    if(hand.get(index2).getType().equals("Cavalry")){
            
                        return cavBonus;
                        
                    }
            
                    if(hand.get(index2).getType().equals("Artillery")){
            
                        return artBonus;
                        
                    }

                } else {

                    return allBonus;

                }

            }

            if(hand.get(index2).getType().equals("Joker")){

                if(hand.get(index1).getType().equals(hand.get(index3).getType())){

                    if(hand.get(index1).getType().equals("Infantry")){

                        return infBonus;
            
                    }
            
                    if(hand.get(index1).getType().equals("Cavalry")){
            
                        return cavBonus;
                        
                    }
            
                    if(hand.get(index1).getType().equals("Artillery")){
            
                        return artBonus;
                        
                    }

                } else {

                    return allBonus;

                }

            }

            if(hand.get(index3).getType().equals("Joker")){

                if(hand.get(index1).getType().equals(hand.get(index2).getType())){

                    if(hand.get(index1).getType().equals("Infantry")){

                        return infBonus;
            
                    }
            
                    if(hand.get(index1).getType().equals("Cavalry")){
            
                        return cavBonus;
                        
                    }
            
                    if(hand.get(index1).getType().equals("Artillery")){
            
                        return artBonus;
                        
                    }

                } else {

                    return allBonus;

                }

            }

        }

        return 0;

    }

	//Checks if player has too many cards
	public void exceedHandSize() {
		
		if (hand.size() >= 5) {

            while(hand.size() > 5){

                hand.remove(hand.size() - 1);

            }
			
		}
		
	}

    public void printHand(Player player, UI ui){

        ui.displayString(player.getName() + "'s hand contains:");

        for(int i = 0; i < hand.size(); i++){
            ui.displayString(hand.get(i).getName() + " " + hand.get(i).getType() + " index:" + i);
        }

    }

    //Checks if a player controls the territory on any out of 3 cards
    public boolean checkControlsTerritory(int index1, int index2, int index3, Player player, Board board){
    	//Put the indexes in an array so they can be easily moved between 
    	int arr[]= {index1, index2, index3};
    	
    	//Check each of the cards 
    	for(int i=0;i<arr.length;i++) {
    		//Get the territory int
    		int territory=Territory.validTerritory(hand.get(arr[i]).getName());
    		
    		//Check if the player owns the territory
    		if(board.getOccupier(territory)==player.getPlayerId()) {
    			return true;
    		}
    	}
    	
       //If you get to here, the player owns none of the terrritories
    	return false;
    }
    
}
    

