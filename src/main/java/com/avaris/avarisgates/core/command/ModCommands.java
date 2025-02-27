package com.avaris.avarisgates.core.command;

import com.avaris.avarisgates.AvarisGates;
import com.avaris.avarisgates.core.network.ChangeAbilityS2C;
import com.avaris.avarisgates.core.player.ability.AbilitySlot;
import com.avaris.avarisgates.core.player.ability.AttachedAbility;
import com.avaris.avarisgates.core.player.ability.PlayerClassAbility;
import com.avaris.avarisgates.core.player.ability.PlayerClassAbilityType;
import com.avaris.avarisgates.core.player.attribute.Attribute;
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

        ArgumentTypeRegistry.registerArgumentType(AvarisGates.id("attribute_type_argument"),
                AttributeArgumentType.class, ConstantArgumentSerializer.of(AttributeArgumentType::create));

        AttributeCommand.init();
        AbilityCommand.init();
    }
}
