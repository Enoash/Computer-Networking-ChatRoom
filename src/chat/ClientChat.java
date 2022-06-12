package chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.awt.BorderLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Objects;
import java.util.Properties;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class ClientChat {

	public static void main(String[] args) {
		BufferedReader KeyBReader=null;
		Client client=null;
		try {
			
			 client = new Client(new Socket("192.168.43.188",8888));
			 Thread clientthread=new Thread(client);
			 clientthread.start();
		} catch (UnknownHostException e) {
			System.out.println("请输入正确的服务器IP ！");
		} catch (ConnectException e) {
			System.out.println("服务器未启动，无法连接聊天室！");
		}  catch (IOException e) {
			e.printStackTrace();
		} finally {
			
		}

	}

}
class Client extends JFrame implements Runnable,ActionListener,KeyListener{
	private Socket socket;
	BufferedReader reader;
	PrintWriter writer;
	
	//文本域
	
	private JTextArea jta;
	
	//滚动条
	private JScrollPane jsp;
	//面板
	private JPanel jp;
	//文本框
	private JTextField jtf;
	//按钮
	private JButton jb;
	private BufferedWriter  bfw=null;
	
	
	public Client(Socket socket) {
		this.socket=socket;
		System.out.println("与服务器连接成功");
		try {
			reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			writer = new PrintWriter(socket.getOutputStream());
		} catch (IOException e) {

			e.printStackTrace();
		}
	}
	public String sendToServer (){
		String msg=jtf.getText();
		jta.append(msg + System.lineSeparator());
		writer.println(msg);
		writer.flush();
		jtf.setText("");
		return msg;
	}
	public void run() {
		
		jta=new JTextArea();
		jta.setEditable(false);
		jsp=new JScrollPane(jta);//文本框加到滚动条实现滚动
		jp= new JPanel();
		jtf=new JTextField(20);
		jb=new JButton("发送");
		
		jp.add(jtf);
		jp.add(jb);
		
		//设置窗口图标
//		String path = Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResource("img/Popcatopen.jpg")).getPath();
//		Toolkit tk= Toolkit.getDefaultToolkit();
//		Image image=tk.createImage(path);  //图片路径或image对象可用于替换网上示例源码中的路径或对象
//		this.setIconImage(image);  //设置软件图标

		
		
		Image frame_icon=Toolkit.getDefaultToolkit().createImage(Client.class.getResource("Popcatopen.jpg"));
		this.setIconImage(frame_icon);
		
		
//		Toolkit toolkit=Toolkit.getDefaultToolkit();
//		Image icon = toolkit.getImage("img/Popcatopen.jpg");
//        this.setIconImage(icon);
//		
		this.add(jsp,BorderLayout.CENTER);
		this.add(jp,BorderLayout.SOUTH);
		
		this.setTitle("客户端聊天室");
		this.setSize(300,300);
		this.setLocation(300,300);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
		jb.addActionListener(this);
		jtf.addKeyListener(this);
		
		
			try {
				while(true) {
				String msg = reader.readLine();
				while((msg=reader.readLine())!=null) {
					jta.append(msg + System.lineSeparator());
				}
				if(msg==null) 
					break;//检测是否被打断
				//System.out.println(msg);
				}
			} catch(SocketException e) {
				
				System.out.println("服务器断开");
			}
			catch (IOException a) {
				a.printStackTrace();
			} finally {
				writer.close();
				try {
					if (reader != null)
						reader.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					if (socket!= null)
						socket.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		this.sendToServer();
	}

		
	
	
}
