/*******************************************************************************
 * Copyright 2013 CRS4
 *  
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package com.crs4.roodin.moduletester;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Typeface;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Style;
import android.graphics.drawable.PaintDrawable;
import android.util.AttributeSet;
import android.util.FloatMath;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.GestureDetector.OnDoubleTapListener;
import android.view.GestureDetector.OnGestureListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.view.animation.OvershootInterpolator;
import android.view.animation.Transformation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Scroller;
import android.widget.TextView;
import android.widget.ZoomButtonsController;
import android.widget.ZoomButtonsController.OnZoomListener;
import java.text.NumberFormat;


/**
 * @author ICT/LBS Team - CRS4 Sardinia, Italy
 *
 */
@SuppressLint("WrongCall")
public class MapScrollView extends ImageView implements OnGestureListener, OnDoubleTapListener, OnZoomListener {

	private static float[] SRC = new float[] {0, 0};
	private static final String TAG = "Scroll";
	private static final float MIN_ZOOM = 0.5f;
	private static final float MAX_ZOOM = 1.3f;
	private static final float ZOOM_FACTOR = MAX_ZOOM / MIN_ZOOM;

	private float mX;
	private float mY;
    private int mContentWidth;
    private int mContentHeight;
	private Scroller mScroller;
	private GestureDetector mGestureDetector;
	private float mScale;
	private ZoomButtonsController mZoomButtonsController;
	private NumberFormat mZoomFormat;
	private TextView mZoomLabel;
	private Matrix mMatrix;
	private float[] mDst;
    private OnDrawListener drawListener;
    private ZoomAnimation mZoomAnimation;
    private Path[] mPaths;
    private Paint mPathPaint;
    
    private boolean scrollingActive;
	private boolean editable = false;

	PointF mid = new PointF();
	// These matrices will be used to move and zoom image
	Matrix matrix = new Matrix();
	Matrix savedMatrix = new Matrix();
	
    // We can be in one of these 3 states
    static final int NONE = 0;
    static final int DRAG = 1;
    static final int ZOOM = 2;
    int mode = NONE;
    float oldDist = 1f;
    private Canvas canvasGlobal;

    
    
	public interface OnDrawListener {
	    public void onDraw(Canvas canvas, Matrix matrix);
	}
	
	

	/**
	 * @param value
	 */
	public void setEditable(boolean value){
		editable = value;
	}

	
	
	@Override
	protected void onDraw(Canvas canvas) {
//		System.out.println("MapScrollView.onDraw");

	    boolean isAnimating = false;
	    if (mZoomAnimation.hasStarted() && !mZoomAnimation.hasEnded()) {
    	    long t = AnimationUtils.currentAnimationTimeMillis();
    	    mZoomAnimation.getTransformation(t, null);
    	    isAnimating = true;
	    }
	    
		int saveCount = canvas.save();
		
		
		
		
		if (mScroller.computeScrollOffset()) {
			mX = mScroller.getCurrX();
			mY = mScroller.getCurrY();
			invalidate();
		}
		
		
		

		int w = getWidth();
		int h = getHeight();
		mMatrix.reset();
		
		float scaledWidth = mContentWidth * mScale;
		float scaledHeight = mContentHeight * mScale;

		
		float dx = scaledWidth > w? mX * mScale : (w - scaledWidth) / 2;
		float dy = scaledHeight > h? mY * mScale : (h - scaledHeight) / 2 ;
		
		mMatrix.preTranslate(dx, dy);
		
		float pivotX = 0;
		if (scaledWidth > w) {
			pivotX = Math.max(Math.min(-mX, w / 2), 2 * w - mContentWidth - mX);
		}
		
		float pivotY = 0;
		if (scaledHeight > h) {
			pivotY = Math.max(Math.min(-mY, h / 2), 2 * h - mContentHeight - mY);
		}
		
		
		mMatrix.preScale(mScale, mScale, pivotX, pivotY);
		canvas.concat(mMatrix);

		// draw content
		if (drawListener != null) {
		    drawListener.onDraw(canvas, mMatrix);
			//System.out.println("MapScrollView.onDraw2");
		}else{
			//System.out.println("drawListener == null");

		}
		
		canvasGlobal = canvas;
	}

	
	@Override
	protected void onDetachedFromWindow (){
		
	}
	
