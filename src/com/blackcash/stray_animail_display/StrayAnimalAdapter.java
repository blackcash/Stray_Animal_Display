package com.blackcash.stray_animail_display;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import com.blackcash.stray_animail_display.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class StrayAnimalAdapter extends BaseAdapter {

	private List<StrayAnimalItem> list;
	private LayoutInflater inflater;
	private Context context;

	public StrayAnimalAdapter(Context context, List<StrayAnimalItem> list) {
		inflater = LayoutInflater.from(context);
		this.list = list;
		this.context = context;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Holder mHolder = null;
		if (convertView == null) {
			// convertView = inflater.inflate(R.layout.list_item, null);
			convertView = LayoutInflater.from(context).inflate(
					R.layout.list_item, null);
			mHolder = new Holder();
			mHolder.iv = (ImageView) convertView.findViewById(R.id.ivImageName);
			mHolder.tv1 = (TextView) convertView.findViewById(R.id.tvName);
			mHolder.tv2 = (TextView) convertView.findViewById(R.id.tvSex);
			mHolder.tv3 = (TextView) convertView.findViewById(R.id.tvType);
			mHolder.tv4 = (TextView) convertView.findViewById(R.id.tvAcceptNum);
			convertView.setTag(mHolder);
		} else {
			mHolder = (Holder) convertView.getTag();
		}
		StrayAnimalItem item = list.get(position);
		if (!item.getImageName().equals("")) {
			Log.d("url",item.getImageName());
//			DownLoadBitmap download = new DownLoadBitmap(mHolder.iv);
//			download.execute(item.getImageName());
			ImageLoader imageLoader = new ImageLoader(context);
			imageLoader.DisplayImage(item.getImageName(), mHolder.iv);
			//mHolder.iv.setImageBitmap(getBitmapFromURL(item.getImageName()));			
			// try {
			// URL url = new URL(item.getImageName());
			// InputStream is = url.openConnection().getInputStream();
			// mHolder.iv.setImageBitmap( BitmapFactory.decodeStream(is));
			// } catch (MalformedURLException e) {
			// mHolder.iv.setImageResource(R.drawable.ic_launcher);
			// } catch (IOException e) {
			// mHolder.iv.setImageResource(R.drawable.ic_launcher);
			// }
		} else {
			mHolder.iv.setImageResource(R.drawable.ic_launcher);
		}
		mHolder.tv1.setText(context.getResources().getString(R.string.strName)
				+ ":" + item.getName());
		mHolder.tv2.setText(context.getResources().getString(R.string.strSex)
				+ ":" + item.getSex());
		mHolder.tv3.setText(context.getResources().getString(R.string.strType)
				+ ":" + item.getType());
		mHolder.tv4.setText(context.getResources().getString(
				R.string.strAcceptNum)
				+ ":" + item.getAcceptNum());
		return convertView;
	}

	public Bitmap getBitmapFromURL(String src) {
	    try {
	        URL url = new URL(src);
	        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
	        connection.setDoInput(true);
	        connection.connect();
	        InputStream input = connection.getInputStream();
	        Bitmap myBitmap = BitmapFactory.decodeStream(input);
	        return myBitmap;
	    } catch (IOException e) {
	        // Log exception
	        return null;
	    }
	}
	
	private class Holder {
		ImageView iv;
		TextView tv1;
		TextView tv2;
		TextView tv3;
		TextView tv4;
	}

	class DownLoadBitmap extends AsyncTask<String, Void, Bitmap> {
		// private final WeakReference<ImageView> imageViewReference;
		private ImageView iv;
		private String address;

		public DownLoadBitmap(ImageView iv) {
			// imageViewReference = new WeakReference<ImageView>(iv);
			this.iv = iv;
		}

		@Override
		protected Bitmap doInBackground(String... params) {
			Bitmap bmp = null;
			try {
				URL newurl = new URL(params[0]);
				address = params[0];
				BitmapFactory.Options option = new BitmapFactory.Options();
				option.inSampleSize = 2;
				//option.inJustDecodeBounds = true;
				option.inPurgeable = true;
				bmp = BitmapFactory.decodeStream(newurl.openConnection()
						.getInputStream(), null,option);
				// bmp = BitmapFactory.decodeStream(newurl.openConnection()
				// .getInputStream());
			} catch (MalformedURLException e) {
				Log.d("DownLoadBitmap", "no img");
			} catch (IOException e) {
				Log.d("DownLoadBitmap", "no img");
			}
			return bmp;
		}

		@Override
		protected void onPostExecute(Bitmap result) {
			// ImageView iv = imageViewReference.get();
			if (result != null) {
					// iv.setImageBitmap(getResizedBitmap(result, 300, 300));
					iv.setImageBitmap(result);
					iv.setTag(address);
			} else {
				iv.setImageResource(R.drawable.ic_launcher);
			}
		}

		private Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
			Bitmap resizedBitmap = Bitmap.createScaledBitmap(bm, newWidth,
					newHeight, false);
			return resizedBitmap;
		}

		private BitmapFactory.Options getBitmapOptions(int scale) {
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inPurgeable = true;
			options.inInputShareable = true;
			options.inSampleSize = scale;
			return options;
		}
	}
}
