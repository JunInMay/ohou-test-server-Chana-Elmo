package shop.ozip.dev.src.product.option.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shop.ozip.dev.src.category.entity.SubCategory;
import shop.ozip.dev.src.global.repository.BaseTimeEntity;

import javax.persistence.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class MachineOption extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long productId;

    @ManyToOne
    @JoinColumn(name = "colorId", referencedColumnName = "id", nullable = false)
    private ColorOption colorId;

//    @Column(nullable = true)
//    private Long additionId;
}
