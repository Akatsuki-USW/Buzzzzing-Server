package bokjak.bokjakserver.common.dummy;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DummyLocationCategory {
    SUBWAY("지하철", "https://s3-buz.s3.ap-northeast-2.amazonaws.com/constant/subway.png"),
    AMUSE("놀이공원", "https://s3-buz.s3.ap-northeast-2.amazonaws.com/constant/amusement.png"),
    DEPART("백화점", "https://s3-buz.s3.ap-northeast-2.amazonaws.com/constant/department.png"),
    ETC("기타", "https://s3-buz.s3.ap-northeast-2.amazonaws.com/constant/etc.png"),
    PARK("공원", "https://s3-buz.s3.ap-northeast-2.amazonaws.com/constant/park.png"),
    MART("마트", "https://s3-buz.s3.ap-northeast-2.amazonaws.com/constant/mart.png"),
    VACATION("휴양지", "https://s3-buz.s3.ap-northeast-2.amazonaws.com/constant/vacation_spot.png"),
    MARKET("시장", "https://s3-buz.s3.ap-northeast-2.amazonaws.com/constant/market.png");

    private final String name;
    private final String iconImageUrl;
}
