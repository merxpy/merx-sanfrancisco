function num() {
    $(".num").on('keypress', function (e) {
        let tecla = (document.all) ? e.keyCode : e.which;
        //Tecla de retroceso para borrar, siempre la permite
        if (tecla === 8) {
            return true;
        }

        // Patron de entrada, en este caso solo acepta numeros
        let patron = /[0-9]/;
        let tecla_final = String.fromCharCode(tecla);
        return patron.test(tecla_final);
    });
}
       