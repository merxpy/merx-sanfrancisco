function DateTime() {
    var d = new Date();
    var date = toDate(d.getDate());
    var month = toDate(d.getMonth() + 1);
    var year = toDate(d.getFullYear());
    var hour = toDate(d.getHours());
    var min = toDate(d.getMinutes());
    return year + "-" + month + "-" + date + "T" + hour + ":" + min;
}
function toDate(a) {
    if (a < 10) {
        a = "0" + a;
    }
    return a;
}