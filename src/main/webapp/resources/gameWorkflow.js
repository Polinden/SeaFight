

//УПРАВЛЕНИЕ СУДАМИ СОПЕРНИКА

//Инициация игровой модели соперника
function initFightModel() {
    $('.tohit').on('click', function() {
        if ($(this).hasClass('hit')) return;
        if (!yourMove()) return;
        $(this).addClass("hit");
        sendMyNextStep($(this).attr("id"));
    });
}

function sendMyNextStep(cell){
        cellObject.fromId(cell);
        var urlTosend="/rest/doMove/"+myName()+"/"+yourPartner;
        var DatatoSent={x:cellObject.x, y:cellObject.y};
        var stepData=JSON.stringify(DatatoSent);
        yourStep=false;
        setPageHeader('.......');
        //Отправка на сервер хода!
        sendAJAXpost (urlTosend, stepData, function (x) {
            switch (x[0]) {
                case "zero" :
                    yourStep=false;
                    setPageHeader('Ходит '+yourPartner+'....');
                    break;
                case "injured" :
                    $('#'+cellObject.toIdPartner()).addClass('injured');
                    yourStep=true;
                    setPageHeader('Ходите Вы....');
                    break;
                case "killed" :
                    $('#'+cellObject.toIdPartner()).addClass('killed');
                    showKill(cellObject.x, cellObject.y);
                    yourStep=true;
                    setPageHeader('Ходите Вы....');
                    break;
                case "victory" :
                    $('#'+cellObject.toIdPartner()).addClass('killed');
                    showKill(cellObject.x, cellObject.y);
                    yourStep=false;
                    setPageHeader('Вы победили!');
                    alertMy("Поздравляем Вас с победой!", function () {goNextStep("next0");});
                    break;
                default :
                    yourStep=true;
                    setPageHeader('Ходите Вы.... (прошлый ход не принят из-за нарушения безопасности!)');
            }},
            function () {},
            function (jqXHR, textStatus, errorThrown) {
                yourStep = true;
                setPageHeader('Ходите Вы.... (повторите, произошел сбой при отправке на сервер!)');
                //alert("Ошибка отправки данных на сервер по причине "+errorThrown);
            }
        );
}




function hitMe(x, y, result) {
    //Меня атакавали
    cellObject.x=x;
    cellObject.y=y;
    switch (result) {
        case "zero" :
            $('#'+cellObject.toId()).addClass('zerohit');
            yourStep=true;
            setPageHeader('Ходите Вы....');
            break;
        case "injured" :
            $('#'+cellObject.toId()).addClass('injured');
            yourStep=false;
            setPageHeader('Ходит '+yourPartner+'....');
            break;
        case "killed" :
            $('#'+cellObject.toId()).addClass('killed');
            showKillMe(cellObject.x, cellObject.y);
            yourStep=false;
            setPageHeader('Ходит '+yourPartner+'....');
            break;
        case "defeated" :
            $('#'+cellObject.toId()).addClass('killed');
            yourStep=false;
            setPageHeader('Вы проиграли!');
            alertMy("Вы проиграли! Удачи в следующей игре...", function () {goNextStep("next0");});
            break;
    }
}



function showKill(x, y) {
    //СканнВверх
    var gotofinish=true;
    cellObject.x=x;
    cellObject.y=y;
    while (gotofinish){
        console.log(cellObject.toIdPartner());
        $('#'+cellObject.toIdPartner()).removeClass('injured');
        $('#'+cellObject.toIdPartner()).addClass('killed');
        if (++cellObject.x>10) gotofinish=false;
        var check=$('#'+cellObject.toIdPartner()).hasClass('injured');
        if (!check) gotofinish=false;
    }
    //СканнВниз
    gotofinish=true;
    cellObject.x=x;
    cellObject.y=y;
    while (gotofinish){
        console.log(cellObject.toIdPartner());
        $('#'+cellObject.toIdPartner()).removeClass('injured');
        $('#'+cellObject.toIdPartner()).addClass('killed');
        if (--cellObject.x<1) gotofinish=false;
        var check=$('#'+cellObject.toIdPartner()).hasClass('injured');
        if (!check) gotofinish=false;
    }
    //СканВлево
    gotofinish=true;
    cellObject.x=x;
    cellObject.y=y;
    while (gotofinish){
        console.log(cellObject.toIdPartner());
        $('#'+cellObject.toIdPartner()).removeClass('injured');
        $('#'+cellObject.toIdPartner()).addClass('killed');
        if (++cellObject.y>10) gotofinish=false;
        var check=$('#'+cellObject.toIdPartner()).hasClass('injured');
        if (!check) gotofinish=false;
    }
    //СканВправо
    gotofinish=true;
    cellObject.x=x;
    cellObject.y=y;
    while (gotofinish){
        console.log(cellObject.toIdPartner());
        $('#'+cellObject.toIdPartner()).removeClass('injured');
        $('#'+cellObject.toIdPartner()).addClass('killed');
        if (--cellObject.y<1) gotofinish=false;
        var check=$('#'+cellObject.toIdPartner()).hasClass('injured');
        if (!check) gotofinish=false;
    }
}




function showKillMe(x, y) {
    //СканнВверх
    var gotofinish=true;
    cellObject.x=x;
    cellObject.y=y;
    while (gotofinish){
        console.log(cellObject.toId());
        $('#'+cellObject.toId()).removeClass('injured');
        $('#'+cellObject.toId()).addClass('killed');
        if (++cellObject.x>10) gotofinish=false;
        var check=$('#'+cellObject.toId()).hasClass('injured');
        if (!check) gotofinish=false;
    }
    //СканнВниз
    gotofinish=true;
    cellObject.x=x;
    cellObject.y=y;
    while (gotofinish){
        console.log(cellObject.toId());
        $('#'+cellObject.toId()).removeClass('injured');
        $('#'+cellObject.toId()).addClass('killed');
        if (--cellObject.x<1) gotofinish=false;
        var check=$('#'+cellObject.toId()).hasClass('injured');
        if (!check) gotofinish=false;
    }
    //СканВлево
    gotofinish=true;
    cellObject.x=x;
    cellObject.y=y;
    while (gotofinish){
        console.log(cellObject.toId());
        $('#'+cellObject.toId()).removeClass('injured');
        $('#'+cellObject.toId()).addClass('killed');
        if (++cellObject.y>10) gotofinish=false;
        var check=$('#'+cellObject.toId()).hasClass('injured');
        if (!check) gotofinish=false;
    }
    //СканВправо
    gotofinish=true;
    cellObject.x=x;
    cellObject.y=y;
    while (gotofinish){
        console.log(cellObject.toId());
        $('#'+cellObject.toId()).removeClass('injured');
        $('#'+cellObject.toId()).addClass('killed');
        if (--cellObject.y<1) gotofinish=false;
        var check=$('#'+cellObject.toId()).hasClass('injured');
        if (!check) gotofinish=false;
    }
}



function alertMy(text, callback) {
    $("#forContentInfo").text(text);
    $("#modalInfoButton").on('click', function () {
        $('#modalDialogInfo').modal('hide');
        $("#modalInfoButton").unbind( "click" );
        callback();
    });
    $('#modalDialogInfo').modal();
}

