package lk.ijse.carhire.entity;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "Car")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class CarEntity {
    @Id
    @Column(name = "ID", nullable = false, length = 7)
    private String id;

    @Column(name = "Vehicle_Number")
    private String vehicleNumber;

    @ManyToOne()
    @JoinColumn(name = "Category", nullable = false)
    private CarCategoryEntity carCategoryEntity;

    @ManyToOne()
    @JoinColumn(name = "Brand", nullable = false)
    private CarBrandEntity carBrandEntity;

    @ManyToOne()
    @JoinColumn(name = "Model", nullable = false)
    private CarModelEntity carModelEntity;

    @Column(name = "Year")
    private Integer year;

    @Column(name = "Daily_Rental")
    private Double dailyRental;

    @Column(name = "Availability", nullable = false)
    private String availability;

    @OneToMany(mappedBy = "carEntity", targetEntity = RentEntity.class, cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<RentEntity> rentEntities;

    @Column(name = "Created_By")
    private String createdBy;

    @Column(name = "Updated_By")
    private String updatedBy;
}
