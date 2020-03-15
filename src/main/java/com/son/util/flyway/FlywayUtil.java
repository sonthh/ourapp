package com.son.util.flyway;

import org.flywaydb.core.api.migration.Context;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

public class FlywayUtil {
    public static JdbcTemplate getJdbcTemplate(Context context) {
        return new JdbcTemplate(new SingleConnectionDataSource(context.getConnection(), true));
    }
}
