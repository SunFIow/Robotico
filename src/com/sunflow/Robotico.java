package com.sunflow;

import java.awt.event.KeyEvent;

import com.sunflow.Settings.ScreenType;
import com.sunflow.engine.eventsystem.events.KeyInputEvent.KeyPressedEvent;
import com.sunflow.game.GameBase;
import com.sunflow.tile.ImageTile;
import com.sunflow.tile.Tile;
import com.sunflow.world.World;
import com.sunflow.world.biome.provider.BiomeProviderType;
import com.sunflow.world.chunk.ChunkProvider;
import com.sunflow.world.gen.ChunkGeneratorType;

public class Robotico extends GameBase {
	public static void main(String[] args) {
		settings.autostart(false).screentype(ScreenType.OPENGL);
		new Robotico().start();
	}

	World world;
	int w, h;

	boolean showTileBorder;

	@Override
	public void setup() {
		createCanvas(480, 480);
//		syncMode(ASYNC);
//		tickRate(20);

		w = 8;
		h = 8;
		ImageTile.init(this::loadImage);

//		this.world = new World(world -> new ChunkProvider(world,
//				new SingleTileGenerator(world,
//						new SingleBiomeProvider(new SingleBiomeProvider.Settings(world.getWorldInfo())),
//						Tiles.DEFAULT)));
		this.world = new World(world -> new ChunkProvider(world,
				ChunkGeneratorType.DEFAULT.create(world,
						BiomeProviderType.SINGLE.create(BiomeProviderType.SINGLE.createSettings(world.getWorldInfo())),
						ChunkGeneratorType.DEFAULT.createSettings())));
	}

	@Override
	public void update() {}

	@Override
	public void onKeyPressed(KeyPressedEvent event) {
		System.out.println("onKeyPressed(KeyPressedEvent event): " + event);
	}

	@Override
	public void keyPressed(KeyEvent e) {
		System.out.println("keyPressed(KeyEvent e): " + e);
	}

	@Override
	public boolean keyPressed() {
		System.out.println("keyPressed(): " + this.key());
		return false;
	}

	float c;

	@Override
	public void draw() {
		background(25);
//		System.out.printf("%f ,%f ,%f\n", tElapsedTime, fElapsedTime, fElapsedTime - tElapsedTime);

		if (isKeyDown(F3) && isKeyPressed('F')) {
			showTileBorder = !showTileBorder;
		}
		push();
		scale(width / w, height / h);
		for (int x = 0; x < w; x++) {
			for (int y = 0; y < h; y++) {
				Tile tile = world.getTileState(x, y).getTile();
				tile.show(this, x, y);
				if (!showTileBorder) continue;
				noFill();
				stroke(255, 0, 0);
				strokeWeight(0.05f);
				rect(x, y, 1, 1);
			}
		}
		pop();

		fill(255);
		c += fMultiplier * 2;
		ellipse(c % width, 200, 20);
	}
}
