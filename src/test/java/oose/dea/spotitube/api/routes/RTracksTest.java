package oose.dea.spotitube.api.routes;

import oose.dea.spotitube.dao.ITrackDAO;
import oose.dea.spotitube.domain.Track;
import oose.dea.spotitube.dto.TracksDTO;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.ws.rs.core.Response;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class RTracksTest {

    private static RTracks rTracks;

    private static ITrackDAO iTrackDAO;

    @BeforeAll
    public static void setup(){
        rTracks = new RTracks();
        iTrackDAO = mock(ITrackDAO.class);
        rTracks.setITrackDAO(iTrackDAO);
    }

    @Test
    @DisplayName("Get tracks returns 200")
    public void getTracksTest() {
        final String USERNAME = "Bert-Jan";
        final int PLAYLIST_ID = 1;
        final int TRACK_ID = 1;

        ArrayList<Track> tracks = new ArrayList<Track>();

        when(iTrackDAO.getTracks()).thenReturn(tracks);

        Response response = rTracks.getTracks();
        TracksDTO tracksDTO = (TracksDTO) response.getEntity();

        assertEquals(200, response.getStatus());
        assertEquals(tracksDTO.tracks, tracks);
    }

    @Test
    @DisplayName("Get tracks in playlist returns 200")
    public void getTracksInSpecificPlaylistTest() {
        final int PLAYLIST_ID = 1;
        ArrayList<Track> tracks = new ArrayList<Track>();

        when(iTrackDAO.getTracksNotInPlayList(PLAYLIST_ID)).thenReturn(tracks);

        Response response = rTracks.getTracksNotInPlaylist(PLAYLIST_ID);
        TracksDTO tracksDTO = (TracksDTO) response.getEntity();

        assertEquals(200, response.getStatus());
        assertEquals(tracksDTO.tracks, tracks);
    }
}
