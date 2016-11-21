package kr.soen.mypart;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * Created by Jin on 2016-11-17.
 */

public class MeetingDAO extends DAO{
    //싱글톤 클래스
    //private static MeetingDAO meetingDAO = new MeetingDAO();

    private ArrayList<MeetingDTO> arrayListMeetingDTO = new ArrayList<>();

    public boolean insert(MeetingDTO dto)
    {
        return true;
    }
    public void deleteInfo(String key){
        class DeleteInfo extends AsyncTask<String, Void, String>{
            //ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                //loading = ProgressDialog.show(MeetingInfoActivity, "Please Wait", null, true, true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                //loading.dismiss();
                //Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(String... params) {

                try{
                    String key = (String)params[0];

                    String link="http://210.94.194.201/deleteMeeting.php";
                    String data  = URLEncoder.encode("meetingKey", "UTF-8") + "=" + URLEncoder.encode(key, "UTF-8");

                    URL url = new URL(link);
                    URLConnection conn = url.openConnection();

                    conn.setDoOutput(true);
                    OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

                    wr.write( data );
                    wr.flush();

                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                    StringBuilder sb = new StringBuilder();
                    String line = null;

                    while((line = reader.readLine()) != null)
                    {
                        sb.append(line);
                        break;
                    }
                    return sb.toString();
                }
                catch(Exception e){
                    return new String("Exception: " + e.getMessage());
                }

            }
        }

        DeleteInfo task = new DeleteInfo();
        task.execute(key);
    }


    /*private MeetingDAO()
    {

    }

    public static MeetingDAO getInstance(){
        return meetingDAO;
    }*/

    public MeetingDTO select(int key)
    {
        MeetingDTO dto = new MeetingDTO();
        return dto;
    }
    public ArrayList<MeetingDTO> selectAll()
    {
        getAllData("http://210.94.194.201/selectAllMeeting.php");
        return arrayListMeetingDTO;
    }

    protected void showList(String myJSON) {

        arrayListMeetingDTO.clear();//업데이트를 위한 초기화부분

        try{
            JSONObject jsonObj = new JSONObject(myJSON);
            JSONArray jsonArrayMeetingDTO = null;
            jsonArrayMeetingDTO = jsonObj.getJSONArray("result");
            Log.d("print","meetingListLength : "+ String.valueOf(jsonArrayMeetingDTO.length()) );
            for(int i=0;i<jsonArrayMeetingDTO.length();i++) {

                //MeetingDTO 객체를 생성
                MeetingDTO meetingDTO = new MeetingDTO();

                JSONObject c = jsonArrayMeetingDTO.getJSONObject(i);
                //MeetingDTO 객체에 정보 삽입
                meetingDTO.setKey(Integer.parseInt(c.getString("meetingKey")));
                meetingDTO.setTitle(c.getString("title"));
                meetingDTO.setPlaceName(c.getString("placeName"));
                meetingDTO.setMeetingInfo(c.getString("meetingInfo"));
                meetingDTO.setPublisher(c.getString("publisher"));
                meetingDTO.setPassword(c.getString("password"));

                //MeetingDTO 객체를 ArrayList에 삽입
                arrayListMeetingDTO.add(meetingDTO);

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
