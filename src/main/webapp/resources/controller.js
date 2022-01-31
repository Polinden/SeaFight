
//УПРАВЛЕНИЕ СОБСТВЕННЫМИ СУДАМИ


//Размеры таблицы-поля
function getMainTableOffset() {
    return $(".todrag").offset();
}
function getMainTableWidth() {
    return $(".todrag").width();
}
function getMainTableHeight() {
    return $(".todrag").height();
}

//Данные для интерактивной таблицы на материнской странице
var start_date;
var mamaTable;
var tableFirst=true;
var STOMPsubscription;
var partnerSetUp=false;
var yourStep=false;
var yourPartner="";
var invited=false;
var iFdragged=false;
var savedPos;
var iPlay=false;

//Обработчик драг энд дроп для расстановки собственных судов
function forDrag() {
    var finalOffset;
    var finalxPos;
    var finalyPos;
    var flowxPos;
    var flowyPos;
    var initOffset;

    $(".draggable").draggable({
        start: function (event, ui) {
            initOffset = $(this).offset();
            flowxPos = initOffset.left;
            flowyPos = initOffset.top;
        },
        stop: function (event, ui) {
            finalOffset = $(this).offset();
            finalxPos = finalOffset.left;
            finalyPos = finalOffset.top;
            //Проверяем попали ли в поле игры, перемещая кораблик
            if (checkNotInsideMainTable(finalOffset, $(this).width(), $(this).height())) {
                //Найден ли по положению элемент куда будем помещать кораблик
                var cell=getElsAt(finalyPos, finalxPos);
                if (cell.length!==0) {
                    cellObject.fromId(cell.attr("id"));
                    //Устанавливаем размер корабля
                    var sl=4;
                    if ( $(this).hasClass( "ship1" ) )  sl=1;
                    if ( $(this).hasClass( "ship2" ) )  sl=2;
                    if ( $(this).hasClass( "ship3" ) )  sl=3;
                    //Проверяем не укладываем ли на уже размещенный кoрабль
                    var resulT=Model.validateShip(cellObject.x, cellObject.y, positioN.HOR, sl, 0);
                    if (resulT) {
                        Model.poseShip(cellObject.x, cellObject.y, positioN.HOR, sl, 0);
                        //Удаляем верхний спейсер для нормального смещения корабликов вверх
                        if (($(this).attr("id")!=="ship11")&&($(this).attr("id")!=="ship13")) {$(this).prev().remove();}
                        else {$(this).next().remove();}
                        //Удаляем установленный кoраблик из панели выбора и обновляем вьюв
                        $(this).remove();
                        Model.updateViev();
                        //Глюк в сафари
                        if (is_safari) $('tr').hide().show(0);
                        insertDiv(finalxPos, finalyPos, sl, positioN.HOR);
                        //Если разместили все корабли то отображаем кнопку "Вперед"
                        goFuther++;
                        if (goFuther>10) $('#gofuther').css("visibility", "visible")
                    }
                    else $(this).offset(initOffset);
                }
            } else {$(this).offset(initOffset);}
        }
    });
}



