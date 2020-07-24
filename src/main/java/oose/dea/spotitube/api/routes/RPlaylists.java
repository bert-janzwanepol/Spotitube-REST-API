package oose.dea.spotitube.api.routes;

import oose.dea.spotitube.dao.IPlaylistDAO;
import oose.dea.spotitube.dao.ITrackDAO;
import oose.dea.spotitube.domain.Playlist;
import oose.dea.spotitube.domain.Track;
import oose.dea.spotitube.dto.PlaylistsDTO;
import oose.dea.spotitube.dto.SinglePlaylistDTO;
import oose.dea.spotitube.dto.TrackDTO;
import oose.dea.spotitube.dto.TracksDTO;
import oose.dea.spotitube.filter.Token;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;

/**
 * Contains all endpoints concerning CRUD operations on playlists.
 */

@Path("/playlists")
public class RPlaylists {

    private IPlaylistDAO IPlaylistDAO;
    private ITrackDAO ITrackDAO;

    /**
     *
     * @param username
     * @return
     */

    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    @Token
    public Response getPlayLists(@HeaderParam("X-token-user") String username) {
        ArrayList<Playlist> playlists = IPlaylistDAO.getPlaylists(username);

        return createPlaylistResponse(playlists, 200);
    }

    /**
     * Updates the name of a playlist.
     * @param username The username of the user doing the update.
     * @param id The id of the playlist that is going te be updated.
     * @param playlist The new playlist object containing the new name.
     * @return A Response containing all playlists.
     */

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Token
    public Response updatePlaylistName(@HeaderParam("X-token-user") String username, @PathParam("id") int id, SinglePlaylistDTO playlist) {
        ArrayList<Playlist> playlists = IPlaylistDAO.updatePlaylistName(id, playlist.name, username);

        return createPlaylistResponse(playlists, 200);
    }

    /**
     * Deletes the name of a playlist.
     * @param id The id of the playlist that is going te be update.
     * @param username The username of the user doing the update.
     * @return A Response containing all playlists.
     */

    @DELETE
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Token
    public Response deletePlaylist(@PathParam("id") int id, @HeaderParam("X-token-user") String username) {
        ArrayList<Playlist> playlists = IPlaylistDAO.deletePlaylist(id, username);

        return createPlaylistResponse(playlists, 200);
    }

    /**
     * Adds a new playlist to the database.
     * @param username The username of the user doing the update.
     * @param singlePlaylistDTO The new playlist object.
     * @return A Response containing all playlists.
     */

    @POST
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    @Token
    public Response addPlaylist(@HeaderParam("X-token-user") String username, SinglePlaylistDTO singlePlaylistDTO) {
        ArrayList<Playlist> playlists = IPlaylistDAO.addPlaylist(singlePlaylistDTO.name, username);

        return createPlaylistResponse(playlists, 201);
    }


    /**
     * Returns all tracks in a playlist.
     * @param id The id of the playlist.
     * @return A Response containing all tracks in a specific playlist.
     */

    @GET
    @Path("/{id}/tracks")
    @Produces(MediaType.APPLICATION_JSON)
    @Token
    public Response getTracks(@PathParam("id") int id) {
        ArrayList<Track> tracks = ITrackDAO.getTracks(id);

        return createTracksResponse(tracks, 200);
    }

    /**
     * Deletes a track from a specific playlist.
     * @param username The username of the user doing the delete.
     * @param playlistid The id of the playlist that contains the track to be deleted.
     * @param trackid The id of the tracks that will be deleted.
     * @return A Response containing all tracks in a specific playlist.
     */

    @DELETE
    @Path("/{playlistid}/tracks/{trackid}")
    @Produces(MediaType.APPLICATION_JSON)
    @Token
    public Response deleteTrack(@HeaderParam("X-token-user") String username, @PathParam("playlistid") int playlistid, @PathParam("trackid") int trackid) {
        ArrayList<Track> tracks = ITrackDAO.deleteTrack(username, playlistid, trackid);

        TracksDTO tracksDTO = new TracksDTO();
        tracksDTO.tracks = tracks;

        return createTracksResponse(tracks, 200);
    }

    /**
     * Adds a track to a specific playlist.
     * @param username The username of the user adding a track to the playlist.
     * @param playlistid The id of the playlist that get's a new track.
     * @param trackDTO The id of the track that will be added.
     * @return A Response containing all tracks in a specific playlist.
     */

    @POST
    @Path("/{playlistid}/tracks")
    @Produces(MediaType.APPLICATION_JSON)
    @Token
    public Response addTrack(@HeaderParam("X-token-user") String username, @PathParam("playlistid") int playlistid, TrackDTO trackDTO) {
        ArrayList<Track> tracks = ITrackDAO.addTrack(trackDTO.id, playlistid, trackDTO.offlineAvailable, username);

         return createTracksResponse(tracks, 201);
    }

    /**
     * Creates a Response containing all playlists.
     * @param playlists The playlists that will be returned.
     * @param statusCode The statusCode of the response, usually 200 or 201.
     * @return A Response containing all playlists.
     */

    private Response createPlaylistResponse(ArrayList<Playlist> playlists, int statusCode) {
        int length = IPlaylistDAO.getPlaylistsLength();

        PlaylistsDTO playlistsDTO = new PlaylistsDTO();
        playlistsDTO.playlists = playlists;
        playlistsDTO.length = length;

        return Response.status(statusCode).entity(playlistsDTO).build();
    }

    /**
     * Creates a Response containing all playlists.
     * @param tracks The tracks that will be returned.
     * @param statusCode The statusCode of the response, usually 200 or 201.
     * @return A Response containing all tracks.
     */

    private Response createTracksResponse(ArrayList<Track> tracks, int statusCode) {
        TracksDTO tracksDTO = new TracksDTO();
        tracksDTO.tracks = tracks;

        return Response.status(statusCode).entity(tracksDTO).build();
    }

    @Inject
    public void setIPlaylistDAO(IPlaylistDAO IPlaylistDAO) {
        this.IPlaylistDAO = IPlaylistDAO;
    }

    @Inject
    public void setITrackDAO(ITrackDAO ITrackDAO) {
        this.ITrackDAO = ITrackDAO;
    }
}
