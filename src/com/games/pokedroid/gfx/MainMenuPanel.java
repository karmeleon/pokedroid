package com.games.pokedroid.gfx;

import java.io.BufferedInputStream;
import java.io.IOException;

import com.games.pokedroid.PokeDroid;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;

public class MainMenuPanel extends PokemonPanel implements SurfaceHolder.Callback
{

	private CanvasThread canvasthread;
	
	public static int viewWidth;
	public static int viewHeight;
	
	private boolean firstRun=true;
	
	private MenuSprite[] sprites;
	
	private static AssetManager am;
	
    public MainMenuPanel(Context context, AttributeSet attrs)
    {
		super(context, attrs);
		sprites=new MenuSprite[16];
	    getHolder().addCallback(this);
	    setFocusable(true);
	    
	    am=PokeDroid.am;
	    firstRun=true;
	}

	public void onDraw(Canvas c)
	{
		for(int i=0; i<sprites.length; i++)
			if(sprites[i]==null || sprites[i].needsDestroying())
				sprites[i]=newRandomSprite();
		
		if(firstRun)
		{
			viewWidth=getWidth();
			viewHeight=getHeight();
			firstRun=false;
		}
		if(c!=null)
			c.drawColor(Color.BLACK);
		drawSprites(c);
	}
	
	public void drawSprites(Canvas c)
	{
		for(MenuSprite i:sprites)
			if(i==null)
				return;
		for(int i=0; i<sprites.length; i++)
			sprites[i].stepAndDraw(c);
	}
	
	public MenuSprite newRandomSprite()
	{
		if(am!=null)
			return new MenuSprite(am, (int) (Math.random()*648)+1);
		return null;
	}
	
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height)
	{
		viewWidth=width;
		viewHeight=height;
		sprites=new MenuSprite[16];
	}
	
	public void surfaceCreated(SurfaceHolder holder)
	{
		sprites=new MenuSprite[16];
		canvasthread=new CanvasThread(getHolder(), this);
	    canvasthread.setRunning(true);
	    canvasthread.start();
	}
	
	public void surfaceDestroyed(SurfaceHolder holder)
	{
		boolean retry = true;
		canvasthread.setRunning(false);
		while (retry)
		{
			try
			{
				canvasthread.join();
				retry = false;
			} catch (InterruptedException e){}
		}
	}
	
	public class MenuSprite
	{
		final int TOP=0, RIGHT=1, BOTTOM=2, LEFT=3;
		
		private float posX, posY, VX, VY;
		private int startEdge=0;
		private Bitmap sprite=null;
		private boolean needsDestroying=false;
		
		public MenuSprite(AssetManager am, int id)
		{
			if(am==null)
			{
				Log.e("pokedroid", "saving you from a painful death.");
				needsDestroying=true;
				return;
			}
			else if(PokeDroid.nixonMode)
			{
		    	try
		    	{
		    		BufferedInputStream buf = new BufferedInputStream(am.open("nixon/"+(int)(Math.random()*7+1)+".png"));
		    		sprite = BitmapFactory.decodeStream(buf);
		    		buf.close();
		    	}   
		    	catch (IOException e)
		    	{
		    	   Log.e("pokedroid",e.toString());
		    	}
			}
			else
			{
				try
				{
					BufferedInputStream buf = new BufferedInputStream(am.open("front/"+id+".png"));
					sprite = BitmapFactory.decodeStream(buf);
					buf.close();
				}   
				catch (IOException e)
				{
					Log.e("pokedroid",e.toString());
				}
			}
			
			if(sprite==null)
			{
				Log.e("pokedroid", "oshit. "+id);
				try
				{
					BufferedInputStream buf = new BufferedInputStream(am.open("front/582.png"));
					sprite = BitmapFactory.decodeStream(buf);
					buf.close();
				}   
				catch (IOException e)
				{
					Log.e("pokedroid",e.toString());
				}
			}
	    	
	    	startEdge=(int) (Math.random()*4);
	    	
	    	int feelingNegative=1;
	    	if(Math.random()>.5)
	    		feelingNegative=-1;
	    	
	    	switch(startEdge)
	    	{
	    	case 0:
	    	{
	    		posX=(float) (Math.random()*MainMenuPanel.viewWidth);
	    		posY=0f;
	    		
	    		VX=(float) (Math.random()*feelingNegative*1);
	    		VY=(float) (Math.random()*5);
	    	}
	    	break;
	    	case 1:
	    	{
	    		posX=MainMenuPanel.viewWidth+sprite.getWidth();
	    		posY=(float) (Math.random()*MainMenuPanel.viewHeight);
	    		
	    		VX=(float) (Math.random()*-2);
	    		VY=(float) (Math.random()*feelingNegative*2);
	    	}
	    	break;
	    	case 2:
	    	{
	    		posX=(float) (Math.random()*MainMenuPanel.viewWidth);
	    		posY=MainMenuPanel.viewHeight+sprite.getHeight();
	    		
	    		VX=(float) (Math.random()*feelingNegative*1);
	    		VY=(float) (Math.random()*-5);
	    	}
	    	break;
	    	case 3:
	    	{
	    		posX=0f;
	    		posY=(float) (Math.random()*MainMenuPanel.viewHeight);
	    		
	    		VX=(float) (Math.random()*2);
	    		VY=(float) (Math.random()*feelingNegative*2);
	    	}
	    	break;
	    	}
	    	VX++;
	    	VY++;
		}
		
		public boolean needsDestroying()
		{
			return needsDestroying;
		}
		
		public void stepAndDraw(Canvas c)
		{
			if(c==null)
				return;
			if(sprite==null)
				return;
			
			c.drawBitmap(sprite, posX-sprite.getWidth(), posY-sprite.getHeight(), null);
			
			posX+=VX;
			posY+=VY;
			if(isSpriteDead())
				needsDestroying=true;
		}
		
		private boolean isSpriteDead()
		{
			boolean res=false;
			if(posX>MainMenuPanel.viewWidth+sprite.getWidth())
				res=true;
			if(posY>MainMenuPanel.viewHeight+sprite.getHeight())
				res=true;
			
			if(posX<-1*sprite.getWidth())
				res=true;
			if(posY<-1*sprite.getHeight())
				res=true;
			
			return res;
		}
	}
}