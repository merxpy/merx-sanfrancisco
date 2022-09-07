define(['paisesControlador', 'cajaControlador'], function (pais, c) {
    var exports = {};

    exports.Moneda = function () {
        spinneron();
        $("#inicio").load("vistas/monedas.html", function () {
            spinneroff();
            $(".active").removeClass("active");
            $(".utilidades").parent().find("ul.nav").removeClass("nav nav-second-level collapse").addClass("nav nav-second-level collapse in");
            $(".utilidades").parent().addClass("active");
            exports.ListarMonedas();
            $(".add").on('click', function () {
                $("#modal").load("vistas/modal_moneda.html", function () {
                    pais.SelectPais();
                    $("#modal-mon").modal();
                    $("#select-pais").select2({
                        allowClear: true,
                        dropdownParent: $("#select-pais").parent()
                    });
                    exports.AgregarMonedas(0);
                });
            });
        });
    };

    exports.ListarMonedas = function () {
        $("#table-mon").DataTable({
            destroy: true,
            ajax: {
                type: 'post',
                url: "monedasControlador",
                data: {accion: "listar"}
            },
            columns: [
                {data: "id_moneda"},
                {data: "valor_moneda",
                    render: (data, type, full, meta) => {
                        return dot(data);
                    }
                },
                {data: "nombre_pais"},
                {data: "moneda_pais"},
                {data: "iso_mon_pais"},
                {data: null,
                    render: (data, type, full, meta) => {
                        return `<div class="btn-group">
                                    <a href="#/monedas/editar?id=${data.id_moneda}" class="btn btn-primary btn-xs det"><i class="far fa-edit"> Editar</i></a>
                                    <a href="#/monedas/eliminar?id=${data.id_moneda}" class="btn btn-warning btn-xs nulled"><i class="fa fa-ban"> Eliminar</i></a>
                                </div>`;
                    }
                }
            ]
        });
    };

    exports.AgregarMonedas = function (id) {
        $("#form-mon").on('submit', function (e) {
            e.preventDefault();
            var fd = ConvertFormToJSON(this);
            fd.id_moneda = id;
            $.post("monedasControlador", {
                accion: "insertar",
                datos: JSON.stringify(fd)
            }).done((response) => {
                if (response) {
                    toastr.success("¡Guardado!");
                    $("#modal-mon").modal("hide");
                    exports.ListarMonedas();
                } else {
                    toastr.error("¡No se ha podido insertar!");
                }
            }).fail((response, jqxhr, error) => {
                toastr.warning(error);
            });
        });
    };

    exports.BuscarMonedas = function (id) {
        $.post("monedasControlador", {
            accion: "buscar",
            id: id
        }).done((response) => {
            $("#modal").load("vistas/modal_moneda.html", function () {
                pais.SelectPais();
                $('input[name="valor_moneda"]').val(response.valor_moneda);
                $('select[name="id_pais"]').val(response.id_pais);
                $("#modal-mon").modal();
                $("#select-pais").select2({
                    allowClear: true,
                    dropdownParent: $("#select-pais").parent()
                });
                $("#modal-mon").modal();
                exports.AgregarMonedas(id);
            });
        }).fail((response, jqxhr, error) => {
            toastr.warning(error);
        });
    };

    exports.EliminarMoneda = function (id) {
        $.post("monedasControlador", {
            accion: 'eliminar',
            id: id
        }).done((response) => {
            if (response) {
                toastr.success("¡Eliminado!");
                exports.ListarMonedas();
            } else {
                toastr.error("No se ha podido eliminar");
            }
        }).fail((response, jqxhr, error) => {
            toastr.error(error);
        });
    };

    exports.BuscarMonedaxPais = function () {
        $.post("monedasControlador", {
            accion: 'monedasxpais'
        }).done((response) => {
            $("#modal").load("vistas/modal_cierre.html", function () {
                for (var i = 0; i < response.length; i++) {
                    $(".modal-body table tbody").append(`<tr>
                                                <td>${dot(response[i].valor_moneda)} Gs.</td>
                                                <td><input data-id="${response[i].id_moneda}" data-val="${response[i].valor_moneda}" value="0" class="form-control cantidad" name="cantidad_cierre"></td>
                                            </tr>`);
                }
                $(".modal-body table").append(`<tfoot>
                                        <tr>
                                            <td><b>Total</b></td>
                                            <td><input class="form-control" name="total" readOnly></td>
                                        <tr>
                                        <tr>
                                            <td><b>Total según el sistema: </b></td>
                                            <td><input class="form-control" name="segun_sistema" readOnly></td>
                                        </tr>
                                   </tfoot>`);

                $.post("cajaControlador", {
                    accion: "ultimo"
                }).done((response) => {
                    $('input[name="segun_sistema"]').val(dot(response.saldo_actual));
                }).fail((response, jqxhr, error) => {
                    toastr.error(error);
                });
                $("#modal-cierre").modal();
            });
        }).fail((response, jqxhr, error) => {
            toastr.error(error);
        }).always(() => {
            setTimeout(function () {
                $(".cantidad").on('change', function () {
                    var tot = 0;
                    $(".modal-body table tbody tr").each(function (i) {
                        tot = tot + ($(this).find('input[name="cantidad_cierre"]').attr("data-val") * ($(this).find('input[name="cantidad_cierre"]').val() ? parseInt($(this).find('input[name="cantidad_cierre"]').val()) : 0));
                    });
                    $('input[name="total"]').val(dot(tot));
                });

                $("#parcial").on('click', function () {
                    this.setAttribute('disabled', true);
                    $.post('cajaControlador', {
                        accion: 'cierreparcial',
                        datos: JSON.stringify(Resultados())
                    }).done(function (response) {
                        if (response) {
                            toastr.success("Cierre Parcial realizado con éxito.");
                            $("#modal-cierre").modal("hide");
                        }
                    }).fail(function (response, jqxhr, error) {
                        toastr.error(`${error}, ha sucecido un error al realizar el cierre, Por favor contacte con el desarrollador.`);
                    });
                });

                $("#cierre").on('click', function () {
                    this.setAttribute('disabled', true);
                    $.post('cajaControlador', {
                        accion: 'cierre',
                        datos: JSON.stringify(Resultados())
                    }).done(function (response) {
                        if (response === 1) {
                            toastr.success(`Cierre de Caja realizado con éxito`);
                            $(".modal-title strong").html("Cambio Inicial del día Siguiente");
                            $(".modal-footer").html(`<button type="submit" class="btn btn-primary">Finalizar</button>`);
                            $('input[name="cantidad_cierre"]').val(0);
                            $("#form-cierre").on('submit', function (e) {
                                e.preventDefault();
                                console.log(Resultados());
                                $.post('cajaControlador', {
                                    accion: 'siguiente',
                                    datos: JSON.stringify(Resultados())
                                }).done(function (response) {
                                    if (response === 1) {
                                        toastr.success(`¡Finalizado!`);
                                        $("#modal-cierre").modal("hide");
                                    }
                                }).fail(function (response, jqxhr, error) {
                                    toastr.error(`${error}, ha sucecido un error al realizar el cierre, Por favor contacte con el desarrollador.`);
                                });
                            });
                        }
                    }).fail(function (response, jqxhr, error) {
                        toastr.error(`${error}, ha sucecido un error al realizar el cierre, Por favor contacte con el desarrollador.`);
                    });
                });

                $(".cancelar").on('click', function () {
                    $("#modal-cierre").modal("hide");
                });
            }, 1000);
        });
    };

    let Resultados = function () {
        const rows = [];
        $(".modal-body table tbody tr").each(function (i) {
            rows[i] = {
                id_moneda: $(this).find('input[name="cantidad_cierre"]').attr('data-id'),
                cantidad_cierre: $(this).find('input[name="cantidad_cierre"]').val() === "" ? 0 : $(this).find('input[name="cantidad_cierre"]').val(),
                valor_moneda: $(this).find('input[name="cantidad_cierre"]').attr('data-val'),
                monto_total: parseInt($(this).find('input[name="cantidad_cierre"]').val()) * parseInt($(this).find('input[name="cantidad_cierre"]').attr("data-val"))
            };
        });
        return rows;
    };

    return exports;
});

