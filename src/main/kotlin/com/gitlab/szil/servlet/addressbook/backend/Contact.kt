package com.gitlab.szil.servlet.addressbook.backend

import org.apache.commons.beanutils.BeanUtils
import java.io.Serializable
import java.util.Date

/**
 * Created by Szilank on 18/03/2017.
 */
        data class Contact (
        var id: Long? = null,
        var firstName: String = "",
        var lastName: String = "",
        var phone: String = "",
        var email: String = "",
        var birthDate: Date? = null) : Serializable, Cloneable {

    override fun clone(): Any {
        try {
            return BeanUtils.cloneBean(this)
        } catch (ex : Exception) {
            throw CloneNotSupportedException()
        }
    }

}