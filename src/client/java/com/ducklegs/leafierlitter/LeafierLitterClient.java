package com.ducklegs.leafierlitter;

import com.ducklegs.leafierlitter.block.ModBlocks;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.minecraft.client.color.world.BiomeColors;
import net.minecraft.client.render.RenderLayer;

public class LeafierLitterClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		// This entrypoint is suitable for setting up client-specific logic, such as rendering.
		ColorProviderRegistry.BLOCK.register((state, world, pos, tintIndex) -> world != null && pos != null ? BiomeColors.getFoliageColor(world, pos) : -12012264, ModBlocks.FRESH_LEAF_LITTER);
		BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.FRESH_LEAF_LITTER, RenderLayer.getCutout());
	}
}