package lk.ijse.carhire.entity;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "Brand")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class CarBrandEntity {
    @Id
    @Column(name = "Brand", length = 50, nullable = false)
    private String brand;

    @OneToMany(mappedBy = "carBrandEntity", targetEntity = CarEntity.class, cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<CarEntity> carEntities;

    @OneToMany(mappedBy = "carBrandEntity", targetEntity = CarModelEntity.class, cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<CarModelEntity> carModelEntities;

    @Column(name = "Created_By")
    private String createdBy;

    @Column(name = "Updated_By")
    private String updatedBy;
}
