package com.untitled.game.topdown;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.Map;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;

import net.dermetfan.gdx.graphics.g2d.AnimatedSprite;

public class TopDownPlayer
{
	private Animation walkLeft;
	private Animation walkRight;
	private Animation walkUp;
	private Animation walkDown;
	private Animation idleLeft;
	private Animation idleRight;
	private Animation idleUp;
	private Animation idleDown;
	
	private TextureAtlas atlas;
	
	private float speed = 5f;
	
	private float animationDuration = 0.25f;
	
	private Map map;
	
	private enum SpriteState { WALK_LEFT,WALK_RIGHT,WALK_UP,WALK_DOWN,IDLE_LEFT,IDLE_RIGHT,IDLE_UP,IDLE_DOWN }
	
	private SpriteState spriteState = SpriteState.IDLE_RIGHT;
	
	private Rectangle playerRectangle;
	
	public AnimatedSprite playerSprite;
	
	public PlayerController playerController;
	
	public TopDownPlayer(Map map)
	{
		this.map = map;
		atlas = new TextureAtlas(Gdx.files.internal("test/playersprite_test-packed/pack.atlas"));
		walkUp = new Animation(animationDuration, atlas.findRegions("walk_up"),Animation.PlayMode.LOOP);
		walkDown = new Animation(animationDuration, atlas.findRegions("walk_down"),Animation.PlayMode.LOOP);
		walkLeft = new Animation(animationDuration, atlas.findRegions("walk_left"),Animation.PlayMode.LOOP);
		TextureRegion walkRight_1 = new TextureRegion(atlas.findRegions("walk_left").get(0));
		TextureRegion walkRight_2 = new TextureRegion(atlas.findRegions("walk_left").get(1));
		TextureRegion idle_Right = new TextureRegion(atlas.findRegion("idle_left"));
		walkRight_1.flip(true,false);
		walkRight_2.flip(true, false);
		idle_Right.flip(true, false);
		walkRight = new Animation(animationDuration, walkRight_1,walkRight_2);
		walkRight.setPlayMode(Animation.PlayMode.LOOP);
		idleLeft = new Animation(animationDuration,atlas.findRegion("idle_left"));
		idleRight = new Animation(animationDuration,idle_Right);
		idleUp = new Animation(animationDuration,atlas.findRegion("idle_up"));
		idleDown = new Animation(animationDuration,atlas.findRegion("idle_down"));
		
		playerSprite = new AnimatedSprite(idleRight);
		
		Rectangle spawnPosition = ((RectangleMapObject)(map.getLayers().get("special").getObjects().get("spawn"))).getRectangle();
		
		playerSprite.setPosition(spawnPosition.x, spawnPosition.y);
		playerRectangle = new Rectangle(spawnPosition.x,spawnPosition.y,16f,32f);
		
		playerController = new PlayerController();
		
		
	}
	public void update()
	{
		move();
		setAnimation();
		
		playerSprite.setPosition(playerRectangle.x, playerRectangle.y);
		
		playerSprite.update();
	}
	
	private void move()
	{
		Rectangle attemptRectangle = new Rectangle(playerRectangle);
		
		switch(spriteState)
		{
		case WALK_LEFT:
			attemptRectangle.setPosition(attemptRectangle.x-speed, attemptRectangle.y);
			break;
		case WALK_RIGHT:
			attemptRectangle.setPosition(attemptRectangle.x+speed, attemptRectangle.y);
			break;
		case WALK_UP:
			attemptRectangle.setPosition(attemptRectangle.x, attemptRectangle.y+speed);
			break;
		case WALK_DOWN:
			attemptRectangle.setPosition(attemptRectangle.x, attemptRectangle.y-speed);
			break;
		default:
			break;
		}
		
		if(isMapColliding(attemptRectangle)) return;
		playerRectangle = attemptRectangle;
	}
	
