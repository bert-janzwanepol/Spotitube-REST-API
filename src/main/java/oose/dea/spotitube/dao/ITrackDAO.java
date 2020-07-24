package oose.dea.spotitube.dao;

import oose.dea.spotitube.domain.Track;

import java.util.ArrayList;

public interface ITrackDAO
{
    /**
     * Gets all tracks in a playlists.
     * @param playlistid The id of the playlist that contains the tracks
     * @return All tracks in a specific playlist.
     */

    public ArrayList<Track> getTracks(int playlistid);

    /**
     * Gets all tracks from the database.
     * @return All tracks whether or not they are in a playlist
     */

    public ArrayList<Track> getTracks();

    /**
     * Gets all tracks not in a playlists.
     * @param playlistid The id of the playlist that contains tracks that need to be excluded.
     * @return All tracks not in a specific playlist.
     */

    public ArrayList<Track> getTracksNotInPlayList(int playlistid);

    /**
     * Adds a track to a playlist
     * @param trackid The id of the track that will be added
     * @param playlistid The id of the playlist that the track will be added to.
     * @param offlineAvailable A flag that will determine whether or not a tracks is available without internet connection.
     * @param username The username of the user doing the request. Used to check if the user owns the playlist.
     * @return {@link #getTracks(int)}
     */

    public ArrayList<Track> addTrack(int trackid, int playlistid, boolean offlineAvailable, String username);

    /**
     * Deletes a track from a playlist
     * @param username The username of the user doing the request. Used to check if the user owns the playlist.
     * @param playlistid The id of the playlist that the track will be deleted from.
     * @param trackid The id of the track that will be deleted.
     * @return {@link #getTracks(int)}
     */

    public ArrayList<Track> deleteTrack(String username, int playlistid, int trackid);
}
