package org.blaze.userapi.repository

import org.blaze.userapi.model.Profile
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface ProfileRepository : JpaRepository<Profile, UUID> {

    fun findByUserId(Id: UUID): Profile

    fun findByFullNameStartingWithIgnoreCase(fullName: String?): List<Profile?>?

}