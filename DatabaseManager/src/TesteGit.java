import java.sql.Timestamp;

public class TesteGit {

	public static void main(String[] args) throws Exception {

		Timestamp data = new Timestamp(System.currentTimeMillis());
		System.out.println("Data: " + data);
		
		Timestamp data2 = new Timestamp(System.currentTimeMillis());
		System.out.println("Data: " + data2);
		
		
		if (data.before(data2)) {
			System.out.println("A data 1 é anterior à data 2");
		}
	}

}
