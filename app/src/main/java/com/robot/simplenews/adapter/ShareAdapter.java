package com.robot.simplenews.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.robot.simplenews.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ShareAdapter extends RecyclerView.Adapter<ShareAdapter.ViewHolder> {
    private final Context mContext;
    private List<ShareEntity> mShareEntityList = new ArrayList<>();
    public static final int SHARE_POSITION_QQ = 0;
    public static final int SHARE_POSITION_SINA = 1;
    public static final int SHARE_POSITION_WECHAT = 2;
    public static final int SHARE_POSITION_SMS = 3;

    public ShareAdapter(Context context) {
        this.mContext = context;
        Resources res = mContext.getResources();
        mShareEntityList.add(new ShareEntity(R.drawable.share_qq_normal, res.getString(R.string.share_qq)));
        mShareEntityList.add(new ShareEntity(R.drawable.share_sina_normal, res.getString(R.string.share_sina)));
        mShareEntityList.add(new ShareEntity(R.drawable.share_wechat_normal, res.getString(R.string.share_wechat)));
        mShareEntityList.add(new ShareEntity(R.drawable.share_sms_normal, res.getString(R.string.share_sms)));
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_share_item, parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        ShareEntity shareEntity = mShareEntityList.get(position);
        holder.mImgShare.setImageResource(shareEntity.imgRes);
        holder.mTvShare.setText(shareEntity.msg);
    }

    @Override
    public int getItemCount() {
        return mShareEntityList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.imgShare)
        ImageView mImgShare;
        @BindView(R.id.tvShare)
        TextView mTvShare;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(view, this.getLayoutPosition());
            }
        }
    }

    public OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    public static class ShareEntity {
        public int imgRes;
        public String msg;

        public ShareEntity(int imgRes, String msg) {
            this.imgRes = imgRes;
            this.msg = msg;
        }
    }
}