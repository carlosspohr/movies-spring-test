package br.inf.carlos.manager.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.inf.carlos.bean.Movie;
import br.inf.carlos.bean.MovieDtoRes;
import br.inf.carlos.db.ConnectionComponent;
import br.inf.carlos.exception.DataImportException;
import br.inf.carlos.exception.DatabaseConnectionException;
import br.inf.carlos.exception.DatabaseException;
import br.inf.carlos.manager.MovieManager;

@Component
public class MovieManagerImpl implements MovieManager{
	
	@Autowired
	private ConnectionComponent connection;
	
	@Override
	public void insertMovies(List<Movie> movies) throws DataImportException {
		
		String query = "insert into tb_movies(`year`, title, studios, producers, winner) values(?,?,?,?,?)";
		
		Connection conn = null;
		try {
			conn = connection.getConnection();
		} catch (DatabaseConnectionException e) {
			e.printStackTrace();
			throw new DataImportException(e);
		}
		
		for (Movie movie : movies) {
			
			try {
				PreparedStatement ps = conn.prepareStatement(query);
				
				ps.setInt(1, movie.getYear());
				ps.setString(2, movie.getTitle());
				ps.setString(3, movie.getStudios());
				ps.setString(4, movie.getProducers());
				ps.setString(5, movie.getWinner());
				
				ps.execute();
				ps.close();
				
				/*String s = "insert into tb_movies(`year`, title, studios, producers, winner) "
						+ " values('"+movie.getYear()+"',\""+movie.getTitle()+"\",\""+movie.getStudios()+"\",\""+movie.getProducers()+"\",\""+movie.getWinner()+"\");";
				System.out.println(s);*/
			} catch (SQLException e) {
				e.printStackTrace();
				throw new DataImportException(e);
			}
		}
		
		try {
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DataImportException(e);
		}
	}

	@Override
	public MovieDtoRes obterProdutoresComPremiacaoMaisRapida() throws DatabaseException {
		
		String query = "SELECT "
				+ "	t.*, "
				+ "	(t.following_win - t.previous_win) as `interval` "
				+ "FROM "
				+ "	( "
				+ "	select "
				+ "		t.*, "
				+ "		(select x.`year` from tb_movies x where x.producers = t.producers and x.winner = 'yes' and x.`year` > t.previous_win limit 1) as following_win "
				+ "	from "
				+ "		( "
				+ "		select "
				+ "			m.producers, "
				+ "			count(m.id) qtde, "
				+ "			min(m.`year`) as previous_win "
				+ "		from "
				+ "			tb_movies m "
				+ "		where "
				+ "			1=1 "
				+ "			and m.winner = 'yes' "
				+ "		group BY "
				+ "			m.producers "
				+ "		having  "
				+ "			qtde >= 2 "
				+ "		) t "
				+ "	) t "
				+ "order by "
				+ "	(t.following_win - t.previous_win) ASC limit 1";
		
		try {
			Connection conn = connection.getConnection();
			Statement stmt = conn.createStatement();
			ResultSet rset = stmt.executeQuery(query);
			
			MovieDtoRes dto = new MovieDtoRes();
			
			if(rset.next()) {
				dto.setProducer(rset.getString("producers"));
				dto.setInterval(rset.getInt("interval"));
				dto.setPreviousWin(rset.getInt("previous_win"));
				dto.setFollowingWin(rset.getInt("following_win"));
			}
			
			rset.close();
			stmt.close();
			conn.close();
			
			return dto;
		} catch (SQLException | DatabaseConnectionException e) {
			e.printStackTrace();
			throw new DatabaseException(e);
		}
	}

