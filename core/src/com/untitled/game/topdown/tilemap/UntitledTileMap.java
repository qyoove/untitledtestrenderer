package com.untitled.game.topdown.tilemap;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.ObjectMap;

public class UntitledTileMap implements Disposable
{
	private TiledMap map;
	private Array<HighSprite> highSprites;
	private TextureAtlas highSpriteAtlas;

	public UntitledTileMap(TiledMap map,Array<Sprite> highSprites,TextureAtlas highSpriteAtlas)
	{
		this.map = map;
		Array<HighSprite> orderedHighSprites = new Array<HighSprite>();
		for(Sprite spr:highSprites)
		{
			orderedHighSprites.add(new HighSprite(spr));
		}
		this.highSprites = orderedHighSprites;
		this.highSpriteAtlas = highSpriteAtlas;
	}
	
	public TiledMap getTiledMap()
	{
		return map;
	}
	
	public Array<Sprite> getHighSprites()
	{
		Array<Sprite> highSprites = new Array<Sprite>();
		for(HighSprite h:this.highSprites)
		{
			highSprites.add(h.getSprite());
		}
		
		return highSprites;
	}
	public void addSprite(Sprite sprite)
	{
		HighSprite newHighSprite = new HighSprite(sprite);
		highSprites.add(newHighSprite);
	}
	
	public void sort()
	{
		highSprites.sort();
	}

	@Override
	public void dispose()
	{
		highSpriteAtlas.dispose();
		map.dispose();
	}
	
}

class HighSprite implements Comparable<HighSprite>
{
	Sprite sprite;
	public HighSprite(Sprite sprite)
	{
		this.sprite = sprite;
	}
	
	public Sprite getSprite()
	{
		return sprite;
	}
	
	@Override
	public int compareTo(HighSprite o)
	{
		if(this.sprite.getY()-o.getSprite().getY() < 0) return 1;
		else return -1;
	}
}
