package stable.horseCafe.web.dto.review;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReviewSaveReqDto {

    private String content;

    @Builder
    public ReviewSaveReqDto(String content) {
        this.content = content;
    }
}
