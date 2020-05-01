package com.son.model;

import org.springframework.stereotype.Component;

@Component("scopes")
public final class Scopes {
    // USER
    public static final String ALL_USER_READ                    = "ALL:USER:READ";
    public static final String ALL_USER_UPDATE                  = "ALL:USER:UPDATE";
    public static final String ALL_USER_CREATE                  = "ALL:USER:CREATE";
    public static final String ALL_USER_DELETE                  = "ALL:USER:DELETE";

    // USER
    public static final String ALL_ROLE_READ                    = "ALL:ROLE:READ";
    public static final String ALL_ROLE_UPDATE                  = "ALL:ROLE:UPDATE";
    public static final String ALL_ROLE_CREATE                  = "ALL:ROLE:CREATE";
    public static final String ALL_ROLE_DELETE                  = "ALL:ROLE:DELETE";

    public static final String PER_USER_READ                    = "PER:USER:READ";
    public static final String PER_USER_UPDATE                  = "PER:USER:UPDATE";

    //PERSONNEL
    public static final String ALL_PERSONNEL_READ               = "ALL:PERSONNEL:READ";
    public static final String ALL_PERSONNEL_UPDATE             = "ALL:PERSONNEL:UPDATE";
    public static final String ALL_PERSONNEL_CREATE             = "ALL:PERSONNEL:CREATE";
    public static final String ALL_PERSONNEL_DELETE             = "ALL:PERSONNEL:DELETE";
}