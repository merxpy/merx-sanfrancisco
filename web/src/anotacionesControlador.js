define(function () {
    var exports = {};

    exports.Notas = function () {
        exports.ListarNotas();
        exports.Calendario();
        $(".nota").on('click', function () {
            var el = this;
            el.setAttribute("disabled", true);
            exports.ModalNota();
        });
    };

    exports.ModalNota = function () {
        $("#modal").load("vistas/modalNota.html", function () {
            $("#modal-nota").modal();
            $('.date input').datepicker({
                format: "dd/mm/yyyy"
            });
            $('[name="color_anotacion"]').paletteColorPicker({
                colors: [
                    {"default": "#ddd"},
                    {"primary": "#2196F3"},
                    {"success": "#1c84c6"},
                    {"info": "#23c6c8"},
                    {"warning": "#f8ac59"},
                    {"danger": "#ed5565"}
                ],
                position: 'downside'
            });
            exports.AgregarNota();
        });
    };

    exports.Anotaciones = function () {
        $(".panel-heading").on('mouseenter', function () {
            $(this).find(".btn-group.hide").removeClass("hide");
        }).on('mouseleave', function () {
            $(this).find(".btn-group").addClass("hide");
        });
    };

    exports.AgregarNota = function () {
        $("#form-nota").on('submit', function (e) {
            e.preventDefault();
            toastr.clear();
            var fd = ConvertFormToJSON(this);
            fd.id_anotacion = $('input[name="titulo_anotacion"]').attr("data-id") ? $('input[name="titulo_anotacion"]').attr("data-id") : 0;
            fd.color_anotacion = $('input[name="color_anotacion"]').val() ? $('input[name="color_anotacion"]').val() : "default";
            $.post("anotacionesControlador", {
                accion: "insertar",
                datos: JSON.stringify(fd)
            }).done((response) => {
                if (response) {
                    toastr.success("¡Guardado!");
                    $("#modal-nota").modal("hide");
                    $('#calendar').fullCalendar('destroy');
                    exports.Calendario();
                } else {
                    toastr.info("Error interno del servidor, contacte con el desarrollador");
                }
            }).fail((response, jqxhr, error) => {
                toastr.error("Error, verifique que el servidor esté encendido o contacte con el desarrollador.");
            });
        }
        );

        $("#modal-nota").on('hidden.bs.modal', () => {
            $(".nota").attr("disabled", false);
            exports.ListarNotas();
        });
    }
    ;

    exports.ListarNotas = function () {
        $(".notes").html("");
        $.post("anotacionesControlador", {
            accion: "listar"
        }).done((response) => {
            if (!$.isEmptyObject(response)) {
                for (var i = 0; i < response.length; i++) {
                    $(".notes").append(exports.MostrarNotas(response[i]));
                }
            }
        }).fail((response, jqxhr, error) => {
            toastr.error(error + ", error interno del servidor, contacte con el desarrollador.");
        }).always(() => {
            exports.Anotaciones();
            exports.BuscarNota();
            exports.EliminarNota();
        });
    };

    exports.BuscarNota = function () {
        $(".editar").on('click', function () {
            var el = this;
            $("#modal").load("vistas/modalNota.html", function () {
                $.post("anotacionesControlador", {
                    accion: "buscar",
                    id: $(el).attr("data-id")
                }).done((response) => {
                    $('input[name="titulo_anotacion"]').attr("data-id", response.id_anotacion);
                    $('input[name="titulo_anotacion"]').val(response.titulo_anotacion);
                    $('textarea[name="contenido_anotacion"]').html(response.contenido_anotacion);
                    $('input[name="color_anotacion"]').val(response.color_anotacion);
                    $('input[name="vencimiento_anotacion"]').val(response.vencimiento_anotacion);
                }).fail((response, jqxhr, error) => {
                    toastr.error("Error, verifique que el servidor esté encendido o contacte con el desarrollador.");
                }).always(() => {
                    $('[name="color_anotacion"]').paletteColorPicker({
                        colors: [
                            {"default": "#ddd"},
                            {"primary": "#2196F3"},
                            {"success": "#1c84c6"},
                            {"info": "#23c6c8"},
                            {"warning": "#f8ac59"},
                            {"danger": "#ed5565"}
                        ],
                        position: 'downside'
                    });
                    $('.date input').datepicker({
                        format: "dd/mm/yyyy"
                    });
                    exports.AgregarNota();
                });
                $("#modal-nota").modal();
            });
        });
    };

    exports.EliminarNota = function () {
        $(".eliminar").on('click', function () {
            toastr.clear();
            var el = this;
            $.post("anotacionesControlador", {
                accion: "eliminar",
                id: $(el).attr("data-id")
            }).done((response) => {
                if (response) {
                    toastr.success("¡Eliminado!");
                    exports.ListarNotas();
                } else {
                    toastr.info("Error interno del servidor contacte con el desarrollador.");
                }
            }).fail((response, jqxhr, error) => {
                toastr.error("Error, verifique que el servidor esté encendido o contacte con el desarrollador.");
            });
        });
    };

    exports.MostrarNotas = function (nota) {
        var note = `<div class="col-lg-6">
                        <div class="panel panel-${nota.color_anotacion}">
                            <div class="panel-heading">
                                <div class="btn-group pull-right hide">
                                    <button class="btn btn-xs btn-${nota.color_anotacion} eliminar" data-id=${nota.id_anotacion}><i class="far fa-trash-alt"></i></button>
                                    <button class="btn btn-xs btn-${nota.color_anotacion} editar" data-id=${nota.id_anotacion}><i class="far fa-edit"></i></button>
                                </div>
                                <b>${nota.titulo_anotacion}</b>
                            </div>
                            <div class="panel-body">
                                <p>${nota.contenido_anotacion}</p>
                            </div>
                        </div>
                    </div>`;
        return note;
    };

    exports.Calendario = function () {
        $('#calendar').fullCalendar({
            header: {
                left: 'prev,next today',
                center: 'title',
                right: 'month,agendaWeek,agendaDay'
            },
            editable: true,
            droppable: true, // this allows things to be dropped onto the calendar
            drop: function () {
                // is the "remove after drop" checkbox checked?
                if ($('#drop-remove').is(':checked')) {
                    // if so, remove the element from the "Draggable Events" list
                    $(this).remove();
                }
            },
            dayRender: function (date, cell) {
                $.post("anotacionesControlador", {
                    accion: "listar"
                }).done(function (response) {
                    $.each(response, function (i) {
                        if (date.format("YYYY-MM-DD") === response[i].vencimiento_anotacion) {
                            if (response[i].color_anotacion === 'default') {
                                cell.css("background-color", "#f5f5f5");
                            } else if (response[i].color_anotacion === 'primary') {
                                cell.css("background-color", "#b2ebf2");
                            } else if (response[i].color_anotacion === 'success') {
                                cell.css("background-color", "#b3e5fc");
                            } else if (response[i].color_anotacion === 'info') {
                                cell.css("background-color", "#b2dfdb");
                            } else if (response[i].color_anotacion === 'warning') {
                                cell.css("background-color", "#f0f4c3");
                            } else if (response[i].color_anotacion === 'danger') {
                                cell.css("background-color", "#f8bbd0");
                            }
                        }
                    });
                }).fail(function (response, jqxhr, error) {
                    toastr.error(error + ", contacte con el desarrollador.");
                });
            }
        });
    };

    return exports;
});