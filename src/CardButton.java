import javax.swing.JButton;

//********************This class only exists to get the index of the cards when a button is pressed*************
public class CardButton extends JButton{
	int index;
	public CardButton(int index) {
		this.index = index;
	}
	
	public void setIndex(int index) {
		this.index = index;
	}

}
