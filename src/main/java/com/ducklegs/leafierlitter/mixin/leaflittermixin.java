package com.ducklegs.leafierlitter.mixin;

import com.ducklegs.leafierlitter.block.ModBlocks;
import net.minecraft.block.*;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LeavesBlock.class)
public class leaflittermixin {

    @Inject(method = "scheduledTick", at = @At("HEAD"))
    private void injectLeafLitterPlacement(BlockState state, ServerWorld world, BlockPos pos, Random random, CallbackInfo ci) {
        if ((state.getBlock() == Blocks.OAK_LEAVES || state.getBlock() == Blocks.BIRCH_LEAVES) && world.getBlockState(pos.down()).isAir()) {
            BlockPos belowPos = pos.down();
            BlockPos currentPos = pos;
            for (int j = 0; j < 7; ++j) {
                float placementChance = random.nextFloat();
                BlockState belowState = world.getBlockState(belowPos);
                if (!belowState.isAir() && !belowState.isSideSolidFullSquare(world, belowPos, Direction.UP)) {
                    break;
                }
                if (world.getBlockState(currentPos).isAir() && world.getBlockState(belowPos).isFullCube(world, belowPos)) {
                    int leafAmount = random.nextBetween(1, 4);
                    Direction facingProperty = Direction.random(random);
                    boolean placeAdjacent = random.nextBoolean();
                    BlockRotation rotation = BlockRotation.random(random);
                    if (state.getBlock() == Blocks.OAK_LEAVES) {
                        if (placementChance < 0.2F) {
                            world.setBlockState(currentPos, (ModBlocks.FRESH_OAK_LEAF_LITTER.getDefaultState().with(Segmented.SEGMENT_AMOUNT, leafAmount)).rotate(rotation), Block.NOTIFY_ALL);
                        }
                        if (placeAdjacent && world.getBlockState(pos.offset(facingProperty)).isAir() && world.getBlockState(belowPos.offset(facingProperty)).isFullCube(world, belowPos.offset(facingProperty)) && world.getBlockState(currentPos.offset(facingProperty)).isAir()) {
                            world.setBlockState(currentPos.offset(facingProperty), (ModBlocks.FRESH_OAK_LEAF_LITTER.getDefaultState().with(Segmented.SEGMENT_AMOUNT, leafAmount)).rotate(rotation), Block.NOTIFY_ALL);
                        }
                    } else if (state.getBlock() == Blocks.BIRCH_LEAVES) {
                        if (placementChance < 0.2F) {
                            world.setBlockState(currentPos, (ModBlocks.FRESH_BIRCH_LEAF_LITTER.getDefaultState().with(Segmented.SEGMENT_AMOUNT, leafAmount)).rotate(rotation), Block.NOTIFY_ALL);
                        }
                        if (placeAdjacent && world.getBlockState(pos.offset(facingProperty)).isAir() && world.getBlockState(belowPos.offset(facingProperty)).isFullCube(world, belowPos.offset(facingProperty)) && world.getBlockState(currentPos.offset(facingProperty)).isAir()) {
                            world.setBlockState(currentPos.offset(facingProperty), (ModBlocks.FRESH_BIRCH_LEAF_LITTER.getDefaultState().with(Segmented.SEGMENT_AMOUNT, leafAmount)).rotate(rotation), Block.NOTIFY_ALL);
                        }
                    }
                    break;
                }
                belowPos = belowPos.down();
                currentPos = currentPos.down();
            }
        }
    }
}
