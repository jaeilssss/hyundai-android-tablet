package com.obigo.hkmotors.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.obigo.hkmotors.common.AdapterEventListener;
import com.obigo.hkmotors.common.Utility;
import com.obigo.hkmotors.model.FavoriteDataListItems;
import com.obigo.hkmotors.R;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;


/**
 * Class for my favorite with expandable list
 */
public class FavoriteExpendableListViewAdapter extends BaseAdapter {

    final static String TAG = "FavoriteExpendableListViewAdapter";

    private GroupViewHolder holder;

    private Context mContext;

    private ArrayList<FavoriteDataListItems> mItems;

    private AdapterEventListener mAdapterEventListener;

    private List<WeakReference<View>> mRecycleList = new ArrayList<WeakReference<View>>();
    private ArrayList<String> mCheckedId = new ArrayList<>();

    private boolean isCheckBoxShow = false;
    private int mPosition=0;


    /**
     * Constructor
     *
     * @param context - context
     */
    public FavoriteExpendableListViewAdapter(Context context) {

        this.mContext = context;
        mItems = new ArrayList<>();

    }

    public void setContents(ArrayList<FavoriteDataListItems> list) {
        mItems = list;
    }

    /**
     *  Class for group view holder
     */
    private static class GroupViewHolder {

        RelativeLayout layoutGroup;
        ImageView listBc;
        ImageView listArrow;
        TextView title;
        TextView date;
        TextView hour;

    }

    /**
     * Insert data
     * in this case, child has just one
     */
    public void addItem(FavoriteDataListItems items) {
        mItems.add(items);
    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public Object getItem(int position) {
        return mItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            holder = new GroupViewHolder();

            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.favorite_item_list, null);

            holder.layoutGroup = (RelativeLayout) convertView.findViewById(R.id.layout_group);
            holder.listBc = (ImageView) convertView.findViewById(R.id.iv_bc);
            holder.listArrow = (ImageView) convertView.findViewById(R.id.iv_arrow);
            holder.title = (TextView) convertView.findViewById(R.id.tv_title);
            holder.date = (TextView) convertView.findViewById(R.id.tv_date);
            holder.hour = (TextView) convertView.findViewById(R.id.tv_hour);

            convertView.setTag(holder);
        } else {
            holder = (GroupViewHolder) convertView.getTag();
        }

        // TODO : after deleting, why the count of groupPosition is not changed
        // I tested it by LG G Pro, it makes crash
        // but it is ok on Galaxy On 7
        // probably, it's android bug
        if(position < 0 || position >= mItems.size()) return null;

        holder.title.setText(mItems.get(position).getTitle());

        String[] date = mItems.get(position).getDate().split(" ");

        holder.date.setText(date[0]);
        holder.hour.setText(date[1]);
/*
        if(mItems.get(position).getChecked()) {
            holder.layoutGroup.setBackgroundResource(R.drawable.layout_full_border);
        } else {
            holder.layoutGroup.setBackgroundResource(0);
        }
        */
        //mGroupHolder.chk.setOnCheckedChangeListener(this);

        mRecycleList.add(new WeakReference<View>(convertView));

        return convertView;

    }

    /**
     * Show checkbox when edit mode is activated
     *
     * @param show show or hide
     */
    public void setShowCheckBox(boolean show) {
        this.isCheckBoxShow = show;
        notifyDataSetChanged();

        if(!show) {
            mCheckedId.clear();
        }
    }

    /**
     * Get checked id list
     */
    public ArrayList<String> getCheckedId(){
        return mCheckedId;
    }

    /**
     * Get checked id list
     */
    public void setCheckedId(ArrayList<String> mCheckedId){
        this.mCheckedId = mCheckedId;
    }
    /**
     * remove checked id
     *
     * @param id removed id
     */
    public void removeCheckedId(String id) {
        for(int i = mCheckedId.size()-1; i >=0 ; i--) {
            if(mCheckedId.get(i) == id) {
                mCheckedId.remove(i);
                break;
            }
        }
    }

    public void removeMitems(String id) {
        for(int i = mItems.size()-1; i >=0 ; i--) {
            if(mItems.get(i).getId() == id) {
                mItems.remove(i);
                break;
            }
        }
    }

    /**
     * Referenced resource will be recursively recycled
     */
    public void recycle() {
        for (WeakReference<View> ref : mRecycleList) {
            Utility.recursiveRecycle(ref.get());
        }
    }
}