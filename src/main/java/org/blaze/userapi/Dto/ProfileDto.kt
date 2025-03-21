package org.blaze.userapi.Dto

import org.blaze.userapi.model.Status
import java.sql.Timestamp
import java.time.LocalDate

data class ProfileDto(
    private val fullName: String,

    private val profilePhoto: String? = "",

    private val bio: String? = "",

    private val birthDate: LocalDate,

    private val userStatus: Status,

    private val lastSeen: Timestamp
)
