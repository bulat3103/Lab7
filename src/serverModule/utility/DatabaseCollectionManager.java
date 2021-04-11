package serverModule.utility;

import common.data.Chapter;
import common.data.Coordinates;
import common.data.SpaceMarine;
import common.data.Weapon;
import common.exceptions.DatabaseManagerException;
import common.utility.SpaceMarineLite;
import common.utility.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.TreeMap;

public class DatabaseCollectionManager {
    private final String SELECT_ALL_SPACE_MARINE = "SELECT * FROM " + DatabaseManager.MARINE_TABLE;
    private final String SELECT_SPACE_MARINE_BY_ID = SELECT_ALL_SPACE_MARINE + " WHERE " +
            DatabaseManager.MARINE_TABLE_ID_COLUMN + " = ?";
    private final String SELECT_SPACE_MARINE_BY_ID_AND_USER_ID = SELECT_SPACE_MARINE_BY_ID + " AND " +
            DatabaseManager.MARINE_TABLE_USER_ID_COLUMN + " = ?";
    private final String INSERT_SPACE_MARINE = "INSERT INTO " +
            DatabaseManager.MARINE_TABLE + " (" +
            DatabaseManager.MARINE_TABLE_KEY_COLUMN + ", " +
            DatabaseManager.MARINE_TABLE_NAME_COLUMN + ", " +
            DatabaseManager.MARINE_TABLE_COORDINATES_ID_COLUMN + ", " +
            DatabaseManager.MARINE_TABLE_CREATION_DATE_COLUMN + ", " +
            DatabaseManager.MARINE_TABLE_HEALTH_COLUMN + ", " +
            DatabaseManager.MARINE_TABLE_HEART_COUNT_COLUMN + ", " +
            DatabaseManager.MARINE_TABLE_ACHIEVEMENTS_COLUMN + ", " +
            DatabaseManager.MARINE_TABLE_WEAPON_TYPE_COLUMN + ", " +
            DatabaseManager.MARINE_TABLE_CHAPTER_ID_COLUMN + ", " +
            DatabaseManager.MARINE_TABLE_USER_ID_COLUMN + ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private final String DELETE_MARINE_BY_ID = "DELETE FROM " + DatabaseManager.MARINE_TABLE +
            " WHERE " + DatabaseManager.MARINE_TABLE_ID_COLUMN + " = ?";
    private final String UPDATE_MARINE_NAME_BY_ID = "UPDATE " + DatabaseManager.MARINE_TABLE + " SET " +
            DatabaseManager.MARINE_TABLE_NAME_COLUMN + " = ?" + " WHERE " +
            DatabaseManager.MARINE_TABLE_ID_COLUMN + " = ?";
    private final String UPDATE_MARINE_HEALTH_BY_ID = "UPDATE " + DatabaseManager.MARINE_TABLE + " SET " +
            DatabaseManager.MARINE_TABLE_HEALTH_COLUMN + " = ?" + " WHERE " +
            DatabaseManager.MARINE_TABLE_ID_COLUMN + " = ?";
    private final String UPDATE_MARINE_HEART_COUNT_BY_ID = "UPDATE " + DatabaseManager.MARINE_TABLE + " SET " +
            DatabaseManager.MARINE_TABLE_HEART_COUNT_COLUMN + " = ?" + " WHERE " +
            DatabaseManager.MARINE_TABLE_ID_COLUMN + " = ?";
    private final String UPDATE_MARINE_ACHIEVEMENTS_BY_ID = "UPDATE " + DatabaseManager.MARINE_TABLE + " SET " +
            DatabaseManager.MARINE_TABLE_ACHIEVEMENTS_COLUMN + " = ?" + " WHERE " +
            DatabaseManager.MARINE_TABLE_ID_COLUMN + " = ?";
    private final String UPDATE_MARINE_WEAPON_TYPE_BY_ID = "UPDATE " + DatabaseManager.MARINE_TABLE + " SET " +
            DatabaseManager.MARINE_TABLE_WEAPON_TYPE_COLUMN + " = ?" + " WHERE " +
            DatabaseManager.MARINE_TABLE_ID_COLUMN + " = ?";
    private final String SELECT_ALL_COORDINATES = "SELECT * FROM " + DatabaseManager.COORDINATES_TABLE;
    private final String SELECT_COORDINATES_BY_ID = SELECT_ALL_COORDINATES + " WHERE " + DatabaseManager.COORDINATES_TABLE_ID_COLUMN + " =?";
    private final String DELETE_COORDINATES_BY_ID = "DELETE FROM " + DatabaseManager.COORDINATES_TABLE +
            " WHERE " + DatabaseManager.COORDINATES_TABLE_ID_COLUMN + " = ?";
    private final String INSERT_COORDINATES = "INSERT INTO " +
            DatabaseManager.COORDINATES_TABLE + " (" +
            DatabaseManager.COORDINATES_TABLE_X_COLUMN + ", " +
            DatabaseManager.COORDINATES_TABLE_Y_COLUMN + ") VALUES (?, ?)";
    private final String UPDATE_COORDINATES_BY_ID = "UPDATE " + DatabaseManager.COORDINATES_TABLE + " SET " +
            DatabaseManager.COORDINATES_TABLE_X_COLUMN + " = ?, " +
            DatabaseManager.COORDINATES_TABLE_Y_COLUMN + " = ?" + " WHERE " +
            DatabaseManager.COORDINATES_TABLE_ID_COLUMN + " = ?";
    private final String SELECT_ALL_CHAPTERS = "SELECT * FROM " + DatabaseManager.CHAPTER_TABLE;
    private final String SELECT_CHAPTER_BY_ID = SELECT_ALL_CHAPTERS + " WHERE " + DatabaseManager.CHAPTER_TABLE_ID_COLUMN + " =?";
    private final String DELETE_CHAPTER_BY_ID = "DELETE FROM " + DatabaseManager.CHAPTER_TABLE +
            " WHERE " + DatabaseManager.CHAPTER_TABLE_ID_COLUMN + " = ?";
    private final String INSERT_CHAPTER = "INSERT INTO " +
            DatabaseManager.CHAPTER_TABLE + " (" +
            DatabaseManager.CHAPTER_TABLE_NAME_COLUMN + ", " +
            DatabaseManager.CHAPTER_TABLE_PARENT_LEGION_COLUMN + ") VALUES (?, ?)";
    private final String UPDATE_CHAPTER_BY_ID = "UPDATE " + DatabaseManager.CHAPTER_TABLE + " SET " +
            DatabaseManager.CHAPTER_TABLE_NAME_COLUMN + " = ?, " +
            DatabaseManager.CHAPTER_TABLE_PARENT_LEGION_COLUMN + " = ?" + " WHERE " +
            DatabaseManager.CHAPTER_TABLE_ID_COLUMN + " = ?";
    private DatabaseManager databaseManager;
    private DatabaseUserManager databaseUserManager;

