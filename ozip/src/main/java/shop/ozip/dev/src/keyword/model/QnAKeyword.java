
package shop.ozip.dev.src.keyword.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class QnAKeyword {
    private Long id;
    private String name;
    private String createdAt;
    private String updatedAt;
    private String status;

}