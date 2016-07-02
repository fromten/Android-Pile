package learn.example.uidesign;

import android.content.Context;
import android.content.res.TypedArray;
import android.preference.Preference;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.Spinner;

import java.util.Arrays;
import java.util.List;

/**
 * Created on 2016/7/2.
 */
public class SpinnerPreference extends Preference {

    private Spinner mSpinner;
    private Button mButton;
    private CharSequence[] mEntries;
  //  private CharSequence[] mEntryValues;
    public SpinnerPreference(Context context) {
        this(context,null);
    }

    public SpinnerPreference(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public SpinnerPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a=context.obtainStyledAttributes(attrs,R.styleable.SpinnerPreference);
        mEntries=a.getTextArray(R.styleable.SpinnerPreference_entries);
      //  mEntryValues = a.getTextArray(R.styleable.SpinnerPreference_entryValues);
        a.recycle();
        setLayoutResource(R.layout.preference_spinner);

    }

    @Override
    protected void onBindView(View view) {
        super.onBindView(view);
        mSpinner= (Spinner) view.findViewById(R.id.spinner);
        if (mSpinner!=null)
        {
            ArrayAdapter<CharSequence> adapter=new ArrayAdapter<>(getContext(),android.R.layout.simple_list_item_1,mEntries);
            adapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
            mSpinner.setAdapter(adapter);
        }
    }

}
