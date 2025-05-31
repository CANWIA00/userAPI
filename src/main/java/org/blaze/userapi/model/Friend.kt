package org.blaze.userapi.model

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import jakarta.persistence.*
import java.io.Serializable
import java.time.LocalDateTime
import java.util.*

@Entity
@Table(name = "friend")
data class Friend(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: UUID? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id")
    @JsonIgnore
    val sender: Profile? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id")
    @JsonIgnore
    val receiver: Profile? = null,

    @Enumerated(EnumType.STRING)
    var status: F_status = F_status.PENDING,

    val createdAt: LocalDateTime = LocalDateTime.now()
) : Serializable{

    constructor() : this(
        id = null,
        sender = null,
        receiver = null,
        status = F_status.PENDING,
        createdAt = LocalDateTime.now()
    )
}





