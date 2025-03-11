package com.avaris.avarisgates.core.player.party;

import net.minecraft.server.network.ServerPlayerEntity;

import java.util.Collection;
import java.util.HashSet;
import java.util.UUID;

public class PlayerParty {
    private UUID leader;
    private HashSet<UUID> players = new HashSet<>();

    public PlayerParty(UUID leader){
        this.leader = leader;
        this.addPlayer(leader);
    }

    public void addPlayer(UUID player) {
        this.players.add(player);
    }

    public void removePlayer(ServerPlayerEntity player) {
        this.removePlayer(player.getUuid());
    }

    public void removePlayer(UUID playerUuid) {
        players.remove(playerUuid);
    }

    public Collection<UUID> getPlayers() {
        return players;
    }

    public UUID getLeader() {
        return leader;
    }
}