//Обработчик драг энд дроп для переноса расставленных судов
function forDragDiv() {
    var finalOffset;
    var finalxPos;
    var finalyPos;
    var flowxPos;
    var flowyPos;
    var initOffset;
    var cellOld;
    var cellNew;
    var lenGH;
    var oldX;
    var oldY;
    $(".draggable1").draggable({
        start: function (event, ui) {
            iFdragged=true;
            initOffset = $(this).offset();
            flowxPos = initOffset.left;
            flowyPos = initOffset.top;
            $(this).css("border", "solid");
            $(this).css("border-color", "red");
            cellOld=getElsAt(flowyPos, flowxPos);
            if (cellOld.length==0) return;
            cellObject.fromId(cellOld.attr("id"));
            lenGH=Model.getWidth(cellObject.x, cellObject.y);
            oldX=cellObject.x;
            oldY=cellObject.y;
            savedPos=Model.getPos(oldX, oldY);
        },
        stop: function (event, ui) {
            finalOffset = $(this).offset();
            finalxPos = finalOffset.left;
            finalyPos = finalOffset.top;
            cellNew=getElsAt(finalyPos, finalxPos);
            //Найден ли по сетоположению элемент куда будем помещать кораблик
            if ((cellOld.length==0) || (cellNew.length==0)) {
                $(this).offset(initOffset);
                $(this).remove();
                insertDiv(flowxPos, flowyPos, lenGH, savedPos);
                return;
            }
            //Проверяем попали ли в поле игры, перемещая кораблик
            if (checkNotInsideMainTable(finalOffset, $(this).width(), $(this).height())) {
                $(this).css("border", "none");
                cellObject.fromId(cellNew.attr("id"));
                var savedId=Model.getId(oldX, oldY);
                //Проверяем не укладываем ли на уже размещенный карабль
                var resulT=Model.validateShip(cellObject.x, cellObject.y, savedPos, lenGH, savedId);
                if (resulT) {
                    Model.deleteShip(oldX, oldY, lenGH);
                    Model.poseShip(cellObject.x, cellObject.y, savedPos, lenGH, savedId);
                    Model.updateViev();
                    $(this).remove();
                    insertDiv(cellNew.offset().left, cellNew.offset().top, lenGH, savedPos);
                } else {
                    $(this).remove();
                    insertDiv(flowxPos, flowyPos, lenGH, savedPos);
                }
            } else {
                $(this).remove();
                insertDiv(flowxPos, flowyPos, lenGH, savedPos);
            }
        }
    });
}



function forDragNot(){
    $(".draggable").draggable({ disabled: true });
    $(".draggable1").draggable({ disabled: true });
    $(".frameFor").remove();
}



//Найдем попали ли в пределы таблицы-поля
function checkNotInsideMainTable(offset, w, h) {
    bigOffset=getMainTableOffset();
    if (offset.top<bigOffset.top) return false;
    if (offset.left<bigOffset.left) return false;
    if ((offset.top+h-forDivBoxDela)>(getMainTableHeight()+bigOffset.top)) return false;
    if ((offset.left+w-forDivBoxDela)>(getMainTableWidth()+bigOffset.left)) return false;
    return true;

}



//Найдем попали в центр какой ячейки +/- погрешность
function getElsAt(top, left){
    return $("body").find(".items")
        .filter(function() {
            centerYBox=$(this).offset().top+forDivBox/2;
            centerXBox=$(this).offset().left+forDivBox/2;
            centerYShip=top+forDivBox/2;
            centerXShip=left+forDivBox/2;
            deltaY=Math.abs(centerYBox-centerYShip);
            deltaX=Math.abs(centerXBox-centerXShip);
            return deltaX<forDivBoxDela
                && deltaY<forDivBoxDela;
        });
}



//Вставляет виртуальную рамку, чтобы показать как мы перемещаем расставленные суда
function insertDiv(myX, myY, widTH, myPos) {
    tagToadd=getElsAt(myY, myX);
    myY=tagToadd.offset().top;
    myX=tagToadd.offset().left;
    if (myPos==positioN.HOR) {
        var newdiv1 = "<div class='frameFor draggable1' style='top: "
            + myY + "px; left: "
            + myX + "px; " +
            "width: " + widTH * forDivBox * 1.1
            + "px; height: " +
            forDivBox * 1.1 + "px;'>" + "</div>";
    } else {
        var newdiv1 = "<div class='frameFor draggable1' style='top: "
            + myY + "px; left: "
            + myX + "px; " +
            "height: " + widTH * forDivBox * 1.1
            + "px; width: " +
            forDivBox * 1.1 + "px;'>" + "</div>";
    }
    $("body").append(newdiv1);
    forDragDiv();

    //Настраиваем обработчик событий для переворота кораблика
    $(".draggable1").on("mousedown", function () {iFdragged=false;}());
    $(".draggable1").on("mouseup", function (){
        onClickRotate($(this));});
}









//АЯКАСЫ И СОКЕТЫ ДЛЯ РАБОТЫ ПРОГРАММЫ

//СТОМП клиент для получения и отправки


//Отправлям сформированную модель на сервер
function sendJSONtoServer() {
    var toSend='{"shipLines":'+JSON.stringify(Model.ships)+'}';
    sendAJAXpost ("/rest/"+myName()+"/modelBoards", toSend, function (x){}, function (){goNextStep('next2');}, ajaxErrorMessage)
}



