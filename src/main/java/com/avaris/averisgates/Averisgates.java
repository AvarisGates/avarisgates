package com.avaris.averisgates;

import com.avaris.averisgates.core.PlayerClassAbility;
import com.avaris.averisgates.core.PlayerClassAbilityType;
import com.avaris.averisgates.core.TeleportAbility;
import com.avaris.averisgates.core.network.CastPlayerClassAbilityC2S;
import com.avaris.averisgates.core.network.ModPackets;
import com.avaris.averisgates.mixin.ClampedEntityAttributeAccessor;
import com.llamalad7.mixinextras.utils.MixinExtrasLogger;
import net.fabricmc.api.ModInitializer;
import com.google.common.collect.ImmutableMap;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.registry.Registries;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Averisgates implements ModInitializer {

    public static String MOD_ID = "averisgates";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

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

    private static final TeleportAbility testAbility = new TeleportAbility();

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
            LOGGER.info("Server got ability packet: '{}', from player '{}'",packet.ability().name(),context.player().getNameForScoreboard());
            PlayerClassAbility ability = getAttachedAbility(context.player(), packet.ability());
            if(ability == null){
                return;
            }
            if(ability.getCooldown(context.server().getTicks()) <= 0){
                ability.trigger(context.server(),context.player());
            }
        });
    }

    private PlayerClassAbility getAttachedAbility(ServerPlayerEntity player, PlayerClassAbilityType type) {
        PlayerClassAbilityType newType = player.getAttached(PlayerClassAbility.PLAYER_CLASS_ABILITY_TYPE_ATTACHMENT_0);
        if(newType == type){
            //Next trigger time - when the ability can be used next time (in ticks)
            Long ntt = player.getAttached(PlayerClassAbility.PLAYER_CLASS_ABILITY_NTT_ATTACHMENT_0);
            return PlayerClassAbility.build(newType,ntt,PlayerClassAbility.PLAYER_CLASS_ABILITY_NTT_ATTACHMENT_0);
        }
        return null;
    }

}

