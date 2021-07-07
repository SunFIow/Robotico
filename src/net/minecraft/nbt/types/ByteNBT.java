package net.minecraft.nbt.types;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import net.minecraft.nbt.INBTType;
import net.minecraft.nbt.NBTSizeTracker;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

public class ByteNBT extends NumberNBT {
	public static final INBTType<ByteNBT> TYPE = new INBTType<ByteNBT>() {
		@Override
		public ByteNBT readNBT(DataInput input, int depth, NBTSizeTracker accounter) throws IOException {
			accounter.read(72L);
			return ByteNBT.valueOf(input.readByte());
		}

		@Override
		public String getName() {
			return "BYTE";
		}

		@Override
		public String getTagName() {
			return "TAG_Byte";
		}

		@Override
		public boolean isPrimitive() {
			return true;
		}
	};
	public static final ByteNBT ZERO = valueOf((byte) 0);
	public static final ByteNBT ONE = valueOf((byte) 1);
	private final byte data;

	private ByteNBT(byte data) {
		this.data = data;
	}

	public static ByteNBT valueOf(byte byteIn) {
		return ByteNBT.Cache.CACHE[128 + byteIn];
	}

	public static ByteNBT valueOf(boolean one) {
		return one ? ONE : ZERO;
	}

	/**
	 * Write the actual data contents of the tag, implemented in NBT extension classes
	 */
	@Override
	public void write(DataOutput output) throws IOException {
		output.writeByte(this.data);
	}

	/**
	 * Gets the type byte for the tag.
	 */
	@Override
	public byte getId() {
		return 1;
	}

	@Override
	public INBTType<ByteNBT> getType() {
		return TYPE;
	}

	@Override
	public String toString() {
		return this.data + "b";
	}

	/**
	 * Creates a clone of the tag.
	 */
	@Override
	public ByteNBT copy() {
		return this;
	}

	@Override
	public boolean equals(Object p_equals_1_) {
		if (this == p_equals_1_) {
			return true;
		} else {
			return p_equals_1_ instanceof ByteNBT && this.data == ((ByteNBT) p_equals_1_).data;
		}
	}

	@Override
	public int hashCode() {
		return this.data;
	}

	@Override
	public ITextComponent toFormattedComponent(String indentation, int indentDepth) {
		ITextComponent itextcomponent = (new StringTextComponent("b")).mergeStyle(SYNTAX_HIGHLIGHTING_NUMBER_TYPE);
		return (new StringTextComponent(String.valueOf(this.data))).append(itextcomponent).mergeStyle(SYNTAX_HIGHLIGHTING_NUMBER);
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
		return this.data;
	}

	@Override
	public byte getByte() {
		return this.data;
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
		private static final ByteNBT[] CACHE = new ByteNBT[256];

		static {
			for (int i = 0; i < CACHE.length; ++i) {
				CACHE[i] = new ByteNBT((byte) (i - 128));
			}

		}
	}
}
