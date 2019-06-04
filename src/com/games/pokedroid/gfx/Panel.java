package com.games.pokedroid.gfx;

import java.text.NumberFormat;

import com.games.pokedroid.R;
import com.games.pokedroid.R.drawable;
import com.games.pokedroid.game.GUIBattle;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.SurfaceHolder;

public class Panel extends PokemonPanel implements SurfaceHolder.Callback
{
	public CanvasThread canvasthread;
	
	private int viewWidth=-1;
	private int viewHeight=-1;
	
	private Paint fadePaint=new Paint();

	public boolean drawCharacters=true;
	public boolean drawHealth=true;
	
	private int fadeAlpha=0;
	private boolean isFadeIn=false;
	private boolean isFadeOut=false;
	
	private boolean drawAlly=true;
	private boolean drawEnemy=true;
	
	private boolean scaledAssetsYet=false;
	private Bitmap scaledAllyBar, scaledEnemyBar, scaledNear, scaledFar, scaledBG;
	
	private Paint highHealth=new Paint(), medHealth=new Paint(), lowHealth=new Paint(), temp=new Paint();
	Paint textPaint=new Paint(), smallTextPaint=new Paint(), reallySmallTextPaint=new Paint();
	
    public Panel(Context context, AttributeSet attrs)
    {
		super(context, attrs); 
	    getHolder().addCallback(this);
	    setFocusable(false);
	}
	
    public void fadeIn()
    {
    	isFadeIn=true;
    	isFadeOut=false;
    	fadeAlpha=255;
    }
    
    public void fadeOut()
    {
    	isFadeOut=true;
    	isFadeIn=false;
    	fadeAlpha=0;
    }
    
	public void initialScale()
	{
		scaleEnemy();
		scaleAlly();
		scaleHealthBars();
		scaleOther();
		highHealth.setARGB(170, 0, 255, 0);
		medHealth.setARGB(170, 220, 220, 0);
		lowHealth.setARGB(170, 220, 0, 0);
		textPaint.setARGB(255, 0, 0, 0);
		textPaint.setShadowLayer(1.5f, 0, 0, Color.LTGRAY);
		textPaint.setStyle(Paint.Style.FILL);
		textPaint.setAntiAlias(true);
		
		float textScaleFactor=((float)viewWidth/480f);
		
		textPaint.setTextSize(22f*textScaleFactor);
		smallTextPaint.set(textPaint);
		smallTextPaint.setTextSize(20f*textScaleFactor);
		reallySmallTextPaint.set(textPaint);
		reallySmallTextPaint.setTextSize(15f*textScaleFactor);
		
		scaledAssetsYet=true;
	}
	
	public void scaleEnemy()
	{
		scaledFar = scaleBitmap(GUIBattle.far, 2);
	}
	
	public void scaleAlly()
	{
		scaledNear = scaleBitmap(GUIBattle.near, 2);
	}
	
