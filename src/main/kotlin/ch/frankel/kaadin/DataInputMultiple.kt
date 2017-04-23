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

import com.vaadin.data.*
import com.vaadin.data.provider.DataCommunicator
import com.vaadin.data.provider.DataProvider
import com.vaadin.server.*
import com.vaadin.ui.*

/**
 * see http://demo.vaadin.com/sampler/#ui/data-input/multiple-value
 */
private fun <T, S : AbstractListing<T>> S.process(container: HasComponents,
                                                  caption: String?,
                                                  dataProvider: DataProvider<T, *>,
                                                  onValueChange: (event: HasValue.ValueChangeEvent<T>) -> Unit,
                                                  init: S.() -> Unit): S {
    return apply {
        caption?.let { this.caption = caption }
        //addValueChangeListener(onValueChange)
        //this.dataProvider = dataProvider
    }
            .apply(init)
            .addTo(container)
}

/* TODO Moslty deprecated (or wrong interface)
fun AbstractListing.allowNewItems() = set setNewItemsAllowed(true)
fun AbstractListing.disallowNewItems() = setNewItemsAllowed(false)
fun AbstractListing.allowNullSelection() = setNullSelectionAllowed(true)
fun AbstractListing.disallowNullSelection() = setNullSelectionAllowed(false)
fun AbstractListing.selectSingle() = setMultiSelect(false)
fun AbstractListing.selectMulti() = setMultiSelect(true)
fun AbstractListing.itemCaption(itemId: Any, caption: String) = setItemCaption(itemId, caption)
fun AbstractListing.unsetItemCaption(itemId: Any) = setItemCaption(itemId, null)
fun AbstractListing.itemIcon(itemId: Any, icon: Resource) = setItemIcon(itemId, icon)
fun AbstractListing.unsetItemIcon(itemId: Any) = setItemIcon(itemId, null)*/

/* FIXME
fun HasComponents.comboBox(caption: String? = null,
                           vararg options: Any,
                           onValueChange: (event: Property.ValueChangeEvent) -> Unit = {},
                           init: ComboBox.() -> Unit = {}) = comboBox(caption, options.toList(), onValueChange, init)

fun HasComponents.comboBox(caption: String? = null,
                           options: Collection<Any> = emptyList(),
                           onValueChange: (event: Property.ValueChangeEvent) -> Unit = {},
                           init: ComboBox.() -> Unit = {}) = ComboBox()
        .process(this, caption, options, IndexedContainer(), onValueChange, init)

fun HasComponents.comboBox(caption: String? = null,
                           dataSource: Container,
                           onValueChange: (event: Property.ValueChangeEvent) -> Unit = {},
                           init: ComboBox.() -> Unit = {}) = ComboBox()
        .process(this, caption, emptyList(), dataSource, onValueChange, init)

fun ComboBox.allowTextInput() = setTextInputAllowed(true)
fun ComboBox.disallowTextInput() = setTextInputAllowed(false)

fun HasComponents.nativeSelect(caption: String? = null,
                               vararg options: Any,
                               onValueChange: (event: Property.ValueChangeEvent) -> Unit = {},
                               init: NativeSelect.() -> Unit = {}) = nativeSelect(caption, options.toList(), onValueChange, init)

fun HasComponents.nativeSelect(caption: String? = null,
                               options: Collection<Any> = emptyList(),
                               onValueChange: (event: Property.ValueChangeEvent) -> Unit = {},
                               init: NativeSelect.() -> Unit = {}) = NativeSelect()
        .process(this, caption, options, IndexedContainer(), onValueChange, init)

fun HasComponents.nativeSelect(caption: String? = null,
                               dataSource: Container,
                               onValueChange: (event: Property.ValueChangeEvent) -> Unit = {},
                               init: NativeSelect.() -> Unit = {}) = NativeSelect()
        .process(this, caption, emptyList(), dataSource, onValueChange, init)

fun HasComponents.listSelect(caption: String? = null,
                             vararg options: Any,
                             onValueChange: (event: Property.ValueChangeEvent) -> Unit = {},
                             init: ListSelect.() -> Unit = {}) = listSelect(caption, options.toList(), onValueChange, init)

fun HasComponents.listSelect(caption: String? = null,
                             options: Collection<Any> = emptyList(),
                             onValueChange: (event: Property.ValueChangeEvent) -> Unit = {},
                             init: ListSelect.() -> Unit = {}) = ListSelect()
        .process(this, caption, options, IndexedContainer(), onValueChange, init)

fun HasComponents.listSelect(caption: String? = null,
                             dataSource: Container,
                             onValueChange: (event: Property.ValueChangeEvent) -> Unit = {},
                             init: ListSelect.() -> Unit = {}) = ListSelect()
        .process(this, caption, emptyList(), dataSource, onValueChange, init)

fun HasComponents.twinColSelect(caption: String? = null,
                                vararg options: Any,
                                onValueChange: (event: Property.ValueChangeEvent) -> Unit = {},
                                init: TwinColSelect.() -> Unit = {}) = twinColSelect(caption, options.toList(), onValueChange, init)

fun HasComponents.twinColSelect(caption: String? = null,
                                options: Collection<Any> = emptyList(),
                                onValueChange: (event: Property.ValueChangeEvent) -> Unit = {},
                                init: TwinColSelect.() -> Unit = {}) = TwinColSelect()
        .process(this, caption, options, IndexedContainer(), onValueChange, init)

fun HasComponents.twinColSelect(caption: String? = null,
                                dataSource: Container,
                                onValueChange: (event: Property.ValueChangeEvent) -> Unit = {},
                                init: TwinColSelect.() -> Unit = {}) = TwinColSelect()
        .process(this, caption, emptyList(), dataSource, onValueChange, init)

fun HasComponents.optionGroup(caption: String? = null,
                              options: Collection<Any> = emptyList(),
                              onValueChange: (event: Property.ValueChangeEvent) -> Unit = {},
                              init: OptionGroup.() -> Unit = {}) = OptionGroup()
        .process(this, caption, options, IndexedContainer(), onValueChange, init)

fun HasComponents.optionGroup(caption: String? = null,
                              vararg options: String,
                              onValueChange: (event: Property.ValueChangeEvent) -> Unit = {},
                              init: OptionGroup.() -> Unit = {}) = optionGroup(caption, options.toList(), onValueChange, init)

fun HasComponents.optionGroup(caption: String? = null,
                              dataSource: Container,
                              onValueChange: (event: Property.ValueChangeEvent) -> Unit = {},
                              init: OptionGroup.() -> Unit = {}) = OptionGroup()
        .process(this, caption, emptyList(), dataSource, onValueChange, init)

fun OptionGroup.allowHtml() = setHtmlContentAllowed(true)
fun OptionGroup.disallowHtml() = setHtmlContentAllowed(false)
fun OptionGroup.enableItem(itemId: Any) = setItemEnabled(itemId, true)
fun OptionGroup.disableItem(itemId: Any) = setItemEnabled(itemId, false)*/
