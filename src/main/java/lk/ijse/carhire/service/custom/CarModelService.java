package lk.ijse.carhire.service.custom;

import lk.ijse.carhire.dto.CarModelDto;
import lk.ijse.carhire.service.SuperService;

import java.util.List;

public interface CarModelService extends SuperService {
    String saveModel(CarModelDto carModelDto);

    String updateModel(CarModelDto carModelDto, String previousModel);

    String deleteModel(String model);

    CarModelDto getModel(String model);

    List<CarModelDto> getAllModels();
}
