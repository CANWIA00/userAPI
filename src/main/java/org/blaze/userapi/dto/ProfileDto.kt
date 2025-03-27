package org.blaze.userapi.dto


import org.blaze.userapi.model.Status
import java.sql.Timestamp
import java.time.Instant
import java.time.LocalDate
import java.util.UUID

data class ProfileDto(

     val id: UUID,

     val fullName: String,

     val userId: UUID,

     val profilePhoto: String? = "",

     val bio: String? = "",

     val birthDate: LocalDate,

     val userStatus: Status,

     val lastSeen: Instant


)
