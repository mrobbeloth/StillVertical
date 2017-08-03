package robbeloth.consulting.services.stillvertical;

import java.util.Date;

/**
 * Created by microbbeloth on 8/3/2017.
 */

public class Notification {
    private Date entry;

    public Notification (Date entry){
        this.entry = entry;
    }

    public Date getEntry() {
        return entry;
    }

    public void setEntry(Date entry) {
        this.entry = entry;
    }

}
