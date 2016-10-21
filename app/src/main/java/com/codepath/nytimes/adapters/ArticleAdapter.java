package com.codepath.nytimes.adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.nytimes.R;
import com.codepath.nytimes.models.Article;

import java.util.ArrayList;

/**
 * Created by mdathrika on 10/20/16.
 */

public class ArticleAdapter extends ArrayAdapter<Article> {
    // View lookup cache
    private static class ViewHolder {
        public ImageView ivCover;
        public TextView tvHeadline;
    }

    public ArticleAdapter(Context context, ArrayList<Article> articles) {
        super(context, 0, articles);
    }

    // Translates a particular `Book` given a position
    // into a relevant row within an AdapterView
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        final Article article = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_article_result, parent, false);
            viewHolder.ivCover = (ImageView)convertView.findViewById(R.id.ivArticleCover);
            viewHolder.tvHeadline = (TextView)convertView.findViewById(R.id.tvArticleHeadline);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        // Populate data into the template view using the data object
        viewHolder.tvHeadline.setText(article.getHeadline());
        Glide.with(getContext()).load(Uri.parse(article.getThumbnailUrl())).placeholder(R.drawable.ic_nocover).into(viewHolder.ivCover);
        // Return the completed view to render on screen
        return convertView;
    }
}
