package stable.horseCafe.web.dto.review;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
public class ReviewSaveReqDto {

    @NotBlank(message = "글 내용은 필수값입니다.")
    private String content;

    @Builder
    public ReviewSaveReqDto(String content) {
        this.content = content;
    }
}
