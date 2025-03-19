package org.blaze.userapi.repository

import org.blaze.userapi.model.BlockedUser
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface BlockedUserRepository : JpaRepository<BlockedUser, UUID> {
}