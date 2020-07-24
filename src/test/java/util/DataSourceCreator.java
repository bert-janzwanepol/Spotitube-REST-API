package util;

import org.apache.commons.dbcp.BasicDataSource;

import javax.sql.DataSource;

public class DataSourceCreator {

    private static DataSourceCreator instance;

    private DataSourceCreator() {}

    public static DataSourceCreator getInstance() {
        if (instance == null) {
            return new DataSourceCreator();
        };

        return instance;
    }

    public DataSource getDataSource() {
        BasicDataSource dataSource = new BasicDataSource();

        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://localhost:3306/test_spotitube");
        dataSource.setUsername("YOUR_MYSQL_USERNAME");
        dataSource.setPassword("YOUR_MYSQL_PASSWORD");

        return dataSource;
    }
}