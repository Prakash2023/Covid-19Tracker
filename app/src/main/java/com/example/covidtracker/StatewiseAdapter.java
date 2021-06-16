package com.example.covidtracker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.text.NumberFormat;
import java.util.ArrayList;

public class StatewiseAdapter extends RecyclerView.Adapter<StatewiseAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<Statewisemodel>statewisemodelArrayList;

    public StatewiseAdapter(Context mContext,ArrayList<Statewisemodel>arrayList){
        this.mContext = mContext;
        this.statewisemodelArrayList=arrayList;
    }
    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.tmp, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull StatewiseAdapter.ViewHolder holder, int position) {
        Statewisemodel currentItem = statewisemodelArrayList.get(position);
        String stateName = currentItem.getState();
        String stateTotal = currentItem.getConfirmed();
        String stateactive=currentItem.getActive();
        String staterecover=currentItem.getRecovered();
        String statedeath=currentItem.getDeath();
        String statenewcase=currentItem.getConfirmed_new();
        String statenewrecover=currentItem.getRecovered_new();
        String statenewdeath=currentItem.getDeath_new();
        holder.tv_stateName.setText(stateName);
        holder.tv_state_active.setText(NumberFormat.getInstance().format(Integer.parseInt(stateactive)));
        holder.tv_state_recover.setText(NumberFormat.getInstance().format(Integer.parseInt(staterecover)));
        holder.tv_state_death.setText(NumberFormat.getInstance().format(Integer.parseInt(statedeath)));
        holder.tv_stateTotalCases.setText(NumberFormat.getInstance().format(Integer.parseInt(stateTotal)));
        holder.tv_state_new_case.setText("+"+NumberFormat.getInstance().format(Integer.parseInt(statenewcase)));
        holder.tv_state_new_recover.setText("+"+NumberFormat.getInstance().format(Integer.parseInt(statenewrecover)));
        holder.tv_state_new_death.setText("+"+NumberFormat.getInstance().format(Integer.parseInt(statenewdeath)));


    }
    public void filterList(ArrayList<Statewisemodel> filteredList) {
        statewisemodelArrayList = filteredList;
        //this.searchText = text;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return statewisemodelArrayList==null ? 0 : statewisemodelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_stateName, tv_stateTotalCases,tv_state_active,tv_state_recover,tv_state_death,tv_state_new_case,tv_state_new_active,tv_state_new_recover,tv_state_new_death;
        LinearLayout lin_state_wise;
        GridLayout gridLayout;
        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            tv_stateName = itemView.findViewById(R.id.statewise_grid_name_textView);
            tv_stateTotalCases = itemView.findViewById(R.id.statewise_grid_confirmed_textView);
            tv_state_active=itemView.findViewById(R.id.statewise_grid_active_textView);
            tv_state_recover=itemView.findViewById(R.id.statewise_grid_recovered_textView);
            tv_state_death=itemView.findViewById(R.id.statewise_grid_death_textView);
            tv_state_new_case=itemView.findViewById(R.id.statewise_grid_new_confirmed_textView);
            tv_state_new_recover=itemView.findViewById(R.id.statewise_grid_new_recovered_textView);
            tv_state_new_death=itemView.findViewById(R.id.new_death_statewise_grid);

           lin_state_wise = itemView.findViewById(R.id.statewise_grid_layout);

            //gridLayout=itemView.findViewById(R.id.grid_layout_statewise);

        }
    }
}
