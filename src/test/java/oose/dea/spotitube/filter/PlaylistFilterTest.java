package oose.dea.spotitube.filter;
import org.junit.jupiter.api.*;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriInfo;

import java.io.IOException;
import java.net.URI;
import java.util.List;

import static org.mockito.Mockito.*;

public class PlaylistFilterTest {

    ForPlaylistFilter filter;
    final URI redirectURI = URI.create("tracks/available");
    final String TESTED_QUERY_PARAM = "forPlaylist";
    ContainerRequestContext context;
    UriInfo uriInfo;
    MultivaluedMap<String, String> queryParams;

    @BeforeEach
    public void setup() {
        filter = new ForPlaylistFilter();
        context = mock(ContainerRequestContext.class);
        uriInfo = mock(UriInfo.class);
        queryParams = mock(MultivaluedMap.class);
    }

    @Test
    @DisplayName("Request for playlist tracks get redirected when parameter present")
    public void playlistRedirectsWhenParamIsPresentTest() throws IOException {

        List<String> queryParamEntry = mock(List.class);

        when(context.getUriInfo()).thenReturn(uriInfo);
        when(uriInfo.getQueryParameters()).thenReturn(queryParams);
        when(queryParams.size()).thenReturn(1);
        when(queryParams.get(TESTED_QUERY_PARAM)).thenReturn(queryParamEntry);

        filter.filter(context);

        verify(context).setRequestUri(redirectURI);
    }

    @Test
    @DisplayName("Request for playlist tracks don't get redirected when no parameter present")
    public void playlistDoesNotRedirectWhenNoParamIsPresentTest() throws IOException {

        when(context.getUriInfo()).thenReturn(uriInfo);
        when(uriInfo.getQueryParameters()).thenReturn(queryParams);
        when(queryParams.size()).thenReturn(0);
        when(queryParams.get(TESTED_QUERY_PARAM)).thenReturn(null);

        filter.filter(context);

        verify(context, never()).setRequestUri(redirectURI);
    }

}
