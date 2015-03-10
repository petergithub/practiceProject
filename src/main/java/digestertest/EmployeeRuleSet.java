package digestertest;

import org.apache.commons.digester.Digester;
import org.apache.commons.digester.RuleSetBase;

public class EmployeeRuleSet extends RuleSetBase {
	public void addRuleInstances(Digester digester) {
		// add rules
		digester.addObjectCreate("employee", "digestertest.Employee");
		digester.addSetProperties("employee");
		
		digester.addObjectCreate("employee/office", "digestertest.Office");
		digester.addSetProperties("employee/office");
		digester.addSetNext("employee/office", "addOffice");
		
		digester.addObjectCreate("employee/office/address", "digestertest.Address");
		digester.addSetProperties("employee/office/address");
		digester.addSetNext("employee/office/address", "setAddress");
	}
}
