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
		//TCP套接字
				ServerSocket welcomeSocket = null;
				try {
					welcomeSocket = new ServerSocket(6789);
				} catch (IOException e) {
					JOptionPane.showMessageDialog(null, "监听Socket初始化失败");
					e.printStackTrace();
					System.exit(0);
				}
				while(true) {
					try {
						Socket connectionSocket = welcomeSocket.accept();
						mainServer.autoReplyWrite("有一个客户端加入");
						mainServer.chatWrite("有一个客户端加入");
						mainServer.socketList.addLast(connectionSocket);
						System.out.println("当前连接数: "+mainServer.socketList.size());
						//消息处理线程
						Handle autoReply = new Handle(mainServer,connectionSocket);
						Thread thread = new Thread(autoReply);
						thread.start();					
					} catch (IOException e) {
						JOptionPane.showMessageDialog(null, "IO异常");
						e.printStackTrace();
					}
			
				}
	}

}
