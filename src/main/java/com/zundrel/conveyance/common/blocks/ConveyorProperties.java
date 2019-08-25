package com.zundrel.conveyance.common.blocks;

import net.minecraft.state.property.BooleanProperty;

public class ConveyorProperties {
    public static final BooleanProperty FRONT = BooleanProperty.of("front");
    public static final BooleanProperty LEFT = BooleanProperty.of("left");
    public static final BooleanProperty RIGHT = BooleanProperty.of("right");
    public static final BooleanProperty UP = BooleanProperty.of("up");
    public static final BooleanProperty DOWN = BooleanProperty.of("down");
}
