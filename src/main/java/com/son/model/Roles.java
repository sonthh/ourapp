package com.son.model;

import static com.son.model.Scopes.*;

public interface Roles {
    String[] ADMIN = {
            ALL_USER_CREATE,
            ALL_USER_DELETE,
            ALL_USER_READ,
            ALL_USER_UPDATE,

            ALL_ROLE_CREATE,
            ALL_ROLE_DELETE,
            ALL_ROLE_READ,
            ALL_ROLE_UPDATE,
    };

    String[] BASIC = {
            PER_USER_READ,
            PER_USER_UPDATE,
    };

    String[] MANAGER = {
            ALL_USER_CREATE,
            ALL_USER_DELETE,
            ALL_USER_READ,
            ALL_USER_UPDATE,

            ALL_ROLE_CREATE,
            ALL_ROLE_DELETE,
            ALL_ROLE_READ,
            ALL_ROLE_UPDATE,
    };
}
