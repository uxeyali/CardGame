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
		
		Local.addListener(SWT.Selection, e -> Player1());
		Player p1Player = new Player();
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
	private static void Player1() {
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
		Display player = new Display();
		Shell Player1 = new Shell(player);
		Label score = new Label(Player1, SWT.BORDER);
		Label turn = new Label(Player1, SWT.BORDER);
		Button button = new Button(Player1, SWT.PUSH);
		
		button.setLocation(100, 100);
		button.setSize(300, 300);
		button.setText("current");
		Player1.setSize(700, 700);
		score.setLocation(0, 0);
		score.setText("Your Score:");
		score.setSize(80, 50);
		turn.setLocation(300, 0);
		turn.setSize(200, 100);
		turn.setText("Who's Turn");
		Image pic = new Image(player, Interface.class.getResourceAsStream("PNG/2C.png"));
		final int width = pic.getBounds().width;
		final int height = pic.getBounds().height;
		final Image scaled050 = new Image(player,
		        pic.getImageData().scaledTo((int)(width*0.2),(int)(height*0.2)));
		button.addListener(SWT.MouseHover, e-> System.out.println("wow"));
		button.setImage(scaled050);
		Player1.open();
		while (!Player1.isDisposed()) {
			if (!player.readAndDispatch()) player.sleep();
		}
		player.dispose();
		
		
	}

	public static void Display() {
	Display display = new Display();
	
	Shell P1 = new Shell(display);
	Shell P2 = new Shell(display);
	Shell P3 = new Shell(display);
	final Label Begin = new Label(P1, SWT.None);
	Begin.setText("Welcome!");
	Begin.setOrientation(SWT.CENTER);
	Menu bar = new Menu (P1, SWT.BAR);
	P1.setMenuBar(bar);
	MenuItem Local1 = new MenuItem (bar, SWT.CASCADE);
	Local1.setText("New Game");
	Menu submenu = new Menu (P1, SWT.DROP_DOWN);
	Local1.setMenu(submenu);
	MenuItem item = new MenuItem (submenu, SWT.PUSH);
	item.addListener(SWT.Selection, e -> P2.open());
	item.setText("Local Game");
	item.setAccelerator(SWT.MOD1 + 'A');
	MenuItem item2 = new MenuItem (submenu, SWT.PUSH);
	item2.addListener(SWT.Selection, e -> P3.open());
	item2.setText("Online Game");
	item2.setAccelerator(SWT.MOD1 + 'A');
	P1.setSize(500, 500);
	Begin.pack();
	P1.open();
	while (!P1.isDisposed ()) {
		if (!display.readAndDispatch ()) display.sleep ();
	}
	display.dispose ();

	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Player1();
	}
}
