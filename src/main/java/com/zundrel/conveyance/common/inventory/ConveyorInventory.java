package com.zundrel.conveyance.common.inventory;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DefaultedList;

/**
     * A simple {@code Inventory} implementation with only default methods + an item list getter.
     *
     * Originally by Juuz
     */
    public interface ConveyorInventory extends Inventory {
        /**
         * Gets the item list of this inventory.
         * Must return the same instance every time it's called.
         */
        DefaultedList<ItemStack> getItems();
        // Creation
        /**
         * Creates an inventory from the item list.
         */
        static ConveyorInventory of(DefaultedList<ItemStack> items) {
            return () -> items;
        }
        /**
         * Creates a new inventory with the size.
         */
        static ConveyorInventory ofSize(int size) {
            return of(DefaultedList.ofSize(size, ItemStack.EMPTY));
        }
        // Inventory
        /**
         * Returns the inventory size.
         */
        @Override
        default int getInvSize() {
            return getItems().size();
        }
        /**
         * @return true if this inventory has only empty stacks, false otherwise
         */
        @Override
        default boolean isInvEmpty() {
            for (int i = 0; i < getInvSize(); i++) {
                ItemStack stack = getInvStack(i);
                if (!stack.isEmpty()) {
                    return false;
                }
            }
            return true;
        }

        default boolean isEmpty() {
            return isInvEmpty();
        }

        /**
         * Gets the item in the slot.
         */
        @Override
        default ItemStack getInvStack(int slot) {
            return getItems().get(slot);
        }

        default ItemStack getStack() {
            return getInvStack( 0);
        }

        /**
         * Takes a stack of the size from the slot.
         * <p>(default implementation) If there are less items in the slot than what are requested,
         * takes all items in that slot.
         */
        @Override
        default ItemStack takeInvStack(int slot, int count) {
            ItemStack result = Inventories.splitStack(getItems(), slot, count);
            if (!result.isEmpty()) {
                markDirty();
            }
            return result;
        }
        /**
         * Removes the current stack in the {@code slot} and returns it.
         */
        @Override
        default ItemStack removeInvStack(int slot) {
            ItemStack stack = Inventories.removeStack(getItems(), slot);
            markDirty();
            return stack;
        }

        default ItemStack removeStack() {
            return removeInvStack(0);
        }

        /**
         * Replaces the current stack in the {@code slot} with the provided stack.
         * <p>If the stack is too big for this inventory ({@link Inventory#getInvMaxStackAmount()}),
         * it gets resized to this inventory's maximum amount.
         */
        @Override
        default void setInvStack(int slot, ItemStack stack) {
            getItems().set(slot, stack);
            if (stack.getCount() > getInvMaxStackAmount()) {
                stack.setCount(getInvMaxStackAmount());
            }
            markDirty();
        }

        default void setStack(ItemStack stack) {
            setInvStack(0, stack);
        }

        /**
         * Clears {@linkplain #getItems() the item list}}.
         */
        @Override
        default void clear() {
            getItems().clear();
            markDirty();
        }
        @Override
        default void markDirty() {
            // Override if you want behavior.
        }
        @Override
        default boolean canPlayerUseInv(PlayerEntity player) {
            return true;
        }
    }

