package com.ducklegs.leafierlitter.mixin;

import net.minecraft.block.SaplingGenerator;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.TreeConfiguredFeatures;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;

import java.util.Optional;

import static net.minecraft.block.SaplingGenerator.OAK;

@Mixin (SaplingGenerator.class)
public class SaplingGeneratorMixin {

    @Shadow @Final private static Map<String, SaplingGenerator> GENERATORS;
    private Optional<RegistryKey<ConfiguredFeature<?, ?>>> regularLeafLitterVarient;
    private Optional<RegistryKey<ConfiguredFeature<?, ?>>> fancyLeafLitterVarient;
    private Optional<RegistryKey<ConfiguredFeature<?, ?>>> beesLeafLitterVarient;
    private Optional<RegistryKey<ConfiguredFeature<?, ?>>> beesFancyLeafLitterVarient;
    private Optional<RegistryKey<ConfiguredFeature<?, ?>>> megaLeafLitterVarient;

    @Inject(method = "<init>*", at = @At("TAIL"))
    private void modifyConstructor(
            String id,
            float rareChance,
            Optional<RegistryKey<ConfiguredFeature<?, ?>>> megaVariant,
            Optional<RegistryKey<ConfiguredFeature<?, ?>>> rareMegaVariant,
            Optional<RegistryKey<ConfiguredFeature<?, ?>>> regularVariant,
            Optional<RegistryKey<ConfiguredFeature<?, ?>>> rareRegularVariant,
            Optional<RegistryKey<ConfiguredFeature<?, ?>>> beesVariant,
            Optional<RegistryKey<ConfiguredFeature<?, ?>>> rareBeesVariant,
            CallbackInfo ci) {

        this.regularLeafLitterVarient = regularVariant;
        this.fancyLeafLitterVarient = rareRegularVariant;
        this.beesLeafLitterVarient = beesVariant;
        this.beesFancyLeafLitterVarient = rareBeesVariant;
        this.megaLeafLitterVarient = megaVariant;

        if (id.equals("oak")) {
            this.regularLeafLitterVarient = Optional.of(TreeConfiguredFeatures.OAK_LEAF_LITTER);
            this.fancyLeafLitterVarient = Optional.of(TreeConfiguredFeatures.FANCY_OAK_LEAF_LITTER);
        }
        if (id.equals("birch")) {
            this.regularLeafLitterVarient = Optional.of(TreeConfiguredFeatures.BIRCH_LEAF_LITTER);
            this.beesLeafLitterVarient = Optional.of(TreeConfiguredFeatures.BIRCH_BEES_0002_LEAF_LITTER);
        }

        if (id.equals("dark_oak")) {
            this.megaLeafLitterVarient = Optional.of(TreeConfiguredFeatures.DARK_OAK_LEAF_LITTER);
        }
    }

    @Inject(method = "getSmallTreeFeature", at = @At("RETURN"), cancellable = true)
    private void modifySmallTreeFeature(Random random, boolean flowersNearby, CallbackInfoReturnable<RegistryKey<ConfiguredFeature<?, ?>>> cir) {
        SaplingGeneratorMixin self = (SaplingGeneratorMixin) (Object) this;

        if (this.beesLeafLitterVarient.isPresent() && flowersNearby && random.nextBoolean()) {
            cir.setReturnValue(self.beesLeafLitterVarient.get());
        } else if (this.regularLeafLitterVarient.isPresent() && random.nextBoolean()) {
            cir.setReturnValue(self.regularLeafLitterVarient.get());
        } else if (this.fancyLeafLitterVarient.isPresent() && random.nextBoolean()) {
            cir.setReturnValue(self.fancyLeafLitterVarient.get());
        }
    }

    @Inject(method = "getMegaTreeFeature", at = @At("RETURN"), cancellable = true)
    private void modifyMegaTreeFeature(Random random, CallbackInfoReturnable<RegistryKey<ConfiguredFeature<?, ?>>> cir) {
        SaplingGeneratorMixin self = (SaplingGeneratorMixin) (Object) this;

        if (this.megaLeafLitterVarient.isPresent() && random.nextBoolean()) {
            cir.setReturnValue(self.megaLeafLitterVarient.get());
        }
    }
}
