package edu.skku.cs.pa2.util;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;

import edu.skku.cs.pa2.BoxInfo;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MapViewModel extends ViewModel {
    private MutableLiveData<ArrayList<BoxInfo>> _boxInfos = new MutableLiveData<>();
    public LiveData<ArrayList<BoxInfo>> boxInfos = _boxInfos;
    private String mazeName;
    private MutableLiveData<Integer> _mazeSize= new MutableLiveData<>();
    public  LiveData<Integer> mazeSize = _mazeSize;
    private ConnectionUtil connectionUtil = new ConnectionUtil();
    private MutableLiveData<Double> _boxWidth= new MutableLiveData<>();
    public LiveData<Double> boxWidth = _boxWidth;
    private MutableLiveData<Player> _player = new MutableLiveData<>();
    public  LiveData<Player> player = _player;
    private MutableLiveData<Integer> _turnCnt = new MutableLiveData<>();
    public LiveData<Integer> turnCnt = _turnCnt;
    private MutableLiveData<Boolean> _finish = new MutableLiveData<>();
    public LiveData<Boolean> finish = _finish;
    private MutableLiveData<Integer> _hintPos = new MutableLiveData();
    public LiveData<Integer> hintPos = _hintPos;
    private boolean hint_used= false;
    public MapViewModel(){
        super();
        _mazeSize.setValue(5);//default maze size , if null runtime error
        _player.setValue(new Player(0,0,2));
        _turnCnt.setValue(0);
        _finish.setValue(false);
        setDefaultHint();
    }
    public void setDefaultHint(){
        _hintPos.setValue(-1);
    }
    public void set_mazeName(String mazeName) {
        this.mazeName=mazeName;
        setMap();
    }
    public void toLeft(){
        Log.d("move_player_left",player.getValue().toString());
        int now = _player.getValue().col;
        if(now !=0 && boxInfos.getValue().get(playerToIndex(_player.getValue())).getLeft()==0){
            now--;
            _turnCnt.setValue(_turnCnt.getValue()+1);
        }
        Player p= new Player(_player.getValue().row,now,3);
        _player.setValue(p);
        Log.d("player_moved",player.getValue().toString());
        checkFinsh();
    }
    public void toRight(){
        Log.d("move_player_right",player.getValue().toString());
        int now = _player.getValue().col;
        if(now!=mazeSize.getValue()-1 && boxInfos.getValue().get(playerToIndex(_player.getValue())).getRight()==0){
            now++;
            _turnCnt.setValue(_turnCnt.getValue()+1);

        }
        Player p= new Player(_player.getValue().row,now,1);
        _player.setValue(p);
        Log.d("player_moved",player.getValue().toString());
        checkFinsh();
    }
    public void toUp(){
        Log.d("move_player_up",player.getValue().toString());
        int now = _player.getValue().row;
        if(now!=0&& boxInfos.getValue().get(playerToIndex(_player.getValue())).getTop()==0){
            now--;
            _turnCnt.setValue(_turnCnt.getValue()+1);

        }
        Player p= new Player(now,_player.getValue().col,0);
        _player.setValue(p);
        Log.d("player_moved",player.getValue().toString());
        checkFinsh();
    }
    public void toDown(){
        Log.d("move_player_down",player.getValue().toString());
        int now = _player.getValue().row;
        if(now!=mazeSize.getValue()-1&& boxInfos.getValue().get(playerToIndex(_player.getValue())).getBottom()==0){
            now++;
            _turnCnt.setValue(_turnCnt.getValue()+1);

        }
        Player p= new Player(now,_player.getValue().col,2);
        _player.setValue(p);
        Log.d("player_moved",player.getValue().toString());
        checkFinsh();
    }
    private void checkFinsh(){
        if(player.getValue().row==mazeSize.getValue()-1 && player.getValue().col==mazeSize.getValue()-1){
            _finish.setValue(true);
            Log.d("maze_finish","FINISH");
        }
    }
    private int playerToIndex(Player player){
        return player.row* mazeSize.getValue()+player.col;
    }
    class Pos{
        int row;
        int col;
        ArrayList<Integer> path= new ArrayList<>();
        public Pos(int row,int col,ArrayList path){
            this.row= row;
            this.col= col;
            this.path = path;
        }
    }
    public void showHint(){
        if(hint_used){
            return;
        }
        hint_used=true;
        ArrayList<Pos> pos = new ArrayList<>();
        ArrayList<Boolean> visit = new ArrayList<>();
        for(int i =0 ; i <mazeSize.getValue()*mazeSize.getValue(); i++){
            visit.add(false);
        }
        ArrayList<Integer> first = new ArrayList<>();
        pos.add(new Pos(player.getValue().row,player.getValue().col,first));
        visit.set(playerToIndex(player.getValue()),true);
        BFS(pos,visit);
    }
    private void BFS(ArrayList<Pos> pos,ArrayList<Boolean> visit){
        ArrayList<Pos> next = new ArrayList<>();
        for(Pos p:pos){
            Log.d("BFS item",Integer.toString(p.row)+"+"+Integer.toString(p.col));
        }
        for(Pos p : pos){
            Log.d("BFS item",Integer.toString(p.row)+"+"+Integer.toString(p.col));
            //left

            ArrayList<Integer> path;
            if(p.col!=0 && boxInfos.getValue().get(posToIndex(p)).getLeft()==0){

                int index= p.row*mazeSize.getValue()+p.col-1;

                if(!visit.get(index)) {
                    Log.d("BFS item left",Integer.toString(index));
                    path = new ArrayList<>();
                    if (index == mazeSize.getValue()*mazeSize.getValue()-1) {
                        p.path.add(index);
                        _hintPos.setValue(p.path.get(0));
                        return;
                    }
                    for (Integer i : p.path) {
                        path.add(i);
                    }
                    path.add(index);
                    next.add(new Pos(p.row, p.col - 1, path));
                    visit.set(index,true);
                }
            }
            //right
            if(p.col!=mazeSize.getValue()-1 && boxInfos.getValue().get(posToIndex(p)).getRight()==0){
                int index =p.row*mazeSize.getValue()+p.col+1;
                if(!visit.get(index)) {
                    Log.d("BFS item right",Integer.toString(index));
                    if (index == mazeSize.getValue()*mazeSize.getValue()-1) {
                        p.path.add(index);
                        _hintPos.setValue(p.path.get(0));
                        return;
                    }
                    path = new ArrayList<>();
                    for (Integer i : p.path) {
                        path.add(i);
                    }
                    path.add(index);
                    next.add(new Pos(p.row, p.col + 1, path));
                    visit.set(index,true);
                }
            }
            //up
            if(p.row!=0 && boxInfos.getValue().get(posToIndex(p)).getTop()==0){
                int index= (p.row-1)*mazeSize.getValue()+p.col;
                if(!visit.get(index)) {
                    Log.d("BFS item up",Integer.toString(index));
                    if (index == mazeSize.getValue()*mazeSize.getValue()-1) {
                        p.path.add(index);
                        _hintPos.setValue(p.path.get(0));
                        return;
                    }
                    path = new ArrayList<>();
                    for (Integer i : p.path) {
                        path.add(i);
                    }
                    path.add(index);
                    next.add(new Pos(p.row - 1, p.col, path));
                    visit.set(index,true);
                }
            }
            //down
            if(p.row!=mazeSize.getValue()-1&& boxInfos.getValue().get(posToIndex(p)).getBottom()==0){
                int index= (p.row+1)*mazeSize.getValue()+p.col;
                if(!visit.get(index)) {
                    Log.d("BFS item down",Integer.toString(index));
                    if (index == mazeSize.getValue()*mazeSize.getValue()-1) {
                        p.path.add(index);
                        _hintPos.setValue(p.path.get(0));
                        return;
                    }
                    path = new ArrayList<>();
                    for (Integer i : p.path) {
                        path.add(i);
                    }
                    path.add(index);
                    next.add(new Pos(p.row + 1, p.col, path));
                    visit.set(index,true);
                }
            }
        }
        Log.d("BFS next",Integer.toString(next.size()));
        BFS(next,visit);
    }
    private int posToIndex(Pos p){
        return p.row*mazeSize.getValue()+p.col;
    }
    public void setMap(){

        connectionUtil.getMap(mazeName,new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.d("get_map_failure",e.toString());

            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {

                String json = response.body().string();
                Gson gson = new Gson();

                Log.d("get_map_success",json);
                ArrayList<BoxInfo> boxInfos = new ArrayList<>();
                try {

                    JSONObject object = new JSONObject(json);
                    String rawMap = object.getString("maze");
                    String[] line= rawMap.split("\n");

                    int size = Integer.parseInt(line[0]);
                    _mazeSize.postValue(size);
                    for(int i=1; i <=size;i++){
                        String[] box_types= line[i].split(" ");
                        for(String t:box_types){
                            int type = Integer.parseInt(t);
                            BoxInfo boxInfo = new BoxInfo(type);
                            boxInfos.add(boxInfo);
                        }
                    }
                    Log.d("parse_map_succeess",Integer.toString(size));
                    _boxInfos.postValue(boxInfos);
                }
                catch (JSONException e) {
                    Log.d("parse_map_fail",e.toString());
                }
                //Log.d("parsed",Integer.toString(_mazeList.getValue().size()));

            }
        });
    }

    public void set_boxWidth(double width) {
        this._boxWidth.setValue(width);
    }
}
