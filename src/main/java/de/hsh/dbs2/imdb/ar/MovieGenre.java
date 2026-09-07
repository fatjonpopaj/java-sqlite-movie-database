package de.hsh.dbs2.imdb.ar;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import de.hsh.dbs2.imdb.other.ConnectionManager;


public class MovieGenre {
	
	private Long id;
	
	private Long movieID;

	private Long genreID;
			
	//Getter and Setter
	
	public Long getId() {
		
		return id;
	}

	public long getMovieID() {
		
		return movieID;
	}

	public void setMovieID(long movieID) {
		
		this.movieID = movieID;
	}

	public long getGenreID() {
		
		return genreID;
	}

	public void setGenreID(long genreID) {
		
		this.genreID = genreID;
	}

	// Insert and Delete Methods
	
	public void insert() throws SQLException {
		
		if (id != null) {
			
			throw new IllegalStateException("Object has already been inserted");
		}
		
		Connection conn = ConnectionManager.getConnection();
		
		String inst1 = "INSERT INTO MovieGenre(MovieID, GenreID) VALUES(?, ?)";
		
		String inst2 = "SELECT last_insert_rowid() AS MovieGenreID";
				
		try (PreparedStatement stmt1 = conn.prepareStatement(inst1)) {
			
			stmt1.setLong(1, movieID);
			stmt1.setLong(2, genreID);
			
			stmt1.executeUpdate();
			
			try (PreparedStatement stmt2 = conn.prepareStatement(inst2)){
			
				try (ResultSet rs = stmt2.executeQuery()) {
					
					rs.next();
					
					id = rs.getLong("MovieGenreID");
				}
			}
		}
	}
	
	public void delete() throws SQLException {
		
		if (id == null) {
			
			throw new IllegalStateException("Object has not been inserted");
		}
		
		Connection conn = ConnectionManager.getConnection();
		
		String inst = "DELETE FROM MovieGenre WHERE MovieGenreID = ? ";
				
		try (PreparedStatement stmt = conn.prepareStatement(inst)) {
			
			stmt.setLong(1, id);
			
			stmt.executeUpdate();
		}
	}
	
    //Static Methods
	
	public static List<MovieGenre> findAll() throws SQLException {
		
		Connection conn = ConnectionManager.getConnection();

		List<MovieGenre> movieGenreList = new ArrayList<>();
				
		String inst = "SELECT MovieGenreID, MovieID, GenreID FROM MovieGenre";
		
		try (PreparedStatement stmt = conn.prepareStatement(inst)) {
						
			try (ResultSet rs = stmt.executeQuery()) {
				
				MovieGenre movieGenre = new MovieGenre();
				
				movieGenre.id = rs.getLong("MovieGenreID");
				movieGenre.setMovieID(rs.getLong("MovieID"));
				movieGenre.setGenreID(rs.getLong("GenreID"));
				
				movieGenreList.add(movieGenre);
			}
		}
		
		return movieGenreList;
	}
	
	public static MovieGenre findById(long id) throws SQLException {
		
		Connection conn = ConnectionManager.getConnection();

		MovieGenre movieGenre = new MovieGenre();
		
		movieGenre.id = id;
		
		String inst = "SELECT MovieID, GenreID FROM MovieGenre WHERE MovieGenreID = ?";
		
		try (PreparedStatement stmt = conn.prepareStatement(inst)) {
			
			stmt.setLong(1, id);
			
			try (ResultSet rs = stmt.executeQuery()) {
				
				if (!rs.next()) {
					
					throw new IllegalArgumentException("Object with ID " + id + " does not exist");
				}
				
				movieGenre.setMovieID(rs.getLong("MovieID"));
				movieGenre.setGenreID(rs.getLong("GenreID"));
			}
		}
		
		return movieGenre;
	}
	 
	public static List<MovieGenre> findByMovieID(long movieID) throws SQLException {
		
		Connection conn = ConnectionManager.getConnection();

		List<MovieGenre> movieGenreList = new ArrayList<>();
				
		String inst = "SELECT MovieGenreID, GenreID FROM MovieGenre WHERE MovieID = ?";
		
		try (PreparedStatement stmt = conn.prepareStatement(inst)) {
			
			stmt.setLong(1, movieID);
			
			try (ResultSet rs = stmt.executeQuery()) {
				
				while (rs.next()) {
					
					MovieGenre movieGenre = new MovieGenre();
					
					movieGenre.id = rs.getLong("MovieGenreID");
					movieGenre.setMovieID(movieID);
					movieGenre.setGenreID(rs.getLong("GenreID"));
					
					movieGenreList.add(movieGenre);
				}
			}
		}
		
		return movieGenreList;
	}
	
	public static List<MovieGenre> findByGenreID(long genreID) throws SQLException {
		
		Connection conn = ConnectionManager.getConnection();

		List<MovieGenre> movieGenreList = new ArrayList<>();
				
		String inst = "SELECT MovieGenreID, MovieID FROM MovieGenre WHERE GenreID = ?";
		
		try (PreparedStatement stmt = conn.prepareStatement(inst)) {
			
			stmt.setLong(1, genreID);
			
			try (ResultSet rs = stmt.executeQuery()) {
				
				while (rs.next()) {
					
					MovieGenre movieGenre = new MovieGenre();
					
					movieGenre.id = rs.getLong("MovieGenreID");
					movieGenre.setMovieID(rs.getLong("MovieID"));
					movieGenre.setGenreID(genreID);
					
					movieGenreList.add(movieGenre);
				}
			}
		}
		
		return movieGenreList;
	}
}