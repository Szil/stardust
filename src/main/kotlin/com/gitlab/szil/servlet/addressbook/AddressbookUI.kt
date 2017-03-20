package com.gitlab.szil.servlet.addressbook

import com.gitlab.szil.model.Contact
import com.gitlab.szil.model.ContactEntity
import com.gitlab.szil.servlet.addressbook.backend.ContactService
import com.vaadin.annotations.Theme
import com.vaadin.annotations.Title
import com.vaadin.data.provider.ListDataProvider
import com.vaadin.server.VaadinRequest
import com.vaadin.ui.*


/**
 * Created by Szilank on 18/03/2017.
 */
@Title("Addressbook")
@Theme("valo")
class AddressbookUI : UI() {

    val filter: TextField = TextField()
    val contactList: Grid<Contact> = Grid()
    val newContact: Button = Button("New Contact")

    val contactForm: ContactForm = ContactForm()

    val service: ContactService = ContactService.createDemoService()

    override fun init(request: VaadinRequest?) {
        configureComponents()
        buildLayout()
    }

    private fun configureComponents(): Unit {
        /*
         * Synchronous event handling.
         *
         * Receive user interaction events on the server-side. This allows you
         * to synchronously handle those events. Vaadin automatically sends only
         * the needed changes to the web page without loading a new page.
         */
        newContact.addClickListener { _ -> contactForm.edit(ContactEntity()) }

        filter.placeholder = "Filter contacts..."
        filter.addValueChangeListener { refreshContacts(it.value) }

        contactList.dataProvider = ListDataProvider<Contact>(ArrayList())
        contactList.addColumn(Contact::firstName).caption = "First name"
        contactList.addColumn(Contact::lastName).caption = "Last name"
        contactList.addColumn(Contact::email).caption = "Email"

        contactList.setSelectionMode(Grid.SelectionMode.SINGLE)
        contactList.addSelectionListener { _ -> contactForm.edit(contactList.selectedItems.firstOrNull()) }
        refreshContacts()
    }

    fun refreshContacts() {
        refreshContacts(filter.value)
    }

    fun refreshContacts(stringFilter: String) {
        contactList.dataProvider = ListDataProvider(service.findAll(stringFilter))
        contactForm.isVisible = false
    }

    private fun buildLayout(): Unit {
        val actions = HorizontalLayout(filter, newContact)
        actions.setWidth("100%")
        filter.setWidth("100%")
        actions.setExpandRatio(filter, 1f)

        val left = VerticalLayout(actions, contactList)
        left.setSizeFull()
        contactList.setSizeFull()
        left.setExpandRatio(contactList, 1f)

        val mainLayout = HorizontalLayout(left, contactForm)
        mainLayout.setSizeFull()
        mainLayout.setExpandRatio(left, 1f)

        // Split and allow resizing
        content = mainLayout
    }

}