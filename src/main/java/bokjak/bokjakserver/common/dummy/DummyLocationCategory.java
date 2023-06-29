package bokjak.bokjakserver.common.dummy;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DummyLocationCategory {
    SUBWAY("지하철", "https://buz-s3.s3.ap-southeast-2.amazonaws.com/constant/subway.png"),
    AMUSE("놀이공원", "https://buz-s3.s3.ap-southeast-2.amazonaws.com/constant/amusement.png"),
    DEPART("백화점", "https://buz-s3.s3.ap-southeast-2.amazonaws.com/constant/department.png"),
    ETC("기타", "https://buz-s3.s3.ap-southeast-2.amazonaws.com/constant/etc.png"),
    PARK("공원", "https://buz-s3.s3.ap-southeast-2.amazonaws.com/constant/park.png"),
    MART("마트", "https://buz-s3.s3.ap-southeast-2.amazonaws.com/constant/mart.png"),
    VACATION("휴양지", "https://buz-s3.s3.ap-southeast-2.amazonaws.com/constant/vacation_spot.png"),
    MARKET("시장", "https://buz-s3.s3.ap-southeast-2.amazonaws.com/constant/market.png"),
    ALL("종합", null);

    private final String name;
    private final String iconImageUrl;
}
