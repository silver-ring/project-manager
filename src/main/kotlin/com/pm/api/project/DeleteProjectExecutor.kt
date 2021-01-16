package com.pm.api.project

import com.pm.api.ApiExecutor
import com.pm.api.NoResponseObject
import com.pm.api.RequestObject
import com.pm.entities.ProjectsRepo
import com.pm.validations.ProjectExistValidation
import com.pm.validations.ValidationContext
import org.springframework.stereotype.Service


class DeleteProjectRequest : RequestObject {
    var id = 0L
}

@Service
class DeleteProjectExecutor(private val projectsRepo: ProjectsRepo, private val validationContext: ValidationContext) :
    ApiExecutor<DeleteProjectRequest, NoResponseObject>() {

    override fun validateInputs(requestObject: DeleteProjectRequest): ValidationContext {
        return validationContext
    }

    override fun validateRules(requestObject: DeleteProjectRequest): ValidationContext {
        validationContext.addValidation(ProjectExistValidation(projectsRepo, requestObject.id))
        return validationContext
    }

    override fun performTransaction(requestObject: DeleteProjectRequest): NoResponseObject {
        projectsRepo.deleteById(requestObject.id)
        return NoResponseObject()
    }

}
