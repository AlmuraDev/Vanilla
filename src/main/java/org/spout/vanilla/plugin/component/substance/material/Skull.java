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
package org.spout.vanilla.plugin.component.substance.material;

import org.spout.vanilla.api.component.substance.material.VanillaBlockComponent;
import org.spout.vanilla.plugin.data.VanillaData;

/**
 * Component that represents a Skull in the world.
 */
public class Skull extends VanillaBlockComponent {
	/**
	 * Retrieve the rotation of the Skull.
	 * @return The rotation.
	 */
	public float getRotation() {
		return getData().get(VanillaData.SKULL_ROTATION);
	}

	/**
	 * Sets the rotation of the skull.
	 * @param rotation The rotation.
	 */
	public void setRotation(float rotation) {
		getData().put(VanillaData.SKULL_ROTATION, rotation);
	}
}
