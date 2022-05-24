package com.river.bank.db.repository

import com.river.bank.db.entity.Operation
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository

interface OperationRepository : JpaRepository<Operation, Long> {

    @EntityGraph(attributePaths = ["source.person", "dest.person"])
    fun findBySourceIdOrDestId(sourceId: Long, destId: Long, pageable: Pageable): Page<Operation>

}
