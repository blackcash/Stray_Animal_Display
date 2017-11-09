package com.blackcash.stray_animail_display;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.blackcash.stray_animail_display.R;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

public class StrayAnimalListActivity extends Activity implements OnItemClickListener {

	private ListView listview;
	private ProgressBar bar;
	private List<StrayAnimalItem> list;
	final public int MSG_OK = 1;
	final public int MSG_NG_INTERNET = 2;

	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			if (msg.what == MSG_OK) {
				listview.setAdapter(new StrayAnimalAdapter(StrayAnimalListActivity.this, list));
				listview.setVisibility(View.VISIBLE);
				bar.setVisibility(View.GONE);
			} else if (msg.what == MSG_NG_INTERNET) {
				Toast.makeText(StrayAnimalListActivity.this, "請確定網路!!", Toast.LENGTH_SHORT).show();
				bar.setVisibility(View.GONE);
			}
		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.activity_zoo_list);
		findViews();
		init();
	}

	private void findViews() {
		listview = (ListView) findViewById(R.id.listView1);
		bar = (ProgressBar) findViewById(R.id.progressBar1);
		listview.setOnItemClickListener(this);
	}

	private void init() {
		list = new ArrayList<StrayAnimalItem>();
		listview.setVisibility(View.GONE);
		bar.setVisibility(View.VISIBLE);
		new Task()
				.execute("http://data.taipei/opendata/datalist/apiAccess?scope=resourceAquire&rid=f4a75ba9-7721-4363-884d-c3820b0b917c");
	}

	class Task extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... params) {
			String result = null;
			try {
				HttpClient client = new DefaultHttpClient();
				HttpGet get = new HttpGet(params[0]);
				HttpResponse response = client.execute(get);
				HttpEntity resEntity = response.getEntity();
				result = EntityUtils.toString(resEntity);
			} catch (Exception e) {
				result = null;
			}
			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			if (result != null) {
				try {
					JSONObject json = new JSONObject(result);
					JSONObject json_result = json.getJSONObject("result");
					JSONArray json_results = json_result
							.getJSONArray("results");
					for (int i = 0; i < json_results.length(); i++) {
						StrayAnimalItem item = new StrayAnimalItem();
						JSONObject j = (JSONObject) json_results.get(i);
						item.set_id(j.getString("_id"));
						item.setName(j.getString("Name"));
						item.setSex(j.getString("Sex"));
						item.setType(j.getString("Type"));
						item.setBuild(j.getString("Build"));
						item.setAge(j.getString("Age"));
						item.setVariety(j.getString("Variety"));
						item.setReason(j.getString("Reason"));
						item.setAcceptNum(j.getString("AcceptNum"));
						item.setChipNum(j.getString("ChipNum"));
						item.setIsSterilization(j.getString("IsSterilization"));
						item.setHairType(j.getString("HairType"));
						item.setHairType(j.getString("HairType"));
						item.setResettlement(j.getString("Resettlement"));
						item.setPhone(j.getString("Phone"));
						item.setEmail(j.getString("Email"));
						item.setChildreAnlong(j.getString("ChildreAnlong"));
						item.setAnimalAnlong(j.getString("AnimalAnlong"));
						item.setBodyweight(j.getString("Bodyweight"));
						item.setImageName(j.getString("ImageName"));
						list.add(item);
					}
					Collections.sort(list, new Comparator<StrayAnimalItem>() {

						@Override
						public int compare(StrayAnimalItem o0,
								StrayAnimalItem o1) {							
							return o0.getType().compareTo(o1.getType());
						}
					});
					mHandler.sendEmptyMessage(MSG_OK);
				} catch (JSONException e) {
					mHandler.sendEmptyMessage(MSG_NG_INTERNET);
				}
			} else {
				mHandler.sendEmptyMessage(MSG_NG_INTERNET);
			}
		}

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		StrayAnimalItem item = list.get(position);
		Intent intent = new Intent(this,ViewPagerActivity.class);
		intent.putExtra("item", item);
		startActivity(intent);
	}

}
