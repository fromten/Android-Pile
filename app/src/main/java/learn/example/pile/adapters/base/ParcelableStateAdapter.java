package learn.example.pile.adapters.base;

import android.os.Parcelable;
import android.support.v7.widget.RecyclerView;

/**
 * Created on 2016/10/4.
 */

public abstract class ParcelableStateAdapter<T extends Parcelable,VH extends RecyclerView.ViewHolder> extends AbsSaveableRVAdapter<T,VH> {

    //empty
}
