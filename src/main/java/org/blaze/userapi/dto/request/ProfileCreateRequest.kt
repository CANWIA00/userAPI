package org.blaze.userapi.dto.request

import java.time.LocalDate

data class ProfileCreateRequest(
     val fullName: String,

     val profilePhoto: String? = "",

     val bio: String? = "",

     val birthDate: LocalDate,

    )
