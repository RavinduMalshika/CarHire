package lk.ijse.carhire.util;

import lk.ijse.carhire.entity.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class SessionFactoryConfiguration {
    private static SessionFactoryConfiguration sessionFactoryConfiguration;
    private SessionFactory sessionFactory;

    private SessionFactoryConfiguration() {
        Configuration configuration = new Configuration().configure()
                .addAnnotatedClass(CarEntity.class)
                .addAnnotatedClass(CarBrandEntity.class)
                .addAnnotatedClass(CarCategoryEntity.class)
                .addAnnotatedClass(CarModelEntity.class)
                .addAnnotatedClass(CustomerEntity.class)
                .addAnnotatedClass(UserEntity.class)
                .addAnnotatedClass(RentEntity.class);

        sessionFactory = configuration.buildSessionFactory();
    }

    public static SessionFactoryConfiguration getInstance() {
        return sessionFactoryConfiguration == null ?
                sessionFactoryConfiguration = new SessionFactoryConfiguration()
                : sessionFactoryConfiguration;
    }

    public Session getSession() {
        return sessionFactory.openSession();
    }
}
