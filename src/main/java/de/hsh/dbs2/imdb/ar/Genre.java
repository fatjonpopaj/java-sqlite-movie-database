package de.hsh.dbs2.imdb.ar;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import de.hsh.dbs2.imdb.other.ConnectionManager;

public class Genre {

        private Long id;

        private String genre;

    	//Getter and Setter

        public Long getID() { 

            return id;
        }

        public String getGenre() {

            return genre;
        }

        public void setGenre(String genre) {

            this.genre = genre;
        }

    	// Insert, Update and Delete Methods

        public void insert() throws SQLException {

            if (id != null) {

                throw new IllegalStateException("Object has already been inserted");
            }

            Connection conn = ConnectionManager.getConnection();

            String inst1 = "INSERT INTO Genre(Genre) VALUES(?)";

            String inst2 = "SELECT last_insert_rowid() AS GenreID";

            try (PreparedStatement stmt1 = conn.prepareStatement(inst1)) {

                stmt1.setString(1, genre);

                stmt1.executeUpdate();

                try (PreparedStatement stmt2 = conn.prepareStatement(inst2)){

                    try (ResultSet rs = stmt2.executeQuery()) {

                        rs.next();

                        id = rs.getLong("GenreID");
                    }
                }
            }
        }

        public void update() throws SQLException {

            if (id == null) {

                throw new IllegalStateException("Object has not been inserted");
            }

            Connection conn = ConnectionManager.getConnection();

            String inst = "UPDATE Genre SET Genre = ? WHERE GenreID = ?";

            try (PreparedStatement stmt = conn.prepareStatement(inst)) {

                stmt.setString(1, genre);
                stmt.setLong(2, id);

                stmt.executeUpdate();
            }
        }

        public void delete() throws SQLException {

            if (id == null) {

                throw new IllegalStateException("Object has not been inserted");
            }

            Connection conn = ConnectionManager.getConnection();

            String inst = "DELETE FROM Genre WHERE GenreID = ? ";

            try (PreparedStatement stmt = conn.prepareStatement(inst)) {

                stmt.setLong(1, id);

                stmt.executeUpdate();
            }
        }
        
        //Static Methods
        
        public static List<Genre> findAll() throws SQLException {

            Connection conn = ConnectionManager.getConnection();

            List<Genre> genreList = new ArrayList<>();

            String inst = "SELECT GenreID, Genre FROM Genre";

            try (PreparedStatement stmt = conn.prepareStatement(inst)) {

                try (ResultSet rs = stmt.executeQuery()) {

                    while (rs.next()) {

                        Genre genre = new Genre();

                        genre.id = rs.getLong("GenreID");
                        genre.setGenre(rs.getString("Genre"));
                        
                        genreList.add(genre);
                    }
                }
            }

            return genreList;
        }

        public static Genre findById(long id) throws SQLException {

            Connection conn = ConnectionManager.getConnection();

            Genre genre = new Genre();


            String inst = "SELECT Genre FROM Genre WHERE GenreID = ?";

            try (PreparedStatement stmt = conn.prepareStatement(inst)) {

                stmt.setLong(1, id);

                try (ResultSet rs = stmt.executeQuery()) {

                    if (!rs.next()) {

                        throw new IllegalArgumentException("Object with ID " + id + " does not exist");
                    }

                    genre.genre = rs.getString("Genre");
                    genre.id = id;
                }
            }

            return genre;
        }
        
    	public static List<Genre> findByGenre(String genre) throws SQLException {
    		
    		Connection conn = ConnectionManager.getConnection();

    		List<Genre> genreList = new ArrayList<>();
    				
    		String inst = "SELECT GenreID, Genre FROM Genre WHERE LOWER(Genre) LIKE ?";
    		
    		try (PreparedStatement stmt = conn.prepareStatement(inst)) {
    			
    			stmt.setString(1, "%" + genre.toLowerCase() + "%");
    			
    			try (ResultSet rs = stmt.executeQuery()) {
    				
    				while (rs.next()) {
    					
    					Genre newGenre = new Genre();
    					
    					newGenre.id = rs.getLong("GenreID");
    					newGenre.setGenre(rs.getString("Genre"));
    					
    					genreList.add(newGenre);
    				}
    			}
    		}
    		
    		return genreList;
    	}
    }