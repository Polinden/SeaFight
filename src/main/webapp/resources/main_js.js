//КОНСТАНТЫ ДЛЯ РАБОТЫ ПРОГРАММЫ
//Размер клеточки
var forDivBox=50;
//Погрешность позиционирования
var forDivBoxDela=25;
//Считаем сколько мы расставили кораблей 
var goFuther=1;





//Узнаем - не мобильный ли у нас
function detectmob() {
    if (window.innerWidth <= 800 && window.innerHeight <= 600) {
        return true;
    } else {
        return false;
    }
};


//Определяем браузер
var is_chrome = navigator.userAgent.indexOf('Chrome') > -1;
var is_explorer = navigator.userAgent.indexOf('MSIE') > -1;
var is_firefox = navigator.userAgent.indexOf('Firefox') > -1;
var is_safari = navigator.userAgent.indexOf("Safari") > -1;
var is_opera = navigator.userAgent.toLowerCase().indexOf("op") > -1;
if ((is_chrome)&&(is_safari)) {is_safari=false;}
if ((is_chrome)&&(is_opera)) {is_chrome=false;}



//И настраиваем под мобильник размеры ячеек и допустимую точность позиционирования
function change() {
    if (detectmob()) {
        forDivBox=22;
        forDivBoxDela=14;
        $(".row1").css("margin-bottom", 4);
        $(".spaser3").width(4);
    }
    else {
        forDivBox=50;
        forDivBoxDela=25;
    }
    $(".items").width(forDivBox);
    $(".items").height(forDivBox);
    $(".appoints").width(forDivBox);
    $(".appoints").height(forDivBox);
    $('body').css("visibility", "visible");
};






