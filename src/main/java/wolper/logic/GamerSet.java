package wolper.logic;


import java.util.concurrent.atomic.AtomicInteger;

public class GamerSet {


    //ГеймерСет - это mutable object, требубщий стредст обеспечения threadSafe


    //Статус (7мь переменых ниже) читаеться многими, но записываеться отдним потоком, возможны проблемы с видимостью
    //Не лучший стиль оставлять их видимыми и не инкапсулированными, но это не вредит коду в данном конкретном случае, так как
    //volatile решает проблемы с видимостью, а вместе с эксклюзивной возможностю записи одним лишь потоком избавляет от
    //проблем race conditions. Поэтому полная инкапсуляция или синхронизация доступа не требуються
    volatile public String playWith="nobody";
    volatile public boolean free=true;
    volatile public String invitedBy="nobody";
    volatile public String name="nobody";
    volatile private int killed = 0;
    volatile private int rating;


    public void setRating(int rating) {
        this.rating=rating;
    }

    public int getRating() {
        return rating;
    }

    public void incRating() {
        rating++;
    }

    public void decRating() {
        if (rating>0) rating--;
    }

    public void addKilled() {killed++;}

    boolean enoughfKilled() {
        return killed>19;
    }


    public void resetMe() {
        this.playWith="nobody";
        this.free=true;
        this.invitedBy="nobody";
        this.killed=0;
    }
}


