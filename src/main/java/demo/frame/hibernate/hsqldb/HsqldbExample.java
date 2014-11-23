package demo.frame.hibernate.hsqldb;


import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;
import org.hibernate.tool.hbm2ddl.SchemaUpdate;

import demo.frame.hibernate.hsqldb.contacts.Folder;

/**
 * @author JavaOpenSourceExample
 * @version Date: Jun 2, 2012 11:03:16 AM
 */
public class HsqldbExample {

	public static void main(String[] args) throws Exception {

		// load configuration
		Configuration config = new Configuration();
		config.configure("hibernate-HsqldbExample.cfg.xml");
//		config.configure("hibernate.cfg.xml");

		// update database schema if required
		new SchemaUpdate(config).execute(true, true);

		ServiceRegistry serviceRegistry = new ServiceRegistryBuilder().applySettings(config.getProperties()).buildServiceRegistry();        
		// open a session
		SessionFactory sessionFactory = config.buildSessionFactory(serviceRegistry);
		
		Session session = sessionFactory.openSession();

		Folder myFolder = new Folder();
		myFolder.setFolderName("My Folder");

		Folder myPrivateFolder = new Folder();
		myPrivateFolder.setFolderName("My Private Folder");

		myFolder.addFolder(myPrivateFolder);

		System.out
				.println("add a folder with a nested owned folder to database...");
		Transaction transaction= session.beginTransaction();
		try {
			session.saveOrUpdate(myFolder);

			session.flush();
			transaction.commit();
		} catch (Exception e) {
			transaction.rollback();
		}

		System.out.println("find all folders...");
		Transaction searchTransaction = session.beginTransaction();
		try {
//			List folders = session.find("from java.lang.Object");
//			session.flush();
//			transaction.commit();
//
//			for (int i = 0; i < folders.size(); i++) {
//				Folder folder = (Folder) folders.get(i);
//				System.out.println("folder:" + folder);
//			}
		} catch (Exception e) {
			e.printStackTrace();
			searchTransaction.rollback();
		}

		// close the session
		session.close();
		sessionFactory.close();

	}
}
