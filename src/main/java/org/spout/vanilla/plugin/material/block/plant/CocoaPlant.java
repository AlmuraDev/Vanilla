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
package org.spout.vanilla.plugin.material.block.plant;

import java.util.Random;
import java.util.Set;

import org.spout.api.entity.Entity;
import org.spout.api.event.Cause;
import org.spout.api.event.player.PlayerInteractEvent.Action;
import org.spout.api.geo.cuboid.Block;
import org.spout.api.material.DynamicMaterial;
import org.spout.api.material.block.BlockFace;
import org.spout.api.material.block.BlockFaces;
import org.spout.api.material.range.EffectRange;
import org.spout.api.math.MathHelper;
import org.spout.api.util.flag.Flag;

import org.spout.vanilla.api.data.GameMode;
import org.spout.vanilla.api.inventory.Slot;
import org.spout.vanilla.api.material.InitializableMaterial;
import org.spout.vanilla.api.material.block.Growing;
import org.spout.vanilla.api.material.block.Plant;

import org.spout.vanilla.plugin.data.VanillaData;
import org.spout.vanilla.plugin.data.drops.flag.BlockFlags;
import org.spout.vanilla.plugin.material.block.attachable.AbstractAttachable;
import org.spout.vanilla.plugin.material.block.solid.Log;
import org.spout.vanilla.plugin.material.item.misc.Dye;
import org.spout.vanilla.plugin.util.PlayerUtil;

public class CocoaPlant extends AbstractAttachable implements Plant, Growing, DynamicMaterial, InitializableMaterial {
	private static final int DIRECTION_MASK = 0x3;
	private static final int GROWTH_MASK = 0xC;

	public CocoaPlant(String name, int id) {
		super(name, id, null);
		this.setAttachable(BlockFaces.NESW);
	}

	@Override
	public void initialize() {
		getDrops().DEFAULT.clear();
		getDrops().DEFAULT.add(Dye.COCOA_BEANS, 1).addFlags(BlockFlags.FULLY_GROWN.NOT);
		getDrops().DEFAULT.add(Dye.COCOA_BEANS, 3).addFlags(BlockFlags.FULLY_GROWN);
	}

	@Override
	public boolean canAttachTo(Block block, BlockFace face) {
		return super.canAttachTo(block, face) && block.isMaterial(Log.JUNGLE);
	}

	@Override
	public void setAttachedFace(Block block, BlockFace attachedFace, Cause<?> cause) {
		block.setDataField(DIRECTION_MASK, BlockFaces.WNES.indexOf(attachedFace, 0));
	}

	@Override
	public BlockFace getAttachedFace(short data) {
		return BlockFaces.WNES.get(data & DIRECTION_MASK);
	}

	@Override
	public int getMinimumLightToGrow() {
		return 0;
	}

	@Override
	public int getGrowthStageCount() {
		return 3;
	}

	@Override
	public void getBlockFlags(Block block, Set<Flag> flags) {
		super.getBlockFlags(block, flags);
		if (this.isFullyGrown(block)) {
			flags.add(BlockFlags.FULLY_GROWN);
		}
	}

	@Override
	public int getGrowthStage(Block block) {
		return block.getDataField(GROWTH_MASK);
	}

	@Override
	public void setGrowthStage(Block block, int stage) {
		block.setDataField(GROWTH_MASK, stage);
	}

	@Override
	public boolean isFullyGrown(Block block) {
		return getGrowthStage(block) >= 2;
	}

	@Override
	public EffectRange getDynamicRange() {
		return EffectRange.NEIGHBORS;
	}

	@Override
	public void onFirstUpdate(Block b, long currentTime) {
		b.dynamicUpdate(getGrowthTime(b) + currentTime, true);
	}

	@Override
	public void onDynamicUpdate(Block block, long updateTime, int data) {
		if (MathHelper.getRandom().nextInt(5) != 0) {
			block.dynamicUpdate(updateTime + getGrowthTime(block), true);
			return;
		}

		int growthStage = getGrowthStage(block);

		if (growthStage < getGrowthStageCount() - 1) {
			setGrowthStage(block, ++growthStage);
		}

		if (growthStage < getGrowthStageCount() - 1) {
			block.dynamicUpdate(updateTime + getGrowthTime(block), true);
		}
	}

	private long getGrowthTime(Block block) {
		return 60000L + MathHelper.getRandom().nextInt(60000);
	}

	@Override
	public void onInteractBy(Entity entity, Block block, Action type, BlockFace clickedFace) {
		Slot inv = PlayerUtil.getHeldSlot(entity);
		if (inv != null && inv.get() != null && inv.get().isMaterial(Dye.BONE_MEAL) && type == Action.RIGHT_CLICK) {
			if (!isFullyGrown(block)) {
				if (entity.getData().get(VanillaData.GAMEMODE).equals(GameMode.SURVIVAL)) {
					inv.addAmount(-1);
				}
				setGrowthStage(block, 2);
			}
		}
	}
}
