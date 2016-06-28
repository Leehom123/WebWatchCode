package com.bingxuan.look;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class MainActivity extends ActionBarActivity {

    private EditText et_url;
	private Button bt_look;
	private TextView tv_result;
	private Handler handler=new Handler(){//�����߳���newһ��handler�����֣����󣬸�дhandlerMessage������
		public void handleMessage(android.os.Message msg) {
			//��ȡmsg�е�ֵ
			String contentString=(String) msg.obj;
			//����ui
			tv_result.setText(contentString);
			
		};
	};

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        et_url = (EditText) findViewById(R.id.et_url);
        tv_result = (TextView) findViewById(R.id.tv_result);
    }
	public void click(View v){
		new Thread(){//������һ�����̣߳�
			public void run() {//��д����run��������
				try {
					String path = et_url.getText().toString().trim();
					//newһ��URL
					URL url=new URL(path);
					//getConnection()�õ�HttpURLConnection���������ͻ�������ݣ�
					HttpURLConnection conn = (HttpURLConnection) url.openConnection();
					//������������get����
					conn.setRequestMethod("GET");//�����д���Ҳ�Ƿ���get����
					//��������ʱʱ��
					conn.setConnectTimeout(4000);
					//��ȡ��������Ӧ��
					int code = conn.getResponseCode();
					if (code==200) {
						//��ȡ���������ص����ݣ���һ������ʽ���صģ���ת�����ַ����ܳ������Գ�ȡ��һ��util��
						InputStream in = conn.getInputStream();
						//��ȡ�����ַ�����ʾ��textview��
						String content = Utils.readStream(in);
						Message msg=new Message();//������Ϣ����
						msg.obj=content;//����Ϣ����ֵ
						handler.sendMessage(msg);//����һ����Ϣ
						tv_result.setText(content);//�޸�ui
						
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				
				
			};
		}.start();
				
	}


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
