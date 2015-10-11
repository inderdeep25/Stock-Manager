

package com.coa.coastock;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.coa.coastock.R;

import java.util.ArrayList;
import java.util.HashMap;

public class CustomListAdapter extends RecyclerView.Adapter<CustomListAdapter.ViewHolder>{

    Context context;
    ArrayList<HashMap<String,String>> modelList;

    public CustomListAdapter(Context context, ArrayList<HashMap<String,String>> modelList){

        this.context = context;
        this.modelList = modelList;

    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.product_list_item, parent,false);
        final ViewHolder viewHolder = new ViewHolder(itemLayoutView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.setPos(position);

        holder.indexLabel.setText(Integer.toString(position + 1)+". ");
        holder.productNameLabel.setText(modelList.get(position).get("name"));
        holder.productQuantity.setText(modelList.get(position).get("quantity"));

    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{



        public TextView productNameLabel;
        public TextView productQuantity;
        public TextView indexLabel;

        private int pos = -1;

        public int getPos() {
            return pos;
        }

        public void setPos(int pos) {
            this.pos = pos;
        }

        public ViewHolder(View itemView) {
            super(itemView);

            indexLabel = (TextView) itemView.findViewById(R.id.index);
            productNameLabel = (TextView) itemView.findViewById(R.id.name);
            productQuantity = (TextView) itemView.findViewById(R.id.quantity);


        }
    }
}