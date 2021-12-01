package kth.jjve.memeolise.game;


import android.content.Context;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import android.util.AttributeSet; //needed to have preview!!!!
import android.widget.LinearLayout;


public class GameView extends LinearLayout {

    private static final String LOG_TAG = GameView.class.getSimpleName();
    private Paint paint;

    private float boardSize = getWidth();

    public static final int SIZE = 3;
    private float cellSize = boardSize / SIZE;

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);

        paint = new Paint(); //Paint provides methods to define that line's color
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        setWillNotDraw(false); //without this line -> no grid in otherwise
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // called when Android is trying to figure out the (new) size for this view
        // necessary to adapt view to tablets
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int squareSide = Math.min(width, height);
        super.onMeasure(MeasureSpec.makeMeasureSpec(squareSide, MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(squareSide, MeasureSpec.EXACTLY));
        setMeasuredDimension(squareSide, squareSide); // make it a square

    }

    @Override
    protected void onDraw(Canvas canvas) {
        //method to draw the grid on the canvas
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(16);

        float boardSize = canvas.getWidth();
        float cellSize = boardSize / SIZE;
        for (int step = 1; step < SIZE; step++) {
            canvas.drawLine(
                    0, cellSize * step, boardSize, cellSize * step, paint);
            canvas.drawLine(
                    cellSize * step, 0, cellSize * step, boardSize, paint);
        }
    }



}
