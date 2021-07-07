package net.minecraft.nbt.types;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Objects;

import net.minecraft.nbt.INBT;
import net.minecraft.nbt.INBTType;
import net.minecraft.nbt.NBTSizeTracker;
import net.minecraft.util.text.ITextComponent;

public class StringNBT implements INBT {
	public static final INBTType<StringNBT> TYPE = new INBTType<StringNBT>() {
		@Override
		public StringNBT readNBT(DataInput input, int depth, NBTSizeTracker accounter) throws IOException {
			accounter.read(288L);
			String s = input.readUTF();
			accounter.readUTF(s);
			return StringNBT.valueOf(s);
		}

		@Override
		public String getName() {
			return "STRING";
		}

		@Override
		public String getTagName() {
			return "TAG_String";
		}

		@Override
		public boolean isPrimitive() {
			return true;
		}
	};
	private static final StringNBT EMPTY_STRING = new StringNBT("");
	private final String data;

	private StringNBT(String data) {
		Objects.requireNonNull(data, "Null string not allowed");
		this.data = data;
	}

	public static StringNBT valueOf(String value) {
		return value.isEmpty() ? EMPTY_STRING : new StringNBT(value);
	}

	/**
	 * Write the actual data contents of the tag, implemented in NBT extension classes
	 */
	@Override
	public void write(DataOutput output) throws IOException {
		output.writeUTF(this.data);
	}

	/**
	 * Gets the type byte for the tag.
	 */
	@Override
	public byte getId() {
		return 8;
	}

	@Override
	public INBTType<StringNBT> getType() {
		return TYPE;
	}

	@Override
	public String toString() {
		return quoteAndEscape(this.data);
	}

	/**
	 * Creates a clone of the tag.
	 */
	@Override
	public StringNBT copy() {
		return this;
	}

	@Override
	public boolean equals(Object p_equals_1_) {
		if (this == p_equals_1_) {
			return true;
		} else {
			return p_equals_1_ instanceof StringNBT && Objects.equals(this.data, ((StringNBT) p_equals_1_).data);
		}
	}

	@Override
	public int hashCode() {
		return this.data.hashCode();
	}

	@Override
	public String getString() {
		return this.data;
	}

	public static String quoteAndEscape(String name) {
		StringBuilder stringbuilder = new StringBuilder(" ");
		char c0 = 0;

		for (int i = 0; i < name.length(); ++i) {
			char c1 = name.charAt(i);
			if (c1 == '\\') {
				stringbuilder.append('\\');
			} else if (c1 == '"' || c1 == '\'') {
				if (c0 == 0) {
					c0 = (char) (c1 == '"' ? 39 : 34);
				}

				if (c0 == c1) {
					stringbuilder.append('\\');
				}
			}

			stringbuilder.append(c1);
		}

		if (c0 == 0) {
			c0 = '"';
		}

		stringbuilder.setCharAt(0, c0);
		stringbuilder.append(c0);
		return stringbuilder.toString();
	}

	@Override
	public ITextComponent toFormattedComponent(String indentation, int indentDepth) {
		return null;
	}
}
