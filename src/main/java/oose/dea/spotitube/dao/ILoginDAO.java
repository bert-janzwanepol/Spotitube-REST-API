package oose.dea.spotitube.dao;

import oose.dea.spotitube.domain.Eigenaar;

public interface ILoginDAO {

    /**
     * Handles a login request.
     * @param gebruikersnaam The username of the user.
     * @param wachtwoord The password of the user
     * @return An Eigenaar object containing the fullname and JWT.
     */
    public Eigenaar login(String gebruikersnaam, String wachtwoord);

}
