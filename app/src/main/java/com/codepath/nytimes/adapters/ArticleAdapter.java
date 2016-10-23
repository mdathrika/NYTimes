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
    private final int IMAGE_TITLE = 0, TITLE = 1;
    private int resource1, resource2;

    public ArticleAdapter(Context context, List<Article> articles, int resource1, int resource2) {
        this.context = context;
        this.articles = articles;
        this.resource1 = resource1;
        this.resource2 = resource2;
    }

    private Context getContext() {
        return context;
    }

    @Override
    public int getItemViewType(int position) {
        if (articles.get(position).getThumbnailUrl() == null || articles.get(position).getThumbnailUrl().length() == 0) {
            return TITLE;
        } else  {
            return IMAGE_TITLE;
        }
    }

    @Override
    public ArticleAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View articleView;
        switch (viewType) {
            case IMAGE_TITLE:
                articleView = inflater.inflate(resource1, parent, false);
                break;
            case TITLE:
                articleView = inflater.inflate(resource2, parent, false);
                break;
            default:
                articleView = inflater.inflate(resource2, parent, false);
                break;
        }
        // Inflate the custom layout


        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(articleView);
        return viewHolder;
    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(ArticleAdapter.ViewHolder viewHolder, int position) {
        // Get the data model based on position
        Article article = articles.get(position);
        switch (viewHolder.getItemViewType()) {
            case IMAGE_TITLE:
                viewHolder.tvHeadline.setText(article.getHeadline());
                Glide.with(getContext()).load(Uri.parse(article.getThumbnailUrl())).placeholder(R.drawable.ic_nocover).into(viewHolder.ivCover);
                break;
            case TITLE:
                viewHolder.tvHeadline.setText(article.getHeadline());
                break;
            default:
                break;
        }

    }

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return articles.size();
    }
}
