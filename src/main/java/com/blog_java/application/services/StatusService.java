package com.blog_java.application.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class StatusService {
        private final JdbcTemplate jdbcTemplate;

        @Value("${spring.datasource.name}")
        private String databaseName;

        public StatusService(JdbcTemplate jdbcTemplate) {
            this.jdbcTemplate = jdbcTemplate;
        }

        public Map<String, Object> getDatabaseStatus() {
            String version = jdbcTemplate.queryForObject("SHOW server_version;", String.class);
            Integer maxConnections = jdbcTemplate.queryForObject("SHOW max_connections;", Integer.class);
            Integer usedConnections = jdbcTemplate.queryForObject(
                    "SELECT COUNT(*) FROM pg_stat_activity WHERE datname = ?;",
                    new Object[]{databaseName},
                    Integer.class
            );

            return Map.of(
                    "version", version,
                    "max_conn", maxConnections,
                    "used_conn", usedConnections
            );
        }
    }


