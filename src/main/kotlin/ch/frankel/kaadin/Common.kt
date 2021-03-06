/*
 * Copyright 2016 Nicolas Fränkel
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ch.frankel.kaadin

import com.vaadin.server.*
import com.vaadin.ui.*
import java.net.URI
import java.util.*

fun page(init: Page.() -> Unit = {}) = Page.getCurrent().apply(init)
fun location(uri: String) = page { setLocation(uri) }
fun location(uri: URI) = page { location = uri }
fun title(title: String) = page { setTitle(title) }
fun uriFragment(newUriFragment: String = "", fireEvents: Boolean = true) = page { setUriFragment(newUriFragment, fireEvents) }

fun ui(init: UI.() -> Unit = {}) = UI.getCurrent().apply(init)
fun theme(theme: String) = ui { setTheme(theme) }
fun uiLocale(locale: Locale) = ui { setLocale(locale) }

fun session(init: VaadinSession.() -> Unit = {}) = VaadinSession.getCurrent().apply(init)
fun sessionLocale(locale: Locale) = session { setLocale(locale) }