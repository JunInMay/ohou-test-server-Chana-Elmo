
package shop.ozip.dev.config;

import lombok.Getter;

/**
 * 에러 코드 관리
 */
@Getter
public enum BaseResponseStatus {
    /**
     * 1000 : 요청 성공
     */
    SUCCESS(true, 1000, "요청에 성공하였습니다."),


    /**
     * 2000 : Request 오류
     */
    // Common
    REQUEST_ERROR(false, 2000, "입력값을 확인해주세요."),
    EMPTY_JWT(false, 2001, "JWT를 입력해주세요."),
    INVALID_JWT(false, 2002, "유효하지 않은 JWT입니다."),
    INVALID_USER_JWT(false,2003,"권한이 없는 유저의 접근입니다."),


    // users
    USERS_EMPTY_USER_ID(false, 2010, "유저 아이디 값을 확인해주세요."),


    // [POST] /users
    POST_USERS_EMPTY_EMAIL(false, 2015, "이메일을 입력해주세요."),
    POST_USERS_INVALID_EMAIL(false, 2016, "이메일 형식을 확인해주세요."),
    POST_USERS_EXISTS_EMAIL(false,2017,"중복된 이메일입니다."),
    POST_USERS_EXISTS_NICKNAME(false, 2018, "중복된 닉네임입니다."),


    // [GET] /app/users/kakao
    KAKAO_LOGIN_FAIL(false, 2050, "카카오 로그인에 실패했습니다."),
    KAKAO_EMPTY_ACCESS_TOKEN(false, 2051, "카카오 액세스 토큰이 없습니다."),

    // 2100 : feeds, comment
    EMPTY_FEED_ID(false, 2100, "피드 ID를 입력해주세요."),

    // 2110 [POST] Mediafeed
    POST_MEDIA_FEED_TYPE_AMBIGUOUS(false, 2110, "해당 미디어피드가 사진 묶음인지, 동영상 미디어피드인지 알 수 없습니다."),
    POST_MEDIA_FEED_WRONG_TYPE_INDEX(false, 2111, "미디어피드 타입 ID가 부적절합니다."),



    AMBIGUOUS_RECOMMENT(false, 2150, "대댓글 여부와 대댓글 ID를 확인해주세요."),
    EMPTY_COMMENT_ID(false, 2151, "댓글 ID를 입력해주세요."),

    // [POST] 댓글달기
    EMPTY_COMMENT_CONTENT(false, 2160, "댓글 내용을 입력해주세요."),
    EMPTY_COMMENT_FEED_ID(false, 2161, "댓글 달 피드의 id를 입력해주세요."),

    // 2200 : media
    // [POST] media 2210~
    EMPTY_OWNER_FEED(false, 2210, "사진 또는 동영상이 속할 미디어피드의 idx를 입력해주세요"),
    EMPTY_MEDIA_URL(false, 2211, "사진 또는 동영상의 주소를 입력해주세요."),
    EMPTY_SPACE_ID(false, 2212, "사진 또는 동영상의 공간 정보를 입력해주세요."),
    INVALID_SPACE_ID(false, 2213, "공간 정보가 부적절합니다."),
    EMPTY_THUMBNAIL_URL(false, 2214, "동영상의 썸네일 URL을 입력해주세요."),
    EMPTY_VIDEO_TIME(false, 2215, "동영상의 재생시간을 입력해주세요."),

    // 2300 : follow, bookmark

    // bookmark(2350~
    EMPTY_SCRAPBOOK_ID(false, 2350, "스크랩북 ID를 입력해주세요."),
    WRONG_SCRAPBOOK_ID(false, 2351, "잘못된 스크랩북 ID 입니다."),


    //[POST / DELETE / PATCH] app/bookmarks/feed

    EMPTY_BOOKMARK_FEED_ID(false, 2360, "북마크할 피드의 id를 입력해주세요."),
    EMPTY_BOOKMARK_FEED_ID_FOR_DELETE(false, 2361, "북마크 해제 할 피드의 id를 입력해주세요."),
    EMPTY_BOOKMARK_FEED_ID_FOR_PATCH(false, 2362, "다른 폴더로 옮길 피드 id를 입력해주세요."),
    EMPTY_BOOKMARK_SCRAPBOOK_ID(false, 2363, "피드를 옮길 폴더의 id를 입력해주세요."),


    //[POST] app/bookmarks
    EMPTY_BOOKMARK_NAME(false, 2370, "북마크의 이름을 입력해주세요."),
    TOO_LONG_BOOKMARK_DESCRIPTION(false, 2371, "북마크의 한 줄 설명은 30자 이내여야 합니다."),




