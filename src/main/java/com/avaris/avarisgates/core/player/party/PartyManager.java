package com.avaris.avarisgates.core.player.party;

import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.net.URI;
import java.util.*;

public class PartyManager {
    private static final HashMap<UUID,PlayerParty> playerParties = new HashMap<>();
    private static final HashMap<UUID,UUID> playerToParty = new HashMap<>();

    // Leader UUID
    private static final HashMap<UUID, PartyInvite> invites = new HashMap<>();
    // Leader UUID
    private static final HashMap<UUID,PartyJoinRequest> joinRequests = new HashMap<>();

    private static final int EXPIRE_TIME = 6_000; // In ticks. 6000 ticks = 5 minutes.

    public static void requestToJoinParty(ServerPlayerEntity requester, ServerPlayerEntity requestee) {
        if(getPlayerParty(requestee.getUuid()) != null){
            if(!isPartyLeader(requestee.getUuid())){
                return;
            }
        }

        String acceptCommand = "/party accept request " + requester.getNameForScoreboard();
        String rejectCommand = "TODO!!!";

        requestee.sendMessage(Text.literal(requester.getNameForScoreboard() + " requests to join your party."));
        requestee.sendMessage(Text.literal("[ACCEPT]").formatted(Formatting.GREEN).styled((style) -> style.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Text.literal(acceptCommand))).withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,acceptCommand))));
        requestee.sendMessage(Text.literal("[REJECT]").formatted(Formatting.RED).styled((style) -> style.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Text.literal(rejectCommand))).withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,rejectCommand))));

        joinRequests.put(requestee.getUuid(),new PartyJoinRequest(requester.getUuid(),requestee.getUuid(),requestee.getServer().getTicks() + EXPIRE_TIME));
    }

    public static void acceptJoinRequest(ServerPlayerEntity requester, ServerPlayerEntity requestee) {
        PartyJoinRequest request = joinRequests.get(requestee.getUuid());
        if(request == null){
            requestee.sendMessage(Text.literal("That player did not request to join your party."));
            return;
        }
        joinRequests.remove(requestee.getUuid());
        boolean expired = request.expireTime() < requestee.getServer().getTicks();
        if(expired){
            requestee.sendMessage(Text.literal("That player did not request to join your party."));
            return;
        }
        UUID partyUuid = getOrCreateParty(requestee.getUuid());
        boolean added = addPlayerToParty(requester, partyUuid);

        requester.sendMessage(Text.literal("You joined " +requestee.getNameForScoreboard() + "'s party."));
        PlayerManager manager = requester.server.getPlayerManager();
        for(UUID player_uuid : getPartyInstance(partyUuid).getPlayers()){
            if(requester.getUuid().equals(player_uuid)){
                continue;
            }
            manager.getPlayer(player_uuid).sendMessage(Text.of(requester.getNameForScoreboard()+" joined your party!"));
        }
    }


    public static boolean invitePlayer(ServerPlayerEntity inviter, ServerPlayerEntity invitee) {
        if(getPlayerParty(inviter.getUuid()) != null){
            if(!isPartyLeader(inviter.getUuid())){
                return false;
            }
        }
        String inviteCommand = "/party accept invite "+inviter.getNameForScoreboard();
        String rejectCommand = "TODO!!!";
        inviter.sendMessage(Text.literal("Party invite sent to "+invitee.getNameForScoreboard()));

        invitee.sendMessage(Text.literal(inviter.getNameForScoreboard() + " invited you to join their party."));
        invitee.sendMessage(Text.literal("[ACCEPT]").formatted(Formatting.GREEN).styled((style) -> style.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Text.literal(inviteCommand))).withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,inviteCommand))));
        invitee.sendMessage(Text.literal("[REJECT]").formatted(Formatting.RED).styled((style) -> style.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Text.literal(rejectCommand))).withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,rejectCommand))));
        invites.put(inviter.getUuid(),new PartyInvite(inviter.getUuid(),invitee.getUuid(),invitee.getServer().getTicks() + EXPIRE_TIME));
        return true;
    }

    private static boolean addPlayerToParty(ServerPlayerEntity player, UUID partyUuid) {
        UUID playerUuid = player.getUuid();
        leaveCurrentParty(player);
        PlayerParty party = getPartyInstance(partyUuid);
        if(party == null){
            return false;
        }
        party.addPlayer(playerUuid);
        playerToParty.put(playerUuid,partyUuid);
        return true;
    }

    private static UUID getOrCreateParty(UUID leader) {
        UUID partyUuid = getPlayerParty(leader);
        if(partyUuid != null){
            return partyUuid;
        }
        return createParty(leader);
    }

    private static UUID createParty(UUID leader) {
        UUID partyUuid = UUID.randomUUID();
        playerParties.put(partyUuid,new PlayerParty(leader));
        playerToParty.put(leader,partyUuid);
        return partyUuid;
    }

    // returns:
    // 0 - success
    // 1 - not leader
    // 2 - player not in party
    public static int tryRemoveFromParty(ServerPlayerEntity kicker, ServerPlayerEntity kickee) {
        if(!isPartyLeader(kicker.getUuid())){
            return 1;
        }
        if(!areInTheSameParty(kicker, kickee)){
            return 2;
        }
        leaveCurrentParty(kickee);
        return 0;
    }

    private static boolean isPartyLeader(UUID playerUuid) {
        return getPartyLeaderUuid(playerUuid) == playerUuid;
    }

    // Check if two players are in the same party
    public static boolean areInTheSameParty(ServerPlayerEntity player,ServerPlayerEntity player1) {
        if(player == null||player1 == null){
            return false;
        }
        return areInTheSameParty(player.getUuid(),player1.getUuid());
    }
    // Check if two players are in the same party
    public static boolean areInTheSameParty(UUID playerUuid,UUID player1Uuid){
        UUID playerPartyUuid = getPlayerParty(playerUuid);
        UUID playerParty1Uuid = getPlayerParty(player1Uuid);
        if(playerPartyUuid == null || playerParty1Uuid == null){
            return false;
        }
        return Objects.equals(playerPartyUuid, playerParty1Uuid);
    }

    public static ServerPlayerEntity getPartyLeader(ServerPlayerEntity player) {
        PlayerParty party = getPartyInstance(getPlayerParty(player.getUuid()));
        if(party == null){
            return null;
        }
        if(player.getServer() == null){
            return null;
        }
        return player.getServer().getPlayerManager().getPlayer(party.getLeader());
    }

    public static UUID getPartyLeaderUuid(UUID player) {
        PlayerParty party = getPartyInstance(getPlayerParty(player));
        if(party == null){
            return null;
        }
        return party.getLeader();
    }

    public static boolean leaveCurrentParty(ServerPlayerEntity player) {
        UUID playerUuid = player.getUuid();

        UUID partyUuid = getPlayerParty(playerUuid);
        if(partyUuid == null){
            return false;
        }
        PlayerManager manager = player.getServer().getPlayerManager();

        PlayerParty party = getPartyInstance(partyUuid);
        ServerPlayerEntity leader = getPartyLeader(player);
        if(Objects.equals(leader, player) ||party.getPlayers().size() == 1){
            disbandParty(manager,partyUuid);
        }else{
            party.removePlayer(playerUuid);
            playerToParty.remove(playerUuid);
        }

        if(!player.isDisconnected()){
            player.sendMessage(Text.literal("You left " + leader.getNameForScoreboard() + "'s party."));
        }
        for(UUID player_uuid : party.getPlayers()){
            if(playerUuid.equals(player_uuid)){
                continue;
            }
            manager.getPlayer(player_uuid).sendMessage(Text.of(player.getNameForScoreboard()+" left your party!"));
        }

        return true;
    }

    private static void disbandParty(PlayerManager manager, UUID partyUuid) {
        for(UUID playerUuid: getPartyInstance(partyUuid).getPlayers()){
            manager.getPlayer(playerUuid).sendMessage(Text.of("Your party has been disbanded."));
            playerToParty.remove(playerUuid);
        }
        playerParties.remove(partyUuid);
    }

    public static PlayerParty getPartyInstance(UUID partyUuid) {
        return playerParties.get(partyUuid);
    }

    public static UUID getPlayerParty(UUID player) {
        return playerToParty.get(player);
    }

    public static void onPlayerDisconnect(ServerPlayerEntity player){
        leaveCurrentParty(player);
    }

    public static void acceptInvite(ServerPlayerEntity inviter, ServerPlayerEntity invitee) {
        PartyInvite invite = invites.get(inviter.getUuid());
        if(invite == null){
            invitee.sendMessage(Text.literal("That player did not invite you to their party."));
            return;
        }
        invites.remove(inviter.getUuid());
        boolean expired = invite.expireTime() < inviter.getServer().getTicks();
        if(expired){
            invitee.sendMessage(Text.literal("That player did not invite you to their party."));
            return;
        }
        UUID partyUuid = getOrCreateParty(inviter.getUuid());
        boolean added = addPlayerToParty(invitee, partyUuid);

        invitee.sendMessage(Text.literal("You joined " +inviter.getNameForScoreboard() + "'s party."));
        PlayerManager manager = inviter.server.getPlayerManager();
        for(UUID player_uuid : getPartyInstance(partyUuid).getPlayers()){
            if(invitee.getUuid().equals(player_uuid)){
                continue;
            }
            manager.getPlayer(player_uuid).sendMessage(Text.of(invitee.getNameForScoreboard()+" joined your party!"));
        }
    }
}
