/*
 * This file is part of Vanilla.
 *
 * Copyright (c) 2011-2012, Spout LLC <http://www.spout.org/>
 * Vanilla is licensed under the Spout License Version 1.
 *
 * Vanilla is free software: you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option)
 * any later version.
 *
 * In addition, 180 days after any changes are published, you can use the
 * software, incorporating those changes, under the terms of the MIT license,
 * as described in the Spout License Version 1.
 *
 * Vanilla is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License for
 * more details.
 *
 * You should have received a copy of the GNU Lesser General Public License,
 * the MIT license and the Spout License Version 1 along with this program.
 * If not, see <http://www.gnu.org/licenses/> for the GNU Lesser General Public
 * License and see <http://spout.in/licensev1> for the full license, including
 * the MIT license.
 */
package org.spout.vanilla.plugin.component.living.passive;

import java.util.Random;

import org.spout.api.entity.Entity;
import org.spout.api.event.player.PlayerInteractEvent.Action;
import org.spout.api.inventory.ItemStack;

import org.spout.vanilla.api.component.Passive;
import org.spout.vanilla.api.inventory.Slot;

import org.spout.vanilla.plugin.VanillaPlugin;
import org.spout.vanilla.plugin.component.living.Living;
import org.spout.vanilla.plugin.component.misc.DropComponent;
import org.spout.vanilla.plugin.component.misc.HealthComponent;
import org.spout.vanilla.plugin.inventory.player.PlayerQuickbar;
import org.spout.vanilla.plugin.material.VanillaMaterials;
import org.spout.vanilla.plugin.protocol.entity.creature.CreatureProtocol;
import org.spout.vanilla.plugin.protocol.entity.creature.CreatureType;
import org.spout.vanilla.plugin.util.PlayerUtil;

/**
 * A component that identifies the entity as a Cow.
 */
public class Cow extends Living implements Passive {
	@Override
	public void onAttached() {
		super.onAttached();
		getOwner().getNetwork().setEntityProtocol(VanillaPlugin.VANILLA_PROTOCOL_ID, new CreatureProtocol(CreatureType.COW));
		DropComponent dropComponent = getOwner().add(DropComponent.class);
		Random random = getRandom();
		dropComponent.addDrop(new ItemStack(VanillaMaterials.RAW_BEEF, random.nextInt(2) + 1));
		dropComponent.addDrop(new ItemStack(VanillaMaterials.LEATHER, random.nextInt(2)));
		dropComponent.addXpDrop((short) (getRandom().nextInt(3) + 1));
		if (getAttachedCount() == 1) {
			getOwner().add(HealthComponent.class).setSpawnHealth(10);
		}
	}

	@Override
	public void onInteract(Action action, Entity source) {
		if (Action.RIGHT_CLICK.equals(action)) {
			PlayerQuickbar playerQuickbar = PlayerUtil.getQuickbar(source);
			if (playerQuickbar == null) {
				return;
			}
			Slot selected = playerQuickbar.getSelectedSlot();
			if (selected.get() != null && selected.get().equalsIgnoreSize(new ItemStack(VanillaMaterials.BUCKET, 0))) {
				selected.addAmount(-1);
				playerQuickbar.add(new ItemStack(VanillaMaterials.MILK_BUCKET, 1));
			}
		}
	}
}
