package Server;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JOptionPane;

public class SocketServer implements Runnable {
	private MainServer mainServer;

	public SocketServer(MainServer m) {
		mainServer=m;
	}
	
	@Override
	public void run() {
		//TCP�׽���
				ServerSocket welcomeSocket = null;
				try {
					welcomeSocket = new ServerSocket(6789);
				} catch (IOException e) {
					JOptionPane.showMessageDialog(null, "����Socket��ʼ��ʧ��");
					e.printStackTrace();
					System.exit(0);
				}
				while(true) {
					try {
						Socket connectionSocket = welcomeSocket.accept();
						mainServer.autoReplyWrite("��һ���ͻ��˼���");
						mainServer.chatWrite("��һ���ͻ��˼���");
						mainServer.socketList.addLast(connectionSocket);
						System.out.println("��ǰ������: "+mainServer.socketList.size());
						//��Ϣ�����߳�
						Handle autoReply = new Handle(mainServer,connectionSocket);
						Thread thread = new Thread(autoReply);
						thread.start();					
					} catch (IOException e) {
						JOptionPane.showMessageDialog(null, "IO�쳣");
						e.printStackTrace();
					}
			
				}
	}

}
