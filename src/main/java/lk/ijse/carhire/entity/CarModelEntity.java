package lk.ijse.carhire.entity;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "Model")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class CarModelEntity {
    @Id
    @Column(name = "Model", length = 50, nullable = false)
    private String model;

    @OneToMany(mappedBy = "carModelEntity", targetEntity = CarEntity.class, cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<CarEntity> carEntities;

    @ManyToOne()
    @JoinColumn(name = "Brand", nullable = false)
    private CarBrandEntity carBrandEntity;

    @ManyToOne()
    @JoinColumn(name = "Category", nullable = false)
    private CarCategoryEntity carCategoryEntity;

    @Column(name = "Created_By")
    private String createdBy;

    @Column(name = "Updated_By")
    private String updatedBy;
}
