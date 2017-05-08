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
		
		//TCP套接字服务端的<接收部分>
		try {
			while(true){
				BufferedReader inFromClient=new BufferedReader(
						new InputStreamReader(socket.getInputStream()));
				//从数据流读入
					clientSentence=inFromClient.readLine();
					//处理字符串
					String temp = clientSentence.substring(0, 1);
					int type = Integer.parseInt(temp);
					clientSentence=clientSentence.substring(1);
					if(type==1){
						DataOutputStream outToClient=new DataOutputStream(
								socket.getOutputStream());
						//转化
						capitalizedSentence=clientSentence.toUpperCase()+'\n';
							outToClient.writeBytes(1+capitalizedSentence);
							//写回去
							mainServer.autoReplyWrite(socket.getInetAddress()+":  "+clientSentence +" -> "+ capitalizedSentence);
					}
					else {	
						mainServer.chatWrite(socket.getInetAddress()+":  "+clientSentence);
					}	
				}
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Server,IO异常,请重试");
		}catch (NullPointerException e){
			mainServer.socketList.remove(socket);
			System.out.println("利用NullPointerException 处理客户端退出");
			System.out.println("当前连接数: "+mainServer.socketList.size());
			mainServer.autoReplyWrite("有一个客户端退出");
			mainServer.chatWrite("有一个客户端退出");
		}
		
	}

}
