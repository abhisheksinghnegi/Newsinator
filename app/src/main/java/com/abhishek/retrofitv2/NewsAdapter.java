package com.abhishek.retrofitv2;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.MyVh> {
    Context context;
    List<ArticleModel> list;
    NewsAdapter(Context context, List<ArticleModel> list)
    {
        this.context = context;
        this.list = list;
    }
    @NonNull
    @Override
    public NewsAdapter.MyVh onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View v = layoutInflater.inflate(R.layout.news_layout,parent,false);
        return new MyVh(v);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsAdapter.MyVh holder, final int position) {
        Glide.with(context).load(list.get(position).getUrlToImage()).into(holder.newsimage);
        holder.title.setText(list.get(position).getTitle());
        holder.author.setText(list.get(position).getAuthor());
        holder.description.setText(list.get(position).getDescription());
        holder.publ.setText(list.get(position).getPublishedAt());
        holder.newsimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,WebActivity.class);
                intent.putExtra("URL",list.get(position).getUrl());
                context.startActivity(intent);
            }
        });
        holder.title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,WebActivity.class);
                intent.putExtra("URL",list.get(position).getUrl());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyVh extends RecyclerView.ViewHolder {
        ImageView newsimage;
        TextView author;
        TextView description;
        TextView title;
        TextView publ;
        public MyVh(@NonNull View itemView) {
            super(itemView);
            newsimage = itemView.findViewById(R.id.newsimage);
            author = itemView.findViewById(R.id.newsauthor);
            description = itemView.findViewById(R.id.newsdesc);
            title = itemView.findViewById(R.id.newstitle);
            publ = itemView.findViewById(R.id.newspubl);

        }
    }
}
