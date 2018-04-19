package cache_server;

import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

import com.google.gson.Gson;

public class AtendenteCache extends Thread {

	public Socket client;
	public Socket clientSGBD;
	public Cache server;
	public Scanner entrada;
	public Scanner entradaSGBD;
	public PrintWriter saida;
	public PrintStream saidaSGBD;
	public boolean shouldRun = true;
	public Gson gson = new Gson();

	public AtendenteCache(Socket socket, Cache server) throws Exception {
		super("CacheConnectionThread");
		this.client = socket;
		this.server = server;
	}
	
	public void run() {
		try {
			entrada = new Scanner(client.getInputStream());
			saida = new PrintWriter(client.getOutputStream());
			
			while (shouldRun) {
				while (!entrada.hasNextLine()) {
					try {
						Thread.sleep(1);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				
				clientSGBD = new Socket("localhost", 1235);
				entradaSGBD = new Scanner(clientSGBD.getInputStream());
				saidaSGBD = new PrintStream(clientSGBD.getOutputStream());
				
				String requisicao = entrada.nextLine();
				saidaSGBD.println(requisicao);
				
				String resposta = entradaSGBD.nextLine();
				
				saida.println(resposta);
				
				saidaSGBD.flush();
				entradaSGBD.close();
				saidaSGBD.close();
				clientSGBD.close();
				
				saida.flush();
			}
			
			entrada.close();
			saida.close();
			client.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
