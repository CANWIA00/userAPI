package org.blaze.userapi.model

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*
import java.io.Serializable
import java.sql.Timestamp
import java.time.Instant
import java.time.LocalDate
import java.util.*

@Entity
data class Profile(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: UUID? = null,

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @JsonIgnore
    var user: User? = null,

    val fullName: String? = null,

    val profilePhoto: String? = "",

    val bio: String? = null,

    val birthDate: LocalDate = LocalDate.now(),

    val userStatus: Status = Status.AVAILABLE,

    val lastSeen: Instant = Instant.now(),

    @OneToMany(mappedBy = "sender", fetch = FetchType.LAZY)
    @JsonIgnore
    val sender: List<Friend> = mutableListOf(),

    @OneToMany(mappedBy = "receiver", fetch = FetchType.LAZY)
    @JsonIgnore
    val receiver: List<Friend> = mutableListOf()
): Serializable {
    constructor() : this(
        id = null,
        user = null,
        fullName = null,
        profilePhoto = null,
        bio = null,
        birthDate = LocalDate.now(),
        userStatus = Status.AVAILABLE,
        lastSeen = Instant.now(),
        sender = mutableListOf(),
        receiver = mutableListOf()
    )
}



