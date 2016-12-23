package ep.rest;

import android.content.Context;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.ViewHolder> {

    private final List<Book> books;

    private OnItemClickListener clickListener;

    public BookAdapter() {
        books = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final Context context = parent.getContext();
        final LayoutInflater inflater = LayoutInflater.from(context);
        final View contactView = inflater.inflate(R.layout.booklist_element, parent, false);
        return new ViewHolder(contactView, clickListener);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Book book = books.get(position);
        holder.tvTitle.setText(book.title);
        holder.tvAuthor.setText(book.author);
        holder.tvPrice.setText(String.format(Locale.ENGLISH, "%.2f EUR", book.price));
    }

    @Override
    public int getItemCount() {
        return books.size();
    }

    public Book getItem(int position) {
        return books.get(position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.clickListener = listener;
    }

    /**
     * Namenski vmesnik za lovljenje klikov na recyclerview-ju
     * https://guides.codepath.com/android/using-the-recyclerview#attaching-click-handlers-to-items
     */
    interface OnItemClickListener {
        void onItemClick(View itemView, int position);
    }

    /**
     * Implementacija zgornjega vmesnika
     */
    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvAuthor, tvPrice;

        ViewHolder(final View itemView, final OnItemClickListener clickListener) {
            super(itemView);
            tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
            tvAuthor = (TextView) itemView.findViewById(R.id.tv_author);
            tvPrice = (TextView) itemView.findViewById(R.id.tv_price);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (clickListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            clickListener.onItemClick(itemView, position);
                        }
                    }
                }
            });
        }
    }

    /**
     * Izračuna razlike med dvema seznamoma in posledično naredi minimalno
     * število potrebnih sprememb.
     * https://guides.codepath.com/android/using-the-recyclerview#diffing-larger-changes
     */
    static class BookDiffCallback extends DiffUtil.Callback {
        private final List<Book> oldList, newList;

        BookDiffCallback(List<Book> oldList, List<Book> newList) {
            this.oldList = oldList;
            this.newList = newList;
        }

        @Override
        public int getOldListSize() {
            return oldList.size();
        }

        @Override
        public int getNewListSize() {
            return newList.size();
        }

        @Override
        public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
            return oldList.get(oldItemPosition).id == newList.get(newItemPosition).id;
        }

        @Override
        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
            return oldList.get(oldItemPosition).equals(newList.get(newItemPosition));
        }
    }

    /*
     * Uporaba razreda BookDiffCallback in posodobitev recyclerview-ja
     * https://guides.codepath.com/android/using-the-recyclerview#diffing-larger-changes
     */
    public void swap(List<Book> newBooks) {
        final BookDiffCallback diffCallback = new BookDiffCallback(books, newBooks);
        final DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffCallback);

        books.clear();
        books.addAll(newBooks);
        diffResult.dispatchUpdatesTo(this);
    }
}
