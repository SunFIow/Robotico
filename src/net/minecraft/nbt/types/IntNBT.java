package net.minecraft.nbt.types;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import net.minecraft.nbt.INBTType;
import net.minecraft.nbt.NBTSizeTracker;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

public class IntNBT extends NumberNBT {
	public static final INBTType<IntNBT> TYPE = new INBTType<IntNBT>() {
		@Override
		public IntNBT readNBT(DataInput input, int depth, NBTSizeTracker accounter) throws IOException {
			accounter.read(96L);
			return IntNBT.valueOf(input.readInt());
		}

		@Override
		public String getName() {
			return "INT";
		}

		@Override
		public String getTagName() {
			return "TAG_Int";
		}

		@Override
		public boolean isPrimitive() {
			return true;
		}
	};
	private final int data;

	private IntNBT(int data) {
		this.data = data;
	}

	public static IntNBT valueOf(int dataIn) {
		return dataIn >= -128 && dataIn <= 1024 ? IntNBT.Cache.CACHE[dataIn + 128] : new IntNBT(dataIn);
	}

	/**
	 * Write the actual data contents of the tag, implemented in NBT extension classes
	 */
	@Override
	public void write(DataOutput output) throws IOException {
		output.writeInt(this.data);
	}

	/**
	 * Gets the type byte for the tag.
	 */
	@Override
	public byte getId() {
		return 3;
	}

	@Override
	public INBTType<IntNBT> getType() {
		return TYPE;
	}

	@Override
	public String toString() {
		return String.valueOf(this.data);
	}

	/**
	 * Creates a clone of the tag.
	 */
	@Override
	public IntNBT copy() {
		return this;
	}

	@Override
	public boolean equals(Object p_equals_1_) {
		if (this == p_equals_1_) {
			return true;
		} else {
			return p_equals_1_ instanceof IntNBT && this.data == ((IntNBT) p_equals_1_).data;
		}
	}

	@Override
	public int hashCode() {
		return this.data;
	}

	@Override
	public ITextComponent toFormattedComponent(String indentation, int indentDepth) {
		return (new StringTextComponent(String.valueOf(this.data))).mergeStyle(SYNTAX_HIGHLIGHTING_NUMBER);
	}

	@Override
	public long getLong() {
		return this.data;
	}

	@Override
	public int getInt() {
		return this.data;
	}

	@Override
	public short getShort() {
		return (short) (this.data & '\uffff');
	}

	@Override
	public byte getByte() {
		return (byte) (this.data & 255);
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
		static final IntNBT[] CACHE = new IntNBT[1153];

		static {
			for (int i = 0; i < CACHE.length; ++i) {
				CACHE[i] = new IntNBT(-128 + i);
			}

		}
	}
}
