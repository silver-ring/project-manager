package com.pm.validations

import com.pm.exception.MissingOwnerId

class ProjectOwnerValidation(private val ownerId: String) : Validation {

    override fun validate() {
        if (ownerId.isBlank()) {
            throw MissingOwnerId()
        }
    }

}
