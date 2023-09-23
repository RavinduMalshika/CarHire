package lk.ijse.carhire.service;

import lk.ijse.carhire.service.custom.impl.*;

public class ServiceFactory {
    private static ServiceFactory serviceFactory;

    private ServiceFactory() {}

    public static ServiceFactory getInstance() {
        return serviceFactory == null ?
                serviceFactory = new ServiceFactory()
                : serviceFactory;
    }

    public enum ServiceTypes {
        CAR, CAR_BRAND, CAR_CATEGORY, CAR_MODEL, CUSTOMER, RENT, USER
    }

    public SuperService getService(ServiceTypes type) {
        switch (type) {
            case CAR:
                return new CarServiceImpl();
            case CAR_BRAND:
                return new CarBrandServiceImpl();
            case CAR_CATEGORY:
                return new CarCategoryServiceImpl();
            case CAR_MODEL:
                return new CarModelServiceImpl();
            case CUSTOMER:
                return new CustomerServiceImpl();
            case RENT:
                return  new RentServiceImpl();
            case USER:
                return new UserServiceImpl();
            default:
                return null;
        }
    }
}
