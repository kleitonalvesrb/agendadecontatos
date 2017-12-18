package kleitonbatista.com.agendadecontatos.helper;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by kleitonbatista on 15/12/2017.
 */

public class RecyclerItemClickListener implements RecyclerView.OnItemTouchListener{

    private OnItemClickListener mListenter;
    private GestureDetector mGestureDetector;


    public interface  OnItemClickListener{
        void onItemClick(View v, int position);
    }


    public RecyclerItemClickListener(Context context, OnItemClickListener listener){
        mListenter = listener;
        mGestureDetector = new GestureDetector(context,new GestureDetector.SimpleOnGestureListener(){
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }
        });
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        View childView = rv.findChildViewUnder(e.getX(),e.getY());
        if (childView != null && mListenter != null & mGestureDetector.onTouchEvent(e)){
            mListenter.onItemClick(childView,rv.getChildAdapterPosition(childView));
        }
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {

    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }
}
