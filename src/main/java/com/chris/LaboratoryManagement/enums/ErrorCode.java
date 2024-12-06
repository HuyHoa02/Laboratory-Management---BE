package com.chris.LaboratoryManagement.enums;

public enum ErrorCode {
    UNCATEGORIZE_EXCEPTION(9999, "Uncategorize Exception"),
    INVALID_KEY(1001, "Invalid error key"),
    USER_EXISTED(1002, "User existed"),
    USERNAME_INVALID(1003,"username must be at least 3 characters"),
    PASSWORD_INVALID(1004,"password must be at least 8 characters"),
    USER_NOTEXISTED(1005, "User is not existed"),
    UNAUTHENTICATED(1006, "Unauthenticated"),
    TOKEN_UNGENERATED(1007, "Cannot generate token"),
    TOKEN_EXPIRED(1008, "Token is expired"),
    VERIFICATION_FAILED(1009, "Verifying process failed"),
    VERIFICATION_EXPIRED(1010,"Verifying Code Expired"),
    COURSE_EXISTED(1011, "Course is existed in database"),
    SCHOOLYEAR_EXISTED(1012, "SchoolYear is existed in database"),
    SEMESTER_EXISTED(1013, "Semester is existed in database"),
    WEEK_EXISTED(1013, "Week is existed in database"),
    SESSION_EXISTED(1014, "Session is existed in database"),
    LABORATORY_EXISTED(1015, "Laboratory is existed in database"),
    SCHOOLYEAR_NOT_EXISTED(1016,"School year id is not existed in database"),
    SEMESTER_NOT_EXISTED(1017,"Semester id is not existed in database"),
    WEEK_NOT_EXISTED(1018,"Week id is not existed in database"),
    CLASS_EXISTED(1019, "Class is existed in database"),
    COURSE_NOT_EXISTED(1020,"Course id is not existed in database"),
    INVALID_LECTURER_ROLE(1021,"The valid roles must contains lecturer role"),
    CLASS_NOT_EXISTED(1022,"Class id is not existed in database"),
    LABORATORY_NOT_EXISTED(1023,"Laboratory id is not existed in database"),
    SESSION_NOT_EXISTED(1025,"Session id is not existed in database"),
    TIMESET_NOT_EXISTED(1025,"Timeset id is not existed in database"),
    REGISTRATION_NOT_EXISTED(1026,"registration id is not existed in database"),
    EMAIL_EXISTED(1027, "This email existed"),
    USERNAME_EXISTED(1028,"username existed in database"),
    INVALID_STARTDATE(1029,"The new startDate must be after the latest endDate"),
    TIMESLOT_ALREADY_EXISTS(1030,"TimeSets with the same schoolYear and Semester already existed"),
    INVALID_RESERVATION_DATE(1031, "The reservationDate is not valid"),
    AGENDA_ALREADY_EXISTS(1032, "An agenda instance already existed with this laboratory, session and reservationDate"),
    REGISTRATION_HANDLED(1033, "This registration already been handled"),
    LECTURER_EXISTED(1001, "Lecturer existed"),
    VERIFY_EMAIL_FAILED(1034,"Verify Code is not valid")
    ;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    private int code;
    private String message;

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
