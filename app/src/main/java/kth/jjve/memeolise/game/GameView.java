package kth.jjve.memeolise.game;

import android.content.Context;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import android.util.AttributeSet; //needed to have preview!!!!
import android.view.View;



public class GameView extends View {

    private Paint paint;

    private float boardSize = getWidth();
    public static final int SIZE = 3; //in tictactoe is in game logic, better there?
    private float cellSize = boardSize / SIZE;


    public GameView(Context context) {
        super(context);
    }


    // first paint object needs to be created
    private void init(){
        paint = new Paint(); //Paint provides methods to define that line's color
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
    }

    // called when Android is trying to figure out the (new) size for this view
    // necessary to adapt view to tablets nd stuff
    @Override
    protected void onMeasure(int width, int height) {
        super.onMeasure(width, height);
        int squareSide = Math.min(getMeasuredWidth(), getMeasuredHeight());
        float boardSize = squareSide;
        cellSize = boardSize / SIZE;
        setMeasuredDimension(squareSide, squareSide); // make it a square
    }

    public float getCellSize() {
        return cellSize;
    }

    //Canvas provides a method to draw a line
    @Override
    protected void onDraw(Canvas canvas) {
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
