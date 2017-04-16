package com.gitlab.szil.servlet

import com.gitlab.szil.config.DatabaseConfig
import com.gitlab.szil.servlet.addressbook.AddressbookUI
import com.vaadin.annotations.VaadinServletConfiguration
import com.vaadin.server.VaadinServlet
import io.undertow.Handlers
import io.undertow.Undertow
import io.undertow.servlet.Servlets
import mu.KLogging
import java.time.Duration
import java.time.Instant
import javax.servlet.annotation.WebServlet

/**
 * Created by Szilank on 18/03/2017.
 */
@WebServlet(value = "/*", asyncSupported = true)
@VaadinServletConfiguration(ui = AddressbookUI::class, productionMode = false)
class SimpleServlet : VaadinServlet() {
    companion object : KLogging()
}

fun main(args: Array<String>) {
    SimpleServlet.logger.info { "setting up database connection" }
    DatabaseConfig()

    SimpleServlet.logger.info { "init undertow servlet" }
    val startTime = Instant.now()
    val servletBuilder = Servlets.deployment().setClassLoader(SimpleServlet::class.java.classLoader)
            .setContextPath("/simple")
            .setDeploymentName("simple.war")
            .addServlet(
                    Servlets.servlet("SimpleServlet", SimpleServlet::class.java)
                            .addMapping("/*")
            )

    val manager = Servlets.defaultContainer().addDeployment(servletBuilder)
    SimpleServlet.logger.info { "Starting deployment" }
    manager.deploy()

    val path = Handlers.path(Handlers.redirect("/simple"))
            .addPrefixPath("/simple", manager.start())

    val envPort : String? = System.getenv("PORT")
    val port = envPort?.toInt() ?: 8080
    val host = "0.0.0.0"

    val server = Undertow.builder()
            .addHttpListener(port, host)
            .setHandler(path)
            .build()
    server.start()
    SimpleServlet.logger.info { "Server running on $port port and host: $host" }
    val elapsedTime = Duration.between(startTime, Instant.now())
    SimpleServlet.logger.info { "Server started in ${elapsedTime.toMillis()} ms" }
}