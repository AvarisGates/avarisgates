package com.avaris.averisgates.core.command;

import com.avaris.averisgates.Averisgates;
import com.avaris.averisgates.core.player.ability.PlayerClassAbilityType;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.context.StringRange;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestion;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.fabricmc.fabric.api.command.v2.ArgumentTypeRegistry;
import net.minecraft.command.argument.UuidArgumentType;
import net.minecraft.command.argument.serialize.ConstantArgumentSerializer;

import java.util.Arrays;
import java.util.Collection;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;

public class AbilityArgumentType implements ArgumentType<PlayerClassAbilityType> {
    static {
    }

    public static AbilityArgumentType create() {
        return new AbilityArgumentType();
    }

    @Override
    public PlayerClassAbilityType parse(StringReader reader) throws CommandSyntaxException {
        String s = reader.readString().toLowerCase(Locale.ROOT);
        for(PlayerClassAbilityType type : PlayerClassAbilityType.values()){
            if(type.name().toLowerCase(Locale.ROOT).equals(s)){
                return type;
            }
        }
        return PlayerClassAbilityType.Swing;
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        for(PlayerClassAbilityType type : PlayerClassAbilityType.values()){
            if (type.name().toLowerCase(Locale.ROOT).startsWith(builder.getRemainingLowerCase())) {
                builder.suggest(type.name());
            }
        }
        return builder.buildFuture();
    }

    @Override
    public Collection<String> getExamples() {
        return ArgumentType.super.getExamples();
    }
}
