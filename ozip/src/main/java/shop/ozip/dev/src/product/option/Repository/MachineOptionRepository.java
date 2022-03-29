package shop.ozip.dev.src.product.option.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import shop.ozip.dev.src.product.option.entity.MachineOption;

import java.util.List;

public interface MachineOptionRepository extends JpaRepository<MachineOption, Long> {
    List<MachineOption> findAllByProductId(Long productId);
}
