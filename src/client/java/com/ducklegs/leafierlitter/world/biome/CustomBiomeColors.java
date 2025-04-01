package com.ducklegs.leafierlitter.world.biome;

import com.ducklegs.leafierlitter.utils.GetBiomeDownfall;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.BlockRenderView;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;

import java.util.HashSet;
import java.util.Set;

public class CustomBiomeColors {
    public enum LeafType {
        OAK, BIRCH
    }

    // Define the colors for different biomes
    private static final int[][] LEAF_COLORS = {
            {0x9C2706, 0xA3740B, 0xDD3708, 0x9B7B73, 0xFF630A, 0x9B4133, 0x9C3449},
            {0xFBBB01, 0xA3740B, 0xFFDB00, 0xA89152, 0xFBCD01, 0xBC7B01, 0xFBBBA3}
    };

    public static int getAutumnalFoliageColor(BlockRenderView world, BlockPos pos, LeafType leafType) {
        Set<RegistryEntry<Biome>> biomes = new HashSet<>();
        biomes.add(world.getBiomeFabric(pos));
        biomes.add(world.getBiomeFabric(pos.north()));
        biomes.add(world.getBiomeFabric(pos.south()));
        biomes.add(world.getBiomeFabric(pos.east()));
        biomes.add(world.getBiomeFabric(pos.west()));

        float[] temperatures = new float[biomes.size()];
        float[] humidities = new float[biomes.size()];
        int index = 0;
        for (RegistryEntry<Biome> biome : biomes) {
            temperatures[index] = biome.value().getTemperature();
            humidities[index] = ((GetBiomeDownfall) (Object) biome.value()).getDownfall();
            index++;
        }

        int color = blendBiomeColors(temperatures, humidities, biomes, leafType);
        return color;
    }

    private static int blendBiomeColors(float[] temperatures, float[] humidities, Set<RegistryEntry<Biome>> biomes, LeafType leafType) {
        int blendedColor = 0;
        RegistryEntry<Biome>[] biomeArray = biomes.toArray(new RegistryEntry[0]);
        for (int i = 0; i < temperatures.length; i++) {
            int color = getColorForBiome(temperatures[i], humidities[i], biomeArray[i], leafType);
            blendedColor = blendColors(blendedColor, color, 1.0f / (i + 1));
        }
        return blendedColor;
    }

    private static int getColorForBiome(float temperature, float humidity, RegistryEntry<Biome> biome, LeafType leafType) {
        int[] colors = LEAF_COLORS[leafType.ordinal()];

        if (temperature < 0.3) {
            return colors[6]; // COLD_COLOR
        } else if (temperature > 0.9 && humidity < 0.2) {
            return colors[5]; // DRY_COLOR
        } else if (humidity > 0.9) {
            if (temperature > 0.8) {
                return colors[2]; // JUNGLE_COLOR
            } else if (temperature > 0.5) {
                return colors[4]; // MANGROVE_COLOR
            } else {
                return colors[1]; // SWAMP_COLOR
            }
        } else {
            if (biome.matchesKey(BiomeKeys.PALE_GARDEN)) {
                return colors[3]; // PALE_GARDEN_COLOR
            }
            return colors[0]; // DEFAULT_COLOR
        }
    }

    private static int blendColors(int color1, int color2, float weight) {
        int r1 = (color1 >> 16) & 0xFF;
        int g1 = (color1 >> 8) & 0xFF;
        int b1 = color1 & 0xFF;

        int r2 = (color2 >> 16) & 0xFF;
        int g2 = (color2 >> 8) & 0xFF;
        int b2 = color2 & 0xFF;

        int r = MathHelper.clamp((int) (r1 * (1 - weight) + r2 * weight), 0, 255);
        int g = MathHelper.clamp((int) (g1 * (1 - weight) + g2 * weight), 0, 255);
        int b = MathHelper.clamp((int) (b1 * (1 - weight) + b2 * weight), 0, 255);

        return (r << 16) | (g << 8) | b;
    }
}