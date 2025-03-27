package org.blaze.userapi.model

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "friends")
data class Friend(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
     val id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id", nullable = false)
     val sender: Profile,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id", nullable = false)
     val receiver: Profile,

     val status:F_status,

     val createTime: LocalDateTime = LocalDateTime.now(),
) {

}
