package com.son.model;

import org.springframework.stereotype.Component;

@Component("scopes")
public final class Scopes {
    // USER
    public static final String ALL_USER_READ                    = "ALL:USER:READ";
    public static final String ALL_USER_UPDATE                  = "ALL:USER:UPDATE";
    public static final String ALL_USER_CREATE                  = "ALL:USER:CREATE";
    public static final String ALL_USER_DELETE                  = "ALL:USER:DELETE";

    public static final String PER_USER_READ                    = "PER:USER:READ";
    public static final String PER_USER_UPDATE                  = "PER:USER:UPDATE";
}