package com.planis.johannes.intelmeme.ui;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.planis.johannes.intelmeme.events.ImagePickedLocallyEvent;
import com.planis.johannes.intelmeme.R;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class LayoutLocalListItemAdapter extends BaseAdapter {

    private List<Integer> objects = new ArrayList<>();

    private Context context;
    private LayoutInflater layoutInflater;

    public LayoutLocalListItemAdapter(Context context, List<Integer> list) {
        objects = list;
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return objects.size();
    }

    @Override
    public Integer getItem(int position) {
        return objects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.layout_local_list_item, null);
            convertView.setTag(new ViewHolder(convertView));
        }
        initializeViews(getItem(position), (ViewHolder) convertView.getTag());
        return convertView;
    }

    private void initializeViews(final Integer object, ViewHolder holder) {

        holder.ivLocalListItem.setImageDrawable(ContextCompat.getDrawable(context,object));
        holder.ivLocalListItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new ImagePickedLocallyEvent(object));
            }
        });
    }

    static class ViewHolder {
        @Bind(R.id.ivLocalListItem) ImageView ivLocalListItem;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }


}
