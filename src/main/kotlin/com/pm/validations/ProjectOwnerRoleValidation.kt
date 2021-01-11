package com.pm.validations

import com.pm.api.project.EmployeesApiProxy
import com.pm.api.project.Roles
import com.pm.exception.InvalidProjectOwner

class ProjectOwnerRoleValidation(
    private val employeesApiProxy: EmployeesApiProxy,
    private val ownerId: String
) : Validation {

    override fun validate() {
        val employee = employeesApiProxy.getEmployee(ownerId);
        if (employee.role != Roles.manager) {
            throw InvalidProjectOwner()
        }
    }

}
