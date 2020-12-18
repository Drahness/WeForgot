package self.joanciscar.myapplication.ui.items;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import self.joanciscar.myapplication.R;
import self.joanciscar.myapplication.data.Item;

import self.joanciscar.myapplication.ui.reminders.RemindersFragment;

public class ItemsFragment extends Fragment {

    private List<Item> mItems;
    private ItemAdapter mItemsAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ItemsViewModel galleryViewModel = new ViewModelProvider(this).get(ItemsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_items, container, false);
        RecyclerView recyclerView = root.findViewById(R.id.itemsRecycler);
        galleryViewModel.getLiveData().observe(getViewLifecycleOwner(), new Observer<List<Item>>() {
            @Override
            public void onChanged(List<Item> updatedItems) {
                if (mItems == null) {
                    mItems = updatedItems;
                    mItemsAdapter = new ItemsFragment.ItemAdapter();
                    recyclerView.setAdapter(mItemsAdapter);
                } else {
                    DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
                        @Override
                        public int getOldListSize() {
                            return mItems.size();
                        }

                        @Override
                        public int getNewListSize() {
                            return updatedItems.size();
                        }

                        @Override
                        public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                            return mItems.get(oldItemPosition).getId() ==
                                    updatedItems.get(newItemPosition).getId();
                        }

                        @Override
                        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                            return mItems.get(oldItemPosition).equals(updatedItems.get(newItemPosition));
                        }
                    });
                    result.dispatchUpdatesTo(mItemsAdapter);
                    mItems = updatedItems;
                }
            }
        });
        return root;
    }

    public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {
        @NonNull
        @Override
        public ItemAdapter.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.weforgot_item_list_item,parent,false);
            return new ItemAdapter.ItemViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ItemAdapter.ItemViewHolder holder, int position) {
            Item value = mItems.get(position);
            if (value.getLocalfoto() != null) {
                holder.picture.setImageBitmap(value.getLocalfoto());
            } else if(value.getFoto() != null) {
                // TODO PILLAR DE FIREBASE

            }
            holder.name.setText(value.getName());
        }

        @Override
        public int getItemCount() {
            return mItems.size();
        }

        public class ItemViewHolder extends RecyclerView.ViewHolder {
            public ImageView picture;
            public TextView name;

            public ItemViewHolder(@NonNull View itemView) {
                super(itemView);
                picture = itemView.findViewById(R.id.imageView2);
                name = itemView.findViewById(R.id.textView2);
            }
        }
    }
}