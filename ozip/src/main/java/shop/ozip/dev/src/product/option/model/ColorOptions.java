package shop.ozip.dev.src.product.option.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import shop.ozip.dev.src.product.option.entity.MachineOption;

@Getter
@Setter
public class ColorOptions {
    private Long optionId;
    private String optionType;
    private String color;

    public ColorOptions(MachineOption machineOption) {
        this.optionId = machineOption.getId();
        this.optionType = "색상";
        this.color = machineOption.getColorId().getColor();
    }
}
