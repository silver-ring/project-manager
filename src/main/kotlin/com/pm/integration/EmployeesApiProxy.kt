package com.pm.api.project

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod


enum class Departments {
    sales, engineering, marketing, noDepartment
}

enum class Roles {
    employee, manager, noRole
}

class Employee {

    var id = ""
    var first_name = ""
    var last_name = ""
    var email = ""
    var department = Departments.noDepartment
    var role = Roles.noRole

}

@FeignClient(
    value = "\${feign.api.employee.name}",
    url = "\${feign.api.employee.url}"
)
interface EmployeesApiProxy {

    @RequestMapping(method = [RequestMethod.GET], value = ["/employees"])
    fun getEmployees(): List<Employee>

    @RequestMapping(method = [RequestMethod.GET], value = ["/employees/{employeeId}"])
    fun getEmployee(@PathVariable employeeId: String): Employee

}
