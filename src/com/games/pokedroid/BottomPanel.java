package com.games.pokedroid;

import com.games.pokedroid.game.GUIBattle;
import com.games.pokedroid.gfx.PokemonPanel;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.SurfaceHolder;

public class BottomPanel extends PokemonPanel implements SurfaceHolder.Callback
{
	CanvasThread canvasthread;
	public int currentView=0;
	public final int MAIN=0;
	public final int ATTACK=1;
	public final int SWITCH=2;
	final int ATKINFO=3;
	public final int NONE=4;
	
	public final int NORMAL=0;
	public final int FIRE=1;
	public final int WATER=2;
	public final int ELECTRIC=3;
	public final int GRASS=4;
	public final int ICE=5;
	public final int FIGHTING=6;
	public final int POISON=7;
	public final int GROUND=8;
	public final int FLYING=9;
	public final int PSYCHIC=10;
	public final int BUG=11;
	public final int ROCK=12;
	public final int GHOST=13;
	public final int DRAGON=14;
	public final int DARK=15;
	public final int STEEL=16;
	public final int UNKNOWN=17;
	
	private float animationOffset=0;
	private float animationSpeed=0;
	private int targetAnimationScreen=0;
	
	private Bitmap attackScreen, switchScreen, mainScreen;
	
	private boolean firstRun=true;
	private boolean clearScreens=false;
	
	public int viewWidth=-1;
	public int viewHeight=-1;
	
	Paint fightPaint=new Paint(), switchPaint=new Paint();
	Paint fightTextPaint=new Paint(), switchTextPaint=new Paint(), attackTextPaint=new Paint();
	Paint attackPanelPaint=new Paint();
	Paint koSwitchPanelPaint=new Paint(), switchPanelPaint=new Paint();
	Paint switchPanelTextPaint=new Paint();
	Paint highHealth=new Paint(), medHealth=new Paint(), lowHealth=new Paint();
	Paint infoPromptText=new Paint();
	Bitmap background;
	static Paint[] types;
	public AssetManager am;
	private boolean isFadeIn=false;
	private boolean isFadeOut=false;
	private int fadeAlpha;
	private Paint fadePaint=new Paint();
	
    public BottomPanel(Context context, AttributeSet attrs)
    {
		super(context, attrs); 
	    getHolder().addCallback(this);
	    setFocusable(false);
	    
	    types=new Paint[18];
	    for(int i=0; i<types.length; i++)
	    	types[i]=new Paint();
	    
		highHealth.setARGB(170, 0, 255, 0);
		medHealth.setARGB(170, 220, 220, 0);
		lowHealth.setARGB(170, 220, 0, 0);
	    
	    types[NORMAL].setColor(0xffa8a878);
	    types[FIRE].setColor(0xfff08030);
	    types[WATER].setColor(0xff6890f0);
	    types[ELECTRIC].setColor(0xfff8d030);
	    types[GRASS].setColor(0xff78c850);
	    types[ICE].setColor(0xff98d8d8);
	    types[FIGHTING].setColor(0xffc03028);
	    types[POISON].setColor(0xffa040a0);
	    types[GROUND].setColor(0xffe0c068);
	    types[FLYING].setColor(0xffa890f0);
	    types[PSYCHIC].setColor(0xfff85888);
	    types[BUG].setColor(0xffa8b820);
	    types[ROCK].setColor(0xffb8a038);
	    types[GHOST].setColor(0xff705898);
	    types[DRAGON].setColor(0xff7038f8);
	    types[DARK].setColor(0xff705848);
	    types[STEEL].setColor(0xffb8b8d0);
	    types[UNKNOWN].setColor(0xff68a090);
	    
		fightPaint.setARGB(255, 255, 0, 0);
		switchPaint.setARGB(255, 0, 255, 255);
		fightTextPaint.setARGB(255, 255, 255, 255);
		
		fightPaint.setShadowLayer(3f, 0, 0, Color.RED);
		switchPaint.setShadowLayer(3f, 0, 0, Color.CYAN);
		fightTextPaint.setShadowLayer(2f, 0, 0, Color.DKGRAY);
		
		fightPaint.setShader(new LinearGradient(50, 0, 50, 60, Color.BLACK, Color.RED, Shader.TileMode.valueOf("CLAMP")));
		switchPaint.setShader(new LinearGradient(50, 0, 60, 60, Color.BLACK, Color.CYAN, Shader.TileMode.valueOf("CLAMP")));
		
		fightTextPaint.setStyle(Paint.Style.FILL);
		fightTextPaint.setAntiAlias(true);
		
		switchTextPaint.set(fightTextPaint);
		switchTextPaint.setARGB(255, 0, 0, 0);
		switchTextPaint.setShadowLayer(2f, 0, 0, Color.LTGRAY);
		
		switchPanelTextPaint.set(fightTextPaint);

		infoPromptText.set(switchPanelTextPaint);
		
		attackPanelPaint.setColor(0xffdaeb9e);
		
		background=BitmapFactory.decodeResource(getResources(), R.drawable.bg);
	}

