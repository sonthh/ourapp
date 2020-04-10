package com.son.model;

import org.springframework.stereotype.Component;

@Component("scopes")
public final class Scopes {
    public static final String PER_PRODUCT_READ                 = "PER:PRODUCT:READ";
    public static final String PER_PRODUCT_UPDATE               = "PER:PRODUCT:UPDATE";
    public static final String PER_PRODUCT_CREATE               = "PER:PRODUCT:CREATE";
    public static final String PER_PRODUCT_DELETE               = "PER:PRODUCT:DELETE";

    public static final String DEPARTMENT_PRODUCT_READ          = "DEPARTMENT:PRODUCT:READ";
    public static final String DEPARTMENT_PRODUCT_UPDATE        = "DEPARTMENT:PRODUCT:UPDATE";
    public static final String DEPARTMENT_PRODUCT_CREATE        = "DEPARTMENT:PRODUCT:CREATE";
    public static final String DEPARTMENT_PRODUCT_DELETE        = "DEPARTMENT:PRODUCT:DELETE";
}