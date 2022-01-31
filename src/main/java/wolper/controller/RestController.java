package wolper.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import wolper.logic.*;
import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.Collection;
import java.util.List;



//Рест контроллеры для передачи игровой инфорамации

@Controller
@RequestMapping(value = "/rest")
public class RestController {

        @Autowired
        AllGames allGames;
        @Autowired
        ShipService shipService;
        @Autowired
        CrossGamerInfoBuss crossGamerInfoBuss;

        SimpMessageSendingOperations messaging;

        @Autowired
        public void setMessaging(SimpMessageSendingOperations messaging) {
            this.messaging = messaging;
        }


        //Контроллер сохранения расстановки кораблей
        @RequestMapping(value = "/{nameGamer}/modelBoards", method = RequestMethod.POST, produces = "application/json")
        @ResponseBody
        public BoardOfShips saveGaimerChios(@PathVariable String nameGamer, @RequestBody BoardOfShips boOfS) {
            shipService.detectSips(nameGamer, boOfS);
            crossGamerInfoBuss.informPartnerIhaveSetUp(nameGamer);
            return boOfS;
        }


        //Тестовый контроллер сервис предоставляет отладочную информвцию (и заодно можно подглядкть корабли соперника)
        @RequestMapping(value = "/test/{name}", method = RequestMethod.GET, produces = "application/json")
        @ResponseBody
        public List getGaimerChios(HttpServletRequest request, @PathVariable String name) {
            return allGames.getShipListByName(name).smallSipList;
        }


        //Контроллер выдачи информации об участниках поединка
        @RequestMapping(value = "/gamerInfo", method = RequestMethod.GET, produces = "application/json")
        @ResponseBody
        public GamerSet[] getGamerInfo(HttpServletRequest request) {
            Collection<GamerSet> collectionGS=allGames.getAllGamers();
            return collectionGS.toArray(new GamerSet[collectionGS.size()]);
        }


        //Контроллер для приема акцепта приглашения поиграть
        @RequestMapping(value = "/invitationAccepted/{acceptedBy}", method = RequestMethod.GET, produces = "application/json")
        @ResponseBody
        public String[] getAccepted(@PathVariable String acceptedBy) {
            String [] names = acceptedBy.split("&");
            if (names[0].equals("accepted")) {
                crossGamerInfoBuss.acceptInvitation(names[1], names[2]);
            }
            if (names[0].equals("rejected")) {
                crossGamerInfoBuss.rejectInvitation(names[1], names[2]);
            }
            return new String[] {"inv","OK"};
        }


        //Контроллер для приема ходов соперников
        @RequestMapping(value = "/doMove/{attacker}/{suffer}", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
        @ResponseBody
        public String [] getAccepted(@PathVariable(value = "attacker") String attacker, @PathVariable(value = "suffer") String suffer, @RequestBody StepsMe step, Principal principal) {
            String name = principal.getName();
            //Безопасность. Проверяем, не фальсифицирован ли ход
            if (!name.equals(attacker)) return new String[] {"you are a cheater", "error"};
            String hit=crossGamerInfoBuss.doNextMove(attacker, suffer, step.x, step.y);
            return new String[] {hit,"OK"};
        }
}
