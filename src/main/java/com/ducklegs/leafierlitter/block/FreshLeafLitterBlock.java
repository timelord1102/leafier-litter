package com.ducklegs.leafierlitter.block;

import com.mojang.serialization.MapCodec;
import java.util.function.Function;

import net.minecraft.block.*;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.state.property.Property;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldView;

public class FreshLeafLitterBlock extends PlantBlock implements Segmented {
    public static final MapCodec<com.ducklegs.leafierlitter.block.FreshLeafLitterBlock> CODEC = createCodec(com.ducklegs.leafierlitter.block.FreshLeafLitterBlock::new);
    public static final EnumProperty<Direction> HORIZONTAL_FACING;
    private final Function<BlockState, VoxelShape> shapeFunction;

    public FreshLeafLitterBlock(AbstractBlock.Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState().with(HORIZONTAL_FACING, Direction.NORTH).with(this.getAmountProperty(), 1));
        this.shapeFunction = this.createShapeFunction();
    }

    private Function<BlockState, VoxelShape> createShapeFunction() {
        return this.createShapeFunction(this.createShapeFunction(HORIZONTAL_FACING, this.getAmountProperty()));
    }

    protected MapCodec<com.ducklegs.leafierlitter.block.FreshLeafLitterBlock> getCodec() {
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
}
