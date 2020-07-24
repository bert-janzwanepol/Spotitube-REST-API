package oose.dea.spotitube.api.routes;

import oose.dea.spotitube.dao.ITrackDAO;
import oose.dea.spotitube.domain.Track;
import oose.dea.spotitube.dto.TracksDTO;
import oose.dea.spotitube.filter.ForPlaylist;
import oose.dea.spotitube.filter.Token;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;

/**
 * Contains all endpoints concerning CRUD operations on tracks.
 */

@Path("/tracks")
public class RTracks {

    private ITrackDAO ITrackDAO;

    /**
     * Gets all tracks in the playlist from the database
     * @return A Response containing all the tracks.
     */

    @GET
    @Path("/")
    @ForPlaylist
    @Produces(MediaType.APPLICATION_JSON)
    @Token
    public Response getTracks() {

        ArrayList<Track> tracks = ITrackDAO.getTracks();

        TracksDTO tracksDTO = new TracksDTO();
        tracksDTO.tracks = tracks;

        return Response.status(200).entity(tracksDTO).build();
    }

    /**
     * Returns all tracks currently not in the playlist. Get's called when a forPlaylist queryparameter is present.
     * @param playlistid the id of the playlist containing the tracks that need to be excluded.
     * @return A Response containing all the tracks not in a specific playlist.
     */

    @GET
    @Path("/available")
    @Produces(MediaType.APPLICATION_JSON)
    @Token
    public Response getTracksNotInPlaylist(@QueryParam("forPlaylist") int playlistid) {
        ArrayList<Track> tracks = ITrackDAO.getTracksNotInPlayList(playlistid);

        TracksDTO tracksDTO = new TracksDTO();
        tracksDTO.tracks = tracks;

        return Response.status(200).entity(tracksDTO).build();
    }

    @Inject
    public void setITrackDAO(ITrackDAO ITrackDAO) {
        this.ITrackDAO = ITrackDAO;
    }
}
