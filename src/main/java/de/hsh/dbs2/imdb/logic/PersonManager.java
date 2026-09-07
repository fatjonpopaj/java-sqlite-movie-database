package de.hsh.dbs2.imdb.logic;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import de.hsh.dbs2.imdb.ar.Person;
import de.hsh.dbs2.imdb.other.ConnectionManager;

public class PersonManager {

	/**
	 * Liefert eine Liste aller Personen, deren Name den Suchstring enthaelt.
	 * @param text Suchstring
	 * @return Liste mit passenden Personennamen, die in der Datenbank eingetragen sind.
	 * @throws Exception
	 */
	public List<String> getPersonList(String text) throws Exception {
			
		List<String> personNames = new ArrayList<>();
		
		boolean ok = false;
		
		try {
						
			personNames = Person.findByName(text).stream().map((person) -> person.getName()).collect(Collectors.toList()); 	
			
			ConnectionManager.getConnection().commit();
			
			ok = true;
			
		} finally {
			
			if (!ok) {
				
				ConnectionManager.getConnection().rollback();
			}
		}
		
		return personNames;

	}

}
