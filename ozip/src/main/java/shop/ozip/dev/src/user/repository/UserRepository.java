package shop.ozip.dev.src.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import shop.ozip.dev.src.user.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
}
