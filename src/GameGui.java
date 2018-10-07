import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.swing.JTextField;
import javax.swing.text.DefaultCaret;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class GameGui extends JFrame {
	JPanel panelN = new JPanel();
	JPanel panelS = new JPanel();
	JPanel panelC = new JPanel();
	NPSOrderedArrayList<CardButton> buttons = new NPSOrderedArrayList<CardButton>();
	//for outputs
	JTextArea textAreaGame;
	JTextArea textAreaScores;
	//labels with icons
	JLabel played1;
	JLabel played2;
	JLabel played3;
	//others
	Player p;
	//CardButtonListener listener;
		
	public GameGui() {
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(new Dimension(800, 760));
		//setExtendedState(JFrame.MAXIMIZED_BOTH);
		getContentPane().add(panelN, BorderLayout.NORTH);
		panelN.setBorder(BorderFactory.createRaisedBevelBorder());
		getContentPane().add(panelS, BorderLayout.SOUTH);
		panelS.setBorder(BorderFactory.createRaisedBevelBorder());
		getContentPane().add(panelC, BorderLayout.CENTER);
		panelC.setBorder(BorderFactory.createRaisedBevelBorder());
		panelC.setBackground(Color.darkGray);
		
		textAreaScores = new JTextArea(4,15);
		panelN.add(textAreaScores);
		textAreaGame = new JTextArea(4,20);
		JScrollPane scrollPane = new JScrollPane();
		DefaultCaret caret = (DefaultCaret) textAreaGame.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		scrollPane.setViewportView(textAreaGame);
		panelN.add(scrollPane);
		
//		listener = new CardButtonListener();
//		listener.setGui(this);
		
		setVisible(true);
	}
	
//	public void setListener(CardButtonListener l) {
//		listener = l;
//	}
	
	public void setPlayer(Player p) {
		this.p = p;
	}
	
	public void displayMessage(String message) {
		textAreaGame.append(message + "\n");
	}
	
	public void setScores(String message) {
		textAreaScores.setText(message);
	}
	

	public void CreateButtons( NPSOrderedArrayList<Card> hand) {
		for ( int i = 0; i < hand.size(); i++) {
			CardButton button = new CardButton(i);
			ImageIcon image = new ImageIcon(getClass().getResource("PNG/" + hand.get(i).value + hand.get(i).suit + ".png"));
			//ImageIcon image = new ImageIcon(getClass().getResource("PNG/10C.png"));
			button.setIcon(image);
			panelC.add(button);
			buttons.add(button);
			//button.setEnabled(false);
			button.setSize(90, 138);
			button.setMinimumSize(getSize());
		
			button.setBorderPainted(false);
			button.setContentAreaFilled(false);
			button.setFocusPainted(false);
			button.setOpaque(false);
			panelC.revalidate();
			Card myCard = hand.get(i);
			//button.addActionListener(listener);
			button.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					displayCard(myCard);
					button.setVisible(false);
					
					//this may cause issues. Keep an eye on it...
					System.out.println(hand.get(0));
					System.out.println(button.index);
					p.play(button.index);
					buttons.remove(button.index);
					updateButtonsIndex();
					disableAll();
				}
			});
		}
	}
	
	//when a card is removed, this will be called to make its indexes match with the player's hand
	public void updateButtonsIndex() {
		for(int i = 0; i < buttons.size(); i++) {
			buttons.get(i).setIndex(i);
		}
	}
	
	public void disableAll() {
		for ( int i = 0; i < buttons.size(); i++) {
			buttons.get(i).setEnabled(false);
		}
		
	}
	
	public void enableAll() {
		for ( int i = 0; i < buttons.size(); i++) {
			buttons.get(i).setEnabled(true);
		}
	}
	
	public void displayCard(Card c) {
		JLabel JSP = new JLabel();
		ImageIcon image = new ImageIcon(getClass().getResource("PNG/" + c.value + c.suit + ".png"));
		JSP.setIcon(image);
		panelS.add(JSP);
	}
	
	public void setTurn(String message) {
		if(message.equals("Your Turn")) {
			displayMessage("It's your turn, " + p.name);
			enableAll();
		}
		else {
			displayMessage(message);
			disableAll();
		}
	}
	
	public static void main(String[] args) throws InterruptedException {
		// TODO Auto-generated method stub
		GameGui GG = new GameGui();
		NPSOrderedArrayList<Card> hand = new NPSOrderedArrayList<Card>();
		hand.add(new Card("H", 2));
		hand.add(new Card("D", 5));
		hand.add(new Card("S", 12));
		hand.add(new Card("H", 2));
		hand.add(new Card("D", 5));
		hand.add(new Card("S", 12));
		hand.add(new Card("H", 2));
		hand.add(new Card("D", 5));
		hand.add(new Card("S", 12));
		hand.add(new Card("H", 2));
		hand.add(new Card("D", 5));
		hand.add(new Card("S", 12));
		hand.add(new Card("H", 2));
		hand.add(new Card("D", 5));
		hand.add(new Card("S", 12));
		hand.add(new Card("H", 2));
		hand.add(new Card("D", 5));

		
		GG.CreateButtons(hand);
		
		
	}
}

