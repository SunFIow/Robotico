package com.sunflow.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.UUID;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.mojang.bridge.game.GameVersion;
import com.sunflow.logging.LogManager;
import com.sunflow.logging.SunLogger;

import net.minecraft.util.JSONUtils;
import net.minecraft.util.SharedConstants;

public class RoboticoVersion implements GameVersion {
	private static final SunLogger LOGGER = LogManager.getLogger();

	public static final GameVersion GAME_VERSION = new RoboticoVersion();
	private final String id;
	private final String name;
	private final boolean stable;
	private final int worldVersion;
	private final int protocolVersion;
	private final int packVersion;
	private final Date buildTime;
	private final String releaseTarget;

	private RoboticoVersion() {
		this.id = UUID.randomUUID().toString().replaceAll("-", "");
		this.name = "0.0.1";
		this.stable = true;
		this.worldVersion = 2584;
		this.protocolVersion = SharedConstants.func_244709_b();
		this.packVersion = 6;
		this.buildTime = new Date();
		this.releaseTarget = "0.0.1";
	}

	private RoboticoVersion(JsonObject json) {
		this.id = JSONUtils.getString(json, "id");
		this.name = JSONUtils.getString(json, "name");
		this.releaseTarget = JSONUtils.getString(json, "release_target");
		this.stable = JSONUtils.getBoolean(json, "stable");
		this.worldVersion = JSONUtils.getInt(json, "world_version");
		this.protocolVersion = JSONUtils.getInt(json, "protocol_version");
		this.packVersion = JSONUtils.getInt(json, "pack_version");
		this.buildTime = Date.from(ZonedDateTime.parse(JSONUtils.getString(json, "build_time")).toInstant());
	}

	/**
	 * Creates a new instance containing game version data from version.json (or fallback data if necessary).
	 * 
	 * For getting data, use {@link SharedConstants#getVersion} instead, as that is cached.
	 */
	public static GameVersion load() {
		try (InputStream inputstream = RoboticoVersion.class.getResourceAsStream("/version.json")) {
			if (inputstream == null) {
				LOGGER.warn("Missing version information!");
				return GAME_VERSION;
			} else {
				RoboticoVersion minecraftversion;
				try (InputStreamReader inputstreamreader = new InputStreamReader(inputstream)) {
					minecraftversion = new RoboticoVersion(JSONUtils.fromJson(inputstreamreader));
				}

				return minecraftversion;
			}
		} catch (JsonParseException | IOException ioexception) {
			throw new IllegalStateException("Game version information is corrupt", ioexception);
		}
	}

	@Override
	public String getId() {
		return this.id;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public String getReleaseTarget() {
		return this.releaseTarget;
	}

	@Override
	public int getWorldVersion() {
		return this.worldVersion;
	}

	@Override
	public int getProtocolVersion() {
		return this.protocolVersion;
	}

	@Override
	public int getPackVersion() {
		return this.packVersion;
	}

	@Override
	public Date getBuildTime() {
		return this.buildTime;
	}

	@Override
	public boolean isStable() {
		return this.stable;
	}
}
