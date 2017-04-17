package com.github.szil.stardust.servlet.addressbook.backend

import com.github.szil.stardust.config.DatabaseContext
import com.github.szil.stardust.model.Contact
import com.github.szil.stardust.model.ContactEntity
import io.requery.kotlin.asc
import mu.KotlinLogging
import java.util.*


/**
 * Created by Szilank on 18/03/2017.
 */
object ContactService {

    private val logger = KotlinLogging.logger {}

    // Create dummy data by randomly combining first and last names
    var fnames = arrayOf("Peter", "Alice", "John", "Mike", "Olivia", "Nina", "Alex", "Rita", "Dan", "Umberto",
            "Henrik", "Rene", "Lisa", "Linda", "Timothy", "Daniel", "Brian", "George", "Scott", "Jennifer")
    var lnames = arrayOf("Smith", "Johnson", "Williams", "Jones", "Brown", "Davis", "Miller", "Wilson", "Moore",
            "Taylor", "Anderson", "Thomas", "Jackson", "White", "Harris", "Martin", "Thompson", "Young", "King", "Robinson")

    fun insertDummyData() {

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
            entitiesTreeSet.add(contact)
        }

        logger.info { "inserting : ${entitiesTreeSet.size} count" }
        DatabaseContext.executeStatement {
            insert(entitiesTreeSet)
        }

    }

    private val entitiesTreeSet = TreeSet(kotlin.Comparator<Contact> { lhs, rhs -> lhs.firstName.compareTo(rhs.firstName) })
    private var nextId: Long = 0

    fun findAll(stringFilter: String?): List<Contact> {
        synchronized(this) {
            // TODO support filtering
            val result = DatabaseContext.dataStore.select(Contact::class) orderBy (Contact::firstName.asc())

            return result.get().toList()
        }
    }

    fun save(entry: Contact) {
        synchronized(this) {
            if (entry.id == 0L) {
                val newContact = ContactEntity()
                newContact.firstName = entry.firstName
                newContact.lastName = entry.lastName
                newContact.email = entry.email
                newContact.phone = entry.phone
                newContact.birthDate = entry.birthDate
                newContact.uuid = UUID.randomUUID()

                logger.info { "inserting new contact $newContact" }
                // DatabaseContext.executeStatement { insert(entry) }
                DatabaseContext.dataStore.insert(entry)
            } else {
                logger.info { "update id: ${entry.id}" }
                // DatabaseContext.executeStatement { update(entry) }
                DatabaseContext.dataStore.update(entry)
            }
        }
    }

    fun delete(entry: Contact) {
        DatabaseContext.dataStore.delete(entry)
    }

    fun count(): Long {
        synchronized(this) {
            return DatabaseContext.dataStore.count(Contact::class).get().value().toLong()
        }
    }

}