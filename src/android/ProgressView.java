package cordova.plugins.capturephotovideo;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Progress bar on the top of screen
 * 
 * @author jiang
 * 
 */
public class ProgressView extends View {

	private final Paint mPaint = new Paint();
	private int shouldBeWidth = 0;

	public void setWidth(int width) {
		shouldBeWidth = width;
	}

	public ProgressView(Context context) {
		super(context);
		init();
	}

	public ProgressView(Context context, AttributeSet paramAttributeSet) {
		super(context, paramAttributeSet);
		init();
	}

	public ProgressView(Context context, AttributeSet paramAttributeSet,
			int paramInt) {
		super(context, paramAttributeSet, paramInt);
		init();
	}

	private void init() {
		this.mPaint.setStyle(Paint.Style.FILL);
		this.mPaint.setColor(getResources().getColor(Constant.color.vine_green));
	}

	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if (this.shouldBeWidth > 0) {
			canvas.drawRect(0.0F, 0.0F, this.shouldBeWidth,
					getMeasuredHeight(), mPaint);
		}
	};
}
