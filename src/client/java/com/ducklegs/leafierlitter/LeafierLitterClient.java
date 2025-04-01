package com.ducklegs.leafierlitter;

import com.ducklegs.leafierlitter.block.ModBlocks;
import com.ducklegs.leafierlitter.world.biome.CustomBiomeColors;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.minecraft.client.color.world.BiomeColors;
import net.minecraft.client.render.RenderLayer;

public class LeafierLitterClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		// This entrypoint is suitable for setting up client-specific logic, such as rendering.
		ColorProviderRegistry.BLOCK.register((state, world, pos, tintIndex) -> world != null && pos != null ? BiomeColors.getFoliageColor(world, pos) : -12012264, ModBlocks.FRESH_OAK_LEAF_LITTER);
		ColorProviderRegistry.BLOCK.register((state, world, pos, tintIndex) -> world != null && pos != null ? CustomBiomeColors.getAutumnalFoliageColor(world, pos, CustomBiomeColors.LeafType.OAK) : -12012264, ModBlocks.AUTUMNAL_OAK_LEAF_LITTER);
		BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.FRESH_OAK_LEAF_LITTER, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.AUTUMNAL_OAK_LEAF_LITTER, RenderLayer.getCutout());

		ColorProviderRegistry.BLOCK.register((state, world, pos, tintIndex) -> -8345771, ModBlocks.FRESH_BIRCH_LEAF_LITTER);
		ColorProviderRegistry.BLOCK.register((state, world, pos, tintIndex) -> world != null && pos != null ? CustomBiomeColors.getAutumnalFoliageColor(world, pos, CustomBiomeColors.LeafType.BIRCH) : -8345771, ModBlocks.AUTUMNAL_BIRCH_LEAF_LITTER);
		ColorProviderRegistry.BLOCK.register((state, world, pos, tintIndex) -> world != null && pos != null ? BiomeColors.getDryFoliageColor(world, pos) : -8345771, ModBlocks.DECAYED_BIRCH_LEAF_LITTER);
		BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.FRESH_BIRCH_LEAF_LITTER, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.AUTUMNAL_BIRCH_LEAF_LITTER, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.DECAYED_BIRCH_LEAF_LITTER, RenderLayer.getCutout());

	}
}