package edu.skku.cs.pa2;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;

import edu.skku.cs.pa2.util.MazeInfo;
import edu.skku.cs.pa2.util.MazeListViewModel;

public class MazeAdapter extends RecyclerView.Adapter<MazeAdapter.ViewHolder> {

    ArrayList<MazeInfo> mazeInfo= new ArrayList<>();
    MazeListViewModel viewModel;

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView tv_mazeName;
        TextView tv_mazeSize;
        Button btn_start;
        public ViewHolder(View view){
            super(view);
            tv_mazeName=view.findViewById(R.id.tv_name);
            tv_mazeSize=view.findViewById(R.id.tv_size);
            btn_start = view.findViewById(R.id.btn_start);
        }
    }
    public MazeAdapter(MazeListViewModel viewModel){
        this.viewModel = viewModel;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.item_maze,parent,false);
        MazeAdapter.ViewHolder vh =  new MazeAdapter.ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tv_mazeSize.setText(Integer.toString(mazeInfo.get(position).getSize()));
        holder.tv_mazeName.setText(mazeInfo.get(position).getName());
        holder.btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewModel.setMoveToMaze(mazeInfo.get(position).getName());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mazeInfo.size();
    }

    public void setMazeList(ArrayList arrayList){
        this.mazeInfo = arrayList;
        Log.d("Maze_List_set",Integer.toString(mazeInfo.size()));
        notifyDataSetChanged();
    }

}
