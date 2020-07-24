package util;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.ibatis.jdbc.ScriptRunner;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.SQLException;

public class Seed {

    private DataSource dataSource;
    private static Seed instance;

    private Seed(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public static Seed getInstance(DataSource dataSource) {
        if (instance == null) {
            return new Seed(dataSource);
        }

        return instance;
    }

    public void runScript(String scriptName) throws IOException, NullPointerException, SQLException {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(scriptName);

        Reader in = new InputStreamReader(inputStream, StandardCharsets.UTF_8);

        Connection connection = dataSource.getConnection();

        ScriptRunner sr = new ScriptRunner(connection);
        sr.setAutoCommit(true);

        sr.runScript(in);
    }
}