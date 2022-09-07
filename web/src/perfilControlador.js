define(['usuariosControlador'], function (usuario) {
    var exports = {};

    exports.Perfil = function () {
        $("#inicio").load("vistas/perfil.html", function () {
            $.post("empleadosControlador", {
                accion: "buscar"
            }).done((response) => {
                $("#name").html(response.nombre_emp + " " + response.apellido_emp);
                $("#dbirth").html(response.fnac_emp);
                $("#dnumber").html(response.ndocumento_emp);
                $("#phone").html(response.telefono_emp);
                $("#cellphone").html(response.celular_emp);
                $("#direccion").html(response.direccion_emp);
                $("#panel").html(response.pagina_inicio === 'paneladministrador.html' ? 'Panel 1' : 'Panel 2');
            }).fail((response, jqxhr, error) => {
                toastr.error(error + ", contacte con el desarrollador");
            });
        });
    };

    exports.BuscarEmpleado = function () {
        $("#modal").load("vistas/modal_perfil.html", function () {
            $("#modal-perfil").modal();
            $('input[name="fnac_emp"]').datepicker({
                format: "dd/mm/yyyy"
            });
            $.post("empleadosControlador", {
                accion: "buscar"
            }).done((response) => {
                $('input[name="nombre_emp"]').val(response.nombre_emp);
                $('input[name="apellido_emp"]').val(response.apellido_emp);
                $('input[name="fnac_emp"]').val(response.fnac_emp);
                $('input[name="ndocumento_emp"]').val(response.ndocumento_emp);
                $('input[name="telefono_emp"]').val(response.telefono_emp);
                $('input[name="celular_emp"]').val(response.celular_emp);
                $('textarea[name="direccion_emp"]').html(response.direccion_emp);
                $('select[name="pagina_inicio"]').val(response.pagina_inicio);
            }).fail((response, jqxhr, error) => {
                toastr.error(error + ", contacte con el desarrollador");
            });
            $("#modal-perfil").on('hidden.bs.modal', () => {
                window.history.back();
            });
            exports.ActualizarPerfil();
        });
    };

    exports.ActualizarPerfil = function () {
        $("form").on("submit", function (e) {
            e.preventDefault();
            var fd = ConvertFormToJSON(this);
            $.post("empleadosControlador", {
                accion: "insertar",
                datos: JSON.stringify(fd)
            }).done((response) => {
                if (response) {
                    toastr.success("¡Actualizado!");
                    usuario.UsuarioActual();
                    $("#modal-perfil").modal('hide');
                } else {
                    toastr.error("No se ha podido actualizar. Contacte con el desarrollador");
                }
            }).fail((response, jqxhr, error) => {
                toastr.error(error + ", contacte con el desarrollador");
            });
        });
    };

    exports.CambiarPassword = function () {
        $("#modal").load("vistas/modal_cambiarpass.html", function () {
            $("#cpass").modal();
            $("#form").on('submit', function () {
                if (exports.ValidarPass()) {
                    $.post('usuarioControlador', {
                        accion: "actualizar",
                        oldpass: $("#old").val(),
                        newpass: $("#pwd").val()
                    }, function (response) {
                        if (response == 0) {
                            $(".noc").removeClass("hide").addClass("shake-horizontal shake-constant");
                            setTimeout(() => {
                                $(".noc").removeClass("shake-horizontal shake-constant");
                            }, 1000);
                        } else {
                            $(".noc").addClass('hide');
                            toastr.success("Guardado");
                            $("#cpass").modal('hide');
                        }
                    });
                }
            });
            $("#cpass").on('hidden.bs.modal', () => {
                window.history.back();
            });
        });
    };

    exports.ValidarPass = function () {
        if ($("#pwd").val().length < 4) {
            $("#pass span").remove();
            $("#con span").remove();
            $(".nada").removeClass("hide");
            $("#pass").append('<span class="glyphicon glyphicon-exclamation-sign form-control-feedback text-danger"></span>');
            $("#con").append('<span class="glyphicon glyphicon-exclamation-sign form-control-feedback text-danger"></span>');
            return false;
        } else {
            $(".nada").addClass("hide");
            if ($('#confirm').val() == $('#pwd').val()) {
                $("#pass span").remove();
                $("#con span").remove();
                $(".no").addClass("hide");
                $("#pass").append('<span class="glyphicon glyphicon-ok form-control-feedback text-success"></span>');
                $("#con").append('<span class="glyphicon glyphicon-ok form-control-feedback text-success"></span>');
                return true;
            } else {
                $("#pass span").remove();
                $("#con span").remove();
                $(".no").removeClass("hide");
                $("#pass").append('<span class="glyphicon glyphicon-exclamation-sign form-control-feedback text-danger"></span>');
                $("#con").append('<span class="glyphicon glyphicon-exclamation-sign form-control-feedback text-danger"></span>');
                return false;
            }
        }
    };

    return exports;
});