package wolper.logic;


import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

public class BoardOfShips {
    public volatile Ships[][] shipLines = new Ships[10][10];


    public BoardOfShips(){
        for (int i=0; i<10; i++)
            for (int j=0; j<10; j++) {
                shipLines[i][j]=new Ships();
        }
    }

    public Ships[][] getshipLines() {
        return shipLines;
    }

    public void setshipLines(Ships[][] shipLines) {
        if (shipLines!=null) this.shipLines = shipLines;
    }
}


class Ships {
    public int x=0;
    public int y=0;
    public int staT=3;
    public int pos=0;
    public int siZe=0;
    public int commonGranz=0;
    public int id=0;
}

