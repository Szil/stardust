package com.gitlab.szil.servlet.addressbook

import com.gitlab.szil.model.Contact
import com.vaadin.data.Binder
import com.vaadin.data.converter.LocalDateToDateConverter
import com.vaadin.event.ShortcutAction
import com.vaadin.ui.*
import com.vaadin.ui.themes.ValoTheme


/**
 * Created by Szilank on 18/03/2017.
 */
class ContactForm : FormLayout() {

    var saveBtn = Button("Save", this::save)
    var cancelBtn = Button("Cancel", this::cancel)
    var firstName = TextField("First name")
    var lastName = TextField("Last name")
    var phone = TextField("Phone")
    var email = TextField("Email")
    var birthDate = DateField("Birth date")

    var contact: Contact? = null

    val binder = Binder<Contact>(Contact::class.java)

    init {
        configureComponents()
        buildLayout()
    }

    private fun buildLayout() {
        setSizeUndefined()
        setMargin(true)

        val actions = HorizontalLayout(saveBtn, cancelBtn)
        actions.isSpacing = true

        addComponents(actions, firstName, lastName, phone, email, birthDate)
    }

    private fun configureComponents() {
        saveBtn.styleName = ValoTheme.BUTTON_PRIMARY
        saveBtn.setClickShortcut(ShortcutAction.KeyCode.ENTER)
        isVisible = false
    }

    fun save(event: Button.ClickEvent) {
        try {
            binder.writeBean(contact)

            // Save DAO to backend with direct synchronous service API
            ui!!.service.save(contact!!)

            Notification.show("Saved '${contact!!.firstName} ${contact!!.lastName}'.", Notification.Type.TRAY_NOTIFICATION)
            ui!!.refreshContacts()
        } catch (ex: Exception) {
            // handle1
        }
    }

    fun cancel(event: Button.ClickEvent) {
        Notification.show("Cancelled", Notification.Type.TRAY_NOTIFICATION)
        ui!!.contactList.deselectAll()
        isVisible = false
    }

    fun edit(contact: Contact?) {
        this.contact = contact
        if (contact != null) {
            binder.forField(firstName)
                    .bind(Contact::firstName.getter, Contact::firstName.setter)
            binder.forField(lastName)
                    .bind(Contact::lastName.getter, Contact::lastName.setter)
            binder.forField(phone)
                    .bind(Contact::phone.getter, Contact::phone.setter)
            binder.forField(email)
                    .bind(Contact::email.getter, Contact::email.setter)
            binder.forField(birthDate)
                    .withConverter(LocalDateToDateConverter())
                    .bind(Contact::birthDate.getter, Contact::birthDate.setter)

            binder.readBean(contact)
            firstName.focus()
        }
        isVisible = contact != null
    }

    override fun getUI(): AddressbookUI? {
        return super.getUI() as AddressbookUI?
    }

}