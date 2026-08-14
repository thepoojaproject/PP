package com.pingpong.game;

import android.content.Context;
import android.graphics.*;
import android.view.*;
import java.util.Random;

public class PongView extends View {
    Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
    Random rnd = new Random();
    float ballX, ballY, vx, vy, playerY, aiY;
    float paddleW, paddleH, ballR;
    int playerScore=0, aiScore=0;
    boolean running=false;
    long last;
    int green = Color.rgb(131,193,66);

    public PongView(Context c) { super(c); p.setTypeface(Typeface.create("sans", Typeface.BOLD)); }

    void resetBall(boolean toPlayer) {
        ballX=getWidth()/2f; ballY=getHeight()/2f;
        float speed=Math.max(500,getWidth()*0.8f);
        vx=(toPlayer?-1:1)*speed;
        vy=(rnd.nextBoolean()?1:-1)*speed*.45f;
        playerY=aiY=getHeight()/2f;
    }

    void start() { playerScore=aiScore=0; running=true; resetBall(false); last=System.nanoTime(); invalidate(); }

    @Override protected void onSizeChanged(int w,int h,int ow,int oh) {
        paddleW=Math.max(22,w*.035f); paddleH=Math.max(110,h*.18f); ballR=Math.max(10,w*.018f);
        if (!running) resetBall(false);
    }

    @Override protected void onDraw(Canvas c) {
        super.onDraw(c);
        c.drawColor(Color.rgb(7,17,10));
        p.setColor(Color.rgb(18,42,22)); p.setStrokeWidth(4);
        for(int y=20;y<getHeight();y+=45) c.drawLine(getWidth()/2f,y,getWidth()/2f,y+22,p);

        p.setColor(Color.WHITE);
        p.setTextSize(Math.max(50,getWidth()*.11f)); p.setTextAlign(Paint.Align.CENTER);
        c.drawText(""+playerScore,getWidth()*.35f,getHeight()*.14f,p);
        c.drawText(""+aiScore,getWidth()*.65f,getHeight()*.14f,p);

        p.setColor(green);
        c.drawRoundRect(25,playerY-paddleH/2,25+paddleW,playerY+paddleH/2,18,18,p);
        c.drawRoundRect(getWidth()-25-paddleW,aiY-paddleH/2,getWidth()-25,aiY+paddleH/2,18,18,p);
        c.drawCircle(ballX,ballY,ballR,p);

        if(!running){
            p.setColor(Color.WHITE); p.setTextSize(Math.max(28,getWidth()*.06f));
            c.drawText("PING PONG",getWidth()/2f,getHeight()*.40f,p);
            p.setTextSize(Math.max(17,getWidth()*.035f));
            c.drawText("TAP TO START • DRAG TO MOVE",getWidth()/2f,getHeight()*.48f,p);
        } else update();
    }

    void update() {
        long now=System.nanoTime(); float dt=Math.min(.025f,(now-last)/1_000_000_000f); last=now;
        ballX+=vx*dt; ballY+=vy*dt;
        if(ballY<ballR){ballY=ballR;vy=Math.abs(vy);}
        if(ballY>getHeight()-ballR){ballY=getHeight()-ballR;vy=-Math.abs(vy);}

        float target=ballY;
        aiY += (target-aiY)*Math.min(1,dt*4.2f);

        if(ballX-ballR<25+paddleW && ballX>25 && Math.abs(ballY-playerY)<paddleH/2+ballR) {
            ballX=25+paddleW+ballR; vx=Math.abs(vx)*1.04f;
            vy += (ballY-playerY)*4;
        }
        if(ballX+ballR>getWidth()-25-paddleW && ballX<getWidth()-25 && Math.abs(ballY-aiY)<paddleH/2+ballR) {
            ballX=getWidth()-25-paddleW-ballR; vx=-Math.abs(vx)*1.04f;
            vy += (ballY-aiY)*4;
        }
        if(ballX<-ballR){aiScore++; resetBall(false);}
        if(ballX>getWidth()+ballR){playerScore++; resetBall(true);}
        invalidate();
    }

    @Override public boolean onTouchEvent(android.view.MotionEvent e) {
        float y=e.getY();
        if(e.getAction()==MotionEvent.ACTION_DOWN && !running){start(); return true;}
        if(running && (e.getAction()==MotionEvent.ACTION_DOWN || e.getAction()==MotionEvent.ACTION_MOVE)) {
            playerY=Math.max(paddleH/2,Math.min(getHeight()-paddleH/2,y)); invalidate(); return true;
        }
        return true;
    }
}
