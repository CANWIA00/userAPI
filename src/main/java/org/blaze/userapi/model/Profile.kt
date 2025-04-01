package org.blaze.userapi.model

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*
import java.sql.Timestamp
import java.time.Instant
import java.time.LocalDate
import java.util.*

@Entity
data class Profile(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
     val id: UUID? = null,

    @OneToOne(cascade = [(CascadeType.ALL)],fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
     var user: User,

     val fullName: String,

     val profilePhoto: String? = "",

     val bio: String? = "",

     val birthDate: LocalDate,

     val userStatus: Status, //TODO Online/Offline/default (websocket)

     val lastSeen: Instant,  //TODO  when he/she were online (websocket)

    @OneToMany(mappedBy = "sender", fetch = FetchType.LAZY)
    @JsonIgnore
    val sender: List<Friend> = mutableListOf(),

    @OneToMany(mappedBy = "receiver", fetch = FetchType.LAZY)
    @JsonIgnore
    val receiver: List<Friend> = mutableListOf(),
) {

    constructor() : this(
        id = null,
        profilePhoto = null,
        user = User(),
        bio = null,
        fullName = String(),
        lastSeen = Instant.now(),
        birthDate = LocalDate.now(),
        userStatus = Status.AVAILABLE,
        sender = mutableListOf(),
        receiver = mutableListOf()
    )

    override fun toString(): String {
        return "Profile(id=$id, user=$user, fullName='$fullName', profilePhoto=$profilePhoto, bio=$bio, birthDate=$birthDate, userStatus=$userStatus, lastSeen=$lastSeen, sender=$sender, receiver=$receiver)"
    }

}


