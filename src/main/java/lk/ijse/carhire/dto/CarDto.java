package lk.ijse.carhire.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class CarDto {
    private String id;
    private String vehicleNumber;
    private String carCategory;
    private String carBrand;
    private String carModel;
    private Integer year;
    private Double dailyRental;
    private String availability;
    private String createdBy;
    private String updatedBy;
}
