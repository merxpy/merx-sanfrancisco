define(['sucursalesControlador'], function (sucursal) {
    var exports = {};
    var tabla;
    var estado;
    exports.Usuarios = function () {
        spinneron();
        $("#inicio").load("vistas/usuarios.html", {}, function () {
            spinneroff();
            $(".active").removeClass("active");
            $("a[href='#/sistema/auditoria']").parent().find("ul.nav").removeClass("nav nav-second-level collapse in").addClass("nav nav-second-level collapse");
            $('a[href="#/sistema/auditoria"]').closest("ul").closest("li").addClass("active");
            exports.ListarUsuarios();
            exports.CerrarUsuarios();
            PermisosRender(14, (p) => {
                if (p.agregar === 'N') {
                    $('a[href="#/sistema/usuarios/nuevo"]').remove();
                }
            });
        });
    };
    exports.ListarUsuarios = function () {
        var btn;
        tabla = $("#table-user").DataTable({
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
                {data: "nombre_sucursal"},
                {data: "estado_usu",
                    render: function (data, type, full, meta) {
                        if (data == 0) {
                            estado = "Inhabilitar";
                            btn = `<a class="btn btn-warning btn-xs nulled" data-id="${full.id_usuario}"><i class="fa fa-ban"> Inhabilitar</i></a>`;
                            return '<label class="label label-primary">Activo</label>';
                        } else if (data == 1) {
                            estado = "habilitar";
                            btn = `<a class="btn btn-info btn-xs nulled" data-id="${full.id_usuario}"><i class="fa fa-ban"> Habilitar</i></a>`;
                            return '<label class="label label-danger">Inactivo</label>';
                        }
                    }
                },
                {data: null,
                    render: function (data, type, full, meta) {
                        return `<div class="btn-group">
                                    <a class="btn btn-primary btn-xs editar" href="#/sistema/usuarios/editar?_id=${data.id_usuario}"><i class="far fa-edit"> Editar</i></a>
                                    ${btn}
                                </div>`;
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
    };
    exports.BuscarUsuario = function (id) {
        $.post('usuarioControlador', {
            accion: "buscar",
            id_usuario: id
        }).done((response) => {
            $("#inicio").load("vistas/insertUsuario.html", function () {
                sucursal.SelectSucursales();
                $("#sucursal").select2({
                    placeholder: "Seleccione la sucursal",
                    allowClear: true
                });
                $('input[name="alias_usu"]').val(response.alias_usu);
                $('input[name="nombre_emp"]').val(response.nombre_emp);
                $('input[name="apellido_emp"]').val(response.apellido_emp);
                setTimeout(() => {
                    $('select[name="id_sucursal"]').val(response.id_sucursal).trigger("change");
                    $('input[name="password_usu"]').parent().parent().parent().remove();
                    if (id !== 0) {
                        $.post("usuarioControlador", {
                            accion: "admin"
                        }).done((response) => {
                            if (response == "SI") {
                                $.post("usuarioControlador", {
                                    accion: "verificarAdmin",
                                    id_usuario: id
                                }).done((response) => {
                                    if (response == "NO") {
                                        $(`.ibox-title`).append(`<a id="change" class="btn btn-primary btn-xs pull-right"><i class="fa fa-key"></i> Cambiar Contraseña</a>`)
                                        CambiarPassword(id);
                                    }

                                }).fail((response, jqxhr, error) => {
                                    toastr.error(error + ", contacte con el desarrollador");
                                });
                            }

                        }).fail((response, jqxhr, error) => {
                            toastr.error(error + ", contacte con el desarrollador");
                        });

                    }
                }, 250);
                exports.AgregarUsuario(id);
            });
        }).fail((response, jqxhr, error) => {
            toastr.error(error + ", contacte con el desarrollador");
        }).always((response, jqxhr, error) => {
            if (jqxhr === 'success') {
                setTimeout(() => {
                    exports.ListarPermisos(id);
                }, 250);
            }
        });
    };
    exports.AgregarUsuario = function (id) {
        $("#form-user").submit(function (e) {
            e.preventDefault();
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
                var p = [];
                $("tbody tr").each(function (i) {
                    p[i] = {
                        id_permiso: $(this).attr("data-row"),
                        ver: $(this).find(".ver input").val(),
                        agregar: $(this).find(".agregar input").val(),
                        editar: $(this).find(".editar input").val(),
                        eliminar: $(this).find(".eliminar input").val(),
                        aprobar: $(this).find(".aprobar input").val()
                    };
                });
                var fd = ConvertFormToJSON(form);
                fd.id_usuario = id ? id : 0;
                $.post("usuarioControlador", {
                    accion: "insertar",
                    datos: JSON.stringify(fd),
                    permisos: JSON.stringify(p)
                }).done((response) => {
                    toastr.success("¡Guardado!");
                    window.history.back();
                }).fail((response, jqxhr, error) => {
                    toastr.error(error + ", contacte con el desarrollador");
                });
                return false; //This doesn't prevent the form from submitting.
            }
        });
    };
    exports.UsuarioActual = function () {
        $.post("usuarioControlador", {accion: "actual"}, function (response) {
            $("button.btn-circle").html(response.nombre_emp.substring(0, 1) + response.apellido_emp.substring(0, 1));
            $("strong.font-bold").html(response.nombre_emp + " " + response.apellido_emp);
        });
    };
    exports.CerrarUsuarios = function () {
        $("#table-user tbody").on('click', '.nulled', function () {
            var id = this.getAttribute("data-id");
            swal({
                title: "¿Está seguro?",
                text: estado + " usuario",
                type: "warning",
                showCancelButton: true,
                confirmButtonColor: "#FFC107",
                confirmButtonText: "¡Sí, " + estado + "!",
                cancelButtonText: "Cancelar",
                closeOnConfirm: false
            }, function () {
                $.post("usuarioControlador", {
                    accion: "inhabilitar",
                    id: id
                }).done((response) => {
                    if (response) {
                        swal("¡Listo!", "", "success");
                        console.log(tabla);
                        tabla.ajax.reload();
                    }
                }).fail((response, jqxhr, error) => {
                    alert(error);
                });
            });
        });
    };
    exports.ListarPermisos = function (id) {
        $.post("permisosControlador", {
            accion: "listar"
        }).done((response) => {
            $.each(response, (i) => {
                var tr = document.createElement("tr");
                tr.setAttribute("data-row", response[i].id_permiso);
                $.post("usuarioControlador", {
                    accion: "uxp",
                    id_usuario: id ? id : 0,
                    id_permiso: response[i].id_permiso
                }).done(function (xhr) {
                    if (xhr.id_usuario) {
                        if (xhr.ver === 'Y' && xhr.agregar === 'Y' && xhr.editar === 'Y' && xhr.eliminar === 'Y' && xhr.aprobar === 'Y') {
                            $(tr).append(`<td><input type="checkbox" class="select-row" checked="checked"></td>`);
                        } else {
                            if (xhr.ver) {
                                if (xhr.ver === 'N' && xhr.agregar === 'N' && xhr.editar === 'N' && xhr.eliminar === 'N' && xhr.aprobar === 'N') {
                                    $(tr).append(`<td><input type="checkbox" class="select-row"></td>`);
                                } else {
                                    var td = document.createElement("td");
                                    var input = document.createElement("input");
                                    $(input).attr("type", "checkbox");
                                    $(input).addClass("select-row");
                                    input.indeterminate = true;
                                    $(td).html(input);
                                    $(tr).append(td);
                                }
                            } else {
                                $(tr).append(`<td><input class="select-row" type="checkbox"/></td>`);
                            }
                        }
                        $(tr).append(` <td> ${response[i].modulo_per} </td>`);
                        if (xhr.ver === 'Y') {
                            $(tr).append(`<td class="ver"><input type="checkbox" value="Y" checked/></td>`);
                        } else {
                            $(tr).append(`<td class="ver"><input type="checkbox" value="N"/></td>`);
                        }
                        if (xhr.agregar === 'Y') {
                            $(tr).append(`<td class="agregar"><input type="checkbox" value="Y" checked/></td>`);
                        } else {
                            $(tr).append(`<td class="agregar"><input type="checkbox" value="N"/></td>`);
                        }
                        if (xhr.editar === 'Y') {
                            $(tr).append(`<td class="editar"><input type="checkbox" value="Y" checked/></td>`);
                        } else {
                            $(tr).append(`<td class="editar"><input type="checkbox" value="N"/></td>`);
                        }
                        if (xhr.eliminar === 'Y') {
                            $(tr).append(`<td class="eliminar"><input type="checkbox" value="Y" checked/></td>`);
                        } else {
                            $(tr).append(`<td class="eliminar"><input type="checkbox" value="N"/></td>`);
                        }
                        if (xhr.aprobar === 'Y') {
                            $(tr).append(`<td class="aprobar"><input type="checkbox" value="Y" checked/></td>`);
                        } else {
                            $(tr).append(`<td class="aprobar"><input type="checkbox" value="N"/></td>`);
                        }
                    } else {
                        $(tr).append(`<td><input class="select-row" type="checkbox"/></td>
                                            <td>${response[i].modulo_per}</td>
                                            <td class="ver"><input type="checkbox" value="N"/></td>
                                            <td class="agregar"><input type="checkbox" value="N"/></td>
                                            <td class="editar"><input type="checkbox" value="N"/></td>
                                            <td class="eliminar"><input type="checkbox" value="N"/></td>
                                            <td class="aprobar"><input type="checkbox" value="N"/></td>`);
                    }
                }).fail((response, jqxhr, error) => {
                    toastr.error(error + ", contacte con el desarrollador");
                }).always(() => {
                    exports.SeleccionarFilas();
                    exports.SeleccionarTodo();
                });
                $("table.table.table-hover tbody").append(tr);
            });
        }).fail((response, jqxhr, error) => {
            toastr.error(error + ", contacte con el desarrollador");
        });
    };
    exports.SeleccionarFilas = function () {
        $(".select-row").on('click', function () {
            if (this.checked) {
                $(this).parent().parent().not(this).find('input:checkbox').prop('checked', this.checked).val("Y");
            } else {
                $(this).parent().parent().not(this).find('input:checkbox').prop('checked', this.checked).val("N");
            }
        });
    };
    exports.SeleccionarTodo = function () {
        $(".select-all").click(function () {
            $('input:checkbox').not(this).prop('checked', this.checked);
            if (this.checked) {
                $('input:checkbox').not(this).not(".select-row").val("Y");
            } else {
                $('input:checkbox').not(this).not(".select-row").val("N");
            }
            $('input:checkbox').not(this).prop('indeterminate', false);
        });
        setTimeout(() => {
            if ($("input:checked").not(".select-all").length === $('input:checkbox').not(".select-all").length) {
                $(".select-all").prop("checked", true);
            } else {
                if ($("input:checked").length) {
                    $(".select-all").prop("indeterminate", true);
                } else {
                    $(".select-all").prop("indeterminate", false);
                }
            }

            $('input:checkbox').not(".select-all").on('click', function () {
                if (this.checked) {
                    this.value = "Y";
                } else {
                    this.value = "N";
                }
                if ($(this).parent().parent().find('input:checkbox').not(".select-row").length === $(this).parent().parent().find('input:checked').not(".select-row").length) {
                    $(this).parent().parent().find('input.select-row').prop("indeterminate", false);
                    $(this).parent().parent().find('input.select-row').prop("checked", true);
                } else {
                    if ($(this).parent().parent().find('input:checked').not(".select-row").length) {
                        $(this).parent().parent().find('input.select-row').prop("checked", false);
                        $(this).parent().parent().find('input.select-row').prop("indeterminate", true);
                    } else {
                        $(this).parent().parent().find('input.select-row').prop("checked", false);
                        $(this).parent().parent().find('input.select-row').prop("indeterminate", false);
                    }
                }

                if ($("input:checked").not(".select-all").length === $('input:checkbox').not(".select-all").length) {
                    $(".select-all").prop("indeterminate", false);
                    $(".select-all").prop("checked", true);
                } else {
                    if ($("input:checked").length) {
                        $(".select-all").prop("checked", false);
                        $(".select-all").prop("indeterminate", true);
                    } else {
                        $(".select-all").prop("indeterminate", false);
                        $(".select-all").prop("checked", false);
                    }
                }
            });
        }, 500);
    };
    var CambiarPassword = function (id) {
        $("#change").on('click', function () {
            $("#modal").load("vistas/modal_password.html", function () {
                $("#modal-pass").modal();
                $("#form-pwd").on('submit', function (e) {
                    e.preventDefault();
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
                        var fd = ConvertFormToJSON(form);
                        fd.id_usuario = id ? id : 0;
                        $.post("usuarioControlador", {
                            accion: "cambiarpwd",
                            datos: JSON.stringify(fd)
                        }).done((response, jqxhr, error) => {
                            if (response === 1) {
                                toastr.success("¡Guardado!");
                                $("#modal-pass").modal('hide');
                            } else if (response === 0) {
                                toastr.error(error + " contacte con el desarrollador");
                            } else if (response === 2) {
                                toastr.warning("¿Qué tratas de hacer?");
                                $("#modal-pass").modal('hide');
                            }
                        }).fail((response, jqxhr, error) => {
                            toastr.error(error + ", contacte con el desarrollador");
                        });
                        return false; //This doesn't prevent the form from submitting.
                    }
                });
            });
        });
    };

    return exports;
});