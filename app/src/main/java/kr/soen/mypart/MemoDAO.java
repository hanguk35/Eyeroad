package kr.soen.mypart;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InterfaceAddress;
import java.net.URL;
import java.sql.Date;
import java.util.ArrayList;

/**
 * Created by Jin on 2016-11-17.
 */

public class MemoDAO extends DAO{

    private ArrayList<MemoDTO> arrayListMemoDTO = new ArrayList<>();
    public boolean insert(MemoDTO dto)
    {
        return true;
    }
    public boolean delete(int key)
    {
        return true;
    }
    public MemoDTO select(int key)
    {
        MemoDTO dto = new MemoDTO();
        return dto;
    }
    public ArrayList<MemoDTO> selectAll()
    {
        getAllData("http://210.94.194.201/selectAllMemo.php");
        Log.d("print","memoDAO arrayListMemoDTO's size : "+ String.valueOf(arrayListMemoDTO.size()) );
        return arrayListMemoDTO;
    }

    protected void showList(String myJSON) {

        arrayListMemoDTO.clear();//업데이트를 위한 초기화부분
        int tmp;
        try{
            JSONObject jsonObj = new JSONObject(myJSON);
            JSONArray jsonArrayMemoDTO = null;
            jsonArrayMemoDTO = jsonObj.getJSONArray("result");
            Log.d("print","memoListLength : "+ String.valueOf(jsonArrayMemoDTO.length()) );
            for(int i=0;i<jsonArrayMemoDTO.length();i++) {

                //MeetingDTO 객체를 생성
                MemoDTO memoDTO = new MemoDTO();

                JSONObject c = jsonArrayMemoDTO.getJSONObject(i);

                /*Log.d("print","memoKey : "+ c.getString("memoKey") );
                Log.d("print","title : "+ c.getString("title") );
                Log.d("print","x : "+ c.getString("x") );
                Log.d("print","y : "+ c.getString("y") );
                Log.d("print","z : "+ c.getString("z") );
                Log.d("print","content : "+ c.getString("content") );
                Log.d("print","date : "+ c.getString("date") );
                Log.d("print","image : "+ c.getString("image") );
                Log.d("print","iconId : "+ c.getString("iconld") );
                Log.d("print","deviceID : "+ c.getString("deviceID") );
                Log.d("print","visibility : "+ c.getString("visibility") );*/
                memoDTO.setKey(Integer.parseInt(c.getString("memoKey")));
                memoDTO.setTitle(c.getString("title"));
                memoDTO.setX(Double.parseDouble(c.getString("x")));
                memoDTO.setY(Double.parseDouble(c.getString("y")));
                memoDTO.setZ(Double.parseDouble(c.getString("z")));
                memoDTO.setContent(c.getString("content"));
                memoDTO.setDate(c.getString("date"));
                memoDTO.setImage(c.getString("image"));
                memoDTO.setIconId(Integer.parseInt(c.getString("iconId")));
                memoDTO.setDeviceID(c.getString("deviceID"));
                memoDTO.setVisibility(Integer.parseInt(c.getString("visibility")));
                arrayListMemoDTO.add(memoDTO);
            }

        }catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public void getAllData(String url)
    {
        class GetDataJSON extends AsyncTask<String, Void, String> {

            @Override
            protected String doInBackground(String... params) {

                String uri = params[0];

                BufferedReader bufferedReader = null;
                try {
                    URL url = new URL(uri);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    StringBuilder sb = new StringBuilder();

                    bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));

                    String json;
                    while((json = bufferedReader.readLine())!= null){
                        sb.append(json+"\n");
                    }

                    return sb.toString().trim();

                }catch(Exception e){
                    return null;
                }
            }

            @Override
            protected void onPostExecute(String result){
                showList(result);
            }
        }
        GetDataJSON g = new GetDataJSON();
        g.execute(url);

    }
}
