package cn.vove7.bingwallpaper.adapters;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.request.RequestOptions;

import java.io.File;

import cn.vove7.bingwallpaper.R;
import cn.vove7.bingwallpaper.utils.LogHelper;

import static cn.vove7.bingwallpaper.services.DownloadService.directory;
import static cn.vove7.bingwallpaper.utils.Utils.isFileExist;

/**
 * Created by Vove on 2017/11/12.
 */

public class ViewPageAdapter extends PagerAdapter {
   private Context context;
   private String[] images;
   private String[] startdates;
   private int imageFrom;
   ProgressBar progressBar;

   public static final int IMAGE_FROM_LOCAL = 0;
   public static final int IMAGE_FROM_INTERNET = 1;

   public ViewPageAdapter(Context context, String[] images, String[] startdates, int imageFrom) {
      this.context = context;
      this.images = images;
      this.imageFrom = imageFrom;
      this.startdates = startdates;
   }

   @Override
   public int getCount() {
      return images.length;
   }

   @Override
   public void destroyItem(ViewGroup container, int position, Object object) {
      container.removeView((View) object);

   }

   @Override
   public boolean isViewFromObject(View view, Object object) {
      return view == object;
   }

   @Override
   public Object instantiateItem(ViewGroup container, int position) {
      View view = LayoutInflater.from(context).inflate(R.layout.fragment_view_image_activity, container, false);
      final ImageView imageView = view.findViewById(R.id.view_image);
      progressBar = view.findViewById(R.id.view_progressbar);

      switch (imageFrom) {
         case IMAGE_FROM_LOCAL: {
            LogHelper.logD(null, "from local***");
            File file = new File(directory + "/" + images[position]);
            glideToView(container, imageView, file, null);
         }
         break;
         case IMAGE_FROM_INTERNET: {
            String filename = directory + "/" + startdates[position] + ".jpg";
            LogHelper.logD(null, "filename->" + filename);
            if (isFileExist(filename)) {
               LogHelper.logD(null, "internet from local***");
               File file = new File(filename);
               glideToView(container, imageView, file, null);
            } else {
               LogHelper.logD(null, "from internet***");
               glideToView(container, imageView, null, images[position] + "_1920x1080.jpg");
            }
         }
         break;
         default:
            return null;
      }
      container.addView(view);
      return view;

   }

   private void glideToView(ViewGroup container, ImageView v, File file, String url) {
      RequestOptions requestOptions = new RequestOptions()
              .centerCrop()
              .override(1920, 1080)
              .skipMemoryCache(true)
              .error(R.drawable.ic_error_white_48dp);
      RequestBuilder builder;
      if (file == null) {
         builder = Glide.with(container)
                 .load(url).apply(requestOptions);
      } else {
         builder = Glide.with(container)
                 .load(file).apply(requestOptions);

      }
      builder.into(v);
   }

}