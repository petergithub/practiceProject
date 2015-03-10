package digestertest;

import java.io.File;

import org.apache.commons.digester.Digester;

public class Test01 {
	String path2 = getClass().getResource("/").getPath();
	
	public static void main(String[] args) {
		System.out.println(new Test01().path2);
		String path = ClassLoader.getSystemResource("").getPath()
				+ new Test01().getClass().getPackage().getName().replace(".", File.separator);
		path = "C:/sp/Dropbox/base/testProject/src/main/webapp/WEB-INF/classes/digestertest";
		File file = new File(path, "employee1.xml");
		Digester digester = new Digester();
		// add rules
		digester.addObjectCreate("employee", "digestertest.Employee");
		digester.addSetProperties("employee");
		digester.addCallMethod("employee", "printName");

		try {
			Employee employee = (Employee) digester.parse(file);
			System.out.println("First name : " + employee.getFirstName());
			System.out.println("Last name : " + employee.getLastName());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
