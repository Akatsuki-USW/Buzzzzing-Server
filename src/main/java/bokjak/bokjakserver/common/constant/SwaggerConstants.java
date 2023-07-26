package bokjak.bokjakserver.common.constant;

public class SwaggerConstants {
    /**
     * swagger
     */

    public static final String[] SWAGGER_APPOINTED_PATHS = {
            "/**"
    };
    public static final String DEFINITION_TITLE = "복쟉복쟉 API 명세서";
    public static final String DEFINITION_DESCRIPTION = "\uD83D\uDE80 Realtime Congestion Based Location Recommendation Service - 복쟉복쟉 Server의 API 명세서입니다.";
    public static final String DEFINITION_VERSION = "v1";

    public static final String SECURITY_SCHEME_NAME = "bearer-key";
    public static final String SECURITY_SCHEME = "bearer";
    public static final String SECURITY_SCHEME_BEARER_FORMAT = "JWT";
    public static final String SECURITY_SCHEME_DESCRIPTION = "JWT 토큰 키를 입력해주세요!";

    /**
     * ETC
     */

    public static final String TAG_CATEGORY = "Category";
    public static final String TAG_CATEGORY_DESCRIPTION = "카테고리 API";

    public static final String CATEGORY_GET_ALL = "전체 카테고리 + 필터링 선택지 조회";
    public static final String CATEGORY_GET_ALL_DESCRIPTION = """
            💡 추후 캐싱 버전으로 업데이트될 예정 (Could)""";

    /**
     * AWS S3
     */

    public static final String TAG_S3 = "AWS S3";
    public static final String TAG_S3_DESCRIPTION = "S3 이미지 파일 API";
    public static final String S3_FILE_UPLOAD = "이미지 파일 업로드";
    public static final String S3_FILE_UPLOAD_DESCRIPTION = """
            💡 파일 확장자 :\s
            { png, jpg, jpeg, gif }
            
            💡`type` 종류 : \n
            `profile` : 유저 프로필 이미지
            `spot` : 추천 스팟 이미지
            """;
    public static final String S3_FILE_DELETE = "단일 이미지 파일 삭제";
    public static final String S3_FILE_DELETE_DESCRIPTION = """
            .""";
    public static final String S3_FILE_UPDATE = "이미지 파일 업데이트";
    public static final String S3_FILE_UPDATE_DESCRIPTION = """
            💡 파일 확장자 :\s
            { png, jpg, jpeg, gif }
            
            `urlsToDelete` : 변경할 기존 url 주소
            `newFiles`: 업로드할 이미지 파일
            """;


    /**
     * Location
     */

    public static final String TAG_LOCATION = "Location";
    public static final String TAG_LOCATION_DESCRIPTION = "로케이션 API";

    public static final String LOCATION_SEARCH = "로케이션 리스트 검색";
    public static final String LOCATION_SEARCH_DESCRIPTION = """
            💡 종합 카테고리 요청의 경우 `categoryId` 1~9를 모두 보내는 대신 null 혹은 빈 리스트 [] 으로 보내주셔도 됩니다.

            ⚠️ 혼잡도 정렬의 경우 `congestionSort` 값뿐만 아니라 `cursorCongestionLevel` 이 필수입니다. (맨 처음의 조회에서는 필수 아님. cursorId와 비슷한 로직)

            정렬 조회는 일반 조회와는 아예 다르니 정렬—일반 스위칭에 따라 cursor 들 초기화에 신경써야 할듯 합니다.""";
    public static final String LOCATION_GET_ALL = "로케이션 단순 리스트 조회";
    public static final String LOCATION_GET_ALL_DESCRIPTION = """
            단순 로케이션 목록을 조회
            로케이션 리스트 조회 API를 쓰셔도 됩니다. 단지 이 API가 쪼금 더 빠릅니다.
                        
            💡 추후 캐싱 버전으로 업데이트될 예정 (Could)""";
    public static final String LOCATION_GET_TOP = "로케이션 혼잡도 낮은순 TOP 5 리스트 조회 ";
    public static final String LOCATION_GET_TOP_DESCRIPTION = """ 
            .""";
    public static final String LOCATION_BOOKMARKED = "내가 북마크한 로케이션 리스트 조회";
    public static final String LOCATION_BOOKMARKED_DESCRIPTION = """
            .""";
    public static final String LOCATION_DETAIL = "로케이션 상세 조회";
    public static final String LOCATION_DETAIL_DESCRIPTION = """
            1~24시의 특정 로케이션 실시간 혼잡도 데이터 조회""";
    public static final String LOCATION_CONGESTION_DAILY = "로케이션 데일리 혼잡도 조회";
    public static final String LOCATION_CONGESTION_DAILY_DESCRIPTION = """
            .""";
    public static final String LOCATION_BOOKMARK = "로케이션 북마크";
    public static final String LOCATION_BOOKMARK_DESCRIPTION = """
            .""";


