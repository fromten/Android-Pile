package learn.example.pile.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;

import com.bumptech.glide.Glide;

import learn.example.pile.R;

/**
 * Created on 2016/8/13.
 */
public class NineTableLayout extends TableLayout implements View.OnClickListener {

    private final int MAX_COLUMN=3;
    private OnItemClickListener mItemClickListener;

    public NineTableLayout(Context context) {
        this(context,null);
    }

    public NineTableLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }




    private TableRow createRow()
    {
        TableRow rowView=new TableRow(getContext());
        TableRow.LayoutParams rowParams=new TableRow.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        rowParams.gravity= Gravity.CENTER;
        rowView.setLayoutParams(rowParams);
        for (int i = 0; i < 3; i++) {
            ImageView imageView = new ImageView(getContext());
            TableRow.LayoutParams  params = new TableRow.LayoutParams (0, getWidth()/3);
            imageView.setLayoutParams(params);
            params.weight=1;
            params.setMargins(0,0,20,20);
            rowView.addView(imageView);
        }
        return rowView;
    }


    public void setUrls(String[] urls)
    {
        if (urls == null) {
            removeAllViews();
            return;
        }
        final int urlLen = urls.length;
        final int rowNum=urlLen/3+(urlLen%3==0?0:1);
        int i=0;
        for (; i < rowNum; i++) {
            TableRow row= (TableRow)getChildAt(i);
            if (row==null)
            {
                row=createRow();
                this.addView(row);
            }
            final int start=3*i;
            for (int j = 0; j < 3; j++) {
                ImageView image= (ImageView) row.getChildAt(j);
                image.setOnClickListener(this);
                int index=start+j;
                image.setTag(R.id.view_tag1,index);
                if (index<urlLen)
                {
                    image.setVisibility(View.VISIBLE);
                    Glide.with(getContext()).load(urls[index]).centerCrop().into(image);
                }else {
                    image.setVisibility(View.INVISIBLE);
                }
            }
        }
        removeViews(i,getChildCount()-rowNum);
    }

    @Override
    public void onClick(View v) {
        if (mItemClickListener!=null)
        {
            mItemClickListener.onItemClick(v,this, (Integer) v.getTag(R.id.view_tag1));
        }
    }

    public OnItemClickListener getItemClickListener() {
        return mItemClickListener;
    }

    public void setItemClickListener(OnItemClickListener itemClickListener) {
        mItemClickListener = itemClickListener;
    }

    public interface OnItemClickListener{
        void onItemClick(View view,ViewGroup parent,int position);
    }
}
