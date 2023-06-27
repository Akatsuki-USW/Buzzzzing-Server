package bokjak.bokjakserver.common.dummy;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DummyLocationCategory {// TODO: 추후 S3 이미지 오브젝트 넣고 url 업데이트
    SUBWAY("지하철", ""), AMUSE("놀이공원", ""), DEPART("백화점", ""),
    ETC("기타", ""), PARK("공원", ""), MART("마트", ""),
    VACATION("휴양지", ""), MARKET("시장", ""), ALL("종합", "");

    private final String name;
    private final String iconImageUrl;
}