    /**
     * 3000 : Response 오류
     */
    // Common
    RESPONSE_ERROR(false, 3000, "값을 불러오는데 실패하였습니다."),

    USER_NOT_EXIST(false, 3010, "유저가 존재하지 않습니다."),

    // [POST] /users
    DUPLICATED_EMAIL(false, 3013, "중복된 이메일입니다."),
    FAILED_TO_LOGIN(false,3014,"없는 이메일이거나 비밀번호가 틀렸습니다."),
    KAKAO_INVALID_ACCESS_TOKEN(false,3015,"유효하지 않은 카카오 액세스 토큰입니다."),

    // 3100 : feed, comment
    FEED_NOT_EXIST(false,3100,"존재하지 않는 피드입니다."),
    NOT_FEED_OWNER(false,3101,"해당 피드의 주인이 아닙니다."),



    COMMENT_NOT_EXIST(false,3150,"해당 댓글이 존재하지 않습니다."),
    RECOMMENT_NOT_EXIST(false,3160,"답글을 달 댓글이 존재하지 않습니다."),
    POST_RECOMMENT_FEED_NOT_MATCH(false,3161,"답글의 피드 idx와 답글을 달 댓글의 피드 idx가 일치하지 않습니다."),

    // 3200 : media , media-feed
    IS_NOT_MEDIA_FEED(false,3200,"해당 피드는 미디어 피드가 아닙니다."),
    IS_NOT_MEDIA(false,3201,"해당 피드는 사진 피드가 아닙니다."),
    MEDIA_FEED_NOT_EXIST(false,3202,"미디어피드가 존재하지 않습니다."),
    MEDIA_FEED_NOT_PHOTO(false,3203,"해당 미디어피드는 사진 묶음이 아닙니다."),

    // [POST] media 3210~


    // 3300 : follow, bookmark

    POST_FOLLOW_ALREADY_FOLLOW(false,3310,"이미 팔로우한 관계입니다."),
    DELETE_FOLLOW_NOT_EXIST(false,3320,"팔로우되지 않은 관계입니다."),


    // bookmark 3350~
    ALREADY_SCRAPPED(false, 3350, "이미 스크랩된 피드입니다."),
    SCRAPBOOK_NOT_EXIST(false, 3351, "존재하지 않는 스크랩북입니다."),
    NOT_SCRAPPED(false, 3360, "스크랩 되지 않은 피드입니다."),

    // [PATCH] /app/bookmarks
    MAIN_CANT_PATCHED(false, 3370, "메인 스크랩북은 수정할 수 없습니다."),
    NOT_SCRAPBOOK_OWNER(false, 3371, "해당 스크랩북의 주인이 아닙니다."),


    // [DELETED] /app/bookmarks
    MAIN_CANT_DELETED(false, 3380, "메인 스크랩북은 삭제할 수 없습니다."),

    // 3400 : homewarming, knowhow, qna 관련
    IS_NOT_HOMEWARMING_FEED(false,3400,"해당 피드는 집들이 피드가 아닙니다."),
    IS_NOT_KNOWHOW_FEED(false,3401,"해당 피드는 노하우 피드가 아닙니다."),
    IS_NOT_QNA_FEED(false,3402,"해당 피드는 질문과 답변 피드가 아닙니다."),
    //

    // 3500 : 그외 + 서드파티
    KEYWORD_NOT_EXIST(false,3500,"키워드가 존재하지 않습니다."),

    ALREADY_LIKED(false,3530,"이미 좋아요 처리됐습니다."),
    NOT_LIKED(false,3531,"좋아요 처리되지 않았습니다."),

    NAVER_API_ERROR(false, 3550, "네이버 API 응답에 오류가 있습니다."),


    /**
     * 4000 : Database, Server 오류
     */
    DATABASE_ERROR(false, 4000, "데이터베이스 연결에 실패하였습니다."),
    SERVER_ERROR(false, 4001, "서버와의 연결에 실패하였습니다."),

    //[PATCH] /users/{userIdx}
    MODIFY_FAIL_USERNAME(false,4014,"유저네임 수정 실패"),

    PASSWORD_ENCRYPTION_ERROR(false, 4011, "비밀번호 암호화에 실패하였습니다."),
    PASSWORD_DECRYPTION_ERROR(false, 4012, "비밀번호 복호화에 실패하였습니다.");


    // 5000 : 필요시 만들어서 쓰세요
    // 6000 : 필요시 만들어서 쓰세요


    private final boolean isSuccess;
    private final int code;
    private final String message;

    private BaseResponseStatus(boolean isSuccess, int code, String message) {
        this.isSuccess = isSuccess;
        this.code = code;
        this.message = message;
    }
}
