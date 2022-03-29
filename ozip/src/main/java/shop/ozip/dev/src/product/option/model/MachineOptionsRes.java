package shop.ozip.dev.src.product.option.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class MachineOptionsRes {
    private List<ColorOptions> colorOptions;
}
