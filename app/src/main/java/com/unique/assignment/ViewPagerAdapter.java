package com.unique.assignment;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import org.json.JSONArray;

import java.io.InputStream;

class ViewPagerAdapter extends PagerAdapter {
    private JSONArray imglist;
    private Context mContext;
    private int[] sliderImageId;


    public ViewPagerAdapter(MainActivity context) {

        this.mContext = context;
    }

    public ViewPagerAdapter(MainActivity context, JSONArray jsonArray) {
        this.mContext = context;
        this.imglist = jsonArray;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((ImageView) object);
    }


    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        sliderImageId = new int[imglist.length()];

        ImageView imageView = new ImageView(mContext);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        try {
            // imageView.setImageResource((Integer) imglist.get(position));
            for (int i = 0; i < imglist.length(); ++i) {

                new DownloadImageFromInternet(imageView).execute(imglist.getString(position));

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        ((ViewPager) container).addView(imageView, 0);
        return imageView;
    }

    private class DownloadImageFromInternet extends AsyncTask<String, Void, Bitmap> {
        ImageView imageView;

        public DownloadImageFromInternet(ImageView imageView) {
            this.imageView = imageView;
            Toast.makeText(mContext, "Please wait, it may take a few minute...", Toast.LENGTH_SHORT).show();
        }

        protected Bitmap doInBackground(String... urls) {
            String imageURL = urls[0];
            Bitmap bimage = null;
            try {
                InputStream in = new java.net.URL(imageURL).openStream();
                bimage = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error Message", e.getMessage());
                e.printStackTrace();
            }
            return bimage;
        }

        protected void onPostExecute(Bitmap result) {
            imageView.setImageBitmap(result);
        }
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((ImageView) object);
    }

    @Override
    public int getCount() {
        return imglist.length();
    }
}