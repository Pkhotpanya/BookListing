package com.example.android.booklisting;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.R.attr.resource;
import static android.R.id.list;

/**
 * Created by pkhotpanya on 6/23/17.
 */

public class BookAdapter extends ArrayAdapter<Book> {

    public BookAdapter(@NonNull Context context, List<Book> books) {
        super(context, 0, books);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listViewItem = convertView;
        ViewHolder holder;
        if (listViewItem == null){
            listViewItem = LayoutInflater.from(getContext()).inflate(R.layout.book_item, parent, false);
            holder = new ViewHolder(listViewItem);
            listViewItem.setTag(holder);
        } else {
            holder = (ViewHolder) listViewItem.getTag();
        }

        Book currentItem = getItem(position);
        holder.title.setText(currentItem.getTitle());
        holder.authors.setText(TextUtils.join(", ", currentItem.getAuthors()));

        return listViewItem;
    }

    public class ViewHolder {
        @BindView(R.id.textview_title)
        TextView title;
        @BindView(R.id.textview_authors)
        TextView authors;

        public ViewHolder(View view){
            ButterKnife.bind(this, view);
        }
    }
}
