package com.ecommerce.dbhelper

import com.ecommerce.entities.ShippingTable
import com.ecommerce.entities.product.category.ProductCategoryTable
import com.ecommerce.entities.product.category.ProductSubCategoryTable
import com.ecommerce.entities.orders.CartItemTable
import com.ecommerce.entities.orders.OrderItemTable
import com.ecommerce.entities.orders.OrdersTable
import com.ecommerce.entities.product.*
import com.ecommerce.entities.shop.ShopCategoryTable
import com.ecommerce.entities.shop.ShopTable
import com.ecommerce.entities.user.UserProfileTable
import com.ecommerce.entities.user.UserTable
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.flywaydb.core.Flyway
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.SchemaUtils.create
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.slf4j.LoggerFactory
import java.net.URI
import javax.sql.DataSource

object DatabaseFactory {
    private val log = LoggerFactory.getLogger(this::class.java)
    fun init() {
        initDB()
        //Database.connect(hikari())
        transaction {
            // print sql to std-out
            addLogger(StdOutSqlLogger)
            create(
                UserTable,
                UserProfileTable,
                ShopTable,
                ShopCategoryTable,
                ProductTable,
                ProductImageTable,
                ProductCategoryTable,
                ProductSubCategoryTable,
                BrandTable,
                CartItemTable,
                OrdersTable,
                OrderItemTable,
                WishListTable,
                ShippingTable
            )
        }
    }

    private fun initDB() {
        val config = HikariConfig("/hikari.properties")
        val dataSource = HikariDataSource(config)
        runFlyway(dataSource)
        Database.connect(dataSource)
    }

    private fun hikari(): HikariDataSource {
        val config = HikariConfig()
        config.driverClassName = System.getenv("JDBC_DRIVER")
        config.jdbcUrl = System.getenv("HEROKU_POSTGRESQL_NAVY_URL")
        config.maximumPoolSize = 3
        config.isAutoCommit = true
        config.transactionIsolation = "TRANSACTION_REPEATABLE_READ"
        config.validate()
        return HikariDataSource(config)
    }

    // For heroku deployment
    private fun hikariForHeroku(): HikariDataSource {
        val config = HikariConfig()
        config.driverClassName = System.getenv("JDBC_DRIVER")
        config.isAutoCommit = false
        config.transactionIsolation = "TRANSACTION_REPEATABLE_READ"

        val uri = URI(System.getenv("DATABASE_URL"))
        val username = uri.userInfo.split(":").toTypedArray()[0]
        val password = uri.userInfo.split(":").toTypedArray()[1]

        config.jdbcUrl =
            "jdbc:postgresql://" + uri.host + ":" + uri.port + uri.path + "?sslmode=require" + "&user=$username&password=$password"

        config.validate()
        return HikariDataSource(config)
    }

    private fun runFlyway(datasource: DataSource) {
        val flyway = Flyway.configure().dataSource(datasource).load()
        try {
            flyway.info()
            flyway.migrate()
        } catch (e: Exception) {
            log.error("Exception running flyway migration", e)
            throw e
        }
        log.info("Flyway migration has finished")
    }
}

suspend fun <T> query(block: () -> T): T = withContext(Dispatchers.IO) {
    transaction {
        block()
    }
}