package lk.ijse.carhire.service.custom.impl;

import lk.ijse.carhire.dao.DaoFactory;
import lk.ijse.carhire.dao.custom.CarBrandDao;
import lk.ijse.carhire.dao.custom.CarCategoryDao;
import lk.ijse.carhire.dao.custom.CarModelDao;
import lk.ijse.carhire.dto.CarModelDto;
import lk.ijse.carhire.entity.CarModelEntity;
import lk.ijse.carhire.service.custom.CarModelService;
import org.hibernate.Transaction;

import java.util.ArrayList;
import java.util.List;

public class CarModelServiceImpl implements CarModelService {
    CarModelDao carModelDao = (CarModelDao) DaoFactory.getInstance().getDao(DaoFactory.DaoTypes.CAR_MODEL);
    CarCategoryDao carCategoryDao = (CarCategoryDao) DaoFactory.getInstance().getDao(DaoFactory.DaoTypes.CAR_CATEGORY);
    CarBrandDao carBrandDao = (CarBrandDao) DaoFactory.getInstance().getDao(DaoFactory.DaoTypes.CAR_BRAND);

    @Override
    public String saveModel(CarModelDto carModelDto) {
        Transaction transaction = session.beginTransaction();
        CarModelEntity carModelEntity = new CarModelEntity();

        carModelEntity.setModel(carModelDto.getModel());
        carModelEntity.setCarBrandEntity(carBrandDao.get(carModelDto.getBrand(), session));
        carModelEntity.setCarCategoryEntity(carCategoryDao.get(carModelDto.getCategory(), session));
        carModelEntity.setCreatedBy(carModelDto.getCreatedBy());

        if(carModelDao.save(carModelEntity, session)) {
            transaction.commit();
            return "Successfully Saved";
        } else {
            transaction.rollback();
            return "Failed";
        }
    }

    @Override
    public String updateModel(CarModelDto carModelDto, String previousModel) {
        Transaction transaction = session.beginTransaction();
        CarModelEntity carModelEntity = new CarModelEntity();

        carModelEntity.setModel(carModelDto.getModel());
        carModelEntity.setCarBrandEntity(carBrandDao.get(carModelDto.getBrand(), session));
        carModelEntity.setCarCategoryEntity(carCategoryDao.get(carModelDto.getCategory(), session));
        carModelEntity.setCreatedBy(carModelDto.getCreatedBy());
        carModelEntity.setUpdatedBy(carModelDto.getUpdatedBy());

        if(carModelDao.update(carModelEntity, session, previousModel)) {
            transaction.commit();
            return "Successfully Updated";
        } else {
            transaction.rollback();
            return "Failed";
        }
    }

    @Override
    public String deleteModel(String model) {
        Transaction transaction = session.beginTransaction();
        if(carModelDao.delete(model, session)) {
            transaction.commit();
            return "Successfully Deleted";
        } else {
            transaction.rollback();
            return "Failed";
        }
    }

    @Override
    public CarModelDto getModel(String model) {
        CarModelEntity carModelEntity = carModelDao.get(model, session);
        return new CarModelDto(
                carModelEntity.getModel(),
                carModelEntity.getCarCategoryEntity().getCategory(),
                carModelEntity.getCarBrandEntity().getBrand(),
                carModelEntity.getCreatedBy(),
                carModelEntity.getUpdatedBy()
        );
    }

    @Override
    public List<CarModelDto> getAllModels() {
        ArrayList<CarModelDto> carModelDtos = new ArrayList<>();
        List<CarModelEntity> carModelEntities = carModelDao.getAll(session);

        for (CarModelEntity carModelEntity : carModelEntities) {
            CarModelDto carModelDto = new CarModelDto(
                    carModelEntity.getModel(),
                    carModelEntity.getCarCategoryEntity().getCategory(),
                    carModelEntity.getCarBrandEntity().getBrand(),
                    carModelEntity.getCreatedBy(),
                    carModelEntity.getUpdatedBy()
            );
            carModelDtos.add(carModelDto);
        }
        return carModelDtos;
    }
}
