package oose.dea.spotitube.api;

import oose.dea.spotitube.domain.Eigenaar;

import oose.dea.spotitube.dto.LoginDTO;
import oose.dea.spotitube.dto.EigenaarDTO;
import oose.dea.spotitube.dao.ILoginDAO;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Contains all general endpoints, like login.
 */

@Path("/")
public class Spotitube {

    ILoginDAO ILoginDAO;

    /**
     * Login endpoint
     * @param user The user object containing username and password
     * @return An Eigenaar object containing the fullname and JWT
     */

    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(LoginDTO user) {

        Eigenaar eigenaar = ILoginDAO.login(user.user, user.password);

        EigenaarDTO eigenaarDTO = new EigenaarDTO();
        eigenaarDTO.token = eigenaar.getToken();
        eigenaarDTO.user = eigenaar.getFullname();

        return Response.status(201).entity(eigenaarDTO).build();
    }

    @Inject
    public void setILoginDAO(ILoginDAO ILoginDAO) {
        this.ILoginDAO = ILoginDAO;
    }
}
