package oose.dea.spotitube.api;

import oose.dea.spotitube.dao.ILoginDAO;
import oose.dea.spotitube.domain.Eigenaar;
import oose.dea.spotitube.dto.EigenaarDTO;
import oose.dea.spotitube.dto.LoginDTO;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.ws.rs.core.Response;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SpotitubeTest {

    private static Spotitube spotitube;

    @BeforeAll
    public static void setup(){
        spotitube = new Spotitube();
    }

    @Test
    @DisplayName("Login with right credentials returns 201")
    public void loginWithRightCredentialsTest() {

        final String USERNAME = "Test Username 1";
        final String PASSWORD = "pw";
        final String TOKEN = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJCZXJ0LUphbiIsImlzcyI6Imhhbi1pY2EifQ.OizfPGJa-EBaRzZKNyk-3KEBNg43hn17TLLi35ZxK6M";
        final String FNAME = "Test Firstname 1";
        final String LNAME = "Test Lastname 1";

        LoginDTO loginDTO = new LoginDTO();
        loginDTO.user = USERNAME;
        loginDTO.password = PASSWORD;

        // Setup Mock
        ILoginDAO loginDAO = mock(ILoginDAO.class);
        Eigenaar eigenaar = new Eigenaar(USERNAME, TOKEN, FNAME, LNAME);
        when(loginDAO.login(USERNAME, PASSWORD)).thenReturn(eigenaar);

        spotitube.setILoginDAO(loginDAO);

        // run code
        Response response = spotitube.login(loginDTO);
        EigenaarDTO eigenaarDTO = (EigenaarDTO) response.getEntity();

        assertEquals(201, response.getStatus());
        assertEquals(FNAME + " " + LNAME, eigenaarDTO.user);
        assertFalse(eigenaarDTO.token.equals(""));

    }
}