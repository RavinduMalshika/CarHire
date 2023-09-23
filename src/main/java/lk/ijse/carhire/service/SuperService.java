package lk.ijse.carhire.service;

import lk.ijse.carhire.util.SessionFactoryConfiguration;
import org.hibernate.Session;

public interface SuperService {
    Session session = SessionFactoryConfiguration.getInstance().getSession();
}
