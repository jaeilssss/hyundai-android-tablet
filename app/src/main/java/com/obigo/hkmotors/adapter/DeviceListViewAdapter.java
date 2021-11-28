package com.obigo.hkmotors.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.obigo.hkmotors.common.Utility;
import com.obigo.hkmotors.model.DeviceDataListItems;
import com.obigo.hkmotors.R;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;


/**
 * Class for device list
 */
public class DeviceListViewAdapter extends BaseAdapter {

    private ViewHolder  mHolder;

    private ArrayList<DeviceDataListItems> listViewItemList = new ArrayList<DeviceDataListItems>() ;

    private List<WeakReference<View>> mRecycleList = new ArrayList<WeakReference<View>>();


    /**
     * Constructor
     */
    public DeviceListViewAdapter() {}

    /**
     * Add items
     *
     * @param name - name
     * @param mac - mac address
     */
    public void addItem(String name, String mac) {
        DeviceDataListItems item = new DeviceDataListItems();

        item.setObdListName(name);
        item.setObdListMac(mac);

        listViewItemList.add(item);
    }

    @Override
    public int getCount() {
        return listViewItemList.size();
    }

    @Override
    public Object getItem(int position) {
        return listViewItemList.get(position) ;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        if (convertView == null) {

            convertView = LayoutInflater.from(context).inflate(R.layout.device_list_item, null);
            mHolder = new ViewHolder();

            mHolder.name = (TextView) convertView.findViewById(R.id.obd_list_name) ;
            mHolder.mac = (TextView) convertView.findViewById(R.id.obd_list_mac) ;

            convertView.setTag(mHolder);
        } else {
            mHolder = (ViewHolder) convertView.getTag();
        }

        DeviceDataListItems listViewItem = listViewItemList.get(position);

        mHolder.name.setText(listViewItem.getObdListName());
        mHolder.mac.setText(listViewItem.getObdListMac());

        mRecycleList.add(new WeakReference<View>(convertView));

        return convertView;
    }

    /**
     * Class for view holder
     */
    private static class ViewHolder {
        TextView name;
        TextView mac;
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
