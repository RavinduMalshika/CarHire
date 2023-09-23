package lk.ijse.carhire.service.custom;

import lk.ijse.carhire.dto.CarCategoryDto;
import lk.ijse.carhire.service.SuperService;

import java.util.List;

public interface CarCategoryService extends SuperService {
    String saveCategory(CarCategoryDto carCategoryDto);

    String updateCategory(CarCategoryDto carCategoryDto, String previousCategory);

    String deleteCategory(String category);

    CarCategoryDto getCategory(String category);

    List<CarCategoryDto> getAllCategories();
}
