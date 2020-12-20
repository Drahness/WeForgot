package self.joanciscar.myapplication.ui.reminders;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.switchmaterial.SwitchMaterial;

import java.util.List;

import self.joanciscar.myapplication.R;

public class RemindersFragment extends Fragment {
    private List<Reminder> mReminders;
    private ReminderAdapter mRemindersAdapter;

    public class ReminderAdapter extends RecyclerView.Adapter<ReminderAdapter.ReminderViewHolder>  {
        @NonNull
        @Override
        public ReminderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.weforgot_item_list_reminder,parent,false);
            return new ReminderAdapter.ReminderViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ReminderViewHolder holder, int position) {
            Reminder value = mReminders.get(position);
            holder.activated.setChecked(value.isActivated());
            if(value.getTime() == null) {
                holder.startTime.setText("null");
            } else {
                holder.startTime.setText(value.getTime().toString());
            }
            if(value.getTime() == null) {
                holder.startTime.setText("null");
            } else {
                holder.endTime.setText(value.getEndTime().toString());
            }
            holder.edit.setText("Edit");
        }

        @Override
        public int getItemCount() {
            return mReminders.size();
        }

        public class ReminderViewHolder extends RecyclerView.ViewHolder {
            SwitchMaterial activated;
            TextView startTime, endTime;
            Button edit; // ?

            public ReminderViewHolder(@NonNull View itemView) {
                super(itemView);
                activated = itemView.findViewById(R.id.switch1);
                startTime = itemView.findViewById(R.id.textView3);
                endTime = itemView.findViewById(R.id.textView4);
                edit = itemView.findViewById(R.id.button);
            }
        }
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        RemindersViewModel remindersViewModel = new ViewModelProvider(this).get(RemindersViewModel.class);
        View root = inflater.inflate(R.layout.fragment_alarms, container, false);
        RecyclerView recyclerView = root.findViewById(R.id.remindersRecycler);
        remindersViewModel.getLiveData().observe(getViewLifecycleOwner(), new Observer<List<Reminder>>() {
            @Override
            public void onChanged(List<Reminder> reminders) {
                if (mReminders == null) {
                    mReminders = reminders;
                    mRemindersAdapter = new ReminderAdapter();
                    recyclerView.setAdapter(mRemindersAdapter);
                } else {
                    DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
                        @Override
                        public int getOldListSize() {
                            return mReminders.size();
                        }

                        @Override
                        public int getNewListSize() {
                            return reminders.size();
                        }

                        @Override
                        public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                            return mReminders.get(oldItemPosition).getId() ==
                                    reminders.get(newItemPosition).getId();
                        }

                        @Override
                        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                            return mReminders.get(oldItemPosition).equals(reminders.get(newItemPosition));
                        }
                    });
                    result.dispatchUpdatesTo(mRemindersAdapter);
                    mReminders = reminders;
                }
            }
        });
        FloatingActionButton add_fab = root.findViewById(R.id.add_reminder);
        add_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        return root;
    }
}