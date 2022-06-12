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
				System.out.println("服务器已连接,等待客户连接");
				Socket socket = serverSocket.accept();
				ClientProxy client = new ClientProxy(socket, clientlist);

				new Thread(client).start();
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {

			try {
				if (serverSocket != null)// 判断serverSocket是否关闭
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
		socketInfo = socket.getInetAddress().getHostAddress() + ":" + socket.getPort();// 获得连接服务器的IP地址和端口
		nickname="【匿名用户"+clientlist.size()+"】";
		System.out.println(socketInfo + nickname + "连接到服务器");
		try {
			reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			writer = new PrintWriter(socket.getOutputStream());
		} catch (IOException e) {

			e.printStackTrace();
		}
		broadcast(nickname + "进入聊天室");
	}

	private void broadcast(String msg) {
		synchronized(clientlist) {
		for (ClientProxy client : clientlist) {
			client.writer.println(msg);
			client.writer.flush();//刷新缓冲区防止信息无法发送出去
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
					break;//退出后断开循环
				}
				else if(msg.startsWith("nickname:")){
					String oldNickname = nickname;
					nickname = "【" + msg.substring("nickname:".length(),
							msg.length() > "nickname:".length() + 8?"nickname:".length() + 8: msg.length()) + "】";
					broadcast(oldNickname + "修改昵称为:" + nickname);

					
				}else {
					broadcast(nickname + msg);// 广播消息
				}
				
			}
			broadcast(nickname + "结束聊天");
			clientlist.remove(this);
			broadcast("聊天室还剩" + clientlist.size() + "人");
			System.out.println(socketInfo + nickname + "断开连接");
		}catch(SocketException e) {//客户端并未quit退出聊天
			broadcast(nickname + "结束聊天");
			clientlist.remove(this);
			broadcast("聊天室还剩" + clientlist.size() + "人");
			System.out.println(socketInfo + nickname + "断开连接");
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
