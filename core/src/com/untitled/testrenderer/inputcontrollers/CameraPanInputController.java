package com.untitled.testrenderer.inputcontrollers;

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.OrthographicCamera;

public class CameraPanInputController extends InputAdapter
{
	OrthographicCamera camera;
	
	public CameraPanInputController(OrthographicCamera camera)
	{
		this.camera = camera;
	}
	
	@Override
	public boolean keyDown(int keycode)
	{
		switch (keycode) 
		{
		case Keys.W:
			camera.translate(0f, 10f);
			break;
		case Keys.S:
			camera.translate(0f, -10f);
			break;
		case Keys.A:
			camera.translate(-10f, 0f);
			break;
		case Keys.D:
			camera.translate(10f,0f);
		case Keys.T:
			camera.translate(0f, 0f, 10f);
		case Keys.G:
			camera.translate(0f, 0f, -10f);
		default:
			break;
		}
		
		return true;
	}
}
