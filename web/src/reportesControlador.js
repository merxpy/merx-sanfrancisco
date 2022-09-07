define(['sucursalesControlador'], function (sucursal) {
    var exports = {};
    exports.Reportes = function () {
        $("#inicio").load("vistas/reportes.html", function () {
            $(".level").parent().find("ul.nav").removeClass("nav nav-second-level collapse in").addClass("nav nav-second-level collapse");
            $(".active").removeClass("active");
            $('a[href="#/reportes"]').parent().addClass("active");

            $('img[src="img/calendar.png"]').parent().on('click', function () {
                ReporteResumen();
            });

            $('img[src="img/sales.png"]').parent().on('click', function () {
                ReporteCaja();
            });

            $('img[src="img/accounting.png"]').parent().on('click', function () {
                ReporteBalance();
            });

            $('img[src="img/dry-cleaning.png"]').parent().on('click', function () {
                ReportePrendas();
            });
        });
    };

    const ReporteCaja = function () {
        $("#modal").load("vistas/reporte_caja.html", function () {
            $("#modal-caja").modal();

            sucursal.SelectSucursales();

            $('.date input[name="start"]').datepicker({
                keyboardNavigation: false,
                forceParse: false,
                autoclose: true,
                format: "dd/mm/yyyy"
            });

            $('input[name="start"]').on('change', function () {
                $("#fin").attr("disabled", false);
                $('.date input[name="end"]').datepicker({
                    keyboardNavigation: false,
                    forceParse: false,
                    autoclose: true,
                    format: "dd/mm/yyyy"
                }).on('hide', function () {
                    if ($('input[name="start"]').data('datepicker').getDate() > $('input[name="end"]').data('datepicker').getDate()) {
                        $('input[name="end"]').val(moment().format('DD/MM/YYYY'));
                    }
                });
            });

            $.post("usuarioControlador", {
                accion: "select"
            }).done(function (response, jqxhr, error) {
                if (response.length) {
                    $(response).each(function (i) {
                        $('select[name="id_usuario"]').append(`<option value="${response[i].id_usuario}">${response[i].alias_usu}</option>`);
                    });
                } else {
                    toastr.info("El documento no tiene páginas");
                }
            }).fail(function (response, jqxhr, error) {
                toastr.error(response + ", contacte con el desarrollador");
            }).always(function () {
                ImprimirReporteCaja();
            });
        });
    };

    let ImprimirReporteCaja = function () {
        $("#form-caja").on('submit', function (e) {
            e.preventDefault();
            var newwindow = window.open("InformeCaja?inicio=" + $('input[name="start"]').val() + "&fin=" + $("#fin").val() + "&sucursal=" + $("#sucursal").val() + "&tipo_pago=" + $("select[name='tipo_pago']").val(), "", "width=1200,height=780");
            return newwindow;
        });
    };

    const ReporteResumen = function () {
        $("#modal").load("vistas/reporte_orden.html", function () {
            $("#modal-resumen").modal();

            $('.date input[name="start"]').datepicker({
                keyboardNavigation: false,
                forceParse: false,
                autoclose: true,
                format: "dd/mm/yyyy"
            });

            $("#inicio").on('change', function () {
                $("#fin").attr("disabled", false);
                $('.date input[name="end"]').datepicker({
                    keyboardNavigation: false,
                    forceParse: false,
                    autoclose: true,
                    format: "dd/mm/yyyy"
                }).on('hide', function () {
                    if ($('input[name="start"]').data('datepicker').getDate() > $('input[name="end"]').data('datepicker').getDate()) {
                        $('input[name="end"]').val(moment().format('DD/MM/YYYY'));
                    }
                });
            });



            $('select[name="id_cliente"]').selectize({
                placeholder: 'Buscar cliente ...',
                valueField: 'id_cliente',
                labelField: 'nombre_cli',
                searchField: ['nombre_cli', 'ndocumento_cli'],
                options: [],
                create: false,
                load: function (query, callback) {
                    if (!query.length)
                        return callback();
                    $.ajax({
                        url: 'clientesControlador',
                        type: 'POST',
                        data: {
                            q: query,
                            accion: "query"
                        },
                        error: function (response, jqxhr, error) {
                            console.log(response);
                            callback(error);
                        },
                        success: function (res) {
                            callback(res);
                        }
                    });
                }
            });
            ImprimirReporteResumen();
        });
    };

    let ImprimirReporteResumen = function () {
        $("#form-resumen").on('submit', function (e) {
            e.preventDefault();
            $("#form-resumen button:submit").attr("disabled", true);
            $.post('ResumenControlador', {
                id_cliente: $('select[name="id_cliente"]').val(),
                fecha_inicio: $('input[name="start"]').val(),
                fecha_fin: $("#fin").val(),
                accion: "validar"
            }).done((response) => {
                if (response === 0) {
                    $("#form-resumen button:submit").attr("disabled", false);
                    toastr.info("El documento no tiene páginas.");
                } else {
                    toastr.info("Aguarde un momento...");
                    var newwindow = window.open("ResumenControlador?accion=ver&cliente=" + $('select[name="id_cliente"]').val() + "&fecha_inicio=" + $("input[name='start']").val() + "&fecha_fin=" + $("#fin").val(), "", "width=1200,height=780");
                    $("#form-resumen button:submit").attr("disabled", false);
                    return newwindow;
                }
            }).fail((response, jqxhr, error) => {
                toastr.error(error, " contacte con el desarrollador.");
            });
        });
    };

    let ReporteBalance = function () {
        $("#modal").load("vistas/reporte_balance.html", function () {
            $("#modal-balance").modal();

            $("#form-balance").on('submit', function (e) {
                e.preventDefault();
                var newwindow = window.open("BalancesControlador?sucursal=" + $("#sucursal").val(), "", "width=1200,height=780");
                return newwindow;
            });
            sucursal.SelectSucursales();
        });
    };

    let ReportePrendas = function () {
        $("#modal").load("vistas/reporte_prendas.html", function () {
            $("#modal-prendas").modal();
            sucursal.SelectSucursales();
            SelectCategoria(1);
            $('.date input[name="start"]').datepicker({
                keyboardNavigation: false,
                forceParse: false,
                autoclose: true,
                format: "dd/mm/yyyy"
            });

            $("#inicio").on('change', function () {
                $('.date input[name="end"]').datepicker({
                    keyboardNavigation: false,
                    forceParse: false,
                    autoclose: true,
                    format: "dd/mm/yyyy"
                });
            });

            $("#form-prendas").on('submit', function (e) {
                e.preventDefault();
                var newwindow = window.open("Prendas?start=" + $("#fecha_inicio").val() + "&end=" + $("#fecha_fin").val() + "&estado=" + $("#estado").val() +
                        "&categoria=" + $("#select-categoria").val() + "&sucursal=" + $("#sucursal").val() + "&accion=" + $('input[name="accion"]:checked').val(), "", "width=1200,height=780");
                return newwindow;
            });
        });
    };

    return exports;
});