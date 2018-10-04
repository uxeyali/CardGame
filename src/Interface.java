import org.eclipse.swt.*;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.layout.*;
import static org.eclipse.swt.events.SelectionListener.*;
public class Interface {
	
	public static void Welcome() {
		/*
		 * splash screen
		 */
		Display Welcome = new Display();
		Shell Start = new Shell(Welcome);
		Start.setLayout(new GridLayout());
		Image image = new Image(Welcome, Interface.class.getResourceAsStream("back_cards-07.png"));
		final int width = image.getBounds().width;
		final int height = image.getBounds().height;
		final Image scaled050 = new Image(Welcome,
		        image.getImageData().scaledTo((int)(width*0.5),(int)(height*0.5)));
		Button Local = new Button(Start, SWT.PUSH);
		Button Online = new Button(Start, SWT.PUSH);
		Start.setBackgroundImage(scaled050);
		Local.setText("Local Game");
		Online.setText("Online Game");
		CardGame CG = new CardGame();
		
		Local.addListener(SWT.Selection, e -> CG.startLocalGame());
		Player p1Player = new Player("Player1");
		//p1Player.hand.get(1).suit;
		//p1Player.hand.get(1).value;
		Start.setSize(1640, 800);
		Local.pack();
		Online.pack();
		Start.open();
		while (!Start.isDisposed ()) {
			if (!Welcome.readAndDispatch ()) Welcome.sleep ();
		}
		Welcome.dispose ();

	}
	public void DisplayStart() {
		Display player = new Display();
	}
	public void Player1() {
		/*
		 * This method defines the use
		 * of Player 1
		 * The window will have 4
		 * main areas. 
		 * Player score,
		 * who's turn it is
		 * Cards being played
		 * Cards to choose
		 */
		
		Display playe1r = new Display();
		for (int i = 0; i < 3; i++ ) {
		Shell Player1 = new Shell(playe1r);
		Label score = new Label(Player1, SWT.BORDER);
		Label turn = new Label(Player1, SWT.BORDER);
		Label center = new Label(Player1, SWT.BORDER);
		//Button button = new Button(Player1, SWT.PUSH);
		Button card1 = new Button(Player1, SWT.PUSH);
		Button card2 = new Button(Player1, SWT.PUSH);
		Button card3 = new Button(Player1, SWT.PUSH);
		Button card4 = new Button(Player1, SWT.PUSH);
		Button card5 = new Button(Player1, SWT.PUSH);
		Button card6 = new Button(Player1, SWT.PUSH);
		Button card7 = new Button(Player1, SWT.PUSH);
		GridLayout cards = new GridLayout(7,true);
		score.setText("Your Score:");
		score.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, true, true, 1, 1));
		Player1.setLayout(cards);
		//button.setText("current");
		turn.setText("Who's Turn");
		center.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, true, 7, 4));
		turn.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, true, true, 3,2));
		card1.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		card2.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		card3.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		card4.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		card5.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		card6.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		card7.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		//card1.setVisible(false);
		card1.setEnabled(false);
		Player1.setSize(1100, 900);
		//This how to add a pic
		Image pic = new Image(playe1r, Interface.class.getResourceAsStream("PNG/2C.png"));
		//card.suit+card.value+".png"
		final int width = pic.getBounds().width;
		final int height = pic.getBounds().height;
		final Image scaled050 = new Image(playe1r,
		        pic.getImageData().scaledTo((int)(width*0.2),(int)(height*0.2)));
		//button.addListener(SWT.MouseHover, e-> System.out.println("wow"));
		//button.setImage(scaled050);
		card1.setImage(scaled050);
		
		Player1.open();
		Interface IN = new Interface();
		
		while (!Player1.isDisposed()) {
			if (!playe1r.readAndDispatch()) playe1r.sleep();
		}
		playe1r.dispose();
		
		}
		
	}
	public void Player2() {
		/*
		 * This method defines the use
		 * of Player 1
		 * The window will have 4
		 * main areas. 
		 * Player score,
		 * who's turn it is
		 * Cards being played
		 * Cards to choose
		 */
		Display playe2r = new Display();
		Shell Player2 = new Shell(playe2r);
		Label score = new Label(Player2, SWT.BORDER);
		Label turn = new Label(Player2, SWT.BORDER);
		Label center = new Label(Player2, SWT.BORDER);
		//Button button = new Button(Player1, SWT.PUSH);
		Button card1 = new Button(Player2, SWT.PUSH);
		Button card2 = new Button(Player2, SWT.PUSH);
		Button card3 = new Button(Player2, SWT.PUSH);
		Button card4 = new Button(Player2, SWT.PUSH);
		Button card5 = new Button(Player2, SWT.PUSH);
		Button card6 = new Button(Player2, SWT.PUSH);
		Button card7 = new Button(Player2, SWT.PUSH);
		GridLayout cards = new GridLayout(7,true);
		score.setText("Your Score:");
		score.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, true, true, 1, 1));
		Player2.setLayout(cards);
		//button.setText("current");
		turn.setText("Who's Turn");
		center.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, true, 7, 4));
		turn.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, true, true, 3,2));
		card1.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		card2.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		card3.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		card4.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		card5.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		card6.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		card7.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		//card1.setVisible(false);
		card1.setEnabled(false);
		Player2.setSize(1100, 900);
		//This how to add a pic
		Image pic = new Image(playe2r, Interface.class.getResourceAsStream("PNG/2C.png"));
		//card.suit+card.value+".png"
		final int width = pic.getBounds().width;
		final int height = pic.getBounds().height;
		final Image scaled050 = new Image(playe2r,
		        pic.getImageData().scaledTo((int)(width*0.2),(int)(height*0.2)));
		//button.addListener(SWT.MouseHover, e-> System.out.println("wow"));
		//button.setImage(scaled050);
		card1.setImage(scaled050);
		
		Player2.open();
		while (!Player2.isDisposed()) {
			if (!playe2r.readAndDispatch()) playe2r.sleep();
		}
		playe2r.dispose();
		
		
	}
	public void Player3() {
		/*
		 * This method defines the use
		 * of Player 1
		 * The window will have 4
		 * main areas. 
		 * Player score,
		 * who's turn it is
		 * Cards being played
		 * Cards to choose
		 */
		Display playe3r = new Display();
		Shell Player3 = new Shell(playe3r);
		Label score = new Label(Player3, SWT.BORDER);
		Label turn = new Label(Player3, SWT.BORDER);
		Label center = new Label(Player3, SWT.BORDER);
		//Button button = new Button(Player1, SWT.PUSH);
		Button card1 = new Button(Player3, SWT.PUSH);
		Button card2 = new Button(Player3, SWT.PUSH);
		Button card3 = new Button(Player3, SWT.PUSH);
		Button card4 = new Button(Player3, SWT.PUSH);
		Button card5 = new Button(Player3, SWT.PUSH);
		Button card6 = new Button(Player3, SWT.PUSH);
		Button card7 = new Button(Player3, SWT.PUSH);
		GridLayout cards = new GridLayout(7,true);
		score.setText("Your Score:");
		score.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, true, true, 1, 1));
		Player3.setLayout(cards);
		//button.setText("current");
		turn.setText("Who's Turn");
		center.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, true, 7, 4));
		turn.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, true, true, 3,2));
		card1.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		card2.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		card3.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		card4.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		card5.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		card6.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		card7.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		//card1.setVisible(false);
		card1.setEnabled(false);
		Player3.setSize(1100, 900);
		//This how to add a pic
		Image pic = new Image(playe3r, Interface.class.getResourceAsStream("PNG/2C.png"));
		//card.suit+card.value+".png"
		final int width = pic.getBounds().width;
		final int height = pic.getBounds().height;
		final Image scaled050 = new Image(playe3r,
		        pic.getImageData().scaledTo((int)(width*0.2),(int)(height*0.2)));
		//button.addListener(SWT.MouseHover, e-> System.out.println("wow"));
		//button.setImage(scaled050);
		card1.setImage(scaled050);
		
		Player3.open();
		while (!Player3.isDisposed()) {
			if (!playe3r.readAndDispatch()) playe3r.sleep();
		}
		playe3r.dispose();
		
		
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Interface IN = new Interface();
		IN.Player1();
		IN.Player2();
		IN.Player3();
		
	}
}
