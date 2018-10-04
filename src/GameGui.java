import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.swing.JTextField;

import org.eclipse.swt.graphics.Image;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class GameGui extends JFrame {
	JPanel panel = new JPanel();
	JPanel panelN = new JPanel();
	JPanel panelS = new JPanel();
	JPanel panelC = new JPanel();
	NPSOrderedArrayList<JButton> buttons = new NPSOrderedArrayList<JButton>();
	
	public GameGui() {
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		getContentPane().add(panelN, BorderLayout.NORTH);	
		getContentPane().add(panelS, BorderLayout.CENTER);
		getContentPane().add(panelC, BorderLayout.SOUTH);
				JTextArea textArea = new JTextArea();
				panelS.add(textArea);
		setVisible(true);
	
		
		
		
	}
	

	private JTextField textField;
	public void CreateButton( NPSOrderedArrayList<Card> hand) {
		
		for ( int i = 0; i < hand.size(); i++) {
			JButton button = new JButton();
			ImageIcon image = new ImageIcon(getClass().getResource("PNG/" + hand.get(i).value + hand.get(i).suit + ".png"));
			//ImageIcon image = new ImageIcon(getClass().getResource("PNG/10C.png"));
			button.setIcon(image);
			panelS.add(button);
			System.out.println("wow");
			buttons.add(button);
			button.setSize(90, 138);
			button.setMinimumSize(getSize());
		
			button.setBorderPainted(false);
			button.setContentAreaFilled(false);
			button.setFocusPainted(false);
			button.setOpaque(false);
			panelS.revalidate();
			Card myCard = hand.get(i);
			button.addActionListener(new ActionListener() {
			
				public void actionPerformed(ActionEvent e) {
					display(myCard);
					System.out.println("wwwwww");
					button.setVisible(false);
				}
			});
		}
	}
	public void disable() {
		for ( int i = 0; i < buttons.size(); i++) {
			buttons.get(i).setEnabled(false);
		}
		
	}
	public void enable() {
		for ( int i = 0; i < buttons.size(); i++) {
			buttons.get(i).setEnabled(true);
		}
		
	}
	public void display(Card c) {
		JLabel JSP = new JLabel();
		ImageIcon image = new ImageIcon(getClass().getResource("PNG/" + c.value + c.suit + ".png"));
		JSP.setIcon(image);
		panelC.add(JSP);
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

		
		GG.CreateButton(hand);
		
		
	}
}

