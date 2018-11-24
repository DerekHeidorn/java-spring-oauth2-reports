package com.example.demo.persist;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;

import com.example.demo.models.TbConfig;

@Repository
public class CommonDao extends JdbcDaoSupport {
	protected final transient Log logger = LogFactory.getLog(getClass());	
	
	@PersistenceContext 
	private EntityManager em;

    @Autowired
    public void setDs(DataSource dataSource) {
         setDataSource(dataSource);
    }
	
	public TbConfig getTbConfigParamByKey(String key) {
		TbConfig config = em.find(TbConfig.class, key.toLowerCase());

    	return config;
	}
	
	public String getConfigParamValue(String key){

		/* If there is no overriding value, look for value
		 * in database
		 */
		TbConfig tbConfig = getTbConfigParamByKey(key);
		if (tbConfig != null) {
			String value = tbConfig.getValue();

			return value.trim();
		} else {
			return null;
		}
		
	}

	public EntityManager getEm() {
		return em;
	}

	public void setEm(EntityManager em) {
		this.em = em;
	}
}
