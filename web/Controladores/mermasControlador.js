define(function () {
    var exports = {};

    exports.Merma = function () {
        spinneron();

        $("#inicio").load("vistas/mermas.html", () => {
            spinneroff();
            $(".active").removeClass("active");
            $("a[href='#/inventario']").parent().find("ul.nav").removeClass("nav nav-second-level collapse in").addClass("nav nav-second-level collapse");
            $('a[href="#/inventario"]').closest("ul").closest("li").addClass("active");
            exports.ListarMermas();
        });
    };

    exports.ListarMermas = function () {
        $("#table-mermas").DataTable({
            "pageLength": 100,
            destroy: true,
            dom: '<"html5buttons"B>lTfgitp',
            buttons: [
                {extend: 'excel', title: 'Caja',
                    exportOptions: {
                        columns: 'th:not(:last-child)'
                    }
                },
                {extend: 'pdf', title: 'Caja',
                    exportOptions: {
                        columns: 'th:not(:last-child)'
                    }
                },
                {extend: 'print',
                    customize: function (win) {
                        $(win.document.body).addClass('white-bg');
                        $(win.document.body).css('font-size', '10px');

                        $(win.document.body).find('table')
                                .addClass('compact')
                                .css('font-size', 'inherit');
                    },
                    exportOptions: {
                        columns: 'th:not(:last-child)'
                    }
                }
            ],
            ajax: {
                url: "mermasControlador",
                type: "post",
                data: {accion: "listar"}
            },
            columns: [
                {data: "id_merma"},
                {data: "concepto_merma"},
                {data: null,
                    render: (data, type, full, meta) => {
                        return `<div class="btn-group">
                                    <a href="#/mermas/editar?id=${data.id_merma}" class="btn btn-primary btn-xs"><i class="far fa-edit"> Editar</i></a>
                                    <a href="#/mermas/eliminar?id=${data.id_merma}" class="btn btn-warning btn-xs"><i class="fa fa-ban"> Eliminar</i></a>
                                </div>`;
                    }
                }
            ]
        });
    };

    exports.BuscarMermas = function (id) {
        $.post("mermasControlador", {
            accion: "buscar",
            id: id
        }).done((response) => {
            $("#modal").load("vistas/modal_merma.html", () => {
                if (!$.isEmptyObject(response)) {
                    $('input[name="concepto_merma"]').val(response.concepto_merma);
                    $('select[name="tipo_merma"]').val(response.tipo_merma);
                }
                $("#modal-merma").modal();
                exports.AgregarMermas(id);
                $("#modal-merma").on('hidden.bs.modal', () => {
                    window.history.back();
                });
            });
        }).fail((response, jqxhr, error) => {
            toastr.error(error);
        });
    };

    exports.AgregarMermas = function (id) {
        $("#form-merma").on("submit", function (e) {
            e.preventDefault();
            var fd = ConvertFormToJSON(this);
            fd.id_merma = id;
            $.post("mermasControlador", {
                accion: "insertar",
                datos: JSON.stringify(fd)
            }).done((response) => {
                if (response) {
                    toastr.success("¡Guardado!");
                    $("#modal-merma").modal("hide");
                }
            }).fail((response, jqxhr, error) => {
                toastr.error(error);
            });
        });
    };

    exports.EliminarMerma = function (id) {
        $.post("mermasControlador", {
            accion: "eliminar",
            id: id
        }).done((response) => {
            if (response) {
                toastr.success("¡Eliminado!");
                window.history.back();
            } else {
                toastr.error("Error en el sevidor");
            }
        }).fail((response, jqxhr, error) => {
            toastr.error(error);
        });
    };

    exports.SelectMermas = function () {
        $.post("mermasControlador", {
            accion: "select"
        }).done((response) => {
            for (var i = 0; i < response.length; i++) {
                var option = document.createElement("option");
                option.setAttribute("value", response[i].id_merma);
                option.innerHTML = response[i].concepto_merma;
                $('select.merma').append(option);
            }
            $('select.merma').removeClass('merma');
        }).fail((response, jqxhr, error) => {
            toastr.error("Error en select2, contacte con el desarrollador");
        });
    };

    return exports;
});