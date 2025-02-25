package com.avaris.avarisgates.core.command;

import com.avaris.avarisgates.AvarisGates;
import com.avaris.avarisgates.core.network.ChangeAbilityS2C;
import com.avaris.avarisgates.core.player.ability.PlayerClassAbility;
import com.avaris.avarisgates.core.player.ability.PlayerClassAbilityType;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.fabricmc.fabric.api.command.v2.ArgumentTypeRegistry;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.command.argument.serialize.ConstantArgumentSerializer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import static com.avaris.avarisgates.core.player.ability.PlayerClassAbility.*;
import static com.mojang.brigadier.arguments.IntegerArgumentType.integer;
import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class ModCommands {
    public static void init(){
        ArgumentTypeRegistry.registerArgumentType(AvarisGates.id("ability_type_argument"),
                AbilityArgumentType.class, ConstantArgumentSerializer.of(AbilityArgumentType::create));
    CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> dispatcher.register(
            literal("ability")
                    .requires(source -> source.hasPermissionLevel(2))
                    .then(literal("get")
                        .executes(context -> {
                            if (context.getSource().getPlayer() instanceof ServerPlayerEntity player) {
                                PlayerClassAbilityType type0 = player.getAttached(PlayerClassAbility.PLAYER_CLASS_ABILITY_TYPE_ATTACHMENT_0);
                                PlayerClassAbilityType type1 = player.getAttached(PLAYER_CLASS_ABILITY_TYPE_ATTACHMENT_1);
                                PlayerClassAbilityType type2 = player.getAttached(PlayerClassAbility.PLAYER_CLASS_ABILITY_TYPE_ATTACHMENT_2);
                                player.sendMessage(Text.literal("Ability 1 - ").append(Text.literal(type0.name()).formatted(Formatting.GOLD))
                                        .append("\nAbility 2 - ").append(Text.literal(type1.name()).formatted(Formatting.GOLD))
                                        .append("\nAbility 3 - ").append(Text.literal(type2.name()).formatted(Formatting.GOLD)));
                            }
                            return 0;
                        })
                    )
                    .then(literal("set")
                            .then(argument("slot", integer(1))
                            .then(argument("type",new AbilityArgumentType())
                            .executes(context -> {
                                if (context.getSource().getPlayer() instanceof ServerPlayerEntity player) {
                                    int slot_int = context.getArgument("slot",Integer.class);
                                    PlayerClassAbilityType type = context.getArgument("type",PlayerClassAbilityType.class);
                                    PlayerClassAbilityType type2;
                                    AttachmentType<PlayerClassAbilityType> slot;
                                    int flagerino = 0;
                                    if(slot_int == 1){
                                        if(type.toString().equals(player.getAttached(PLAYER_CLASS_ABILITY_TYPE_ATTACHMENT_0).toString())){
                                            player.sendMessage(Text.literal("You already have this ability equipped in this slot."));
                                            flagerino = 1;
                                        }
                                        else{
                                            if(type.toString().equals(player.getAttached(PLAYER_CLASS_ABILITY_TYPE_ATTACHMENT_1).toString())){
                                                type2 = player.getAttached(PLAYER_CLASS_ABILITY_TYPE_ATTACHMENT_0);
                                                player.setAttached(PLAYER_CLASS_ABILITY_TYPE_ATTACHMENT_0,type);
                                                player.setAttached(PLAYER_CLASS_ABILITY_TYPE_ATTACHMENT_1,type2);
                                                ServerPlayNetworking.send(player,new ChangeAbilityS2C(1,type2));
                                            }
                                            else if(type.toString().equals(player.getAttached(PLAYER_CLASS_ABILITY_TYPE_ATTACHMENT_2).toString())){
                                                type2 = player.getAttached(PLAYER_CLASS_ABILITY_TYPE_ATTACHMENT_0);
                                                player.setAttached(PLAYER_CLASS_ABILITY_TYPE_ATTACHMENT_0,type);
                                                player.setAttached(PLAYER_CLASS_ABILITY_TYPE_ATTACHMENT_2,type2);
                                                ServerPlayNetworking.send(player,new ChangeAbilityS2C(2,type2));
                                            }
                                            else{
                                                slot = PlayerClassAbility.PLAYER_CLASS_ABILITY_TYPE_ATTACHMENT_0;
                                                player.setAttached(slot,type);
                                            }
                                        }
                                    }
                                    if(slot_int == 2){
                                        if(type.toString().equals(player.getAttached(PLAYER_CLASS_ABILITY_TYPE_ATTACHMENT_1).toString())){
                                            player.sendMessage(Text.literal("You already have this ability equipped in this slot."));
                                            flagerino = 1;
                                        }
                                        else{
                                            if(type.toString().equals(player.getAttached(PLAYER_CLASS_ABILITY_TYPE_ATTACHMENT_0).toString())){
                                                type2 = player.getAttached(PLAYER_CLASS_ABILITY_TYPE_ATTACHMENT_1);
                                                player.setAttached(PLAYER_CLASS_ABILITY_TYPE_ATTACHMENT_1,type);
                                                player.setAttached(PLAYER_CLASS_ABILITY_TYPE_ATTACHMENT_0,type2);
                                                ServerPlayNetworking.send(player,new ChangeAbilityS2C(0,type2));
                                            }
                                            else if(type.toString().equals(player.getAttached(PLAYER_CLASS_ABILITY_TYPE_ATTACHMENT_2).toString())){
                                                type2 = player.getAttached(PLAYER_CLASS_ABILITY_TYPE_ATTACHMENT_1);
                                                player.setAttached(PLAYER_CLASS_ABILITY_TYPE_ATTACHMENT_1,type);
                                                player.setAttached(PLAYER_CLASS_ABILITY_TYPE_ATTACHMENT_2,type2);
                                                ServerPlayNetworking.send(player,new ChangeAbilityS2C(2,type2));
                                            }
                                            else{
                                                slot = PlayerClassAbility.PLAYER_CLASS_ABILITY_TYPE_ATTACHMENT_1;
                                                player.setAttached(slot,type);
                                            }
                                        }
                                    }
                                    if(slot_int == 3){
                                        if(type.toString().equals(player.getAttached(PLAYER_CLASS_ABILITY_TYPE_ATTACHMENT_2).toString())){
                                            player.sendMessage(Text.literal("You already have this ability equipped in this slot."));
                                            flagerino = 1;
                                        }
                                        else{
                                            if(type.toString().equals(player.getAttached(PLAYER_CLASS_ABILITY_TYPE_ATTACHMENT_0).toString())){
                                                type2 = player.getAttached(PLAYER_CLASS_ABILITY_TYPE_ATTACHMENT_2);
                                                player.setAttached(PLAYER_CLASS_ABILITY_TYPE_ATTACHMENT_2,type);
                                                player.setAttached(PLAYER_CLASS_ABILITY_TYPE_ATTACHMENT_0,type2);
                                                ServerPlayNetworking.send(player,new ChangeAbilityS2C(0,type2));
                                            }
                                            else if(type.toString().equals(player.getAttached(PLAYER_CLASS_ABILITY_TYPE_ATTACHMENT_2).toString())){
                                                type2 = player.getAttached(PLAYER_CLASS_ABILITY_TYPE_ATTACHMENT_2);
                                                player.setAttached(PLAYER_CLASS_ABILITY_TYPE_ATTACHMENT_2,type);
                                                player.setAttached(PLAYER_CLASS_ABILITY_TYPE_ATTACHMENT_1,type2);
                                                ServerPlayNetworking.send(player,new ChangeAbilityS2C(1,type2));
                                            }
                                            else{
                                                slot = PlayerClassAbility.PLAYER_CLASS_ABILITY_TYPE_ATTACHMENT_2;
                                                player.setAttached(slot,type);
                                            }
                                        }
                                    }
                                    if(flagerino == 1){
                                        AvarisGates.LOGGER.info("Some dude tried to put the same ability lul");
                                    }
                                    else{
                                        player.sendMessage(Text.literal("Set Ability Slot ").append(String.valueOf(slot_int)).append(" to ").append(Text.literal(type.toString()).formatted(Formatting.GOLD)));
                                        ServerPlayNetworking.send(player,new ChangeAbilityS2C(slot_int - 1,type));
                                    }
                                }
                                return 0;
                            }))
                            ))
                    ));
    }
}
