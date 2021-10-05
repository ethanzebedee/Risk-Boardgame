/**
 * 
 * Author: Ethan Hammond
 * 
 * Class to create card/holds all card info
 * 
 **/
public class Card {

  private String type;
  private String territory;
  private boolean joker;
  

  public Card(String type, String territory, boolean isJoker) {
		this.type = type;
		this.territory = territory;
    this.joker = isJoker;
  }
	
	public String getName() {
		return territory;
	}

  public String getType() {
		return type;
  }

  public boolean getJoker(){

    return joker;

  }

}
