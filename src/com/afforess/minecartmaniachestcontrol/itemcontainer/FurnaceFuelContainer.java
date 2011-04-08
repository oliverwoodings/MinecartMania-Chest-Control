package com.afforess.minecartmaniachestcontrol.itemcontainer;

import com.afforess.minecartmaniacore.AbstractItem;
import com.afforess.minecartmaniacore.Item;
import com.afforess.minecartmaniacore.MinecartManiaFurnace;
import com.afforess.minecartmaniacore.MinecartManiaInventory;
import com.afforess.minecartmaniacore.utils.DirectionUtils.CompassDirection;

public class FurnaceFuelContainer extends GenericItemContainer implements ItemContainer{
	MinecartManiaFurnace furnace;
	private static final int SLOT = 1;

	public FurnaceFuelContainer(MinecartManiaFurnace furnace, String fuel, CompassDirection direction) {
		super(fuel, direction);
		this.furnace = furnace;
		if (fuel.toLowerCase().contains("fuel")) {
			String[] split = fuel.split(":");
			fuel = "";
			for (String s : split) {
				if (!s.toLowerCase().contains("fuel")) {
					fuel += s + ":";
				}
			}
		}
		this.line = fuel;
	}

	@Override
	public void doCollection(MinecartManiaInventory withdraw) {
		for (CompassDirection direction : directions) {
			AbstractItem[] list = getItemList(direction);
			for (AbstractItem item : list) {
				if (item != null) {
					if (item.isInfinite()) {
						item.setAmount(64);
					}
					short data = (short) (item.hasData() ? item.getData() : -1);
					//does not match the item already in the slot, continue
					if (furnace.getItem(SLOT) != null && !item.equals(Item.getItem(furnace.getItem(SLOT)))) {
						continue;
					}
					int toAdd = item.getAmount();
					if (furnace.getItem(SLOT) != null) {
						toAdd -= furnace.getItem(SLOT).getAmount();
						item.setAmount(furnace.getItem(SLOT).getAmount() + toAdd);
					}
					if (withdraw.contains(item.type()) && withdraw.canRemoveItem(item.getId(), toAdd, data)) {
						if (furnace.canAddItem(item.toItemStack())) {
							withdraw.removeItem(item.getId(), toAdd, data);
							furnace.setItem(SLOT, item.toItemStack());
							return;
						}
					}
					
				}
			}
		}
	}
}