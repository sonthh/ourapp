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

            ALL_PERMISSION_READ,

            ALL_PERSONNEL_READ,
            ALL_PERSONNEL_UPDATE,
            ALL_PERSONNEL_CREATE,
            ALL_PERSONNEL_DELETE,

            ALL_BRANCH_READ,
            ALL_BRANCH_UPDATE,
            ALL_BRANCH_CREATE,
            ALL_BRANCH_DELETE,

            ALL_DEPARTMENT_READ,
            ALL_DEPARTMENT_UPDATE,
            ALL_DEPARTMENT_CREATE,
            ALL_DEPARTMENT_DELETE,

            ALL_CONTRACT_READ,
            ALL_CONTRACT_UPDATE,
            ALL_CONTRACT_CREATE,
            ALL_CONTRACT_DELETE,

            ALL_TIMEKEEPING_READ,
            ALL_TIMEKEEPING_UPDATE,
            ALL_TIMEKEEPING_CREATE,
            ALL_TIMEKEEPING_DELETE,

            ALL_REQUEST_READ,
            ALL_REQUEST_UPDATE,
            ALL_REQUEST_CREATE,
            ALL_REQUEST_DELETE,
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

            ALL_PERSONNEL_READ,
            ALL_PERSONNEL_UPDATE,
            ALL_PERSONNEL_CREATE,
            ALL_PERSONNEL_DELETE,

            ALL_BRANCH_READ,
            ALL_BRANCH_UPDATE,
            ALL_BRANCH_CREATE,
            ALL_BRANCH_DELETE,

            ALL_DEPARTMENT_READ,
            ALL_DEPARTMENT_UPDATE,
            ALL_DEPARTMENT_CREATE,
            ALL_DEPARTMENT_DELETE,

            ALL_CONTRACT_READ,
            ALL_CONTRACT_UPDATE,
            ALL_CONTRACT_CREATE,
            ALL_CONTRACT_DELETE,

            ALL_TIMEKEEPING_READ,
            ALL_TIMEKEEPING_UPDATE,
            ALL_TIMEKEEPING_CREATE,
            ALL_TIMEKEEPING_DELETE,

            ALL_REQUEST_READ,
            ALL_REQUEST_UPDATE,
            ALL_REQUEST_CREATE,
            ALL_REQUEST_DELETE,
    };
}
