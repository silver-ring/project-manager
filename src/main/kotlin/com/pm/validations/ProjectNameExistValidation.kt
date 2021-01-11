package com.pm.validations

import com.pm.entities.ProjectsRepo
import com.pm.exception.InvalidProjectOwner
import com.pm.exception.ProjectNameExist

class ProjectNameExistValidation(private val projectsRepo: ProjectsRepo, private val projectName: String) :
    Validation {
    override fun validate() {
        if (projectsRepo.findProjectByNameEquals(projectName).isPresent) {
            throw ProjectNameExist()
        }
    }
}
