package br.inf.carlos.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import br.inf.carlos.exception.DatabaseConnectionException;

@Component
public class ConnectionComponent {

	@Autowired
    private Environment environment;
	
	
	public Connection getConnection() throws DatabaseConnectionException{
		
		String url = environment.getProperty("spring.datasource.url");
		String username = environment.getProperty("spring.datasource.username");
		String password = environment.getProperty("spring.datasource.password");
		
		try {
			return DriverManager.getConnection(url, username, password);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DatabaseConnectionException(e);
		}
	}
}
