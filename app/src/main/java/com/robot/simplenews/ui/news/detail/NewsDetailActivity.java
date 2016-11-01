package com.robot.simplenews.ui.news.detail;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.robot.simplenews.R;
import com.robot.simplenews.entity.NewsEntity;
import com.robot.simplenews.util.DensityUtil;
import com.robot.simplenews.util.ImageLoaderUtil;

import org.sufficientlysecure.htmltextview.HtmlTextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.imid.swipebacklayout.lib.SwipeBackLayout;
import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

/**
 * 新闻详情界面
 */
public class NewsDetailActivity extends SwipeBackActivity implements NewsDetailContract.View {
    private static final String TAG = NewsDetailActivity.class.getSimpleName();
    private static final String KEY_PARAM_NEWS = "news";

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.htNewsContent)
    HtmlTextView mTVNewsContent;
    @BindView(R.id.progress)
    ProgressBar mProgressBar;

    private SwipeBackLayout mSwipeBackLayout;
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

        mSwipeBackLayout = getSwipeBackLayout();
        mSwipeBackLayout.setEdgeSize(DensityUtil.getWidthInPx(this));
        mSwipeBackLayout.setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT);

        mNews = (NewsEntity) getIntent().getParcelableExtra(KEY_PARAM_NEWS);

        CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(mNews.getTitle());

        ImageLoaderUtil.display(getApplicationContext(), (ImageView) findViewById(R.id.ivImage), mNews.getImgsrc());

        mNewsDetailPresenter = new NewsDetailPresenter(this);
        mNewsDetailPresenter.attachView(this);
        mNewsDetailPresenter.loadNewsDetail(mNews.getDocid());
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mNewsDetailPresenter.detachView();
    }
}
