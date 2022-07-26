package br.inf.carlos.manager;

import java.util.List;

import br.inf.carlos.bean.Movie;
import br.inf.carlos.bean.MovieDtoRes;
import br.inf.carlos.exception.DataImportException;
import br.inf.carlos.exception.DatabaseException;

public interface MovieManager {

	MovieDtoRes obterProdutoresComPremiacaoMaisRapida() throws DatabaseException;
	
	List<MovieDtoRes> obterProdutoresPorIntervaloTempo() throws DatabaseException;
	
	List<Movie> listarTodos() throws DatabaseException;
	
	void insertMovies(List<Movie> movies) throws DataImportException;
}
