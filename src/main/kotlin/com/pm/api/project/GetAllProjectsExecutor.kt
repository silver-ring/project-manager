package com.pm.api.project

import com.pm.api.ApiExecutor
import com.pm.api.NoRequestObject
import com.pm.entities.ProjectsRepo
import com.pm.validations.ValidationContext
import org.springframework.stereotype.Service
import kotlin.streams.toList

@Service
class GetAllProjectsExecutor(
    private val projectsRepo: ProjectsRepo,
    private val validationContext: ValidationContext
) :
    ApiExecutor<NoRequestObject, GetAllProjectsResponse>() {

    override fun validateInputs(requestObject: NoRequestObject): ValidationContext {
        return validationContext
    }

    override fun validateRules(requestObject: NoRequestObject): ValidationContext {
        return validationContext
    }

    override fun performTransaction(requestObject: NoRequestObject): GetAllProjectsResponse {
        val projects = projectsRepo.findAll()
        val getAllProjectsResponseDataItems = projects.stream().map {
            val getAllProjectsResponseData = GetAllProjectsResponseData()
            getAllProjectsResponseData.ownerId = it.ownerId!!
            getAllProjectsResponseData.projectId = it.id!!
            getAllProjectsResponseData.projectName = it.name!!
            getAllProjectsResponseData
        }.toList()
        val getAllProjectsResponse = GetAllProjectsResponse()
        getAllProjectsResponse.data = getAllProjectsResponseDataItems
        return getAllProjectsResponse
    }
}

