package com.gitlab.szil.servlet.addressbook.backend

import com.gitlab.szil.config.DatabaseConfig
import com.gitlab.szil.model.Contact
import com.gitlab.szil.model.ContactEntity
import io.requery.kotlin.asc
import io.requery.kotlin.desc
import mu.KLogging
import java.util.*


/**
 * Created by Szilank on 18/03/2017.
 */
object ContactService {

    val logger = KLogging().logger

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
                val contact = ContactEntity()
                contact.firstName = fnames[r.nextInt(fnames.size)]
                contact.lastName = lnames[r.nextInt(fnames.size)]
                contact.email = contact.firstName.toLowerCase() + "@" +
                        contact.lastName.toLowerCase() + ".com"
                contact.phone = "+ 358 555 " + (100 + r.nextInt(900))
                cal.set(1930 + r.nextInt(70),
                        r.nextInt(11), r.nextInt(28))
                contact.birthDate = cal.time
                contact.uuid = UUID.randomUUID()
                //contactService.save(contact)
                insert.add(contact)
            }

            logger.info { "inserting : ${insert.size} count" }
            DatabaseConfig.data.insert(insert)
            instance = contactService
        }

        return instance!!
    }

    private val insert = TreeSet(kotlin.Comparator<com.gitlab.szil.model.Contact> { lhs, rhs -> lhs.firstName.compareTo(rhs.firstName) })
    private var nextId: Long = 0

    fun findAll(stringFilter: String?): List<Contact> {
        synchronized(this) {
            // TODO support filtering
            val result = DatabaseConfig.data.select(Contact::class) orderBy (Contact::firstName.asc())

            return result.get().toList()
        }
    }

    fun save(entry: Contact) {
        synchronized(this) {
            if (entry.id == 0L) {
                logger.info { "insert" }
                val newContact = ContactEntity()
                newContact.firstName = entry.firstName
                newContact.lastName = entry.lastName
                newContact.email = entry.email
                newContact.phone = entry.phone
                newContact.birthDate = entry.birthDate
                newContact.uuid = UUID.randomUUID()

                DatabaseConfig.data.insert(entry)
            } else {
                logger.info { "update id: ${entry.id}" }
                DatabaseConfig.data.update(entry)
            }
        }
    }

    fun delete(entry: Contact) {
        DatabaseConfig.data.delete(entry)
    }

    fun count(): Long {
        synchronized(this) {
            return DatabaseConfig.data.count(Contact::class).get().value().toLong()
        }
    }

}