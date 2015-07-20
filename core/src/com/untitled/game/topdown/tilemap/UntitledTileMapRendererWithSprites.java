package com.untitled.game.topdown.tilemap;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapImageLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

public class UntitledTileMapRendererWithSprites
{
	private UntitledTileMap map;
	private UntitledTileMapRenderer tileRenderer;
	
	public UntitledTileMapRendererWithSprites(UntitledTileMap map)
	{
		this.map = map;
		this.tileRenderer = new UntitledTileMapRenderer(map.getTiledMap());
	}
	
	public void setView(OrthographicCamera camera)
	{
		tileRenderer.setView(camera);
	}
	
	public void render()
	{
		tileRenderer.render();
	}
	
	private class UntitledTileMapRenderer extends OrthogonalTiledMapRenderer
	{

		public UntitledTileMapRenderer(TiledMap map)
		{
			super(map);
			
		}
		
		@Override
		public void render()
		{
			beginRender();
			
			for(MapLayer layer : map.getLayers())
			{
				if(layer.isVisible())
				{
					if(layer instanceof TiledMapTileLayer) renderTileLayer((TiledMapTileLayer)layer);
					else if(layer instanceof TiledMapImageLayer) renderImageLayer((TiledMapImageLayer)layer);
					else
					{
						for(MapObject object:layer.getObjects()) renderObject(object);
					}
				}
			}
			
			UntitledTileMapRendererWithSprites.this.map.sort();
			
			for(Sprite highSprite : UntitledTileMapRendererWithSprites.this.map.getHighSprites())
			{
				highSprite.draw(this.getBatch());
			}
			
			endRender();
				
		}
	}
	
}
