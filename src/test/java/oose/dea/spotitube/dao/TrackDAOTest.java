package oose.dea.spotitube.dao;

import oose.dea.spotitube.domain.Track;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import util.DataSourceCreator;
import util.Seed;

import javax.sql.DataSource;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.NotFoundException;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class TrackDAOTest {

    final int TOTAL_NUMBER_OF_TRACKS = 5;
    final String USERNAME = "Test Username 1";

    TrackDAO trackDAO;

    @BeforeEach
    public void setup() {
        DataSource dataSource = DataSourceCreator.getInstance().getDataSource();
        Seed seed = Seed.getInstance(dataSource);

        try {
            seed.runScript("DDL.sql");
        } catch (Exception e) {
            fail(e.getMessage());
        }

        trackDAO = new TrackDAO();
        trackDAO.setDataSource(dataSource);
    }

    @AfterEach
    public void teardown() {
        DataSource dataSource = DataSourceCreator.getInstance().getDataSource();
        Seed seed = Seed.getInstance(dataSource);

        try {
            seed.runScript("EMPTY.sql");
        }  catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    @DisplayName("Get all tracks returns all tracks")
    public void getAllTracksTest() {
        ArrayList<Track> tracks = trackDAO.getTracks();

        assertEquals(TOTAL_NUMBER_OF_TRACKS, tracks.size());
    }

    @Test
    @DisplayName("Get all tracks returns all tracks in playlist")
    public void getTracksInPlaylistTest() {
        final int PLAYLIST_ID = 3;
        final int N_TRACKS_IN_PLAYLIST = 2;

        ArrayList<Track> tracks = trackDAO.getTracks(PLAYLIST_ID);

        assertEquals(N_TRACKS_IN_PLAYLIST, tracks.size());
    }

    @Test
    @DisplayName("Get all tracks in Playlist returns Track data")
    public void getAllTracksReturnsTrackDataTest() {
        final int PLAYLIST_ID = 3;

        ArrayList<Track> tracks = trackDAO.getTracks(PLAYLIST_ID);

        Track selectedTrack = tracks.get(0);

        assertNotNull(selectedTrack.getId());
        assertNotNull(selectedTrack.getTitle());
        assertNotNull(selectedTrack.getPlaycount());
        assertNotNull(selectedTrack.getPerformer());
        assertNotNull(selectedTrack.getDuration());
        assertNotNull(selectedTrack.isOfflineAvailable());
    }

    @Test
    @DisplayName("Tracks get joined with Video data")
    public void tracksGetJoinedWithVideoDataTest() {
        final int PLAYLIST_ID = 3;

        ArrayList<Track> tracks = trackDAO.getTracks(PLAYLIST_ID);

        Track selectedTrack = tracks.get(0);

        assertNotNull(selectedTrack.getDescription());
        assertNotNull(selectedTrack.getPublicationDate());
    }

    @Test
    @DisplayName("Tracks get joined with Song data")
    public void tracksGetJoinedWithSongDataTest() {
        final int PLAYLIST_ID = 1;

        ArrayList<Track> tracks = trackDAO.getTracks(PLAYLIST_ID);

        Track selectedTrack = tracks.get(0);

        assertNotNull(selectedTrack.getAlbum());
    }

    @Test
    @DisplayName("Owner can delete tracks from playlist")
    public void deleteTrackTest() {
        final int TRACK_ID = 1;
        final int PLAYLIST_ID = 1;

        ArrayList<Track> tracks = trackDAO.deleteTrack(USERNAME, PLAYLIST_ID, TRACK_ID);

        assertEquals(TOTAL_NUMBER_OF_TRACKS - 1, tracks.size());
    }

    @Test
    @DisplayName("User can not delete tracks from unowned playlist")
    public void deleteTrackFromUnownedPlaylistTest() {
        final int TRACK_ID = 1;
        final int PLAYLIST_ID = 2;

        assertThrows(NotAuthorizedException.class, () -> {
            ArrayList<Track> tracks = trackDAO.deleteTrack(USERNAME, PLAYLIST_ID, TRACK_ID);
        });
    }

    @Test
    @DisplayName("Can not delete tracks if playlist does not exist")
    public void canNotDeleteTracksFromNonExistingPlaylistTest() {
        final int TRACK_ID = 1;
        final int NON_EXISTING_PLAYLIST_ID = -1;

        assertThrows(NotFoundException.class, () -> {
            ArrayList<Track> tracks = trackDAO.deleteTrack(USERNAME, NON_EXISTING_PLAYLIST_ID, TRACK_ID);
        });
    }

    @Test
    @DisplayName("Can not delete tracks if track does not exist")
    public void deletingNonExistingTrackReturnsTracksTest() {
        final int NON_EXISTING_TRACK_ID = -1;
        final int PLAYLIST_ID = 1;


        assertThrows(NotFoundException.class, () -> {
            ArrayList<Track> tracks = trackDAO.deleteTrack(USERNAME, PLAYLIST_ID, NON_EXISTING_TRACK_ID);
        });
    }

    @Test
    @DisplayName("Can get tracks not in playlist")
    public void getTracksNotInPlaylistTest() {
        final int[] TRACK_IDS_IN_PLAYLIST = {1, 2, 3};
        final int PLAYLIST_ID = 2;

        ArrayList<Track> tracks = trackDAO.getTracksNotInPlayList(PLAYLIST_ID);

        assertTrue(TOTAL_NUMBER_OF_TRACKS == TRACK_IDS_IN_PLAYLIST.length + tracks.size());

        for (Track t : tracks) {
            for (int i : TRACK_IDS_IN_PLAYLIST) {
                assertNotEquals(i, t.getId());
            }
        }
    }

    @Test
    @DisplayName("Can't add more tracks than are available")
    public void getTracksNotInPlaylistTestReturnsZero() {
        final int PLAYLIST_ID = 1;

        ArrayList<Track> tracks = trackDAO.getTracksNotInPlayList(PLAYLIST_ID);

        assertEquals(0, tracks.size());
    }

    @Test
    @DisplayName("Owner can add track to playlist")
    public void addTrackToOwnedPlaylistTest() {

        final int PLAYLIST_ID = 2;
        final int TRACK_ID = 4;
        final boolean OFFLINE_AVAILABLE = false;
        final String PLAYLIST_OWNER = "Test Username 2";

        int oldNoTracks = trackDAO.getTracks(PLAYLIST_ID).size();
        ArrayList<Track> tracks = trackDAO.addTrack(TRACK_ID, PLAYLIST_ID, OFFLINE_AVAILABLE, PLAYLIST_OWNER);
        int newNoTracks = tracks.size();

        Track addedTrack = tracks.get(TRACK_ID - 1);

        assertEquals(newNoTracks, oldNoTracks + 1);
    }

    @Test
    @DisplayName("User can not add track to unowned playlist")
    public void addTrackToUnownedPlaylistTest() {

        final int PLAYLIST_ID = 2;
        final int TRACK_ID = 4;
        final boolean OFFLINE_AVAILABLE = false;
        final String NON_PLAYLIST_OWNER = "Test Username 1";

        assertThrows(NotAuthorizedException.class, () -> {
            ArrayList<Track> tracks = trackDAO.addTrack(TRACK_ID, PLAYLIST_ID, OFFLINE_AVAILABLE, NON_PLAYLIST_OWNER);
        });
    }

    @Test
    @DisplayName("Owner can not add track that already exists in playlist")
    public void addAlreadyAddedTrackToOwnedPlaylistTest() {

        final int PLAYLIST_ID = 1;
        final int TRACK_ID = 1;
        final boolean OFFLINE_AVAILABLE = false;
        final String PLAYLIST_OWNER = "Test Username 1";

        assertThrows(BadRequestException.class, () -> {
            ArrayList<Track> tracks = trackDAO.addTrack(TRACK_ID, PLAYLIST_ID, OFFLINE_AVAILABLE, PLAYLIST_OWNER);
        });
    }
}
