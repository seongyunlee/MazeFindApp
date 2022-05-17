package edu.skku.cs.pa2.util;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.databinding.BaseObservable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;

import edu.skku.cs.pa2.MazeAdapter;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MazeListViewModel extends ViewModel {
    private MutableLiveData<ArrayList<MazeInfo>> _mazeList = new MutableLiveData<>();
    private LiveData<ArrayList<MazeInfo>> mazeList = _mazeList;
    private ConnectionUtil connectionUtil = new ConnectionUtil();
    private MutableLiveData<String> _moveToMaze = new MutableLiveData<>();
    private LiveData<String> moveToMaze = _moveToMaze;

    public LiveData<String> getMoveToMaze() {
        return moveToMaze;

    }

    public void setMoveToMaze(String mazeName) {
        this._moveToMaze.setValue(mazeName);
    }



    public MazeListViewModel() {
        super();
        setMazeList();
    }

    private void setMazeList(){
        ArrayList<MazeInfo> temp = new ArrayList<>();
        MazeInfo info = new MazeInfo("maze1",1);
        temp.add(info);
        _mazeList.setValue(temp);

        connectionUtil.getMazeList(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.d("get_maze_failure",e.toString());

            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {

                String json = response.body().string();
                Gson gson = new Gson();
                Type jsonType = new TypeToken<ArrayList<MazeInfo>>() {}.getType();
                ArrayList<MazeInfo> mazeInfos = gson.fromJson(json,jsonType);
                _mazeList.postValue(mazeInfos);
                Log.d("parsed",Integer.toString(_mazeList.getValue().size()));

            }
        });
    }
    public LiveData<ArrayList<MazeInfo>> getMazeList(){
        return mazeList;
    }
}
