function usuarios() {
    spinneron();
    $("#inicio").load("vistas/usuarios.html", {}, function () {
        spinneroff();
        $(".active").removeClass("active");
        $("a[href='#/sistema/empresa']").parent().find("ul.nav").removeClass("nav nav-second-level collapse in").addClass("nav nav-second-level collapse");
        $('a[href="#/sistema/empresa"]').closest("ul").closest("li").addClass("active");
        listarUsuarios();
        cerrarUsuario();
        PermisosRender(14, (p) => {
            if (p.agregar === 'N') {
                $('a[href="#/sistema/usuarios/nuevo"]').remove();
            }
        });
    });
}

function listarUsuarios() {
    $("#table-user").DataTable({
        "pageLength": 100,
        destroy: true,
        ajax: {
            type: "post",
            url: "usuarioControlador",
            data: {accion: "listar"}
        },
        columns: [
            {data: "id_usuario"},
            {data: "alias_usu"},
            {data: "nombre_emp",
                render: function (data, type, full, meta) {
                    return data + " " + full.apellido_emp;
                }
            },
            {data: "estado_usu",
                render: function (data, type, full, meta) {
                    if (data == 0) {
                        return '<label class="label label-primary">Activo</label>';
                    } else if (data == 1) {
                        return '<label class="label label-danger">Inactivo</label>';
                    }
                }
            },
            {data: null,
                render: function (data, type, full, meta) {
                    return '<div class="btn-group"><a class="btn btn-primary btn-xs editar" href="#/sistema/usuarios/editar?_id=' + data.id_usuario + '"><i class="far fa-edit"> Editar</i></a><a class="btn btn-warning btn-xs nulled" data-id="' + data.id_usuario + '"><i class="fa fa-ban"> Inhabilitar</i></a></div>';
                }
            }
        ],
        initComplete: function () {
            PermisosRender(14, (p) => {
                if (p.editar === 'N') {
                    $('.editar"]').remove();
                }
                if (p.eliminar === 'N') {
                    $('.nulled').remove();
                }
            });
        }
    });
}

function seleccionarFila() {
    $(".all-row").on('change', function () {
        var check = $(this).parent().siblings();
        if ($(this).is(":checked")) {
            check.each(function () {
                if ($(this).find("input.ver").is(":disabled")) {
                    $(this).find("input.ver").removeAttr("disabled")
                }
                $(this).find('input[type="checkbox"]').prop("checked", true);
            });
        } else {
            check.each(function () {
                $(this).find('input[type="checkbox"]').prop("checked", false);
            });
        }
    });
}

function seleccionarTodo() {
    $(".all").on('change', function () {
        if ($(this).is(":checked")) {
            $(".all-row").each(function (i) {
                if ($(this).is(":indeterminate")) {
                    $(this).prop("indeterminate", false);
                }
                $(this).prop("checked", true);
                var check = $(this).parent().siblings();
                if ($(this).is(":checked")) {
                    check.each(function () {

                        if ($(this).find("input.ver").is(":disabled")) {
                            $(this).find("input.ver").removeAttr("disabled")
                        }

                        $(this).find('input[type="checkbox"]').prop("checked", true);
                    });
                } else {
                    check.each(function () {
                        $(this).find('input[type="checkbox"]').prop("checked", false);
                    });
                }
            });
        } else {
            $(".all-row").each(function (i) {
                $(this).prop("checked", false);
                var check = $(this).parent().siblings();
                if ($(this).is(":checked")) {
                    check.each(function () {
                        $(this).find('input[type="checkbox"]').prop("checked", true);
                    });
                } else {
                    check.each(function () {
                        $(this).find('input[type="checkbox"]').prop("checked", false);
                    });
                }
            });
        }
    });
}

