package com.obigo.hkmotors.adapter;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.obigo.hkmotors.R;
import com.obigo.hkmotors.common.OnItemClickListener;
import com.obigo.hkmotors.model.FavoriteData;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class MyFavoriteRecyclerAdapter extends RecyclerView.Adapter<MyFavoriteRecyclerAdapter.MyFavoriteRecyclerViewHolder> {

    public OnItemClickListener onItemClickListener;

    public int clickIndex = 0;


    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }


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
        holder.setData(list.get(position).getTitle(),list.get(position).getDate(),position);
    }


    @Override
    public int getItemCount() {
        return list.size();
    }
    public class MyFavoriteRecyclerViewHolder extends RecyclerView.ViewHolder{

        TextView title ;
        TextView date;
        ConstraintLayout layout;
        MyFavoriteRecyclerViewHolder(View itemView)
        {
            super(itemView);

            title = itemView.findViewById(R.id.my_favorite_item_title);
            date = itemView.findViewById(R.id.my_favorite_item_date);
            layout = itemView.findViewById(R.id.favorite_layout);
            layout.setOnClickListener(new ButtonClick());
        }

        public void setData(String title , String date,int position){
            this.title.setText(title);
            this.date.setText(date);
            if(clickIndex==position){
                layout.setBackgroundResource(R.drawable.favorite_border_5_selected);
            }else{
                layout.setBackgroundResource(R.drawable.favorite_border_5_grey);
            }
        }

        public class ButtonClick implements View.OnClickListener{


            @Override
            public void onClick(View view) {
                if(onItemClickListener!=null){
                    if(getAdapterPosition() != RecyclerView.NO_POSITION){
                        if(view!=null){
                            onItemClickListener.onItemClick(view, getAdapterPosition());
                        }
                    }
                }
            }
        }
    }
}