	/**
	 * @param context
	 */
	public MapScrollView(Context context) {
		this(context, null);
	}
	
	/**
	 * @param context
	 * @param attrs
	 */
	public MapScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mScroller = new Scroller(context);
		mGestureDetector = new GestureDetector(this);
		mScale = 1;
		mZoomButtonsController = new ZoomButtonsController(this);
		mZoomButtonsController.setAutoDismissed(true);
		mZoomButtonsController.setOnZoomListener(this);
		mZoomButtonsController.setZoomSpeed(25);
		mZoomButtonsController.setZoomInEnabled(mScale < MAX_ZOOM);
		mZoomButtonsController.setZoomOutEnabled(mScale > MIN_ZOOM);
		makeZoomLabel(context, mZoomButtonsController);
		
		mZoomFormat = NumberFormat.getPercentInstance();
		mZoomLabel.setText("Zoom: " + mZoomFormat.format(mScale));
		
		setVerticalScrollBarEnabled(true);
		setHorizontalScrollBarEnabled(true);
		TypedArray a = context.obtainStyledAttributes(R.styleable.Scroll);
		initializeScrollbars(a);
		a.recycle();
		mMatrix = new Matrix();
		mDst = new float[2];
		mZoomAnimation = new ZoomAnimation();
		mPaths = new Path[2];
		mPathPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mPathPaint.setStyle(Style.STROKE);
        mPathPaint.setStrokeCap(Cap.SQUARE);
        
     
	}
	
	
	/**
	 * @param newX
	 * @param newY
	 */
	public void mScrollTo(int newX, int newY){
		 mScroller.setFinalX((int) (-newX/mScale));
		 mScroller.setFinalY((int) (-newY/mScale));
		 
	}
	
	/**
	 * @param x
	 * @param y
	 */
	public void centerDisplayTo(float x, float y){
//		int width = (int) (getWidth() );
//		int height = (int) (getHeight() );
//		 
//		int x = (int) ((-f + (width/2))) ;
//		int y = (int) ((-g + (height/2))) ;
//		
//		mScroller.setFinalX(x);
//		mScroller.setFinalY(y);

		
		int newX = (int) (-x  + getLayoutParams().height/2);
		int newY = (int) (-y + getLayoutParams().height/2);

		

		mScroller.setFinalX(newX);
		mScroller.setFinalY(newY);

	}
	


    /**
     * @param listener
     * @param width
     * @param height
     */
    public void setOnDrawListener(OnDrawListener listener, int width, int height) {
	    drawListener = listener;
	    mContentWidth = width;
	    mContentHeight = height;
	}




    
    /**
     * @param scroll
     * @return
     */
    public boolean isScrolling(boolean scroll){
    	return scrollingActive;
    }


    @Override
	public int computeHorizontalScrollExtent() {
		return Math.round(computeHorizontalScrollRange() * getWidth() / (mContentWidth * mScale));
	}
	
	@Override
	public int computeHorizontalScrollOffset() {
		mMatrix.mapPoints(mDst, SRC);
		float x = -mDst[0] / mScale;
		return Math.round(computeHorizontalScrollRange() * x / mContentWidth);
	}
	
	@Override
	protected int computeVerticalScrollExtent() {
		return Math.round(computeVerticalScrollRange() * getHeight() / (mContentHeight * mScale));
	}
	
	@Override
	protected int computeVerticalScrollOffset() {
		mMatrix.mapPoints(mDst, SRC);
		float y = -mDst[1] / mScale;
		return Math.round(computeVerticalScrollRange() * y / mContentHeight);
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
		int minX = (getWidth() - mContentWidth);
		int minY = (getHeight() - mContentHeight);
		mScroller.fling((int) mX, (int) mY, (int) velocityX, (int) velocityY, minX, 0, minY, 0);
		invalidate();
		return true;
	}
	
	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
		mX -= distanceX / mScale;
		mY -= distanceY / mScale;
		mX = Math.max(getWidth() - mContentWidth, Math.min(0, mX));
		mY = Math.max(getHeight() - mContentHeight, Math.min(0, mY));
		return true;
	}
	
	
	@Override
	public boolean onDown(MotionEvent e) {
		mZoomButtonsController.setVisible(false);
		return true;
	}
	

	@Override
	public void onLongPress(MotionEvent e) {
		mZoomButtonsController.setVisible(true);
		
		if (editable = true){
			//(MyZoomScrollView)this.getContext()
		}
	}

	@Override
	public void onShowPress(MotionEvent e) {
		
	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		return false;
	}

	@Override
	public boolean onDoubleTap(MotionEvent e) {
//	    mX -= (e.getX() - getWidth() / 2) / mScale;
//        mY -= (e.getY() - getHeight() / 2) / mScale;
		mZoomAnimation.start(mScale < (MAX_ZOOM + MIN_ZOOM) / 2);
				
		return true;
	}

	@Override
	public boolean onDoubleTapEvent(MotionEvent e) {
		return false;
	}

	@Override
	public boolean onSingleTapConfirmed(MotionEvent e) {
		return false;
	}

	@Override
	public void onVisibilityChanged(boolean visible) {
	}

	@Override
	public void onZoom(boolean zoomIn) {
		changeZoom(zoomIn, 0.1f);
	}
	

	@Override
	public boolean onTouchEvent(MotionEvent event) {

		//Log.d("POINTERCOUNT", ""+event.getPointerCount());

		switch (event.getAction()) {
			case MotionEvent.ACTION_POINTER_DOWN:
				oldDist = spacing(event);
				//Log.d("OLDDIST", "oldDist=" + oldDist);
				
				if (oldDist > 100f && (event.getPointerCount() == 2)) {
					mode = ZOOM;
					//Log.d("ZOOM", "mode=ZOOM");
				}
				else {
					mode = DRAG;
				}
				break;
	
			case MotionEvent.ACTION_MOVE:
				mScroller.abortAnimation();
				if (mode == ZOOM && (event.getPointerCount() == 2)) {
					float newDist = spacing(event);
					//Log.d("ZOOM", "newDist=" + newDist);
					//Log.d("NEWDISTOLDDIST", "newDist=" + (newDist-oldDist));

					if (newDist > oldDist) {
						if ((newDist-oldDist) > 7 )       // avoid flickring
							setZoom(mScale+newDist/5000, false);
					}else{
						if ((oldDist-newDist) > 7 ) 
							setZoom(mScale-newDist/5000, false);
					}
					midPoint(event);
					oldDist = newDist;
					
				}
				break;
		}
		

		return mGestureDetector.onTouchEvent(event);
	}
	
	/**
	 * @param event
	 * @return
	 */
	private float spacing(MotionEvent event) {
		float x = event.getX(0) - event.getX(1);
		float y = event.getY(0) - event.getY(1);
		return FloatMath.sqrt(x * x + y * y);
	}
	
	/**
	 * @param event
	 */
	private void midPoint(MotionEvent event) {
		   float x = event.getX(0) + event.getX(1);
		   float y = event.getY(0) + event.getY(1);
			//Log.d("---CENTRO", " "+x / 2+"  "+y / 2);

	}
	

	// changes zoom. false for zoom out, true for zoom in.
	/**
	 * @param zoomIn
	 * @param delta
	 */
	public void changeZoom(boolean zoomIn, float delta) {
		setZoom(mScale + (zoomIn? delta : -delta), true);
	}
	
	/**
	 * @param zoom
	 * @param adjust
	 */
	public void setZoom(float zoom, boolean adjust) {
        mScale = zoom;
        Log.i("mScale",""+mScale);
        
        if (adjust) {
            mScale = Math.min(MAX_ZOOM, Math.max(MIN_ZOOM, mScale));
        }
        
        mZoomLabel.setText("Zoom: " + mZoomFormat.format(mScale));
        mZoomButtonsController.setZoomInEnabled(mScale != MAX_ZOOM);
        mZoomButtonsController.setZoomOutEnabled(mScale != MIN_ZOOM);

        invalidate();
    }

	/**
	 * @param context
	 * @param zoomController
	 */
	private void makeZoomLabel(Context context, ZoomButtonsController zoomController) {
		ViewGroup container = zoomController.getContainer();
		View controls = zoomController.getZoomControls();
		LayoutParams p0 = controls.getLayoutParams();
		container.removeView(controls);
		LinearLayout layout = new LinearLayout(context);
		layout.setOrientation(LinearLayout.VERTICAL);
		mZoomLabel = new TextView(context);
		mZoomLabel.setPadding(12, 0, 12, 0);
		mZoomLabel.setTypeface(Typeface.DEFAULT_BOLD);
		mZoomLabel.setTextColor(0xff000000);
		PaintDrawable d = new PaintDrawable(0xeeffffff);
		d.setCornerRadius(6);
		mZoomLabel.setBackgroundDrawable(d);
		mZoomLabel.setTextSize(20);
		mZoomLabel.setGravity(Gravity.CENTER_HORIZONTAL);
		LinearLayout.LayoutParams p1 = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		p1.gravity = Gravity.CENTER_HORIZONTAL;
		layout.addView(mZoomLabel, p1);
		layout.addView(controls);
		container.addView(layout, p0);
	}
	
	
	


    /**
     * @author ICT/LBS Team - CRS4 Sardinia, Italy
     *
     */
    class ZoomAnimation extends Animation {
	    private static final int DURATION = 1500;
        private static final float MAX_ALPHA = 0.75f;
        private float mFrom;
        private float mTo;
        private Interpolator mInInterpolator;
        private Interpolator mOutInterpolator;
        private Interpolator mInterpolator;
        private int mAlpha;
	    
        public ZoomAnimation() {
            mInInterpolator = new OvershootInterpolator(4);
            mOutInterpolator = new OvershootInterpolator(1.5f);
            setDuration(DURATION);
        }
        /**
         * @param zoomIn
         */
        public void start(boolean zoomIn) {
	        mFrom = mScale;
	        mTo = mScale < (MAX_ZOOM + MIN_ZOOM) / 2? MAX_ZOOM : MIN_ZOOM;
	        mInterpolator = zoomIn ? mInInterpolator : mOutInterpolator;
	        start();
            long t = AnimationUtils.currentAnimationTimeMillis();
            mZoomAnimation.getTransformation(t, null);
	    }
	    @Override
	    protected void applyTransformation(float interpolatedTime, Transformation t) {
	        float time = interpolatedTime;
	        float alpha = (float) Math.sin(time * Math.PI);
	        alpha = Math.min(alpha, MAX_ALPHA);
	        mAlpha = (int) (255 * alpha / MAX_ALPHA);
	        // i could use setInterpolator() but i also need to have linear alpha
	        interpolatedTime = mInterpolator.getInterpolation(time);
	        setZoom(mFrom + (mTo - mFrom) * interpolatedTime, false);
	    }
	}
    
    
}

//String names[] = { "DOWN", "UP", "MOVE", "CANCEL", "OUTSIDE",
//		"POINTER_DOWN", "POINTER_UP", "7?", "8?", "9?" };
//StringBuilder sb = new StringBuilder();
//int action = event.getAction();
//int actionCode = action & MotionEvent.ACTION_MASK;
//sb.append("event ACTION_").append(names[actionCode]);
//
//if (actionCode == MotionEvent.ACTION_POINTER_DOWN || actionCode == MotionEvent.ACTION_POINTER_UP) {
//	sb.append("(pid ").append(action >> MotionEvent.ACTION_POINTER_ID_SHIFT);
//	sb.append(")");
//}
//sb.append("[");
//
//for (int i = 0; i < event.getPointerCount(); i++) {
//	sb.append("#").append(i);
//	sb.append("(pid ").append(event.getPointerId(i));
//	sb.append(")=").append((int) event.getX(i));
//	sb.append(",").append((int) event.getY(i));
//	if (i + 1 < event.getPointerCount())
//		sb.append(";");
//}
//sb.append("]");
//
//Log.d(TAG, sb.toString());
