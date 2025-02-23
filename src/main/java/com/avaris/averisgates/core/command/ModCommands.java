package com.avaris.averisgates.core.command;

import com.avaris.averisgates.core.player.ability.PlayerClassAbility;
import com.avaris.averisgates.core.player.ability.PlayerClassAbilityType;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import static com.mojang.brigadier.arguments.IntegerArgumentType.integer;
import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class ModCommands {
    public static void init(){
    CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> dispatcher.register(
            literal("ability")
                    .requires(source -> source.hasPermissionLevel(2))
                    .then(literal("get")
                        .executes(context -> {
                            if (context.getSource().getPlayer() instanceof ServerPlayerEntity player) {
                                PlayerClassAbilityType type0 = player.getAttached(PlayerClassAbility.PLAYER_CLASS_ABILITY_TYPE_ATTACHMENT_0);
                                PlayerClassAbilityType type1 = player.getAttached(PlayerClassAbility.PLAYER_CLASS_ABILITY_TYPE_ATTACHMENT_1);
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
                            .then(argument("type",integer(0))
                            .executes(context -> {
                                if (context.getSource().getPlayer() instanceof ServerPlayerEntity player) {
                                    int slot_int = context.getArgument("slot",Integer.class);
                                    int type_int = context.getArgument("type",Integer.class);
                                    PlayerClassAbilityType type = PlayerClassAbilityType.fromInt(type_int);
                                    AttachmentType<PlayerClassAbilityType> slot;
                                    if(slot_int == 1){
                                        slot = PlayerClassAbility.PLAYER_CLASS_ABILITY_TYPE_ATTACHMENT_0;
                                        player.setAttached(slot,type);
                                    }
                                    if(slot_int == 2){
                                        slot = PlayerClassAbility.PLAYER_CLASS_ABILITY_TYPE_ATTACHMENT_1;
                                        player.setAttached(slot,type);
                                    }
                                    if(slot_int == 3){
                                        slot = PlayerClassAbility.PLAYER_CLASS_ABILITY_TYPE_ATTACHMENT_2;
                                        player.setAttached(slot,type);
                                    }
                                    player.sendMessage(Text.literal("Set Ability Slot ").append(String.valueOf(slot_int)).append(" to ").append(Text.literal(type.toString()).formatted(Formatting.GOLD)));
                                }
                                return 0;
                            }))
                            ))
                    ));
    }
}
