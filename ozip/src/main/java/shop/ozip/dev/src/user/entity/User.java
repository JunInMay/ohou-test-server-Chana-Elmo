package shop.ozip.dev.src.user.entity;

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
public class User extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100, nullable = false)
    private String email;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String password;

    @Column(columnDefinition = "varchar(10) default 'local'", nullable = false)
    private String provider;

    @Column(length = 15, nullable = false)
    private String nickname;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String profileImageUrl;

    @Column(columnDefinition = "TEXT")
    private String personalUrl;

    @Column(length = 40)
    private String description;

    @Column(columnDefinition = "INT default 0", nullable = false)
    private int point;

    @Column(columnDefinition = "TINYINT default 0", nullable = false)
    private byte isDeleted;

    @Column(columnDefinition = "TINYINT default 0", nullable = false)
    private byte isProfessional;

    @Column(columnDefinition = "TINYINT default 0", nullable = false)
    private byte isProvider;
}
