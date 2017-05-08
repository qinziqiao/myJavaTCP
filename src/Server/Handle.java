package Server;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import javax.swing.JOptionPane;

public class Handle implements Runnable{
	private Socket socket;
	MainServer mainServer;
	public Handle(MainServer m,Socket s) {
		mainServer=m;
		socket=s;
	}
	@Override
	public void run() {
		String clientSentence;
		String capitalizedSentence;
		
		//TCP�׽��ַ���˵�<���ղ���>
		try {
			while(true){
				BufferedReader inFromClient=new BufferedReader(
						new InputStreamReader(socket.getInputStream()));
				//������������
					clientSentence=inFromClient.readLine();
					//�����ַ���
					String temp = clientSentence.substring(0, 1);
					int type = Integer.parseInt(temp);
					clientSentence=clientSentence.substring(1);
					if(type==1){
						DataOutputStream outToClient=new DataOutputStream(
								socket.getOutputStream());
						//ת��
						capitalizedSentence=clientSentence.toUpperCase()+'\n';
							outToClient.writeBytes(1+capitalizedSentence);
							//д��ȥ
							mainServer.autoReplyWrite(socket.getInetAddress()+":  "+clientSentence +" -> "+ capitalizedSentence);
					}
					else {	
						mainServer.chatWrite(socket.getInetAddress()+":  "+clientSentence);
					}	
				}
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Server,IO�쳣,������");
		}catch (NullPointerException e){
			mainServer.socketList.remove(socket);
			System.out.println("����NullPointerException ����ͻ����˳�");
			System.out.println("��ǰ������: "+mainServer.socketList.size());
			mainServer.autoReplyWrite("��һ���ͻ����˳�");
			mainServer.chatWrite("��һ���ͻ����˳�");
		}
		
	}

}
