package com.ducklegs.leafierlitter.block;

import com.ducklegs.leafierlitter.LeafierLitter;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.registry.OxidizableBlocksRegistry;
import net.minecraft.block.*;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;

import java.util.function.Function;

public class ModBlocks {
    public static final Block FRESH_OAK_LEAF_LITTER = register("fresh_oak_leaf_litter", (settings) -> new GenericLeafLitterBlock(Oxidizable.OxidationLevel.UNAFFECTED, settings), AbstractBlock.Settings.create().mapColor(MapColor.GREEN).replaceable().noCollision().sounds(BlockSoundGroup.LEAF_LITTER).pistonBehavior(PistonBehavior.DESTROY), true);
    public static final Block AUTUMNAL_OAK_LEAF_LITTER = register("autumnal_oak_leaf_litter", (settings) -> new GenericLeafLitterBlock(Oxidizable.OxidationLevel.WEATHERED, settings), AbstractBlock.Settings.create().mapColor(MapColor.DULL_RED).replaceable().noCollision().sounds(BlockSoundGroup.LEAF_LITTER).pistonBehavior(PistonBehavior.DESTROY), true);
    public static final Block FRESH_BIRCH_LEAF_LITTER = register("fresh_birch_leaf_litter", (settings) -> new GenericLeafLitterBlock(Oxidizable.OxidationLevel.UNAFFECTED, settings), AbstractBlock.Settings.create().mapColor(MapColor.GREEN).replaceable().noCollision().sounds(BlockSoundGroup.LEAF_LITTER).pistonBehavior(PistonBehavior.DESTROY), true);
    public static final Block AUTUMNAL_BIRCH_LEAF_LITTER = register("autumnal_birch_leaf_litter", (settings) -> new GenericLeafLitterBlock(Oxidizable.OxidationLevel.WEATHERED, settings), AbstractBlock.Settings.create().mapColor(MapColor.YELLOW).replaceable().noCollision().sounds(BlockSoundGroup.LEAF_LITTER).pistonBehavior(PistonBehavior.DESTROY), true);
    public static final Block DECAYED_BIRCH_LEAF_LITTER = register("decayed_birch_leaf_litter", (settings) -> new GenericLeafLitterBlock(Oxidizable.OxidationLevel.UNAFFECTED, settings), AbstractBlock.Settings.create().mapColor(MapColor.BROWN).replaceable().noCollision().sounds(BlockSoundGroup.LEAF_LITTER).pistonBehavior(PistonBehavior.DESTROY), true);

    public static void initialize() {
        putInNaturalTab(ModBlocks.FRESH_OAK_LEAF_LITTER);
        putInNaturalTab(ModBlocks.AUTUMNAL_OAK_LEAF_LITTER);
        putInNaturalTab(ModBlocks.FRESH_BIRCH_LEAF_LITTER);
        putInNaturalTab(ModBlocks.AUTUMNAL_BIRCH_LEAF_LITTER);
        putInNaturalTab(ModBlocks.DECAYED_BIRCH_LEAF_LITTER);
        OxidizableBlocksRegistry.registerOxidizableBlockPair(ModBlocks.FRESH_OAK_LEAF_LITTER, ModBlocks.AUTUMNAL_OAK_LEAF_LITTER);
        OxidizableBlocksRegistry.registerOxidizableBlockPair(ModBlocks.AUTUMNAL_OAK_LEAF_LITTER, Blocks.LEAF_LITTER);
        OxidizableBlocksRegistry.registerOxidizableBlockPair(ModBlocks.FRESH_BIRCH_LEAF_LITTER, ModBlocks.AUTUMNAL_BIRCH_LEAF_LITTER);
        OxidizableBlocksRegistry.registerOxidizableBlockPair(ModBlocks.AUTUMNAL_BIRCH_LEAF_LITTER, ModBlocks.DECAYED_BIRCH_LEAF_LITTER);

    }

    public static void putInNaturalTab(Block block) {
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.NATURAL)
                .register((itemGroup) -> itemGroup.add(block.asItem()));
    }

    private static RegistryKey<Block> keyOfBlock(String name) {
        return RegistryKey.of(RegistryKeys.BLOCK, Identifier.of(LeafierLitter.MOD_ID, name));
    }

    private static RegistryKey<Item> keyOfItem(String name) {
        return RegistryKey.of(RegistryKeys.ITEM, Identifier.of(LeafierLitter.MOD_ID, name));
    }

    private static Block register(String name, Function<AbstractBlock.Settings, Block> blockFactory, AbstractBlock.Settings settings, boolean shouldRegisterItem) {
        // Create a registry key for the block
        RegistryKey<Block> blockKey = keyOfBlock(name);
        // Create the block instance
        Block block = blockFactory.apply(settings.registryKey(blockKey));

        // Sometimes, you may not want to register an item for the block.
        // Eg: if it's a technical block like `minecraft:moving_piston` or `minecraft:end_gateway`
        if (shouldRegisterItem) {
            // Items need to be registered with a different type of registry key, but the ID
            // can be the same.
            RegistryKey<Item> itemKey = keyOfItem(name);

            BlockItem blockItem = new BlockItem(block, new Item.Settings().registryKey(itemKey));
            Registry.register(Registries.ITEM, itemKey, blockItem);

        }

        return Registry.register(Registries.BLOCK, blockKey, block);
    }


}
