package com.wallpaper.pokenixon;

import java.io.BufferedInputStream;
import java.io.IOException;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.Log;

public class MenuSprite
{
	final int TOP=0, RIGHT=1, BOTTOM=2, LEFT=3;
	
	AssetManager am;
	int id, index;
	float posX, posY, VX, VY;
	int startEdge=0;
	Bitmap sprite;
	
	public MenuSprite(AssetManager am, int id, int index)
	{		
		this.index=index;
    	try
    	{
    		BufferedInputStream buf = new BufferedInputStream(am.open("front/"+id+".png"));
    		sprite = BitmapFactory.decodeStream(buf);
    		buf.close();
    	}   
    	catch (IOException e)
    	{
    	   Log.e("Image loader",e.toString());
    	}
    	
    	if(sprite==null)
    	{
    		Log.e("sprite", "oshit. "+id);
        	try
        	{
        		BufferedInputStream buf = new BufferedInputStream(am.open("front/582.png"));
        		sprite = BitmapFactory.decodeStream(buf);
        		buf.close();
        	}   
        	catch (IOException e)
        	{
        	   Log.e("Image loader",e.toString());
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
    		posX=(float) (Math.random()*PokemonWallpaper.viewWidth);
    		posY=0f;
    		
    		VX=(float) (Math.random()*feelingNegative*2);
    		VY=(float) (Math.random()*6);
    	}
    	break;
    	case 1:
    	{
    		posX=PokemonWallpaper.viewWidth+sprite.getWidth();
    		posY=(float) (Math.random()*PokemonWallpaper.viewHeight);
    		
    		VX=(float) (Math.random()*-3);
    		VY=(float) (Math.random()*feelingNegative*3);
    	}
    	break;
    	case 2:
    	{
    		posX=(float) (Math.random()*PokemonWallpaper.viewWidth);
    		posY=PokemonWallpaper.viewHeight+sprite.getHeight();
    		
    		VX=(float) (Math.random()*feelingNegative*2);
    		VY=(float) (Math.random()*-6);
    	}
    	break;
    	case 3:
    	{
    		posX=0f;
    		posY=(float) (Math.random()*PokemonWallpaper.viewHeight);
    		
    		VX=(float) (Math.random()*3);
    		VY=(float) (Math.random()*feelingNegative*3);
    	}
    	break;
    	}
	}
	
	public void stepAndDraw(Canvas c)	//(float) (posX-sprite.getWidth()+((0.5-mOffset)*mCenterX*2))
	{
		c.drawBitmap(sprite, (float) (posX-sprite.getWidth()+((0.5-PokemonWallpaper.mOffset)*PokemonWallpaper.mCenterX*2)), posY-sprite.getHeight(), null);
		
		posX+=VX;
		posY+=VY;
		
		if(isSpriteDead())
			PokemonWallpaper.onSpriteNeedsDestroying(index);
	}
	
	private boolean isSpriteDead()
	{
		boolean res=false;
		if(posX>PokemonWallpaper.viewWidth+sprite.getWidth())
			res=true;
		if(posY>PokemonWallpaper.viewHeight+sprite.getHeight())
			res=true;
		
		if(posX<-1*sprite.getWidth())
			res=true;
		if(posY<-1*sprite.getHeight())
			res=true;
		
		return res;
	}
}