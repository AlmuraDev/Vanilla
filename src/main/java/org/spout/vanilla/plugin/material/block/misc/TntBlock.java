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
package org.spout.vanilla.plugin.material.block.misc;

import org.spout.api.event.Cause;
import org.spout.api.geo.World;
import org.spout.api.geo.cuboid.Block;
import org.spout.api.geo.discrete.Point;
import org.spout.api.material.BlockMaterial;
import org.spout.api.material.block.BlockFace;
import org.spout.api.math.Vector3;

import org.spout.vanilla.api.material.Burnable;
import org.spout.vanilla.api.material.block.redstone.RedstoneTarget;

import org.spout.vanilla.plugin.component.substance.object.Tnt;
import org.spout.vanilla.plugin.material.VanillaMaterials;
import org.spout.vanilla.plugin.material.block.Solid;
import org.spout.vanilla.plugin.resources.VanillaMaterialModels;
import org.spout.vanilla.plugin.util.RedstoneUtil;

public class TntBlock extends Solid implements RedstoneTarget, Burnable {
	public TntBlock(String name, int id) {
		super(name, id, VanillaMaterialModels.TNT_BLOCK);
		this.setHardness(0.0F).setResistance(0.0F).setOpacity((byte) 1);
	}

	@Override
	public boolean hasPhysics() {
		return true;
	}

	@Override
	public int getBurnPower() {
		return 15;
	}

	@Override
	public int getCombustChance() {
		return 100;
	}

	@Override
	public boolean canSupport(BlockMaterial mat, BlockFace face) {
		return mat.equals(VanillaMaterials.FIRE);
	}

	@Override
	public void onIgnite(Block block, Cause<?> cause) {
		// spawn a primed TntBlock
		Point pos = block.getPosition();
		World world = pos.getWorld();
		Tnt tnt = world.createEntity(pos, Tnt.class).add(Tnt.class);
		double v = 0.5d;
		tnt.getOwner().getScene().impulse(new Vector3(v,v,v)); //TODO: Fix this
		world.spawnEntity(tnt.getOwner());
		block.setMaterial(VanillaMaterials.AIR, cause);
	}

	@Override
	public void onUpdate(BlockMaterial oldMaterial, Block block) {
		super.onUpdate(oldMaterial, block);
		Block powerSource = RedstoneUtil.getReceivingPowerLocation(block);
		if (powerSource != null) {
			this.onIgnite(block, toCause(powerSource));
		}
	}

	@Override
	public boolean isReceivingPower(Block block) {
		return RedstoneUtil.isReceivingPower(block);
	}
}
