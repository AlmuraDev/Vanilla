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
package org.spout.vanilla.plugin.component.misc;

import java.util.HashMap;

import org.spout.api.component.type.EntityComponent;

import org.spout.vanilla.api.data.Difficulty;

import org.spout.vanilla.plugin.data.Damage;

/**
 * Component that contains the amount of damage this entity does.
 */
public class DamageComponent extends EntityComponent {
	private HashMap<Difficulty, Damage> damageList = new HashMap<Difficulty, Damage>();

	public DamageComponent() {
		for (Difficulty difficulty : Difficulty.values()) {
			damageList.put(difficulty, new Damage());
		}
	}

	/**
	 * Get the damage level depending of the difficulty level.
	 * @param difficulty The difficulty level
	 * @return The {@link Damage} associated with the difficulty.
	 */
	public Damage getDamageLevel(Difficulty difficulty) {
		return damageList.get(difficulty);
	}
}
