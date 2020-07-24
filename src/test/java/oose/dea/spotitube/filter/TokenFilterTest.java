package oose.dea.spotitube.filter;

import org.junit.jupiter.api.*;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriInfo;
import java.io.IOException;
import java.util.List;

import static org.mockito.Mockito.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TokenFilterTest {

    final String TESTED_QUERY_PARAM = "token";
    // expires at 27-03-2021
    private final String VALID_TEST_TOKEN = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJCZXJ0LUphbiIsImlzcyI6Imhhbi1pY2EiLCJleHAiOjE2MTY4MzMwMjksImlhdCI6MTU4NTI5NzE5OX0.QuiiqRW0VVCjG-OL4oNJAWr68Eaa8m9n_Q3Xk5F_PCY";
    private final String VALID_TEST_USERNAME = "Bert-Jan";

    private TokenFilter filter;
    private ContainerRequestContext context;
    private UriInfo uriInfo;
    private MultivaluedMap<String, String> queryParams;
    private MultivaluedMap<String, String> headers;

    @BeforeEach
    public void setup() {
        filter = new TokenFilter();
        context = mock(ContainerRequestContext.class);
        uriInfo = mock(UriInfo.class);
        queryParams = mock(MultivaluedMap.class);
        headers = mock(MultivaluedMap.class);
    }

    @Test
    @DisplayName("Username from token gets added to request headers when token present")
    public void usernameGetsAddedToHeadersTest() throws IOException {

        List<String> queryParamEntry = mock(List.class);

        when(context.getUriInfo()).thenReturn(uriInfo);
        when(uriInfo.getQueryParameters()).thenReturn(queryParams);
        when(queryParams.size()).thenReturn(1);
        when(queryParams.get(TESTED_QUERY_PARAM)).thenReturn(queryParamEntry);
        when(queryParamEntry.get(anyInt())).thenReturn(VALID_TEST_TOKEN);
        when(context.getHeaders()).thenReturn(headers);

        filter.filter(context);

        verify(context.getHeaders()).putSingle("X-token-user", VALID_TEST_USERNAME);
    }

    @Test
    @DisplayName("Request gets aborted when no params are present")
    public void requestGetsAbortedWhenNoParamsTest() throws IOException {

        when(context.getUriInfo()).thenReturn(uriInfo);
        when(uriInfo.getQueryParameters()).thenReturn(queryParams);
        when(queryParams.size()).thenReturn(0);
        when(context.getHeaders()).thenReturn(headers);

        filter.filter(context);

        verify(context.getHeaders(), never()).putSingle(anyString(), anyString());
    }

    @Test
    @DisplayName("Request gets aborted when no token param is present")
    public void requestGetsAbortedWhenNoTokenTest() throws IOException {

        when(context.getUriInfo()).thenReturn(uriInfo);
        when(uriInfo.getQueryParameters()).thenReturn(queryParams);
        when(queryParams.size()).thenReturn(1);
        when(queryParams.get(TESTED_QUERY_PARAM)).thenReturn(null);

        when(context.getHeaders()).thenReturn(headers);

        filter.filter(context);

        verify(context.getHeaders(), never()).putSingle(eq("X-token-user"), anyString());
    }

    @Test
    @DisplayName("Request gets aborted when token is null")
    public void requestGetsAbortedWhenTokenIsNullTest() throws IOException {
        List<String> queryParamEntry = mock(List.class);

        when(context.getUriInfo()).thenReturn(uriInfo);
        when(uriInfo.getQueryParameters()).thenReturn(queryParams);
        when(queryParams.size()).thenReturn(1);
        when(queryParams.get(TESTED_QUERY_PARAM)).thenReturn(queryParamEntry);
        when(queryParams.get(TESTED_QUERY_PARAM).get(0)).thenReturn(null);

        when(context.getHeaders()).thenReturn(headers);

        filter.filter(context);

        verify(context.getHeaders(), never()).putSingle(eq("X-token-user"), anyString());
    }

    @Test
    @DisplayName("Request gets aborted when token is not signed with the right key")
    public void requestGetsAbortedWhenTokenIsNotSigned() throws IOException {
        final String WRONGLY_SIGNED_TOKEN =
                "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJCZXJ0LUphbiIsImlzcyI6Imhhbi1pY2EiLCJleHAiOjE1ODUyOTcyOTksImlhdCI6MTU4NTI5NzE5OX0.tjq1xNbT_TknyCP9BCIMeagYGWSZ_ytMxHVt3UWuoaA";
        List<String> queryParamEntry = mock(List.class);

        when(context.getUriInfo()).thenReturn(uriInfo);
        when(uriInfo.getQueryParameters()).thenReturn(queryParams);
        when(queryParams.size()).thenReturn(1);
        when(queryParams.get(TESTED_QUERY_PARAM)).thenReturn(queryParamEntry);
        when(queryParamEntry.get(anyInt())).thenReturn(WRONGLY_SIGNED_TOKEN);
        when(context.getHeaders()).thenReturn(headers);

        filter.filter(context);

        verify(context.getHeaders(), never()).putSingle(eq("X-token-user"), anyString());
    }

    @Test
    @DisplayName("Request gets aborted when token has expired")
    public void requestGetsAbortedWhenTokenHasExpired() throws IOException {
        final String EXPIRED_TOKEN =
                "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJCZXJ0LUphbiIsImlzcyI6Imhhbi1pY2EiLCJleHAiOjE1ODUyOTcyOTksImlhdCI6MTU4NTI5NzE5OX0._WLcF7Mn4E8vIVW8sL_sQK0oARW3L_K4q2FLTqGwVbA";
        List<String> queryParamEntry = mock(List.class);

        when(context.getUriInfo()).thenReturn(uriInfo);
        when(uriInfo.getQueryParameters()).thenReturn(queryParams);
        when(queryParams.size()).thenReturn(1);
        when(queryParams.get(TESTED_QUERY_PARAM)).thenReturn(queryParamEntry);
        when(queryParamEntry.get(anyInt())).thenReturn(EXPIRED_TOKEN);
        when(context.getHeaders()).thenReturn(headers);

        filter.filter(context);

        verify(context.getHeaders(), never()).putSingle(eq("X-token-user"), anyString());
    }
}
