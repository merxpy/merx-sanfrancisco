define(() => {
    var exports = {};
    exports.ListarPermisos = function () {
        $.post("permisosControlador", {
            accion: "listar"
        }).done((response) => {
            for (var i = 0; i < response.length; i++) {
                $("#permisos").append(`<tr>
                                            <td><input class="check all-row" type="checkbox" value=""></td>
                                            <td>${response[i].modulo_per}</td>
                                            <td><input class="check ver" type="checkbox" value=""></td>
                                            <td><input class="check agregar" type="checkbox" value=""></td>
                                            <td><input class="check editar" type="checkbox" value=""></td>
                                            <td><input class="check agregar" type="checkbox" value=""></td>
                                            <td><input class="check editar" type="checkbox" value=""></td>
                                        </tr>`);
            }
        }).fail((response, jqxhr, error) => {
            toastr.error(error + ", contacte con el desarrollador");
        });
    };

    exports.MostrarUsuario = function () {
        $("#inicio").load("vistas/insertUsuario.html", {}, function () {
            userCrumb();
            seleccionarTodo();
            seleccionarFila();
            agregarUsuarios(0, 0);
            checkboxIndeterminados();
            selectSucursales();
            exports.ListarPermisos();
            $("#sucursal").select2({
                placeholder: "Seleccione una sucursal",
                allowClear: true
            });
        });
    };

    exports.EditarUsuario = function (id) {
        $("#inicio").load("vistas/insertUsuario.html", {}, function () {
            seleccionarFila();
            seleccionarTodo();
            checkboxIndeterminados();
            agregarUsuarios(1, id);
            selectSucursales();
            exports.ListarPermisos();
            $("#sucursal").select2({
                placeholder: "Seleccione una sucursal",
                allowClear: true
            });

            $.post("usuarioControlador", {
                accion: "buscar",
                id_usuario: id
            }).done(function (response) {
                $("#nombre").val(response.alias_usu);
                $("#nom_emp").val(response.nombre_emp);
                $("#ape_emp").val(response.apellido_emp);
                $("#sucursal").val(response.id_sucursal).trigger('change');
                $(".rm").remove();
            }).fail(function (response, jqxhr, error) {
                toastr.error(error);
            });

            $.post("usuarioControlador", {
                accion: "buscarPermisos",
                id_usuario: id
            }).done(function (response) {
                $("#permisos tr").each(function (i) {
                    if (i < $("#permisos tr").length) {
                        if (response[i].ver == "Y") {
                            $(this).find("td input.ver").prop("checked", true);
                        }
                        if (response[i].agregar == "Y") {
                            $(this).find("td input.agregar").prop("checked", true);
                        }
                        if (response[i].editar == "Y") {
                            $(this).find("td input.editar").prop("checked", true);
                        }
                    }

                });
            }).fail(function (response, jqxhr, error) {
                toastr.error(error);
            });
        });
    };

    exports.PermisosxUsuario = function () {
        $.post('usuarioControlador', {
            accion: 'permisos'
        }).done((response) => {
            $.each(response, (i) => {
                if (response[i].ver === 'N') {
                    $('li a[href="' + response[i].url + '"]').remove();
                    $('li a[href="#/anulados/' + response[i].url.replace("#/", "") + '"]').remove();
                }
            });
        }).fail((response, jqxhr, error) => {
            toastr.error(error + ", contacte con el desarrollador");
        }).always(() => {
            $('.orden-trabajo').parent().find('ul li a').length ? null : $('.orden-trabajo').parent().remove();
            $('.inventario').parent().find('ul li a').length ? null : $('.inventario').parent().remove();
            $('.anulado').parent().find('ul li a').length ? null : $('.anulado').parent().remove();
            $('.sistema').parent().find('ul li a').length ? null : $('.sistema').parent().remove();
            $('.utilidades').parent().find('ul li a').length ? null : $('.utilidades').parent().remove();
        });
    };

    exports.VerificarPermisos = function (permiso, callback) {
        $.post('usuarioControlador', {
            accion: "usuario",
            id_permiso: permiso
        }).done((response) => {
            callback(response);
        }).fail((response, jqxhr, error) => {
            toastr.error(error + ", contacte con el desarrollador");
        });
    };

    return exports;
});

