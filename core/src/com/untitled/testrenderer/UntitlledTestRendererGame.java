package com.untitled.testrenderer;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.untitled.testrenderer.screens.MenuScreen;
import com.untitled.testrenderer.util.Resource;

public class UntitlledTestRendererGame extends Game {
	
	@Override
	public void create () {
		Resource.assetManager.load("uiskin.json",Skin.class);
		Resource.assetManager.load("nichi.png",Texture.class);
		Resource.assetManager.finishLoading();
		setScreen(new MenuScreen(this));
	}


}
