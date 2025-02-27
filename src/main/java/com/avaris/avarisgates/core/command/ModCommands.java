package com.avaris.avarisgates.core.command;

import com.avaris.avarisgates.AvarisGates;
import net.fabricmc.fabric.api.command.v2.ArgumentTypeRegistry;
import net.minecraft.command.argument.serialize.ConstantArgumentSerializer;

public class ModCommands {
    public static void init(){
        ArgumentTypeRegistry.registerArgumentType(AvarisGates.id("ability_type_argument"),
                AbilityArgumentType.class, ConstantArgumentSerializer.of(AbilityArgumentType::create));

        ArgumentTypeRegistry.registerArgumentType(AvarisGates.id("attribute_type_argument"),
                AttributeArgumentType.class, ConstantArgumentSerializer.of(AttributeArgumentType::create));

        AttributeCommand.init();
        AbilityCommand.init();
        DungeonCommand.init();
    }
}
