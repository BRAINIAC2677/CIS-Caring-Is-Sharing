package util;

public enum ResponseCode {
    SUCCESSFUL_REGISTRATION,
    SUCCESSFUL_LOGIN,
    SUCCESSFUL_LOGOUT,
    SUCCESSFUL_LIST_USER,
    SUCCESSFUL_MKDIR,
    SUCCESSFUL_RMDIR,
    SUCCESSFUL_CD,
    SUCCESSFUL_LS,
    SUCCESSFUL_BUFFER_ALLOCATION,
    SUCCESSFUL_UPLOAD,

    FAILED_REGISTRATION,
    USER_NOT_FOUND,
    USER_ALREADY_LOGGED_IN,
    INCORRECT_PASSWORD,
    FAILED_LOGOUT,
    DIRECTORY_ALREADY_EXISTS,
    DIRECTORY_DOES_NOT_EXIST,
    DIRECTORY_NOT_EMPTY,
    NOT_A_DIRECTORY,
    FAILED_BUFFER_ALLOCATION,
    FAILED_UPLOAD;
}