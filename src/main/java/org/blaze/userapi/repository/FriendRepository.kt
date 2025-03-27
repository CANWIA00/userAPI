package org.blaze.userapi.repository

import org.blaze.userapi.model.F_status
import org.blaze.userapi.model.Friend
import org.blaze.userapi.model.Profile
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface FriendRepository : JpaRepository<Friend, Long> {

    fun findBySenderAndReceiver(user:Profile,friend: Profile):Friend?
    fun findByReceiverAndStatus(friend:Profile,status: F_status):Friend?
}