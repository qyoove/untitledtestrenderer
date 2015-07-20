package com.untitled.testrenderer.inputcontrollers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.untitled.game.topdown.TopDownPlayer;

public class CameraController implements InputProcessor {
	
	OrthographicCamera camera;
	private float lastX = 0;
	private float lastY = 0;
	private TopDownPlayer player;
	public boolean followPlayer = false;
	
	public CameraController(OrthographicCamera camera)
	{
		this.camera = camera;
	}
	
	@Override
	public boolean scrolled(int amount) {
		float zoom = camera.zoom+amount;
		camera.zoom = MathUtils.clamp(zoom, 1f, 4f);
		return true;
	}
	
	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		lastX = (float)(screenX);
		lastY = (float)(Gdx.graphics.getHeight() - screenY);
		
		return true;
	}
	
	
	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		float newY = (float)(Gdx.graphics.getHeight() - screenY);
		float newX = (float)(screenX);
		camera.translate(-newX+lastX, -newY+lastY);

		lastX = newX;
		lastY = newY;

		return true;
	}

	@Override
	public boolean keyDown(int keycode) {
		if(keycode == Keys.F) followPlayer = !followPlayer;
		return true;
	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}
	
}
