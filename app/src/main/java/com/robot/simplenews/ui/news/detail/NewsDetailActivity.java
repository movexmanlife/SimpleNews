package com.robot.simplenews.ui.news.detail;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.jakewharton.rxbinding.view.RxView;
import com.robot.simplenews.R;
import com.robot.simplenews.adapter.OnItemClickListener;
import com.robot.simplenews.adapter.ShareAdapter;
import com.robot.simplenews.entity.NewsEntity;
import com.robot.simplenews.ui.base.BaseActivity;
import com.robot.simplenews.util.DensityUtil;
import com.robot.simplenews.util.ImageLoaderUtil;
import com.robot.simplenews.util.ToastUtil;

import org.sufficientlysecure.htmltextview.HtmlTextView;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.imid.swipebacklayout.lib.SwipeBackLayout;
import me.imid.swipebacklayout.lib.app.SwipeBackActivity;
import rx.functions.Action1;

/**
 * 新闻详情界面
 */
public class NewsDetailActivity extends BaseActivity implements NewsDetailContract.View {
    private static final String TAG = NewsDetailActivity.class.getSimpleName();
    private static final String KEY_PARAM_NEWS = "news";

    @BindView(R.id.fab)
    FloatingActionButton mFab;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.htNewsContent)
    HtmlTextView mTVNewsContent;
    @BindView(R.id.progress)
    ProgressBar mProgressBar;
    BottomSheetDialog mDialog;

    private NewsDetailPresenter mNewsDetailPresenter;
    private NewsEntity mNews;

    public static void start(Activity activity, NewsEntity newsEntity, View transitionView) {
        Intent intent = new Intent(activity, NewsDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(KEY_PARAM_NEWS, newsEntity);
        intent.putExtras(bundle);

        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity,
                transitionView, activity.getResources().getString(R.string.transition_news_img));
        ActivityCompat.startActivity(activity, intent, options.toBundle());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        mNews = (NewsEntity) getIntent().getParcelableExtra(KEY_PARAM_NEWS);

        CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(mNews.getTitle());

        ImageLoaderUtil.display(getApplicationContext(), (ImageView) findViewById(R.id.ivImage), mNews.getImgsrc());

        mNewsDetailPresenter = new NewsDetailPresenter(this);
        mNewsDetailPresenter.attachView(this);
        mNewsDetailPresenter.loadNewsDetail(mNews.getDocid());

        /**
         * 防止抖动
         */
        RxView.clicks(mFab)
                .throttleFirst(2000, TimeUnit.MILLISECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        onShare();
                    }
                });
    }

    @Override
    public void showNewsDetialContent(String newsDetailContent) {
        mTVNewsContent.setHtmlFromString(newsDetailContent, new HtmlTextView.LocalImageGetter());
    }

    @Override
    public void showProgress() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        mProgressBar.setVisibility(View.GONE);
    }

    public void onShare() {
        showShareDialog();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mNewsDetailPresenter.detachView();
    }

    @Override
    public void showShareDialog() {
        RecyclerView recyclerView = (RecyclerView) LayoutInflater.from(this)
                .inflate(R.layout.share_list, null);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        ShareAdapter adapter = new ShareAdapter(this);
        adapter.setOnItemClickListener(new OnShareItemClickListener());
        recyclerView.setAdapter(adapter);

        if (mDialog == null) {
            mDialog = new BottomSheetDialog(this);
        }
        mDialog.setContentView(recyclerView);
        mDialog.show();
    }

    public class OnShareItemClickListener implements OnItemClickListener {
        public OnShareItemClickListener() {
        }

        @Override
        public void onItemClick(View view, int position) {
            int msgResId = R.string.share_qq;
            if (position == ShareAdapter.SHARE_POSITION_QQ) {
                msgResId = R.string.share_qq;
            } else if (position == ShareAdapter.SHARE_POSITION_SINA) {
                msgResId = R.string.share_sina;
            } else if (position == ShareAdapter.SHARE_POSITION_WECHAT) {
                msgResId = R.string.share_wechat;
            }else if (position == ShareAdapter.SHARE_POSITION_SMS) {
                msgResId = R.string.share_sms;
            }
            ToastUtil.show(msgResId);
            mDialog.dismiss();
        }
    }
}
