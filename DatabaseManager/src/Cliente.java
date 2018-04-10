import java.util.*;
import java.net.*;
import java.io.*;

public class Cliente {

	public static void main(String[] args) {
		try {
			Socket client = new Socket("localhost", 1234);
			BufferedReader entrada = new BufferedReader(new InputStreamReader(client.getInputStream()));
			PrintStream saida = new PrintStream(client.getOutputStream());

			Scanner teclado = new Scanner(System.in);
			System.out.println("===== Digite as requisições conforme o protocolo: =====");
			while (true) {
				System.out.print(">> ");
				String requisicao = teclado.nextLine();

				if ("FIM".equals(requisicao)) {
					break;
				}
				
				saida.println(requisicao);
				String resposta = entrada.readLine();

				System.out.println(resposta);
			}
			entrada.close();
			saida.close();
			client.close();
			teclado.close();
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

}
