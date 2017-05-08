package Client;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import javax.swing.JOptionPane;

public class AutoReply implements Runnable{
	private Socket socket;
	MainClient mainClient;
	public AutoReply(MainClient m,Socket s) {
		mainClient=m;
		socket=s;
	}
	@Override
	public void run() {
		String clientSentence;
		String capitalizedSentence;
		try {
			BufferedReader inFromClient=new BufferedReader(
					new InputStreamReader(socket.getInputStream()));
			DataOutputStream outToClient=new DataOutputStream(
					socket.getOutputStream());
			//������������
			clientSentence=inFromClient.readLine();
			capitalizedSentence=clientSentence.toUpperCase()+'\n';
			outToClient.writeBytes(capitalizedSentence);
			//д��ȥ
			mainClient.autoReplyWrite(capitalizedSentence);
			
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "IO�쳣");
			e.printStackTrace();
		}
		
	}

}
