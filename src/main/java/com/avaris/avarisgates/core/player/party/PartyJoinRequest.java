package com.avaris.avarisgates.core.player.party;

import java.util.UUID;

public record PartyJoinRequest(UUID requester, UUID requestee, int expireTime){
}
