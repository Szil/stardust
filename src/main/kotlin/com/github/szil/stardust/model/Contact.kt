package com.github.szil.stardust.model

import io.requery.*
import java.util.*

/**
 * Created by Szilank on 18/03/2017.
 */
@Entity
interface Contact : Persistable {

    @get:Key
    @get:Generated
    val id: Long

    var firstName: String
    var lastName: String
    var phone: String
    var email: String
    var birthDate: Date

    @get:Column(unique = true)
    var uuid: UUID

}