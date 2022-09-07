define(['trabajoControlador'], function (trabajo) {
    var exports = {};
    exports.Cuentas = function () {
        spinneron();
        $(".level").parent().find("ul.nav").removeClass("nav nav-second-level collapse in").addClass("nav nav-second-level collapse");
        $("#inicio").load("vistas/cuentas.html", () => {
            spinneroff();
            ListarCuentas();
        });
    };

    exports.CobrarCuentas = function (id) {
        spinneron();
        $("#inicio").load("vistas/cobrarCuentas.html", function () {
            spinneroff();
            miles();
            exports.MetodoPago();
            $.post("pedidosControlador", {
                accion: "cobrar",
                id: id
            }).done(function (response, jqxhr, error) {
                let total = 0;
                $('input[name="nombre_cli"]').val(response[0].nombre_cli);
                $('input[name="ruc_cli"]').val(response[0].ndocumento_cli);
                $(response).each(function (i, e) {
                    $('tbody').append(`<tr>
                                            <td>${e.id_pedido}</td>
                                            <td>${e.fecha_ped}</td>
                                            <td>${dot(e.senha_ped)}</td>
                                            <td>${dot(e.total_ped)}</td>
                                        </tr>`);
                    total += e.total_ped;
                });
                $('tfoot').html(`<tr>
                                    <td colspan="3"><b class="pull-right">Total:</b></td>
                                    <td><b>${dot(total)}</b></td>
                                </tr>`);
                $('input[name="cobrar"]').val(dot(total));
                $('input[name="porcentaje"]').on('change', function () {
                    if ($('input[name="porcentaje"]').is(':checked')) {
                        $('input[name="descuento_ped"]').prop("type", "number").attr('max', 100).attr('min', 0);
                        $.post('clientesControlador', {
                            accion: 'buscar',
                            id_cliente: id
                        }).done(function (response) {
                            let descuentos = Descuento(total, response.descuento_cli);
                            $('input[name="descuento_ped"]').val(response.descuento_cli);
                            CalcularDescuentos(parseInt(total), parseInt(descuentos));
                            $('input[name="descuento_ped"]').on('change', function () {
                                descuentos = Descuento(total, this.value);
                                CalcularDescuentos(parseInt(total), parseInt(descuentos));
                            });
                        }).fail(function (response, jqxhr, error) {
                            toastr.error(`Algo no anda bien, ${error}`);
                        });
                    } else {
                        $('input[name="cobrar"]').val(dot(total));
                        $('input[name="descuento_ped"]').prop("type", "text");
                        $('input[name="descuento_ped"]').val("");
                        $('tfoot').html(`<tr>
                                            <td colspan="3"><b class="pull-right">Total:</b></td>
                                            <td><b>${dot(total)}</b></td>
                                        </tr>`);
                    }
                });

                $('input[name="descuento_ped"]').on('input', function () {
                    if (!$('input[name="porcentaje"]').is(':checked')) {
                        $('input[name="descuento_ped"]').off('change');
                        if (parseInt(this.value.replace(/[.+]/g, "")) > 0) {
                            CalcularDescuentos(total, this.value.replace(/[.+]/g, ""));
                        } else {
                            this.value = 0;
                            this.select();
                            CalcularDescuentos(total, 0);
                        }
                    }
                });
                RealizarCobro(id, total);
            }).fail(function (response, jqxhr, error) {
                toastr.error(`Algo no anda bien, ${error}`)
            });
        });
    };

    let CalcularDescuentos = function (total, descuentos) {
        if (!isNaN(parseInt(descuentos))) {
            $('input[name="cobrar"]').val(dot(total - parseInt(descuentos)));
        }

        $('tfoot').html(`<tr>
                            <td colspan="3"><b class="pull-right">Descuentos:</b></td>
                            <td><b>${!isNaN(parseInt(descuentos)) ? dot(descuentos) : 0}</b></td>
                        </tr>                        
                        <tr>
                            <td colspan="3"><b class="pull-right">Total:</b></td>
                            <td><b>${dot(total)}</b></td>
                        </tr>
                        <tr>
                            <td colspan="3"><b class="pull-right">Saldo:</b></td>
                            <td><b>${!isNaN(parseInt(descuentos)) ? dot(total - parseInt(descuentos)) : dot(total)}</b></td>
                        </tr>`);
    };

    let ListarCuentas = function () {
        $("#tabla-cuenta").DataTable({
            destroy: true,
            pageLength: 100,
            buttons: [
                {extend: 'excel', title: 'Cuentas',
                    exportOptions: {
                        columns: 'th:not(:last-child)'
                    }
                },
                {extend: 'pdf', title: 'Cuentas',
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
                type: 'post',
                url: 'pedidosControlador',
                data: {accion: 'cuentas'}
            },
            columns: [
                {data: 'id_cliente'},
                {data: 'nombre_cli'},
                {data: 'ndocumento_cli'},
                {data: 'nombre_sucursal'},
                {data: 'total_ped',
                    render: function (data, type, full, meta) {
                        return dot(data);
                    }
                },
                {data: 'senha_ped',
                    render: function (data, type, full, meta) {
                        return dot(data);
                    }
                },
                {data: 'saldo',
                    render: function (data, type, full, meta) {
                        return dot(data);
                    }
                },
                {data: null,
                    render: function (data, type, full, meta) {
                        if (full.saldo !== 0) {
                            return `<div class="btn-group">
                                    <a class="btn btn-primary btn-xs" href="#/cuentas/cobrar?_id=${data.id_cliente}"><i class="far fa-credit-card"></i> Cobrar</a>
                                </div>`;
                        } else {
                            return `<span class="label label-info"><i class="fas fa-ban"></i> No Aplica</span>`;
                        }
                    }
                }
            ]
        });
    };

    exports.MetodoPago = function () {
        $("#select-tipopago").on('change', function () {
            if (parseInt($(this).val()) === 2 || parseInt($(this).val()) === 6) {
                $("img[src='img/manager.png']").closest("div.row").hide();
                $("#efectivo").closest("div.form-group").hide();
                $(".aux").html(`<div class="form-group">
                                    <label>Banco</label>
                                    <select name="banco" class="form-control" required>
                                    </select>
                                </div>
                                <div class="form-group">
                                    <label>Nro de Comprobante</label>
                                    <input type="text" class="form-control m-b text-center miles" id="cupon" autocomplete="off" required>
                                </div>`);
                trabajo.SelectizeBanco();
                $("#aceptar button[type=\"submit\"]").attr("disabled", true);
                $('select[name="banco"]').on('change', function () {
                    this.value ? $("#cupon").val() ? $("#aceptar button[type=\"submit\"]").attr("disabled", false) : $("#aceptar button[type=\"submit\"]").attr("disabled", true) : $("#aceptar button[type=\"submit\"]").attr("disabled", true);
                });
                $("#cupon").on('input', function () {
                    this.value ? $('select[name="banco"]').val() ? $("#aceptar button[type=\"submit\"]").attr("disabled", false) : $("#aceptar button[type=\"submit\"]").attr("disabled", true) : $("#aceptar button[type=\"submit\"]").attr("disabled", true);
                });
            } else if (parseInt($(this).val()) === 3 || parseInt($(this).val()) === 4 || parseInt($(this).val()) === 5) {
                $("#aceptar button[type=\"submit\"]").attr("disabled", true);
                $(".aux").html(`<div class="form-group">
                                    <label>Nro de Comprobante</label>
                                    <input type="text" class="form-control" id="cupon" autocomplete="off" required>
                                </div>`);
                $("#cupon").on('input', function () {
                    this.value ? $("#aceptar button[type=\"submit\"]").attr("disabled", false) : $("#aceptar button[type=\"submit\"]").attr("disabled", true);
                });
            } else {
                $("#aceptar button[type=\"submit\"]").attr("disabled", false);
                $(".aux").html("");
                $("img[src='img/manager.png']").closest("div.row").show();
                $("#efectivo").closest("div.form-group").show();
                $("#efectivo").focus().select();
            }
        });
    };

    let RealizarCobro = function (id, total) {
        $("#form-cobrar").on('submit', function (e) {
            e.preventDefault();
            if ($("#inicio #select-tipopago").val() === "1") {
                $("#modal").load("vistas/modal_pago.html", function () {
                    $('#aceptar button[type="submit"]').attr("disabled", true);
                    $("#Modal-pago #select-tipopago").remove();
                    $("#Modal-pago .modal-title").html("Concretar Pago");
                    $("#Modal-pago").modal();
                    $("#pagar").html("<b>" + $('input[name="cobrar"]').val() + " Gs.</b>");
                    $("#letras").html("<b>" + NumeroALetras($('input[name="cobrar"]').val().replace(/[.+]/g, "")) + "</b>");
                    $("#customer").html($('input[name="nombre_cli"]').val());
                    $("#documento").html($('input[name="ruc_cli"]').val());
                    CalcularVuelto($('input[name="cobrar"]').val());
                    $("#aceptar").on('submit', function (e) {
                        e.preventDefault();
                        $("#aceptar button[type=\"submit\"]").attr("disabled", true);
                        let fd = ConvertFormToJSON($("#form-cobrar"));
                        fd.id_cliente = id;
                        fd.descuento_ped = $('input[name="descuento_ped"').val() !== "" ? $('input[name="porcentaje"]').is(":checked") ? Descuento(total, $('input[name="descuento_ped"').val().replace(/[.+]/g, "")) : fd.descuento_ped : 0;
                        fd.total_ped = fd.cobrar.replace(/[.+]/g, "");
                        GuardarCobro(fd);
                    });
                });
            } else {
                let fd = ConvertFormToJSON($("#form-cobrar"));
                fd.id_cliente = id;
                fd.id_pedido = id;
                fd.descuento_ped = $('input[name="descuento_ped"').val() !== "" ? $('input[name="porcentaje"]').is(":checked") ? Descuento(total, $('input[name="descuento_ped"').val().replace(/[.+]/g, "")) : fd.descuento_ped : 0;
                fd.total_ped = fd.cobrar.replace(/[.+]/g, "");
                GuardarCobro(fd);
            }
        });
    };

    let GuardarCobro = function (fd) {
        $.post('pedidosControlador', {
            accion: 'cobrarcuenta',
            datos: JSON.stringify(fd)
        }).done(function (response) {
            if (response === 1) {
                if (parseInt(fd.id_tipo_pago) === 1) {
                    $("#Modal-pago").modal('hide');
                    $("#Modal-pago").on('hidden.bs.modal', function () {
                        toastr.success("¡Cobro realizado con éxito!");
                        window.history.back();
                    });
                } else {
                    toastr.success("¡Cobro realizado con éxito!");
                    window.history.back();
                }
            } else {
                toastr.warning("No se ha podido realizar el Cobro");
            }
        }).fail(function (response, jqxhr, error) {
            toastr.error(`${error}, algo anda mal.`);
        });
    };

    let CalcularVuelto = function (total) {
        document.getElementById("efectivo").oninput = function () {
            var v = calcularVuelto(parseInt(this.value.replace(/[.+]/g, "")), parseInt(total.replace(/[.+]/g, "")));
            if (v < 100000) {
                $("#cambio").html("<b>" + dot(v) + "</b>");
            } else {
                var aux = this.value.replace(/[.+]/g, "");
                $("#efectivo").val(aux.substring(0, aux.length - 1));
            }
            if (this.value !== "") {
                parseInt(this.value.replace(/[.+]/g, "")) < parseInt(total.replace(/[.+]/g, "")) ? $("#aceptar button[type=\"submit\"]").attr("disabled", true) : $("#aceptar button[type=\"submit\"]").attr("disabled", false);
            } else {
                $("#aceptar button[type=\"submit\"]").attr("disabled", true);
            }
            var miles = dot(this.value.replace(/[.+]/g, ""));
            if (miles) {
                this.value = miles;
            } else {
                this.value = "";
            }
        };
    };

    return exports;
});