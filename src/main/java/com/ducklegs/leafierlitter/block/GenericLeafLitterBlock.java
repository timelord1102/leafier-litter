package com.ducklegs.leafierlitter.block;

import com.mojang.serialization.MapCodec;

import java.util.Optional;
import java.util.function.Function;

import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.*;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldView;
import net.minecraft.world.biome.BiomeKeys;
import net.minecraft.world.dimension.DimensionTypes;

public class GenericLeafLitterBlock extends PlantBlock implements Segmented, Oxidizable{
    public static final MapCodec<GenericLeafLitterBlock> CODEC = RecordCodecBuilder.mapCodec((instance) -> instance.group(OxidationLevel.CODEC.fieldOf("weathering_state").forGetter(Degradable::getDegradationLevel), createSettingsCodec()).apply(instance, GenericLeafLitterBlock::new));
    public static final EnumProperty<Direction> HORIZONTAL_FACING;
    private final Function<BlockState, VoxelShape> shapeFunction;
    private final Oxidizable.OxidationLevel oxidationLevel;

    public GenericLeafLitterBlock(Oxidizable.OxidationLevel oxidationLevel, AbstractBlock.Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState().with(HORIZONTAL_FACING, Direction.NORTH).with(this.getAmountProperty(), 1));
        this.shapeFunction = this.createShapeFunction();
        this.oxidationLevel = oxidationLevel;
    }

    private Function<BlockState, VoxelShape> createShapeFunction() {
        return this.createShapeFunction(this.createShapeFunction(HORIZONTAL_FACING, this.getAmountProperty()));
    }

    protected MapCodec<GenericLeafLitterBlock> getCodec() {
        return CODEC;
    }

    public BlockState rotate(BlockState state, BlockRotation rotation) {
        return state.with(HORIZONTAL_FACING, rotation.rotate(state.get(HORIZONTAL_FACING)));
    }

    public BlockState mirror(BlockState state, BlockMirror mirror) {
        return state.rotate(mirror.getRotation(state.get(HORIZONTAL_FACING)));
    }

    public boolean canReplace(BlockState state, ItemPlacementContext context) {
        return this.shouldAddSegment(state, context, this.getAmountProperty()) || super.canReplace(state, context);
    }

    protected boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        BlockPos blockPos = pos.down();

        return world.getBlockState(blockPos).isSideSolidFullSquare(world, blockPos, Direction.UP);
    }

    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return this.shapeFunction.apply(state);
    }

    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getPlacementState(ctx, this, this.getAmountProperty(), HORIZONTAL_FACING);
    }

    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(HORIZONTAL_FACING, this.getAmountProperty());
    }

    static {
        HORIZONTAL_FACING = Properties.HORIZONTAL_FACING;
    }

    protected void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if (world.getBiome(pos).matchesKey(BiomeKeys.PALE_GARDEN)) {
            if (this.getDegradationLevel() == Oxidizable.OxidationLevel.UNAFFECTED) {
                Optional<BlockState> finalDegradationState = getDegradationResult(state)
                        .flatMap(this::getDegradationResult);
                finalDegradationState.ifPresent((degraded2) -> {
                    world.setBlockState(pos, degraded2);
                });
            }
            else {
                getDegradationResult(state).ifPresent((degraded) -> {
                    world.setBlockState(pos, degraded);
                });
            }
        }
        if (!(world.getBiomeFabric(pos).matchesKey(BiomeKeys.SWAMP) || world.getBiomeFabric(pos).matchesKey(BiomeKeys.MANGROVE_SWAMP)
                || (world.getBiomeFabric(pos).matchesKey(BiomeKeys.JUNGLE) || world.getBiomeFabric(pos).matchesKey(BiomeKeys.BAMBOO_JUNGLE)
                || world.getBiomeFabric(pos).matchesKey(BiomeKeys.SPARSE_JUNGLE)))) {
            this.customTickDegradation(state, world, pos, random);
        }
    }

    protected boolean hasRandomTicks(BlockState state) {
        return Oxidizable.getIncreasedOxidationBlock(state.getBlock()).isPresent();
    }

    public Oxidizable.OxidationLevel getDegradationLevel() {
        return this.oxidationLevel;
    }

    private void customTickDegradation(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        float f = 0.85688889F;
        if (random.nextFloat() < f) {
            this.customTryDegrade(state, world, pos, random).ifPresent((degraded) -> world.setBlockState(pos, degraded));
        }

    }

    private Optional<BlockState> customTryDegrade(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        int segments = state.get(this.getAmountProperty());
        float baseChance = 0.1F;


        if (world.getBiomeFabric(pos).matchesKey(BiomeKeys.DESERT) || world.getBiomeFabric(pos).matchesKey(BiomeKeys.SAVANNA)
                || (world.getBiomeFabric(pos).matchesKey(BiomeKeys.SAVANNA_PLATEAU) || world.getBiomeFabric(pos).matchesKey(BiomeKeys.BADLANDS)
                || world.getBiomeFabric(pos).matchesKey(BiomeKeys.ERODED_BADLANDS) || world.getBiomeFabric(pos).matchesKey(BiomeKeys.WOODED_BADLANDS))){
            baseChance = 0.3F;
        }
        if (world.getDimensionEntry().matchesKey(DimensionTypes.THE_NETHER)) {
            baseChance = 0.7F;
        }

        float adjustedChance = baseChance / segments;

        int j = 0;
        int k = 0;

        for (int l = 0; l < segments; ++l) {
            if (random.nextFloat() < adjustedChance) {
                ++j;
            } else {
                ++k;
            }
        }

        float f = (float) j / k;

        return random.nextFloat() < f ? this.getDegradationResult(state) : Optional.empty();
    }
}
