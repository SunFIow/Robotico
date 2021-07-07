package net.minecraft.nbt;

import java.util.UUID;

import javax.annotation.Nullable;

import com.google.common.annotations.VisibleForTesting;
import com.sunflow.logging.LogManager;
import com.sunflow.logging.SunLogger;
import com.sunflow.util.math.TilePos;

import net.minecraft.nbt.types.CompoundNBT;
import net.minecraft.nbt.types.IntArrayNBT;
import net.minecraft.nbt.types.ListNBT;
import net.minecraft.util.UUIDCodec;

public final class NBTUtil {
	private static final SunLogger LOGGER = LogManager.getLogger();

	@VisibleForTesting
	public static boolean areNBTEquals(@Nullable INBT nbt1, @Nullable INBT nbt2, boolean compareTagList) {
		if (nbt1 == nbt2) {
			return true;
		} else if (nbt1 == null) {
			return true;
		} else if (nbt2 == null) {
			return false;
		} else if (!nbt1.getClass().equals(nbt2.getClass())) {
			return false;
		} else if (nbt1 instanceof CompoundNBT) {
			CompoundNBT compoundnbt = (CompoundNBT) nbt1;
			CompoundNBT compoundnbt1 = (CompoundNBT) nbt2;

			for (String s : compoundnbt.keySet()) {
				INBT inbt1 = compoundnbt.get(s);
				if (!areNBTEquals(inbt1, compoundnbt1.get(s), compareTagList)) {
					return false;
				}
			}

			return true;
		} else if (nbt1 instanceof ListNBT && compareTagList) {
			ListNBT listnbt = (ListNBT) nbt1;
			ListNBT listnbt1 = (ListNBT) nbt2;
			if (listnbt.isEmpty()) {
				return listnbt1.isEmpty();
			} else {
				for (int i = 0; i < listnbt.size(); ++i) {
					INBT inbt = listnbt.get(i);
					boolean flag = false;

					for (int j = 0; j < listnbt1.size(); ++j) {
						if (areNBTEquals(inbt, listnbt1.get(j), compareTagList)) {
							flag = true;
							break;
						}
					}

					if (!flag) {
						return false;
					}
				}

				return true;
			}
		} else {
			return nbt1.equals(nbt2);
		}
	}

	public static IntArrayNBT func_240626_a_(UUID p_240626_0_) {
		return new IntArrayNBT(UUIDCodec.encodeUUID(p_240626_0_));
	}

	/**
	 * Reads a UUID from the passed NBTTagCompound.
	 */
	public static UUID readUniqueId(INBT tag) {
		if (tag.getType() != IntArrayNBT.TYPE) {
			throw new IllegalArgumentException("Expected UUID-Tag to be of type " + IntArrayNBT.TYPE.getName() + ", but found " + tag.getType().getName() + ".");
		} else {
			int[] aint = ((IntArrayNBT) tag).getIntArray();
			if (aint.length != 4) {
				throw new IllegalArgumentException("Expected UUID-Array to be of length 4, but found " + aint.length + ".");
			} else {
				return UUIDCodec.decodeUUID(aint);
			}
		}
	}

	/**
	 * Creates a TilePos object from the data stored in the passed NBTTagCompound.
	 */
	public static TilePos readTilePos(CompoundNBT tag) {
		return new TilePos(tag.getInt("X"), tag.getInt("Y"));
	}

	/**
	 * Creates a new NBTTagCompound from a TilePos.
	 */
	public static CompoundNBT writeTilePos(TilePos pos) {
		CompoundNBT compoundnbt = new CompoundNBT();
		compoundnbt.putInt("X", pos.getX());
		compoundnbt.putInt("Y", pos.getY());
		return compoundnbt;
	}

}
