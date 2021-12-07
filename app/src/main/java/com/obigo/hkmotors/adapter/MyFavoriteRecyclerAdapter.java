package com.obigo.hkmotors.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.obigo.hkmotors.R;
import com.obigo.hkmotors.model.FavoriteData;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class MyFavoriteRecyclerAdapter extends RecyclerView.Adapter<MyFavoriteRecyclerAdapter.MyFavoriteRecyclerViewHolder> {

    public ArrayList<FavoriteData> list = new ArrayList<>();

    public MyFavoriteRecyclerAdapter(ArrayList<FavoriteData> list) {
        this.list = list;
    }

    @Override
    public MyFavoriteRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater =  (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = layoutInflater.inflate(R.layout.my_favorite_item,parent,false);
        return new MyFavoriteRecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyFavoriteRecyclerViewHolder holder, int position) {

    }


    @Override
    public int getItemCount() {
        return 10;
    }
    public class MyFavoriteRecyclerViewHolder extends RecyclerView.ViewHolder{

        TextView title ;
        TextView date;
        MyFavoriteRecyclerViewHolder(View itemView)
        {
            super(itemView);

            title = itemView.findViewById(R.id.my_favorite_item_title);
            date = itemView.findViewById(R.id.my_favorite_item_date);
        }

        public void setData(String title , String date){
            this.title.setText(title);
            this.date.setText(date);
        }
    }
}
