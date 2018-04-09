package cache_server;

import java.io.*;
import java.net.*;

public class Cache {

	public static void main(String[] args) {

		try {
			ServerSocket server = new ServerSocket(1234);
			System.out.println("Servidor Cache aguardando conexão...");
			try {
				Socket client = server.accept();
				System.out.println("Conexão estabelecida!");
				BufferedReader entrada = new BufferedReader(new InputStreamReader(client.getInputStream()));
				PrintStream saida = new PrintStream(client.getOutputStream());
				String requisicao;

				while (true) {

				}
			} catch (Exception e) {
				// TODO: handle exception
			}
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

}
