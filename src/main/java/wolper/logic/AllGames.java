package wolper.logic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import javax.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;



//Класс объявлен как одиночка, так как все что он делает - это манипуляции с единственной на всю программу
//базой "ин мемори" данных об игроках и о ходе игры, хранящейся в конкурентных коллекциях

@Service("allGames")
@Scope(value = "singleton")
public class AllGames {

    //Это основная игровая информация, доступная для всех игроков и, следовательно,  многих потоков
    private  static  final ConcurrentMap<String, GamerSet> listOfGamer= new ConcurrentHashMap<String, GamerSet>();
    private  static  final ConcurrentMap<String, ShipList> listOfShips= new ConcurrentHashMap<String, ShipList>();
    public String name;



    @Autowired
    GamerDAO gamerDAO;


    SimpMessageSendingOperations messaging;

    @Autowired
    public void setMessaging(SimpMessageSendingOperations messaging) {
        this.messaging = messaging;
    }



    public void removeByName(String name){
        informPartnerOnGoOut(name);
        deleteGamerByName(name);
    }


    public GamerSet getGamerByName(String name){
        return listOfGamer.get(name);
    }



    @Async
    public void createGamerByName(String name){
        //ГеймерСет - это mutable object, требубщий всех стредст обеспечения threadSafe
        final GamerSet gamerSet = new GamerSet();
        gamerSet.name=name;
        gamerSet.setRating(gamerDAO.getRatingOnStartUp(name));
        listOfGamer.put(name, gamerSet);
        //Даем время вновьприбывшему подключиться к Вебсокету
        try {TimeUnit.SECONDS.sleep(3);}
        catch (InterruptedException  ie) {}
        finally { messaging.convertAndSend("/topic/renewList", "newCreated");}
    }



    public void deleteGamerByName(String name){
	final GamerSet deleted = listOfGamer.remove(name);
	//не допускаем уделения игрока дважде, что может произойти, если игрок пытался открыть несколько сессий (система выбивает такого дублера)
	if (deleted==null) return; 
	gamerDAO.setRatingOnExit(name, deleted.getRating());
        listOfShips.remove(name);
        messaging.convertAndSend("/topic/renewList", "reMoved");
    }


    public Collection<GamerSet> getAllGamers() {
        return listOfGamer.values();
    }



    public void informPartnerOnGoOut(String name) {
        Set<String> listNames = listOfGamer.keySet();
        for (String key : listNames) {
            GamerSet gamerSet =  listOfGamer.get(key);
            if (gamerSet.playWith.equals(name)) {
                gamerSet.resetMe();
                messaging.convertAndSend("/topic/"+key, "esceped&Выш соперник неожиданнo вышел из игры!");
            }

        }
    }


    public ShipList getShipListByName(String name) {
        return listOfShips.get(name);
    }



    public void setShipListByName(String name, ShipList shipList) {
        listOfShips.put(name, shipList);

    }



    public void addScore(String attacker, String suffer){
        GamerSet gamerSetattacker =listOfGamer.get(attacker);
        gamerSetattacker.incRating();
        GamerSet gamerSetsuffer =listOfGamer.get(suffer);
        gamerSetsuffer.decRating();
    }

}