    /**
     * Spot
     */
    public static final String TAG_SPOT = "Spot";
    public static final String TAG_SPOT_DESCRIPTION = "스팟 API";

    public static final String SPOT_GET_ALL_BY_LOCATION = "스팟 리스트 조회";
    public static final String SPOT_GET_ALL_BY_LOCATION_DESCRIPTION = """
            특정 로케이션, 특정 스팟 카테고리의 스팟 리스트를 최신순(id 내림차순)으로 조회합니다.
            
            차단한 유저의 스팟들은 제외됩니다.\s""";
    public static final String SPOT_GET_ALL_SIMPLE = "모든 로케이션의 스팟 리스트 조회";
    public static final String SPOT_GET_ALL_SIMPLE_DESCRIPTION = """
            모든 로케이션, 특정 스팟 카테고리의 스팟 리스트를 최신순(id 내림차순)으로 조회합니다.
             
            차단한 유저의 스팟들은 제외됩니다.\s""";
    public static final String SPOT_GET_DETAIL = "스팟 상세 조회";
    public static final String SPOT_GET_DETAIL_DESCRIPTION = """
            - `isBookmarked` : 현재 유저가 해당 스팟을 북마크했는지 여부
            - `isAuthor` : 현재 유저가 해당 스팟의 작성자인지 여부
            \s""";

    public static final String SPOT_BOOKMARK = "스팟 북마크";
    public static final String SPOT_BOOKMARK_DESCRIPTION = """
            💡`isBookmarked` : 등록 → true, 취소 → false""";

    public static final String SPOT_GET_BOOKMARKED = "내가 북마크한 스팟 조회";
    public static final String SPOT_GET_BOOKMARKED_DESCRIPTION = """
            내가 북마크한 스팟들을 최신순(id 내림차순)으로 조회합니다.""";
    public static final String SPOT_GET_MY = "내가 쓴 스팟 조회";
    public static final String SPOT_GET_MY_DESCRIPTION = """
            내가 작성한 스팟들을 최신순(id 내림차순)으로 조회합니다.""";
    public static final String SPOT_GET_COMMENTED = "내가 댓글단 스팟 조회";
    public static final String SPOT_GET_COMMENTED_DESCRIPTION = """
            차단한 유저를 제외
            
            내가 댓글을 작성한 스팟들을 최신순(id 내림차순)으로 조회합니다.""";
    public static final String SPOT_CREATE = "스팟 생성";
    public static final String SPOT_CREATE_DESCRIPTION = """
            |  | 타입 | 제약 조건 | 설명 |
            | --- | --- | --- | --- |
            | locationId, spotCategoryId | Integer | Not Null |  |
            | title, address, content | String | Not Null && 1자 이상 | at least one non-whitespace character. |
            | imageUrls | List<String> | Not Null | 빈 배열 가능 |
            """;
    public static final String SPOT_UPDATE = "스팟 수정";
    public static final String SPOT_UPDATE_DESCRIPTION = """
            |  | 타입 | 제약 조건 | 설명 |
            | --- | --- | --- | --- |
            | locationId, spotCategoryId | Integer | Not Null |  |
            | title, address, content | String | Not Null && 1자 이상 | at least one non-whitespace character. |
            | imageUrls | List<String> | Not Null | 빈 배열 가능 |
            """;
    public static final String SPOT_DELETE = "스팟 삭제";
    public static final String SPOT_DELETE_DESCRIPTION = """
            💡 스팟 이미지 삭제는 서버에서 진행합니다.
            """;

    /**
     * Comment
     */
    public static final String TAG_COMMENT = "Spot Comment";
    public static final String TAG_COMMENT_DESCRIPTION = "스팟 댓글 API";

    public static final String COMMENT_GET_ALL = "스팟 댓글 리스트 조회";
    public static final String COMMENT_GET_ALL_DESCRIPTION = """
            특정 스팟의 모든 댓글을 조회합니다.
            차단한 유저가 작성한 댓글들은 제외
            
            💡 추후 대댓글 구현시 sequence, depth 필드가 생길 수 있음
            """;
    public static final String COMMENT_CREATE = "스팟 댓글 생성";
    public static final String COMMENT_CREATE_DESCRIPTION = """
            ⚠️ 추후 대댓글 구현시 parentCommentId 생김
            """;
    public static final String COMMENT_UPDATE = "스팟 댓글 수정";
    public static final String COMMENT_UPDATE_DESCRIPTION = """
            .""";
    public static final String COMMENT_DELETE = "스팟 댓글 삭제";
    public static final String COMMENT_DELETE_DESCRIPTION = """
            .""";

}
