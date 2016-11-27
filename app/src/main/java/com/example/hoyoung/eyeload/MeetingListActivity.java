package kr.soen.mypart;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

/**
 * Created by Jin on 2016-10-8.
 */

public class MeetingListActivity extends AppCompatActivity {

    private MeetingControl control = MeetingControl.getInstance();
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        control.getAllMeeting(); // DB에서 Meeting에 관한 모든 정보를 가져옴
        setContentView(R.layout.activity_meeting_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setupListView();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {//Activity 안의 버튼이 눌린 경우
                Intent intent = new Intent(MeetingListActivity.this,MakingMeetingActivity.class);

                startActivity(intent);
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                //        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        setupListView();

    }

    //List내용을 xml에 추가하는 부분
    public void setupListView()
    {

        SelectAllMeeting selectAllMeeting = new SelectAllMeeting();
        selectAllMeeting.execute();

    }

    public void meetingClicked() {
        // Meeting list 선택 작동하는 부분
        AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(MeetingListActivity.this, control.getMeetingList().get(position).getTitle() + " is clicked.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MeetingListActivity.this,MeetingInfoActivity.class);
                intent.putExtra("meetingKey",String.valueOf(control.getMeetingList().get(position).getKey()));
                startActivity(intent);
            }
        };

        listView.setOnItemClickListener(listener);
    }


    class SelectAllMeeting extends AsyncTask<Void, Void, Void> {
        ProgressDialog loading;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loading = new ProgressDialog(MeetingListActivity.this);
            loading.setMessage("불러오는 중입니다.");

            listView = (ListView) findViewById(R.id.meetingListview1);

            control = MeetingControl.getInstance();

            loading.show();
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            loading.dismiss();

            listView.setAdapter(control);

            meetingClicked();

        }


        @Override
        protected Void doInBackground(Void ... params) {
            control.getAllMeeting();

            return null;
        }
    }
}
