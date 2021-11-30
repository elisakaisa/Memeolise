package kth.jjve.memeolise.game;

import static android.view.ViewGroup.*;

import android.content.Context;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import android.util.AttributeSet; //needed to have preview!!!!
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import kth.jjve.memeolise.GameActivity;


public class GameView extends ViewGroup {

    private static final String LOG_TAG = GameView.class.getSimpleName();
    private Paint paint;

    private float boardSize = getWidth();

    //TODO belongs in game logic or game view?
    public static final int SIZE = 3;
    private float cellSize = boardSize / SIZE;
    private int childCount;

    boolean bCheck = true;


    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);

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
        int count = getChildCount(); //child count is 3 for some reason
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
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
    //TODO: figure out what to do with this, probs sets layout for children
        Log.d(LOG_TAG, "onLayout(" + changed + ", " + left + ", "  + top + ", " + right + ", " + bottom + ")");
        childCount = getChildCount();
        for(int i=0; i<childCount;i++) {
            View v = getChildAt(i);
            v.layout(left, top, right, bottom);
        }
    }


}
