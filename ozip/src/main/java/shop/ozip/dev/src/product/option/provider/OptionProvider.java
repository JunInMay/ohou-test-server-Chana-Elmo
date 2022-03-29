package shop.ozip.dev.src.product.option.provider;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseBody;
import shop.ozip.dev.config.BaseException;
import shop.ozip.dev.src.product.option.Repository.MachineOptionRepository;
import shop.ozip.dev.src.product.option.entity.MachineOption;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@ResponseBody
@Service
public class OptionProvider {

    private final MachineOptionRepository machineOptionRepository;

    public List<MachineOption> getAllMachineOptions(Long productId) throws BaseException {
        List<MachineOption> machineOptions = machineOptionRepository.findAllByProductId(productId);

        return machineOptions;
    }
}
