package com.example.administrator.studentmanager.adapter;

/**
 * Created by Administrator on 14/05/2017.
 */

import android.content.Context;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.style.BackgroundColorSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.studentmanager.R;
import com.example.administrator.studentmanager.activity.ListStudentsActivity;
import com.example.administrator.studentmanager.model.Student;

import java.util.List;


/**
 * Created by Administrator on 4/21/2016.
 */
public class StudentAdapter extends ArrayAdapter<Student> {

    private Context mContext;
    private int mResourceID;
    private List<Student> mListStudents;

    public StudentAdapter(Context context, int resource, List<Student> objects) {
        super(context, resource, objects);
        this.mContext = context;
        this.mResourceID = resource;
        this.mListStudents = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        viewHolder holder;
        if(convertView == null || convertView.getTag() == null){
            convertView = View.inflate(mContext,mResourceID,null);

            holder = new viewHolder();
            holder.tvOrder = (TextView) convertView.findViewById(R.id.tvOrder);
            holder.tvId = (TextView) convertView.findViewById(R.id.tvId);
            holder.tvName = (TextView) convertView.findViewById(R.id.tvName);
            holder.tvBirthday = (TextView) convertView.findViewById(R.id.tvBirthday);
            holder.ivGender = (ImageView) convertView.findViewById(R.id.ivGender);

            convertView.setTag(holder);
        }else
            holder = (viewHolder) convertView.getTag();

        Student student = mListStudents.get(position);

        holder.tvOrder.setText(String.valueOf(position + 1));
        holder.tvId.setText(student.getId());

        // search text effect
        String name = student.getName();
        if(ListStudentsActivity.mSearchText != null){
            // equals ignore Case
            int startIndex = name.toLowerCase().indexOf(ListStudentsActivity.mSearchText.toLowerCase());
            if(startIndex >= 0){
                int endIndex = ListStudentsActivity.mSearchText.length();
                SpannableString textSpan = new SpannableString(name);
                textSpan.setSpan(new BackgroundColorSpan(Color.RED),startIndex,endIndex,SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
                holder.tvName.setText(textSpan);
            }
        }else
            holder.tvName.setText(student.getName());

        holder.tvBirthday.setText(student.getBirthday());
        if(student.getGender() == 0)
            holder.ivGender.setImageResource(R.drawable.girlicon);
        else
            holder.ivGender.setImageResource(R.drawable.boyicon);

        return convertView;
    }

    private class viewHolder{
        TextView tvOrder, tvId, tvName, tvBirthday;
        ImageView ivGender;
    }
}
