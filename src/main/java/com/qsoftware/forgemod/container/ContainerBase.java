package com.qsoftware.forgemod.container;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;

/**
 * Base container class.
 *
 * @author Qboi123
 */
public abstract class ContainerBase extends Container {

    protected IInventory inventory;
    protected IInventory playerInventory;

    public ContainerBase(ContainerType type, int id, IInventory playerInventory, IInventory inventory) {
        super(type, id);
        this.playerInventory = playerInventory;
        this.inventory = inventory;
    }

    protected void addInvSlots() {
        if (playerInventory != null) {
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 9; j++) {
                    addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18 + getInvOffset()));
                }
            }

            for (int k = 0; k < 9; k++) {
                addSlot(new Slot(playerInventory, k, 8 + k * 18, 142 + getInvOffset()));
            }
        }
    }

    public int getInvOffset() {
        return 0;
    }

    public abstract int getInventorySize();

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return true;
    }

    @Nullable
    public IInventory getPlayerInventory() {
        return playerInventory;
    }

    @Override
    public ItemStack transferStackInSlot(PlayerEntity playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = inventorySlots.get(index);

        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (index < getInventorySize()) {
                if (!this.mergeItemStack(itemstack1, getInventorySize(), inventorySlots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.mergeItemStack(itemstack1, 0, getInventorySize(), false)) {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty()) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }
        }
        return itemstack;
    }

}
