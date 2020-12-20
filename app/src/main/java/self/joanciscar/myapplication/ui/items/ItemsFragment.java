package self.joanciscar.myapplication.ui.items;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.List;

import self.joanciscar.myapplication.R;
import self.joanciscar.myapplication.ui.CameraActivity;
import self.joanciscar.myapplication.utilities.FirebaseUtils;
import self.joanciscar.myapplication.utilities.Utils;

public class ItemsFragment extends Fragment {
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private List<Item> mItems;
    private ItemsViewModel itemsViewModel;
    private ItemAdapter mItemsAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        itemsViewModel = new ViewModelProvider(this).get(ItemsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_items, container, false);
        RecyclerView recyclerView = root.findViewById(R.id.itemsRecycler);
        itemsViewModel.getLiveData().observe(getViewLifecycleOwner(), new Observer<List<Item>>() {
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
                            return mItems.get(oldItemPosition).getId().equals(updatedItems.get(newItemPosition).getId());
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
        FloatingActionButton add_fab = root.findViewById(R.id.add_item);
        add_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("Inserte los datos para crear un nuevo item:");
                builder.setView(getActivity().getLayoutInflater().inflate(R.layout.activity_item_formulary,new LinearLayout(getActivity()), false));
                builder.setPositiveButton("Crear", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        AlertDialog di = (AlertDialog) dialog;
                        EditText text = (EditText) di.findViewById(R.id.name);
                        Item updated = new Item();
                        updated.setName(text.getText().toString());
                        itemsViewModel.add(updated);
                    }
                });
                builder.create().show();
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
            byte[] localfoto = value.getLocalfoto();
            String foto = value.getFoto();
            if(foto != null) {
                boolean isSync = PreferenceManager.getDefaultSharedPreferences(ItemsFragment.this.getContext()).getBoolean("sync_pictures",false);
                boolean isAuth = PreferenceManager.getDefaultSharedPreferences(ItemsFragment.this.getContext()).getBoolean("authenticated",false);
                if(isAuth && isSync) {
                    Picasso.get().load(value.getFotoAsUri()).fit().into(holder.picture);
                }
            } else if (localfoto != null) {
                holder.picture.setImageBitmap(BitmapFactory.decodeByteArray(localfoto,0,localfoto.length));
            } else {
                holder.picture.setImageResource(R.drawable.ic_launcher_foreground);
            }
            holder.name.setText(value.getName());
            holder.name.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage("Editando item:");
                    builder.setView(getActivity().getLayoutInflater().inflate(R.layout.activity_item_formulary,new LinearLayout(getActivity()), false));
                    builder.setPositiveButton("Editar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            AlertDialog di = (AlertDialog) dialog;
                            EditText text = (EditText) di.findViewById(R.id.name);
                            Item old = mItems.get(position);
                            Item updated = new Item();
                            updated.setId(old.getId());
                            updated.setLocalfoto(old.getLocalfoto());
                            updated.setFoto(old.getFoto());
                            updated.setName(text.getText().toString());
                            itemsViewModel.update(old,updated);
                        }
                    });
                    builder.setNegativeButton("Cancelar", null);
                    builder.create().show();
                    return false;
                }
            });
            holder.picture.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder adb = new AlertDialog.Builder(ItemsFragment.this.getContext());
                    adb.setMessage("Quieres cambiar la foto del objeto?");
                    adb.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent takePictureIntent = new Intent();
                            takePictureIntent.setClass(ItemsFragment.this.getContext(),CameraActivity.class);
                            takePictureIntent.putExtra("array_positioion",position);
                            //takePictureIntent.putExtra("item_array_index",position);
                            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                        }
                    });
                    adb.setNegativeButton("Cancelar",null);
                    adb.create().show();
                }
            });
        }

        @Override
        public int getItemCount() {
            return mItems.size();
        }

        public class ItemViewHolder extends RecyclerView.ViewHolder {
            public ImageButton picture;
            public TextView name;

            public ItemViewHolder(@NonNull View itemView) {
                super(itemView);
                picture = itemView.findViewById(R.id.imageButton);
                name = itemView.findViewById(R.id.textView2);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_IMAGE_CAPTURE) {
            if (resultCode == Activity.RESULT_OK) {
                int position = data.getIntExtra("array_positioion",-1);
                Bitmap picture = data.getParcelableExtra("data");
                if(position > -1) {
                    Item old = mItems.get(position);
                    Item updated = new Item();
                    SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this.getContext());
                    /*if(sp.getBoolean("authenticated",false) && sp.getBoolean("sync_pictures",false)) {
                        StorageReference itemRef = FirebaseStorage.getInstance().getReference(FirebaseAuth.getInstance().getUid() +"/item/"+old.getId());
                        FirebaseUtils.putBytes(itemRef,Utils.bitmapToBytes(picture));
                        old.setFoto(FirebaseUtils.getEagerlyUri(itemRef));
                    }*/
                    updated.setId(old.getId());
                    updated.setLocalfoto(picture);
                    updated.setFoto(old.getFoto());
                    updated.setName(old.getName());

                    itemsViewModel.update(old,updated);
                }
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}