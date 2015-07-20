package com.untitled.testrenderer.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.untitled.testrenderer.UntitlledTestRendererGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 1366;
		config.height = 768;

		new LwjglApplication(new UntitlledTestRendererGame(), config);
	}
}
