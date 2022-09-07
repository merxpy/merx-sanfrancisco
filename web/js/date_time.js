function date_time(iddate, idtime) {
    setInterval(function () {
        date = new Date;
        year = date.getFullYear();
        month = date.getMonth();
        months = new Array('Enero', 'Febrero', 'Marzo', 'Abril', 'Mayo', 'Junio', 'Julio', 'Agosto', 'Septiembre', 'Octubre', 'Noviembre', 'Deciembre');
        d = date.getDate();
        day = date.getDay();
        days = new Array('Domingo', 'Lunes', 'Martes', 'Miércoles', 'Jueves', 'Viernes', 'Sábado');
        h = date.getHours();
        if (h < 10)
        {
            h = "0" + h;
        }
        m = date.getMinutes();
        if (m < 10)
        {
            m = "0" + m;
        }
        s = date.getSeconds();
        if (s < 10)
        {
            s = "0" + s;
        }
        resultdate = '' + days[day] + ', ' + d + ' de ' + months[month] + ' del ' + year;
        resulttime = h + ':' + m + ':' + s;
        if (document.getElementById(iddate)) {
            document.getElementById(iddate).innerHTML = resultdate;
        }
        if (document.getElementById(idtime)) {
            document.getElementById(idtime).innerHTML = resulttime;
        }
        return true;
    }, 1000);
}