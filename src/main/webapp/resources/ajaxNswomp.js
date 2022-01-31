
//ВСЕ ДЛЯ СВОМПА

//Число попыток реконнекта
var tries=0;

function initSTOMP(urlToGo, urlOnExpire) {

    //Проверка поддержки
    checkSupport();

    initSTOMP.url=urlToGo;

    //Отладка стомпа
    //client.debug = onDebug;
    
    reconnect();

    //Отладка стомпа
    function onDebug(m) {
        alert("STOMP DEBUG "+m);
    }

    //Проверка поддержки
    function checkSupport() {
        if (window.WebSocket){
            console.log("BROWSER SUPPORTED");
        } else {
            console.log("BROWSER NOT SUPPORTED");
            alert("Загрузите игру в браузере с поддержкой современных функций (Веб Соккет в т.ч.)");
        }
    }

    //Обработка ошибок делает реконнект 3 раза - а потом сдаеться и выходит их игры
    function stompError (error) {
        console.log("STOMP reconnecting.......");
        setTimeout(reconnect, 300);
    }

    //Коннект и реконнект с настройкой опций
    function reconnect() {
        //Исходные позиции
        var token = $("meta[name='_csrf']").attr("content");
        var header = $("meta[name='_csrf_header']").attr("content");
        var headers = {};
        headers[header] = token;
        initSTOMP.ws = new SockJS(initSTOMP.url);
        initSTOMP.resultLink =  Stomp.over(initSTOMP.ws);
        initSTOMP.resultLink.heartbeat.incoming = 40000;
        initSTOMP.resultLink.heartbeat.outgoing = 40000;
       //Для RabbiMQ
       // initSTOMP.resultLink.heartbeat.incoming = 0;
       // initSTOMP.resultLink.heartbeat.outgoing = 0;
        if ((tries++)>0)  {console.log('!!!STOMP CLOSED!!!'); window.location = urlOnExpire;}
        else initSTOMP.resultLink.connect(headers, initSTOMP.callback, stompError);
    }
}





//ВСЕ ДЛЯ АЯКСА

// Колбеки: 1 - обрадотка полученной информации, 2 - по завершении получения, 3 - обработка ошибок
function sendAJAXget (URLserver, callback1, callback2, callback3) {
    initAJAXforSpring();
    $("body").css('cursor','wait !important; z-index: 999; height: 100%; width: 100%;');
    $.ajax({
        type: "GET",
        url: URLserver,
        dataType: "json",
        contentType: "application/json; charset=utf-8",
        error: function(jqXHR, textStatus, errorThrown) {
            callback3(jqXHR, textStatus, errorThrown);
        },
        success: function (result) {
            callback1(result);
        },
        complete:
            function() {
                callback2();
                $('body').css('cursor', 'default');
            }
    });
}


// Колбеки: 1 - обрадотка полученной информации, 2 - по завершении получения, 3 - обработка ошибок
function  sendAJAXpost (URLserver, toSend, callback1, callback2, callback3) {
    initAJAXforSpring();
    $("body").css('cursor','wait !important; z-index: 999; height: 100%; width: 100%;');
    $.ajax({
        type: "POST",
        url: URLserver,
        contentType: "application/json; charset=utf-8",
        data: toSend,
        dataType: "json",
        error: function(jqXHR, textStatus, errorThrown) {
            callback3(jqXHR, textStatus, errorThrown);
        },
        success: function (result) {
            callback1(result);
        },
        complete:
            function() {
                callback2();
                $('body').css('cursor', 'default');
            }
    });
}



//Настройка безопасности для Аякс
function initAJAXforSpring() {
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");
    $(document).ajaxSend(function(e, xhr, options) {
        xhr.setRequestHeader(header, token);
    });
}



//Заготовочка для обработки ошибок
var ajaxErrorMessage = function (jqXHR, textStatus, errorThrown) {
    alert( "Игровые данные не получены с сервера: ошибка интренета или истек таймаут Вашей сессии");
};


