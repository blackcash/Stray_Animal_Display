package com.blackcash.stray_animail_display;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.blackcash.stray_animail_display.R;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.PageTransformer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class ViewPagerActivity extends Activity implements OnInitListener,
		OnClickListener {

	private ViewPager mViewPager;
	private Button btnTTS;
	List<View> viewList;
	List<ItemData> list = new ArrayList<ItemData>();
	TextToSpeech mTts = null;
	static private boolean isShow = true;

	Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			if (msg.what == 1) {
				mViewPager.setAdapter(new MyPagerAdapter(viewList));
				mViewPager.setCurrentItem(0); // 设置默认当前页
			} else if (msg.what == 2) {
				Toast.makeText(ViewPagerActivity.this, "⊿Τ戈",
						Toast.LENGTH_SHORT).show();
				finish();
			}
		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.activity_main);
		init();
		// 实例化适配器
		mViewPager = (ViewPager) findViewById(R.id.viewpager);
		btnTTS = (Button) findViewById(R.id.btnTTS);
		btnTTS.setOnClickListener(this);
		mViewPager.setPageTransformer(true, new PageTransformer() {
			private static final float MIN_SCALE = 0.75f;

			@Override
			public void transformPage(View view, float position) {

				int pageWidth = view.getWidth();

				if (position < -1) { // [-Infinity,-1)
					// This page is way off-screen to the left.
					view.setAlpha(0);

				} else if (position <= 0) { // [-1,0]
					// Use the default slide transition when moving to the left
					// page
					view.setAlpha(1);
					view.setTranslationX(0);
					view.setScaleX(1);
					view.setScaleY(1);

				} else if (position <= 1) { // (0,1]
					// Fade the page out.
					view.setAlpha(1 - position);

					// Counteract the default slide transition
					view.setTranslationX(pageWidth * -position);

					// Scale the page down (between MIN_SCALE and 1)
					float scaleFactor = MIN_SCALE + (1 - MIN_SCALE)
							* (1 - Math.abs(position));
					view.setScaleX(scaleFactor);
					view.setScaleY(scaleFactor);

				} else { // (1,+Infinity]
					// This page is way off-screen to the right.
					view.setAlpha(0);
				}

			}
		});
		new Thread() {

			@Override
			public void run() {
				updateBitmap();
			}

		}.start();
	}

	private void init() {
		StrayAnimalItem item1 = (StrayAnimalItem) getIntent()
				.getSerializableExtra("item");
		mTts = new TextToSpeech(ViewPagerActivity.this, ViewPagerActivity.this);
		viewList = new ArrayList<View>();
		StringBuffer sb = new StringBuffer();
		sb.append(getResources().getString(R.string.strName) + ":"
				+ item1.getName() + "\n");
		sb.append(getResources().getString(R.string.strSex) + ":"
				+ item1.getSex() + "\n");
		sb.append(getResources().getString(R.string.strType) + ":"
				+ item1.getType() + "\n");
		sb.append(getResources().getString(R.string.strBuild) + ":"
				+ item1.getBuild() + "\n");
		sb.append(getResources().getString(R.string.strAge) + ":"
				+ item1.getAge() + "\n");
		sb.append(getResources().getString(R.string.strVariety) + ":"
				+ item1.getVariety() + "\n");
		sb.append(getResources().getString(R.string.strReason) + ":"
				+ item1.getReason() + "\n");
		sb.append(getResources().getString(R.string.strAcceptNum) + ":"
				+ item1.getAcceptNum() + "\n");
		sb.append(getResources().getString(R.string.strChipNum) + ":"
				+ item1.getChipNum() + "\n");
		sb.append(getResources().getString(R.string.strIsSterilization) + ":"
				+ item1.getIsSterilization() + "\n");
		sb.append(getResources().getString(R.string.strHairType) + ":"
				+ item1.getHairType() + "\n");
		sb.append(getResources().getString(R.string.strNote) + ":"
				+ item1.getNote() + "\n");
		sb.append(getResources().getString(R.string.strResettlement) + ":"
				+ item1.getResettlement() + "\n");
		sb.append(getResources().getString(R.string.strPhone) + ":"
				+ item1.getPhone() + "\n");
		sb.append(getResources().getString(R.string.strEmail) + ":"
				+ item1.getEmail() + "\n");
		sb.append(getResources().getString(R.string.strChildreAnlong) + ":"
				+ item1.getChildreAnlong() + "\n");
		sb.append(getResources().getString(R.string.strAnimalAnlong) + ":"
				+ item1.getAnimalAnlong() + "\n");
		sb.append(getResources().getString(R.string.strBodyweight) + ":"
				+ item1.getBodyweight() + "\n");
		list.add(new ItemData(sb.toString(), item1.getImageName()));
	}

	private void updateBitmap() {
		LayoutInflater mInflater = getLayoutInflater().from(this);
		if (list.size() > 0) {
			for (ItemData item : list) {
				try {
					View v1 = mInflater.inflate(R.layout.item, null);
					TextView tvTitle = (TextView) v1.findViewById(R.id.tvTitle);
					ImageView iv = (ImageView) v1.findViewById(R.id.ivPicture);
					final ScrollView sv = (ScrollView) v1
							.findViewById(R.id.scrollView1);
					if (!item.url.equals("")) {
						URL newurl = new URL(item.url);
						Bitmap mIcon_val = BitmapFactory.decodeStream(newurl
								.openConnection().getInputStream());
						iv.setImageBitmap(mIcon_val);
					} else {
						iv.setImageResource(R.drawable.ic_launcher);
					}
					iv.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							isShow = !isShow;
							if (isShow)
								sv.setVisibility(View.VISIBLE);
							else
								sv.setVisibility(View.GONE);
						}
					});
					tvTitle.setText(item.title);
					viewList.add(v1);
				} catch (MalformedURLException e) {
					mHandler.sendEmptyMessage(2);
				} catch (IOException e) {
					mHandler.sendEmptyMessage(2);
				}
			}
			mHandler.sendEmptyMessage(1);
		} else {
			mHandler.sendEmptyMessage(2);
		}
	}

	class ItemData {
		String title;
		String url;

		public ItemData(String title, String url) {
			this.title = title;
			this.url = url;
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (mTts != null) {
			mTts.stop();
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mTts.shutdown();
	}

	@Override
	public void onInit(int status) {
		if (status == TextToSpeech.SUCCESS) {
			int result = mTts.setLanguage(Locale.CHINESE);
			if (result == TextToSpeech.LANG_MISSING_DATA
					|| result == TextToSpeech.LANG_NOT_SUPPORTED) {
				btnTTS.setEnabled(false);
			} else {
				btnTTS.setEnabled(true);
			}
		}
	}

	@Override
	public void onClick(View v) {
		int index = mViewPager.getCurrentItem();
		String text = list.get(index).title;
		if (mTts != null) {
			mTts.stop();
		}
		mTts.speak(text, TextToSpeech.QUEUE_ADD, null);
	}
}
