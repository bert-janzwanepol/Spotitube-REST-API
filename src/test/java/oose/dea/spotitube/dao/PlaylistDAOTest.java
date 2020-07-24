package oose.dea.spotitube.dao;

import oose.dea.spotitube.domain.Playlist;
import org.junit.jupiter.api.*;
import util.DataSourceCreator;
import util.Seed;

import javax.sql.DataSource;
import javax.ws.rs.ForbiddenException;
import javax.ws.rs.NotFoundException;

import java.util.ArrayList;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class PlaylistDAOTest {

    private PlayListDao playListDao;
    private final String USERNAME = "Test Username 1";
    private final int NO_OF_TEST_PLAYLISTS = 4;

    @BeforeEach
    public void setup() {
        DataSource dataSource = DataSourceCreator.getInstance().getDataSource();
        Seed seed = Seed.getInstance(dataSource);

        try {
           seed.runScript("DDL.sql");
        } catch (Exception e) {
            fail(e.getMessage());
        }

        playListDao = new PlayListDao();
        playListDao.setDataSource(dataSource);
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

    @Nested
    @DisplayName("When No Data")
    public class WhenNoData {

        @BeforeEach
        public void setup() {
            DataSource dataSource = DataSourceCreator.getInstance().getDataSource();
            Seed seed = Seed.getInstance(dataSource);

            try {
                seed.runScript("EMPTY_PLAYLISTS.sql");
            } catch (Exception e) {
                fail(e.getMessage());
            }
        }

        @Test
        @DisplayName("Get on playlists throws 404")
        public void noPlaylistsAreReturnedWhenNoneAreInDb() {
            assertThrows(NotFoundException.class, () -> {
                ArrayList<Playlist> playlists = playListDao.getPlaylists(USERNAME);
            });
        }

        @Test
        @DisplayName("Delete on non existing playlist throws 404")
        public void userCanNotDeleteNonExistingPlaylist() {
            final int NON_EXISTING_PLAYLIST_ID = 1;

            assertThrows(NotFoundException.class, () -> {
                ArrayList<Playlist> playlists = playListDao.deletePlaylist(NON_EXISTING_PLAYLIST_ID, USERNAME);
            });
        }

        @Test
        @DisplayName("Update on non existing playlist throws 404")
        public void userCanNotUpdateNonExistingPlaylist() {
            final int NON_EXISTING_PLAYLIST_ID = 1;
            final String NEW_PLAYLIST_NAME = "New Playlist Name " + NON_EXISTING_PLAYLIST_ID;

            assertThrows(NotFoundException.class, () -> {
                ArrayList<Playlist> playlists = playListDao.updatePlaylistName(NON_EXISTING_PLAYLIST_ID, NEW_PLAYLIST_NAME, USERNAME);
            });
        }

        @Test
        @DisplayName("Total playlist length is zero")
        public void getPlaylistLengthTestReturnsZero() {
            int playlistsLength = playListDao.getPlaylistsLength();

            assertEquals(playlistsLength, 0);
        }
    }

    @Test
    @DisplayName("Get all playlists returns playlists")
    public void getAllPlayListsTest(){
        ArrayList<Playlist> playlists = playListDao.getPlaylists("Test user 1");

        // select random playlistindex
        int randomPlaylistIndex = new Random().nextInt((4 - 1));

        Playlist selectedPlaylist = playlists.get(randomPlaylistIndex);

        // increment randomPlaylistIndex by 1 because DB autoincrement starts at 1
        assertEquals(selectedPlaylist.getId(), randomPlaylistIndex + 1);
        assertEquals(selectedPlaylist.getName(), "Test Playlist " + (randomPlaylistIndex + 1));
    }

    @Test
    @DisplayName("User can delete his own playlists")
    public void userCanDeleteOwnedPlaylistTest() {
        ArrayList<Playlist> playlists = playListDao.deletePlaylist(1, "Test Username 1");

        assertEquals(playlists.size(), NO_OF_TEST_PLAYLISTS - 1);
    }

    @Test
    @DisplayName("User can not delete others' playlists")
    public void userCanNotDeleteOthersPlaylistTest() {

        assertThrows(ForbiddenException.class, () -> {
            ArrayList playlists = playListDao.deletePlaylist(2, "Test Username 1");
        });

    }

    @Test
    @DisplayName("Newly added playlists get added to the users account")
    public void playlistGetsAddedToAUsersAccountTest() {
        final String NEW_PLAYLIST_NAME = "Test Playlist " + NO_OF_TEST_PLAYLISTS + 1;

        ArrayList<Playlist> playlists = playListDao.addPlaylist(NEW_PLAYLIST_NAME, "Test Username 1");

        // index = NO_OF_TEST_PLAYLISTS, even though playlist.id is 1 higher
        Playlist newlyAddedPlaylist = playlists.get(NO_OF_TEST_PLAYLISTS);

        assertEquals(newlyAddedPlaylist.getId(), NO_OF_TEST_PLAYLISTS + 1);
        assertEquals(newlyAddedPlaylist.getName(), NEW_PLAYLIST_NAME);
        assertTrue(newlyAddedPlaylist.isOwner());
    }

    @Test
    @DisplayName("User can update his own playlist name")
    public void userCanUpdateOwnedPlaylistName() {

        final int PLAYLIST_ID = 1;
        final String NEW_PLAYLIST_NAME = "New Playlist Name " + PLAYLIST_ID;

        ArrayList<Playlist> playlists = playListDao.updatePlaylistName(PLAYLIST_ID, NEW_PLAYLIST_NAME, USERNAME);

        Playlist updatedPlaylist = playlists.get(PLAYLIST_ID - 1);

        assertEquals(updatedPlaylist.getName(), NEW_PLAYLIST_NAME);
    }

    @Test
    @DisplayName("User can not update others' playlist name")
    public void userCanNotUpdateOthersPlaylistName() {

        final int UNOWNED_PLAYLIST_ID = 2;
        final String NEW_PLAYLIST_NAME = "New Playlist Name " + UNOWNED_PLAYLIST_ID;

        assertThrows(ForbiddenException.class, () -> {
            ArrayList playlists = playListDao.updatePlaylistName(UNOWNED_PLAYLIST_ID, NEW_PLAYLIST_NAME, USERNAME);
        });
    }

    @Test
    @DisplayName("Get playlist length returns total playlist length")
    public void getPlaylistLengthReturnsTotalLengthTest() {
        final int TOTAL_PLAYLIST_LENGTH_OF_1 = 500 + 120 + 600 + 5000 + 70;
        final int TOTAL_PLAYLIST_LENGTH_OF_2 = 500 + 120 + 600;
        final int TOTAL_PLAYLIST_LENGTH_OF_3 = 500 + 120;
        final int TOTAL_PLAYLIST_LENGTH_OF_4 = 5000;

        int playlistsLength = playListDao.getPlaylistsLength();

        assertEquals(playlistsLength, TOTAL_PLAYLIST_LENGTH_OF_1 + TOTAL_PLAYLIST_LENGTH_OF_2 + TOTAL_PLAYLIST_LENGTH_OF_3 + TOTAL_PLAYLIST_LENGTH_OF_4);
    }
}
