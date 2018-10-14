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
import java.io.IOException;

import javax.swing.JTextField;
import javax.swing.text.DefaultCaret;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class GameGui extends JFrame {
	JPanel panelN = new JPanel();
	JPanel panelS = new JPanel();
	JPanel panelC = new JPanel();
	NPSOrderedArrayList<CardButton> buttons;
	//for outputs
	JTextArea textAreaGame;
	JTextArea textAreaScores;
	//labels with icons
	JLabel played1;
	JLabel played2;
	JLabel played3;
	//for playAgain
	JButton playAgainBtn;
	JButton quitBtn;
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
		
		//add labels to panelS
		played1 = new JLabel();
		panelS.add(played1);
		played2 = new JLabel();
		panelS.add(played2);
		played3 = new JLabel();
		panelS.add(played3);
		
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
	
	//creates card buttons
	public void CreateButtons( NPSOrderedArrayList<Card> hand) {
		buttons = new NPSOrderedArrayList<CardButton>();
		for ( int i = 0; i < hand.size(); i++) {
			CardButton button = new CardButton(i);
			ImageIcon image = new ImageIcon(getClass().getResource("PNG/" + hand.get(i).value + hand.get(i).suit + ".png"));
			//ImageIcon image = new ImageIcon(getClass().getResource("PNG/10C.png"));
			button.setIcon(image);
			panelC.add(button);
			buttons.add(button);
			button.setEnabled(false);
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
					//displayCard(myCard);
					button.setVisible(false);
					try {
						p.sendToServer(p.hand.get(button.index));
						//this may cause issues. Keep an eye on it...
						System.out.println(hand.get(0));
						System.out.println(button.index);
						
						//ideally, we want to listen for ack before doing this
						p.play(button.index);
						buttons.remove(button.index);
						updateButtonsIndex();
						disableAll();
					} catch (IndexOutOfBoundsException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}//end of catch
				}//end of actionPerformed
			});//end of inner class
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
	
	//enables any card that is a leading suit. If they don't have a leading suit, enableAll()
	public void enableSome(String suit) {
		//hand and buttons should have the same length
		boolean hasLead = false;
		for ( int i = 0; i < buttons.size(); i++) {
			if(p.hand.get(i).suit.equals(suit)) {
				hasLead = true;
				buttons.get(i).setEnabled(true);
			}
		}
		//if no lead, enable all
		if(!hasLead) {
			enableAll();
		}
	}
	
	//displays cards on panelS
	public void displayCard(Card c) {
		ImageIcon image = new ImageIcon(getClass().getResource("PNG/" + c.value + c.suit + ".png"));
		//order assures that the cards will not appear twice if the server accidentally sends the message twice
		if(c.order == 1) {
			played1.setIcon(image);
		}
		else if(c.order == 2) {
			played2.setIcon(image);
		}
		else if(c.order == 3) {
			played3.setIcon(image);
		}
		panelS.repaint();
		panelS.revalidate();
	}
	
	//clears panelS so new cards can be displayed
	public void clearCards() {
		panelS.removeAll();
		played1 = new JLabel();
		panelS.add(played1);
		played2 = new JLabel();
		panelS.add(played2);
		played3 = new JLabel();
		panelS.add(played3);
		panelS.repaint();
		panelS.revalidate();
	}
	
	//asks player if they want to play again
	public void playAgain() {
		playAgainBtn = new JButton("Play Again");
		quitBtn = new JButton("Quit");
		panelC.add(playAgainBtn);
		panelC.add(quitBtn);
		panelC.repaint();
		panelC.revalidate();
		//listeners
		playAgainBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					p.sendToServer("Play Again");
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				playAgainBtn.setEnabled(false);
				quitBtn.setEnabled(false);
				clearCenter();
			}//end ActionPerformed
		});//end ActionListener
		quitBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					p.sendToServer("Quit");
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				playAgainBtn.setEnabled(false);
				quitBtn.setEnabled(false);
				clearCenter();
			}//end ActionPerformed
		});//end ActionListener
	}
	
	public void clearCenter() {
		panelC.removeAll();
		panelC.repaint();
		panelC.revalidate();
	}
	
	//unused method
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
	
//	public static void main(String[] args) throws InterruptedException {
//		// TODO Auto-generated method stub
//		GameGui GG = new GameGui();
//		NPSOrderedArrayList<Card> hand = new NPSOrderedArrayList<Card>();
//		hand.add(new Card("H", 2));
//		hand.add(new Card("D", 5));
//		hand.add(new Card("S", 12));
//		hand.add(new Card("H", 2));
//		hand.add(new Card("D", 5));
//		hand.add(new Card("S", 12));
//		hand.add(new Card("H", 2));
//		hand.add(new Card("D", 5));
//		hand.add(new Card("S", 12));
//		hand.add(new Card("H", 2));
//		hand.add(new Card("D", 5));
//		hand.add(new Card("S", 12));
//		hand.add(new Card("H", 2));
//		hand.add(new Card("D", 5));
//		hand.add(new Card("S", 12));
//		hand.add(new Card("H", 2));
//		hand.add(new Card("D", 5));
//
//		
//		GG.CreateButtons(hand);
//	}
}

