package com.avaris.avarisgates.core.item;

import com.avaris.avarisgates.AvarisGates;
import com.avaris.avarisgates.core.ModComponents;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

import java.util.ArrayList;

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

    public static final Identifier TEST_SOCKETABLE_ID = AvarisGates.id("test_socketable");
    public static final RegistryKey<Item> TEST_SOCKETABLE_KEY = RegistryKey.of(RegistryKeys.ITEM, TEST_SOCKETABLE_ID);

    public static final SocketableItem TEST_SOCKETABLE = (SocketableItem) register(
            new SocketableItem(new Item.Settings().registryKey(TEST_SOCKETABLE_KEY)
                    .component(ModComponents.FREE_SOCKETS,0)
                    .component(ModComponents.SOCKET_EFFECTS, new ArrayList<>())),
            TEST_SOCKETABLE_ID
    );

    public static final Identifier RUBY_GEM_ID = AvarisGates.id("ruby_gem");
    public static final RegistryKey<Item> RUBY_GEM_KEY = RegistryKey.of(RegistryKeys.ITEM, RUBY_GEM_ID);

    public static final RubyGemItem RUBY_GEM = (RubyGemItem) register(
            new RubyGemItem(new Item.Settings().registryKey(RUBY_GEM_KEY)),
            RUBY_GEM_ID
    );

    public static void init(){
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT)
                .register((itemGroup) -> {
                    itemGroup.add(GREAT_SWORD);
                });
    }
}
