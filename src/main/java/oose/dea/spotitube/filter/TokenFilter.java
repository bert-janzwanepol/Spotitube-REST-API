package oose.dea.spotitube.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import oose.dea.spotitube.util.JWTProperties;

import javax.annotation.Priority;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

@Provider
@Priority(1)
@Token
public class TokenFilter implements ContainerRequestFilter {

    /**
     * Checks if a JWT queryparameter is present and attempts to validate the JWT.
     * If the JWT is valid, the username in the JWT will be added to the request headers.
     * @param requestContext JAX-RS requestcontext
     */

    @Override
    public void filter(ContainerRequestContext requestContext) {
        MultivaluedMap<String, String> queryParameters = requestContext.getUriInfo().getQueryParameters();
        JWTProperties properties = new JWTProperties();
        String jwt_key = properties.getProperty("jwt_key");

        // check if jwt secret has loaded properly
        if(jwt_key == null) {
            requestContext.abortWith(Response.status(500).build());
            return;
        }

        // check if token is present in queryparams
        // checking for .get(0) == null so "http://path/resource?token=" doesn't give an error
        if (queryParameters.size() == 0 || queryParameters.get("token") == null || queryParameters.get("token").get(0) == null) {
            requestContext.abortWith(Response.status(400).build());
            return;
        }

        try {
            String token = queryParameters.get("token").get(0);
            Algorithm algorithm = Algorithm.HMAC256(jwt_key);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer("han-ica")
                    .build();
            DecodedJWT jwt = verifier.verify(token);
            requestContext.getHeaders().putSingle("X-token-user", jwt.getSubject());
        } catch (JWTVerificationException | IllegalArgumentException exception) {
            requestContext.abortWith(Response.status(403).build());
        }
    }
}