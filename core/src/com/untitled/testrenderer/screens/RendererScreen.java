package com.untitled.testrenderer.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.loaders.resolvers.AbsoluteFileHandleResolver;
import com.badlogic.gdx.assets.loaders.resolvers.ExternalFileHandleResolver;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.untitled.game.topdown.TopDownPlayer;
import com.untitled.game.topdown.tilemap.UntitledTileMap;
import com.untitled.game.topdown.tilemap.UntitledTileMapLoader;
import com.untitled.game.topdown.tilemap.UntitledTileMapRendererWithSprites;
import com.untitled.testrenderer.inputcontrollers.CameraController;
import com.untitled.testrenderer.inputcontrollers.CameraPanInputController;
import com.untitled.testrenderer.util.Resource;

public class RendererScreen implements Screen {

	Game game;
	OrthographicCamera camera;
	UntitledTileMap map;
	UntitledTileMapRendererWithSprites renderer;
	TopDownPlayer player;
	CameraController cameraController;
	CheckBox checkbox;
	Stage stage;
	
	
	public RendererScreen(Game game)
	{
		this.game = game;
	}
	
	@Override
	public void show() {
		stage = new Stage(new ScreenViewport());
		
		Skin skin = Resource.assetManager.get("uiskin.json");		
		Table table = new Table(skin);
		checkbox = new CheckBox("Game Mode",skin);
		checkbox.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				cameraController.followPlayer = !cameraController.followPlayer;
			}	
		});
		TextButton refresh = new TextButton("Refresh",skin);
		refresh.addListener(new ChangeListener(){
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				map.dispose();
				map = new UntitledTileMapLoader(new AbsoluteFileHandleResolver()).load(Resource.preferences.getString("tmxfile"),Resource.preferences.getString("atlasfile"));
				map.addSprite(player.playerSprite);
				renderer = new UntitledTileMapRendererWithSprites(map);
				renderer.setView(camera);
			}
		});
		table.add(checkbox);
		table.left().top().setFillParent(true);
		table.row();
		table.add(refresh);
		stage.addActor(table);
		camera = new OrthographicCamera(Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
		map = new UntitledTileMapLoader(new AbsoluteFileHandleResolver()).load(Resource.preferences.getString("tmxfile"),Resource.preferences.getString("atlasfile"));
		player = new TopDownPlayer(map.getTiledMap());
		map.addSprite(player.playerSprite);
		renderer = new UntitledTileMapRendererWithSprites(map);
		renderer.setView(camera);
		cameraController = new CameraController(camera);
		InputMultiplexer multiplexer = new InputMultiplexer(stage,player.playerController,cameraController);
		Gdx.input.setInputProcessor(multiplexer);
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		if(cameraController.followPlayer)
		{
			camera.position.x = player.playerSprite.getX();
			camera.position.y = player.playerSprite.getY();
			camera.zoom = 1f;
			checkbox.setChecked(true);
			Gdx.graphics.setDisplayMode(640,320,false);
		}
		else
		{
			checkbox.setChecked(false);
		}
		player.update();
		camera.update();
		renderer.setView(camera);
		renderer.render();
		stage.draw();
	}

	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width, height,true);
		camera.viewportWidth = width;
		camera.viewportHeight = height;

	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		map.dispose();
		stage.dispose();
		Resource.assetManager.unload("uiskin.json");
		

	}

}
