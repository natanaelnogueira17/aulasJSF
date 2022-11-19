package br.com.framework.hibernate.session;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;

import javax.activation.DataSource;
import javax.faces.bean.ApplicationScoped;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.engine.SessionFactoryImplementor;

import br.com.framework.implementacao.crud.VariavelConexaoUtil;

/**
 * responsavel por estabelecer conexao com hibernate
 * @author IBYTE PC
 *
 */
@ApplicationScoped
public class HibernatilUtil implements Serializable{

	
	private static final long serialVersionUID = 1L;
	
	public static String JAVA_COMP_ENV_JDBC_DATA_SOURCE = "java:/comp/env/jdbc/datasource";
	
	private static SessionFactory sessionFactory = buildSessionFactory();
	/**
	 * respponsavel por ler o arquivo de configurações hibernate.cfg.xml
	 * @return
	 */
	private static SessionFactory buildSessionFactory() {
		try {
			if(sessionFactory == null) {
				sessionFactory = new Configuration().configure().buildSessionFactory();
			}
			return sessionFactory;
		} catch (Exception e) {
			e.printStackTrace();
			throw new ExceptionInInitializerError("Erro ao criar conexao SessionFactory");
		}
	}
	
	/**
	 * retorna o SessionFactory corrente 
	 * @return
	 */
	public static SessionFactory getSessionFactory() {
		return sessionFactory;
	}
	
	
	/**
	 * retorna a sessao do sessionfactory
	 * @return
	 */
	public static Session getCurrentSession() {
		return getSessionFactory().getCurrentSession();
	}
	
	
	/**
	 * abre uma noc]va sessao no sessionfactory
	 */
	public static Session openSession() {
		if(sessionFactory==null) {
			buildSessionFactory();
		}
		return sessionFactory.openSession();
	}
	
	public static Connection getConnectionProvider() throws SQLException{
		return ((SessionFactoryImplementor)sessionFactory).getConnectionProvider().getConnection();
	}
	/**
	 * 
	 * @return Connection no InitialContext java:/comp/env/jdbc/datasource
	 * @throws Exception
	 */
	public static Connection getConnection() throws Exception{
		InitialContext context = new InitialContext();
		DataSource ds = (DataSource) context.lookup(JAVA_COMP_ENV_JDBC_DATA_SOURCE);
		return  ((HibernatilUtil) ds).getConnection();
	}
	
	public DataSource getDataSourceJndi() throws NamingException{
		InitialContext context = new InitialContext();
		return (DataSource)context.lookup(VariavelConexaoUtil.JAVA_COMP_ENV_JDBC_DATA_SOURCE);
	}
	
	
	
}
