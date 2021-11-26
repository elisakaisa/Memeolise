package kth.jjve.memeolise.game;

import android.content.Context;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import android.util.AttributeSet; //needed to have preview!!!!
import android.view.View;
import android.view.ViewGroup;



public class GameView extends ViewGroup {

    private Paint paint;

    private float boardSize = getWidth();

    //TODO belongs in game logic or game view?
    public static final int SIZE = 3;
    private float cellSize = boardSize / SIZE;


    public GameView(Context context, AttributeSet attrs) {
        super(context);

        paint = new Paint(); //Paint provides methods to define that line's color
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        setWillNotDraw(false); //without this line -> no grid in viewGroup
    }

    @Override
    protected void onMeasure(int width, int height) {
        // called when Android is trying to figure out the (new) size for this view
        // necessary to adapt view to tablets
        super.onMeasure(width, height);
        int squareSide = Math.min(getMeasuredWidth(), getMeasuredHeight());
        float boardSize = squareSide;
        cellSize = boardSize / SIZE;
        setMeasuredDimension(squareSide, squareSide); // make it a square
    }

    public float getCellSize() {
        return cellSize;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //Canvas provides a method to draw a line
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

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

    }


}
