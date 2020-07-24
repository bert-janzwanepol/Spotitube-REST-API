package oose.dea.spotitube.dao;

import oose.dea.spotitube.domain.Track;

import javax.annotation.Resource;
import javax.sql.DataSource;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.NotFoundException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class TrackDAO implements ITrackDAO {

    @Resource(name = "jdbc/Spotitube")
    DataSource dataSource;

    public ArrayList<Track> getTracks(int playlistid) {
        try {
            Connection connection = dataSource.getConnection();

            String sql =
                    "SELECT * FROM Track_in_Playlist " +
                    "LEFT JOIN Tracks ON Track_in_Playlist.trackid = Tracks.id " +
                    "LEFT JOIN Songs ON Tracks.id = Songs.id " +
                    "LEFT JOIN Videos ON Tracks.id = Videos.id " +
                    "WHERE playlistid = ?";

            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, playlistid);
            ResultSet resultSet = statement.executeQuery();

            ArrayList<Track> tracks = new ArrayList<Track>();

            while (resultSet.next()) {
                Track track = new Track(
                        resultSet.getInt("id"),
                        resultSet.getString("title"),
                        resultSet.getString("performer"),
                        resultSet.getInt("duration"),
                        resultSet.getString("album"),
                        resultSet.getInt("playcount"),
                        resultSet.getString("publicationDate"),
                        resultSet.getString("description"),
                        resultSet.getBoolean("offlineAvailable")
                );

                tracks.add(track);
            }

            statement.close();
            connection.close();

            return tracks;

        } catch(SQLException e) {
            throw new InternalServerErrorException("Something went wrong.");
        }
    }

    @Override
    public ArrayList<Track> getTracks() {
        try {
            Connection connection = dataSource.getConnection();

            String sql =
                    "SELECT Tracks.id, Tracks.title, Tracks.performer, Tracks.duration, Tracks.playcount, " +
                            "Songs.album, " +
                            "Videos.publicationDate, Videos.description " +
                    "FROM Tracks " +
                    "LEFT JOIN Songs ON Tracks.id = Songs.id " +
                    "LEFT JOIN Videos ON Tracks.id = Videos.id";

            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();

            ArrayList<Track> tracks = new ArrayList<Track>();

            while (resultSet.next()) {

                Track track = new Track(
                        resultSet.getInt("id"),
                        resultSet.getString("title"),
                        resultSet.getString("performer"),
                        resultSet.getInt("duration"),
                        resultSet.getString("album"),
                        resultSet.getInt("playcount"),
                        resultSet.getString("publicationDate"),
                        resultSet.getString("description")
                );

                tracks.add(track);
            }

            statement.close();
            connection.close();

            return tracks;

        } catch(SQLException e) {
            throw new InternalServerErrorException("Something went wrong.");
        }
    }

    @Override
    public ArrayList<Track> deleteTrack(String username, int playlistid, int trackid) {
        try {
            if (!trackExistsInPlaylist(playlistid, trackid)) {
                throw new NotFoundException("Can't delete a track that doesn't exist");
            }

            Connection connection = dataSource.getConnection();
            String sql = "SELECT owner from Playlists where id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, playlistid);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                if (resultSet.getString("owner").equals(username)) {
                    deleteTrack(playlistid, trackid);
                } else {
                    statement.close();
                    connection.close();
                    throw new NotAuthorizedException("Not authorized");
                }
            } else {
                throw new NotFoundException("Could not find playlist with id: " + playlistid);
            }

            statement.close();
            connection.close();

            return getTracks(playlistid);
        } catch (SQLException e) {
            throw new InternalServerErrorException("Something went wrong.");
        }
    }

    private void deleteTrack(int playlistid, int trackid) {
        try {
            Connection connection = dataSource.getConnection();
            String sql = "DELETE FROM Track_in_Playlist WHERE playlistid = ? AND trackid = ?";

            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, playlistid);
            statement.setInt(2, trackid);
            statement.executeUpdate();

            statement.close();
            connection.close();

        } catch (SQLException e) {
            throw new InternalServerErrorException("Something went wrong.");
        }
    }

    private boolean trackExistsInPlaylist(int playlistid, int trackid ) throws SQLException {

        Connection connection = dataSource.getConnection();
        String sql = "SELECT * FROM Track_in_Playlist WHERE playlistid = ? AND trackid = ?";

        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, playlistid);
        statement.setInt(2, trackid);
        ResultSet resultSet = statement.executeQuery();

        if (resultSet.next()) {
            statement.close();
            connection.close();

            return true;
        }

        return false;
    }

    @Override
    public ArrayList<Track> getTracksNotInPlayList(int playlistid) {
        try {
            Connection connection = dataSource.getConnection();

            String sql =
                    "SELECT  *" +
                    "FROM Tracks " +
                    "LEFT JOIN Songs ON Tracks.id = Songs.id " +
                    "LEFT JOIN Videos ON Tracks.id = Videos.id " +
                    "WHERE Tracks.id NOT IN (" +
                            "SELECT trackid " +
                            "FROM Track_in_Playlist " +
                            "WHERE playlistid = ?" +
                    ")";

            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, playlistid);
            ResultSet resultSet = statement.executeQuery();

            ArrayList<Track> tracks = new ArrayList<Track>();

            while (resultSet.next()) {

                Track track = new Track(
                        resultSet.getInt("id"),
                        resultSet.getString("title"),
                        resultSet.getString("performer"),
                        resultSet.getInt("duration"),
                        resultSet.getString("album"),
                        resultSet.getInt("playcount"),
                        resultSet.getString("publicationDate"),
                        resultSet.getString("description")
                );

                tracks.add(track);
            }

            statement.close();
            connection.close();

            return tracks;

        } catch(SQLException e) {
            throw new InternalServerErrorException("Something went wrong.");
        }
    }

    @Override
    public ArrayList<Track> addTrack(int trackid, int playlistid, boolean offlineAvailabe, String username) {
        try {
            if (trackExistsInPlaylist(playlistid, trackid)) {
                throw new BadRequestException("Can't add a track that already exists");
            }

            Connection connection = dataSource.getConnection();
            String sql = "SELECT owner from Playlists where id = ?";

            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, playlistid);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                if (resultSet.getString("owner").equals(username)) {
                    addTrack(trackid, playlistid, offlineAvailabe);
                } else {
                    statement.close();
                    connection.close();
                    throw new NotAuthorizedException("Not authorized");
                }
            } else {
                throw new NotFoundException("Could not find playlist with id: " + playlistid);
            }

            statement.close();
            connection.close();

            return getTracks(playlistid);
        } catch (SQLException e) {
            throw new InternalServerErrorException("Something went wrong.");
        }
    }

    private void addTrack(int trackid, int playlistid, boolean offlineAvailabe) {

        try {
            Connection connection = dataSource.getConnection();
            String sql = "INSERT INTO Track_in_Playlist (trackid, playlistid, offlineAvailable) " +
                    "VALUES(?, ?, ?)";

            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, trackid);
            statement.setInt(2, playlistid);
            statement.setBoolean(3, offlineAvailabe);
            statement.executeUpdate();

            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

}
