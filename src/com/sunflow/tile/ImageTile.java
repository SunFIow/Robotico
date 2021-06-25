package com.sunflow.tile;

import java.util.function.Function;

import com.sunflow.game.GameBase;
import com.sunflow.gfx.SImage;

public class ImageTile extends Tile {
	private static Function<String, SImage> imageLoader;

	public static void init(Function<String, SImage> imageLoader) { ImageTile.imageLoader = imageLoader; }

	SImage image;

	public ImageTile(String name) {
		super(name);
		this.image = imageLoader.apply("resources/assets/" + "robotico" + "/textures/tiles/" + name + ".png");
	}

	@Override
	public void show(GameBase game, int x, int y) {
		game.image(this.image, x, y, 1, 1);
	}

}
