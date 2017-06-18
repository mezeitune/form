package com.adox.matias.formadox.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;

import java.util.ArrayList;

public class BrushView extends View {
	private Paint brush = new Paint();
	private Paint mPaint = new Paint();
	private Path path = new Path();
	public Button btnEraseAll;
	public LayoutParams params;

	public BrushView(Context context) {
		super(context);
		brush.setAntiAlias(true);




		brush.setColor(Color.BLACK);
		brush.setStyle(Paint.Style.STROKE);
		brush.setStrokeJoin(Paint.Join.ROUND);
		brush.setStrokeWidth(15f);

		btnEraseAll = new Button(context);
		btnEraseAll.setText("Erase Everything!!");
		params = new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT);
		btnEraseAll.setLayoutParams(params);

	}

	public void borr(){

				path.reset();
				// invalidate the view
				postInvalidate();


	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		float pointX = event.getX();
		float pointY = event.getY();

		// Checks for the event that occurs
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			path.moveTo(pointX, pointY);

			return true;
		case MotionEvent.ACTION_MOVE:
			path.lineTo(pointX, pointY);
			break;
		case MotionEvent.ACTION_UP:
			break;
		default:
			return false;
		}
		// Force a view to draw.
		postInvalidate();
		return false;

	}
	@Override
	protected void onDraw(Canvas canvas) {
		int width = this.getWidth();
		int height = this.getHeight();
		int radius = width > height ? height/2 : width/2;
		int center_x = width/2;
		int center_y = height/2;

		mPaint.setColor(Color.WHITE);
		mPaint.setStyle(Paint.Style.FILL); //fill the background with blue color
		canvas.drawRect(0, 0, width, height, mPaint);


		canvas.drawPath(path, brush);

	}
}
