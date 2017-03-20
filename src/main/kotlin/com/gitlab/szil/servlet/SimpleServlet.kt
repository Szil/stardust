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
@WebServlet(urlPatterns = arrayOf("/*"))
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
            .setContextPath("/vaadin")
            .setDeploymentName("simple.war")
            .addServlet(
                    Servlets.servlet("SimpleServlet", SimpleServlet::class.java)
                            .addMapping("/*")
            )

    val manager = Servlets.defaultContainer().addDeployment(servletBuilder)
    SimpleServlet.logger.info { "Starting deployment" }
    manager.deploy()

    val path = Handlers.path(Handlers.redirect("/vaadin"))
            .addPrefixPath("/vaadin", manager.start())

    val server = Undertow.builder()
            .addHttpListener(8080, "localhost")
            .setHandler(path)
            .build()
    server.start()
    val elapsedTime = Duration.between(startTime, Instant.now())
    SimpleServlet.logger.info { "Server started in ${elapsedTime.toMillis()} ms" }
}