function agregarUsuarios(accion, id) {
    $("#form-user").submit(function (e) {
        e.preventDefault();
        $("#save").attr("disabled", true);
    }).validate({
        rules: {
            pwd_again: {
                equalTo: "#pwd"
            }
        },
        messages: {
            pwd_again: {
                equalTo: "Las contraseñas no coinciden"
            }
        },
        submitHandler: function (form) {
            var permisos = cargarPermisos();
            $.post("usuarioControlador", {
                accion: accion,
                id_usuario: id,
                alias_usu: $("#nombre").val(),
                nombre_emp: $("#nom_emp").val(),
                apellido_emp: $("#ape_emp").val(),
                password_usu: $("#pwd").val(),
                id_sucursal: $("#sucursal").val(),
                permisos: JSON.stringify(permisos)
            }).done(function (response) {
                if (response == 1) {
                    toastr.success("Guardado!");

                    swal({
                        title: "¿Desea agregar un nuevo usuario?",
                        text: "",
                        type: "info",
                        showCancelButton: true,
                        confirmButtonColor: "#1976d2",
                        confirmButtonText: "¡Sí, agregar!",
                        cancelButtonText: "Volver",
                        closeOnConfirm: false
                    }, function (e) {
                        if (e) {
                            swal.close();
                            cargarUsuario();
                        } else {
                            swal.close();
                            usuarios();
                        }
                    });
                } else {
                    toastr.error("Error Inesperado")
                }
            }).fail(function (response, jqxhr, error) {
                toastr.error(error);
            });
            return false;  //This doesn't prevent the form from submitting.
        }
    });
}

function cargarPermisos() {
    var v = [];
    var a = [];
    var e = [];

    $(".ver").each(function (i) {
        if ($(this).is(":checked")) {
            v[i] = "Y";
        } else {
            v[i] = "N";
        }
    });

    $(".agregar").each(function (i) {
        if ($(this).is(":checked")) {
            a[i] = "Y";
        } else {
            a[i] = "N";
        }
    });

    $(".editar").each(function (i) {
        if ($(this).is(":checked")) {
            e[i] = "Y";
        } else {
            e[i] = "N";
        }
    });

    var permisos =
            {
                articulos: [
                    {ver: v[0], agregar: a[0], editar: e[0]}
                ],
                clientes: [
                    {ver: v[1], agregar: a[1], editar: e[1]}
                ],
                compras: [
                    {ver: v[2], agregar: a[2], editar: e[2]}
                ],
                ventas: [
                    {ver: v[3], agregar: a[3], editar: e[3]}
                ],
                ubicacion: [
                    {ver: v[4], agregar: a[4], editar: e[4]}
                ],
                proveedores: [
                    {ver: v[5], agregar: a[5], editar: e[5]}
                ],
                stock: [
                    {ver: v[6], agregar: a[6], editar: e[6]}
                ],
                anulados: [
                    {ver: v[7], agregar: a[7], editar: e[7]}
                ],
                fondos: [
                    {ver: v[8], agregar: a[8], editar: e[8]}
                ],
                utilidades: [
                    {ver: v[9], agregar: a[9], editar: e[9]}
                ],
                sistema: [
                    {ver: v[10], agregar: a[10], editar: e[10]}
                ],
                configurar: [
                    {ver: v[11], agregar: a[11], editar: e[11]}
                ]
            };
    return permisos;

}

