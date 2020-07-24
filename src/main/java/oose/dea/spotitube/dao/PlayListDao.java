package oose.dea.spotitube.dao;

import oose.dea.spotitube.domain.Playlist;

import javax.annotation.Resource;
import javax.enterprise.inject.Default;
import javax.sql.DataSource;
import javax.ws.rs.ForbiddenException;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.NotFoundException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

@Default
public class PlayListDao implements IPlaylistDAO {

    @Resource(name = "jdbc/Spotitube")
    DataSource dataSource;


    @Override
    public ArrayList<Playlist> getPlaylists(String username) {

        try {
            Connection connection = dataSource.getConnection();
            String sql = "SELECT id, name, owner, IF(owner = ?, true, false) AS isOwner FROM playlists";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();

            // Initialize fields for resultset
            ArrayList<Playlist> playlists = new ArrayList<Playlist>();

            while (resultSet.next()) {
                Playlist playlist = new Playlist();

                playlist.setName(resultSet.getString("name"));
                playlist.setId(resultSet.getInt("id"));
                playlist.setOwner(resultSet.getBoolean("isOwner"));

                playlists.add(playlist);
            }

            statement.close();
            connection.close();

             if (playlists.size() == 0) {
                throw new NotFoundException("No playlists found");
             }

            return playlists;
        } catch (SQLException e) {
            throw new InternalServerErrorException("Something went wrong.");
        }
    }

    @Override
    public ArrayList<Playlist> deletePlaylist(int id, String username) {
        try {
            Connection connection = dataSource.getConnection();
            String sql = "SELECT owner from Playlists where id = ?";

            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                if (resultSet.getString("owner").equals(username)) {
                    deletePlaylist(id);
                } else {
                    throw new ForbiddenException("Unauthorized deletion attempt for playlist with id: " + id);
                }
            } else {
                throw new NotFoundException("Resource playlist with id: " + id + " not found");
            }

            statement.close();
            connection.close();

            return getPlaylists(username);
        } catch (SQLException e) {
            throw new InternalServerErrorException("Something went wrong.");
        }
    }

    @Override
    public ArrayList<Playlist> addPlaylist(String playlistname, String username) {

        try {
            Connection connection = dataSource.getConnection();
            String sql =
                    "INSERT INTO Playlists (name, owner) " +
                    "VALUES(?, ?)";

            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, playlistname);
            statement.setString(2, username);
            statement.executeUpdate();

            statement.close();
            connection.close();

            return getPlaylists(username);
        } catch (SQLException e) {
            throw new InternalServerErrorException("Something went wrong.");
        }

    }

    @Override
    public ArrayList<Playlist> updatePlaylistName(int playlistid, String name, String username) {
        try {
            Connection connection = dataSource.getConnection();
            String sql = "SELECT owner from Playlists where id = ?";

            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, playlistid);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                if (resultSet.getString("owner").equals(username)) {
                    updatePlaylistName(playlistid, name);
                } else {
                    throw new ForbiddenException("Unauthorized update attempt for playlist with id: " + playlistid);
                }
            } else {
                throw new NotFoundException("Resource playlist with id: " + playlistid + " not found");
            }

            statement.close();
            connection.close();

            return getPlaylists(username);

        } catch (SQLException e) {
            throw new InternalServerErrorException("Something went wrong.");
        }
    }

    // actual update
    private void updatePlaylistName(int playlistid, String playlistname) throws SQLException {
        Connection connection = dataSource.getConnection();
        String sql = "UPDATE Playlists SET name = ? WHERE id = ?";

        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, playlistname);
        statement.setInt(2, playlistid);
        statement.executeUpdate();

        statement.close();
        connection.close();
    }

    // actual deletion
    private void deletePlaylist(int id) {
        try {
            Connection connection = dataSource.getConnection();
            String sql = "DELETE FROM Playlists WHERE id = ?";

            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, id);
            statement.executeUpdate();

            statement.close();
            connection.close();
        } catch(SQLException e) {
            throw new InternalServerErrorException("Something went wrong.");
        }
    }

    public int getPlaylistsLength() {
        try {
            Connection connection = dataSource.getConnection();

            // HAVING is needed here because SUM() always returns a resultset
            String sql = "SELECT SUM(duration) AS totalDuration " +
                    "FROM Track_in_Playlist " +
                    "LEFT JOIN Tracks ON Track_in_Playlist.trackid = Tracks.id " +
                    "HAVING totalDuration IS NOT NULL";

            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                int totalLength = resultSet.getInt("totalDuration");

                statement.close();
                connection.close();

                return totalLength;
            }

            statement.close();
            connection.close();

            return 0;
        } catch (SQLException e) {
            throw new InternalServerErrorException("Something went wrong.");
        }
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }
}
