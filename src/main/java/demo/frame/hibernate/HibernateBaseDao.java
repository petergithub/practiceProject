package demo.frame.hibernate;


import java.util.List;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.util.Assert;

import demo.web.pageutil.ICriteria;


/**
 * This class serves as the Base class for all other Daos - namely to hold
 * common methods that they might all use. Can be used for standard CRUD
 * operations.</p>
 * 
 * @author lidnux
 */
public abstract class HibernateBaseDao extends HibernateDaoSupport {

	protected final Logger log = Logger.getLogger(getClass());

	protected Object getObjectById(Integer id) {
		Class<? extends Object> clz = getEntityClass();
		Object obj = getHibernateTemplate().get(clz, id);
		return obj;
	}

	protected org.hibernate.Criteria getEntityCriteria() {
		Class<?> clz = getEntityClass();
		return getSession().createCriteria(clz);
	}

	protected List<?> getAll(Class<?> clz) {
		return getHibernateTemplate().loadAll(clz);
	}

	public void remove(Object o) {
		getHibernateTemplate().delete(o);
	}

	public void save(Object o) {
		getHibernateTemplate().saveOrUpdate(o);
	}

	public List<?> findBy(Class<?> clz, String name, Object value) {
		Assert.hasText(name);
		Criteria criteria = getSession().createCriteria(clz);
		criteria.add(Restrictions.eq(name, value));
		return criteria.list();
	}

	public List<?> findByLike(Class<?> clz, String name, String value) {
		Assert.hasText(name);
		Criteria criteria = getSession().createCriteria(clz);
		criteria.add(Restrictions.like(name, value, MatchMode.ANYWHERE));
		return criteria.list();
	}

	@SuppressWarnings("rawtypes")
	public List<?> executeSQLFind(final String sql, final String[] alias,
			final Class[] clasz) {
		Session session = getHibernateTemplate().getSessionFactory()
				.openSession();
		SQLQuery realQuery = session.createSQLQuery(sql);
		for (int i = 0; i < alias.length; i++) {
			realQuery.addEntity(alias[i], clasz[i]);
		}
		List<?> dataList = realQuery.list();
		return dataList;
	}

	public int getObjectCount(ICriteria condition) {
		Criteria criteria = getSession().createCriteria(getEntityClass());
		renderCriteria(criteria, condition);
		int totalCount = ((Integer) criteria.setProjection(
				Projections.rowCount()).uniqueResult()).intValue();
		return totalCount;
	}

	public List<?> getObjects(ICriteria condition) {
		Criteria criteria = getSession().createCriteria(getEntityClass());
		renderCriteria(criteria, condition);
		if (condition.getPageInfo() != null) {
			int pageSize = condition.getPageInfo().getPageSize();
			int curPage = condition.getPageInfo().getCurPageNo();
			return criteria.setFirstResult((curPage - 1) * pageSize)
					.setMaxResults(pageSize).list();
		}
		return criteria.list();
	}

	public boolean isNotUnique(Object entity, String names) {
		Assert.hasText(names);
		Criteria criteria = getSession().createCriteria(entity.getClass())
				.setProjection(Projections.rowCount());
		String[] nameList = names.split(",");
		try {
			for (int i = 0; i < nameList.length; i++) {
				String name = nameList[i];
				criteria.add(Restrictions.eq(name,
						PropertyUtils.getProperty(entity, name)));
			}

			String keyName = getSessionFactory().getClassMetadata(
					entity.getClass()).getIdentifierPropertyName();
			if (keyName != null) {
				Object id = PropertyUtils.getProperty(entity, keyName);
				if (id != null)
					criteria.add(Restrictions.not(Restrictions.eq(keyName, id)));
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			return false;
		}
		return ((Integer) criteria.uniqueResult()).intValue() > 0;
	}

	public List<?> loadAll() {
		return getHibernateTemplate().loadAll(getEntityClass());
	}

	protected abstract Class<? extends Object> getEntityClass();

	protected abstract void renderCriteria(Criteria criteria,
			ICriteria condition);

}
