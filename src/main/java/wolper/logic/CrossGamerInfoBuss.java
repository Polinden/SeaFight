package wolper.logic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;


@Service(value = "gamerBuss")
@Scope(value = "prototype")
public class CrossGamerInfoBuss {

    @Autowired
    AllGames allGames;

    @Autowired
    ShipService shipService;

    SimpMessageSendingOperations messaging;

    @Autowired
    public void setMessaging(SimpMessageSendingOperations messaging) {
        this.messaging = messaging;
    }

    //Сватаемся
    public void inviteOneAnother(String inviter, String invitee) {
        final GamerSet gamerSetInvitee = allGames.getGamerByName(invitee);
        final GamerSet gamerSetInviter = allGames.getGamerByName(inviter);
        if ((gamerSetInvitee==null)||(gamerSetInvitee==null)) {
            messaging.convertAndSend("/topic/"+inviter, "error&Что то пошло не так!");
            return;
        }
        if (gamerSetInvitee.free==false) {
            //Приглашение не состоялось
            messaging.convertAndSend("/topic/invite", inviter+"&invitedFail&"+invitee);
            return;
        }
        if (gamerSetInviter.free==false) {
            //Приглашение не состоялось
            messaging.convertAndSend("/topic/invite", inviter+"&invitedFail&"+invitee);
            return;
        }
            messaging.convertAndSend("/topic/invite", invitee+"&invitedNew&"+inviter);
    }

    //Соглашаемся поиграть
    public void acceptInvitation(String inviter, String invitee) {
        final GamerSet gamerSetInvitee = allGames.getGamerByName(invitee);
        final GamerSet gamerSetInviter = allGames.getGamerByName(inviter);
        if ((gamerSetInvitee==null)||(gamerSetInvitee==null)) {
            messaging.convertAndSend("/topic/"+inviter, "error&Что то пошло не так!");
            return;
        }

        gamerSetInvitee.free=false;
        gamerSetInviter.free=false;
        gamerSetInvitee.playWith=inviter;
        gamerSetInviter.playWith=invitee;
        informWeFree();
        messaging.convertAndSend("/topic/invite", inviter+"&invitedDone&"+invitee);
    }

    //Отклоняем приглашение
    public void rejectInvitation(String inviter, String invitee) {
        final GamerSet gamerSetInvitee = allGames.getGamerByName(invitee);
        final GamerSet gamerSetInviter = allGames.getGamerByName(inviter);
        if ((gamerSetInvitee==null)||(gamerSetInvitee==null)) {
            messaging.convertAndSend("/topic/"+inviter, "error&Что то пошло не так!");
            return;
        }
        gamerSetInvitee.free=true;
        gamerSetInviter.free=true;
        messaging.convertAndSend("/topic/invite", inviter+"&invitedFail&"+invitee);
    }

    //Объявляем, что расставили корабли
    public void informPartnerIhaveSetUp(String name) {
        final GamerSet partner1 = allGames.getGamerByName(name);
        if (partner1==null) {
            messaging.convertAndSend("/topic/"+name, "error&Что то пошло не так!");
            return;
        }
        final GamerSet partner2 = allGames.getGamerByName(partner1.playWith);
        if (partner2==null) {
            messaging.convertAndSend("/topic/"+name, "esceped&Ваш соперник неожиданнo вышел из игры!");
            return;
        }
        messaging.convertAndSend("/topic/"+partner1.playWith, "setUp&Cоперник уже расставил фигуры!");
    }

    //Ход соперника - проверка попадания - выдача поражения или победы
    public String doNextMove(String attacker, String suffer, int x, int y) {
        if (checkMyRightToHit(attacker, suffer))
            switch (shipService.doHit(suffer, y - 1, x - 1)) {
                case 0:
                    messaging.convertAndSend("/topic/" + suffer, "hitYou&" + x + "&" + y + "&zero");
                    return "zero";
                case 1:
                    messaging.convertAndSend("/topic/" + suffer, "hitYou&" + x + "&" + y + "&injured");
                    return "injured";
                case 2:
                    if (shipService.checkKillAll(suffer)) {
                        allGames.addScore(attacker, suffer);
                        messaging.convertAndSend("/topic/" + suffer, "hitYou&" + x + "&" + y + "&defeated");
                        allGames.getGamerByName(attacker).resetMe();
                        allGames.getGamerByName(suffer).resetMe();
                        return "victory";
                    }
                    messaging.convertAndSend("/topic/" + suffer, "hitYou&" + x + "&" + y + "&killed");
                    return "killed";
            }
        return "";
    }

    //Сообщаем, что нас можно приглашать
    public void informWeFree(){
        messaging.convertAndSend("/topic/renewList", "newCreated");
    }

    //Безопасность - проверяем не подделан ли запрос
    public boolean checkMyRightToHit(String attacker, String suffer) {
        final GamerSet gamerSetAttacker = allGames.getGamerByName(attacker);
        final GamerSet gamerSetSuffer = allGames.getGamerByName(suffer);
        if ((gamerSetAttacker==null)||(gamerSetSuffer==null)) return false;
        return gamerSetAttacker.playWith.equals(gamerSetSuffer.name);
    }
}
