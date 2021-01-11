package com.pm.validations

import org.springframework.stereotype.Component
import java.util.*

@Component
class ValidationContext {

    var validations: Queue<Validation> = LinkedList()

    fun addValidation(validation: Validation) {
        validations.add(validation)
    }

    fun validate() {
        val validationIterator = validations.iterator()
        while (validationIterator.hasNext()) {
            validations.poll().validate()
        }
    }

}
