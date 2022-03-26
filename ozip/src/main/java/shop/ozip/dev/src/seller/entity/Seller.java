package shop.ozip.dev.src.seller.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shop.ozip.dev.src.global.repository.BaseTimeEntity;

import javax.persistence.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class Seller extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(length = 100, nullable = false)
    private String company;

    @Column(length = 50, nullable = false)
    private String ceo;

    @Column(length = 100, nullable = false)
    private String companyLocation;

    @Column(length = 30, nullable = false)
    private String phone;

    @Column(length = 100, nullable = false)
    private String email;

    @Column(length = 50, nullable = false)
    private String businessCode;
}
