package oose.dea.spotitube.dao;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import oose.dea.spotitube.domain.Eigenaar;
import oose.dea.spotitube.util.JWTProperties;
import org.springframework.security.crypto.bcrypt.BCrypt;

import javax.annotation.Resource;
import javax.enterprise.inject.Default;
import javax.sql.DataSource;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.NotAuthorizedException;
import java.sql.*;
import java.time.Instant;

@Default
public class LoginDAO implements ILoginDAO {
    @Resource(name = "jdbc/Spotitube")
    DataSource dataSource;

    @Override
    public Eigenaar login(String gebruikersnaam, String wachtwoord) {

        try {
            Connection connection = dataSource.getConnection();
            String sql = "select firstname, lastname, password from Gebruikers where username = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, gebruikersnaam);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String pass_from_the_database = resultSet.getString("password");
                String firstname = resultSet.getString("firstname");
                String lastname = resultSet.getString("lastname");

                String jwt_key = new JWTProperties().getProperty("jwt_key");
                if (jwt_key == null) {
                    throw new InternalServerErrorException("Something went wrong getting the secret key.");
                }

                if (BCrypt.checkpw(wachtwoord, pass_from_the_database)) {

                    try {
                        Algorithm algorithm = Algorithm.HMAC256(jwt_key);

                        final long iatTime = Instant.now().toEpochMilli();
                        final long expTime = iatTime + 21600000; // 21600000 = 6 hours

                        String token = JWT.create()
                                .withIssuer("han-ica")
                                .withClaim("sub", gebruikersnaam)
                                .withIssuedAt(new Date(iatTime))
                                .withExpiresAt(new Date(expTime))
                                .sign(algorithm);

                        return new Eigenaar(gebruikersnaam, token, firstname, lastname);

                    } catch (JWTCreationException exception) {
                        throw new InternalServerErrorException("Something went wrong creating the token.");
                    } finally {
                        statement.close();
                        connection.close();
                    }
                }
            }

            throw new NotAuthorizedException("Unauthorized");
        } catch (SQLException e) {
            throw new InternalServerErrorException("Something went wrong.");
        }
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }
}
