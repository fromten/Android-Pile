package learn.example.pile.activity.base;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import learn.example.pile.R;

/**
 * Created on 2016/7/31.
 */
public class FullScreenActivity extends AppCompatActivity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.FullScreenTheme);
        if (getSupportActionBar()!=null)
        {
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            enableSupportHomeUp();
        }
    }

    public void enableSupportHomeUp()
    {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home)
        {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void showActionBar()
    {
        if (getSupportActionBar()!=null)
        {
            getSupportActionBar().show();
        }
    }

    public void hideActionBar()
    {
        if (getSupportActionBar()!=null)
        {
            getSupportActionBar().hide();
        }
    }


}
