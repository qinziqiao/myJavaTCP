package Client;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

import javax.swing.JOptionPane;

public class SocketClient implements Runnable {
	MainClient client;

	public SocketClient(MainClient c) {
		client=c;
	}
	
	@Override
	public void run() {
		
		String sentence;
		String modifiedSentence;
		BufferedReader inFromUser=new BufferedReader(
				new InputStreamReader(System.in));
		
		//TCP�׽��ֿͻ��˵�<���ղ���>
		try{
			BufferedReader inFromServer=new BufferedReader(
					new InputStreamReader(client.clientSocket.getInputStream()));
			while(true){
					String serverSentence =  inFromServer.readLine();
					String temp = serverSentence.substring(0, 1);
					int type = Integer.parseInt(temp);
					serverSentence=serverSentence.substring(1);
					if(type==1){
						//�յ��ظ�
						client.autoReplyWrite("From Server: "+serverSentence);
					}//�յ��㲥��Ϣ
					else if(type==0){
						client.chatWrite("Broadcast from Server:\n  ->"+serverSentence);
					}		 	
			}
		}
		catch(SocketException e1){
			System.out.println("�ͻ��˹ر�");
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
				
	}

}
