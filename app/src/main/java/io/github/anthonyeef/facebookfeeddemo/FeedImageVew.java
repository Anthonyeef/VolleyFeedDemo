package io.github.anthonyeef.facebookfeeddemo;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.volley.toolbox.ImageLoader;

/**
 * Created by anthonyeef on 8/8/15.
 * Keep the code clean and organized.
 * RTFM.
 */
public class FeedImageVew extends ImageView{

    private String mUrl;
    private int mDefaultImageId;
    private int mErrorImageId;
    private ResponseObserver mObserver;

    private ImageLoader mImageLoader;
    private ImageLoader.ImageContainer mImageContainer;

    public interface ResponseObserver {
        public void onError();
        public void onSuccess();
    }


    public void setResponseObserver(ResponseObserver observer) {
        mObserver = observer;
    }


    public FeedImageVew(Context context) {
        this(context, null);
    }
    public FeedImageVew(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }
    public FeedImageVew(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setImageUrl(String url, ImageLoader imageLoader) {
        mUrl = url;
        mImageLoader = imageLoader;
        loadImageIfNecessary(false);
    }

    public void setDefaultImageResId(int defaultImage) {
        mDefaultImageId = defaultImage;
    }

    public void setErrorImageResId(int errorImage) {
        mErrorImageId = errorImage;
    }

    private void loadImageIfNecessary(final boolean isInLayoutPass) {
        final int width = getWidth();
        int height = getHeight();

        boolean isFullWrapContent = getLayoutParams() != null
                && getLayoutParams().height == ViewGroup.LayoutParams.WRAP_CONTENT
                && getLayoutParams().width == ViewGroup.LayoutParams.WRAP_CONTENT;

        if (width == 0 && height == 0 && isFullWrapContent) {
            return;
        }

        if (TextUtils.isEmpty(mUrl)) {
            if (mImageContainer != null) {
                mImageContainer.cancelRequest();
                mImageContainer = null;
            }
            setDefaultImageOrNull();
            return;
        }
        if (mImageContainer != null && mImageContainer.getRequestUrl() != null) {
            if (mImageContainer.getRequestUrl().equals(mUrl)){
                return;
            } else {
                mImageContainer.cancelRequest();
                setDefaultImageOrNull();
            }
        }

    }

    private void setDefaultImageOrNull() {
        if (mDefaultImageId != 0) {
            setImageResource(mDefaultImageId);
        } else {
            setImageBitmap(null);
        }
    }
}
