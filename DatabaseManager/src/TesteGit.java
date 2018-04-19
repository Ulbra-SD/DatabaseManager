
public class TesteGit {

	public static void main(String[] args) throws InterruptedException {

		long cacheTimeout = 10002;
		long millis = System.currentTimeMillis();
		
		Thread.sleep(10000);
		
		long millis2 = System.currentTimeMillis();
		
		if((millis2 - millis) >= cacheTimeout) {
			System.out.println("Primeiro: " + millis);
			System.out.println("Atual: " + millis2);
			System.out.println("Cache expirou!!");
		} else {
			System.out.println("Primeiro: " + millis);
			System.out.println("Atual: " + millis2);
			System.out.println("Cache ainda válido!");
		}
	}

}