	@Override
	public List<Movie> listarTodos() throws DatabaseException {
		try {
			List<Movie> movies = new ArrayList<Movie>(0);
			
			Connection conn = connection.getConnection();
			
			Statement stmt = conn.createStatement();
			ResultSet rset = stmt.executeQuery("select * from tb_movies");
			
			while(rset.next()) {
				Movie movie = new Movie();
				
				movie.setYear(rset.getInt("year"));
				movie.setTitle(rset.getString("title"));
				movie.setStudios(rset.getString("studios"));
				movie.setProducers(rset.getString("producers"));
				movie.setWinner(rset.getString("winner"));
				
				movies.add(movie);
			}
			
			rset.close();
			stmt.close();
			conn.close();
			
			return movies;
		} catch (SQLException | DatabaseConnectionException e) {
			e.printStackTrace();
			throw new DatabaseException(e);
		}
	}

	@Override
	public List<MovieDtoRes> obterProdutoresPorIntervaloTempo() throws DatabaseException {
		
		String query = "( "
				+ "SELECT "
				+ "	'MAIOR INTERVALO' AS consulta, "
				+ "	t.PRODUCERS, "
				+ "	t.`interval`, "
				+ "	t.previous_win, "
				+ "	t.following_win "
				+ "FROM "
				+ "	( "
				+ "	SELECT "
				+ "		t.*, "
				+ "		(t.following_win - t.previous_win) AS `interval` "
				+ "	FROM "
				+ "		( "
				+ "		select "
				+ "			m.title, "
				+ "			m.PRODUCERS, "
				+ "			m.`year` AS previous_win, "
				+ "			(SELECT x.`year` FROM tb_movies x WHERE x.PRODUCERS = m.PRODUCERS AND x.`year` > m.`year` LIMIT 1) following_win "
				+ "		from "
				+ "			tb_movies m "
				+ "		where "
				+ "			1=1 "
				+ "			and m.winner = 'yes' "
				+ "		ORDER BY "
				+ "			m.`year` asc "
				+ "		) t "
				+ "	WHERE "
				+ "		t.following_win IS NOT null "
				+ "	) t "
				+ "ORDER BY "
				+ "	t.`interval` DESC LIMIT 1 "
				+ ") "
				+ "UNION all "
				+ "(SELECT "
				+ "	'OBTEVE MAIS RAPIDO' AS consulta, "
				+ "	t.PRODUCERS, "
				+ "	(t.following_win - t.previous_win) as `interval`, "
				+ "	t.previous_win, "
				+ "	t.following_win "
				+ "FROM "
				+ "	( "
				+ "	select "
				+ "		t.*, "
				+ "		(select x.`year` from tb_movies x where x.producers = t.producers and x.winner = 'yes' and x.`year` > t.previous_win limit 1) as following_win "
				+ "	from "
				+ "		( "
				+ "		select "
				+ "			m.producers, "
				+ "			count(m.id) qtde, "
				+ "			min(m.`year`) as previous_win "
				+ "		from "
				+ "			tb_movies m "
				+ "		where "
				+ "			1=1 "
				+ "			and m.winner = 'yes' "
				+ "		group BY "
				+ "			m.producers "
				+ "		having  "
				+ "			qtde >= 2 "
				+ "		) t "
				+ "	) t "
				+ "order by "
				+ "	(t.following_win - t.previous_win) ASC limit 1) "
				+ ";";
		
		try {
			List<MovieDtoRes> producers = new ArrayList<MovieDtoRes>(0);
			
			Connection conn = connection.getConnection();
			
			Statement stmt = conn.createStatement();
			ResultSet rset = stmt.executeQuery(query);
			
			while(rset.next()) {
				MovieDtoRes dto = new MovieDtoRes();
				
				dto.setProducer(rset.getString("producers"));
				dto.setInterval(rset.getInt("interval"));
				dto.setPreviousWin(rset.getInt("previous_win"));
				dto.setFollowingWin(rset.getInt("following_win"));
				
				producers.add(dto);
			}
			
			rset.close();
			stmt.close();
			conn.close();
			
			return producers;
		} catch (SQLException | DatabaseConnectionException e) {
			e.printStackTrace();
			throw new DatabaseException(e);
		}
	}

}
