package oose.dea.spotitube.filter;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.net.URI;

@Provider
@PreMatching
@ForPlaylist
public class ForPlaylistFilter implements ContainerRequestFilter {

    /**
     * Checks if the forPlaylist queryparameter is presents and redirects to the appropriate endpoint
     * @param requestContext JAX-RS requestcontext
     * @throws IOException
     */

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        MultivaluedMap<String, String> queryParameters = requestContext.getUriInfo().getQueryParameters();

        if (queryParameters.size() != 0 && queryParameters.get("forPlaylist") != null) {
            requestContext.setRequestUri(URI.create("tracks/available"));
        }
    }
}
