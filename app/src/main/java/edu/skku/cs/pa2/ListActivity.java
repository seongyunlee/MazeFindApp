package edu.skku.cs.pa2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;

import java.util.ArrayList;

import edu.skku.cs.pa2.databinding.ActivityListBinding;
import edu.skku.cs.pa2.util.MazeInfo;
import edu.skku.cs.pa2.util.MazeListViewModel;

public class ListActivity extends AppCompatActivity {
    ActivityListBinding activityListBinding;
    MazeListViewModel viewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityListBinding = DataBindingUtil.setContentView(this,R.layout.activity_list);
        activityListBinding.executePendingBindings();
        viewModel = new ViewModelProvider(this).get(MazeListViewModel.class);
        setUserName();
        setMazeList();
        setMovetoMaze();
    }
    private void setUserName(){
        Intent intent = getIntent();
        activityListBinding.tvUsername.setText(intent.getStringExtra("username"));
    }
    private void setMazeList(){
        MazeAdapter adapter = new MazeAdapter(viewModel);
        activityListBinding.rvList.setAdapter(adapter);
        activityListBinding.rvList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        viewModel.getMazeList().observe(this, new Observer<ArrayList<MazeInfo>>() {
            @Override
            public void onChanged(ArrayList<MazeInfo> mazeInfos) {
                adapter.setMazeList(mazeInfos);
            }
        });
    }
    private void setMovetoMaze(){
        viewModel.getMoveToMaze().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if(s!=null) {
                    Intent intent = new Intent(ListActivity.this, PlayMazeActivity.class);
                    intent.putExtra("mazeName", s);
                    startActivity(intent);
                    viewModel.setMoveToMaze(null);
                }
            }
        });
    }
}