import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import java.awt.GridLayout;
import javax.swing.JTextField;
import javax.swing.JPanel;

public class GameGui extends JFrame {
	JPanel panel = new JPanel();
	public GameGui() {
	
		
		getContentPane().add(panel);
		
				
				JTextArea textArea = new JTextArea();
				panel.add(textArea);
		setVisible(true);
		
		
		
	}
	JFrame Card = new JFrame();
	private JTextField textField;
	public void CreateButton( NPSOrderedArrayList<Card> hand) {
		Player h = new Player();
		for ( int i = 0; i < hand.size(); i++) {
			JButton button = new JButton();
			//ImageIcon image = new ImageIcon("PNG/" + hand.get(i).value + hand.get(i).suit + ".png");
			ImageIcon image = new ImageIcon(getClass().getResource("PNG/10C.png"));
			button.setBounds(i* 30, i * 30, 100, 300);
			button.setIcon(image);
			panel.add(button);
			System.out.println("wow");
		}
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		GameGui GG = new GameGui();
		NPSOrderedArrayList<Card> hand = new NPSOrderedArrayList<Card>();
		hand.add(new Card("h", 1));
		hand.add(new Card("h", 1));
		hand.add(new Card("h", 1));
		
		GG.CreateButton(hand);
		
	}
}

