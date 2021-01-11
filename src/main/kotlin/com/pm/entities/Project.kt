package com.pm.entities

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Repository
interface ProjectsRepo : JpaRepository<Project, Long> {
    fun findProjectByNameEquals(projectName: String): Optional<Project>
}

enum class ProjectState {
    PLANNED,
    ACTIVE,
    DONE,
    FAILED,
    NOT_STARTED
}


@Entity
class Project {

    @Id
    @GeneratedValue
    var id: Long? = null
    var name: String? = null
    var ownerId: String? = null
    var state: ProjectState? = null

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as Project
        if (id != other.id) return false
        if (name != other.name) return false
        if (ownerId != other.ownerId) return false
        if (state != other.state) return false
        return true
    }

    override fun hashCode(): Int {
        var result = id?.hashCode() ?: 0
        result = 31 * result + (name?.hashCode() ?: 0)
        result = 31 * result + (ownerId?.hashCode() ?: 0)
        result = 31 * result + (state?.hashCode() ?: 0)
        return result
    }

}