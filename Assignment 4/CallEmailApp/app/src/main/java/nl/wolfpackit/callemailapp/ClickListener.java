package nl.wolfpackit.callemailapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

public class ClickListener implements RecyclerView.OnItemTouchListener {

    private GestureDetector mGestureDetector;
    private itemClickListener clickListener;

    public ClickListener(Context context, final RecyclerView rv, itemClickListener cl){
        clickListener = cl;
        mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener(){

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }

            @Override
            public void onLongPress(MotionEvent e) {
                View childView = rv.findChildViewUnder(e.getX(), e.getY());
                if (childView != null && clickListener != null) {
                    clickListener.onItemLongClick(childView, rv.getChildAdapterPosition(childView));
                }
            }
        });
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        View childView = rv.findChildViewUnder(e.getX(), e.getY());
        if(childView != null && clickListener != null & mGestureDetector.onTouchEvent(e)) {
            clickListener.onItemClick(childView, rv.getChildAdapterPosition(childView));
            return true;
        }
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {

    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }

    public interface itemClickListener {
        void onItemClick(View view, int position);
        void onItemLongClick(View view, int position);
    }
}

