package com.avaris.avarisgates.core.command;

import com.avaris.avarisgates.AvarisGates;
import com.avaris.avarisgates.core.network.ChangeAbilityS2C;
import com.avaris.avarisgates.core.player.ability.AbilitySlot;
import com.avaris.avarisgates.core.player.ability.AttachedAbility;
import com.avaris.avarisgates.core.player.ability.PlayerClassAbilityType;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import static com.mojang.brigadier.arguments.IntegerArgumentType.integer;
import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class AbilityCommand {
    public static void init(){
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> dispatcher.register(
                literal("ability")
                        .requires(source -> source.hasPermissionLevel(2))
                        .then(literal("get")
                                .executes(context -> {
                                    if (context.getSource().getPlayer() instanceof ServerPlayerEntity player) {
                                        PlayerClassAbilityType type0 = AttachedAbility.getAttached(player, AbilitySlot.SLOT0).getType();
                                        PlayerClassAbilityType type1 = AttachedAbility.getAttached(player, AbilitySlot.SLOT1).getType();
                                        PlayerClassAbilityType type2 = AttachedAbility.getAttached(player, AbilitySlot.SLOT2).getType();
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
                                                        int flagerino = 0;
                                                        PlayerClassAbilityType type_0 = AttachedAbility.getAttached(player, AbilitySlot.SLOT0).getType();
                                                        PlayerClassAbilityType type_1 = AttachedAbility.getAttached(player, AbilitySlot.SLOT1).getType();
                                                        PlayerClassAbilityType type_2 = AttachedAbility.getAttached(player, AbilitySlot.SLOT2).getType();
                                                        boolean first_check = type.toString().equals(type_0.toString());
                                                        boolean second_check = type.toString().equals(type_1.toString());
                                                        boolean third_check = type.toString().equals(type_2.toString());
                                                        if(slot_int == 1){
                                                            if(first_check){
                                                                player.sendMessage(Text.literal("You already have this ability equipped in this slot."));
                                                                flagerino = 1;
                                                            }
                                                            else{
                                                                if(second_check){
                                                                    type2 = AttachedAbility.getAttached(player,AbilitySlot.SLOT1).getType();
                                                                    AttachedAbility.setAttached(player,new AttachedAbility(type,0L,AbilitySlot.SLOT0));
                                                                    AttachedAbility.setAttached(player,new AttachedAbility(type2,0L,AbilitySlot.SLOT1));
                                                                    ServerPlayNetworking.send(player,new ChangeAbilityS2C(1,type2));
                                                                }
                                                                else if(third_check){
                                                                    type2 = AttachedAbility.getAttached(player,AbilitySlot.SLOT2).getType();
                                                                    AttachedAbility.setAttached(player,new AttachedAbility(type,0L,AbilitySlot.SLOT0));
                                                                    AttachedAbility.setAttached(player,new AttachedAbility(type2,0L,AbilitySlot.SLOT2));
                                                                    ServerPlayNetworking.send(player,new ChangeAbilityS2C(2,type2));
                                                                }
                                                                else{
                                                                    AttachedAbility.setAttached(player,new AttachedAbility(type,0L,AbilitySlot.SLOT0));
                                                                }
                                                            }
                                                        }
                                                        if(slot_int == 2){
                                                            if(second_check){
                                                                player.sendMessage(Text.literal("You already have this ability equipped in this slot."));
                                                                flagerino = 1;
                                                            }
                                                            else{
                                                                if(first_check){
                                                                    type2 = AttachedAbility.getAttached(player,AbilitySlot.SLOT0).getType();
                                                                    AttachedAbility.setAttached(player,new AttachedAbility(type,0L,AbilitySlot.SLOT1));
                                                                    AttachedAbility.setAttached(player,new AttachedAbility(type2,0L,AbilitySlot.SLOT0));
                                                                    ServerPlayNetworking.send(player,new ChangeAbilityS2C(0,type2));
                                                                }
                                                                else if(third_check){
                                                                    type2 = AttachedAbility.getAttached(player,AbilitySlot.SLOT2).getType();
                                                                    AttachedAbility.setAttached(player,new AttachedAbility(type,0L,AbilitySlot.SLOT1));
                                                                    AttachedAbility.setAttached(player,new AttachedAbility(type2,0L,AbilitySlot.SLOT2));
                                                                    ServerPlayNetworking.send(player,new ChangeAbilityS2C(2,type2));
                                                                }
                                                                else{
                                                                    AttachedAbility.setAttached(player,new AttachedAbility(type,0L,AbilitySlot.SLOT1));
                                                                }
                                                            }
                                                        }
                                                        if(slot_int == 3){
                                                            if(third_check){
                                                                player.sendMessage(Text.literal("You already have this ability equipped in this slot."));
                                                                flagerino = 1;
                                                            }
                                                            else{
                                                                if(first_check){
                                                                    type2 = AttachedAbility.getAttached(player,AbilitySlot.SLOT0).getType();
                                                                    AttachedAbility.setAttached(player,new AttachedAbility(type,0L,AbilitySlot.SLOT2));
                                                                    AttachedAbility.setAttached(player,new AttachedAbility(type2,0L,AbilitySlot.SLOT0));
                                                                    ServerPlayNetworking.send(player,new ChangeAbilityS2C(0,type2));
                                                                }
                                                                else if(second_check){
                                                                    type2 = AttachedAbility.getAttached(player,AbilitySlot.SLOT1).getType();
                                                                    AttachedAbility.setAttached(player,new AttachedAbility(type,0L,AbilitySlot.SLOT2));
                                                                    AttachedAbility.setAttached(player,new AttachedAbility(type2,0L,AbilitySlot.SLOT1));
                                                                    ServerPlayNetworking.send(player,new ChangeAbilityS2C(1,type2));
                                                                }
                                                                else{
                                                                    AttachedAbility.setAttached(player,new AttachedAbility(type,0L,AbilitySlot.SLOT2));
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
