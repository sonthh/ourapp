package com.son.model;

import static com.son.model.Scopes.*;

public interface Roles {
    String[] ADMIN = {
            PER_PRODUCT_CREATE,
            PER_PRODUCT_DELETE,
            PER_PRODUCT_READ,
            PER_PRODUCT_UPDATE,

            DEPARTMENT_PRODUCT_CREATE,
            DEPARTMENT_PRODUCT_DELETE,
            DEPARTMENT_PRODUCT_READ,
            DEPARTMENT_PRODUCT_UPDATE,
    };

    String[] BASIC = {
            PER_PRODUCT_CREATE,
            PER_PRODUCT_DELETE,
            PER_PRODUCT_READ,
            PER_PRODUCT_UPDATE,
    };

    String[] MANAGER = {
            DEPARTMENT_PRODUCT_CREATE,
            DEPARTMENT_PRODUCT_DELETE,
            DEPARTMENT_PRODUCT_READ,
            DEPARTMENT_PRODUCT_UPDATE,
    };
}
