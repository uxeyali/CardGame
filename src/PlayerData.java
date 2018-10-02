import java.io.Serializable;

/* *************This class is necessary to send info to the Server********************** */
public class PlayerData implements Serializable{
	String name;
	int port;
	public PlayerData(String name, int port) {
		this.name = name;
		this.port = port;
	}
}
