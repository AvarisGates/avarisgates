package com.avaris.avarisgates.common.command;

import com.avaris.avarisgates.common.currency.CurrencyAttachment;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import static com.mojang.brigadier.arguments.IntegerArgumentType.integer;
import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class CurrencyCommand {
    public static void init(){
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> dispatcher.register(
                literal("currency").executes(CurrencyCommand::getCurrency)
                .requires(serverCommandSource -> serverCommandSource.hasPermissionLevel(2))
                .then(literal("add").then(argument("value",integer()).executes(CurrencyCommand::addCurrency)))
                .then(literal("set").then(argument("value",integer()).executes(CurrencyCommand::setCurrency)))
        ));
    }

    private static int setCurrency(CommandContext<ServerCommandSource> context) {
        ServerPlayerEntity player = context.getSource().getPlayer();
        if(player == null){
            return 1;
        }
        long value = context.getArgument("value",Integer.class);
        CurrencyAttachment.setCurrency(player,new CurrencyAttachment(value));
        player.sendMessage(Text.of("You have: " + value + "$"));
        return 0;
    }

    private static int addCurrency(CommandContext<ServerCommandSource> context) {
        ServerPlayerEntity player = context.getSource().getPlayer();
        if(player == null){
            return 1;
        }
        long value = context.getArgument("value",Integer.class);
        long basicCurrency = CurrencyAttachment.getCurrency(player).getBasicCurrency();
        CurrencyAttachment.setCurrency(player,new CurrencyAttachment(value + basicCurrency));
        player.sendMessage(Text.of("Added "+value+"; You have: "+(basicCurrency+value)+"$"));
        return 0;
    }

    private static int getCurrency(CommandContext<ServerCommandSource> context) {
        ServerPlayerEntity player = context.getSource().getPlayer();
        if(player == null){
            return 1;
        }
        long basicCurrency = CurrencyAttachment.getCurrency(player).getBasicCurrency();
        player.sendMessage(Text.of("You have: "+basicCurrency+"$"));

        return 0;
    }

}
