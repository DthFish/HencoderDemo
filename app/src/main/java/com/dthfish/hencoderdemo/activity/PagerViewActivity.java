package com.dthfish.hencoderdemo.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dthfish.hencoderdemo.R;

public class PagerViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pager_view);
        ViewPager cvpOne = findViewById(R.id.cvp_one);
        ViewPager cvpTwo = findViewById(R.id.cvp_two);
        cvpOne.setAdapter(new TextAdapter("ViewPager One", 2));
        cvpTwo.setAdapter(new RvAdapter());

        RecyclerView rv = findViewById(R.id.rv);
        rv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rv.setAdapter(new ListAdapter("SinglePage"));

    }

    private static class ListAdapter extends RecyclerView.Adapter<TextViewHolder> {

        private final String name;

        public ListAdapter(String name) {
            this.name = name;
        }

        @Override
        public TextViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            TextView textView = new TextView(parent.getContext());
            textView.setGravity(Gravity.CENTER);
            textView.setTextSize(18);
            textView.setPadding(10, 10, 10, 10);
            return new TextViewHolder(textView);
        }

        @Override
        public void onBindViewHolder(TextViewHolder holder, int position) {
            holder.tv.setText("Recycler " + name + ":" + position);

        }

        @Override
        public int getItemCount() {
            return 10;
        }

    }

    private static class TextViewHolder extends RecyclerView.ViewHolder {
        TextView tv;

        public TextViewHolder(View itemView) {
            super(itemView);
            tv = (TextView) itemView;
        }

    }

    private static class RvAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @SuppressLint("SetTextI18n")
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            RecyclerView recyclerView = new RecyclerView(container.getContext());
            recyclerView.setLayoutManager(new LinearLayoutManager(container.getContext()
                    , LinearLayoutManager.HORIZONTAL, false));
            recyclerView.setAdapter(new ListAdapter("No." + position));

            container.addView(recyclerView,
                    new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT));
            return recyclerView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }


    private static class TextAdapter extends PagerAdapter {
        private final String name;
        private final int count;

        public TextAdapter(String name, int count) {
            this.name = name;
            this.count = count;
        }

        @Override
        public int getCount() {
            return count;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @SuppressLint("SetTextI18n")
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            TextView textView = new TextView(container.getContext());
            textView.setGravity(Gravity.CENTER);
            textView.setTextSize(30);
            textView.setText(name + ":" + position);
            container.addView(textView,
                    new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT));
            return textView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

}
