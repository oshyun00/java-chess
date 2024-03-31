package chess.service;

import chess.exception.DBConnectionException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.NoSuchElementException;
import java.util.Properties;

public class ConnectionGenerator {
    private final Properties properties;

    public ConnectionGenerator(Properties properties) {
        this.properties = properties;
    }

    public static ConnectionGenerator from(String configurationFileName) {
        return new ConnectionGenerator(loadProperties(configurationFileName));
    }

    private static Properties loadProperties(String configurationFileName) {
        try {
            FileInputStream fileInputStream = new FileInputStream(configurationFileName);
            Properties properties = new Properties();
            properties.load(fileInputStream);
            return properties;
        } catch (FileNotFoundException e) {
            throw new NoSuchElementException("해당되는파일이 없습니다.");
        } catch (IOException e) {
            throw new IllegalArgumentException("파일을 읽어올 수 없습니다.");
        }
    }

    public Connection getConnection() {
        try {
            return DriverManager.getConnection(
                    "jdbc:mysql://" + properties.get("server") + "/"
                            + properties.get("database") + properties.get("option"),
                    properties.get("username").toString(), properties.get("password").toString());
        } catch (SQLException e) {
            handleSQLException(e);
            throw new DBConnectionException("데이터베이스 연결에 실패했습니다.");
        }
    }

    public void handleSQLException(SQLException e) {
        System.err.println("DB 연결 오류:" + e.getMessage());
        e.printStackTrace();
    }
}
