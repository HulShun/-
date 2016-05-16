package com.example.newsclient.view.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.newsclient.R;

/**
 * Created by Administrator on 2016-05-16.
 */
public class FooterViewHolder extends RecyclerView.ViewHolder {
    public LinearLayout loadingLayout;
    public Button button;


    public FooterViewHolder(View itemView) {
        super(itemView);
        loadingLayout = (LinearLayout) itemView.findViewById(R.id.footer_loadinglayout);
        button = (Button) itemView.findViewById(R.id.footer_btn);

    }
}
