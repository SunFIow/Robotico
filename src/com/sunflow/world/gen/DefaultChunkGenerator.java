package com.sunflow.world.gen;

import com.sunflow.tile.TileState;
import com.sunflow.tile.Tiles;
import com.sunflow.world.World;
import com.sunflow.world.biome.provider.BiomeProvider;
import com.sunflow.world.chunk.Chunk;
import com.sunflow.world.chunk.ChunkHolder;
import com.sunflow.world.chunk.ChunkStatus;
import com.sunflow.world.chunk.IChunk;

public class DefaultChunkGenerator extends ChunkGenerator<DefaultChunkGenerator.Settings> {

	public DefaultChunkGenerator(World world, BiomeProvider biomeProvider, DefaultChunkGenerator.Settings settings) { super(world, biomeProvider, settings); }

	@Override
	public IChunk generateChunk(ChunkHolder chunkHolder, ChunkStatus status) {
		Chunk chunk = new Chunk();

		for (int x = 0; x < Chunk.SIZE_X; x++) for (int y = 0; y < Chunk.SIZE_Y; y++) {
			TileState state = new TileState();
			state.setTile(Tiles.DEFAULT);
			chunk.setTileStateAt(x, y, state);
		}

		return chunk;
	}

	public static class Settings extends ChunkGenerationSettings {}
}
