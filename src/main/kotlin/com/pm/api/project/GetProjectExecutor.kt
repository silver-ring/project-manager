package com.pm.api.project

import com.pm.api.ApiExecutor
import com.pm.api.RequestObject
import com.pm.api.ResponseObject
import com.pm.entities.ProjectState
import com.pm.entities.ProjectsRepo
import com.pm.validations.ProjectExistValidation
import com.pm.validations.ValidationContext
import org.springframework.stereotype.Service


class GetProjectRequest : RequestObject {
    var id = 0L
}

class GetProjectResponse : ResponseObject {
    var id = 0L
    var name = ""
    var ownerId = ""
    var state = ProjectState.NOT_STARTED
}

@Service
class GetProjectExecutor(
    private val validationContext: ValidationContext,
    private val projectsRepo: ProjectsRepo
) :
    ApiExecutor<GetProjectRequest, GetProjectResponse>() {

    override fun validateInputs(requestObject: GetProjectRequest): ValidationContext {
        return validationContext
    }

    override fun validateRules(requestObject: GetProjectRequest): ValidationContext {
        validationContext.addValidation(ProjectExistValidation(projectsRepo, requestObject.id))
        return validationContext
    }

    override fun performTransaction(requestObject: GetProjectRequest): GetProjectResponse {
        val project = projectsRepo.getOne(requestObject.id)
        val getProjectResponse = GetProjectResponse()
        getProjectResponse.id = project.id!!
        getProjectResponse.name = project.name!!
        getProjectResponse.ownerId = project.ownerId!!
        getProjectResponse.state = project.state!!
        return getProjectResponse
    }

}
