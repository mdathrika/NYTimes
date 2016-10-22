package com.codepath.nytimes.adapters;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.nytimes.R;
import com.codepath.nytimes.models.Article;

import java.util.List;

/**
 * Created by mdathrika on 10/20/16.
 */

public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ViewHolder> {
    // View lookup cache
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView ivCover;
        public TextView tvHeadline;

        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            ivCover = (ImageView)itemView.findViewById(R.id.ivArticleCover);
            tvHeadline = (TextView)itemView.findViewById(R.id.tvArticleHeadline);
        }
    }

    private Context context;
    private List<Article> articles;

    public ArticleAdapter(Context context, List<Article> articles) {
        this.context = context;
        this.articles = articles;
    }

    private Context getContext() {
        return context;
    }

    @Override
    public ArticleAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View articleView = inflater.inflate(R.layout.item_article_result, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(articleView);
        return viewHolder;
    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(ArticleAdapter.ViewHolder viewHolder, int position) {
        // Get the data model based on position
        Article article = articles.get(position);
        viewHolder.tvHeadline.setText(article.getHeadline());
        Glide.with(getContext()).load(Uri.parse(article.getThumbnailUrl())).placeholder(R.drawable.ic_nocover).into(viewHolder.ivCover);

    }

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return articles.size();
    }
}
