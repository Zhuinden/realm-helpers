package com.zhuinden.realmmanagerexample;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.zhuinden.realmmanager.RealmManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Case;
import io.realm.OrderedRealmCollection;
import io.realm.Realm;
import io.realm.RealmRecyclerViewAdapter;

public class MainActivity
        extends AppCompatActivity {
    RealmManager realmManager;

    @BindView(R.id.search_view)
    SearchView searchView;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    CatAdapter adapter;

    @SuppressLint("NewAPI")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        realmManager = Injector.get().realmManager();
        realmManager.openLocalInstance();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setupRecyclerView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realmManager.closeLocalInstance();
    }

    //
    private void setupRecyclerView() {
        Realm realm = realmManager.getLocalInstance();
        recyclerView.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL,
                false));
        adapter = new CatAdapter(realm.where(Cat.class).findAllAsync());
        recyclerView.setAdapter(adapter);
        setupSearchView();
    }

    private void setupSearchView() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                adapter.getFilter().filter(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(searchView.getQuery().length() == 0) {
                    adapter.getFilter().filter("");
                }
                return true;
            }
        });
    }

    ////
    private static class ViewHolder
            extends RecyclerView.ViewHolder {

        TextView textView;

        public ViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView;
        }

        public void bind(Cat cat) {
            textView.setText(cat.getName());
        }

    }

    private class CatAdapter
            extends RealmRecyclerViewAdapter<Cat, ViewHolder>
            implements Filterable {

        public CatAdapter(@Nullable OrderedRealmCollection<Cat> data) {
            super(data, true);
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            TextView textView = new AppCompatTextView(MainActivity.this); // don't do this at home
            RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            int _16dpInPx = ((Float) ScreenUtils.dpToPx(16)).intValue();
            textView.setPadding(_16dpInPx, _16dpInPx, _16dpInPx, _16dpInPx);
            textView.setLayoutParams(layoutParams);
            return new ViewHolder(textView);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            // noinspection ConstantConditions
            holder.bind(getData().get(position));
        }

        // filtering
        public void filterResults(String text) {
            Realm realm = realmManager.getLocalInstance();
            text = text == null ? null : text.toLowerCase().trim();
            if(text == null || "".equals(text)) {
                updateData(realm.where(Cat.class).findAllAsync());
            } else {
                updateData(realm
                        .where(Cat.class)
                        .contains(CatFields.NAME, text, Case.INSENSITIVE)
                        .findAllAsync());
            }
        }

        public Filter getFilter() {
            return new CatFilter(this);
        }

        private class CatFilter
                extends Filter {
            private final CatAdapter adapter;

            private CatFilter(CatAdapter adapter) {
                super();
                this.adapter = adapter;
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                return new FilterResults();
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                adapter.filterResults(constraint.toString());
            }
        }
    }
}
