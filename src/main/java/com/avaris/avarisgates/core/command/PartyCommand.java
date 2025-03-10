package com.avaris.avarisgates.core.command;

import com.avaris.avarisgates.core.player.party.PartyManager;
import com.avaris.avarisgates.core.player.party.PlayerParty;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.Objects;
import java.util.UUID;

import static net.minecraft.server.command.CommandManager.*;


public class PartyCommand {
    static void init(){
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> dispatcher.register(
            literal("party").executes(PartyCommand::sendPartyInfo)
                .then(literal("join")
                        .then(argument("player", EntityArgumentType.player())
                                .executes(PartyCommand::requestJoinParty)))
                .then(literal("leave")
                        .executes(PartyCommand::leaveParty))
                .then(literal("invite")
                        .then(argument("player", EntityArgumentType.player())
                                .executes(PartyCommand::invitePlayer)))
                .then(literal("kick")
                        .then(argument("player", EntityArgumentType.player())
                                .executes(PartyCommand::kickPlayer)))
                .then(literal("accept")
                        .then(literal("invite")
                                .then(argument("player", EntityArgumentType.player())
                                        .executes(PartyCommand::acceptInvite)))
                        .then(literal("request")
                                .then(argument("player", EntityArgumentType.player())
                                        .executes(PartyCommand::acceptJoinRequest)))
                )
        ));
    }

    private static int acceptInvite(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity invitee = context.getSource().getPlayer();

        if(!context.getSource().isExecutedByPlayer()||invitee == null) {
            context.getSource().sendError(Text.literal("Please execute as a player."));
            return 1;
        }

        ServerPlayerEntity inviter = EntityArgumentType.getPlayer(context,"player");

        if(inviter == null){
            context.getSource().sendError(Text.literal("Player not found."));
            return 0;
        }

        PartyManager.acceptInvite(inviter,invitee);
        return 0;
    }

    private static int acceptJoinRequest(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity requestee = context.getSource().getPlayer();

        if(!context.getSource().isExecutedByPlayer()||requestee == null) {
            context.getSource().sendError(Text.literal("Please execute as a player."));
            return 1;
        }

        ServerPlayerEntity requester = EntityArgumentType.getPlayer(context,"player");

        if(requester == null){
            context.getSource().sendError(Text.literal("Player not found."));
            return 0;
        }

        PartyManager.acceptJoinRequest(requester,requestee);
        return 0;
    }

    private static int invitePlayer(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
       ServerPlayerEntity inviter = context.getSource().getPlayer();

        if(!context.getSource().isExecutedByPlayer()||inviter == null) {
            context.getSource().sendError(Text.literal("Please execute as a player."));
            return 1;
        }

        ServerPlayerEntity invitee = EntityArgumentType.getPlayer(context,"player");

        if(invitee == null){
            context.getSource().sendError(Text.literal("Player not found."));
            return 0;
        }

        if(invitee.equals(inviter)){
            context.getSource().sendError(Text.literal("You cannot invite yourself."));
            return 1;
        }

        if(PartyManager.areInTheSameParty(inviter,invitee)){
            context.getSource().sendError(Text.literal(invitee.getNameForScoreboard() + " is already in your party."));
            return 0;
        }

        PartyManager.invitePlayer(inviter,invitee);
        return 0;
    }

    private static int leaveParty(CommandContext<ServerCommandSource> context) {
        ServerPlayerEntity player = context.getSource().getPlayer();

        if(!context.getSource().isExecutedByPlayer()||player == null) {
            context.getSource().sendError(Text.literal("Please execute as a player."));
            return 1;
        }

        if(PartyManager.getPlayerParty(player.getUuid()) == null){
            context.getSource().sendError(Text.literal("You are not in a party."));
            return 0;
        }

        ServerPlayerEntity leader = PartyManager.getPartyLeader(player);
        if(!PartyManager.leaveCurrentParty(player)){
            context.getSource().sendMessage(Text.literal("You can't leave this party."));
        }

        return 0;
    }

    private static int sendPartyInfo(CommandContext<ServerCommandSource> context) {
        ServerPlayerEntity player = context.getSource().getPlayer();
        if(!context.getSource().isExecutedByPlayer()||player == null) {
            context.getSource().sendError(Text.literal("Please execute as a player or pass more arguments."));
            return 1;
        }
        UUID playerUuid = player.getUuid();
        UUID partyUuid = PartyManager.getPlayerParty(playerUuid);
        if(partyUuid == null){
            context.getSource().sendMessage(Text.of("You are not in a party"));
            return 0;
        }
        PlayerParty party = PartyManager.getPartyInstance(partyUuid);
        PlayerManager playerManager = context.getSource().getServer().getPlayerManager();
        String leaderName = playerManager.getPlayer(party.getLeader()).getNameForScoreboard();
        context.getSource().sendMessage(Text.of(leaderName+"'s party:"));
        for(UUID player_uuid : party.getPlayers()){
            Formatting formatting = Formatting.GREEN;
            if(player_uuid.equals(party.getLeader())){
                formatting = Formatting.GOLD;
            }
            player.sendMessage(Text.of("   "+playerManager.getPlayer(player_uuid).getNameForScoreboard()).copy().formatted(formatting));
        }
        return 0;
    }

    private static int requestJoinParty(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        if(!context.getSource().isExecutedByPlayer()) {
            context.getSource().sendError(Text.literal("Please execute as a player."));
            return 1;
        }
        // The player requesting to join
        ServerPlayerEntity requestor = context.getSource().getPlayer();

        ServerPlayerEntity requestee = EntityArgumentType.getPlayer(context,"player");
        if(requestee == null){
            context.getSource().sendError(Text.literal("Player not found."));
            return 1;
        }
        if(requestee.equals(requestor)){
            context.getSource().sendError(Text.literal("You cannot join your own party."));
            return 1;
        }
        if(PartyManager.areInTheSameParty(requestor,requestee)){
            context.getSource().sendMessage(Text.literal("You are in the same party as "+requestee.getNameForScoreboard()+"."));
            return 1;
        }
        context.getSource().sendMessage(Text.literal("Requesting to join "+requestee.getNameForScoreboard() + "'s party."));
        PartyManager.requestToJoinParty(requestor,requestee);

        return 0;
    }
    private static int kickPlayer(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        if(!context.getSource().isExecutedByPlayer()) {
            context.getSource().sendError(Text.literal("Please execute as a player."));
            return 1;
        }
        // The player kicking
        ServerPlayerEntity kicker = context.getSource().getPlayer();

        // The player to be kicked
        ServerPlayerEntity kickee = EntityArgumentType.getPlayer(context,"player");
        if(kickee == null){
            context.getSource().sendError(Text.literal("Player not found."));
            return 1;
        }
        if(kickee.equals(kicker)){
            context.getSource().sendError(Text.literal("You cannot kick yourself."));
            return 1;
        }
        int result = PartyManager.tryRemoveFromParty(kicker,kickee);
        if(result == 0){
            PlayerParty party = PartyManager.getPartyInstance(PartyManager.getPlayerParty(kicker.getUuid()));
            for(UUID playerUuid : party.getPlayers()){
                kickee.getServer().getPlayerManager().getPlayer(playerUuid)
                        .sendMessage(Text.literal(kickee.getNameForScoreboard() + " has been kicked from your party."));
            }
        }else if(result == 1){
            context.getSource().sendMessage(Text.literal("Only the party leader can kick players."));
        }else if(result == 2){
            context.getSource().sendMessage(Text.literal(kickee.getNameForScoreboard() + " isn't in your party."));
        }
        return 0;
    }
}
