package com.avaris.avarisgates.core.entity.custom;

import com.avaris.avarisgates.core.item.ModItems;
import net.minecraft.entity.AnimationState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class GoblinEntity extends AnimalEntity {
    public final AnimationState idleAnimationState = new AnimationState();
    private int idleAnimationTimeout = 0;

    public GoblinEntity(EntityType<? extends AnimalEntity> entityType, World world) {
        super(entityType, world);
    }

    //Goals of the mob, what we want it to do basically and with which priority, please put SwimGoal first, don't make mobs stupid
    @Override
    protected void initGoals() {
        this.goalSelector.add(0, new SwimGoal(this));

        //ATTACK THE PLAYER
        this.goalSelector.add(1, new MeleeAttackGoal(this, 1.10D, false));
        //DO WE WANT MOBS TO BE LURED BY SOMETHING?
        this.goalSelector.add(2, new TemptGoal(this, 1.25D, Ingredient.ofItems(ModItems.GREAT_SWORD), false)); //TODO: I just put a great sword for the sake of it we got to change this don't forget

        this.goalSelector.add(3, new WanderAroundFarGoal(this, 1.0D));
        this.goalSelector.add(4, new LookAtEntityGoal(this, PlayerEntity.class, 4.0F));
        this.goalSelector.add(5, new LookAroundGoal(this));
    }

    //Base attributes of the mob
    public static DefaultAttributeContainer.Builder createAttributes() {
        return MobEntity.createMobAttributes()
                .add(EntityAttributes.MAX_HEALTH, 10)
                .add(EntityAttributes.MOVEMENT_SPEED, .05)
                .add(EntityAttributes.ATTACK_DAMAGE, 10)
                .add(EntityAttributes.ATTACK_SPEED, 0.5)
                .add(EntityAttributes.ENTITY_INTERACTION_RANGE, 1);
    }

    //ANIMATION STUFF
    private void setupAnimationStates() {
        if (this.idleAnimationTimeout <= 0) {
            this.idleAnimationTimeout = 50; //Put this in ticks based on the length of the animation
            this.idleAnimationState.start(this.age);
        } else {
            --this.idleAnimationTimeout;
        }
    }

    @Override
    public void tick() {
        super.tick();

        if (this.getWorld().isClient()) {
            this.setupAnimationStates();
        }
    }

    //Do you want to breed goblins...?
    @Override
    public boolean isBreedingItem(ItemStack stack) {
        return false;
    }

    //Baby goblins?????
    @Override
    public @Nullable PassiveEntity createChild(ServerWorld world, PassiveEntity entity) {
        return null;
    }
}
