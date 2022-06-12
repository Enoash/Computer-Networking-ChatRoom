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
			System.out.println("��������ȷ�ķ�����IP ��");
		} catch (ConnectException e) {
			System.out.println("������δ�������޷����������ң�");
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
	
	//�ı���
	
	private JTextArea jta;
	
	//������
	private JScrollPane jsp;
	//���
	private JPanel jp;
	//�ı���
	private JTextField jtf;
	//��ť
	private JButton jb;
	private BufferedWriter  bfw=null;
	
	
	public Client(Socket socket) {
		this.socket=socket;
		System.out.println("����������ӳɹ�");
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
		jsp=new JScrollPane(jta);//�ı���ӵ�������ʵ�ֹ���
		jp= new JPanel();
		jtf=new JTextField(20);
		jb=new JButton("����");
		
		jp.add(jtf);
		jp.add(jb);
		
		//���ô���ͼ��
//		String path = Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResource("img/Popcatopen.jpg")).getPath();
//		Toolkit tk= Toolkit.getDefaultToolkit();
//		Image image=tk.createImage(path);  //ͼƬ·����image����������滻����ʾ��Դ���е�·�������
//		this.setIconImage(image);  //�������ͼ��

		
		
		Image frame_icon=Toolkit.getDefaultToolkit().createImage(Client.class.getResource("Popcatopen.jpg"));
		this.setIconImage(frame_icon);
		
		
//		Toolkit toolkit=Toolkit.getDefaultToolkit();
//		Image icon = toolkit.getImage("img/Popcatopen.jpg");
//        this.setIconImage(icon);
//		
		this.add(jsp,BorderLayout.CENTER);
		this.add(jp,BorderLayout.SOUTH);
		
		this.setTitle("�ͻ���������");
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
					break;//����Ƿ񱻴��
				//System.out.println(msg);
				}
			} catch(SocketException e) {
				
				System.out.println("�������Ͽ�");
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
