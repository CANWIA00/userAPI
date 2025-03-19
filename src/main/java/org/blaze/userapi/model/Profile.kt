package org.blaze.userapi.model

import jakarta.persistence.*
import java.sql.Timestamp
import java.time.Instant
import java.time.LocalDate
import java.util.*

@Entity
class Profile(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private val id: UUID? = null,

    @OneToOne(mappedBy = "profile")
    private var user: User? = null,

    private val name: String,

    private val surname: String,

    private val profilePhoto: String? = "",

    private val bio: String? = "",

    private val birthDate: LocalDate,

    private val userStatus: Status,

    private val lastSeen: Timestamp
) {

    constructor() : this(
        id = null,
        name = "",
        surname = "",
        profilePhoto = "",
        bio = "",
        birthDate = LocalDate.now(),
        userStatus = Status.OFFLINE,
        lastSeen = Timestamp.from(Instant.now())
    )
}


