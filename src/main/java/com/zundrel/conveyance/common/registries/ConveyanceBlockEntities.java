package com.zundrel.conveyance.common.registries;

import com.zundrel.conveyance.Conveyance;
import com.zundrel.conveyance.common.blocks.entities.*;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.function.Supplier;

public class ConveyanceBlockEntities {
	public static BlockEntityType<DoubleMachineBlockEntity> ALTERNATOR = register("alternator", AlternatorBlockEntity::new, ConveyanceBlocks.ALTERNATOR);
	public static BlockEntityType<DoubleMachineBlockEntity> SPLITTER = register("splitter", SplitterBlockEntity::new, ConveyanceBlocks.SPLITTER);
	public static BlockEntityType<InserterBlockEntity> INSERTER = register("inserter", InserterBlockEntity::new, ConveyanceBlocks.INSERTER, ConveyanceBlocks.INSERTER_FAST);

    public static BlockEntityType<ConveyorBlockEntity> CONVEYOR = register("conveyor", ConveyorBlockEntity::new, ConveyanceBlocks.CONVEYOR, ConveyanceBlocks.FAST_CONVEYOR, ConveyanceBlocks.EXPRESS_CONVEYOR);
    public static BlockEntityType<VerticalConveyorBlockEntity> VERTICAL_CONVEYOR = register("vertical_conveyor", VerticalConveyorBlockEntity::new, ConveyanceBlocks.VERTICAL_CONVEYOR, ConveyanceBlocks.VERTICAL_FAST_CONVEYOR, ConveyanceBlocks.VERTICAL_EXPRESS_CONVEYOR);
    public static BlockEntityType<DownVerticalConveyorBlockEntity> DOWN_VERTICAL_CONVEYOR = register("down_vertical_conveyor", DownVerticalConveyorBlockEntity::new, ConveyanceBlocks.DOWN_VERTICAL_CONVEYOR, ConveyanceBlocks.DOWN_VERTICAL_FAST_CONVEYOR, ConveyanceBlocks.DOWN_VERTICAL_EXPRESS_CONVEYOR);

    public static void init() {
        // NO-OP
    }

    private static <T extends BlockEntity> BlockEntityType<T> register(String name, Supplier<T> blockEntity, Block... block) {
        return Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(Conveyance.MODID, name), BlockEntityType.Builder.create(blockEntity, block).build(null));
    }
}
