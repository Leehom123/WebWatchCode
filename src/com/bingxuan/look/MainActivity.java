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
	private Handler handler=new Handler(){//在主线程中new一个handler（助手）对象，复写handlerMessage方法。
		public void handleMessage(android.os.Message msg) {
			//获取msg中的值
			String contentString=(String) msg.obj;
			//更新ui
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
		new Thread(){//创建你一个子线程，
			public void run() {//复写他的run（）方法
				try {
					String path = et_url.getText().toString().trim();
					//new一个URL
					URL url=new URL(path);
					//getConnection()得到HttpURLConnection对象，来发送或接受数据，
					HttpURLConnection conn = (HttpURLConnection) url.openConnection();
					//给服务器发送get请求
					conn.setRequestMethod("GET");//如果不写这句也是发送get请求
					//设置请求超时时间
					conn.setConnectTimeout(4000);
					//获取服务器响应码
					int code = conn.getResponseCode();
					if (code==200) {
						//获取服务器返回的数据，是一流的形式返回的，流转换成字符串很常用所以抽取成一个util类
						InputStream in = conn.getInputStream();
						//将取出的字符串显示到textview中
						String content = Utils.readStream(in);
						Message msg=new Message();//创建消息对象
						msg.obj=content;//给消息对象赋值
						handler.sendMessage(msg);//发送一条消息
						tv_result.setText(content);//修改ui
						
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
