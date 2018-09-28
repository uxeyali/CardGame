import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;


public class CardGameServer extends CardGame{
	//sending port 5001
	//receiving port 30481
	InetAddress clientIP[] = new InetAddress[3];
	int IPcount = 0;
	
	public void listenForClientsObject() throws IOException{
		byte[] buffer = new byte[1024];
		DatagramSocket receiveSocket = new DatagramSocket(30481);
		//serverView.textAreaServer.append("Waiting...\n");
		System.out.println("Waiting...");
		DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
		receiveSocket.receive(packet);
		clientIP[IPcount] = packet.getAddress();
		IPcount++;
		byte[] data = packet.getData();
		ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
		ObjectInputStream objectStream = new ObjectInputStream(inputStream);
		try {
			String message = (String) objectStream.readObject();
			//in the final program, we'll check if the object is a String, card, or player array
			if(message instanceof String) {
				//serverView.textAreaServer.append(message + "\n");
				System.out.println(message);
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			//serverView.textAreaServer.append("Not correct object. Moving on...\n");
			System.out.println("Not correct object. Moving on...");
		}
		receiveSocket.close();
	}
	
	public void sendObjectTo(Object object, InetAddress ip) throws IOException{
		DatagramSocket sendSocket = new DatagramSocket();
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		ObjectOutputStream objectStream = new ObjectOutputStream(outputStream);
		objectStream.writeObject(object);
		byte[] data = outputStream.toByteArray();
		DatagramPacket packet = new DatagramPacket(data, data.length, ip, 5001);
		sendSocket.send(packet);
		sendSocket.close();
	}
}
