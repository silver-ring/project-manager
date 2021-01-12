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
class GetAllProjectsApiTest {

    @MockBean
    lateinit var projectsRepo: ProjectsRepo

    @Autowired
    lateinit var mockMvc: MockMvc

    val projectUrl = "/projects/"

    @Test
    fun gelAllProjects_success() {

        val project1 = Project()
        project1.id = 1L
        project1.name = "test project one"
        project1.state = ProjectState.NOT_STARTED
        project1.ownerId = "owner-one"

        val project2 = Project()
        project2.id = 2
        project2.name = "test project two"
        project2.state = ProjectState.NOT_STARTED
        project2.ownerId = "owner-two"

        val projects = listOf(project1, project2)

        Mockito.`when`(projectsRepo.findAll()).thenReturn(projects)

        val result = mockMvc.perform(
            MockMvcRequestBuilders.get(projectUrl)
        ).andExpect(MockMvcResultMatchers.status().isOk).andReturn().response.contentAsString

        val project1Data = GetAllProjectsResponseData()
        project1Data.ownerId = project1.ownerId!!
        project1Data.projectId = project1.id!!
        project1Data.projectName = project1.name!!

        val project2Data = GetAllProjectsResponseData()
        project2Data.ownerId = project2.ownerId!!
        project2Data.projectId = project2.id!!
        project2Data.projectName = project2.name!!

        val getAllProjectsResponse =  GetAllProjectsResponse()
        getAllProjectsResponse.data = listOf(project1Data, project2Data)


        val expectedResult = jacksonObjectMapper().writeValueAsString(getAllProjectsResponse);

        Assertions.assertFalse(result.isEmpty())
        Assertions.assertEquals(expectedResult, result)
        Mockito.verify(projectsRepo, Mockito.times(1)).findAll()
        Mockito.verifyNoMoreInteractions(projectsRepo)
    }

    @Test
    fun gelAllProjects_noProjectsExist() {

        Mockito.`when`(projectsRepo.findAll()).thenReturn(emptyList())

        val result = mockMvc.perform(
            MockMvcRequestBuilders.get(projectUrl)
        ).andExpect(MockMvcResultMatchers.status().isOk).andReturn().response.contentAsString

        val getAllProjectsResponse =  GetAllProjectsResponse()
        val expectedResult = jacksonObjectMapper().writeValueAsString(getAllProjectsResponse);

        Assertions.assertFalse(result.isEmpty())
        Assertions.assertEquals(expectedResult, result)
        Mockito.verify(projectsRepo, Mockito.times(1)).findAll()
        Mockito.verifyNoMoreInteractions(projectsRepo)
    }

}
