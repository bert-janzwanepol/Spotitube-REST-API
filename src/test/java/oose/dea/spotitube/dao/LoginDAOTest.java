package oose.dea.spotitube.dao;

import oose.dea.spotitube.domain.Eigenaar;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import util.DataSourceCreator;
import util.Seed;

import javax.sql.DataSource;
import javax.ws.rs.NotAuthorizedException;

import static org.junit.jupiter.api.Assertions.*;

public class LoginDAOTest {

    private LoginDAO loginDAO;
    private final String USERNAME = "Test Username 1";
    private final String PASSWORD = "Test Password 1";
    private final String WRONG_PASSWORD = "Wrong Test Password";

    @BeforeEach
    public void setup() {
        DataSource dataSource = DataSourceCreator.getInstance().getDataSource();
        Seed seed = Seed.getInstance(dataSource);

        try {
            seed.runScript("DDL.sql");
        } catch (Exception e) {
            fail(e.getMessage());
        }

        loginDAO = new LoginDAO();
        loginDAO.setDataSource(dataSource);
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

    @Test
    @DisplayName("Login with valid credentials returns Eigenaar")
    public void loginWithValidCredentialsSucceedsTest(){
        Eigenaar eigenaar = loginDAO.login(USERNAME, PASSWORD);

        assertNotNull(eigenaar);
    }

    @Test
    @DisplayName("Login with invalid credentials throws exception")
    public void loginWithInValidCredentialsTestFails(){

        assertThrows(NotAuthorizedException.class, () -> {
            loginDAO.login(USERNAME, WRONG_PASSWORD);
        });

        assertThrows(NotAuthorizedException.class, () -> {
            loginDAO.login(null, "false");
        });

    }
}
