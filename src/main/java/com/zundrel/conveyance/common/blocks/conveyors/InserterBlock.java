package com.zundrel.conveyance.common.blocks.conveyors;

import com.zundrel.conveyance.api.ConveyableBlock;
import com.zundrel.conveyance.common.blocks.entities.InserterBlockEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.EntityContext;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

import java.util.Random;

public class InserterBlock extends HorizontalFacingBlock implements BlockEntityProvider, ConveyableBlock {
	private String type;
	private int speed;

    public InserterBlock(String type, int speed, Settings settings) {
        super(settings);

        this.type = type;
        this.speed = speed;
    }

	public String getType() {
		return type;
	}

	public int getSpeed() {
    	return speed;
	}

    @Override
    public BlockEntity createBlockEntity(BlockView blockView) {
        return new InserterBlockEntity();
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> stateManagerBuilder) {
        stateManagerBuilder.add(FACING, Properties.POWERED);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext itemPlacementContext) {
        return this.getDefaultState().with(Properties.POWERED, false).with(FACING, itemPlacementContext.getPlayer().isSneaking() ? itemPlacementContext.getPlayerFacing().getOpposite() : itemPlacementContext.getPlayerFacing());
    }

    @Override
	public void neighborUpdate(BlockState state, World world, BlockPos pos, Block block, BlockPos neighborPos, boolean moved) {
		if (!world.isClient) {
			boolean bl = state.get(Properties.POWERED);
			if (bl != world.isReceivingRedstonePower(pos)) {
				if (bl) {
					world.getBlockTickScheduler().schedule(pos, this, 4);
				} else {
					world.setBlockState(pos, state.cycle(Properties.POWERED), 2);
				}
			}
		}
	}

	@Override
	public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		if (state.get(Properties.POWERED) && !world.isReceivingRedstonePower(pos)) {
			world.setBlockState(pos, state.cycle(Properties.POWERED), 2);
		}
	}

	@Override
	public void onBlockAdded(BlockState blockState, World world, BlockPos blockPos, BlockState blockState2, boolean boolean_1) {
		updateDiagonals(world, this, blockPos);
	}

	@Override
	public void onBlockRemoved(BlockState state, World world, BlockPos pos, BlockState newState, boolean notify) {
		if (state.getBlock() != newState.getBlock()) {
			BlockEntity blockEntity = world.getBlockEntity(pos);
			if (blockEntity instanceof InserterBlockEntity) {
				ItemScatterer.spawn(world, pos.getX(), pos.getY(), pos.getZ(), ((InserterBlockEntity) blockEntity).getStack());
			}

			super.onBlockRemoved(state, world, pos, newState, notify);
		}

		updateDiagonals(world, this, pos);
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, EntityContext context) {
		return VoxelShapes.cuboid(0, 0, 0, 1, 0.5, 1);
	}
}
