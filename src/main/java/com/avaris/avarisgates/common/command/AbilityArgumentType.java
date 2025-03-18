package com.avaris.avarisgates.common.command;

import com.avaris.avarisgates.common.player.ability.PlayerClassAbilityType;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;

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
