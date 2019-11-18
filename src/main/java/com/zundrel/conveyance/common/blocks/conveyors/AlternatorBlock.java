package com.zundrel.conveyance.common.blocks.conveyors;

import com.zundrel.conveyance.api.IConveyorMachine;
import com.zundrel.conveyance.common.blocks.entities.ConveyorBlockEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Property;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

public class AlternatorBlock extends HorizontalFacingBlock implements IConveyorMachine {
    public AlternatorBlock(Settings settings) {
        super(settings);

        setDefaultState(getDefaultState().with(ConveyorProperties.CONVEYOR, false).with(ConveyorProperties.LEFT, false));
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState blockState, Direction fromDirection, BlockState fromState, IWorld world, BlockPos blockPos, BlockPos fromPos) {
        BlockState newState = blockState;
        Direction direction = newState.get(FACING);

        BlockPos leftPos = blockPos.offset(direction.rotateYCounterclockwise());
        BlockPos rightPos = blockPos.offset(direction.rotateYClockwise());

        if (world.getBlockState(leftPos).getBlock() instanceof ConveyorBlock && world.getBlockState(rightPos).getBlock() instanceof ConveyorBlock)
            newState = newState.with(ConveyorProperties.CONVEYOR, true);
        else
            newState = newState.with(ConveyorProperties.CONVEYOR, false);

        return newState;
    }

    @Override
    public void neighborUpdate(BlockState blockState, World world, BlockPos blockPos, Block block, BlockPos blockPos2, boolean boolean_1) {
        world.setBlockState(blockPos, blockState.getStateForNeighborUpdate(null, blockState, world, blockPos, blockPos2));
    }

    @Override
    public void insert(World world, BlockPos pos, BlockState state, ConveyorBlockEntity blockEntity, ItemStack stack, Direction direction) {
        if (state.get(ConveyorProperties.CONVEYOR)) {
            Direction facing = state.get(FACING);
            BlockPos leftPos = pos.offset(facing.rotateYCounterclockwise());
            BlockPos rightPos = pos.offset(facing.rotateYClockwise());
            ConveyorBlockEntity conveyorOne = (ConveyorBlockEntity) world.getBlockEntity(leftPos);
            ConveyorBlockEntity conveyorTwo = (ConveyorBlockEntity) world.getBlockEntity(rightPos);

            if (state.get(ConveyorProperties.LEFT)) {
                conveyorOne.setStack(stack);
            } else {
                conveyorTwo.setStack(stack);
            }

            world.setBlockState(pos, state.cycle(ConveyorProperties.LEFT));

            world.playSound(null, pos, SoundEvents.BLOCK_PISTON_EXTEND, SoundCategory.BLOCKS, 0.1F, 1);
        }
    }

    @Override
    public boolean canInsert(World world, BlockPos pos, BlockState state, ConveyorBlockEntity blockEntity, ItemStack stack, Direction direction) {
        Direction facing = state.get(FACING);
        ConveyorBlockEntity conveyorOne = (ConveyorBlockEntity) world.getBlockEntity(pos.offset(facing.rotateYCounterclockwise()));
        ConveyorBlockEntity conveyorTwo = (ConveyorBlockEntity) world.getBlockEntity(pos.offset(facing.rotateYClockwise()));

        if (state.get(ConveyorProperties.CONVEYOR) && conveyorOne != null && conveyorTwo != null) {
            return direction == facing.getOpposite() && conveyorOne.isEmpty() && conveyorTwo.isEmpty();
        }

        return false;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> stateManagerBuilder) {
        stateManagerBuilder.add(new Property[]{FACING, ConveyorProperties.CONVEYOR, ConveyorProperties.LEFT});
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext itemPlacementContext) {
        return this.getDefaultState().with(FACING, itemPlacementContext.getPlayer().isSneaking() ? itemPlacementContext.getPlayerFacing().getOpposite() : itemPlacementContext.getPlayerFacing());
    }
}
