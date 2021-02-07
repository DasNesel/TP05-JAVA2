package fr.isen.java2.db.daos;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import fr.isen.java2.db.entities.Film;
import fr.isen.java2.db.entities.Genre;

import javax.sql.DataSource;

public class FilmDao {

	public List<Film> listFilms() {
		/*
		 * Function use to get the list of the films
		 * Return : List<Genre> with all the genre in the database
		 */
		List<Film> list = new ArrayList<>();
		DataSource db = DataSourceFactory.getDataSource();
		try(Connection connection = db.getConnection()) { // connection to the database
			String sqlQuery = "SELECT * FROM film JOIN genre ON film.genre_id = genre.idgenre"; // requete SQL
			try(PreparedStatement statement = connection.prepareStatement(sqlQuery)){
				try(ResultSet resultSet = statement.executeQuery()){ //Recupere le resultat de la requete SQL
					while(resultSet.next()){
						LocalDate release = resultSet.getDate("release_date").toLocalDate();
						int id = resultSet.getInt("genre_id");
						int idFilm = resultSet.getInt("idfilm");
						int duration = resultSet.getInt("duration");
						String name = resultSet.getString("name");
						String title = resultSet.getString("title");
						String director = resultSet.getString("director");
						String summary = resultSet.getString("summary");
						Genre genre = new Genre(id, name);
						Film film = new Film(idFilm, title, release, genre, duration, director, summary);
						list.add(film);
					}
					resultSet.close();
				}
				statement.close();
			}
			connection.close();
		} catch(SQLException e){
			e.printStackTrace();
		}
		return list;
	}

	public List<Film> listFilmsByGenre(String genreName) {
	/*
	 *Function use to get the list of the films that have a specific genre
	 *
	 */
	List<Film> list = new ArrayList<>();
	DataSource db = DataSourceFactory.getDataSource();
	try(Connection connection = db.getConnection()) {
		String sqlQuery = "SELECT * FROM film JOIN genre ON film.genre_id = genre.idgenre WHERE genre.name = ?";
		try(PreparedStatement statement = connection.prepareStatement(sqlQuery)){
			statement.setString(1, genreName);
			try(ResultSet resultSet = statement.executeQuery()){
				while(resultSet.next()){
					LocalDate release = resultSet.getDate("release_date").toLocalDate();
					int id = resultSet.getInt("genre_id");
					int idFilm = resultSet.getInt("idfilm");
					int duration = resultSet.getInt("duration");
					String name = resultSet.getString("name");
					String title = resultSet.getString("title");
					String director = resultSet.getString("director");
					String summary = resultSet.getString("summary");
					Genre genre = new Genre(id, name);
					Film film = new Film(idFilm, title, release, genre, duration, director, summary);
					list.add(film);
				}
				resultSet.close();
			}
			statement.close();
		}
		connection.close();
	} catch(SQLException e){
		e.printStackTrace();
	}
	return list;
	}

	public void addFilm(Film film) {
		/*
		* Function that permit to add a film to the database
		 */
		DataSource db = DataSourceFactory.getDataSource();
		try (Connection connection = db.getConnection()) {
			String sqlQuery = "INSERT INTO film(title,release_date,genre_id,duration,director,summary)" + "VALUES(?,?,?,?,?,?)";
			try (PreparedStatement statement = connection.prepareStatement(sqlQuery, Statement.RETURN_GENERATED_KEYS)) {
				statement.setString(1, film.getTitle());
				statement.setDate(2, java.sql.Date.valueOf(film.getReleaseDate()));
				statement.setInt(3, film.getGenre().getId());
				statement.setInt(4, film.getDuration());
				statement.setString(5, film.getDirector());
				statement.setString(6, film.getSummary());

				statement.executeUpdate();
				statement.close();
			}
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}