    public DatabaseCollectionManager(DatabaseManager databaseManager, DatabaseUserManager databaseUserManager) {
        this.databaseManager = databaseManager;
        this.databaseUserManager = databaseUserManager;
    }

    private SpaceMarine returnSpaceMarine(ResultSet resultSet, int id) throws SQLException{
        String name = resultSet.getString(DatabaseManager.MARINE_TABLE_NAME_COLUMN);
        Coordinates coordinates = getCoordinatesByID(resultSet.getInt(DatabaseManager.MARINE_TABLE_COORDINATES_ID_COLUMN));
        LocalDateTime creationDate = resultSet.getTimestamp(DatabaseManager.MARINE_TABLE_CREATION_DATE_COLUMN).toLocalDateTime();
        int health = resultSet.getInt(DatabaseManager.MARINE_TABLE_HEALTH_COLUMN);
        Integer heartCount = resultSet.getInt(DatabaseManager.MARINE_TABLE_HEART_COUNT_COLUMN);
        String achieve = resultSet.getString(DatabaseManager.MARINE_TABLE_ACHIEVEMENTS_COLUMN);
        Weapon weaponType = Weapon.valueOf(resultSet.getString(DatabaseManager.MARINE_TABLE_WEAPON_TYPE_COLUMN));
        Chapter chapter = getChapterByID(resultSet.getInt(DatabaseManager.MARINE_TABLE_CHAPTER_ID_COLUMN));
        User owner = databaseUserManager.getUserById(resultSet.getInt(DatabaseManager.MARINE_TABLE_USER_ID_COLUMN));
        return new SpaceMarine(id, name, coordinates, creationDate, health, heartCount, achieve, weaponType, chapter, owner );
    }

    public TreeMap<Integer, SpaceMarine> getCollection() {
        TreeMap<Integer, SpaceMarine> marines = new TreeMap<>();
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = databaseManager.doPreparedStatement(SELECT_ALL_SPACE_MARINE, false);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt(DatabaseManager.MARINE_TABLE_ID_COLUMN);
                int key = resultSet.getInt(DatabaseManager.MARINE_TABLE_KEY_COLUMN);
                marines.put(key, returnSpaceMarine(resultSet, id));
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return marines;
    }

