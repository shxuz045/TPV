package com.sonia.tpv.config;

import java.sql.Connection;
import java.sql.Statement;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DatabaseMigrationConfig {

    @Autowired
    public void migrate(DataSource dataSource) {
        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute("ALTER TABLE comandas ADD COLUMN IF NOT EXISTS ticket_impreso BOOLEAN DEFAULT FALSE NOT NULL");
        } catch (Exception ignored) {
        }
    }
}

