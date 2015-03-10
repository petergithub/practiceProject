package digestertest;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.commons.digester.Digester;

public class Test03 {

	public static void main(String[] args) {
		String path = ClassLoader.getSystemResource("").getPath()
				+ new Test03().getClass().getPackage().getName().replace(".", File.separator);
		path = "C:/sp/Dropbox/base/testProject/src/main/webapp/WEB-INF/classes/digestertest";
		File file = new File(path, "employee2.xml");
		Digester digester = new Digester();
		digester.addRuleSet(new EmployeeRuleSet());
		try {
			Employee employee = (Employee) digester.parse(file);
			ArrayList<?> offices = employee.getOffices();
			Iterator<?> iterator = offices.iterator();
			System.out.println("-------------------------------------------------");
			while (iterator.hasNext()) {
				Office office = (Office) iterator.next();
				Address address = office.getAddress();
				System.out.println(office.getDescription());
				System.out
						.println("Address : " + address.getStreetNumber() + " " + address.getStreetName());
				System.out.println("--------------------------------");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
