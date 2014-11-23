package demo.frame.hibernate;

import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;

/**
 * HibernateBase
 * 
 * @author woody
 */
public abstract class HibernateBase {
	protected static SessionFactory sessionFactory;
	protected static Logger logger = Logger.getLogger(HibernateBase.class);
	protected static ThreadLocal<Session> session = new ThreadLocal<Session>();
	protected static ThreadLocal<Transaction> transaction = new ThreadLocal<Transaction>();
	protected static Boolean isStarted = Boolean.FALSE;
	protected static Set<Session> sessionSet = new HashSet<Session>();

	protected static void initHibernate() throws HibernateException {
		synchronized (isStarted) {
			if (isStarted.equals(Boolean.FALSE)) {
				Configuration config = new Configuration();
				ServiceRegistry serviceRegistry = new ServiceRegistryBuilder().applySettings(config.getProperties()).buildServiceRegistry();        
				// open a session
				sessionFactory = config.buildSessionFactory(serviceRegistry);
				isStarted = Boolean.TRUE;
			}
		}
	}


	public static void beginTransaction() throws HibernateException {
		try {
			Session s = currentSession();
			Transaction t = s.beginTransaction();
			transaction.set(t);
		} catch (HibernateException e) {
			logger.error(e);
			throw e;
		}
	}

	/**
	 * end current transaction
	 * @throws HibernateException
	 */
	public static void endTransaction() throws HibernateException {
		Transaction t = transaction.get();
		Session s = session.get();
		if (t != null) {
			try {
				t.commit();
			} catch (HibernateException e) {
				logger.error("error in endTransaction", e);
				try {
					t.rollback();
				} catch (HibernateException e2) {
					logger.error("error in endTransaction", e2);
					throw e2;
				}
				throw e;
			} finally {
				if (s != null) s.close();
				session.set(null);
				transaction.set(null);
			}
		}
	}

	public static void abortTransaction() throws HibernateException {
		Transaction t = transaction.get();
		if (t != null) {
			try {
				t.rollback();
			} catch (HibernateException e) {
				throw e;
			} finally {
				transaction.set(null);
			}
		}
	}

	public static Session currentSession() throws HibernateException {
		Session s = session.get();
		if (s == null) {
			if (sessionFactory == null) {
				initHibernate();
			}
			s = sessionFactory.openSession();
			session.set(s);

			logger.debug(s + " is opened.");
			sessionSet.add(s);
		}
		return s;
	}

	/**
	 * Close current session.
	 */
	public static void closeSession() {
		Session s = session.get();
		if (s == null) {
			logger.debug("close a null session?");
			return;
		}
		try {
			s.flush();
		} catch (Exception e) {
			logger.error(e);
		}
		try {
			s.close();
			sessionSet.remove(s);
		} catch (Exception e) {
			logger.error(e);
		}
		session.set(null);
	}

	/**
	 * Close current session.It does not exec flush action.
	 */
	public static void closeSessionWithNoFlush() {
		Session s = session.get();
		if (s == null) {
			logger.debug("close a null session?");
			return;
		}
		try {
			s.close();
			sessionSet.remove(s);
		} catch (Exception e) {
			logger.error(e);
		}
		session.set(null);
	}

}
