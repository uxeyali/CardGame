import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.layout.*;
import static org.eclipse.swt.events.SelectionListener.*;
public class Interface {

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
		Display();
	}
}
