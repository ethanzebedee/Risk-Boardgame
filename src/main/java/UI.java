import java.awt.BorderLayout;
import javax.swing.JFrame;

public class UI {

	private static final int FRAME_WIDTH = 1000;
	private static final int FRAME_HEIGHT = 800;
	
	private JFrame frame = new JFrame();
	private MapPanel mapPanel;	
	private InfoPanel infoPanel = new InfoPanel();
	private CommandPanel commandPanel = new CommandPanel();
	
	UI (Board board) {
		mapPanel = new MapPanel(board);
		frame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
		frame.setTitle("Risk");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(mapPanel, BorderLayout.NORTH);
		frame.add(infoPanel, BorderLayout.CENTER);
		frame.add(commandPanel,BorderLayout.SOUTH);
		frame.setResizable(true);
		frame.setVisible(true);
		return;
	}
	
	//method checks if a player has no territories left and should be eliminated
	public boolean eliminationCheck(Player player, Board board) {
		int i;
		
		for(i = 0; i < GameData.NUM_COUNTRIES; i++) {
			if(board.checkOccupier(player, i)) return false;
		}
		
		return true;
	}
	
	//method counts how many players have not been eliminated
	public int playersLeft(Player[] players) {
		int count = 0;
		
		for(int i = 0; i < players.length; i++) {
			if(!players[i].isEliminated()) count++; 
		}
		
		if(count == 0) {
			displayString("Error: Somehow, all players have been destroyed. There is no winners");
		}
		
		return count;
	}
	
	public String getCommand () {
		return commandPanel.getCommand();
	}

	public void displayMap () {
		mapPanel.refresh();
		return;
	}
	
	public void displayString (String string) {
		infoPanel.addText(string);
		return;
	}
	
}
