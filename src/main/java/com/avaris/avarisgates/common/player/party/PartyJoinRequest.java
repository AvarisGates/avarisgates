package com.avaris.avarisgates.common.player.party;

import java.util.UUID;

public record PartyJoinRequest(UUID requester, UUID requestee, int expireTime){
}
