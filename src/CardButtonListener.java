import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

//*****************THIS CLASS IS UNUSED. DELETE IF NECESSARY****************
public class CardButtonListener implements ActionListener {
	GameGui gui;
	
	public void setGui(GameGui g) {
		gui = g;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		CardButton clicked = (CardButton) e.getSource();
		clicked.setVisible(false);
		System.out.println(gui.p.name);
		System.out.println(clicked.index);
		gui.p.play(clicked.index);
	}

}
