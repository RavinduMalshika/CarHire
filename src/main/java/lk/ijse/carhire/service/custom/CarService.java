package lk.ijse.carhire.service.custom;

import lk.ijse.carhire.dto.CarDto;
import lk.ijse.carhire.service.SuperService;

import java.util.List;

public interface CarService extends SuperService {
    String saveCar(CarDto carDto);

    String updateCar(CarDto carDto);

    String deleteCar(String id);

    CarDto getCar(String id);

    List<CarDto> getAllCars();
}
