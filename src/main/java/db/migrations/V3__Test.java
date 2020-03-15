package db.migrations;

import com.son.util.flyway.FlywayUtil;
import org.flywaydb.core.api.migration.BaseJavaMigration;
import org.flywaydb.core.api.migration.Context;
import org.springframework.jdbc.core.JdbcTemplate;

public class V3__Test extends BaseJavaMigration {

    @Override
    public void migrate(Context context) {
        String sql = "ALTER TABLE users COMMENT = 'this is user table'";

        JdbcTemplate jdbcTemplate = FlywayUtil.getJdbcTemplate(context);
        jdbcTemplate.execute(sql);
    }
}