package gui;

/**
 * Created with IntelliJ IDEA.
 * User: danya_000
 * Date: 9/17/13
 * Time: 12:48 PM
 * To change this template use File | Settings | File Templates.
 */
public class SimpleTime {
    String time;

    public SimpleTime(String t) {
        try {
            isValidTimeString(t);
        } catch (SimpleTimeFormatException e) {
            e.printStackTrace();
        }

        time = t;
    }

    @Override
    public boolean equals(Object obj) {
        SimpleTime st = (SimpleTime) obj;
        return time.equals(st.getTime());
    }

    public int compare(SimpleTime st) {
        if (this.equals(st)) return 0;

        int hThis = Integer.parseInt(time.split(":")[0]);
        int hSt = Integer.parseInt(st.getTime().split(":")[0]);

        int mThis = Integer.parseInt(time.split(":")[1]);
        int mSt = Integer.parseInt(st.getTime().split(":")[1]);

        if (hThis == hSt)
            if (mThis == mSt) return 0;
            else if (mThis < mSt) return -1;
            else return 1;
        else if(hThis < hSt) return -1;
        else return 1;
    }
    
    @Override
    public String toString() {
        return time;
    }
    
    public String getTime() {
        return time;
    }

    private boolean isValidTimeString(String time) throws SimpleTimeFormatException {
        String[] timeElements = time.split(":");

        if (timeElements.length != 2)
            throw new SimpleTimeFormatException();

        int hours, mins;

        try {
            hours = Integer.parseInt(timeElements[0]);
        } catch (NumberFormatException e) {
            throw new SimpleTimeFormatException();
        }

        try {
            mins = Integer.parseInt(timeElements[1]);
        } catch (NumberFormatException e) {
            throw new SimpleTimeFormatException();
        }

        if (hours < 0 || hours > 23 || mins < 0 || mins > 59)
            throw new SimpleTimeFormatException();

        return true;
    }

    public static void main(String[] args) {
        SimpleTime s1 = new SimpleTime("25:59");
        SimpleTime s2 = new SimpleTime("00:00");
        System.out.print(s1.compare(s1));
    }
}