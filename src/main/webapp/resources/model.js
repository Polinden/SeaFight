

//КОНСТАНТЫ
var statuS = {
    KILLED: 0,
    ALIFE :1,
    NEIGBOUR:2,
    FREE:3,
    INJURED:4
};

var positioN = {
    VERT:0,
    HOR:1
};


//МОДЕЛЬ СОБСТВЕННОЙ ИГРОВОЙ ТАБЛИЦЫ
var Model;
var idStart=0;



//Объект - адаптер для описания ячейки 
var cellObject = {
    x: 1,
    y: 1,
    toId: function () {
        if (this.x == 10) xn = 0; else xn = this.x;
        if (this.y == 10) yn = 0; else yn = this.y;
        return xn + "and" + yn;
    },
    toIdPartner: function () {
        if (this.x == 10) xn = 0; else xn = this.x;
        if (this.y == 10) yn = 0; else yn = this.y;
        return xn + "pls" + yn;
    },
    fromId: function (id) {
        this.x = id.substring(0, 1);
        this.y = id.substring(4, 5);
        if (this.x == 0) this.x = 10;
        if (this.y == 0) this.y = 10;
    }
};



//Инициируем модель таблицы кораблей
function initModel() {
    Model = new modelShipBoard();

    //Модель таблицы
    function modelShipBoard() {
        this.ships = [
            [new modelShip(1, 1), new modelShip(1, 2), new modelShip(1, 3), new modelShip(1, 4), new modelShip(1, 5), new modelShip(1, 6), new modelShip(1, 7), new modelShip(1, 8), new modelShip(1, 9), new modelShip(1, 10)],
            [new modelShip(2, 1), new modelShip(2, 2), new modelShip(2, 3), new modelShip(2, 4), new modelShip(2, 5), new modelShip(2, 6), new modelShip(2, 7), new modelShip(2, 8), new modelShip(2, 9), new modelShip(2, 10)],
            [new modelShip(3, 1), new modelShip(3, 2), new modelShip(3, 3), new modelShip(3, 4), new modelShip(3, 5), new modelShip(3, 6), new modelShip(3, 7), new modelShip(3, 8), new modelShip(3, 9), new modelShip(3, 10)],
            [new modelShip(4, 1), new modelShip(4, 2), new modelShip(4, 3), new modelShip(4, 4), new modelShip(4, 5), new modelShip(4, 6), new modelShip(4, 7), new modelShip(4, 8), new modelShip(4, 9), new modelShip(4, 10)],
            [new modelShip(5, 1), new modelShip(5, 2), new modelShip(5, 3), new modelShip(5, 4), new modelShip(5, 5), new modelShip(5, 6), new modelShip(5, 7), new modelShip(5, 8), new modelShip(5, 9), new modelShip(5, 10)],
            [new modelShip(6, 1), new modelShip(6, 2), new modelShip(6, 3), new modelShip(6, 4), new modelShip(6, 5), new modelShip(6, 6), new modelShip(6, 7), new modelShip(6, 8), new modelShip(6, 9), new modelShip(6, 10)],
            [new modelShip(7, 1), new modelShip(7, 2), new modelShip(7, 3), new modelShip(7, 4), new modelShip(7, 5), new modelShip(7, 6), new modelShip(7, 7), new modelShip(7, 8), new modelShip(7, 9), new modelShip(7, 10)],
            [new modelShip(8, 1), new modelShip(8, 2), new modelShip(8, 3), new modelShip(8, 4), new modelShip(8, 5), new modelShip(8, 6), new modelShip(8, 7), new modelShip(8, 8), new modelShip(8, 9), new modelShip(8, 10)],
            [new modelShip(9, 1), new modelShip(9, 2), new modelShip(9, 3), new modelShip(9, 4), new modelShip(9, 5), new modelShip(9, 6), new modelShip(9, 7), new modelShip(9, 8), new modelShip(9, 9), new modelShip(9, 10)],
            [new modelShip(10, 1), new modelShip(10, 2), new modelShip(10, 3), new modelShip(10, 4), new modelShip(10, 5), new modelShip(10, 6), new modelShip(10, 7), new modelShip(10, 8), new modelShip(10, 9), new modelShip(10, 10)]
        ];

        var self=this;

        this.poseShip = function (startY, startX, position, sizE, myId) {
                if (position == positioN.VERT) {
                    setNeigbours(startX, startY, positioN.VERT, sizE);
                    for (i = 0; i < sizE; i++) {
                        self.ships[startX - 1][startY - 1 + i].pos = positioN.VERT;
                        self.ships[startX - 1][startY - 1 + i].staT = statuS.ALIFE;
                        self.ships[startX - 1][startY - 1 + i].siZe = sizE;
                        self.ships[startX - 1][startY - 1 + i].commonGranz=0;
                        if (myId==0) myId+=++idStart;
                        self.ships[startX - 1][startY - 1 + i].id=myId;

                         $('#debug2').text(" from "+self.ships[startX - 1][startY - 1].x + " : "+self.ships[startX - 1][startY - 1].y
                              + " to "+self.ships[startX - 1][startY - 1+i].x + " : "+self.ships[startX - 1][startY - 1+i].y);
                    }
                }
                if (position == positioN.HOR) {
                    setNeigbours(startX, startY, positioN.HOR, sizE);
                    for (var i = 0; i < sizE; i++) {
                        self.ships[startX - 1 +i][startY - 1].pos=positioN.HOR;
                        self.ships[startX - 1 +i][startY - 1].staT=statuS.ALIFE;
                        self.ships[startX - 1 +i][startY - 1].siZe=sizE;
                        self.ships[startX - 1 +i][startY - 1].commonGranz=0;
                        if (myId==0) myId+=++idStart;
                        self.ships[startX - 1 +i][startY - 1].id=myId;
                    }
                }
        };


        this.validateShip = function (startY, startX, position, sizE, ids) {
            var resT=true;
            if (position == positioN.VERT)
                for (var i = 0; i < sizE; i++) {
                    if ((startY - 1 + i) > 9) return false;
                    if (self.ships[startX - 1][startY - 1 + i].staT== statuS.ALIFE) {
                        resT=false;
                        if ((ids!==0)&&(self.ships[startX - 1][startY - 1 + i].id==ids)) resT=true;
                    }

                    if (self.ships[startX - 1][startY - 1 + i].staT== statuS.NEIGBOUR) {
                        resT=false;
                        if ((ids!==0)&&(self.ships[startX - 1][startY - 1 + i].commonGranz==1)) {
                            if (lookaround((startX - 1),(startY - 1 + i), ids)) resT=true;
                        }
                    }
                    if (!resT) return resT;
                }
            if (position == positioN.HOR)
                for (var i = 0; i < sizE; i++) {
                    if ((startX - 1 + i) > 9) return false;
                    if (self.ships[startX - 1+i][startY - 1].staT== statuS.ALIFE) {
                        resT=false;
                        if ((ids!==0)&&(self.ships[startX - 1+i][startY - 1].id==ids)) {
                            resT = true;
                        }
                    }

                    if (self.ships[startX - 1+i][startY - 1].staT== statuS.NEIGBOUR) {
                        resT=false;
                        if ((ids!==0)&&(self.ships[startX - 1+i][startY - 1].commonGranz==1)) {
                            if (lookaround((startX - 1+i),(startY - 1), ids)) {
                                resT=true;
                            }
                        }
                    }
                    if (!resT) return resT;
                }
            return resT;
        };



        function lookaround(x, y, id) {
            for (i=-1; i<2; i++)
                for(j=-1; j<2; j++) {
                    var xx=x+i;
                    var yy=y+j;
                    if ((xx>-1)&&(xx<10))
                        if ((yy>-1)&&(yy<10))
                            if ((self.ships[xx][yy].id!==0) &&
                                (self.ships[xx][yy].id!==id)) {
                                return false;
                            }
                }
            return true;
        }



        function setNeigbours(startX, startY, poSS, sizEE) {
            var startXX;
            var startYY;
            sizEE++;
            if (poSS==positioN.HOR) {
                for(var i=-1; i<sizEE; i++) {
                    startXX=i+(startX-1);
                    if ((startXX>-1)&&(startXX<10)) {
                        for (var j=-1; j<2; j++) {
                            startYY=j+(startY-1);
                            if ((startYY>-1)&&(startYY<10)) {
                                self.ships[startXX][startYY].commonGranz++;
                                self.ships[startXX][startYY].id=0;
                                if (j!==0) self.ships[startXX][startYY].staT=statuS.NEIGBOUR;
                                if ((i==-1)||(i==sizEE-1)) self.ships[startXX][startYY].staT=statuS.NEIGBOUR;
                            }
                        }
                    }
                }
            }
            if (poSS==positioN.VERT) {
                for(var i=-1; i<sizEE; i++) {
                    startYY=i+(startY-1);
                    if ((startYY>-1)&&(startYY<10)) {
                        for (var j=-1; j<2; j++) {
                            startXX=j+(startX-1);
                            if ((startXX>-1)&&(startXX<10)) {
                                self.ships[startXX][startYY].commonGranz++;
                                self.ships[startXX][startYY].id=0;
                                if (j!==0) self.ships[startXX][startYY].staT=statuS.NEIGBOUR;
                                if ((i==-1)||(i==sizEE-1)) self.ships[startXX][startYY].staT=statuS.NEIGBOUR;

                            }
                        }
                    }
                }
            }

        }



        function delNeigbours(startX, startY, poSS, sizEE) {
            var startXX;
            var startYY;
            sizEE++;
            if (poSS==positioN.HOR) {
                for(var i=-1; i<sizEE; i++) {
                    startXX=i+(startX-1);
                    if ((startXX>-1)&&(startXX<10)) {
                        for (var j=-1; j<2; j++) {
                            startYY=j+(startY-1);
                            if ((startYY>-1)&&(startYY<10)) {
                                if (self.ships[startXX][startYY].commonGranz>0) self.ships[startXX][startYY].commonGranz--;
                                if (self.ships[startXX][startYY].commonGranz==0) {
                                    if (j !== 0) self.ships[startXX][startYY].staT = statuS.FREE;
                                    if ((i == -1) || (i == sizEE - 1)) self.ships[startXX][startYY].staT = statuS.FREE;
                                }
                            }
                        }
                    }
                }
            }
            if (poSS==positioN.VERT) {
                for(var i=-1; i<sizEE; i++) {
                    startYY=i+(startY-1);
                    if ((startYY>-1)&&(startYY<10)) {
                        for (var j=-1; j<2; j++) {
                            startXX=j+(startX-1);
                            if ((startXX>-1)&&(startXX<10)) {
                                if (self.ships[startXX][startYY].commonGranz>0) self.ships[startXX][startYY].commonGranz--;
                                if (self.ships[startXX][startYY].commonGranz==0) {
                                    if (j !== 0) self.ships[startXX][startYY].staT = statuS.FREE;
                                    if ((i == -1) || (i == sizEE - 1)) self.ships[startXX][startYY].staT = statuS.FREE;
                                }
                            }
                        }
                    }
                }
            }

        }


        //Обновление экреного представления нашей модели
        this.updateViev = function () {
            var i, j;
            var id;
            for (i = 0; i < 10; i++) {
                for (j = 0; j < 10; j++) {
                    cellObject.x = j+1;
                    cellObject.y = i+1;
                    id = cellObject.toId();
                    $('#'+id).removeClass("alife");
                    $('#'+id).removeClass("killed");
                    $('#'+id).removeClass("neighbour");
                    $('#'+id).removeClass("free");
                    if (self.ships[i][j].staT == statuS.ALIFE) $('#'+id).addClass("alife");
                    if (self.ships[i][j].staT == statuS.KILLED)  $('#'+id).addClass("killed");
                    if (self.ships[i][j].staT == statuS.NEIGBOUR)  $('#'+id).addClass("neighbour");
                    if (self.ships[i][j].staT == statuS.FREE)  $('#'+id).addClass("free");
                    if (self.ships[i][j].staT == statuS.INJURED)  $('#'+id).addClass("injured");
                }
            }
        };



        //Некоторые гетеры для упрощения работы
        this.getWidth = function (startY, startX) {
            return self.ships[startX - 1][startY - 1].siZe;
        };

        this.getId = function (startY, startX) {
            return self.ships[startX - 1][startY - 1].id;
        };

        this.getPos = function (startY, startX) {
            return self.ships[startX - 1][startY - 1].pos;
        };


        //Удаление корабля
        this.deleteShip = function (startY, startX, lenGH) {
            var poS=self.ships[startX - 1][startY - 1].pos;
            var shipID=self.ships[startX - 1][startY - 1].shipID;
            if (poS == positioN.VERT) {
                for (i = 0; i < lenGH; i++) {
                    self.ships[startX - 1][startY - 1+i].staT=statuS.FREE;
                    self.ships[startX - 1][startY - 1+i].siZe=4;
                    self.ships[startX - 1][startY - 1+i].id=0;
                }
                delNeigbours(startX, startY, positioN.VERT, lenGH, shipID);
            }
            if (poS == positioN.HOR) {
                for (var i = 0; i < lenGH; i++) {
                    self.ships[startX - 1 +i][startY - 1].staT=statuS.FREE;
                    self.ships[startX - 1 +i][startY - 1].siZe=4;
                    self.ships[startX - 1 +i][startY - 1].id=0;
                }
                delNeigbours(startX, startY, positioN.HOR, lenGH, shipID);
            }
        };
        
    }



    //Объект клеточки в таблице модели
    function modelShip(y, x) {
        this.x = x;
        this.y = y;
        this.staT = statuS.FREE;
        this.pos = positioN.HOR;
        this.siZe = 0;
        this.commonGranz=0;
        this.id=0;
    }
}







