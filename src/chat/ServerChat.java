package chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

public class ServerChat {

	public static void main(String[] args) {
		List<ClientProxy> clientlist = Collections.synchronizedList( new ArrayList<ClientProxy>());
		ServerSocket serverSocket = null;
		// TODO Auto-generated method stub
		try {
			serverSocket = new ServerSocket(8888);
			while (true) {
				System.out.println("������������,�ȴ��ͻ�����");
				Socket socket = serverSocket.accept();
				ClientProxy client = new ClientProxy(socket, clientlist);

				new Thread(client).start();
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {

			try {
				if (serverSocket != null)// �ж�serverSocket�Ƿ�ر�
					serverSocket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

}

class ClientProxy implements Runnable {
	private Socket socket;
	private String socketInfo;
	private String nickname; 
	BufferedReader reader;
	PrintWriter writer;
	List<ClientProxy> clientlist;
	
	

	public ClientProxy(Socket socket, List<ClientProxy> clientlist) {
		this.socket = socket;
		this.clientlist = clientlist;
		clientlist.add(this);
		socketInfo = socket.getInetAddress().getHostAddress() + ":" + socket.getPort();// ������ӷ�������IP��ַ�Ͷ˿�
		nickname="�������û�"+clientlist.size()+"��";
		System.out.println(socketInfo + nickname + "���ӵ�������");
		try {
			reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			writer = new PrintWriter(socket.getOutputStream());
		} catch (IOException e) {

			e.printStackTrace();
		}
		broadcast(nickname + "����������");
	}

	private void broadcast(String msg) {
		synchronized(clientlist) {
		for (ClientProxy client : clientlist) {
			client.writer.println(msg);
			client.writer.flush();//ˢ�»�������ֹ��Ϣ�޷����ͳ�ȥ
		}
		
		}
	}

	@Override
	public void run() {
		try {
			while (true) {
				String msg = reader.readLine();
				System.out.println(socketInfo + nickname + msg);
				if ("quit".equals(msg)) {
					break;//�˳���Ͽ�ѭ��
				}
				else if(msg.startsWith("nickname:")){
					String oldNickname = nickname;
					nickname = "��" + msg.substring("nickname:".length(),
							msg.length() > "nickname:".length() + 8?"nickname:".length() + 8: msg.length()) + "��";
					broadcast(oldNickname + "�޸��ǳ�Ϊ:" + nickname);

					
				}else {
					broadcast(nickname + msg);// �㲥��Ϣ
				}
				
			}
			broadcast(nickname + "��������");
			clientlist.remove(this);
			broadcast("�����һ�ʣ" + clientlist.size() + "��");
			System.out.println(socketInfo + nickname + "�Ͽ�����");
		}catch(SocketException e) {//�ͻ��˲�δquit�˳�����
			broadcast(nickname + "��������");
			clientlist.remove(this);
			broadcast("�����һ�ʣ" + clientlist.size() + "��");
			System.out.println(socketInfo + nickname + "�Ͽ�����");
		}
		catch (IOException e) {
			e.printStackTrace();
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
				if (socket != null)
					socket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
