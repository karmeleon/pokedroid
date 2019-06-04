package com.games.pokedroid.gfx;

import com.games.pokedroid.BottomPanel;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public abstract class PokemonPanel extends SurfaceView
{

	public boolean isAnimating;

	public PokemonPanel(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}
	
	public abstract void onDraw(Canvas c);
	
	public class CanvasThread extends Thread
	{
	    private SurfaceHolder _surfaceHolder;
	    private PokemonPanel _panel;
	    private boolean _run = false;
	    private boolean unlimited;
	    private Paint FPSPaint=new Paint();

	    public CanvasThread(SurfaceHolder surfaceHolder, PokemonPanel panel)
	    {
	        _surfaceHolder = surfaceHolder;
	        _panel = panel;
	        unlimited=_panel instanceof BottomPanel;
	        FPSPaint.setARGB(255, 255, 255, 255);
	        FPSPaint.setStyle(Paint.Style.FILL);
	        FPSPaint.setTextSize(12f);
	    }

	    public void setRunning(boolean run)
	    {
	        _run = run;
	    }
	    
	    public boolean isRunning()
	    {
	    	return _run;
	    }
	    
	    public void run()
	    {
	        Canvas c;
	        while (_run)
	        {
	            c = null;
	            try
	            {
	                c = _surfaceHolder.lockCanvas(null);
	                synchronized (_surfaceHolder)
	                {
	                	long timer=System.currentTimeMillis();
	                    _panel.onDraw(c);
	                    if(!(unlimited && _panel.isAnimating) && (System.currentTimeMillis()-timer)<=16)
	    					try
							{
	    						Thread.sleep(Math.abs(16-(System.currentTimeMillis()-timer)));
							}
							catch (InterruptedException e){/*don't give a fuck.*/}
		                double et=(double)(System.currentTimeMillis()-timer)/1000d;
		                if(c!=null)
		                	c.drawText(""+(int)(1d/et)+"fps", 0, 20, FPSPaint);
	                }
	            }
	            finally
	            {
	                if (c != null)
	                {
	                    _surfaceHolder.unlockCanvasAndPost(c);
	                }
	            }
	        }
	    }
	}
}
