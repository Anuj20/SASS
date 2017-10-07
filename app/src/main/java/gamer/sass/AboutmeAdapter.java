package gamer.sass;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

/**
 * Created by gamer on 8/26/2017.
 */

public class AboutmeAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<String> ListHeader;
    private HashMap<String, List<String>> ListChild;

    public AboutmeAdapter(Context context, List<String> ListHeader, HashMap<String, List<String>> ListChild){
        this.context= context;
        this.ListHeader= ListHeader;
        this.ListChild= ListChild;
    }

    @Override
    public int getGroupCount() {
        return this.ListHeader.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this.ListChild.get(this.ListHeader.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.ListHeader.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return this.ListChild.get(this.ListHeader.get(groupPosition))
                .get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
        //return childPosition;

    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String headerview= (String) getGroup(groupPosition);

        if(convertView==null){
            LayoutInflater infl= (LayoutInflater) this.context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

            convertView= infl.inflate(R.layout.list_group, null);
        }
        TextView listheader= (TextView) convertView.findViewById(R.id.ListHeader);
        listheader.setTypeface(null, Typeface.BOLD);
        listheader.setText(headerview);

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final String child= (String) getChild(groupPosition, childPosition);
        if(convertView==null){
            LayoutInflater inflator= (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView= inflator.inflate(R.layout.list_item, null);
        }
        TextView childtext= (TextView) convertView.findViewById(R.id.childListItem);
        childtext.setText(child);
        return convertView;
    }
}

