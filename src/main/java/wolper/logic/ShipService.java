package wolper.logic;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;




@Service("shipService")
@Scope(value = "prototype")
public class ShipService {



    @Autowired
    AllGames allGames;




    //Метод берет расставленные корабли (как их присылает фронтэнд) и сканирует в удобное для
    // бекэнеда предатвление - получаеться список кораблей с перечнем занимаемых ими клеточек для быстрой и
    //эффективной проверки
    public void detectSips(String name, BoardOfShips boardOfShips) {


        boolean [][] foundSips = new boolean[10][10];
        for (int i = 0; i < 10; i++)
            for (int j = 0; j < 10; j++)
                foundSips[i][j] = false;


        final ShipList shipList = new ShipList();

        Ships[][] shipses = boardOfShips.getshipLines();
        for (int j = 0; j < 10; j++)
            for (int i = 0; i < 10; i++) {
                if (!foundSips[i][j]) {
                    if (shipses[i][j].staT == 1) {
                        //FoundShip
                        final SmallSip smallSip = new SmallSip();
                        smallSip.setSize(shipses[i][j].siZe);
                        if (shipses[i][j].pos == 1) {
                            //Horizontal Ship
                            for (int m = 0; m < shipses[i][j].siZe; m++) {
                                smallSip.addCheck(i + m, j);
                                foundSips[i + m][j] = true;
                            }
                        } else {
                            //Vertical Ship
                            for (int m = 0; m < shipses[i][j].siZe; m++) {
                                smallSip.addCheck(i, j + m);
                                foundSips[i][m + j] = true;
                            }
                        }
                        //AdShipToList
                        shipList.smallSipList.add(smallSip);
                    }
                }
                //StoreShipListInAllGamesList
                allGames.setShipListByName(name, shipList);
            }
    }



    public byte doHit(String name, int i, int j) {
        ShipList shipList = allGames.getShipListByName(name);

        for (SmallSip smallSip : shipList.smallSipList) {
            if (smallSip.contains(i,j)) {
               if(smallSip.chechKillifNot()) {
                   allGames.getGamerByName(name).addKilled();
                   return 2;
               }
                allGames.getGamerByName(name).addKilled();
                return 1;
            }
        }
        return 0;

    }



    public boolean checkKillAll(String name){
        GamerSet gamerSet = allGames.getGamerByName(name);
        return (gamerSet.enoughfKilled());
    }

}




