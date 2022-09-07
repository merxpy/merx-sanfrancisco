requirejs.config({
    baseUrl: 'Controladores',
    paths: {
        rutasControlador: 'rutasControlador',
        paisesControlador: 'paisesControlador',
        monedasControlador: 'monedasControlador',
        tipopagoControlador: "tipopagoControlador",
        empresaControlador: "empresaController",
        mermasControlador: "mermasControlador",
        articuloControlador: "articuloControlador",
        perfilControlador: "../src/perfilControlador",
        sucursalesControlador: "../src/sucursalesControlador",
        loginControlador: "loginController",
        usuariosControlador: "../src/usuariosControlador",
        cajaControlador: "../src/cajaControlador",
        timbradosControlador: "../src/timbradosControlador",
        serialesControlador: "../src/serialesControlador",
        comprasControlador: "../src/comprasControlador",
        permisosControlador: "../src/permisosControlador",
        inicioControlador: "../src/inicioControlador",
        anotacionesControlador: "../src/anotacionesControlador",
        ordencompraControlador: "../src/ordencompraControlador",
        declaracionesControlador: "../src/declaracionesControlador",
        informeinventarioControlador: "../src/informeinventarioControlador",
        arqueocajaControlador: "../src/arqueocajaControlador",
        plantillasControlador: "../src/plantillasControlador",
        reportesControlador: "../src/reportesControlador",
        trabajoControlador: "../src/trabajoControlador",
        maquinariasControlador: "../src/maquinariaControlador",
        categoriasControlador: "../src/categoriasControlador",
        notificacionesControlador: "../src/notificacionesControlador",
        gastosControlador: "../src/gastosControlador",
        cuentasControlador: "../src/cuentasControlador",
        deliveryControlador: "../src/deliveryControlador",
    }
});


require(['config'], function () {
    require([
        'rutasControlador',
        'loginControlador',
        'usuariosControlador',
        'permisosControlador',
        'declaracionesControlador',
        'empresaControlador',
        'notificacionesControlador',
        'cajaControlador'
    ], function (r, iniciar, usuario, permiso, declaracion, empresa, notificacion, caja) {
        if (localStorage.getItem("bodyclass")) {
            $("body").removeAttr('class');
            $("body").addClass(localStorage.getItem("bodyclass"));
        }
        permiso.PermisosxUsuario();
        r.Rutas(document.location.toString());
        usuario.UsuarioActual();
        notificacion.ListarNotificaciones();
        empresa.VerificarTimbrado(function (x) {
            x;
        });
        iniciar.VerificarSession();
        $("#ddji").on('click', function () {
            declaracion.Declaracion();
        });

        $(".navbar-minimalize.minimalize-styl-2.btn.btn-primary").on('click', function () {
            localStorage.setItem("bodyclass", $("body").attr("class"));
        });
    });
});
