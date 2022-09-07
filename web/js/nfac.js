function nfac() {
    $(".nfac").on('keypress', function (e) {
        var n = $(this).val();
        tecla = (document.all) ? e.keyCode : e.which;

        //Tecla de retroceso para borrar, siempre la permite
        if (tecla == 8 || tecla == 9) {
            return true;
        }

        // Patron de entrada, en este caso solo acepta numeros
        patron = /[0-9]/;
        tecla_final = String.fromCharCode(tecla);

        if (n.length == 3 || n.length == 7) {
            $(this).val(n + "-");
        }

        if (n.length == 15) {
            var s = window.getSelection().toString();
            if (s.length) {
                nfac();
            } else {
                return false;
            }
        } else {
            return patron.test(tecla_final);
        }

    });
}
       