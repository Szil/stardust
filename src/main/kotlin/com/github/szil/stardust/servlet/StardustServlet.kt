/*
 * Copyright 2017 Gergő Nemes
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.szil.stardust.servlet

import com.github.szil.stardust.config.DatabaseContext
import com.github.szil.stardust.servlet.addressbook.AddressbookUI
import com.github.szil.stardust.servlet.addressbook.backend.ContactService
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
class StardustServlet : VaadinServlet() {
    companion object : KLogging()
}

fun main(args: Array<String>) {
    DatabaseContext.runFlywayMigration()
    ContactService.insertDummyData()

    StardustServlet.logger.info { "init undertow servlet" }
    val startTime = Instant.now()
    val servletBuilder = Servlets.deployment().setClassLoader(StardustServlet::class.java.classLoader)
            .setContextPath("/app")
            .setDeploymentName("stardust.war")
            .addServlet(
                    Servlets.servlet("StardustServlet", StardustServlet::class.java)
                            .addMapping("/*")
            )

    val manager = Servlets.defaultContainer().addDeployment(servletBuilder)
    StardustServlet.logger.info { "Starting deployment" }
    manager.deploy()

    val path = Handlers.path(Handlers.redirect("/app"))
            .addPrefixPath("/app", manager.start())

    val envPort : String? = System.getenv("PORT")
    val port = envPort?.toInt() ?: 8080
    val host = "0.0.0.0"

    val server = Undertow.builder()
            .addHttpListener(port, host)
            .setHandler(path)
            .build()
    server.start()
    StardustServlet.logger.info { "Server running on $port port and host: $host" }
    val elapsedTime = Duration.between(startTime, Instant.now())
    StardustServlet.logger.info { "Server started in ${elapsedTime.toMillis()} ms" }
}