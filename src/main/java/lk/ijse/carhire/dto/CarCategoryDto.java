package lk.ijse.carhire.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class CarCategoryDto {
    private String category;
    private String createdBy;
    private String updatedBy;
}
