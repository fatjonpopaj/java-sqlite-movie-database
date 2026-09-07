package de.hsh.dbs2.imdb.logic;

import java.util.ArrayList;
import java.util.List;

import de.hsh.dbs2.imdb.ar.Genre;
import de.hsh.dbs2.imdb.ar.Movie;
import de.hsh.dbs2.imdb.ar.MovieCharacter;
import de.hsh.dbs2.imdb.ar.MovieGenre;
import de.hsh.dbs2.imdb.ar.Person;
import de.hsh.dbs2.imdb.logic.dto.*;
import de.hsh.dbs2.imdb.other.ConnectionManager;

public class MovieManager {

	/**
	 * Ermittelt alle Filme, deren Filmtitel den Suchstring enthaelt.
	 * Wenn der String leer ist, sollen alle Filme zurueckgegeben werden.
	 * Der Suchstring soll ohne Ruecksicht auf Gross/Kleinschreibung verarbeitet werden.
	 * @param search Suchstring. 
	 * @return Liste aller passenden Filme als MovieDTO
	 * @throws Exception
	 */
	public List<MovieDTO> getMovieList(String search) throws Exception {

		List<MovieDTO> movies = new ArrayList<>();
		
        boolean ok = false;

        try {
            
            for (Movie movie : Movie.findByTitle(search)) {
            	movies.add(movie.toDTO());
			}
            
            ConnectionManager.getConnection().commit();
            
            ok = true;
            
        } finally {
        	
            if (!ok) {
            	
            	ConnectionManager.getConnection().rollback();
            }
        }

        return movies;
    }

	/**
	 * Speichert die uebergebene Version des Films neu in der Datenbank oder aktualisiert den
	 * existierenden Film.
	 * Dazu werden die Daten des Films selbst (Titel, Jahr, Typ) beruecksichtigt,
	 * aber auch alle Genres, die dem Film zugeordnet sind und die Liste der Charaktere
	 * auf den neuen Stand gebracht.
	 * @param movie Film-Objekt mit Genres und Charakteren.
	 * @throws Exception
	 */
	public void insertUpdateMovie(MovieDTO movieDTO) throws Exception {
		
	    boolean ok = false;

	    Movie movie;
	    
	    try {

	        if (movieDTO.getId() == null) {
	        	
	        	movie = new Movie();
	        	movie.setTitle(movieDTO.getTitle());
	        	movie.setType(movieDTO.getType().charAt(0));
	        	movie.setYear(movieDTO.getYear());
	        	
	            movie.insert();
	            
	            for (String genre : movieDTO.getGenres()) {
					
	            	MovieGenre movieGenre = new MovieGenre();
	            	
	            	movieGenre.setMovieID(movie.getId());
	            	movieGenre.setGenreID(Genre.findByGenre(genre).get(0).getID());
	            	movieGenre.insert();
				}
	            
	            for (int i = 0; i < movieDTO.getCharacters().size() ; i++) {
					
	            	CharacterDTO character = movieDTO.getCharacters().get(i);
	            	MovieCharacter movieCharacter = new MovieCharacter();
	            	
	            	movieCharacter.setCharacter(character.getCharacter());
	            	movieCharacter.setAlias(character.getAlias()); 	
	            	movieCharacter.setPosition(i+1);
	            	movieCharacter.setMovieID(movie.getId());
	            	movieCharacter.setPersonID(Person.findByName(character.getPlayer()).get(0).getId());
	            	movieCharacter.insert();
				}
	            
	        } else {

		        movie = Movie.findById(movieDTO.getId());

	            movie.update();
	            
	            for (MovieGenre movieGenre : MovieGenre.findByMovieID(movieDTO.getId())) {
	            	
	            	movieGenre.delete();
	            }
	            
	            for (MovieCharacter movieCharacter : MovieCharacter.findByMovieID(movieDTO.getId())) {
	            	
	            	movieCharacter.delete();
	            }
	            
	            for (String genre : movieDTO.getGenres()) {
					
	            	MovieGenre movieGenre = new MovieGenre();
	            	
	            	movieGenre.setMovieID(movie.getId());
	            	movieGenre.setGenreID(Genre.findByGenre(genre).get(0).getID());
	            	movieGenre.insert();
				}
	            
	            for (int i = 0; i < movieDTO.getCharacters().size() ; i++) {
					
	            	CharacterDTO character = movieDTO.getCharacters().get(i);
	            	MovieCharacter movieCharacter = new MovieCharacter();
	            	
	            	movieCharacter.setCharacter(character.getCharacter());
	            	movieCharacter.setAlias(character.getAlias()); 	
	            	movieCharacter.setPosition(i+1);
	            	movieCharacter.setMovieID(movie.getId());
	            	movieCharacter.setPersonID(Person.findByName(character.getPlayer()).get(0).getId());
	            	movieCharacter.insert();
				}
	            
	        }

	        ConnectionManager.getConnection().commit();
	        
	        ok = true;
	        
	    } finally {
	    	
	        if (!ok) {
	        	
	        	ConnectionManager.getConnection().rollback();
	        }
	    }
	}

	/**
	 * Loescht einen Film aus der Datenbank. Es werden auch alle abhaengigen Objekte geloescht,
	 * d.h. alle Charaktere und alle Genre-Zuordnungen.
	 * @param movie
	 * @throws Exception
	 */
	public void deleteMovie(long movieId) throws Exception {
				
	    boolean ok = false;

	    try {
		
	    	for (MovieCharacter movieCharacter : MovieCharacter.findByMovieID(movieId)) {
			
	    		movieCharacter.delete();
	    	}
		
	    	for (MovieGenre movieGenre : MovieGenre.findByMovieID(movieId)) {
			
	    		movieGenre.delete();
	    	}

	    	Movie.findById(movieId).delete();
	    	
	    	ConnectionManager.getConnection().commit();
	        
	        ok = true;
        
	    } finally {
    	
	    	if (!ok) {
        	
	    		ConnectionManager.getConnection().rollback();
	    	}
	    }    
	}

	/**
	 * Liefert die Daten eines einzelnen Movies zurück
	 * @param movieId
	 * @return
	 * @throws Exception
	 */
	public MovieDTO getMovie(long movieId) throws Exception {
		
		MovieDTO movieDTO;
		
		boolean ok = false;
		
	    try {
	    	
	    	movieDTO = Movie.findById(movieId).toDTO();
	    	
	    	ConnectionManager.getConnection().commit();
	        
	        ok = true;
		
	    } finally {
    	
    		if (!ok) {
    	
    			ConnectionManager.getConnection().rollback();
    		}
    	} 
	    
	    return movieDTO;
	}
}
