package br.inf.carlos.migration;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import br.inf.carlos.bean.Movie;
import br.inf.carlos.db.ConnectionComponent;
import br.inf.carlos.exception.DataImportException;
import br.inf.carlos.exception.DatabaseConnectionException;
import br.inf.carlos.exception.DatabaseException;
import br.inf.carlos.manager.MovieManager;
import br.inf.carlos.util.CSVService;

@Component
public class CreateDatabaseComponent {
	
	@Autowired
    private MovieManager mm;
	
	@Autowired
	private ConnectionComponent connection;
	
	@Autowired
	private CSVService csvService;
	
	@PostConstruct
	public void createDatabaseSchema() throws DatabaseException{
		System.out.println("CreateDatabaseComponent.createDatabaseSchema()");
		
		try {			
			Statement st = this.connection.getConnection().createStatement();
			
			st.execute("CREATE TABLE tb_movies ( "
					+ "  id INT AUTO_INCREMENT  PRIMARY KEY, "
					+ "  `year` INT NOT NULL, "
					+ "  title VARCHAR(350) NOT NULL, "
					+ "  studios VARCHAR(350) NOT NULL, "
					+ "  producers VARCHAR(350) NOT NULL, "
					+ "  winner varchar(3) "
					+ ");");
			
			
			List<Movie> movies = csvService.readCSVFile(ResourceUtils.getFile("classpath:movielist.csv"));
			
			mm.insertMovies(movies);
			
		} catch (SQLException e) {
			throw new DatabaseException(e);
		} catch (DataImportException e) {
			e.printStackTrace();
			throw new DatabaseException(e);
		} catch (IOException e) {
			e.printStackTrace();
			throw new DatabaseException(e);
		} catch (DatabaseConnectionException e) {
			e.printStackTrace();
			throw new DatabaseException(e);
		}
	}
	
	/*
	private Movie read(ResultSet rset) throws SQLException {
		
		Movie movie = new Movie();
		
		movie.setYear(rset.getInt("year"));
		movie.setTitle(rset.getString("title"));
		movie.setStudios(rset.getString("studios"));
		movie.setProducers(rset.getString("producers"));
		movie.setWinner(rset.getString("winner"));
		
		return movie;
	}
	*/
}
