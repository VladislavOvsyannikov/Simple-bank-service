package com.river.bank.service

import com.river.bank.db.entity.Person
import com.river.bank.db.repository.PersonRepository
import com.river.bank.dto.request.PersonRequestDto
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers.anyLong
import org.mockito.ArgumentMatchers.anyString
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import java.util.Optional
import javax.persistence.EntityNotFoundException

@ExtendWith(MockitoExtension::class)
internal class PersonServiceTest {

    @Mock
    lateinit var personRepository: PersonRepository
    @InjectMocks
    lateinit var personService: PersonService

    @Test
    fun find() {
        `when`(personRepository.findAll(any<Pageable>())).thenReturn(PageImpl(listOf(Person("login"))))

        val persons = personService.find(Pageable.unpaged())

        assertEquals(1, persons.size)
        assertEquals("login", persons.content[0].login)
        verify(personRepository, times(1)).findAll(any<Pageable>())
    }

    @Test
    fun findById_personFound_success() {
        `when`(personRepository.findById(anyLong())).thenReturn(Optional.of(Person("login")))

        val person = personService.findById(100)

        assertEquals("login", person.login)
        verify(personRepository, times(1)).findById(anyLong())
    }

    @Test
    fun findById_personNotFound_exception() {
        `when`(personRepository.findById(anyLong())).thenReturn(Optional.empty())

        assertThrows<EntityNotFoundException> { personService.findById(100) }

        verify(personRepository, times(1)).findById(anyLong())
    }

    @Test
    fun create_personAlreadyExists_exception() {
        `when`(personRepository.existsByLoginIgnoreCase(anyString())).thenReturn(true)

        assertThrows<IllegalArgumentException> { personService.create(PersonRequestDto("login")) }

        verify(personRepository, times(1)).existsByLoginIgnoreCase(anyString())
    }

    @Test
    fun create_correctUrl_successfulCreation() {
        `when`(personRepository.existsByLoginIgnoreCase(anyString())).thenReturn(false)
        `when`(personRepository.save(any())).thenAnswer { it.getArgument(0) }

        val person = personService.create(PersonRequestDto("login"))

        assertEquals("login", person.login)
        verify(personRepository, times(1)).existsByLoginIgnoreCase(anyString())
        verify(personRepository, times(1)).save(any())
    }

}
