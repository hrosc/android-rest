package ep.rest;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookDetailActivity extends AppCompatActivity implements Callback<Book> {
    private static final String TAG = BookDetailActivity.class.getCanonicalName();

    private Book book;
    private TextView tvBookDetail;
    private CollapsingToolbarLayout toolbarLayout;
    private FloatingActionButton fabEdit, fabDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);

        tvBookDetail = (TextView) findViewById(R.id.tv_book_detail);

        fabEdit = (FloatingActionButton) findViewById(R.id.fab_edit);
        fabEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Intent intent = new Intent(BookDetailActivity.this, BookFormActivity.class);
                intent.putExtra("ep.rest.book", book);
                startActivity(intent);
            }
        });
        fabDelete = (FloatingActionButton) findViewById(R.id.fab_delete);
        fabDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder dialog = new AlertDialog.Builder(BookDetailActivity.this);
                dialog.setTitle("Confirm deletion");
                dialog.setMessage("Are you sure?");
                dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteBook();
                    }
                });
                dialog.setNegativeButton("Cancel", null);
                dialog.create().show();
            }
        });


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final int id = getIntent().getIntExtra("ep.rest.id", 0);
        if (id > 0) {
            BookService.getInstance().get(id).enqueue(this);
        }
    }

    private void deleteBook() {
        BookService.getInstance().delete(book.id).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Log.i(TAG, "Deletion succeeded");
                final Intent intent = new Intent(BookDetailActivity.this, MainActivity.class);
                startActivity(intent);
                Toast.makeText(BookDetailActivity.this, "Deleted: " + book.title, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.i(TAG, "Deletion failed: " + t.getMessage(), t);
                Toast.makeText(BookDetailActivity.this, "Deletion failed.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onResponse(Call<Book> call, Response<Book> response) {
        book = response.body();
        Log.i(TAG, "Got result: " + book);

        if (response.isSuccessful()) {
            tvBookDetail.setText(book.description);
            toolbarLayout.setTitle(book.title);
        } else {
            String errorMessage;
            try {
                errorMessage = "An error occurred: " + response.errorBody().string();
            } catch (IOException e) {
                errorMessage = "An error occurred: error while decoding the error message.";
            }
            Log.e(TAG, errorMessage);
            tvBookDetail.setText(errorMessage);
        }
    }

    @Override
    public void onFailure(Call<Book> call, Throwable t) {
        Log.w(TAG, "Error: " + t.getMessage(), t);
    }
}