//СТОМП для мамы всех страниц


function initMamaSTOMP() {
    //Подписка на все темы
    initSTOMP.callback=function(frame) {
        var linkSource=initSTOMP.resultLink;
        STOMPsubscription= linkSource.subscribe("/topic/renewList", handleListofUsers, {'ack': 'client', 'durable': 'true'});
        linkSource.subscribe("/topic/invite", handleInviitations, {'ack': 'client', 'durable': 'true'});
        linkSource.subscribe("/topic/"+myName(), handleInfoExchange, {'ack': 'client', 'durable': 'true'});
        //И первая иницаилизация стартовой таблицы
        drowMamaTable();
        $("body").css('cursor','default');
        $("#clockWait").remove();
        };

    $("#clockWait").css('visibility', "visible");
    $("body").css('cursor','wait !important; z-index: 999; height: 100%; width: 100%;');



     initSTOMP('http://'+window.location.host + ':80/data', '/goout');   


    //Подписались на обмен сообщениями по добавлению и уходу игроков
    var handleListofUsers = function (incoming) {
        incoming.ack();
        if (iPlay) return;
        if (incoming.body=="newCreated" || incoming.body=="reMoved") {drowMamaTable()}
    };

    //Подписались на обмен сообщениями по приглашению
    var handleInviitations = function (incoming) {
        incoming.ack();
        if (strStartsWith(incoming.body,myName()+"&invitedNew")) {inviteShowDialog(incoming.body)}
        if (strStartsWith(incoming.body,myName()+"&invitedFail")) alertMy('Приглашение не состоялось (отвергнуто)!', function () {});
        if (strStartsWith(incoming.body,myName()+"&invitedDone")) {
            yourPartner=incoming.body.split("&")[2];
            alertMy("Приглашение принято игроком " + yourPartner, function () {});
            goNextStep("next1");}
    };
}




//Рисование таблицы игроков (с обновлением по добавлению новых)
function drowMamaTable() {

            var callback=function(NewrestultJS) {
                start_date=new Array();
                for (var i=0; i<NewrestultJS.length; i++)
                    start_date[i]=[NewrestultJS[i].name.toString(), NewrestultJS[i].playWith.toString(),
                        NewrestultJS[i].free.toString(), NewrestultJS[i].rating.toString()];
                setDataforTable();
            };

            sendAJAXget("/rest/gamerInfo", callback, function () {}, function () {});



            function setDataforTable() {
                if (tableFirst) {
                    tableFirst = false;

                    //Используем плаги ДатаТэблс для создания адаптивной таблицы
                    mamaTable = $("#mamaTable").DataTable({
                        "bAutoWidth": false,
                        "responsive": true,
                        "paging": false,
                        "info": false,
                        "searching": true,
                        "ordering": false,
                        "language": {
                            "search": "Найти:",
                            "zeroRecords": "Не найдено"
                        },
                        "columns": [
                            {"title": 'Кто', "contentPadding": "i", className: "dt-body-left"},
                            {"title": 'С кем', "contentPadding": "i", className: "dt-body-left"},
                            {"title": 'Играет', "contentPadding": "i", className: "dt-body-left"},
                            {"title": 'Рейтинг', "contentPadding": "i", className: "dt-body-left"}
                        ],
                        "columnDefs": [{
                            "render": function (data, type, row) {
                                return '<b style="color: navy">' + data + '</b>';
                            },
                            "targets": 0
                        },
                            {
                                "render": function (data, type, row) {
                                    if (data == "true") return '<b style="color: green">Нет</b>';
                                    return '<b style="color: red">Да</b>';
                                },
                                "targets": 2
                            },
                            {
                                "render": function (data, type, row) {
                                    if (data == "nobody") return '<b style="color: green">...</b>';
                                    return '<b style="color: red">' + data + '</b>';
                                },
                                "targets": 1
                            }]
                    });
                    $("#mamaTable").css("visibility", "visible");
                }
                    mamaTable.clear().rows.add(start_date).draw();

                //Устанавливаем обработчики нажатия ее строк (кроме заголовка)
                $("#mamaTable").find("tr").on('click', function() {
                    var nameHis=$(this).children(':first').text();
                    var free=$(this).children(':nth-child(3)').text();
                    //Сам себе не отправляем приглашение
                    if (nameHis==myName()) return;
                    if(nameHis=="Имя") return;
                    if (free!=="Нет") return;
                    //Отправляем приглашение
                    alertMy("Отправляем приглашение "+$(this).children(':first').text(), function () {
                        initSTOMP.resultLink.send("/app/infoExchange", {"persistent":"true"}, "invite&" + myName() + "&" + nameHis);
                    });
                });
            }
}





