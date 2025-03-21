package org.blaze.userapi.model

import jakarta.persistence.*
import java.sql.Timestamp
import java.time.Instant
import java.time.LocalDate
import java.util.*

@Entity
data class Profile(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private val id: UUID? = null,

    @OneToOne(cascade = [(CascadeType.ALL)],fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private var user: User,

    private val fullName: String,

    private val profilePhoto: String? = "",

    private val bio: String? = "",

    private val birthDate: LocalDate,

    private val userStatus: Status,

    private val lastSeen: Timestamp
) {


}


