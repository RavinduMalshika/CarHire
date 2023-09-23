package lk.ijse.carhire;

import lk.ijse.carhire.util.SessionFactoryConfiguration;
import org.hibernate.Session;

public class Main {
    public static void main(String[] args) {
        Session session = SessionFactoryConfiguration.getInstance().getSession();
    }
}
