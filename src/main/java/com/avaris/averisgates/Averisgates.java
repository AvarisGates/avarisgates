package com.avaris.averisgates;

import com.avaris.averisgates.core.network.CastPlayerClassAbilityC2S;
import com.avaris.averisgates.core.network.ModPackets;
import com.avaris.averisgates.mixin.ClampedEntityAttributeAccessor;
import net.fabricmc.api.ModInitializer;
import com.google.common.collect.ImmutableMap;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.particle.TrailParticleEffect;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;


public class Averisgates implements ModInitializer {

    public static String MOD_ID = "averisgates";

    public static Identifier id(String id){
        return Identifier.of(MOD_ID,id);
    }

    private static final Double AttrLimit = 1000000000D; //Very important limit (VIL)

    private static final ImmutableMap<Identifier,Double> NEW_DEFAULT_ATTRIBUTES = ImmutableMap.of(
            Identifier.ofVanilla("generic.max_health"), AttrLimit,
            Identifier.ofVanilla("generic.armor"), AttrLimit,
            Identifier.ofVanilla("generic.armor_toughness"), AttrLimit,
            Identifier.ofVanilla("generic.attack_damage"), AttrLimit,
            Identifier.ofVanilla("generic.attack_knockback"),AttrLimit
    );

    @Override
    public void onInitialize() {
        for (Identifier id : Registries.ATTRIBUTE.getIds()) {
            Double new_def_value = NEW_DEFAULT_ATTRIBUTES.get(id);
            if(new_def_value == null){
                continue;
            }
            ClampedEntityAttributeAccessor attr = (ClampedEntityAttributeAccessor)Registries.ATTRIBUTE.get(id);
            attr.attributefix$setMaxValue(new_def_value);
        }

        ModPackets.init();

        ServerPlayNetworking.registerGlobalReceiver(CastPlayerClassAbilityC2S.ID,(packet, context)->{
            context.player().sendMessage(Text.literal("Server got ability packet: ").append(Text.of(packet.ability().name())));
            context.player().addCritParticles(context.player());
        });
    }

}

