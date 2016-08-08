package ml.fli.db.Dao;

import ml.fli.db.models.User;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by fab on 08.08.2016.
 */
public interface UserDao {
    public void addUser(User user) throws SQLException;
    public User getUser(long id) throws SQLException;
    public List<User> getUsers() throws SQLException;

}
