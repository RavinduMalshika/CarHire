package lk.ijse.carhire.dao;

import lk.ijse.carhire.dao.custom.impl.*;

public class DaoFactory {
    private static DaoFactory daoFactory;

    private DaoFactory() {}

    public static DaoFactory getInstance() {
        return daoFactory == null ?
                daoFactory = new DaoFactory() :
                daoFactory;
    }

    public enum DaoTypes {
        CAR, CAR_BRAND, CAR_CATEGORY, CAR_MODEL, CUSTOMER, RENT, USER
    }

    public SuperDao getDao(DaoTypes type) {
        switch (type) {
            case CAR :
                return new CarDaoImpl();
            case CAR_BRAND :
                return new CarBrandDaoImpl();
            case CAR_CATEGORY :
                return new CarCategoryDaoImpl();
            case CAR_MODEL :
                return new CarModelDaoImpl();
            case CUSTOMER :
                return new CustomerDaoImpl();
            case RENT :
                return new RentDaoImpl();
            case USER :
                return new UserDaoImpl();
            default :
                return null;
        }
    }
}
