package ml.fli.db.utils;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

/**
 * Created by fab on 08.08.2016.
 */
public class HibernateUtil {

    private static SessionFactory sessionFactory;
    //private static ServiceRegistry serviceRegistry;


    static{
        try {
            sessionFactory = new Configuration().configure().buildSessionFactory();
        }catch (Throwable e){
            throw new ExceptionInInitializerError(e);
        }
    }

   /* private static SessionFactory configureSessionFactory() {
        Configuration configuration = new Configuration();
        configuration.configure();
        serviceRegistry = new StandardServiceRegistryBuilder().applySettings(
                configuration.getProperties()).buildServiceRegistry();
        sessionFactory = configuration.buildSessionFactory(serviceRegistry);
        return sessionFactory;
    }*/

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    private HibernateUtil(){ }



}
