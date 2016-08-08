package ml.fli.db.General;

import ml.fli.db.Dao.UserDao;
import ml.fli.db.models.User;
import ml.fli.db.utils.HibernateUtil;
import org.hibernate.Session;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by fab on 08.08.2016.
 */
public class UserDaoImpl implements UserDao {

    @Override
    public void addUser(User user) throws SQLException {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            session.save(user);
            session.getTransaction().commit();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if((session != null) && session.isOpen())
                session.close();
        }
    }

    @Override
    public User getUser(long id) throws SQLException {
        User result = null;
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            result = (User) session.load(User.class, id);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if ((session != null) && session.isOpen())
                session.close();


            return result;
        }
    }

    @Override
    public List<User> getUsers() throws SQLException {
        List<User> result = null;
        Session session = null;

        try {
            session = HibernateUtil.getSessionFactory().openSession();
            result = session.createCriteria(User.class).list();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if ((session != null) && session.isOpen())
                session.close();

            return result;
        }
    }
}
