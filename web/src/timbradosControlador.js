define(() => {
    var exports = {};
    exports.Timbrados = function () {
        spinneron();
        $("#inicio").load("vistas/timbrados.html", {}, function () {
            spinneroff();
            $(".active").removeClass("active");
            $(".config").parent().find("ul.nav").removeClass("nav nav-second-level collapse").addClass("nav nav-second-level collapse in");
            $(".config").parent().addClass("active");
            setTimeout(() => {
                $("#htab-1").addClass("active");
                $("#tab-1").addClass("active");
            }, 250);
            exports.ListarTimbrados();
            exports.ListarTimbradosVencidos();
        });
    };

    exports.ListarTimbrados = function () {
        $("#table-disponibles").DataTable({
            destroy: true,
            ajax: {
                type: "post",
                url: "timbradoControlador",
                data: {accion: "listar"}
            },
            columns: [
                {data: "id_timbrado"},
                {data: "numero_tim"},
                {data: "vencimiento_tim"},
                {data: null,
                    render: function (data, type, full, meta) {
                        return `<div class="btn-group">
                                    <a class="btn btn-primary btn-xs det" href="#/configuracion/timbrado/editar?_id=${data.id_timbrado}"><i class="far fa-edit"> Editar</i></a>
                                    <a class="btn btn-warning btn-xs clo" data-id="${data.id_timbrado}"><i class="fa fa-ban"> Inhabilitar</i></a>
                                </div>`;
                    }
                }
            ]
        });
    };

    exports.ListarTimbradosVencidos = function () {
        $("#tabla-vencidos").DataTable({
            destroy: true,
            ajax: {
                type: "post",
                url: "timbradoControlador",
                data: {accion: "vencidos"}
            },
            columns: [
                {data: "id_timbrado"},
                {data: "numero_tim"},
                {data: "vencimiento_tim"}
            ]
        });
    };

    exports.BuscarTimbrado = function (id) {
        $("#modal").load("vistas/modaltim.html", {}, function () {
            $.post("timbradoControlador", {
                accion: "buscar",
                id_timbrado: id
            }).done(function (response) {
                $("#Modal-tim").modal();
                $('input[name="numero_tim"]').val(response.numero_tim);
                $('input[name="vencimiento_tim"]').val(response.vencimiento_tim);
                $("#Modal-tim").on('hidden.bs.modal', function () {
                    window.history.back();
                });
            }).fail(function (response, jqxhr, error) {
                toastr.error(error);
            }).always((response, jqxhr) => {
                if (jqxhr === 'success') {
                    num();
                    $('.date input').datepicker({
                        format: "dd/mm/yyyy",
                        autoClose: true
                    });
                    exports.AgregarTimbrado(id);
                }
            });
        });
    };

    exports.AgregarTimbrado = function (id) {
        $("#form-tim").on('submit', function (e) {
            e.preventDefault();
            var fd = ConvertFormToJSON(this)
            fd.id_timbrado = id;
            $.post("timbradoControlador", {
                accion: "insertar",
                datos: JSON.stringify(fd)
            }).done(function (response) {
                if (response == 1) {
                    toastr.success("Guardado!");
                    $("#Modal-tim").modal("hide");
                }
            }).fail(function (response, jqxhr, error) {
                toastr.error(error);
            });
        });
    };

    exports.SelectTimbrado = function () {
        $.post("timbradoControlador", {
            accion: "select",
        }).done(function (response) {
            for (var i = 0; i < response.length; i++) {
                var option = document.createElement("option");
                option.setAttribute("value", response[i].id_timbrado);
                option.innerHTML = response[i].numero_tim;
                $('select[name="id_timbrado"]').append(option);
            }
        }).fail(function (response, jqxhr, error) {
            toastr.error(error);
        });
    };

    return exports;
});