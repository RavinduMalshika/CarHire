package lk.ijse.carhire.entity;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "Category")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class CarCategoryEntity {
    @Id
    @Column(name = "Category", length = 50, nullable = false)
    private String category;

    @OneToMany(mappedBy = "carCategoryEntity", targetEntity = CarEntity.class, cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<CarEntity> carEntities;

    @OneToMany(mappedBy = "carCategoryEntity", targetEntity = CarModelEntity.class, cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<CarModelEntity> carModelEntities;

    @Column(name = "Created_By")
    private String createdBy;

    @Column(name = "Updated_By")
    private String updatedBy;
}
