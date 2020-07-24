package oose.dea.spotitube.dao;
import oose.dea.spotitube.domain.Playlist;

import java.util.ArrayList;

public interface IPlaylistDAO
{
    /**
     * Gets all playlist from a database.
     * @param username The username of the user doing the request. Used to check if the user owns a playlist.
     * @return All playlists in a database.
     */

    public ArrayList<Playlist> getPlaylists(String username);

    /**
     * Deletes a playlist from the database
     * @param id The id of the playlist that will be deleted
     * @param username The username of the user doing the request. Used to check if the user owns the playlist.
     * @return All playlists in a database.
     */

    public ArrayList<Playlist> deletePlaylist(int id, String username);

    /**
     * Adds a playlist to the database.
     * @param playlistname The name of the new playlist.
     * @param username The username of the user doing the request. Get's stored in the database with the playlist as proof of ownership.
     * @return All playlists in a database.
     */

    public ArrayList<Playlist> addPlaylist(String playlistname, String username);

    /**
     * Updates the name of playlist in the database.
     * @param playlistId The id of the playlist that will be updated
     * @param name The new name of the  playlist.
     * @param username he username of the user doing the request. Used to check if the user owns the playlist.
     * @return All playlists in a database.
     */

    public ArrayList<Playlist> updatePlaylistName(int playlistId, String name, String username);

    /**
     * Gets the length of all playlists in a database
     * @return The sum of the length of all playlists.
     */

    int getPlaylistsLength();
}
