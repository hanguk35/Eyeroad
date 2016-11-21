package kr.soen.mypart;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

/**
 * Created by Jin on 2016-11-5.
 */

public class MemoInfoActivity extends AppCompatActivity implements View.OnClickListener{
    private String key;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo_info);
        findViewById(R.id.memoInfoDelete).setOnClickListener(this);

        Intent intent = new Intent(this.getIntent());
        String memoKey = intent.getStringExtra("memoKey");
        TextView textView=(TextView)findViewById(R.id.memoInfoTextView);
        key = memoKey;
        textView.setText(memoKey);
    }

    public void onClick(View v) { // 메뉴의 버튼 선택 시 activity 이동
        switch (v.getId()) {
            case R.id.memoInfoDelete:
                //Toast.makeText(MeetingInfoActivity.this, key, Toast.LENGTH_SHORT).show();
                //MeetingControl meetingControl = MeetingControl.getInstance();
                //meetingControl.deleteInfo(key);
                deleteInfo(key);
        }
    }

    private void showMemoInfo()
    {
        //Control의 DTO를 가져와 Activity에 반환하거나 xml에 표시해줘야함
    }

    private void deleteInfo(String key){
        Toast.makeText(MemoInfoActivity.this, key, Toast.LENGTH_SHORT).show();
        class DeleteInfo extends AsyncTask<String, Void, String> {
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

                    String link="http://210.94.194.201/deleteMemo.php";
                    String data  = URLEncoder.encode("memoKey", "UTF-8") + "=" + URLEncoder.encode(key, "UTF-8");

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
}
