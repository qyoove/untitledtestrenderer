package com.untitled.game.topdown.tilemap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.MapRenderer;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.maps.tiled.TiledMapTileSets;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.TmxMapLoader.Parameters;
import com.badlogic.gdx.maps.tiled.tiles.AnimatedTiledMapTile;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;

public class UntitledTileMapLoader
{
	TmxMapLoader loader;
	
	public UntitledTileMapLoader(FileHandleResolver resolver)
	{
		loader = new TmxMapLoader(resolver);
	}
	
	public UntitledTileMap load(String fileName,String highSpritePackFile)
	{
		TiledMap map = loader.load(fileName);
		
		loadAnimatedTiles(map);
		
		Array<Sprite> highSprites = new Array<Sprite>();	
		TextureAtlas atlas = new TextureAtlas(loader.resolve((highSpritePackFile)));
		
		loadHighSprites(highSprites, atlas, map);		
		
		return new UntitledTileMap(map, highSprites, atlas);		
	}
	
	private void loadAnimatedTiles(TiledMap map)
	{		
		TiledMapTileSets tilesets = map.getTileSets();
		
		Array<String> animatedTileNames = new Array<String>();
		
		for(TiledMapTileSet tileset : tilesets)
		{
			for(TiledMapTile tile : tileset)
			{
				Object property = tile.getProperties().get("animated");
				if(property != null && !(animatedTileNames.contains((String)property, true)))
				{
					animatedTileNames.add((String)property);
				}
			}
		}
		
		for(String name : animatedTileNames)
		{
			float interval = 0f;
			Array<FrameCompareHelpTile> frames = new Array<FrameCompareHelpTile>();
			
			for(TiledMapTileSet tileset : tilesets)
			{
				for(TiledMapTile tile : tileset)
				{
					Object property = tile.getProperties().get(name);
					if(property != null)
					{					
						frames.add(new FrameCompareHelpTile(new StaticTiledMapTile(tile.getTextureRegion()), name));
						property = tile.getProperties().get("interval");
						if(property != null)
						{
							interval = Float.parseFloat((String)property);
						}
					}
				}
			}
			
			Array<StaticTiledMapTile> orderedFrames = new Array<StaticTiledMapTile>();
			
			for(FrameCompareHelpTile orderedFrameTile : frames)
			{
				orderedFrames.add(orderedFrameTile.getStaticTile());
			}
			
			AnimatedTiledMapTile animatedTile = new AnimatedTiledMapTile(interval, orderedFrames);
			
			for(MapLayer _layer : map.getLayers())
			{
				if(_layer instanceof TiledMapTileLayer)
				{
					TiledMapTileLayer layer = (TiledMapTileLayer)_layer;			
					for(int x = 0;x < layer.getWidth();x++)
					{
						for(int y = 0;y < layer.getHeight();y++)
						{
							TiledMapTileLayer.Cell cell = layer.getCell(x, y);
							if(cell != null)
							{
								Object property = cell.getTile().getProperties().get(name);
								if(property != null)
								{
									cell.setTile(animatedTile);
								}
							}
						}
					}	
				}
			}
		}	
		
	}
	
	private void loadHighSprites(Array<Sprite> highSprites,TextureAtlas atlas,TiledMap map)
	{
		MapLayer highSpriteLayer = map.getLayers().get("highsprites");
		MapObjects highSpriteRectangles = highSpriteLayer.getObjects();
		
		for(MapObject obj:highSpriteRectangles)
		{
			Object property = obj.getProperties().get("file");
			if(property != null)
			{
				String file = (String)property;
				Rectangle spritePosition = ((RectangleMapObject)obj).getRectangle();
				Sprite sprite = atlas.createSprite(file);
				sprite.setPosition(spritePosition.x, spritePosition.y);
				highSprites.add(sprite);			
			}			 
		}
	}
	
	private class FrameCompareHelpTile implements Comparable<FrameCompareHelpTile>
	{
		private StaticTiledMapTile tile;
		private String name;
		
		FrameCompareHelpTile(StaticTiledMapTile tile,String name)
		{
			this.tile = tile;
		}
				
		StaticTiledMapTile getStaticTile(){return tile;}

		@Override
		public int compareTo(FrameCompareHelpTile o)
		{
			
			int otherFrame = Integer.parseInt((String)(o.getStaticTile().getProperties().get(name)));
			
			int frame = Integer.parseInt((String)(tile.getProperties().get(name)));
			
			return frame-otherFrame;
		}
	}
}