	public void scaleHealthBars()
	{
		scaledEnemyBar = scaleBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.enemybar), 1);
		scaledAllyBar = scaleBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.allybar), 0);
	}
	
	public void scaleOther()
	{
		scaledBG=scaleBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.battlebg), 3);
	}
	
	public Bitmap scaleBitmap(Bitmap input, int target)
	{
		
		if(viewWidth==-1 || viewHeight==-1)
		{
			viewWidth = getMeasuredWidth();
			viewHeight = getMeasuredHeight();
		}
		
		int imageWidth = input.getWidth(); //finds current image size
		int imageHeight = input.getHeight();
		
		int width=0, height=0;
		
		switch (target)
		{
		case 0:	//ally health
		{
			width=(int) (viewWidth*.476);
			height=(int) (viewHeight*.357);
		}
		break;
		case 1:	//enemy health
		{
			width=(int) (viewWidth*.476);
			height=(int) (viewHeight*.333);
		}
		break;
		case 2:	//pokemon
		{
			width=(int) (viewWidth*.5);
			height=(int) (viewHeight*.75);
		}
		break;
		case 3:	//bg
		{
			width=(int) (viewWidth);
			height=(int) (viewHeight);
		}
		break;
		}
		
		float scaleWidth=((float)width) / imageWidth;
		float scaleHeight=((float)height) / imageHeight;
		
		Matrix matrix = new Matrix();
		matrix.postScale(scaleWidth, scaleHeight);
		
		return Bitmap.createBitmap(input, 0, 0, imageWidth, imageHeight, matrix, true);
	}

	public void onDraw(Canvas canvas)
	{
		drawEnemy=drawAlly=true;
		if(!scaledAssetsYet)
			initialScale();
		canvas.drawBitmap(scaledBG, 0, 0, null);
		
		boolean isEnemyAfflicted=GUIBattle.team2[GUIBattle.activeEnemy].hasStatus;
		int enemyAffliction=-1;
		if(isEnemyAfflicted)
		{
			if(GUIBattle.team2[GUIBattle.activeEnemy].isAsleep)
				enemyAffliction=1;
			if(GUIBattle.team2[GUIBattle.activeEnemy].isPoisoned)
				enemyAffliction=2;
			if(GUIBattle.team2[GUIBattle.activeEnemy].isBurned)
				enemyAffliction=3;
			if(GUIBattle.team2[GUIBattle.activeEnemy].isFrozen)
				enemyAffliction=4;
			if(GUIBattle.team2[GUIBattle.activeEnemy].isParalyzed)
				enemyAffliction=5;
			if(GUIBattle.team2[GUIBattle.activeEnemy].isBadlyPoisoned)
				enemyAffliction=6;
		}
		
		boolean isAllyAfflicted=GUIBattle.team1[GUIBattle.activeAlly].hasStatus;
		int allyAffliction=-1;
		if(isAllyAfflicted)
		{
			if(GUIBattle.team1[GUIBattle.activeAlly].isAsleep)
				allyAffliction=1;
			if(GUIBattle.team1[GUIBattle.activeAlly].isPoisoned)
				allyAffliction=2;
			if(GUIBattle.team1[GUIBattle.activeAlly].isBurned)
				allyAffliction=3;
			if(GUIBattle.team1[GUIBattle.activeAlly].isFrozen)
				allyAffliction=4;
			if(GUIBattle.team1[GUIBattle.activeAlly].isParalyzed)
				allyAffliction=5;
			if(GUIBattle.team1[GUIBattle.activeAlly].isBadlyPoisoned)
				allyAffliction=6;
		}
		
		if(GUIBattle.team1[GUIBattle.activeAlly].currentHP==0)
			drawAlly=false;
		if(GUIBattle.team2[GUIBattle.activeEnemy].currentHP==0)
			drawEnemy=false;
		
		if(drawCharacters)
		{
			if(drawEnemy)
				canvas.drawBitmap(scaledFar, (float) (viewWidth*.5) ,(float) (viewHeight*.07), null);
			if(drawAlly)
				canvas.drawBitmap(scaledNear, (float) (viewWidth*.05) , (float) (viewHeight*.45), null);
		}
		
		if(drawHealth)
		{
			if(drawEnemy)
				canvas.drawBitmap(scaledEnemyBar, (float) (viewWidth*.03), (float) (viewHeight*.1), null);
			if(drawAlly)
				canvas.drawBitmap(scaledAllyBar, (float) (viewWidth*.5), (float) (viewHeight*.60), null);
			drawHealthBars(canvas);
			drawText(canvas);
			if(!isEnemyAfflicted && drawEnemy)
				drawEnemyLevel(canvas);
			
			if(drawEnemy)
				drawHealthPCT(canvas);
		}
		
		//status
		
		if(drawAlly && isAllyAfflicted)
			drawStatus(true, allyAffliction, canvas);
		
		if(drawEnemy && isEnemyAfflicted)
			drawStatus(false, enemyAffliction, canvas);
		
		if(isFadeIn)
		{
			if(fadeAlpha<=0)
				isFadeIn=false;
			else
			{
				fadePaint.setARGB(fadeAlpha, 0, 0, 0);
				fadeAlpha-=10;
				canvas.drawRect(0, 0, viewWidth, viewHeight, fadePaint);
			}
		}
		
		if(isFadeOut)
		{
			if(fadeAlpha>=255)
				isFadeIn=false;
			else
			{
				fadePaint.setARGB(fadeAlpha, 0, 0, 0);
				fadeAlpha+=10;
				canvas.drawRect(0, 0, viewWidth, viewHeight, fadePaint);
			}
		}
	}
	
	private void drawEnemyLevel(Canvas c)
	{
		c.drawText("Lv:", viewWidth*.34f, viewHeight*.25f, reallySmallTextPaint);
		c.drawText(""+GUIBattle.team2[GUIBattle.activeEnemy].level, viewWidth*.38f, viewHeight*.25f, reallySmallTextPaint);
	}
	
	/**
	 * Draws little box for status ailments
	 * @param side true for ally, false for enemy
	 * @param statusType see Pokemon.
	 */
	private void drawStatus(boolean side, int statusType, Canvas c)
	{
		float yPos, xPos, ATX=0, ATY=0, ETX=0, ETY=-1*viewHeight*.0035f, X=0, Y=0;	//correction values
		if(side)
		{
			xPos=viewWidth*.85f;
			yPos=viewHeight*.88f;
			X=ATX;
			Y=ATY;
		}
		else
		{
			xPos=viewWidth*.34f;
			yPos=viewHeight*.255f;
			X=ETX;
			Y=ETY;
		}
		
		RectF rect=new RectF(xPos, yPos-(viewHeight*.06f), xPos+(viewWidth*.08f), yPos);
		Paint paint=new Paint();
		Paint text=new Paint();
		text.set(smallTextPaint);
		float textScaleFactor=((float)viewWidth/480f);
		text.setTextSize(textScaleFactor*18f);
		text.setARGB(255, 255, 255, 255);
		String s="";
		
		switch(statusType)
		{
		case -1:
			return;
		case 1:
		{
			paint.setColor(0xffa8a878);
			s="SLP";
			X=viewWidth*.0035f;
			break;
		}
		case 2:
		{
			paint.setColor(0xffa040a0);
			s="PSN";
			break;
		}
		case 3:
		{
			paint.setColor(0xfff08030);
			s="BRN";
			X=-1*viewWidth*.002f;
			break;
		}
		case 4:
		{
			paint.setColor(0xff98d8d8);
			s="FRZ";
			X=viewWidth*.002f;
			break;
		}
		case 5:
		{
			paint.setColor(0xfff8d030);
			s="PAR";
			break;
		}
		case 6:
		{
			paint.setColor(0xffa040a0);
			s="BPS";
			X=viewWidth*.002f;
			break;
		}
		}
		c.drawRoundRect(rect, 3, 3, paint);
		c.drawText(s, xPos+(viewWidth*.006f)+X, yPos-(viewHeight*.005f)+Y, text);
	}
	
	private void drawHealthPCT(Canvas c)
	{
		float enemyHealthPct=(float)GUIBattle.team2[GUIBattle.activeEnemy].currentHP/(float)GUIBattle.team2[GUIBattle.activeEnemy].maxHP;
		NumberFormat fmt=NumberFormat.getPercentInstance();
		c.drawText(fmt.format(enemyHealthPct), viewWidth*.05f, viewHeight*.33f, reallySmallTextPaint);
	}

	private void drawText(Canvas c)
	{	
		if(drawEnemy)
			c.drawText(GUIBattle.team2[GUIBattle.activeEnemy].name, viewWidth*.08f, viewHeight*.23f, textPaint);
		
		if(drawAlly)
		{
			c.drawText(GUIBattle.team1[GUIBattle.activeAlly].name, viewWidth*.58f, viewHeight*.72f, textPaint);
			c.drawText(""+GUIBattle.team1[GUIBattle.activeAlly].currentHP, viewWidth*.65f, viewHeight*.885f, smallTextPaint);
			c.drawText("/", viewWidth*.72f, viewHeight*.885f, smallTextPaint);
			c.drawText(""+GUIBattle.team1[GUIBattle.activeAlly].maxHP, viewWidth*.74f, viewHeight*.885f, smallTextPaint);
			
			c.drawText("Lv:", viewWidth*.84f, viewHeight*.73f, reallySmallTextPaint);
			c.drawText(""+GUIBattle.team1[GUIBattle.activeAlly].level, viewWidth*.88f, viewHeight*.73f, reallySmallTextPaint);
		}
	}

	private void drawHealthBars(Canvas canvas)
	{
		float allyHealthPct=(float)GUIBattle.team1[GUIBattle.activeAlly].currentHP/(float)GUIBattle.team1[GUIBattle.activeAlly].maxHP;
		float enemyHealthPct=(float)GUIBattle.team2[GUIBattle.activeEnemy].currentHP/(float)GUIBattle.team2[GUIBattle.activeEnemy].maxHP;
		
		float EHBSX=(float) (viewWidth*.215);
		float EHBSY=(float) (viewHeight*.3);
		float EHBEY=(float) (viewHeight*.328);
		float EHBL=((float) (viewWidth*.444)-EHBSX)*enemyHealthPct;
		
		float AHBSX=(float) (viewWidth*.715);
		float AHBSY=(float) (viewHeight*.77);
		float AHBEY=(float) (viewHeight*.79);
		float AHBL=((float) (viewWidth*.94)-AHBSX)*allyHealthPct;
		
		if(enemyHealthPct>.5)
			temp.set(highHealth);
		if(enemyHealthPct>.25 && enemyHealthPct<=.5)
			temp.set(medHealth);
		if(enemyHealthPct<=.25)
			temp.set(lowHealth);
			
		if(drawEnemy)
			canvas.drawRect(EHBSX, EHBSY, EHBSX+EHBL, EHBEY, temp);
		
		if(allyHealthPct>.5)
			temp.set(highHealth);
		if(allyHealthPct>.25 && allyHealthPct<=.5)
			temp.set(medHealth);
		if(allyHealthPct<=.25)
			temp.set(lowHealth);
		if(drawAlly)
			canvas.drawRect(AHBSX, AHBSY, AHBSX+AHBL, AHBEY, temp);
		
		//Log.e("pokedroid","drew health bars. ally:"+allyHealthPct+" enemy:"+enemyHealthPct);
		//Log.e("pokedroid","allylength:"+AHBL+" enemylength:"+EHBL);
	}
	 
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height)
	{
		viewWidth=width;
		viewHeight=height;
	}
	
	public void surfaceCreated(SurfaceHolder holder)
	{
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
}   