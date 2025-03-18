package com.avaris.avarisgates.common.item;

import com.avaris.avarisgates.common.ModComponents;
import com.avaris.avarisgates.common.player.attribute.AttributeType;
import net.minecraft.component.ComponentMap;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.StackReference;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.ClickType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class SocketableItem extends Item {

    @Override
    public ItemStack getDefaultStack() {
        ItemStack stack = new ItemStack(this);
        stack.set(ModComponents.FREE_SOCKETS,0);
        stack.set(ModComponents.SOCKET_EFFECTS,List.of());
        return stack;
    }

    @Override
    public ComponentMap getComponents() {
        return super.getComponents();
    }

    public SocketableItem(Settings settings) {
        super(settings);
    }

    @Override
    public int getMaxCount() {
        return 1;
    }

    @Override
    public boolean onClicked(ItemStack stack, ItemStack otherStack, Slot slot, ClickType clickType, PlayerEntity player, StackReference cursorStackReference) {
        if(otherStack.isEmpty()){
            return false;
        }
        if(otherStack.getItem() instanceof InsertableItem insertable){
           int freeSockets = getFreeSockets(stack);
           if(SocketableItem.insertItem(player,stack,otherStack,insertable,cursorStackReference,freeSockets,slot.getIndex())){
               player.playSound(SoundEvents.BLOCK_AMETHYST_BLOCK_RESONATE);
               player.playSound(SoundEvents.BLOCK_BONE_BLOCK_HIT);
               //player.playSound(SoundEvents.BLOCK_ANVIL_USE,0.1f,2.1f);
               return true;
           }
        }
        return false;
    }

    private static boolean insertItem(PlayerEntity player, ItemStack stack, ItemStack otherStack, InsertableItem insertable, StackReference cursorStackReference, int freeSockets, int slotIndex) {
        if(freeSockets <= 0||stack.getCount() > 1) {
            return false;
        }
        if(player instanceof ServerPlayerEntity serverPlayer){
            // Decrease the number of free sockets
            if(freeSockets == 1){
                // If there are no free sockets remove the component, to save memory and disk space
                stack.remove(ModComponents.FREE_SOCKETS);
            }else{
                stack.set(ModComponents.FREE_SOCKETS,freeSockets - 1);
            }

            SocketableItem.addSocketEffects(stack,insertable.getEffects(otherStack));

            //serverPlayer.getInventory().setStack(slotIndex,stack);
            //serverPlayer.getInventory().markDirty();
            //serverPlayer.currentScreenHandler.sendContentUpdates();

            // Consume the insertable item
            otherStack.decrement(1);
            cursorStackReference.set(otherStack);
        }
        return true;
    }

    public static int getFreeSockets(ItemStack stack){
        if(stack.getItem() instanceof SocketableItem){
            Integer ret = stack.get(ModComponents.FREE_SOCKETS);
            if(ret == null){
                return 0;
            }
            return ret;
        }
        return 0;
    }

    public static Collection<SocketEffect> getSocketEffects(ItemStack stack){
        List<SocketEffect> socketEffects = stack.get(ModComponents.SOCKET_EFFECTS);
        if(socketEffects == null){
            socketEffects = List.of();
        }
        return socketEffects;
    }

    public static ItemStack addSocketEffects(ItemStack stack, Collection<SocketEffect> effects){
        // DO NOT MODIFY THIS ARRAY!!!!!!!
        // IT WILL BREAK THE CODE!!!!!!!!!
        List<SocketEffect> ret = (List<SocketEffect>) getSocketEffects(stack);

        ArrayList<SocketEffect> socketEffects = new ArrayList<>(effects);
        socketEffects.addAll(ret);

        stack.set(ModComponents.SOCKET_EFFECTS,socketEffects);
        return stack;
    }

    public static long getAttributeAdditiveModifications(LivingEntity entity, AttributeType type) {
        long modifier = 0;
        if(entity instanceof PlayerEntity player){
            PlayerInventory inventory = player.getInventory();
            int selectedSlot = inventory.selectedSlot;
            for(int i = 0; i < inventory.size(); i++){
                ItemStack stack = inventory.getStack(i);
                if(stack.getItem() instanceof SocketableItem item){
                    if(!item.isInCorrectSlot(i,selectedSlot)){
                        continue;
                    }
                    Optional<Long> current_modifier = getSocketEffects(stack).stream()
                            .map(effect -> effect.getAttributeAdditiveModification(type))
                            .reduce(Long::sum);
                   if(current_modifier.isPresent()){
                       modifier += current_modifier.get();
                   }
                }

            }
        }
        return modifier;
    }

    private boolean isInCorrectSlot(int slotIndex, int selectedSlot) {
        return slotIndex == selectedSlot;
    }


    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        super.appendTooltip(stack, context, tooltip, type);
        int freeSockets = getFreeSockets(stack);
        String freeSocketsStr = "****";
        if(freeSockets > 4){
           freeSocketsStr =  String.valueOf(freeSockets);
        }else{
            freeSocketsStr = freeSocketsStr.substring(0,freeSockets);
        }

        if(freeSockets > 0){
            tooltip.add(Text.literal("Sockets " +freeSocketsStr));
        }
        SocketEffect.appendTooltip(tooltip,getSocketEffects(stack));
    }
}
