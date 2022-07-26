package br.inf.carlos.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import br.inf.carlos.bean.Movie;
import br.inf.carlos.bean.MovieDtoRes;
import br.inf.carlos.manager.MovieManager;

@RestController
public class MovieController {

	@Autowired
    private MovieManager mm;
	
	@GetMapping("/produtores")
	public List<MovieDtoRes> getProdutores() throws Exception{
		return mm.obterProdutoresPorIntervaloTempo();
	}
	
	@GetMapping("/movies")
	public List<Movie> getAllMovies() throws Exception{
		return mm.listarTodos();
	}
}
