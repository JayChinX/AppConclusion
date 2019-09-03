package com.hymnal.base.image;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.module.AppGlideModule;

import java.io.IOException;
import java.io.InputStream;

import androidx.annotation.NonNull;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Registers OkHttp related classes via Glide's annotation processor.
 *
 * <p>For Applications that depend on this library and include an
 * {@link AppGlideModule} and Glide's annotation processor, this class
 * will be automatically included.
 */
@GlideModule
public final class OkHttpLibraryGlideModule extends AppGlideModule {
  @Override
  public void registerComponents(@NonNull Context context, @NonNull Glide glide,
                                 @NonNull Registry registry) {

    //定制OkHttp
    OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();

    //请求头设置
    httpClientBuilder.interceptors().add(new HeadInterceptor());

    OkHttpClient okHttpClient = httpClientBuilder.build();
    registry.replace(GlideUrl.class, InputStream.class, new OkHttpUrlLoader.Factory(okHttpClient));
  }

  public static class HeadInterceptor implements Interceptor {

    public HeadInterceptor() {
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
      Request.Builder request = chain.request().newBuilder();

      //这里添加我们需要的请求头 请求证书等等
      request.addHeader("Referer", "http://www.baidu.com");
      return chain.proceed(request.build());
    }
  }
}
