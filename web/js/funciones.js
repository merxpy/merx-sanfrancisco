function cero(n) {
    var ncob = "0000000";
    var res = ncob.substring(0, 7 - n.length) + n;
    return res;
}

function escribirFecha(fecha) {
    //se le pasa año, mes, dia 
    var date = fecha ? moment(fecha, 'YYYY-MM-DD').toDate() : new Date;
    var d = date.getDate();
    var day = date.getDay();
    var month = date.getMonth();
    var year = date.getFullYear();
    var months = new Array('Enero', 'Febrero', 'Marzo', 'Abril', 'Mayo', 'Junio', 'Julio', 'Agosto', 'Septiembre', 'Octubre', 'Noviembre', 'Diciembre');
    var days = new Array('Domingo', 'Lunes', 'Martes', 'Miércoles', 'Jueves', 'Viernes', 'Sábado');
    var resultdate = '' + days[day] + ', ' + d + ' de ' + months[month] + ' del ' + year;
    return resultdate;
}

function formatDate(fecha) {
    var m = parseInt(fecha.substring(5, 7)) - 1;
    var date = new Date;
    if (fecha.length > 10) {
        date = new Date(fecha.substring(0, 4), m, fecha.substring(8, 10), fecha.substring(11, 13), fecha.substring(14, 16));
    } else {
        date = new Date(fecha.substring(0, 4), m, fecha.substring(8));
    }
    return date;
}
function startTime() {
    var today = new Date();
    var hr = today.getHours();
    var min = today.getMinutes();
    var sec = today.getSeconds();
    var ap = (hr < 12) ? "<span>AM</span>" : "<span>PM</span>";
    hr = (hr === 0) ? 12 : hr;
    hr = (hr > 12) ? hr - 12 : hr;
    //Add a zero in front of numbers<10
    hr = checkTime(hr);
    min = checkTime(min);
    sec = checkTime(sec);
    $(".clock").html(hr + ":" + min + ":" + sec + " " + ap);

    setTimeout(function () {
        startTime();
    }, 500);

    function checkTime(i) {
        if (i < 10) {
            i = "0" + i;
        }
        return i;
    }
}