package lk.ijse.carhire.service.custom.impl;

import lk.ijse.carhire.dao.DaoFactory;
import lk.ijse.carhire.dao.custom.CarCategoryDao;
import lk.ijse.carhire.dto.CarCategoryDto;
import lk.ijse.carhire.entity.CarCategoryEntity;
import lk.ijse.carhire.service.custom.CarCategoryService;
import org.hibernate.Transaction;

import java.util.ArrayList;
import java.util.List;

public class CarCategoryServiceImpl implements CarCategoryService {
    CarCategoryDao carCategoryDao = (CarCategoryDao) DaoFactory.getInstance().getDao(DaoFactory.DaoTypes.CAR_CATEGORY);

    @Override
    public String saveCategory(CarCategoryDto carCategoryDto) {
        Transaction transaction = session.beginTransaction();

        CarCategoryEntity carCategoryEntity = new CarCategoryEntity();

        carCategoryEntity.setCategory(carCategoryDto.getCategory());
        carCategoryEntity.setCreatedBy(carCategoryDto.getCreatedBy());

        if(carCategoryDao.save(carCategoryEntity, session)) {
            transaction.commit();
            return "Saved Successfully";
        } else {
            transaction.rollback();
            return "Failed";
        }
    }

    @Override
    public String updateCategory(CarCategoryDto carCategoryDto, String previousCategory) {
        Transaction transaction = session.beginTransaction();
        CarCategoryEntity carCategoryEntity = new CarCategoryEntity();

        carCategoryEntity.setCategory(carCategoryDto.getCategory());
        carCategoryEntity.setCreatedBy(carCategoryDto.getCreatedBy());
        carCategoryEntity.setUpdatedBy(carCategoryDto.getUpdatedBy());

        if(carCategoryDao.update(carCategoryEntity, session, previousCategory)) {
            transaction.commit();
            return "Updated Successfully";
        } else {
            transaction.rollback();
            return "Failed";
        }
    }

    @Override
    public String deleteCategory(String category) {
        Transaction transaction = session.beginTransaction();
        if(carCategoryDao.delete(category, session)) {
            transaction.commit();
            return "Deleted Successfully";
        } else {
            transaction.rollback();
            return "Failed";
        }
    }

    @Override
    public CarCategoryDto getCategory(String category) {
        CarCategoryEntity carCategoryEntity = carCategoryDao.get(category, session);
        return new CarCategoryDto(
                carCategoryEntity.getCategory(),
                carCategoryEntity.getCreatedBy(),
                carCategoryEntity.getUpdatedBy()
        );
    }

    @Override
    public List<CarCategoryDto> getAllCategories() {
        ArrayList<CarCategoryDto> carCategoryDtos = new ArrayList<>();
        List<CarCategoryEntity> carCategoryEntities = carCategoryDao.getAll(session);

        for(CarCategoryEntity carCategoryEntity : carCategoryEntities) {
            CarCategoryDto carCategoryDto = new CarCategoryDto(
                    carCategoryEntity.getCategory(),
                    carCategoryEntity.getCreatedBy(),
                    carCategoryEntity.getUpdatedBy()
            );
            carCategoryDtos.add(carCategoryDto);
        }
        return carCategoryDtos;
    }
}
