package com.river.bank.service

import com.river.bank.db.entity.Person
import com.river.bank.db.entity.toDto
import com.river.bank.db.repository.PersonRepository
import com.river.bank.dto.PersonDto
import com.river.bank.dto.request.PersonRequestDto
import mu.KotlinLogging
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import javax.persistence.EntityNotFoundException

@Service
class PersonService(private val personRepository: PersonRepository) {

    private val log = KotlinLogging.logger {}

    fun find(pageable: Pageable) = personRepository
        .findAll(pageable)
        .map { it.toDto() }

    fun findById(id: Long) = personRepository.findByIdOrNull(id)
        ?: throw EntityNotFoundException("Person with id = $id was not found")

    fun create(request: PersonRequestDto): PersonDto {
        require(!personRepository.existsByLoginIgnoreCase(request.login)) {
            "Such person already exists"
        }

        val newPerson = Person(request.login)
        val savedPerson = personRepository.save(newPerson)

        log.info { "[Person created] [login: ${request.login}]" }

        return savedPerson.toDto()
    }

}