function checkboxIndeterminados() {
    $(".ver").on("change", function () {
        if ($(this).is(":checked")) {
            $(this).parent().parent().find("td input.all-row").prop("indeterminate", true);
        } else {

            if ($(this).parent().parent().find("td input.editar").is(":checked")) {
                $(this).parent().parent().find("td input.editar").prop("checked", false);
            }

            if ($(this).parent().parent().find("td input.agregar").is(":checked")) {
                $(this).parent().parent().find("td input.agregar").prop("checked", false);
            }

            if ($(this).parent().parent().find("td input.all-row").is(":indeterminate")) {
                $(this).parent().parent().find("td input.all-row").prop("indeterminate", false);
            } else if ($(this).parent().parent().find("td input.all-row").is(":checked")) {
                $(this).parent().parent().find("td input.all-row").prop("checked", false);
            }

        }
    });

    $(".agregar").on("change", function () {
        if ($(this).is(":checked")) {
            if (!$(this).parent().parent().find("td input.editar").is(":checked")) {

                $(this).parent().parent().find("td input.all-row").prop("indeterminate", true);

                if (!$(this).parent().parent().find("td input.ver").is(":checked")) {
                    $(this).parent().parent().find("td input.ver").prop("checked", true).attr("disabled", true);
                } else if (!$(this).parent().parent().find("td input.ver").is(":disabled")) {
                    $(this).parent().parent().find("td input.ver").prop("checked", true).attr("disabled", true);
                }
            } else {
                $(this).parent().parent().find("td input.all-row").prop("indeterminate", false);
                $(this).parent().parent().find("td input.all-row").prop("checked", true);
                $(this).parent().parent().find("td input.ver").removeAttr("disabled");
            }
        } else {

            if ($(this).parent().parent().find("td input.all-row").is(":checked")) {
                $(this).parent().parent().find("td input.all-row").prop("checked", false);
                $(this).parent().parent().find("td input.all-row").prop("indeterminate", true);
            }

            if (!$(this).parent().parent().find("td input.editar").is(":checked")) {
                $(this).parent().parent().find("td input.ver").removeAttr("disabled");
            } else {
                $(this).parent().parent().find("td input.ver").attr("disabled", true);
            }
        }
    });

    $(".editar").on("change", function () {
        if ($(this).is(":checked")) {
            if (!$(this).parent().parent().find("td input.agregar").is(":checked")) {

                $(this).parent().parent().find("td input.all-row").prop("indeterminate", true);

                if ($(this).parent().parent().find("td input.all-row").is(":checked")) {
                    $(this).parent().parent().find("td input.all-row").prop("checked", false);
                    $(this).parent().parent().find("td input.all-row").prop("indeterminate", true);
                }

                if (!$(this).parent().parent().find("td input.ver").is(":checked")) {
                    $(this).parent().parent().find("td input.ver").prop("checked", true).attr("disabled", true);
                } else if (!$(this).parent().parent().find("td input.ver").is(":disabled")) {
                    $(this).parent().parent().find("td input.ver").prop("checked", true).attr("disabled", true);
                }

            } else {
                $(this).parent().parent().find("td input.all-row").prop("indeterminate", false);
                $(this).parent().parent().find("td input.all-row").prop("checked", true);
                $(this).parent().parent().find("td input.ver").removeAttr("disabled");
            }
        } else {
            if ($(this).parent().parent().find("td input.all-row").is(":checked")) {
                $(this).parent().parent().find("td input.all-row").prop("checked", false);
                $(this).parent().parent().find("td input.all-row").prop("indeterminate", true);
            }
            if (!$(this).parent().parent().find("td input.agregar").is(":checked")) {
                $(this).parent().parent().find("td input.ver").removeAttr("disabled");
            } else {
                $(this).parent().parent().find("td input.ver").attr("disabled", true);
            }
        }
    });
}

function cargarUsuario() {
    $("#inicio").load("vistas/insertUsuario.html", {}, function () {
        userCrumb();
        seleccionarTodo();
        seleccionarFila();
        agregarUsuarios(0, 0);
        checkboxIndeterminados();
        selectSucursales();
        $("#sucursal").select2({
            placeholder: "Seleccione una sucursal",
            allowClear: true
        });
    });
}

function UsuarioActual() {
    $.post("usuarioControlador", {accion: "actual"}, function (response) {
        $("button.btn-circle").html(response.nombre_emp.substring(0, 1) + response.apellido_emp.substring(0, 1));
        $("strong.font-bold").html(response.nombre_emp + " " + response.apellido_emp);
    });
}

function editarUsuario(id) {
    $("#inicio").load("vistas/insertUsuario.html", {}, function () {
        seleccionarFila();
        seleccionarTodo();
        checkboxIndeterminados();
        agregarUsuarios(1, id);
        selectSucursales();
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
}

function cerrarUsuario() {
    $("#table-user tbody").on('click', '.nulled', function () {
        var id = this.getAttribute("data-id");
        swal({
            title: "¿Está seguro?",
            text: "Inhabilitar usuario",
            type: "warning",
            showCancelButton: true,
            confirmButtonColor: "#FFC107",
            confirmButtonText: "¡Sí, inhabilitar!",
            cancelButtonText: "Cancelar",
            closeOnConfirm: false
        }, function () {
            $.post("usuarioControlador", {
                accion: "inhabilitar",
                id: id
            }).done((response) => {
                if (response) {
                    swal("¡Listo!", "Usuario inhabilitado.", "success");
                    listarUsuarios();
                }
            }).fail((response, jqxhr, error) => {
                alert(error);
            });
        });
    });
}