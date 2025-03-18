package com.avaris.avarisgates.common.player.party;

import java.util.UUID;

public record PartyInvite(UUID inviter,UUID invitee,int expireTime) {
}
