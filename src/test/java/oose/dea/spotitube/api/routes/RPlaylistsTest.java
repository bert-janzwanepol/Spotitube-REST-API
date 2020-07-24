package oose.dea.spotitube.api.routes;

import oose.dea.spotitube.dao.IPlaylistDAO;

import oose.dea.spotitube.dao.ITrackDAO;
import oose.dea.spotitube.domain.Playlist;

import oose.dea.spotitube.domain.Track;
import oose.dea.spotitube.dto.PlaylistsDTO;
import oose.dea.spotitube.dto.SinglePlaylistDTO;
import oose.dea.spotitube.dto.TrackDTO;
import oose.dea.spotitube.dto.TracksDTO;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.ws.rs.core.Response;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class RPlaylistsTest {

    private static RPlaylists rPlaylists;

    @BeforeAll
    public static void setup(){
        rPlaylists = new RPlaylists();
    }

    @Test
    @DisplayName("Get playlists returns 200")
    public void getPlaylistsTest() {
        final String USERNAME = "Bert-Jan";
        final int PLAYLIST_LENGTH = 1;

        // Setup mocks
        IPlaylistDAO iPlaylistDAO = mock(IPlaylistDAO.class);
        ArrayList<Playlist> playlists = new ArrayList<Playlist>();

        when(iPlaylistDAO.getPlaylists(USERNAME)).thenReturn(playlists);
        when(iPlaylistDAO.getPlaylistsLength()).thenReturn(PLAYLIST_LENGTH);

        rPlaylists.setIPlaylistDAO(iPlaylistDAO);

        Response response = rPlaylists.getPlayLists(USERNAME);
        PlaylistsDTO playlistsDTO = (PlaylistsDTO) response.getEntity();

        assertEquals(200, response.getStatus());
        assertEquals(playlistsDTO.length, PLAYLIST_LENGTH);
        assertEquals(playlistsDTO.playlists, playlists);
    }

    @Test
    @DisplayName("Update playlist returns 200")
    public void updatePlaylistTest() {
        final int PLAYLIST_ID = 1;
        final int PLAYLIST_LENGTH = 1;
        final String USERNAME = "Bert-Jan";
        final String NEW_PLAYLIST_NAME = "NEW NAME";

        SinglePlaylistDTO singlePlaylistDTO = new SinglePlaylistDTO();
        singlePlaylistDTO.name = NEW_PLAYLIST_NAME;
        singlePlaylistDTO.id = PLAYLIST_ID;
        singlePlaylistDTO.owner = true;

        IPlaylistDAO iPlaylistDAO = mock(IPlaylistDAO.class);
        ArrayList<Playlist> playlists = new ArrayList<Playlist>();

        when(iPlaylistDAO.deletePlaylist(PLAYLIST_ID, USERNAME)).thenReturn(playlists);
        when(iPlaylistDAO.getPlaylistsLength()).thenReturn(PLAYLIST_LENGTH);

        rPlaylists.setIPlaylistDAO(iPlaylistDAO);

        Response response = rPlaylists.updatePlaylistName(USERNAME, PLAYLIST_ID, singlePlaylistDTO);
        PlaylistsDTO playlistsDTO = (PlaylistsDTO) response.getEntity();

        assertEquals(200, response.getStatus());
        assertEquals(playlistsDTO.length, PLAYLIST_LENGTH);
        assertEquals(playlistsDTO.playlists, playlists);
    }

    @Test
    @DisplayName("Delete playlist returns 200")
    public void deletePlaylistTest() {
        final int PLAYLIST_ID = 1;
        final int PLAYLIST_LENGTH = 1;
        final String USERNAME = "Bert-Jan";

        // Setup mocks
        IPlaylistDAO iPlaylistDAO = mock(IPlaylistDAO.class);
        ArrayList<Playlist> playlists = new ArrayList<Playlist>();

        when(iPlaylistDAO.deletePlaylist(PLAYLIST_ID, USERNAME)).thenReturn(playlists);
        when(iPlaylistDAO.getPlaylistsLength()).thenReturn(PLAYLIST_LENGTH);

        rPlaylists.setIPlaylistDAO(iPlaylistDAO);

        Response response = rPlaylists.deletePlaylist(PLAYLIST_ID, USERNAME);
        PlaylistsDTO playlistsDTO = (PlaylistsDTO) response.getEntity();

        assertEquals(200, response.getStatus());
        assertEquals(playlistsDTO.length, PLAYLIST_LENGTH);
        assertEquals(playlistsDTO.playlists, playlists);
    }

    @Test
    @DisplayName("Add playlist returns 201")
    public void addPlaylistTest() {
        final int PLAYLIST_ID = 1;
        final int PLAYLIST_LENGTH = 1;
        final String NEW_PLAYLIST_NAME = "NEW NAME";
        final String USERNAME = "Bert-Jan";

        SinglePlaylistDTO singlePlaylistDTO = new SinglePlaylistDTO();
        singlePlaylistDTO.id = PLAYLIST_ID;
        singlePlaylistDTO.name = NEW_PLAYLIST_NAME;
        singlePlaylistDTO.owner = false;

        // Setup mocks
        IPlaylistDAO iPlaylistDAO = mock(IPlaylistDAO.class);
        ArrayList<Playlist> playlists = new ArrayList<Playlist>();

        when(iPlaylistDAO.addPlaylist(NEW_PLAYLIST_NAME, USERNAME)).thenReturn(playlists);
        when(iPlaylistDAO.getPlaylistsLength()).thenReturn(PLAYLIST_LENGTH);

        rPlaylists.setIPlaylistDAO(iPlaylistDAO);

        Response response = rPlaylists.addPlaylist(USERNAME, singlePlaylistDTO);
        PlaylistsDTO playlistsDTO = (PlaylistsDTO) response.getEntity();

        assertEquals(201, response.getStatus());
        assertEquals(playlistsDTO.length, PLAYLIST_LENGTH);
        assertEquals(playlistsDTO.playlists, playlists);
    }

    @Test
    @DisplayName("Get Tracks in playlist returns 200")
    public void getTracksInPlaylistTest() {
        final int PLAYLIST_ID = 1;

        ITrackDAO iTrackDAO = mock(ITrackDAO.class);
        ArrayList<Track> tracks = new ArrayList<Track>();

        when(iTrackDAO.getTracks(PLAYLIST_ID)).thenReturn(tracks);

        rPlaylists.setITrackDAO(iTrackDAO);

        Response response = rPlaylists.getTracks(PLAYLIST_ID);
        TracksDTO tracksDTO = (TracksDTO) response.getEntity();

        assertEquals(200, response.getStatus());
        assertEquals(tracksDTO.tracks, tracks);

    }

    @Test
    @DisplayName("Delete playlist returns 200")
    public void deleteTrackTest() {
        final String USERNAME = "Bert-Jan";
        final int PLAYLIST_ID = 1;
        final int TRACK_ID = 1;

        ITrackDAO iTrackDAO = mock(ITrackDAO.class);
        ArrayList<Track> tracks = new ArrayList<Track>();

        when(iTrackDAO.deleteTrack(USERNAME, PLAYLIST_ID, TRACK_ID)).thenReturn(tracks);

        rPlaylists.setITrackDAO(iTrackDAO);

        Response response = rPlaylists.deleteTrack(USERNAME, PLAYLIST_ID, TRACK_ID);
        TracksDTO tracksDTO = (TracksDTO) response.getEntity();

        assertEquals(200, response.getStatus());
        assertEquals(tracksDTO.tracks, tracks);
    }

    @Test
    @DisplayName("Add playlist returns 201")
    public void addTrackTest() {
        final String USERNAME = "Bert-Jan";
        final int PLAYLIST_ID = 1;
        final int TRACK_ID = 1;

        ArrayList<Track> tracks = new ArrayList<Track>();

        ITrackDAO iTrackDAO = mock(ITrackDAO.class);
        TrackDTO trackDTO = new TrackDTO();
        trackDTO.id = TRACK_ID;
        trackDTO.title = "MY Amazing track title";
        trackDTO.performer = "Some random performer";
        trackDTO.duration = 500;
        trackDTO.offlineAvailable = true;

        when(iTrackDAO.addTrack(trackDTO.id, PLAYLIST_ID, trackDTO.offlineAvailable, USERNAME)).thenReturn(tracks);

        rPlaylists.setITrackDAO(iTrackDAO);

        Response response = rPlaylists.addTrack(USERNAME, TRACK_ID, trackDTO);
        TracksDTO tracksDTO = (TracksDTO) response.getEntity();

        assertEquals(201, response.getStatus());
        assertEquals(tracksDTO.tracks, tracks);
    }

}