    private Coordinates getCoordinatesByID(int id) throws SQLException {
        Coordinates coordinates;
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = databaseManager.doPreparedStatement(SELECT_COORDINATES_BY_ID, false);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                coordinates = new Coordinates(
                        resultSet.getDouble(DatabaseManager.COORDINATES_TABLE_X_COLUMN),
                        resultSet.getFloat(DatabaseManager.COORDINATES_TABLE_Y_COLUMN)
                );
            } else throw new SQLException();
        } catch (SQLException e) {
            System.out.println("Произошла ошибка при выполнении запроса SELECT_COORDINATES_BY_ID!");
            throw new SQLException(e);
        } finally {
            databaseManager.closePreparedStatement(preparedStatement);
        }
        return coordinates;
    }

    private Chapter getChapterByID(int id) throws SQLException {
        Chapter chapter;
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = databaseManager.doPreparedStatement(SELECT_CHAPTER_BY_ID, false);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                chapter = new Chapter(
                        resultSet.getString(DatabaseManager.CHAPTER_TABLE_NAME_COLUMN),
                        resultSet.getString(DatabaseManager.CHAPTER_TABLE_PARENT_LEGION_COLUMN)
                );
            } else throw new SQLException();
        } catch (SQLException e) {
            System.out.println("Произошла ошибка при выполнении запроса SELECT_CHAPTER_BY_ID!");
            throw new SQLException(e);
        } finally {
            databaseManager.closePreparedStatement(preparedStatement);
        }
        return chapter;
    }

    private int getChapterIdBySpaceMarineID(int spaceMarineID) throws SQLException {
        int chapterID;
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = databaseManager.doPreparedStatement(SELECT_SPACE_MARINE_BY_ID, false);
            preparedStatement.setInt(1, spaceMarineID);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                chapterID = resultSet.getInt(DatabaseManager.MARINE_TABLE_CHAPTER_ID_COLUMN);
            } else throw new SQLException();
        } catch (SQLException e) {
            System.out.println("Произошла ошибка при выполнении запроса SELECT_SPACE_MARINE_BY_ID!");
            throw new SQLException(e);
        } finally {
            databaseManager.closePreparedStatement(preparedStatement);
        }
        return chapterID;
    }

    private int getCoordinatesIdBySpaceMarineID(int spaceMarineID) throws SQLException {
        int coordinatesID;
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = databaseManager.doPreparedStatement(SELECT_SPACE_MARINE_BY_ID, false);
            preparedStatement.setInt(1, spaceMarineID);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                coordinatesID = resultSet.getInt(DatabaseManager.MARINE_TABLE_COORDINATES_ID_COLUMN);
            } else throw new SQLException();
        } catch (SQLException e) {
            System.out.println("Произошла ошибка при выполнении запроса SELECT_SPACE_MARINE_BY_ID!");
            throw new SQLException(e);
        } finally {
            databaseManager.closePreparedStatement(preparedStatement);
        }
        return coordinatesID;
    }

    public SpaceMarine insertSpaceMarine(SpaceMarineLite marineLite, User user, int key) throws DatabaseManagerException {
        SpaceMarine marineToInsert;
        PreparedStatement insertMarine = null;
        PreparedStatement insertCoordinates = null;
        PreparedStatement insertChapter = null;
        try {
            databaseManager.setCommit();
            databaseManager.setSavepoint();

            LocalDateTime localDateTime = LocalDateTime.now();

            insertChapter = databaseManager.doPreparedStatement(INSERT_CHAPTER, true);
            insertChapter.setString(1, marineLite.getChapter().getName());
            insertChapter.setString(2, marineLite.getChapter().getParentLegion());
            if (insertChapter.executeUpdate() == 0) throw new SQLException();
            ResultSet resultSetChapter = insertChapter.getGeneratedKeys();
            int chapterID;
            if (resultSetChapter.next()) chapterID = resultSetChapter.getInt(1);
            else throw new SQLException();

            insertCoordinates = databaseManager.doPreparedStatement(INSERT_COORDINATES, true);
            insertCoordinates.setDouble(1, marineLite.getCoordinates().getX());
            insertCoordinates.setFloat(2, marineLite.getCoordinates().getY());
            if (insertCoordinates.executeUpdate() == 0) throw new SQLException();
            ResultSet resultSetCoordinates = insertCoordinates.getGeneratedKeys();
            int coordinatesID;
            if (resultSetCoordinates.next()) coordinatesID = resultSetCoordinates.getInt(1);
            else throw new SQLException();

            insertMarine = databaseManager.doPreparedStatement(INSERT_SPACE_MARINE, true);
            insertMarine.setInt(1, key);
            insertMarine.setString(2, marineLite.getName());
            insertMarine.setInt(3, coordinatesID);
            insertMarine.setTimestamp(4, Timestamp.valueOf(localDateTime));
            insertMarine.setInt(5, marineLite.getHealth());
            insertMarine.setInt(6, marineLite.getHeartCount());
            insertMarine.setString(7, marineLite.getAchievements());
            insertMarine.setString(8, marineLite.getWeaponType().toString());
            insertMarine.setInt(9, chapterID);
            insertMarine.setInt(10, databaseUserManager.getUserIdByUsername(user));
            if (insertMarine.executeUpdate() == 0) throw new SQLException();
            ResultSet resultSetMarine = insertMarine.getGeneratedKeys();
            int spaceMarineID;
            if (resultSetMarine.next()) spaceMarineID = resultSetMarine.getInt(1);
            else throw new SQLException();
            marineToInsert = new SpaceMarine(
                    spaceMarineID,
                    marineLite.getName(),
                    marineLite.getCoordinates(),
                    localDateTime,
                    marineLite.getHealth(),
                    marineLite.getHeartCount(),
                    marineLite.getAchievements(),
                    marineLite.getWeaponType(),
                    marineLite.getChapter(),
                    user
            );
            databaseManager.commit();
            return marineToInsert;
        } catch (SQLException exception) {
            System.out.println("Произошла ошибка при добавлении нового объекта в БД!");
            exception.printStackTrace();
            databaseManager.rollback();
            throw new DatabaseManagerException();
        } finally {
            databaseManager.closePreparedStatement(insertChapter);
            databaseManager.closePreparedStatement(insertCoordinates);
            databaseManager.closePreparedStatement(insertMarine);
            databaseManager.setAutoCommit();
        }
    }

    public void updateSpaceMarineByID(int spaceMarineID, SpaceMarineLite marineLite) throws DatabaseManagerException {
        PreparedStatement updateSpaceMarineName = null;
        PreparedStatement updateSpaceMarineCoordinates = null;
        PreparedStatement updateSpaceMarineHealth = null;
        PreparedStatement updateSpaceMarineHeartCount = null;
        PreparedStatement updateSpaceMarineAchievements = null;
        PreparedStatement updateSpaceMarineWeaponType = null;
        PreparedStatement updateSpaceMarineChapter = null;
        try {
            databaseManager.setCommit();
            databaseManager.setSavepoint();

            updateSpaceMarineName = databaseManager.doPreparedStatement(UPDATE_MARINE_NAME_BY_ID, false);
            updateSpaceMarineCoordinates = databaseManager.doPreparedStatement(UPDATE_COORDINATES_BY_ID, false);
            updateSpaceMarineHealth = databaseManager.doPreparedStatement(UPDATE_MARINE_HEALTH_BY_ID, false);
            updateSpaceMarineHeartCount = databaseManager.doPreparedStatement(UPDATE_MARINE_HEART_COUNT_BY_ID, false);
            updateSpaceMarineAchievements = databaseManager.doPreparedStatement(UPDATE_MARINE_ACHIEVEMENTS_BY_ID, false);
            updateSpaceMarineWeaponType = databaseManager.doPreparedStatement(UPDATE_MARINE_WEAPON_TYPE_BY_ID, false);
            updateSpaceMarineChapter = databaseManager.doPreparedStatement(UPDATE_CHAPTER_BY_ID, false);

            if (marineLite.getName() != null) {
                updateSpaceMarineName.setString(1, marineLite.getName());
                updateSpaceMarineName.setInt(2, spaceMarineID);
                if (updateSpaceMarineName.executeUpdate() == 0) throw new SQLException();
            }
            if (marineLite.getCoordinates() != null) {
                updateSpaceMarineCoordinates.setDouble(1, marineLite.getCoordinates().getX());
                updateSpaceMarineCoordinates.setFloat(2, marineLite.getCoordinates().getY());
                updateSpaceMarineCoordinates.setInt(3, getCoordinatesIdBySpaceMarineID(spaceMarineID));
                if (updateSpaceMarineCoordinates.executeUpdate() == 0) throw new SQLException();
            }
            if (marineLite.getHealth() != -1) {
                updateSpaceMarineHealth.setInt(1, marineLite.getHealth());
                updateSpaceMarineHealth.setInt(2, spaceMarineID);
                if (updateSpaceMarineHealth.executeUpdate() == 0) throw new SQLException();
            }
            if (marineLite.getHeartCount() != -1) {
                updateSpaceMarineHeartCount.setInt(1, marineLite.getHeartCount());
                updateSpaceMarineHeartCount.setInt(2, spaceMarineID);
                if (updateSpaceMarineHeartCount.executeUpdate() == 0) throw new SQLException();
            }
            if (marineLite.getAchievements() != null) {
                updateSpaceMarineAchievements.setString(1, marineLite.getAchievements());
                updateSpaceMarineAchievements.setInt(2, spaceMarineID);
                if (updateSpaceMarineAchievements.executeUpdate() == 0) throw new SQLException();
            }
            if (marineLite.getWeaponType() != null) {
                updateSpaceMarineWeaponType.setString(1, marineLite.getWeaponType().toString());
                updateSpaceMarineWeaponType.setInt(2, spaceMarineID);
                if (updateSpaceMarineWeaponType.executeUpdate() == 0) throw new SQLException();
            }
            if (marineLite.getChapter() != null) {
                updateSpaceMarineChapter.setString(1, marineLite.getChapter().getName());
                updateSpaceMarineChapter.setString(2, marineLite.getChapter().getParentLegion());
                updateSpaceMarineChapter.setInt(3, getChapterIdBySpaceMarineID(spaceMarineID));
                if (updateSpaceMarineChapter.executeUpdate() == 0) throw new SQLException();
            }
            databaseManager.commit();
        } catch (SQLException exception) {
            System.out.println("Произошла ошибка при выполнении группы запросов на обновление объекта!");
            databaseManager.rollback();
            throw new DatabaseManagerException();
        } finally {
            databaseManager.closePreparedStatement(updateSpaceMarineName);
            databaseManager.closePreparedStatement(updateSpaceMarineCoordinates);
            databaseManager.closePreparedStatement(updateSpaceMarineHealth);
            databaseManager.closePreparedStatement(updateSpaceMarineHeartCount);
            databaseManager.closePreparedStatement(updateSpaceMarineAchievements);
            databaseManager.closePreparedStatement(updateSpaceMarineWeaponType);
            databaseManager.closePreparedStatement(updateSpaceMarineChapter);
            databaseManager.setAutoCommit();
        }
    }

    public boolean checkSpaceMarineByIdAndUserId(int spaceMarineID, User user) throws DatabaseManagerException {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = databaseManager.doPreparedStatement(SELECT_SPACE_MARINE_BY_ID_AND_USER_ID, false);
            preparedStatement.setInt(1, spaceMarineID);
            preparedStatement.setInt(2, databaseUserManager.getUserIdByUsername(user));
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        } catch (SQLException exception) {
            System.out.println("Произошла ошибка при выполнении запроса SELECT_SPACE_MARINE_BY_ID_AND_USER_ID!");
            throw new DatabaseManagerException();
        } finally {
            databaseManager.closePreparedStatement(preparedStatement);
        }
    }

    public void deleteSpaceMarineById(int spaceMarineID) throws DatabaseManagerException {
        PreparedStatement deleteSpaceMarine = null;
        PreparedStatement deleteCoordinates = null;
        PreparedStatement deleteChapter = null;
        try {
            int coordinatesID = getCoordinatesIdBySpaceMarineID(spaceMarineID);
            int chapterID = getChapterIdBySpaceMarineID(spaceMarineID);
            deleteSpaceMarine = databaseManager.doPreparedStatement(DELETE_MARINE_BY_ID, false);
            deleteSpaceMarine.setInt(1, spaceMarineID);
            if (deleteSpaceMarine.executeUpdate() == 0) throw new SQLException();
            deleteCoordinates = databaseManager.doPreparedStatement(DELETE_COORDINATES_BY_ID, false);
            deleteCoordinates.setInt(1, coordinatesID);
            if (deleteCoordinates.executeUpdate() == 0) throw new SQLException();
            deleteChapter = databaseManager.doPreparedStatement(DELETE_CHAPTER_BY_ID, false);
            deleteChapter.setInt(1, chapterID);
            if (deleteChapter.executeUpdate() == 0) throw new SQLException();
        } catch (SQLException exception) {
            System.out.println("Произошла ошибка при выполнении запроса DELETE_MARINE_BY_ID!");
            throw new DatabaseManagerException();
        } finally {
            databaseManager.closePreparedStatement(deleteSpaceMarine);
            databaseManager.closePreparedStatement(deleteCoordinates);
            databaseManager.closePreparedStatement(deleteChapter);
        }
    }

    public void clearCollection() throws DatabaseManagerException{
        TreeMap<Integer, SpaceMarine> marines = getCollection();
        for (SpaceMarine marine : marines.values()) {
            deleteSpaceMarineById(marine.getId());
        }
    }
}
