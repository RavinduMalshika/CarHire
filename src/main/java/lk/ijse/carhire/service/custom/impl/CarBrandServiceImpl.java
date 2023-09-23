package lk.ijse.carhire.service.custom.impl;

import lk.ijse.carhire.dao.DaoFactory;
import lk.ijse.carhire.dao.custom.CarBrandDao;
import lk.ijse.carhire.dto.CarBrandDto;
import lk.ijse.carhire.entity.CarBrandEntity;
import lk.ijse.carhire.service.custom.CarBrandService;
import org.hibernate.Transaction;

import java.util.ArrayList;
import java.util.List;

public class CarBrandServiceImpl implements CarBrandService {
    CarBrandDao carBrandDao = (CarBrandDao) DaoFactory.getInstance().getDao(DaoFactory.DaoTypes.CAR_BRAND);
    @Override
    public String saveBrand(CarBrandDto carBrandDto) {
        Transaction transaction = session.beginTransaction();
        CarBrandEntity carBrandEntity = new CarBrandEntity();

        carBrandEntity.setBrand(carBrandDto.getBrand());
        carBrandEntity.setCreatedBy(carBrandDto.getCreatedBy());

        if(carBrandDao.save(carBrandEntity, session)) {
            transaction.commit();
            return "Successfully Saved";
        } else {
            transaction.rollback();
            return "Failed";
        }
    }

    @Override
    public String updateBrand(CarBrandDto carBrandDto, String previousBrand) {
        Transaction transaction = session.beginTransaction();
        CarBrandEntity carBrandEntity = new CarBrandEntity();

        carBrandEntity.setBrand(carBrandDto.getBrand());
        carBrandEntity.setCreatedBy(carBrandDto.getCreatedBy());
        carBrandEntity.setUpdatedBy(carBrandDto.getUpdatedBy());

        if(carBrandDao.update(carBrandEntity, session, previousBrand)) {
            transaction.commit();
            return "Successfully Saved";
        } else {
            transaction.rollback();
            return "Failed";
        }
    }

    @Override
    public String deleteBrand(String brand) {
        Transaction transaction = session.beginTransaction();
        if(carBrandDao.delete(brand, session)) {
            transaction.commit();
            return "Successfully Saved";
        } else {
            transaction.rollback();
            return "Failed";
        }
    }

    @Override
    public CarBrandDto getBrand(String brand) {
        CarBrandEntity carBrandEntity = carBrandDao.get(brand, session);
        return new CarBrandDto(
                carBrandEntity.getBrand(),
                carBrandEntity.getCreatedBy(),
                carBrandEntity.getUpdatedBy()
        );
    }

    @Override
    public List<CarBrandDto> getAllBrands() {
        ArrayList<CarBrandDto> carBrandDtos = new ArrayList<>();
        List<CarBrandEntity> carBrandEntities = carBrandDao.getAll(session);

        for(CarBrandEntity carBrandEntity : carBrandEntities) {
            CarBrandDto carBrandDto = new CarBrandDto(
                    carBrandEntity.getBrand(),
                    carBrandEntity.getCreatedBy(),
                    carBrandEntity.getUpdatedBy()
            );
            carBrandDtos.add(carBrandDto);
        }
        return carBrandDtos;
    }
}
