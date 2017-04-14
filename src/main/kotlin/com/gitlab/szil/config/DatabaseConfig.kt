package com.gitlab.szil.config

import com.gitlab.szil.model.Models
import com.gitlab.szil.servlet.SimpleServlet
import com.heroku.sdk.jdbc.DatabaseUrl
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.requery.Persistable
import io.requery.sql.KotlinConfiguration
import io.requery.sql.KotlinEntityDataStore
import org.flywaydb.core.Flyway
import javax.sql.DataSource

/**
 * Created by Szilank on 19/03/2017.
 */
class DatabaseConfig {

    val envVar = "DATABASE_URL"

    val dataSource: KotlinEntityDataStore<Persistable>
    val logger = SimpleServlet.logger()

    init {
        val ds = initDataSource()
        val configuration = KotlinConfiguration(dataSource = ds, model = Models.DEFAULT)
        dataSource = KotlinEntityDataStore(configuration)
        runFlyway(ds)
    }

    fun initDataSource(): DataSource {
        val jdbcUrl = System.getenv(envVar)
        logger.info { "JdbcUrl: $jdbcUrl" }
        val local = jdbcUrl == null

        val config = HikariConfig()

        if (local) {
            logger.info { "Setting up database using local config" }
            config.jdbcUrl = "jdbc:postgresql://localhost/vertx"
            config.username = "vertx"
            config.password = "qwert"
        } else {
            logger.info { "Setting up database using Heroku config" }
            config.jdbcUrl = jdbcUrl
        }

        config.addDataSourceProperty("cachePrepStmts", "true")
        config.addDataSourceProperty("prepStmtCacheSize", "250")
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048")

        return HikariDataSource(config)
    }

    fun runFlyway(dataSource: DataSource) {
        val flyway = Flyway()

        flyway.dataSource = dataSource

        flyway.setLocations("db/migration")
        flyway.clean()
        flyway.migrate()
    }

    companion object {
        val data = DatabaseConfig().dataSource
    }

}