package digestertest;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.commons.digester.Digester;

public class Test02 {

	public static void main(String[] args) {
		String path = ClassLoader.getSystemResource("").getPath()
				+ new Test02().getClass().getPackage().getName().replace(".", File.separator);
		File file = new File(path, "employee2.xml");
		path = "C:/sp/Dropbox/base/testProject/src/main/webapp/WEB-INF/classes/digestertest";
		Digester digester = new Digester();
		// add rules
		digester.addObjectCreate("employee", "digestertest.Employee");
		digester.addSetProperties("employee");
		
		digester.addObjectCreate("employee/office", "digestertest.Office");
		digester.addSetProperties("employee/office");
		digester.addSetNext("employee/office", "addOffice");
		
		digester.addObjectCreate("employee/office/address", "digestertest.Address");
		digester.addSetProperties("employee/office/address");
		digester.addSetNext("employee/office/address", "setAddress");
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
