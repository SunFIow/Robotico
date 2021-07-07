package net.minecraft.nbt.types;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import net.minecraft.nbt.INBTType;
import net.minecraft.nbt.NBTSizeTracker;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

public class LongNBT extends NumberNBT {
	public static final INBTType<LongNBT> TYPE = new INBTType<LongNBT>() {
		@Override
		public LongNBT readNBT(DataInput input, int depth, NBTSizeTracker accounter) throws IOException {
			accounter.read(128L);
			return LongNBT.valueOf(input.readLong());
		}

		@Override
		public String getName() {
			return "LONG";
		}

		@Override
		public String getTagName() {
			return "TAG_Long";
		}

		@Override
		public boolean isPrimitive() {
			return true;
		}
	};
	private final long data;

	private LongNBT(long data) {
		this.data = data;
	}

	public static LongNBT valueOf(long value) {
		return value >= -128L && value <= 1024L ? LongNBT.Cache.CACHE[(int) value + 128] : new LongNBT(value);
	}

	/**
	 * Write the actual data contents of the tag, implemented in NBT extension classes
	 */
	@Override
	public void write(DataOutput output) throws IOException {
		output.writeLong(this.data);
	}

	/**
	 * Gets the type byte for the tag.
	 */
	@Override
	public byte getId() {
		return 4;
	}

	@Override
	public INBTType<LongNBT> getType() {
		return TYPE;
	}

	@Override
	public String toString() {
		return this.data + "L";
	}

	/**
	 * Creates a clone of the tag.
	 */
	@Override
	public LongNBT copy() {
		return this;
	}

	@Override
	public boolean equals(Object p_equals_1_) {
		if (this == p_equals_1_) {
			return true;
		} else {
			return p_equals_1_ instanceof LongNBT && this.data == ((LongNBT) p_equals_1_).data;
		}
	}

	@Override
	public int hashCode() {
		return (int) (this.data ^ this.data >>> 32);
	}

	@Override
	public ITextComponent toFormattedComponent(String indentation, int indentDepth) {
		ITextComponent itextcomponent = (new StringTextComponent("L")).mergeStyle(SYNTAX_HIGHLIGHTING_NUMBER_TYPE);
		return (new StringTextComponent(String.valueOf(this.data))).append(itextcomponent).mergeStyle(SYNTAX_HIGHLIGHTING_NUMBER);
	}

	@Override
	public long getLong() {
		return this.data;
	}

	@Override
	public int getInt() {
		return (int) (this.data & -1L);
	}

	@Override
	public short getShort() {
		return (short) ((int) (this.data & 65535L));
	}

	@Override
	public byte getByte() {
		return (byte) ((int) (this.data & 255L));
	}

	@Override
	public double getDouble() {
		return this.data;
	}

	@Override
	public float getFloat() {
		return this.data;
	}

	@Override
	public Number getAsNumber() {
		return this.data;
	}

	static class Cache {
		static final LongNBT[] CACHE = new LongNBT[1153];

		static {
			for (int i = 0; i < CACHE.length; ++i) {
				CACHE[i] = new LongNBT(-128 + i);
			}

		}
	}
}
