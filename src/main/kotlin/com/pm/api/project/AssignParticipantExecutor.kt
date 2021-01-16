package com.pm.api.project

import com.pm.api.ApiExecutor
import com.pm.api.RequestObject
import com.pm.api.ResponseObject
import com.pm.entities.ProjectsRepo
import com.pm.validations.*
import org.springframework.stereotype.Service


class AssignParticipantRequest : RequestObject {
    var id: Long = 0
    var employeesId: List<String> = listOf()
}

class AssignParticipantResponse : ResponseObject {
    var id: Long = 0
    var employeesId: List<String> = listOf()
}


@Service
class AssignParticipantExecutor(
    private val validationContext: ValidationContext,
    private val employeesApiProxy: EmployeesApiProxy,
    private val projectsRepo: ProjectsRepo
) : ApiExecutor<AssignParticipantRequest, AssignParticipantResponse>() {

    override fun validateInputs(requestObject: AssignParticipantRequest): ValidationContext {
        return validationContext
    }

    override fun validateRules(requestObject: AssignParticipantRequest): ValidationContext {
        return validationContext
    }

    override fun performTransaction(requestObject: AssignParticipantRequest): AssignParticipantResponse {
        val project = projectsRepo.getOne(requestObject.id)
        project.assignedEmployees = requestObject.employeesId
        val updatedProject = projectsRepo.save(project)
        val assignParticipantResponse = AssignParticipantResponse()
        assignParticipantResponse.id = updatedProject.id!!
        assignParticipantResponse.employeesId = updatedProject.assignedEmployees
        return assignParticipantResponse
    }


}