    public void clearScreens()
    {
    	clearScreens=true;
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
    
    public void animate(int targetScreen)
    {
    	isAnimating=true;
    	targetAnimationScreen=targetScreen;
    	switch(targetScreen)
    	{
    	case MAIN:
    	{
    		if(currentView==ATTACK)
    			animationSpeed=-50;
    		if(currentView==SWITCH)
    			animationSpeed=50;
    	}
    	break;
    	
    	case ATTACK:
    		animationSpeed=50;
    	break;
    	
    	case SWITCH:
    		animationSpeed=-50;
    	break;
    	}
    }
    
	public void onDraw(Canvas c)
	{
		if(clearScreens)
		{
			clearScreens=false;
			switchScreen=null;
			attackScreen=null;
		}
		
		if(firstRun)
		{
			koSwitchPanelPaint.setShader(new LinearGradient(0, 60, viewWidth, 50, Color.RED, Color.DKGRAY, Shader.TileMode.valueOf("CLAMP")));
			switchPanelPaint.setShader(new LinearGradient(0, 60, viewWidth, 50, Color.CYAN, Color.DKGRAY, Shader.TileMode.valueOf("CLAMP")));
			
			float textScaleFactor=((float)viewWidth/480f);
			infoPromptText.setTextSize(23f*textScaleFactor);
			switchPanelTextPaint.setTextSize(15f*textScaleFactor);
			fightTextPaint.setTextSize(50f*textScaleFactor);
			switchTextPaint.setTextSize(20f*textScaleFactor);
		}
		
		if(mainScreen==null)
			mainScreen=InitialBitmapGenerator.generateMainMenu(viewWidth, viewHeight);
		
		if(switchScreen==null)
			switchScreen=InitialBitmapGenerator.generateSwitch(viewWidth, viewHeight, am);
		
		if(attackScreen==null)
			attackScreen=InitialBitmapGenerator.generateAttack(viewWidth, viewHeight);
		
		c.drawBitmap(background, 0, 0, null);
		
		boolean shouldStillDraw=true;
		
		if(isAnimating)
		{
			shouldStillDraw=false;
			animationOffset+=animationSpeed;	//moves animation
			if((animationSpeed<0 && animationOffset<=-1*viewWidth) || (animationSpeed>0 && animationOffset>=viewWidth))	//animation done?
			{
				isAnimating=false;	//resets all the animation stuff
				animationSpeed=0;
				animationOffset=0;
				currentView=targetAnimationScreen;
				targetAnimationScreen=0;
				shouldStillDraw=true;
			}
			else	//draws both views
			{
				if(targetAnimationScreen==ATTACK)
				{
					drawMain(c);
					float temp=animationOffset;
					animationOffset=temp-viewWidth;
					drawAttack(c);
					animationOffset=temp;
				}
				else if(targetAnimationScreen==MAIN && currentView==ATTACK)
				{
					drawAttack(c);
					float temp=animationOffset;
					animationOffset=temp+viewWidth;
					drawMain(c);
					animationOffset=temp;
				}
				else if(targetAnimationScreen==SWITCH)
				{
					drawMain(c);
					float temp=animationOffset;
					animationOffset=temp+viewWidth;
					drawSwitch(c);
					animationOffset=temp;
				}
				else if(targetAnimationScreen==MAIN && currentView==SWITCH)
				{
					drawSwitch(c);
					float temp=animationOffset;
					animationOffset=temp-viewWidth;
					drawMain(c);
					animationOffset=temp;
				}
			}
		}
		if(shouldStillDraw)
		{
			switch(currentView)
			{
			case 0:
				drawMain(c);
				break;
			case 1:
				drawAttack(c);
				break;
			case 2:
				drawSwitch(c);
				break;
			}
		}
		
		if(isFadeIn)
		{
			if(fadeAlpha<=0)
				isFadeIn=false;
			else
			{
				fadePaint.setARGB(fadeAlpha, 0, 0, 0);
				fadeAlpha-=10;
				c.drawRect(0, 0, viewWidth, viewHeight, fadePaint);
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
				c.drawRect(0, 0, viewWidth, viewHeight, fadePaint);
			}
		}
	}


	private void drawSwitch(Canvas c)
	{
		drawBitmap(switchScreen, 0, 0, null, c);
	}

	private void drawAttack(Canvas c)
	{
		drawBitmap(attackScreen, 0, 0, null, c);
	}

	private void drawMain(Canvas c)
	{
		drawBitmap(mainScreen, 0, 0, null, c);
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
			}
			catch (InterruptedException e){}
		}
	}
	
	public Bitmap scaleBitmap(Bitmap input)
	{
		int imageWidth = input.getWidth();
		int imageHeight = input.getHeight();
		
		int width=(int) (viewWidth*.12f);
		int height=(int) (viewHeight*.16f);
		
		float scaleWidth=((float)width) / imageWidth;
		float scaleHeight=((float)height) / imageHeight;
		
		Matrix matrix = new Matrix();
		matrix.postScale(scaleWidth, scaleHeight);
		
		return Bitmap.createBitmap(input, 0, 0, imageWidth, imageHeight, matrix, true);
	}
	
	public Bitmap scaleBitmapBG(Bitmap input)
	{
		int imageWidth = input.getWidth();
		int imageHeight = input.getHeight();
		
		int width=viewWidth;
		int height=viewHeight;
		
		float scaleWidth=((float)width) / imageWidth;
		float scaleHeight=((float)height) / imageHeight;
		
		Matrix matrix = new Matrix(); //requred stuff
		matrix.postScale(scaleWidth, scaleHeight);
		
		return Bitmap.createBitmap(input, 0, 0, imageWidth, imageHeight, matrix, true);
	}
	
	private void drawBitmap(Bitmap bitmap, float left, float top, Paint paint, Canvas c)
	{
		c.drawBitmap(bitmap, left+animationOffset, top, paint);
	}
	
	private void drawText(String text, float x, float y, Paint paint, Canvas c)
	{
		c.drawText(text, x+animationOffset, y, paint);
	}
	
	public static class InitialBitmapGenerator
	{
		public static Bitmap generateMainMenu(float viewWidth, float viewHeight)
		{
			Bitmap out=Bitmap.createBitmap((int)viewWidth, (int)viewHeight, Bitmap.Config.valueOf("ARGB_8888"));
			Canvas c=new Canvas(out);
			
			Paint fightPaint=new Paint();
			Paint switchPaint=new Paint();
			Paint fightTextPaint=new Paint();
			Paint switchTextPaint=new Paint();
			fightPaint.setARGB(255, 255, 0, 0);
			fightPaint.setShadowLayer(3f, 0, 0, Color.RED);
			fightPaint.setShader(new LinearGradient(50, 0, 50, 60, Color.BLACK, Color.RED, Shader.TileMode.valueOf("CLAMP")));
			
			switchPaint.setARGB(255, 0, 255, 255);
			switchPaint.setShadowLayer(3f, 0, 0, Color.CYAN);
			switchPaint.setShader(new LinearGradient(50, 0, 60, 60, Color.BLACK, Color.CYAN, Shader.TileMode.valueOf("CLAMP")));
			
			fightTextPaint.setARGB(255, 255, 255, 255);
			fightTextPaint.setShadowLayer(2f, 0, 0, Color.DKGRAY);
			fightTextPaint.setStyle(Paint.Style.FILL);
			fightTextPaint.setAntiAlias(true);
			switchTextPaint.set(fightTextPaint);
			switchTextPaint.setARGB(255, 0, 0, 0);
			switchTextPaint.setShadowLayer(2f, 0, 0, Color.LTGRAY);
			
			float textScaleFactor=((float)viewWidth/480f);
			fightTextPaint.setTextSize(50f*textScaleFactor);
			switchTextPaint.setTextSize(20f*textScaleFactor);
			c.drawRect(viewWidth*.05f, viewHeight*.1f, viewWidth*.95f, viewHeight*.65f, fightPaint);
			c.drawRect(viewWidth*.05f, viewHeight*.7f, viewWidth*.95f, viewHeight*.9f, switchPaint);
			c.drawText("Fight!", viewWidth*.38f, viewHeight*.45f, fightTextPaint);
			c.drawText("Switch", viewWidth*.45f, viewHeight*.82f, switchTextPaint);
			return out;
		}
		
		public static Bitmap generateAttack(float viewWidth, float viewHeight)
		{
			Bitmap out=Bitmap.createBitmap((int)viewWidth, (int)viewHeight, Bitmap.Config.valueOf("ARGB_8888"));
			Canvas c=new Canvas(out);
			
			Paint attackPanelPaint=new Paint();
			attackPanelPaint.setColor(0xffdaeb9e);
			
			Paint switchTextPaint=new Paint();
			switchTextPaint.setStyle(Paint.Style.FILL);
			switchTextPaint.setAntiAlias(true);
			switchTextPaint.setARGB(255, 0, 0, 0);
			switchTextPaint.setShadowLayer(2f, 0, 0, Color.LTGRAY);
			
			float textScaleFactor=((float)viewWidth/480f);
			switchTextPaint.setTextSize(20f*textScaleFactor);
			
			c.drawRect(viewWidth*.01f, viewHeight*.1f, viewWidth*.49f, viewHeight*.45f, BottomPanel.types[GUIBattle.team1[GUIBattle.activeAlly].attack1.type]);	//TL
			c.drawRect(viewWidth*.01f, viewHeight*.5f, viewWidth*.49f, viewHeight*.9f, BottomPanel.types[GUIBattle.team1[GUIBattle.activeAlly].attack3.type]);		//BL
			c.drawRect(viewWidth*.51f, viewHeight*.1f, viewWidth*.99f, viewHeight*.45f, BottomPanel.types[GUIBattle.team1[GUIBattle.activeAlly].attack2.type]);	//TR
			c.drawRect(viewWidth*.51f, viewHeight*.5f, viewWidth*.99f, viewHeight*.9f, BottomPanel.types[GUIBattle.team1[GUIBattle.activeAlly].attack4.type]);		//BR
			
			c.drawRect(viewWidth*.03f, viewHeight*.13f, viewWidth*.47f, viewHeight*.43f, attackPanelPaint);	//TL
			c.drawRect(viewWidth*.03f, viewHeight*.53f, viewWidth*.47f, viewHeight*.87f, attackPanelPaint);	//BL
			c.drawRect(viewWidth*.53f, viewHeight*.13f, viewWidth*.97f, viewHeight*.43f, attackPanelPaint);	//TR
			c.drawRect(viewWidth*.53f, viewHeight*.53f, viewWidth*.97f, viewHeight*.87f, attackPanelPaint);	//BR
			
			c.drawText(GUIBattle.team1[GUIBattle.activeAlly].attack1.name, viewWidth*.12f, viewHeight*.25f, switchTextPaint);	//1
			c.drawText(GUIBattle.team1[GUIBattle.activeAlly].attack2.name, viewWidth*.62f, viewHeight*.25f, switchTextPaint);	//2
			c.drawText(GUIBattle.team1[GUIBattle.activeAlly].attack3.name, viewWidth*.12f, viewHeight*.65f, switchTextPaint);	//3
			c.drawText(GUIBattle.team1[GUIBattle.activeAlly].attack4.name, viewWidth*.62f, viewHeight*.65f, switchTextPaint);	//4
			
			c.drawText(""+GUIBattle.team1[GUIBattle.activeAlly].attack1.CurrentPP, viewWidth*.12f, viewHeight*.35f, switchTextPaint);	//1
			c.drawText(""+GUIBattle.team1[GUIBattle.activeAlly].attack3.CurrentPP, viewWidth*.12f, viewHeight*.75f, switchTextPaint);	//3
			c.drawText(""+GUIBattle.team1[GUIBattle.activeAlly].attack2.CurrentPP, viewWidth*.62f, viewHeight*.35f, switchTextPaint);	//2
			c.drawText(""+GUIBattle.team1[GUIBattle.activeAlly].attack4.CurrentPP, viewWidth*.62f, viewHeight*.75f, switchTextPaint);	//4
			
			c.drawText("/", viewWidth*.17f, viewHeight*.35f, switchTextPaint);
			c.drawText("/", viewWidth*.17f, viewHeight*.75f, switchTextPaint);
			c.drawText("/", viewWidth*.67f, viewHeight*.35f, switchTextPaint);
			c.drawText("/", viewWidth*.67f, viewHeight*.75f, switchTextPaint);
			
			c.drawText(""+GUIBattle.team1[GUIBattle.activeAlly].attack1.MaxPP, viewWidth*.20f, viewHeight*.35f, switchTextPaint);
			c.drawText(""+GUIBattle.team1[GUIBattle.activeAlly].attack3.MaxPP, viewWidth*.20f, viewHeight*.75f, switchTextPaint);
			c.drawText(""+GUIBattle.team1[GUIBattle.activeAlly].attack2.MaxPP, viewWidth*.70f, viewHeight*.35f, switchTextPaint);
			c.drawText(""+GUIBattle.team1[GUIBattle.activeAlly].attack4.MaxPP, viewWidth*.70f, viewHeight*.75f, switchTextPaint);
			
			return out;
		}
		
		public static Bitmap generateSwitch(float viewWidth, float viewHeight, AssetManager am)
		{
			Bitmap out=Bitmap.createBitmap((int)viewWidth, (int)viewHeight, Bitmap.Config.valueOf("ARGB_8888"));
			Canvas c=new Canvas(out);
			
			switch(GUIBattle.team1.length)
			{
			case 6:
				drawSwitchBox(c, .865f, 5, viewWidth, viewHeight, am);
			case 5:
				drawSwitchBox(c, .695f, 4, viewWidth, viewHeight, am);
			case 4:
				drawSwitchBox(c, .525f, 3, viewWidth, viewHeight, am);
			case 3:
				drawSwitchBox(c, .355f, 2, viewWidth, viewHeight, am);
			case 2:
				drawSwitchBox(c, .190f, 1, viewWidth, viewHeight, am);
			case 1:
				drawSwitchBox(c, .025f, 0, viewWidth, viewHeight, am);
			}
			
			return out;
		}
		
		private static void drawSwitchBox(Canvas c, float verticalOffset, int index, float viewWidth, float viewHeight, AssetManager am)
		{
			Paint koSwitchPanelPaint=new Paint();
			Paint switchPanelPaint=new Paint();
			Paint switchPanelTextPaint=new Paint();
			
			Paint highHealth=new Paint();
			Paint medHealth=new Paint();
			Paint lowHealth=new Paint();
			
			highHealth.setARGB(170, 0, 255, 0);
			medHealth.setARGB(170, 220, 220, 0);
			lowHealth.setARGB(170, 220, 0, 0);
			
			switchPanelTextPaint.setARGB(255, 255, 255, 255);
			switchPanelTextPaint.setShadowLayer(2f, 0, 0, Color.DKGRAY);
			switchPanelTextPaint.setStyle(Paint.Style.FILL);
			switchPanelTextPaint.setAntiAlias(true);
			
			float textScaleFactor=((float)viewWidth/480f);
			switchPanelTextPaint.setTextSize(15f*textScaleFactor);
			
			koSwitchPanelPaint.setShader(new LinearGradient(0, 60, viewWidth, 50, Color.RED, Color.DKGRAY, Shader.TileMode.valueOf("CLAMP")));
			switchPanelPaint.setShader(new LinearGradient(0, 60, viewWidth, 50, Color.CYAN, Color.DKGRAY, Shader.TileMode.valueOf("CLAMP")));
			
			if(GUIBattle.team1[index].hasFainted)
				c.drawRoundRect(new RectF(viewWidth*.02f, viewHeight*verticalOffset, viewWidth*.98f, viewHeight*(verticalOffset+.13f)), 5, 5, koSwitchPanelPaint);
			else
				c.drawRoundRect(new RectF(viewWidth*.02f, viewHeight*verticalOffset, viewWidth*.98f, viewHeight*(verticalOffset+.13f)), 5, 5, switchPanelPaint);
			
			c.drawBitmap(scaleBitmap(GUIBattle.team1[index].getFrontSprite(am), viewWidth, viewHeight), viewWidth*.03f, viewHeight*(verticalOffset-.01f), null);
			c.drawText(GUIBattle.team1[index].name, viewWidth*.15f, viewHeight*(verticalOffset+.05f), switchPanelTextPaint);
			c.drawText("Lv: "+GUIBattle.team1[index].level, viewWidth*.15f, viewHeight*(verticalOffset+.11f), switchPanelTextPaint);
			
			float healthPct=(float)GUIBattle.team1[index].currentHP/(float)GUIBattle.team1[index].maxHP;
			float AHBSX=(float) (viewWidth*.35f);
			float AHBSY=(float) (viewHeight*(verticalOffset+.03f));
			float AHBEY=(float) (viewHeight*(verticalOffset+.07f));
			float AHBL=((float) (viewWidth*.95f)-AHBSX)*healthPct;
			
			Paint temp=new Paint();
			
			if(healthPct>.5)
				temp.set(highHealth);
			if(healthPct>.25 && healthPct<=.5)
				temp.set(medHealth);
			if(healthPct<=.25)
				temp.set(lowHealth);
			
			RectF rect=new RectF(AHBSX-(viewWidth*.01f), AHBSY-(viewHeight*.01f), viewWidth*.96f, AHBEY+(viewHeight*.01f));
			c.drawRoundRect(rect, 3f, 3f, BottomPanel.types[15]);
			
			rect=new RectF(AHBSX, AHBSY, AHBSX+AHBL, AHBEY);
			c.drawRoundRect(rect, 3f, 3f, temp);
			
			c.drawText(""+GUIBattle.team1[index].currentHP, viewWidth*.5f, viewHeight*(verticalOffset+.11f), switchPanelTextPaint);
			c.drawText("/", viewWidth*.58f, viewHeight*(verticalOffset+.11f), switchPanelTextPaint);
			c.drawText(""+GUIBattle.team1[index].maxHP, viewWidth*.6f, viewHeight*(verticalOffset+.11f), switchPanelTextPaint);
		}
		
		public static Bitmap scaleBitmap(Bitmap input, float viewWidth, float viewHeight)
		{
			int imageWidth = input.getWidth(); //finds current image size
			int imageHeight = input.getHeight();
			
			int width=(int) (viewWidth*.12f);
			int height=(int) (viewHeight*.16f);
			
			float scaleWidth=((float)width) / imageWidth;
			float scaleHeight=((float)height) / imageHeight;
			
			Matrix matrix = new Matrix(); //requred stuff
			matrix.postScale(scaleWidth, scaleHeight);
			
			return Bitmap.createBitmap(input, 0, 0, imageWidth, imageHeight, matrix, true);
		}
	}
}