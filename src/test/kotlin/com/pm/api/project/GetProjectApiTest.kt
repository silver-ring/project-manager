package com.pm.api.project

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.pm.entities.Project
import com.pm.entities.ProjectState
import com.pm.entities.ProjectsRepo
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

@SpringBootTest
@AutoConfigureMockMvc
class GetProjectApiTest {

    @MockBean
    lateinit var projectsRepo: ProjectsRepo

    @Autowired
    lateinit var mockMvc: MockMvc

    val projectUrl = "/projects/"

    @Test
    fun getProject_success() {

        val project = Project()
        project.id = 2L
        project.name = "test project two"
        project.state = ProjectState.NOT_STARTED
        project.ownerId = "owner-two"

        Mockito.`when`(projectsRepo.existsById(project.id!!)).thenReturn(true)
        Mockito.`when`(projectsRepo.getOne(project.id!!)).thenReturn(project)

        val result = mockMvc.perform(
            MockMvcRequestBuilders.get(projectUrl + project.id)
        ).andExpect(MockMvcResultMatchers.status().isOk).andReturn().response.contentAsString

        val getProjectResponse = GetProjectResponse()
        getProjectResponse.id = project.id!!
        getProjectResponse.name = project.name!!
        getProjectResponse.ownerId = project.ownerId!!
        getProjectResponse.state = project.state!!

        val expectedResult = jacksonObjectMapper().writeValueAsString(getProjectResponse);

        Assertions.assertFalse(result.isEmpty())
        Assertions.assertEquals(expectedResult, result)

        Mockito.verify(projectsRepo, Mockito.times(1)).existsById(project.id!!)
        Mockito.verify(projectsRepo, Mockito.times(1)).getOne(project.id!!)
        Mockito.verifyNoMoreInteractions(projectsRepo)
    }

    @Test
    fun getProject_projectNotFound() {
        val projectId = 2L

        Mockito.`when`(projectsRepo.existsById(projectId)).thenReturn(false)

        val result = mockMvc.perform(
            MockMvcRequestBuilders.get(projectUrl + projectId)
        ).andExpect(MockMvcResultMatchers.status().isNotFound).andReturn().response.contentAsString

        Assertions.assertFalse(result.isEmpty())
        Assertions.assertEquals(
            "{\"detail\":\"Project not found\",\"status\":404,\"title\":\"Not Found\"}",
            result
        )
        Mockito.verify(projectsRepo, Mockito.times(1)).existsById(projectId)
        Mockito.verifyNoMoreInteractions(projectsRepo)
    }

}
