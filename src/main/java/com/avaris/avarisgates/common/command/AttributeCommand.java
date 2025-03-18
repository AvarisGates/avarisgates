package com.avaris.avarisgates.common.command;

import com.avaris.avarisgates.common.player.attribute.Attribute;
import com.avaris.avarisgates.common.player.attribute.AttributeType;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import static com.mojang.brigadier.arguments.IntegerArgumentType.integer;
import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class AttributeCommand {
    public static void init(){
        //I couldn't make the command be 'attribute' because minecraft already uses it. It would work, but it didn't feel right.
        //We can change it no problem.
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> dispatcher.register(
                literal("attributes")
                        .requires(x -> x.hasPermissionLevel(2))
                        .then(literal("get").executes(AttributeCommand::getAttributes)
                                .then(argument("attribute", new AttributeArgumentType())
                                        .executes(AttributeCommand::getAttribute)))
                        .then(literal("set").then(argument("attribute", new AttributeArgumentType())
                                .then(argument("value", integer(0))
                                        .executes(AttributeCommand::setAttribute))))
                        .then(literal("reset").executes(AttributeCommand::resetAttributes)
                                .then(argument("attribute", new AttributeArgumentType())
                                        .executes(AttributeCommand::resetAttribute)))
        ));
    }

    private static int resetAttribute(CommandContext<ServerCommandSource> context) {
        AttributeType type = context.getArgument("attribute", AttributeType.class);
        ServerPlayerEntity player = context.getSource().getPlayer();
        if (player == null) {
            return 1;
        }
        Attribute attribute = Attribute.resetAttribute(player, type);
        player.sendMessage(Text.literal("Resetting " + type + "..."));
        player.sendMessage(Text.literal(type + ": " + attribute.getValue()));
        return 0;
    }

    private static int resetAttributes(CommandContext<ServerCommandSource> context) {
        ServerPlayerEntity player = context.getSource().getPlayer();
        if (player == null) {
            return 1;
        }
        player.sendMessage(Text.literal("Resetting attributes..."));
        for (AttributeType type : AttributeType.values()) {
            Attribute attribute = Attribute.resetAttribute(player, type);
            player.sendMessage(Text.literal(type + ": " + attribute.getValue()));
        }
        return 0;
    }

    private static int getAttribute(CommandContext<ServerCommandSource> context) {
        AttributeType type = context.getArgument("attribute", AttributeType.class);
        ServerPlayerEntity player = context.getSource().getPlayer();
        if (player == null) {
            return 1;
        }
        Attribute attribute = Attribute.getAttributeWithEffects(player, type);
        player.sendMessage(Text.literal(attribute.getType() + ": " + attribute.getValue()));
        return 0;
    }

    private static int getAttributes(CommandContext<ServerCommandSource> context) {
        ServerPlayerEntity player = context.getSource().getPlayer();
        if (player == null) {
            return 1;
        }
        for (AttributeType type : AttributeType.values()) {
            Attribute attribute = Attribute.getAttributeWithEffects(player, type);
            player.sendMessage(Text.literal(attribute.getType() + ": " + attribute.getValue()));
        }
        return 0;
    }

    private static int setAttribute(CommandContext<ServerCommandSource> context) {
        AttributeType type = context.getArgument("attribute", AttributeType.class);
        long value = context.getArgument("value", Integer.class);
        ServerPlayerEntity player = context.getSource().getPlayer();
        if (player == null) {
            return 1;
        }
        Attribute attribute = new Attribute(type, value);
        Attribute.setAttribute(player, attribute);
        player.sendMessage(Text.literal(attribute.getType() + " set: " + attribute.getValue()));
        return 0;
    }
}
