//Create

CREATE TABLE Movie(
MovieID INTEGER, Title VARCHAR(100), Year INTEGER, Type CHAR,
CONSTRAINT pk_movie PRIMARY KEY (MovieID)
);

CREATE TABLE Genre(
GenreID INTEGER, Genre VARCHAR(100),
CONSTRAINT pk_genre PRIMARY KEY (GenreID)
);

CREATE TABLE MovieGenre(
MovieGenreID INTEGER, MovieID INTEGER, GenreID INTEGER,
CONSTRAINT pk_movchar PRIMARY KEY (MovieGenreID),
CONSTRAINT fk_hasmovie FOREIGN KEY (MovieID) REFERENCES Movie(MovieID),
CONSTRAINT fk_hasgenre FOREIGN KEY (GenreID) REFERENCES Movie(GenreID)
);

CREATE TABLE MovieCharacter(
MovCharID INTEGER, Character VARCHAR(100), Alias VARCHAR(100), Position INTEGER, MovieID INTEGER, PersonID INTEGER,
CONSTRAINT pk_movchar PRIMARY KEY (MovCharID),
CONSTRAINT fk_movchar FOREIGN KEY (MovieID) REFERENCES Movie(MovieID),
CONSTRAINT fk_movchar FOREIGN KEY (PersonID) REFERENCES Person(PersonID)
);

CREATE TABLE Person(
PersonID INTEGER, Name VARCHAR(100), Sex CHAR,
CONSTRAINT pk_person PRIMARY KEY (PersonID),
CONSTRAINT u_name UNIQUE (Name)
);

//Insert

INSERT INTO Movie(MovieID, Title, Year, Type) 
VALUES ( 0, 'MyMovie', 2023, 'N' );

INSERT INTO Genre(GenreID, Genre) 
VALUES ( 0, 'Action' );

INSERT INTO MovieGenre(MovieGenreID, MovieID, GenreID) 
VALUES ( 0, 0, 0 );

INSERT INTO MovieCharacter(MovCharID, Character, Alias, Position, MovieID, PersonID) 
VALUES ( 0, 'MainChar', 'main', 1, 0, 0 );

INSERT INTO Person(PersonID, Name, Sex) 
VALUES ( 0, 'Jan', 'M' );



INSERT INTO Movie(MovieID, Title, Year, Type) 
VALUES ( 1, 'Test', 2000, 'N' );

INSERT INTO Genre(GenreID, Genre) 
VALUES ( 1, 'Horror' );

INSERT INTO MovieGenre(MovieGenreID, MovieID, GenreID) 
VALUES ( 1, 1, 0 );

INSERT INTO MovieCharacter(MovCharID, Character, Alias, Position, MovieID, PersonID) 
VALUES ( 1, 'Bob', 'SideChar', 2, 1, 1);

INSERT INTO Person(PersonID, Name, Sex) 
VALUES ( 1, 'Fatjon', 'M' );

//Drop

DROP TABLE Movie;

DROP TABLE Genre;

DROP TABLE MovieGenre;

DROP TABLE MovieCharacter;

DROP TABLE Person;

//SELECT

SELECT * FROM Movie;

SELECT * FROM Genre;

SELECT * FROM MovieGenre;

SELECT * FROM MovieCharacter;

SELECT * FROM Person;