package ru.skyfire.zeta.dailyrewards.database;

import ru.skyfire.zeta.dailyrewards.DailyRewards;

import java.io.IOException;
import java.nio.file.Path;
import java.sql.*;
import java.util.UUID;

import static ru.skyfire.zeta.dailyrewards.DailyRewards.logger;

public class SqliteEntry {
    private static final String TABLE_NAME = "player_login";
    private Path databasePath;
    private Connection connection;

    public SqliteEntry() {
        initPath();
        initConnection();
        initDb();
    }

    private void initPath() {
        databasePath = DailyRewards.getInst().getConfigDir().resolve("player_login.db");
        if(!databasePath.toFile().exists()) {
            try {
                databasePath.toFile().createNewFile();
            } catch (IOException e) {
                logger.error("Can not create file for database: " + databasePath.toString());
            }
        }
    }

    private void initConnection() {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:" + databasePath.toString());
        } catch (ClassNotFoundException e) {
            logger.error("Can not load sqlite driver");
        } catch (SQLException e) {
            logger.error("Can not create connection to database");
        }
    }

    private void initDb() {
        try {
            DatabaseMetaData dbm = connection.getMetaData();
            ResultSet tables = dbm.getTables(null, null, TABLE_NAME, null);
            if (!tables.next()) {
                createNewTable();
            }
        } catch (SQLException e) {
            logger.error("Failed to load database meta data");
        }
    }

    private void createNewTable() {
        String sql1 = "CREATE TABLE player_login (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                "uuid CHARACTER(36) NOT NULL, " +
                "day INTEGER, " +
                "status INTEGER" +
                ");";
        String sql2 = "CREATE UNIQUE INDEX "+TABLE_NAME+"_id_uindex ON "+TABLE_NAME+" (id);";
        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(sql1);
            stmt.executeUpdate(sql2);
            stmt.close();
        } catch (SQLException e) {
            logger.error("Failed to create table");
            e.printStackTrace();
        }
    }

    public void addEntry(UUID uuid, int day, int status) {
        String sql = "INSERT INTO " + TABLE_NAME + "(uuid, day, status) VALUES (?, ?, ?);";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, uuid.toString());
            stmt.setInt(2, day);
            stmt.setInt(3, status);
            stmt.executeUpdate();
        } catch (SQLException e) {
            logSqlError(sql);
            e.printStackTrace();
        }
    }

    public void updateEntry(UUID uuid, int day, int status) {
        String sql = "UPDATE " + TABLE_NAME + " SET status = ?, day = ? WHERE uuid = ?;";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(3, uuid.toString());
            stmt.setInt(2, day);
            stmt.setInt(1, status);
            stmt.executeUpdate();
        } catch (SQLException e) {
            logSqlError(sql);
        }
    }

    public int getStatus(UUID uuid){
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE uuid = ?;";
        int result = -1;
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, uuid.toString());
            ResultSet resultSet = stmt.executeQuery();
            if (!resultSet.next() || resultSet.isClosed()) {
                logger.error("There is no such a player: "+uuid.toString());
                return -1;
            }
            result = resultSet.getInt("status");
            return result;
        }
        catch (SQLException e){
            logSqlError(sql);
            e.printStackTrace();
        }
        return result;
    }

    public int getCurrentDay(UUID uuid){
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE uuid = ?;";
        int result = 0;
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, uuid.toString());
            ResultSet resultSet = stmt.executeQuery();
            if (!resultSet.next() || resultSet.isClosed()) {
                logger.error("There is no such a player: "+uuid.toString());
                return 0;
            }
            result = resultSet.getInt("day");
        }
        catch (SQLException e){
            logSqlError(sql);
            e.printStackTrace();
        }
        return result;
    }

    public void clearStatuses(){
        String sql = "UPDATE " + TABLE_NAME + " SET status = ? WHERE status = ?;";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, 0);
            stmt.setInt(2, 1);
            if (stmt.executeUpdate()==0){
                logger.error("All statuses are 0 already!");
            }
        } catch (SQLException e) {
            logSqlError(sql);
            e.printStackTrace();
        }
    }

    public void clearDaysHard(){
        String sql = "UPDATE " + TABLE_NAME + " SET day = ? WHERE status = ?;";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, 1);
            stmt.setInt(2, 0);
            if (stmt.executeUpdate()==0){
                logger.error("All statuses are 0 already!");
            }
        } catch (SQLException e) {
            logSqlError(sql);
            e.printStackTrace();
        }
    }

    private void logSqlError(String sql) {
        logger.error("[SQL] Failed to execute: " + sql);
    }

}