	private void setAnimation()
	{
		switch(spriteState)
		{
		case WALK_LEFT:
			playerSprite.setAnimation(walkLeft);
			break;
		case WALK_RIGHT:
			playerSprite.setAnimation(walkRight);
			break;
		case WALK_UP:
			playerSprite.setAnimation(walkUp);
			break;
		case WALK_DOWN:
			playerSprite.setAnimation(walkDown);
			break;
		case IDLE_LEFT:
			playerSprite.setAnimation(idleLeft);
			break;
		case IDLE_RIGHT:
			playerSprite.setAnimation(idleRight);
			break;
		case IDLE_UP:
			playerSprite.setAnimation(idleUp);
			break;
		case IDLE_DOWN:
			playerSprite.setAnimation(idleDown);
			break;
		default:
			break;
		}
	}
	
	
	private boolean isMapColliding(Rectangle rectangle)
	{
		for(MapObject object : map.getLayers().get("collision").getObjects())
		{
			if(object instanceof RectangleMapObject)
			{
				Rectangle collisionRectangle = ((RectangleMapObject)object).getRectangle();
				if(Intersector.overlaps(rectangle,collisionRectangle)) return true;
			}
		}
		
		return false;
			
	}
	
	private void interact()
	{
		
	}
	
	public class PlayerController implements InputProcessor
	{
		private Array<Integer> pushedKeys;
		
		public PlayerController()
		{
			pushedKeys = new Array<Integer>();
			pushedKeys.ordered = false;
		}
		
		
		
		@Override
		public boolean keyDown(int keycode)
		{
			if(keycode != Keys.W && keycode != Keys.A && keycode != Keys.S && keycode != Keys.D) return false;
			
			pushedKeys.add(keycode);
			
			switch(pushedKeys.peek())
			{
			case Keys.W:
				spriteState = SpriteState.WALK_UP;
				break;
			case Keys.S:
				spriteState = SpriteState.WALK_DOWN;
				break;
			case Keys.A:;
				spriteState = SpriteState.WALK_LEFT;
				break;
			case Keys.D:
				spriteState = SpriteState.WALK_RIGHT;
				break;
			case Keys.ENTER:
				TopDownPlayer.this.interact();
				break;
			default:
				break;
			}
			return true;
		}
		
		@Override
		public boolean keyUp(int keycode)
		{
			if(keycode != Keys.W && keycode != Keys.A && keycode != Keys.S && keycode != Keys.D) return false;
			
			pushedKeys.removeValue(keycode, false);
			
			if(pushedKeys.size == 0)
				switch(spriteState)
				{
				case WALK_UP:
					spriteState = SpriteState.IDLE_UP;
					break;
				case WALK_DOWN:
					spriteState = SpriteState.IDLE_DOWN;
					break;
				case WALK_LEFT:
					spriteState = SpriteState.IDLE_LEFT;
					break;
				case WALK_RIGHT:
					spriteState = SpriteState.IDLE_RIGHT;
					break;
				default:
					break;
				}
			else
				switch(pushedKeys.peek())
				{
				case Keys.W:
					spriteState = SpriteState.WALK_UP;
					break;
				case Keys.S:
					spriteState = SpriteState.WALK_DOWN;
					break;
				case Keys.A:;
					spriteState = SpriteState.WALK_LEFT;
					break;
				case Keys.D:
					spriteState = SpriteState.WALK_RIGHT;
					break;
				case Keys.ENTER:
					TopDownPlayer.this.interact();
					break;
				default:
					break;
				}		
			return true;
		}



		@Override
		public boolean keyTyped(char character) {
			// TODO Auto-generated method stub
			return false;
		}



		@Override
		public boolean touchDown(int screenX, int screenY, int pointer,
				int button) {
			// TODO Auto-generated method stub
			return false;
		}



		@Override
		public boolean touchUp(int screenX, int screenY, int pointer, int button) {
			// TODO Auto-generated method stub
			return false;
		}



		@Override
		public boolean touchDragged(int screenX, int screenY, int pointer) {
			// TODO Auto-generated method stub
			return false;
		}



		@Override
		public boolean mouseMoved(int screenX, int screenY) {
			// TODO Auto-generated method stub
			return false;
		}



		@Override
		public boolean scrolled(int amount) {
			// TODO Auto-generated method stub
			return false;
		}
	}

}
