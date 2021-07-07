package net.minecraft.util;

import java.time.Duration;

import com.mojang.bridge.game.GameVersion;
import com.sunflow.util.RoboticoVersion;

public class SharedConstants {
	public static final long field_240855_b_ = Duration.ofMillis(300L).toNanos();
	public static boolean useDatafixers = true;
	public static boolean developmentMode;
	public static final char[] ILLEGAL_FILE_CHARACTERS = new char[] { '/', '\n', '\r', '\t', '\u0000', '\f', '`', '?', '*', '\\', '<', '>', '|', '"', ':' };
	private static GameVersion version;

	/**
	 * Checks if the given character is allowed to be put into chat.
	 */
	public static boolean isAllowedCharacter(char character) {
		return character != 167 && character >= ' ' && character != 127;
	}

	/**
	 * Filter a string, keeping only characters for which {@link #isAllowedCharacter(char)} returns true.
	 * 
	 * Note that this method strips line breaks, as {@link #isAllowedCharacter(char)} returns false for those.
	 * 
	 * @return A filtered version of the input string
	 */
	public static String filterAllowedCharacters(String input) {
		StringBuilder stringbuilder = new StringBuilder();

		for (char c0 : input.toCharArray()) {
			if (isAllowedCharacter(c0)) {
				stringbuilder.append(c0);
			}
		}

		return stringbuilder.toString();
	}

	public static GameVersion getVersion() {
		if (version == null) {
			version = RoboticoVersion.load();
		}

		return version;
	}

	public static int func_244709_b() {
		return 754;
	}

}
