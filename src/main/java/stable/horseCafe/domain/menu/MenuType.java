package stable.horseCafe.domain.menu;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MenuType {

    COFFEE("COFFEE", "커피"),
    TEA("TEA", "차"),
    ADE("ADE", "에이드");

    private final String key;
    private final String value;
}
