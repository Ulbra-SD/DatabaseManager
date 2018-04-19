package cache_server;

import java.net.*;

public class Cache {
	
	ServerSocket server;
	boolean shouldRun = true;

	public static void main(String[] args) {
		
		new Cache();
		
	}
	
	public Cache() {
		try {
			server = new ServerSocket(1234);
			System.out.println("CacheServer aguardando conexão...");
			while (shouldRun) {
				Socket client = server.accept();
				System.out.println("Conexão estabelecida com " + client.getInetAddress().getHostName() + " na porta " + client.getPort());
				AtendenteCache atendente = new AtendenteCache(client, this);
				atendente.start();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}