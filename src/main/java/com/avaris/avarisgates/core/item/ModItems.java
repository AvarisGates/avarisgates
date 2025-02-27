package com.avaris.avarisgates.core.item;

import com.avaris.avarisgates.AvarisGates;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

public class ModItems {
    public static Item register(Item item, Identifier id){
        return Registry.register(Registries.ITEM, id, item);
    }

    public static final Identifier GREAT_SWORD_ID = AvarisGates.id("great_sword");
    public static final RegistryKey<Item> GREAT_SWORD_KEY = RegistryKey.of(RegistryKeys.ITEM, GREAT_SWORD_ID);

    public static final GreatSwordItem GREAT_SWORD = (GreatSwordItem) register(
            new GreatSwordItem(GreatSwordItem.SETTINGS),
            GREAT_SWORD_ID
    );


    public static void init(){
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT)
                .register((itemGroup) -> {
                    itemGroup.add(GREAT_SWORD);
                });
    }
}
