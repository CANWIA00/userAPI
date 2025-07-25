package org.blaze.userapi.repository

import org.blaze.userapi.model.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface UserRepository : JpaRepository<User, UUID> {

      fun findByEmail(email: String): User?
      fun findUserByProfileId(id: UUID): User?
}