package com.ducklegs.leafierlitter.mixin;

import com.ducklegs.leafierlitter.utils.GetBiomeDownfall;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeEffects;
import net.minecraft.world.biome.GenerationSettings;
import net.minecraft.world.biome.SpawnSettings;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Biome.class)
public class GetBiomeWeatherMixin implements GetBiomeDownfall {
	@Unique
	private float downfall;

	@Inject(method = "<init>*", at = @At("TAIL"))
	private void init(Biome.Weather weather, BiomeEffects effects, GenerationSettings generationSettings, SpawnSettings spawnSettings, CallbackInfo ci) {
		this.downfall = weather.downfall();
	}

	@Override
	public float getDownfall() {
		return downfall;
	}
}
