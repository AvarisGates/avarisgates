package com.avaris.avarisgates.common.player.ability;

import com.avaris.avarisgates.AvarisGates;
import com.avaris.avarisgates.common.player.ManaAttachment;
import com.avaris.avarisgates.common.player.player_class.PlayerClassType;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

/**
 * To add a new ability:<br>
 * 1. Create a new ability type in PlayerClassAbilityType<br>
 * 2. Register it in the AbilityRegistrar<br>
 * 3. Create a new ability class extending from this class, make sure to call super.trigger() at the start of the trigger function,<br>
 * if it returns false the ability shouldn't trigger, it's also necessary so the ability goes on cooldown properly<br>
 * 4. (If the ability doesn't require an entity go to step 6.)<br>
 * Create a new Entity Type, Entity Renderer, and register them, the type in common code, the renderer on the client only<br>
 * 5. Implement the new Entity Type, and the Renderer, the renderer can be mostly left blank. See CleaveEntityRenderer<br>
 * 6. I think that's it, add new steps in the future if more steps are required<br>
 */
public abstract class PlayerClassAbility{
    protected long minLevel;
    protected long nextTriggerTime; //In ticks

    protected AttachedAbility ability;

    public PlayerClassAbility(AttachedAbility ability) {
        this.nextTriggerTime = ability.getNtt();
        this.ability = ability;
    }

    public abstract PlayerClassAbilityType getAbilityType();

    public abstract PlayerClassType getClassType();

    public long getMinLevel(){
        return this.minLevel;
    }

    public abstract long getBaseCooldown();

    public long getBaseManaCost(){
        return 20;
    }

    public long getNextTriggerTime(){
        return nextTriggerTime;
    }

    public long getCooldown(long time){
       return Math.max(0, nextTriggerTime - time);
    }

    /**
     * Trigger the given ability<br>
     * Super has to be called before the subclass' implementation so cooldown is handled properly
     * @param server the minecraft server instance
     * @param player the player who triggered the ability
     * @return if the ability triggered successfully
     */
    public boolean trigger(MinecraftServer server, ServerPlayerEntity player){
        if(!player.isCreative()){
            if(!ManaAttachment.consumeMana(player,getBaseManaCost())){
                player.sendMessage(Text.literal("You don't have enough mana"));
                return false;
            }
        }
        this.nextTriggerTime = server.getTicks() + this.getBaseCooldown();
        this.ability.setNtt(this.nextTriggerTime);
        AttachedAbility.setAttached(player,this.ability);
        AvarisGates.LOGGER.info("{} triggered",this.getAbilityType());
        return true;
    }
}
