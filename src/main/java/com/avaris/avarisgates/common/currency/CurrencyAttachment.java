package com.avaris.avarisgates.common.currency;

import com.avaris.avarisgates.AvarisGates;
import com.mojang.serialization.Codec;
import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentSyncPredicate;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.codec.PacketCodecs;

public class CurrencyAttachment {
    private long basicCurrency = 0;

    public static void init(){

    }

    public CurrencyAttachment(long basicCurrency){
        this.basicCurrency = basicCurrency;
    }

    public static CurrencyAttachment getCurrency(PlayerEntity player){
        Long basicCurrency = player.getAttachedOrCreate(BASIC_CURRENCY_ATTACHMENT);
        if(basicCurrency == null){
            basicCurrency = 0L;
        }
        return new CurrencyAttachment(basicCurrency);
    }

    public static void setCurrency(PlayerEntity player,CurrencyAttachment currencyAttachment){
        player.setAttached(BASIC_CURRENCY_ATTACHMENT,Math.max(0,currencyAttachment.basicCurrency));
    }

    public static final AttachmentType<Long> BASIC_CURRENCY_ATTACHMENT = AttachmentRegistry.create(
            AvarisGates.id("basic_currency"),
            builder -> builder
                    .initializer(() -> 0L)
                    .persistent(Codec.LONG)
                    .copyOnDeath()
                    .syncWith(PacketCodecs.LONG, AttachmentSyncPredicate.targetOnly())
    );

    public long getBasicCurrency() {
        return basicCurrency;
    }
}
