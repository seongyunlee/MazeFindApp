package edu.skku.cs.pa2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.ArrayList;

import edu.skku.cs.pa2.util.ConnectionUtil;
import edu.skku.cs.pa2.util.MapViewModel;

public class MapAdapter extends BaseAdapter {
    MapViewModel viewModel;
    ArrayList<BoxInfo> boxInfos = new ArrayList<>();

    Context context;
    @Override
    public int getCount() {
        return boxInfos.size();
    }

    @Override
    public Object getItem(int i) {
        return boxInfos.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        context= viewGroup.getContext();
        if(view==null){
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.item_box,viewGroup,false);
        }
        double box_size = viewModel.boxWidth.getValue();

        ImageView iv_goal = view.findViewById(R.id.iv_goal);
        ConstraintLayout box = view.findViewById(R.id.layoutBox);
        ImageView playerIcon = view.findViewById(R.id.iv_person);
        ImageView hintIcon = view.findViewById(R.id.iv_hint);
        ViewGroup.LayoutParams layoutParams = box.getLayoutParams();
        layoutParams.width=(int)box_size;
        layoutParams.height=(int)box_size;
        BoxInfo info = boxInfos.get(i);
        if(i/viewModel.mazeSize.getValue()==viewModel.player.getValue().getRow() && i%viewModel.mazeSize.getValue()==viewModel.player.getValue().getCol()){
            playerIcon.setVisibility(View.VISIBLE);
            playerIcon.setRotation(90*viewModel.player.getValue().getDirection());
            iv_goal.setVisibility(View.GONE);
            if(hintIcon.getVisibility()==View.VISIBLE) {
                hintIcon.setVisibility(View.GONE);
                viewModel.setDefaultHint();
            }
        }
        else {
            if(i==boxInfos.size()-1){
                iv_goal.setVisibility(View.VISIBLE);
            }
            else if(i==viewModel.hintPos.getValue()){
                hintIcon.setVisibility(View.VISIBLE);
            }
            else{
                hintIcon.setVisibility(View.GONE);
            }
            playerIcon.setVisibility(View.GONE);
        }
        box.setPadding(info.getLeft(),info.getTop(),info.getRight(),info.getBottom());

        return view;
    }

    public MapAdapter(MapViewModel viewModel) {
        this.viewModel = viewModel;
    }

    public void setBoxInfos(ArrayList<BoxInfo> boxInfos) {
        this.boxInfos = boxInfos;
        notifyDataSetChanged();
    }
}
