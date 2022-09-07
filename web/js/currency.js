function currency() {
    $(document).on('keyup', ".price", function (e) {
        var nro = Entero($(this).val());

        if (!isNaN(parseInt(nro))) {
            $(this).val(parseInt(nro).toLocaleString('de-DE'));
        }
    });
}

function Entero(nro) {
    nro = nro !== undefined ? nro.toString() : "";
    if (nro.length > 3) {
        if (nro.split(".").length - 1) {
            for (var i = 0; i <= nro.split(".").length - 1; i++) {
                nro = nro.replace(".", "");
            }
        }
    }
    return nro.replace(",", ".");
}

function dot(num) {
    var nro;
    if (!isNaN(parseInt(num))) {
        nro = parseInt(num).toLocaleString('de-DE');
    }
    return nro;
}

function decimales(d) {
    if (d.includes(".")) {
        return d.replace(".", ",");
    } else {
        return d;
    }
}

function calcularTotal(e) {
    var c = parseFloat($(e).find("td input.cant").val());
    var p = Entero($(e).find("td input.price").val());
    var total = c * p;
    return dot(total);
}

function calcularIva(i, pr) {
    var p = Entero(pr);
    var iva;
    if (i != "E") {
        switch (i) {
            case "10":
                iva = p / 11;
                break;
            case "5":
                iva = p / 21;
                break;
        }
    } else {
        iva = p;
    }
    return Math.round(iva);
}