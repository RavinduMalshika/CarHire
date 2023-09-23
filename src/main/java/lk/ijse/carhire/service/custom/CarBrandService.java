package lk.ijse.carhire.service.custom;

import lk.ijse.carhire.dto.CarBrandDto;
import lk.ijse.carhire.service.SuperService;

import java.util.List;

public interface CarBrandService extends SuperService {
    String saveBrand(CarBrandDto carBrandDto);

    String updateBrand(CarBrandDto carBrandDto, String previousBrand);

    String deleteBrand(String brand);

    CarBrandDto getBrand(String brand);

    List<CarBrandDto> getAllBrands();
}
