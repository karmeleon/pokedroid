package com.wallpaper.pokenixon;

import android.content.res.AssetManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Handler;
import android.service.wallpaper.WallpaperService;
import android.view.SurfaceHolder;

public class PokemonWallpaper extends WallpaperService
{
    private final Handler mHandler = new Handler();
    public static AssetManager am;
    public static float viewWidth, viewHeight;
    public static MenuSprite[] sprites;
    
    public static float mOffset;
    public static float mCenterX;
    public static float mCenterY;

    @Override
    public void onCreate()
    {
        super.onCreate();
        am=getAssets();
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
    }

    @Override
    public Engine onCreateEngine()
    {
        return new SpriteEngine();
    }
    
	public static void onSpriteNeedsDestroying(int index)	//called by sprites when they exit the screen. replaces dead sprite with new one
	{
		sprites[index]=newRandomSprite(index);
	}
	
	public static MenuSprite newRandomSprite(int id)
	{
		return new MenuSprite(am, (int) (Math.random()*648)+1, id);
	}
    
    class SpriteEngine extends Engine
    {
        private boolean firstRun=true;

        private final Runnable BGThread = new Runnable()
        {
            public void run()
            {
                drawFrame();
            }
        };
        private boolean mVisible;

        SpriteEngine()
        {
        }

        public void onCreate(SurfaceHolder surfaceHolder)
        {
            super.onCreate(surfaceHolder);
        }

        public void onDestroy()
        {
            super.onDestroy();
            mHandler.removeCallbacks(BGThread);
        }

        @Override
        public void onVisibilityChanged(boolean visible)
        {
            mVisible = visible;
            if (visible)
            {
                drawFrame();
            }
            else
            {
                mHandler.removeCallbacks(BGThread);
            }
        }

        @Override
        public void onSurfaceChanged(SurfaceHolder holder, int format, int width, int height)
        {
            super.onSurfaceChanged(holder, format, width, height);
            //gets center to draw logo in right spot
            mCenterX = width/2.0f;
            mCenterY = height/2.0f;
            drawFrame();
        }

        public void onSurfaceDestroyed(SurfaceHolder holder)
        {
            super.onSurfaceDestroyed(holder);
            mVisible = false;
            mHandler.removeCallbacks(BGThread);
        }

        public void onOffsetsChanged(float xOffset, float yOffset, float xStep, float yStep, int xPixels, int yPixels)
        {
            mOffset = xOffset;
            drawFrame();
        }

        void drawFrame()
        {
            final SurfaceHolder holder = getSurfaceHolder();

            Canvas c = null;
            try {
                c = holder.lockCanvas();
                if (c != null)
                {
                	if(firstRun)
                	{
            			viewWidth=c.getWidth();
            			viewHeight=c.getHeight();
            			sprites=new MenuSprite[20];
            			for(int i=0; i<sprites.length; i++)
            				sprites[i]=newRandomSprite(i);
            			firstRun=false;
                	}
                	c.drawColor(Color.BLACK);
                	for(int i=0; i<sprites.length; i++)
                		sprites[i].stepAndDraw(c);
                }
            }
            finally
            {
                if (c != null) holder.unlockCanvasAndPost(c);
            }

            // Reschedule the next redraw
            mHandler.removeCallbacks(BGThread);
            if (mVisible)
            {
                mHandler.postDelayed(BGThread, 10);
            }
        }
    }
}