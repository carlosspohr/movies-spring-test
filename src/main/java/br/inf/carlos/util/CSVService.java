package br.inf.carlos.util;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;

import br.inf.carlos.bean.Movie;

@Service
public class CSVService {
	
	public List<Movie> readCSVFile(File csvFile) throws IOException{
		
		List<String> lines = FileUtils.readLines(csvFile, Charset.defaultCharset());
		
		List<Movie> movies = new ArrayList<Movie>(0);
		
		for (int i = 1; i < lines.size(); i++) {
			
			String line = lines.get(i);
			
			Movie movie = this.read(line);
			
			movies.add(movie);
		}
		
		System.out.println("CreateDatabaseComponent.extractFromCSV() fetched movies ->" + movies.size());
		return movies;
	}
	
	private Movie read(String line) throws NumberFormatException{
		
		Movie movie = new Movie();
		
		String[] parts = line.split(";");
		
		movie.setYear(Integer.parseInt(parts[0]));
		movie.setTitle(parts[1]);
		movie.setStudios(parts[2]);
		movie.setProducers(parts[3]);
		
		if(parts.length == 5) {			
			movie.setWinner("yes".equals(parts[4]) ? "yes" : "no");
		}else {
			movie.setWinner("no");
		}
		
		return movie;
	}
	
}
