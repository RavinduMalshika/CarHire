package lk.ijse.carhire.service.custom.impl;

import lk.ijse.carhire.dao.DaoFactory;
import lk.ijse.carhire.dao.custom.CarBrandDao;
import lk.ijse.carhire.dao.custom.CarCategoryDao;
import lk.ijse.carhire.dao.custom.CarDao;
import lk.ijse.carhire.dao.custom.CarModelDao;
import lk.ijse.carhire.dto.CarDto;
import lk.ijse.carhire.entity.CarEntity;
import lk.ijse.carhire.service.custom.CarService;
import org.hibernate.Transaction;

import java.util.ArrayList;
import java.util.List;

public class CarServiceImpl implements CarService {
    CarDao carDao = (CarDao) DaoFactory.getInstance().getDao(DaoFactory.DaoTypes.CAR);
    CarCategoryDao carCategoryDao = (CarCategoryDao) DaoFactory.getInstance().getDao(DaoFactory.DaoTypes.CAR_CATEGORY);
    CarBrandDao carBrandDao = (CarBrandDao) DaoFactory.getInstance().getDao(DaoFactory.DaoTypes.CAR_BRAND);
    CarModelDao carModelDao = (CarModelDao) DaoFactory.getInstance().getDao(DaoFactory.DaoTypes.CAR_MODEL);

    @Override
    public String saveCar(CarDto carDto) {
        Transaction transaction = session.beginTransaction();
        CarEntity carEntity = new CarEntity();

        carEntity.setId(carDto.getId());
        carEntity.setVehicleNumber(carDto.getVehicleNumber());
        carEntity.setCarCategoryEntity(carCategoryDao.get(carDto.getCarCategory(), session));
        carEntity.setCarBrandEntity(carBrandDao.get(carDto.getCarBrand(), session));
        carEntity.setCarModelEntity(carModelDao.get(carDto.getCarModel(), session));
        carEntity.setYear(carDto.getYear());
        carEntity.setDailyRental(carDto.getDailyRental());
        carEntity.setAvailability(carDto.getAvailability());
        carEntity.setCreatedBy(carDto.getCreatedBy());
        System.out.println("From Service: " + carEntity.toString());

        if(carDao.save(carEntity, session)) {
            transaction.commit();
            return "Successfully Saved";
        } else {
            transaction.rollback();
            return "Failed";
        }
    }

    @Override
    public String updateCar(CarDto carDto) {
        session.clear();
        Transaction transaction = session.beginTransaction();
        CarEntity carEntity = new CarEntity();

        carEntity.setId(carDto.getId());
        carEntity.setVehicleNumber(carDto.getVehicleNumber());
        carEntity.setCarCategoryEntity(carCategoryDao.get(carDto.getCarCategory(), session));
        carEntity.setCarBrandEntity(carBrandDao.get(carDto.getCarBrand(), session));
        carEntity.setCarModelEntity(carModelDao.get(carDto.getCarModel(), session));
        carEntity.setYear(carDto.getYear());
        carEntity.setDailyRental(carDto.getDailyRental());
        carEntity.setAvailability(carDto.getAvailability());
        carEntity.setCreatedBy(carDto.getCreatedBy());
        carEntity.setUpdatedBy(carDto.getUpdatedBy());
        System.out.println("From Service: " + carEntity.toString());

        if(carDao.update(carEntity, session)) {
            transaction.commit();
            return "Successfully Saved";
        } else {
            transaction.rollback();
            return "Failed";
        }
    }

    @Override
    public String deleteCar(String id) {
        Transaction transaction = session.beginTransaction();
        if(carDao.delete(id, session)) {
            transaction.commit();
            return "Successfully Saved";
        } else {
            transaction.rollback();
            return "Failed";
        }
    }

    @Override
    public CarDto getCar(String id) {
        CarEntity carEntity = carDao.get(id, session);
        return carEntity != null ? new CarDto(
                carEntity.getId(),
                carEntity.getVehicleNumber(),
                carEntity.getCarCategoryEntity().getCategory(),
                carEntity.getCarBrandEntity().getBrand(),
                carEntity.getCarModelEntity().getModel(),
                carEntity.getYear(),
                carEntity.getDailyRental(),
                carEntity.getAvailability(),
                carEntity.getCreatedBy(),
                carEntity.getUpdatedBy()
        ) : null;
    }

    @Override
    public List<CarDto> getAllCars() {
        ArrayList<CarDto> carDtos = new ArrayList<>();
        List<CarEntity> carEntities = carDao.getAll(session);

        for(CarEntity carEntity : carEntities) {
            CarDto carDto = new CarDto(
                    carEntity.getId(),
                    carEntity.getVehicleNumber(),
                    carEntity.getCarCategoryEntity().getCategory(),
                    carEntity.getCarBrandEntity().getBrand(),
                    carEntity.getCarModelEntity().getModel(),
                    carEntity.getYear(),
                    carEntity.getDailyRental(),
                    carEntity.getAvailability(),
                    carEntity.getCreatedBy(),
                    carEntity.getUpdatedBy()
            );
            carDtos.add(carDto);
        }
        return carDtos;
    }
}
