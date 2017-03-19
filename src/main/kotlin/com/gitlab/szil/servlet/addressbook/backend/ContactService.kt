package com.gitlab.szil.servlet.addressbook.backend

import org.apache.commons.beanutils.BeanUtils
import java.util.*
import kotlin.collections.ArrayList


/**
 * Created by Szilank on 18/03/2017.
 */
object ContactService {

    // Create dummy data by randomly combining first and last names
    var fnames = arrayOf("Peter", "Alice", "John", "Mike", "Olivia", "Nina", "Alex", "Rita", "Dan", "Umberto",
            "Henrik", "Rene", "Lisa", "Linda", "Timothy", "Daniel", "Brian", "George", "Scott", "Jennifer")
    var lnames = arrayOf("Smith", "Johnson", "Williams", "Jones", "Brown", "Davis", "Miller", "Wilson", "Moore",
            "Taylor", "Anderson", "Thomas", "Jackson", "White", "Harris", "Martin", "Thompson", "Young", "King", "Robinson")

    var instance: ContactService? = null

    fun createDemoService(): ContactService {
        if (instance == null) {
            val contactService = ContactService

            val r = Random(0)
            val cal = Calendar.getInstance()

            for (i in 0..99) {
                val contact = Contact()
                contact.firstName = fnames[r.nextInt(fnames.size)]
                contact.lastName = lnames[r.nextInt(fnames.size)]
                contact.email = contact.firstName.toLowerCase() + "@" +
                        contact.lastName.toLowerCase() + ".com"
                contact.phone = "+ 358 555 " + (100 + r.nextInt(900))
                cal.set(1930 + r.nextInt(70),
                        r.nextInt(11), r.nextInt(28))
                contact.birthDate = cal.time
                contactService.save(contact)
            }
            instance = contactService
        }

        return instance!!
    }

    private val contacts = HashMap<Long, Contact>()
    private var nextId: Long = 0

    fun findAll(stringFilter: String?): List<Contact> {
        synchronized(this) {
            val arrayList = ArrayList<Contact>()

            contacts.values.filter { stringFilter == null || stringFilter.isEmpty() ||
                    it.toString().toLowerCase().contains(stringFilter.toLowerCase()) }
                    .sortedWith(compareBy { it.id }).toCollection(arrayList)

            return arrayList
        }
    }

    fun save(entry: Contact) {
        synchronized(this) {
            if (entry.id == null) {
                entry.id = nextId++
            }
            val clonedEntry: Contact?
            try {
                clonedEntry = BeanUtils.cloneBean(entry) as Contact
            } catch (ex: Exception) {
                throw RuntimeException(ex)
            }

            contacts.put(clonedEntry.id!!, clonedEntry)
        }
    }

    fun delete(entry: Contact) {
        contacts.remove(entry.id)
    }

    fun count(): Long {
        synchronized(this) {
            return contacts.size.toLong()
        }
    }

}