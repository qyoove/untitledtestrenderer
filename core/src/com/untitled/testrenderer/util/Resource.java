package com.untitled.testrenderer.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.assets.AssetManager;

public class Resource {

	public static final AssetManager assetManager = new AssetManager();
	public static final Preferences preferences = Gdx.app.getPreferences("untitledrendererPrefs");
	
	private Resource(){}
	
}
