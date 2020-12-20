package self.joanciscar.myapplication.ui.reminders;

import java.sql.Time;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import self.joanciscar.myapplication.data.Entity;
import self.joanciscar.myapplication.ui.items.Item;

public class Reminder extends Entity<Long> {
    private Long id;
    private int importance;
    private List<Item> items = new ArrayList<>();
    private Time endTime; // Only for the notification IMPORTANT. The others ignore these att
    private int graceTime; // After the check
    private Time time;
    private boolean activated;

    public Reminder() {}
    public Reminder(long id) {
        this.id = id;
        this.endTime = new Time(12313L);
        this.time = new Time(0L);
    }

    public Time getEndTime() {
        return endTime;
    }

    public Reminder(Long id, int importance, List<Item> items, Time endTime, int graceTime, Time time) {
        this.id = id;
        this.importance = importance;
        this.items = items;
        this.endTime = endTime;
        this.graceTime = graceTime;
        this.time = time;
    }

    public void setEndTime(Time endTime) {
        this.endTime = endTime;
    }

    public void setEndTime(String time) {
        if(time == null) {
            this.time = null;
        } else {
            this.setEndTime(Time.valueOf(time));
        }
    }

    public int getGraceTime() {
        return graceTime;
    }

    public void setGraceTime(int graceTime) {
        this.graceTime = graceTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
    }

    public void setTime(String time) {
        if(time == null) {
            this.time = null;
        } else {
            this.setTime(Time.valueOf(time));
        }
    }

    public int getImportance() {
        return importance;
    }

    public void setImportance(int importance) {
        this.importance = importance;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    @SuppressWarnings("EqualsReplaceableByObjectsCall")
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Reminder reminder = (Reminder) o;

        if (id != reminder.id) return false;
        if (importance != reminder.importance) return false;
        if (graceTime != reminder.graceTime) return false;
        if (items != null ? !items.equals(reminder.items) : reminder.items != null) return false;
        if (endTime != null ? !endTime.equals(reminder.endTime) : reminder.endTime != null)
            return false;
        return time.equals(reminder.time);
    }

    public boolean isActivated() {
        return activated;
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
    }

    public void setActivated(Long activated) {
        if(activated != null && activated == 1) {
            this.activated = true;
        } else if(activated == null || activated == 0){
            this.activated = false;
        } else {
            throw new RuntimeException("Unknown boolean from number " + activated);
        }
    }
    @Override
    public int hashCode() {
        int result = id != null ? id.intValue() : 0;
        result = 31 * result + importance;
        result = 31 * result + (items != null ? items.hashCode() : 0);
        result = 31 * result + (endTime != null ? endTime.hashCode() : 0);
        result = 31 * result + graceTime;
        result = 31 * result + time.hashCode();
        return result;
    }
    public Map<String,Object> getMap() {
        Map<String,Object> map = new HashMap<>();
        map.put("endtime",this.endTime);
        map.put("time",this.time);
        map.put("graceTime",this.graceTime);
        map.put("activated",this.activated);
        //TODO map.put("items",this.items);
        map.put("importance",importance);
        map.put("id",id);
        return map;
    }
    public void fromMap(Map<String,Object> map) {
        endTime = (Time) map.get("endtime");
        this.setTime((String) map.get("time"));
        graceTime = (int) map.get("graceTime");
        activated = (boolean) map.get("activated");
        // TODO items = (List<Item>) map.get("items");
        importance = (int) map.get("importance");
        id = (long) map.get("id");
    }
}
