package com.untitled.testrenderer.screens;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.untitled.testrenderer.util.Resource;

public class MenuScreen implements Screen{

	private Game game;
	private Stage stage;
	
	public MenuScreen(Game game) {
		this.game = game;
	}
	@Override
	public void show() {
		Skin skin = Resource.assetManager.get("uiskin.json");
		stage = new Stage(new ScreenViewport());
		Table table = new Table(skin);
		Label label = new Label("Ren-do-rah!", skin);
		Texture texture = Resource.assetManager.get("4f1.jpg");
		Image image = new Image(texture);
		
		TextButton loadTmxFile = new TextButton("Select Tmx File", skin);
		loadTmxFile.addListener(new ChangeListener(){
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				JFileChooser chooser = new JFileChooser();
				FileNameExtensionFilter filter = new FileNameExtensionFilter("mapfile(.tmx)", "tmx");
				chooser.setFileFilter(filter);
				int returnVal = chooser.showOpenDialog(null);
				if(returnVal != JFileChooser.APPROVE_OPTION) return;
				File selectedFile = chooser.getSelectedFile();
				if(selectedFile == null) return;
				Resource.preferences.putString("tmxfile", selectedFile.getAbsolutePath());
			}
		});
		TextButton loadAtlasFile = new TextButton("Select HighSpriteSheet",skin);
		loadAtlasFile.addListener(new ChangeListener(){
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				JFileChooser chooser = new JFileChooser();
				FileNameExtensionFilter filter = new FileNameExtensionFilter("spritesheet", "atlas");
				chooser.setFileFilter(filter);
				int returnVal = chooser.showOpenDialog(null);
				if(returnVal != JFileChooser.APPROVE_OPTION) return;
				File selectedFile = chooser.getSelectedFile();
				if(selectedFile == null) return;
				Resource.preferences.putString("atlasfile", selectedFile.getAbsolutePath());
			}
		});
		TextButton startRenderer = new TextButton("Start Renderer",skin);
		startRenderer.addListener(new ChangeListener(){
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				Resource.preferences.flush();
				game.setScreen(new RendererScreen(game));
			}
		});
		
		table.add(image).colspan(2).space(10f).width(356f).height(390f);
		table.row();
		table.add(label).colspan(2).space(10f);
		table.row();
		table.add(loadTmxFile).space(10f);
		table.add(loadAtlasFile).space(10f);
		table.row();
		table.add(startRenderer).colspan(2);
		table.setFillParent(true);
		stage.addActor(table);
				
		Gdx.input.setInputProcessor(stage);
		
		
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		stage.draw();
	}

	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width, height, true);
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
		dispose();
		
	}

	@Override
	public void dispose() {
		stage.dispose();
		Resource.assetManager.unload("4f1.jpg");
	}
	
}
