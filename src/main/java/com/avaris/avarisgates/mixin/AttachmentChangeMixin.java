package com.avaris.avarisgates.mixin;

import net.fabricmc.fabric.api.attachment.v1.AttachmentTarget;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.fabricmc.fabric.impl.attachment.sync.AttachmentChange;
import net.fabricmc.fabric.impl.attachment.sync.AttachmentTargetInfo;
import net.minecraft.client.MinecraftClient;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.lang.invoke.CallSite;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Mixin(AttachmentChange.class)
public abstract class AttachmentChangeMixin {
    @Final
    @Shadow
    AttachmentTargetInfo<?> targetInfo;

    @Final
    @Shadow
    AttachmentType<?> type;

    @Unique
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    @Inject(method = "apply",at = @At("HEAD"), cancellable = true)
    void onApply(World world, CallbackInfo ci){
        AttachmentTarget target = targetInfo.getTarget(world);
        if(target == null){
            scheduler.schedule(() -> {
                    targetInfo.getTarget(world).setAttached((AttachmentType<Object>) type, decodeValue(world.getRegistryManager()));
            },100, TimeUnit.MILLISECONDS);
        }
        else{
            target.setAttached((AttachmentType<Object>) type, decodeValue(world.getRegistryManager()));
        }
        ci.cancel();
    }

    @Shadow
    public abstract Object decodeValue(DynamicRegistryManager registryManager);
}