//Выдача диалога при поступлении сообщения о приглашении игрока
function inviteShowDialog(inviter) {
    var names=inviter.split("&");
    //Проверяем не приглашены ли уже
    if(!invited) {
        invited=true;
        
        $('#modalDialogInvite').find("#modalName").text("Приглашает игрок "+names[2]);
        $('#modalDialogInvite').find("#acceptInviteButton").on('click', function() {

            //Отправляем акцепт приглашения
            var toSend="accepted&"+ names[2]+"&"+myName();
            yourPartner=names[2];
            sendAJAXget("/rest/invitationAccepted/"+toSend, function (x) {}, function () {invited=false; goNextStep("next1");}, ajaxErrorMessage);

        });
        $('#modalDialogInvite').find("#rejectInviteButton").on('click', function() {

            //Отправляем режект приглашения
            var toSend="rejected&"+ names[2]+"&"+myName();
            sendAJAXget("/rest/invitationAccepted/"+toSend, function (x) {}, function () {invited=false;}, ajaxErrorMessage);
        });
        $('#modalDialogInvite').modal();
        
    } else {
        //Если уже кем то приглашены - отправляем режект приглашения (можно усложнить алгоритм... потом)
        var toSend="rejected&"+ names[2]+"&"+myName();
        sendAJAXget("/rest/invitationAccepted/"+toSend, function (x) {}, function () {invited=false;}, ajaxErrorMessage);
    }
}









// СОБЫТИЯ

//Переход по вебФлоу по событию
function goNextStep(step) {

    var newURL=$('#forFolwPlacer').text()+'&_eventId=';
    switch (step) {
        case "next0":
            newURL=newURL+step;
            window.location = newURL;
            break;

        case "next1":
            //Отписываемся от подписки и начинам игру
            STOMPsubscription.unsubscribe();
            iPlay=true;
            //Перерисовываем поле игры
            $('#modalDialogInvite').modal('hide');
            $('#mamaTable').empty();
            $('#mamaTable').remove();
            $('#next0').empty();
            $('#next0').remove();
            initModel();
            forDrag();
            $('#next1').css('visibility', 'visible');
            setPageHeader('Противники готовяться....');
            break;

        case "next2":
            //Перерисовываем поле игры
            $('#next1').empty();
            $('#next1').remove();
            forDragNot();
            Model.updateViev();
            initFightModel();
            $('#next2').css('visibility', 'visible');
            if (partnerSetUp) setPageHeader('Ходит '+yourPartner+'....');
            else {setPageHeader(yourPartner+' расставляет! Ждем....'); yourStep=true;}
            break;

        case "next3":
            newURL=newURL+step;
            window.location = newURL;
            break;
    }
    
}



//Обработка входящих сообщений о ходе игры
var handleInfoExchange = function (incoming) {
    incoming.ack();
    var context=incoming.body.split("&");
    switch (context[0]) {
        case "error":
            alertMy ("Ошибка на сервере! Перегрузитесь", function () {});
            break;
        case "esceped":
            alertMy (context[1], function () {goNextStep("next0");});
            break;
        case "setUp":
            if (yourStep) setPageHeader('Ходите Вы....');
            else setPageHeader(yourPartner+' расставил фигуры и ожидает...');
            partnerSetUp=true;
            break;
        case "hitYou":
            hitMe(context[1], context[2], context[3]);
            break;
    }
};






//Устанавливае заголовок (статус игры)
function setPageHeader(input) {
    $("#headerForMessage").text(input);
}


function yourMove() {
    return (partnerSetUp && yourStep);
}


function myName() {
    return $("#forNamePlacer").text();
}


function strStartsWith(str, prefix) {
    return str.indexOf(prefix) === 0;
}




