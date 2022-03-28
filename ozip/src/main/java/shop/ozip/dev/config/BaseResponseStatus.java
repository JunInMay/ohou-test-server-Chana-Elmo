
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


    AMBIGUOUS_RECOMMENT(false, 2150, "대댓글 여부와 대댓글 ID를 확인해주세요."),

    // [POST] 댓글달기
    EMPTY_COMMENT_CONTENT(false, 2160, "댓글 내용을 입력해주세요."),
    EMPTY_COMMENT_FEED_ID(false, 2161, "댓글 달 피드의 id를 입력해주세요."),

    // 2300 : follow, bookmark

    // bookmark(2350~

    //[POST] app/bookmarks/feed

    EMPTY_BOOKMARK_FEED_ID(false, 2360, "북마크할 피드의 id를 입력해주세요."),


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


    RECOMMENT_NOT_EXIST(false,3160,"답글을 달 댓글이 존재하지 않습니다."),
    POST_RECOMMENT_FEED_NOT_MATCH(false,3161,"답글의 피드 idx와 답글을 달 댓글의 피드 idx가 일치하지 않습니다."),

    // 3200 : media , media-feed
    IS_NOT_MEDIA_FEED(false,3200,"해당 피드는 미디어 피드가 아닙니다."),

    // 3300 : follow, bookmark

    POST_FOLLOW_ALREADY_FOLLOW(false,3310,"이미 팔로우한 관계입니다."),
    DELETE_FOLLOW_NOT_EXIST(false,3320,"팔로우되지 않은 관계입니다."),

    // bookmark 3350~
    ALREADY_SCRAPPED(false, 3350, "이미 스크랩된 피드입니다."),


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
