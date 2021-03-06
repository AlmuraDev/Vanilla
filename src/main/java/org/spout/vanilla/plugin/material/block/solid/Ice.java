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
package org.spout.vanilla.plugin.material.block.solid;

import java.util.Random;

import org.spout.api.event.Cause;
import org.spout.api.geo.cuboid.Block;
import org.spout.api.material.BlockMaterial;
import org.spout.api.material.block.BlockFace;
import org.spout.api.material.block.BlockFaces;
import org.spout.api.material.range.CubicEffectRange;
import org.spout.api.material.range.EffectRange;
import org.spout.api.math.MathHelper;

import org.spout.vanilla.api.data.Climate;
import org.spout.vanilla.api.event.cause.PlayerBreakCause;
import org.spout.vanilla.api.material.InitializableMaterial;

import org.spout.vanilla.plugin.component.living.neutral.Human;
import org.spout.vanilla.plugin.material.VanillaMaterials;
import org.spout.vanilla.plugin.material.block.SpreadingSolid;
import org.spout.vanilla.plugin.resources.VanillaMaterialModels;
import org.spout.vanilla.plugin.world.generator.nether.NetherGenerator;

public class Ice extends SpreadingSolid implements InitializableMaterial {
	private static final byte MIN_MELT_LIGHT = 11;
	private static final EffectRange ICE_SPREAD_RANGE = new CubicEffectRange(1);

	public Ice(String name, int id) {
		super(name, id, VanillaMaterialModels.ICE);
		this.setHardness(0.5F).setResistance(0.8F).setOcclusion((short) 0, BlockFaces.NONE).setOpacity((byte) 2);
		this.getDrops().clear();
	}

	@Override
	public void initialize() {
		setReplacedMaterial(VanillaMaterials.WATER);
		getDrops().DEFAULT.clear();
	}

	@Override
	public boolean canSupport(BlockMaterial material, BlockFace face) {
		return false;
	}

	@Override
	public boolean onDestroy(Block block, Cause<?> cause) {
		if (!(block.getWorld().getGenerator() instanceof NetherGenerator) && block.translate(BlockFace.BOTTOM).getMaterial() != VanillaMaterials.AIR) {
			if (cause instanceof PlayerBreakCause) {
				if (!((PlayerBreakCause) cause).getSource().get(Human.class).isCreative()) {
					return onDecay(block, cause);
				}
			} else {
				return onDecay(block, cause);
			}
		}
		return super.onDestroy(block, cause);
	}

	@Override
	public EffectRange getSpreadRange() {
		return ICE_SPREAD_RANGE;
	}

	@Override
	public int getMinimumLightToSpread() {
		return 0;
	}

	@Override
	public boolean canDecayAt(Block block) {
		if ((block.getBlockLight() + this.getOpacity() + 1) > MIN_MELT_LIGHT) {
			return true;
		}
		if (Climate.get(block).isMelting()) {
			if ((block.getSkyLight() + this.getOpacity() + 1) > MIN_MELT_LIGHT) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean canSpreadFrom(Block block) {
		return Climate.get(block).isFreezing();
	}

	@Override
	public boolean canSpreadTo(Block from, Block to) {
		return super.canSpreadTo(from, to) && VanillaMaterials.WATER.isSource(to) && to.isAtSurface();
	}

	@Override
	public long getSpreadingTime(Block b) {
		return 120000L + MathHelper.getRandom().nextInt(60000) * 5;
	}

	@Override
	public boolean isFaceRendered(BlockFace face, BlockMaterial neighbor) {
		return neighbor != this && !neighbor.isOpaque();
	}
}
