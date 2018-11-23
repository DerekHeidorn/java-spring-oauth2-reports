package com.example.demo.persist;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;

import com.example.demo.models.TbGroup;

@Repository
public class ReportDao extends JdbcDaoSupport {
	
	@PersistenceContext 
	private EntityManager em;

    @Autowired
    public void setDs(DataSource dataSource) {
         setDataSource(dataSource);
    }
	
	public List<TbGroup> getTbGroups() {
		
		CriteriaBuilder cb = em.getCriteriaBuilder();

		CriteriaQuery<TbGroup> q = cb.createQuery(TbGroup.class);
		Root<TbGroup> c = q.from(TbGroup.class);
		q.select(c);
		
		TypedQuery<TbGroup> query = em.createQuery(q);
		List<TbGroup> results = query.getResultList();
    	
    	return results;
	}

	public EntityManager getEm() {
		return em;
	}

	public void setEm(EntityManager em) {
		this.em = em;
	}

}
