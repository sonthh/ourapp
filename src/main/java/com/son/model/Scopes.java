package com.son.model;

import org.springframework.stereotype.Component;

@Component("scopes")
public final class Scopes {
    // USER
    public static final String ALL_USER_READ                    = "ALL_USER_READ";
    public static final String ALL_USER_UPDATE                  = "ALL_USER_UPDATE";
    public static final String ALL_USER_CREATE                  = "ALL_USER_CREATE";
    public static final String ALL_USER_DELETE                  = "ALL_USER_DELETE";

    public static final String PER_USER_READ                    = "PER_USER_READ";
    public static final String PER_USER_UPDATE                  = "PER_USER_UPDATE";

    // ROLE
    public static final String ALL_ROLE_READ                    = "ALL_ROLE_READ";
    public static final String ALL_ROLE_UPDATE                  = "ALL_ROLE_UPDATE";
    public static final String ALL_ROLE_CREATE                  = "ALL_ROLE_CREATE";
    public static final String ALL_ROLE_DELETE                  = "ALL_ROLE_DELETE";

    public static final String ALL_PERMISSION_READ              = "ALL_PERMISSION_READ";

    //PERSONNEL
    public static final String ALL_PERSONNEL_READ               = "ALL_PERSONNEL_READ";
    public static final String ALL_PERSONNEL_UPDATE             = "ALL_PERSONNEL_UPDATE";
    public static final String ALL_PERSONNEL_CREATE             = "ALL_PERSONNEL_CREATE";
    public static final String ALL_PERSONNEL_DELETE             = "ALL_PERSONNEL_DELETE";

    // BRANCH
    public static final String ALL_BRANCH_READ                  = "ALL_BRANCH_READ";
    public static final String ALL_BRANCH_UPDATE                = "ALL_BRANCH_UPDATE";
    public static final String ALL_BRANCH_CREATE                = "ALL_BRANCH_CREATE";
    public static final String ALL_BRANCH_DELETE                = "ALL_BRANCH_DELETE";

    // DEPARTMENT
    public static final String ALL_DEPARTMENT_READ              = "ALL_DEPARTMENT_READ";
    public static final String ALL_DEPARTMENT_UPDATE            = "ALL_DEPARTMENT_UPDATE";
    public static final String ALL_DEPARTMENT_CREATE            = "ALL_DEPARTMENT_CREATE";
    public static final String ALL_DEPARTMENT_DELETE            = "ALL_DEPARTMENT_DELETE";
}