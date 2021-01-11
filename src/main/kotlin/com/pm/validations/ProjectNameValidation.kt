package com.pm.validations

import com.pm.exception.MissingProjectName

class ProjectNameValidation(private val projectName: String) : Validation {

    override fun validate() {
        if (projectName.isBlank()) {
            throw MissingProjectName()
        }
    }
}
