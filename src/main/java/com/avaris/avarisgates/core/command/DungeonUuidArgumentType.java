package com.avaris.avarisgates.core.command;

import com.avaris.avarisgates.AvarisGates;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.text.Text;

import java.util.Collection;
import java.util.Locale;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DungeonUuidArgumentType implements ArgumentType<UUID> {
    public static final SimpleCommandExceptionType INVALID_UUID = new SimpleCommandExceptionType(Text.translatable("argument.uuid.invalid"));
    private static final Pattern VALID_CHARACTERS = Pattern.compile("^([-A-Fa-f0-9]+)");
    public static DungeonUuidArgumentType create() {
        return new DungeonUuidArgumentType();
    }

    @Override
    public UUID parse(StringReader stringReader) throws CommandSyntaxException {
        String string = stringReader.getRemaining();
        Matcher matcher = VALID_CHARACTERS.matcher(string);
        if (matcher.find()) {
            String string2 = matcher.group(1);

            try {
                UUID uUID = UUID.fromString(string2);
                stringReader.setCursor(stringReader.getCursor() + string2.length());
                return uUID;
            } catch (IllegalArgumentException var6) {
            }
        }

        throw INVALID_UUID.createWithContext(stringReader);
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        for(UUID uuid : AvarisGates.dungeonManager.dungeonUuids()){
            if (uuid.toString().toLowerCase(Locale.ROOT).startsWith(builder.getRemainingLowerCase())) {
                builder.suggest(uuid.toString());
            }
        }
        return builder.buildFuture();
    }

    @Override
    public Collection<String> getExamples() {
        return AvarisGates.dungeonManager.dungeonUuids().stream().map(UUID::toString).toList();
    }
}
