package de.hsh.dbs2.imdb.ar;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import de.hsh.dbs2.imdb.other.ConnectionManager;

public class Person {

	private Long id;

	private String name;
	
	private char sex;
		
	//Getter and Setter
	
	public Long getId() {
		
		return id;
	}

	public String getName() {
		
		return name;
	}

	public void setName(String name) {
	
		
		this.name = name;
	}

	public int getSex() {
		
		return sex;
	}

	public void setSex(char sex) {
		
		this.sex = sex;
	}

	// Insert, Update and Delete Methods
	
	public void insert() throws SQLException {
		
		if (id != null) {
			
			throw new IllegalStateException("Object has already been inserted");
		}
		
		Connection conn = ConnectionManager.getConnection();
		
		String inst1 = "INSERT INTO Person(Name, Sex) VALUES(?, ?)";
		
		String inst2 = "SELECT last_insert_rowid() AS PersonID";
				
		try (PreparedStatement stmt1 = conn.prepareStatement(inst1)) {
			
			stmt1.setString(1, name);
			stmt1.setString(2, String.valueOf(sex));
			
			stmt1.executeUpdate();
			
			try (PreparedStatement stmt2 = conn.prepareStatement(inst2)){
			
				try (ResultSet rs = stmt2.executeQuery()) {
					
					rs.next();
					
					id = rs.getLong("PersonID");
				}
			}
		}
	}
	
	public void update() throws SQLException {
		
		if (id == null) {
			
			throw new IllegalStateException("Object has not been inserted");
		}
		
		Connection conn = ConnectionManager.getConnection();
		
		String inst = "UPDATE Person SET Name = ?, Sex = ? WHERE PersonID = ?";
				
		try (PreparedStatement stmt = conn.prepareStatement(inst)) {
			
			stmt.setString(1, name);
			stmt.setString(2, String.valueOf(sex));
			stmt.setLong(3, id);
			
			stmt.executeUpdate();
		}
	}
	
	public void delete() throws SQLException {
		
		if (id == null) {
			
			throw new IllegalStateException("Object has not been inserted");
		}
		
		Connection conn = ConnectionManager.getConnection();
		
		String inst = "DELETE FROM Person WHERE PersonID = ? ";
				
		try (PreparedStatement stmt = conn.prepareStatement(inst)) {
			
			stmt.setLong(1, id);
			
			stmt.executeUpdate();
		}
	}
	
    //Static Methods
	
	public static List<Person> findAll() throws SQLException {
		
		Connection conn = ConnectionManager.getConnection();

		List<Person> personList = new ArrayList<>();
		
		String inst = "SELECT PersonID, Name, Sex FROM Person";
		
		try (PreparedStatement stmt = conn.prepareStatement(inst)) {
			
			
			try (ResultSet rs = stmt.executeQuery()) {

				
				Person person = new Person();

				person.id = rs.getLong("PersonID");
				person.setName(rs.getString("Name"));
				person.setSex(rs.getString("Sex").charAt(0));
				
				personList.add(person);
			}
		}
		
		return personList;
	}
	
	public static Person findById(long id) throws SQLException {
		
		Connection conn = ConnectionManager.getConnection();

		Person person = new Person();
		
		person.id = id;
		
		String inst = "SELECT Name, Sex FROM Person WHERE PersonID = ?";
		
		try (PreparedStatement stmt = conn.prepareStatement(inst)) {
			
			stmt.setLong(1, id);
			
			try (ResultSet rs = stmt.executeQuery()) {
				
				if (!rs.next()) {
					
					throw new IllegalArgumentException("Object with ID " + id + " does not exist");
				}
				
				person.setName(rs.getString("Name"));
				person.setSex(rs.getString("Sex").charAt(0));
			}
		}
		
		return person;
	}
	 
	public static List<Person> findByName(String title) throws SQLException {
		
		Connection conn = ConnectionManager.getConnection();

		List<Person> people = new ArrayList<>();
				
		String inst = "SELECT PersonID, Name, Sex FROM Person WHERE LOWER(Name) LIKE ?";
		
		try (PreparedStatement stmt = conn.prepareStatement(inst)) {
			
			stmt.setString(1, "%" + title.toLowerCase() + "%");
			
			try (ResultSet rs = stmt.executeQuery()) {
				
				while (rs.next()) {
					
					Person person = new Person();
					
					person.id = rs.getLong("PersonID");
					person.setName(rs.getString("Name"));
					person.setSex(rs.getString("Sex").charAt(0));
					
					people.add(person);
				}
			}
		}
		
		return people;
	}
}
