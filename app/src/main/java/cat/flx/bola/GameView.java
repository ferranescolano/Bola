package cat.flx.bola;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

public class GameView extends View {

    public static final int DESIRED_DELTA_T = 50;
    public static final int BALL_RADIUS = 20;
    public static final float IMPACT = 0.85f;
    public static final float ACCEL_FACTOR = 3.0f;
    protected Paint paint;
    protected Handler handler;

    public GameView(Context context) { this(context, null, 0); }
    public GameView(Context context, @Nullable AttributeSet attrs) { this(context, attrs, 0); }
    public GameView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setTextSize(25);
        handler = new Handler();
        handler.postDelayed(runnable, DESIRED_DELTA_T);
    }

    private long before = -1;

    private Runnable runnable = new Runnable() {
        @Override public void run() {
            handler.postDelayed(runnable, DESIRED_DELTA_T);
            long now = System.currentTimeMillis();
            if (before == -1) before = now;
            float deltaT = (now - before) / 1000.0f * ACCEL_FACTOR;
            before = now;
            updatePhysics(deltaT);
            invalidate();
        }
    };

    @Override public void onDraw(Canvas canvas) {
        canvas.drawColor(Color.LTGRAY);
        int w = getWidth();
        int h = getHeight();
        canvas.translate(w/2, h/2);
        canvas.drawCircle(ballX, ballY, BALL_RADIUS, paint);
        String text = "X=" + (int)ballX + " Y=" + (int)ballY;
        canvas.drawText(text, -w/2, h/2 - 20, paint);
    }

    private float ballX, ballY, vX, vY, aX, aY;

    public void setAccel(float aX, float aY) {
        this.aX = aX;
        this.aY = aY;
    }

    protected void updatePhysics(float deltaT) {
        vX += aX * deltaT;
        vY += aY * deltaT;
        ballX += vX * deltaT;
        ballY += vY * deltaT;
        int w = getWidth();
        int h = getHeight();
        boolean impact = false;
        if (ballX < -w / 2 + BALL_RADIUS) { impact = true; vX = -IMPACT * vX; ballX = -w / 2 + BALL_RADIUS; }
        if (ballX > +w / 2 - BALL_RADIUS) { impact = true; vX = -IMPACT * vX; ballX = +w / 2 - BALL_RADIUS; }
        if (ballY < -h / 2 + BALL_RADIUS) { impact = true; vY = -IMPACT * vY; ballY = -h / 2 + BALL_RADIUS; }
        if (ballY > +h / 2 - BALL_RADIUS) { impact = true; vY = -IMPACT * vY; ballY = +h / 2 - BALL_RADIUS; }
        if (impact) {
            mainActivity.clic();
        }
    }

    private MainActivity mainActivity;
    public void setMainActivity(MainActivity mainActivity) { this.mainActivity = mainActivity; }

}
