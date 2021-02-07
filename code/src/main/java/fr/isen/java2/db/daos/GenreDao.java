package fr.isen.java2.db.daos;

import java.lang.reflect.GenericSignatureFormatError;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import fr.isen.java2.db.entities.Genre;
import org.sqlite.SQLiteDataSource;

import javax.sql.DataSource;

public class GenreDao {

	public List<Genre> listGenres() {
		/*
		* Function use to get the list of the genres
		* Return : List<Genre> with all the genre in the database
		 */
		List<Genre> list = new ArrayList<>();
		DataSource db = DataSourceFactory.getDataSource();
		try(Connection connection = db.getConnection()) {
			String sqlQuery = "SELECT * FROM genre";
			try(PreparedStatement statement = connection.prepareStatement(sqlQuery)){
				try(ResultSet resultSet = statement.executeQuery()){
					while(resultSet.next()){
						int id = resultSet.getInt("idgenre");
						String name = resultSet.getString("name");
						Genre genre = new Genre(id, name);
						list.add(genre);
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

	public Genre getGenre(String name) {
	/*
	*Function use to get the id and the name of genres
	*
	 */
		Genre result;
		DataSource db = DataSourceFactory.getDataSource();
		try(Connection connection = db.getConnection()) {
			String sqlQuery = "SELECT * FROM genre WHERE name = ?";
			try(PreparedStatement statement = connection.prepareStatement(sqlQuery)){
				statement.setString(1, name);
				try(ResultSet resultSet = statement.executeQuery()){
					if(resultSet.next()){
						int id = resultSet.getInt("idgenre");
						String genreName = resultSet.getString("name");
						Genre genre = new Genre(id, genreName);
						result = genre;
					}
					else{
						result = null; //return null if the genre doesn't in the table
					}
					resultSet.close();
				}
				statement.close();
			}
			connection.close();
			return result; // return the result after close all the results, statements and connections
		} catch(SQLException e){
			e.printStackTrace();
		}
		return null;
	}

	public void addGenre(String name) {
		DataSource db = DataSourceFactory.getDataSource();
		try(Connection connection = db.getConnection()) {
			String sqlQuery = "INSERT INTO genre(name)" + "VALUES(?)";
			try(PreparedStatement statement = connection.prepareStatement(sqlQuery, Statement.RETURN_GENERATED_KEYS)){
				statement.setString(1, name);
				statement.executeUpdate();
				statement.close();
			}
			connection.close();
		} catch(SQLException e){
			e.printStackTrace();
		}
	}
}
