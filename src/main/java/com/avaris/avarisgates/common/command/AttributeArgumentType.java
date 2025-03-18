package com.avaris.avarisgates.common.command;

import com.avaris.avarisgates.common.player.attribute.AttributeType;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;

import java.util.Collection;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;

public class AttributeArgumentType implements ArgumentType<AttributeType> {

    public static AttributeArgumentType create() {
        return new AttributeArgumentType();
    }

    @Override
    public AttributeType parse(StringReader reader) throws CommandSyntaxException {
        String s = reader.readString().toLowerCase(Locale.ROOT);
        for(AttributeType type : AttributeType.values()){
            if(type.name().toLowerCase(Locale.ROOT).equals(s)){
                return type;
            }
        }
        return AttributeType.Vitality;
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        for(AttributeType type : AttributeType.values()){
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
