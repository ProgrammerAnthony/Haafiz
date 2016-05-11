package edu.com.mvpcommon.main;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;



import java.util.List;

import edu.com.mvpcommon.R;


public class NavDrawerListAdapter extends BaseAdapter {

    private Context context;
    private List<DrawerData.DrawerItem> drawerItems;

    public NavDrawerListAdapter(Context context, List<DrawerData.DrawerItem> drawerItems) {
        this.context = context;
        this.drawerItems = drawerItems;
    }

    @Override
    public int getCount() {
        return drawerItems.size();
    }

    @Override
    public Object getItem(int position) {
        return drawerItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater)
                    context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.drawer_list_item, null);
        }

        ImageView imgIcon = (ImageView) convertView.findViewById(R.id.drawer_icon);
        TextView txtTitle = (TextView) convertView.findViewById(R.id.drawer_title);
        TextView txtCount = (TextView) convertView.findViewById(R.id.drawer_counter);

        imgIcon.setImageResource(drawerItems.get(position).getIcon());
        imgIcon.setColorFilter(context.getResources().getColor(drawerItems.get(position).getTint()));
        txtTitle.setText(drawerItems.get(position).getTitle());

        // displaying count
        // check whether it set visible or not
        if (drawerItems.get(position).getCounterVisibility()) {
            txtCount.setText(drawerItems.get(position).getCount());
        } else {
            // hide the counter view
            txtCount.setVisibility(View.GONE);
        }
        return convertView;
    }

}
