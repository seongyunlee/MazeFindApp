package edu.skku.cs.pa2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.Toast;

import java.util.ArrayList;

import edu.skku.cs.pa2.databinding.ActivityPlayMazeBinding;
import edu.skku.cs.pa2.util.MapViewModel;
import edu.skku.cs.pa2.util.Player;

public class PlayMazeActivity extends AppCompatActivity {

    ActivityPlayMazeBinding binding;
    MapViewModel viewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_maze);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_play_maze);
        binding.executePendingBindings();
        viewModel = new ViewModelProvider(this).get(MapViewModel.class);
        viewModel.set_mazeName(getIntent().getStringExtra("mazeName"));
        setMap();
        setTurnCntDisplay();
        setCheckFinish();

    }
    private void setMap(){
        MapAdapter mapAdapter = new MapAdapter(viewModel);
        binding.gvMaze.setAdapter(mapAdapter);
        viewModel.mazeSize.observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                double grid_width =binding.gvMaze.getLayoutParams().width;
                Log.d("box_width",Double.toString(grid_width));
                viewModel.set_boxWidth(grid_width/(double)integer);
                binding.gvMaze.setNumColumns(integer);
            }
        });
        viewModel.boxInfos.observe(this, new Observer<ArrayList<BoxInfo>>() {
            @Override
            public void onChanged(ArrayList<BoxInfo> boxInfos) {
                mapAdapter.setBoxInfos(boxInfos);
            }
        });
        viewModel.player.observe(this, new Observer<Player>() {
            @Override
            public void onChanged(Player player) {
                mapAdapter.notifyDataSetChanged();
            }
        });
        viewModel.hintPos.observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                Log.d("Show_hint",Integer.toString(integer));
                mapAdapter.notifyDataSetChanged();
            }
        });
    }
    public void toLeft(View v){
        viewModel.toLeft();
    }
    public void toRight(View v){
        viewModel.toRight();
    }
    public void toUp(View v){
        viewModel.toUp();
    }
    public void toDown(View v){
        viewModel.toDown();
    }
    private void setTurnCntDisplay(){
        viewModel.turnCnt.observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                binding.tvTurn.setText("Turn : "+Integer.toString(integer));
            }
        });
    }
    private void setCheckFinish(){
        viewModel.finish.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if(aBoolean.booleanValue()) {
                    Log.d("finish",aBoolean.toString());
                    Toast.makeText(PlayMazeActivity.this, "Finish!", Toast.LENGTH_SHORT).show();
                }
                }
        });
    }
    public void showHint(View v){
        viewModel.showHint();
    }
}