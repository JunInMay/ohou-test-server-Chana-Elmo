package shop.ozip.dev.src.product.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RatedCountList {
    int fiveScoreCount = 0;
    int fourScoreCount = 0;
    int threeScoreCount = 0;
    int twoScoreCount = 0;
    int oneScoreCount = 0;
}
