package com.github.szil.stardust.config

import com.github.szil.stardust.model.Models
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.reactivex.Completable
import io.requery.Persistable
import io.requery.sql.KotlinConfiguration
import io.requery.sql.KotlinEntityDataStore
import mu.KotlinLogging
import org.flywaydb.core.Flyway
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import javax.sql.DataSource

/**
 * Created by Szilank on 17/04/2017.
 *
 * Setup class for Database
 *
 */
object DatabaseContext {

    private val JDBC_URL_ENVVAR = "JDBC_DATABASE_URL"

    private val logger = KotlinLogging.logger {}

    val dataSource: DataSource by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
        logger.info { "Creating HikariDataSource" }
        val jdbcUrl = System.getenv(JDBC_URL_ENVVAR)
        val isLocal = jdbcUrl == null

        if (isLocal) {
            logger.info { "Setting up database using local dsConfig" }
            return@lazy HikariDataSource(dsConfig {
                this.jdbcUrl = "jdbc:postgresql://localhost/vertx"
                username = "vertx"
                password = "qwert"
            })
        }

        logger.info { "Setting up database using Heroku dsConfig" }
        HikariDataSource(dsConfig {
            this.jdbcUrl = jdbcUrl
            addDataSourceProperty("cachePrepStmts", "false")
        })
    }

    fun dsConfig(init: HikariConfig.() -> Unit = {}) = HikariConfig()
            .apply {
                maximumPoolSize = 10
                minimumIdle = 3
            }
            .apply(init)

    val executor: ExecutorService by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
        Executors.newFixedThreadPool(10)
    }

    fun executeStatement(statement: KotlinEntityDataStore<Persistable>.() -> Unit = {}) {
        executor.execute { dataStore.apply(statement) }
    }

    fun reactiveExecute(statement: KotlinEntityDataStore<Persistable>.() -> Unit = {}): Completable? {
        return Completable.fromCallable { dataStore.apply(statement) }
    }

    val dataStore: KotlinEntityDataStore<Persistable> by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
        val configuration = KotlinConfiguration(dataSource = dataSource, model = Models.DEFAULT)
        KotlinEntityDataStore<Persistable>(configuration)
    }

    fun runFlywayMigration() {
        val flyway = Flyway()

        flyway.dataSource = dataSource

        flyway.setLocations("db/migration")
        logger.info { "Flyway clean database" }
        flyway.clean()
        logger.info { "Flyway migrate database" }
        flyway.migrate()
    }

}