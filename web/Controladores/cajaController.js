define(['monedasControlador'], function (m) {
    var exports = {};

    exports.Caja = function () {

        spinneron();
        $("#inicio").load("vistas/caja.html", {}, function () {
            $(".active").removeClass("active");
            $(".fondos").parent().find("ul.nav").removeClass("nav nav-second-level collapse").addClass("nav nav-second-level collapse in");
            $(".fondos").parent().addClass("active");
            exports.HistorialdeCaja();
            spinneroff();
            $("#fecha").html(escribirFecha(""));
            exports.ListarMovimientos("listar", "");
        });
    };

    exports.AbrirCaja = function () {
        $.post("cajaControlador", {
            accion: "verificar"
        }).done(function (response) {
            if (response === 0) {
                $.post("monedasControlador", {
                    accion: 'monedasxpais'
                }).done((response) => {
                    $("#modal-aux").load("vistas/modal_cierre.html", function () {
                        $(".modal-title strong").html("Apertura de Caja");
                        $(".modal-footer").html(`<button type="submit" class="btn btn-primary">Abrir Caja</button>`);
                        for (var i = 0; i < response.length; i++) {
                            $(".modal-body table tbody").append(`<tr>
                                                <td>${dot(response[i].valor_moneda)} Gs.</td>
                                                <td><input data-id="${response[i].id_moneda}" data-val="${response[i].valor_moneda}" class="form-control cantidad" name="cantidad"></td>
                                            </tr>`);
                        }
                        $(".modal-body table").append(`<tfoot>
                                        <td><b>Total</b></td>
                                        <td><input id="total" class="form-control" name="total" readOnly></td>
                                   </tfoot>`);
                        $("#modal-cierre").modal({
                            backdrop: 'static',
                            keyboard: false
                        });
                        exports.BuscarSaldoAnterior();
                    });
                }).fail((response, jqxhr, error) => {
                    toastr.error(error);
                }).always(() => {
                    setTimeout(function () {
                        $(".cantidad").on('change', function () {
                            var tot = 0;
                            $(".modal-body table tbody tr").each(function (i) {
                                tot = tot + ($(this).find('input[name="cantidad"]').attr("data-val") * ($(this).find('input[name="cantidad"]').val() ? parseInt($(this).find('input[name="cantidad"]').val()) : 0));
                            });
                            $("#total").val(dot(tot));
                        });
                        $("#form-cierre").on('submit', function (e) {
                            e.preventDefault();
                            exports.AperturaCaja("A");
                        });
                    }, 1000);
                });
            }
        });
    };

    exports.SaldoCaja = function () {
        $("#form-saldo").submit(function (e) {
            e.preventDefault();
            $.post("cajaControlador", {
                accion: "apertura",
                saldo_caja: Entero($("#saldo").val())
            }).done(function (response) {
                if (response == 1) {
                    toastr.success("¡Listo!");
                    $("#modal-cja").modal("hide");
                } else {
                    toastr.error("Error Inesperado!");
                }
            }).fail(function (response, jqxhr, error) {
                toastr.error(error);
            });
        });
    };

    exports.ListarMovimientos = function (accion, fecha) {
        $("#table-cja").DataTable({
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
                url: "cajaControlador",
                type: "post",
                data: {accion: accion, fecha: fecha}
            },
            columns: [
                {data: "hora_caja"},
                {data: "concepto_caja"},
                {data: "saldo_anterior",
                    render: function (data, type, full, meta) {
                        return dot(data);
                    }
                },
                {data: "debe_caja",
                    render: function (data, type, full, meta) {
                        return dot(data);
                    }
                },
                {data: "haber_caja",
                    render: function (data, type, full, meta) {
                        return dot(data);
                    }
                },
                {data: "saldo_actual",
                    render: function (data, type, full, meta) {
                        return dot(data);
                    }
                }
            ]
        });
    };

    exports.HistorialdeCaja = function () {
        $(".cajacrumb").on('click', function () {
            $("#inicio").load("vistas/historialdecaja.html", {}, function () {

                $.post("cajaControlador", {
                    accion: "todos"
                }, function (response) {
                    var eventos = [];
                    for (var i = 0; i < response.length; i++) {
                        eventos[i] = {
                            title: response[i].concepto_caja,
                            start: formatDate(response[i].fecha_caja + " " + response[i].hora_caja)
                        };
                    }

                    $('#calendar').fullCalendar({
                        header: {
                            left: 'prev,next today',
                            center: 'title',
                            right: 'month,agendaWeek,agendaDay'
                        },
                        navLinks: true,
                        events: eventos,
                        navLinkDayClick: function (date, jsEvent) {
                            spinneron();
                            $("#inicio").load("vistas/caja.html", {}, function () {
                                spinneroff();
                                exports.HistorialdeCaja();
                                $("#fecha").html(escribirFecha(date.format()));
                                exports.ListarMovimientos("buscar", date.format());
                            });
                        }
                    });
                }).fail(function (response, jqxhr, error) {
                    toastr.error(error);
                });
            });
        });
    };

    exports.AperturaCaja = function (estado) {
        var row = [];
        $(".modal-body table tbody tr").each(function (i) {
            row[i] = {
                id_moneda: $(this).find('input[name="cantidad"]').attr('data-id'),
                cantidad: $(this).find('input[name="cantidad"]').val() === "" ? 0 : $(this).find('input[name="cantidad"]').val(),
                monto_total: parseInt($(this).find('input[name="cantidad"]').val()) * parseInt($(this).find('input[name="cantidad"]').attr("data-val"))
            };
        });

        $.post("cajaControlador", {
            accion: "apertura",
            saldo_caja: Entero($("#total").val()),
            estado_caja: estado,
            datos: JSON.stringify(row)
        }).done((response) => {
            if (response) {
                toastr.success("¡Listo!");
                $("#modal-cierre").modal('hide');
            } else {
                toastr.error("No se ha podido realizar la transacción");
            }
        }).fail((response, jqxhr, error) => {
            toastr.error(error);
        });
    };

    exports.BuscarSaldoAnterior = function () {
        $.post("cajaControlador", {
            accion: "saldo"
        }).done((response) => {
            if (!$.isEmptyObject(response)) {
                $("#total").val(dot(response[0].saldo_actual));
                for (var i = 0; i < response.length; i++) {
                    $('input[data-id="' + response[i].id_moneda + '"]').val(response[i].cantidad);
                }
            }
        }).fail((response, jqxhr, error) => {
            toastr.error(error);
        });
    };

    return exports;
});