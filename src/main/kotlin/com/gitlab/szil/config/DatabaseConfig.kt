package com.gitlab.szil.config

import com.gitlab.szil.model.Models
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

    val dataSource : KotlinEntityDataStore<Persistable>
    
    init {
        val ds = initDataSource()
        val configuration = KotlinConfiguration(dataSource = ds, model = Models.DEFAULT)
        dataSource = KotlinEntityDataStore(configuration)
        runFlyway(ds)
    }

    fun initDataSource(): DataSource {
        val local = "cedar-14" != System.getenv("STACK")

        var databaseUrl = DatabaseUrl.extract(local)

        val config = HikariConfig()
        config.jdbcUrl = databaseUrl.jdbcUrl()
        config.username = databaseUrl.username()
        config.password = databaseUrl.password()
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