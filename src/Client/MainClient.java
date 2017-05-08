package Client;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.security.auth.callback.Callback;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import java.io.*;
import java.net.*;


public class MainClient extends JPanel implements Callback {

	/**各种Swing组件*/
	private JFrame mainFrame;
    //一个底层JPanel包括整个ChatDisplay
    private JPanel interfacePanel;
    //菜单
    private JMenuBar jMenuBar=new JMenuBar();
    private JMenu[] jMenus={
    		new JMenu("File"),
    		new JMenu("Action")
    };
    private JMenuItem[] items={
	   new JMenuItem("Save"),
	   new JMenuItem("Exit")
    };
    
    //选项卡
    private JTabbedPane tab ;
    private JPanel jp[];
   	//不可编辑文本框
    private JTextField jTextField ;
   	//public
    private JTextArea chatText;  
   	//private 
    private JTextArea autoReplyText;
   	//用户列表
   	private JPanel jPanel1,jPanel2,jPanel3;
	String[] users={"Leaving a message","Auto-reply"};
    private JComboBox<String> userComboBox;
    private JLabel jLabel;
    //消息编辑
    private JLabel jLabel2;
    private JButton jButton;
    private JTextField jTextField2;
    
    //socket
    Socket clientSocket;
    
    /**构造函数*/
    public MainClient() {
    	GUI();	
    	CreateFrame();
    	
    	//连接服务器,注意第二个参数的端口号是你想要连接的
    	try {
			clientSocket=new Socket(InetAddress.getLocalHost(),6789);
			System.out.println("客户端端口号-服务器端口号: "+clientSocket.getLocalPort()+"-"+clientSocket.getPort());
		} catch (Exception  e) {
			JOptionPane.showMessageDialog(null, "初始连接失败");
			e.printStackTrace();
			System.exit(0);
		}
    	autoReplyText.setText("连接成功");
	  	//开线程进行服务
	    	Thread thread = new Thread(new SocketClient(this));
			thread.start();
	}
	
    /**页面绘制*/
	private void GUI(){
		interfacePanel = new JPanel(new BorderLayout());
		
    	/**1.加入菜单*/
    	for(JMenuItem item:items)
    		jMenus[0].add(item);
    	for(JMenu menu:jMenus)
    		jMenuBar.add(menu);
    	/**2.JTabbedPane选项卡*/
    	tab = new JTabbedPane(JTabbedPane.BOTTOM);
    	jp=new JPanel[users.length];
    	for(int i=0;i<jp.length;i++){
    		jp[i]=new JPanel(new BorderLayout());
    		tab.add(jp[i],users[i]);
    	} 	
    	interfacePanel.add(BorderLayout.CENTER,tab);
    	
    	/**3.不可编辑的文本框*/
    	chatText = new JTextArea("等待连接...");
    	chatText.setEditable(false);
    	chatText.setLineWrap(true);// 设置文本区的换行策略。
    	jp[0].add(chatText);
    	
    	autoReplyText = new JTextArea("等待连接...");
    	autoReplyText.setEditable(false);
    	autoReplyText.setLineWrap(true);// 设置文本区的换行策略。
    	jp[1].add(autoReplyText);
    		
    	/**4.底部控件*/
    	/**加入JComboBox 下拉选*/
    	jPanel1 =new JPanel(new GridBagLayout());
    	jPanel2 = new JPanel(new GridBagLayout());
    	jPanel3=new JPanel(new GridLayout(2,1));
  
    	//GridBag布局控制器
    	GridBagConstraints gbc = new GridBagConstraints();
    	gbc.gridwidth = 1;  //放的格子数
    	gbc.weightx = 1;    //水平拉伸
    	gbc.fill = GridBagConstraints.BOTH; 
    	
    	jLabel = new JLabel("Send To ");
    	userComboBox=new JComboBox<String>(users);
    	jPanel1.add(jLabel);
    	jPanel1.add(userComboBox,gbc);	
    	
    	/**5.加入文本输入*/
    	jLabel2 = new JLabel("Message");
    	jTextField2=new JTextField();
    	jButton = new JButton("Send");
    	//增加按键监听器
    	jButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				//TCP套接字 客户端的<发送部分>
						
				try{	
					//进行读写操作
					DataOutputStream outToServer=new DataOutputStream(
							clientSocket.getOutputStream());
					//从编辑区读取并写入Socket中
					int type = userComboBox.getSelectedIndex();
					if(type==1){
						outToServer.writeBytes(type+jTextField2.getText()+'\n');												
						jTextField2.setText("");	
					}
					else{
						outToServer.writeBytes(type+jTextField2.getText()+'\n');
						chatWrite("我："+jTextField2.getText());
						jTextField2.setText("");	
					}		
					
				}
				catch(Exception e){
					e.printStackTrace();
				}
				
			}
		});
    	jPanel2.add(jLabel2);
    	
    	//GridBag布局控制器
    	GridBagConstraints gbc2 = new GridBagConstraints();
    	gbc2.gridwidth=1;
    	gbc2.weightx = 1;
    	gbc2.fill = GridBagConstraints.BOTH;	
    	jPanel2.add(jTextField2,gbc2);
    	/**jButton加到最后*/
    	jPanel2.add(jButton);
    	
    	/**JPanel的嵌套。*/
    	jPanel3.add(jPanel1);
    	jPanel3.add(jPanel2);
    	interfacePanel.add(BorderLayout.SOUTH,jPanel3);
    	
    	/**最后把interfacePanel加入到this类本身上面，因为这个类继承自JPanel，可以序列化后传值*/
    	this.setLayout(new BorderLayout());
    	this.add(interfacePanel);
	}
	
	@SuppressWarnings("deprecation")
	private void CreateFrame(){
		mainFrame = new JFrame("Client");
		mainFrame.getContentPane().add(this,BorderLayout.CENTER);
		//当点击窗口x时自动关闭程序
		mainFrame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				try {
					
					clientSocket.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				System.exit(0);
			}
		});
		// 加入菜单
		mainFrame.setJMenuBar(this.jMenuBar);
		mainFrame.setSize(new Dimension(300, 400));
		mainFrame.show();
	}
	
	//同步问题加锁
		public synchronized void autoReplyWrite(String s){
			autoReplyText.append("\r\n");
			autoReplyText.append(s);
		}
		public synchronized void chatWrite(String s){
			chatText.append("\r\n");
			chatText.append(s);
		}
	
	public static void main(String[] args)  {
		MainClient mainServer = new MainClient();
		
	}

}
