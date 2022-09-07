define(['monedasControlador'], function (moneda) {
    var exports = {};

    exports.Caja = function (id) {
        spinneron();
        $("#inicio").load("vistas/caja.html", {}, function () {
            $(".level").parent().find("ul.nav").removeClass("nav nav-second-level collapse in").addClass("nav nav-second-level collapse");
            $(".active").removeClass("active");
            $('a[href="#/caja"]').parent().addClass("active");
            $('a[href="#tab-1"]').parent().addClass("active");
            $('#tab-1').addClass("active");
            spinneroff();
            $(".now").html(moment().format('DD/MM/YYYY'));
            exports.AbrirCaja();
            exports.ListarMovimientos("listar", id);
            exports.VerificarEstadoCaja().then(function (success) {
                let a = document.createElement("a");
                switch (success) {
                    case 0:
                        a.setAttribute("class", "btn btn-info btn-xs pull-right");
                        a.setAttribute("href", "#/caja/abrir");
                        a.innerHTML = `<i class="fas fa-book-open"></i> Abrir Caja`;
                        document.getElementById('cajabtn').before(a);
                        break;
                    default:
                        a.setAttribute("class", "btn btn-info btn-xs pull-right");
                        a.setAttribute("href", "#/caja/cerrar");
                        a.innerHTML = `<i class="fas fa-book"></i> Cerrar Caja`;
                        document.getElementById('cajabtn').before(a);
                        break;
                }
            });
        });
    };

    exports.AbrirCaja = function () {
        $("#cajabtn").on('click', function () {
            $("#modal").load("vistas/modal_movcaja.html", function () {
                $("#movcaja").modal();

                $(".miles").on('keypress keyup', function () {
                    this.value !== "" ? this.value = dot(Entero(this.value)) : 0;
                    $("#total_local").val("G. " + this.value);
                });

                $("#contar").on('click', function (e) {
                    e.preventDefault();
                    $.post("monedasControlador", {
                        accion: 'monedasxpais'
                    }).done((response) => {
                        $("#modal-ext").load("vistas/modal_cierre.html", function () {
                            $("#modal-cierre .modal-title strong").html("Contador de Dinero");
                            $("#modal-cierre .modal-footer").html(`<center>
                                                        <button type="button" class="btn btn-primary" id="finalizar" ><i class="fa fa-check text-warning"></i> Aceptar</button>
                                                    </center>`);
                            for (var i = 0; i < response.length; i++) {
                                $(".modal-body table tbody").append(`<tr>
                                                                        <td>${dot(response[i].valor_moneda)} Gs.</td>
                                                                        <td><input type="text" data-id="${response[i].id_moneda}" data-val="${response[i].valor_moneda}" class=" cantidad" name="cantidad_cierre" value="0" /></td>
                                                                        <td><input type="text" class="total" readonly="true" disabled="true" tabindex="-1" value="G. 0" /></td>                                
                                                                    </tr>`);
                            }
                            $(".modal-body table").append(`<tfoot>
                                        <td><b>Total</b></td>
                                        <td><input id="total" name="total" readOnly disabled="true" value="G. 0" /></td>                                        
                                   </tfoot>`);
                            $("#modal-cierre").modal({
                                backdrop: false
                            });
                            exports.BuscarSaldoAnterior();
                        });
                    }).fail((response, jqxhr, error) => {
                        toastr.error(error);
                    }).always(() => {
                        setTimeout(function () {
                            $(".cantidad").on('change', function () {
                                var tot = 0;
                                $(this).parent().parent().find('input.total').val("G. " + ($(this).attr("data-val") * ($(this).val() ? parseInt($(this).val()) : 0)));
                                $(".modal-body table tbody tr").each(function (i) {
                                    tot = tot + ($(this).find('input[name="cantidad_cierre"]').attr("data-val") * ($(this).find('input[name="cantidad_cierre"]').val() ? parseInt($(this).find('input[name="cantidad_cierre"]').val()) : 0));
                                });
                                $("#total").val("G. " + dot(tot));
                                $("#finalizar").on('click', function () {
                                    $("#monto").val($("#total").val().replace("G. ", ""));
                                    $("#total_local").val(($("#total").val()));
                                    $("#modal-cierre").modal('hide');
                                });
                            });
                        }, 1000);
                    });
                });

                $("#movimiento_caja").on('submit', function (e) {
                    e.preventDefault();
                    $('button[type="submit"]').attr("disabled", true);
                    if (parseInt($("#monto").val()) !== 0) {
                        $.post("cajaControlador", {
                            accion: 'apertura',
                            saldo_caja: Entero($("#total_local").val().replace("G. ", "")),
                            tipo_detalle: $('select[name="tipo_movimiento"]').val(),
                            concepto_detalle: $('textarea[name="concepto_detalle"]').val()
                        }).done(function (response) {
                            if (response === 1) {
                                toastr.success("¡Guardado!");
                                $("#movcaja").modal('hide');
                                $('#table-cja').DataTable().ajax.reload();
                                $('button[type="submit"]').attr("disabled", false);
                            } else {
                                toastr.error("¡Error inesperado, contacte con el desarrollador!");
                            }
                        }).fail(function (response, jqxhr, error) {
                            toastr.error("Error, " + error + " | " + response + " | " + jqxhr);
                        });
                    } else {
                        $("#monto").select();
                        toastr.warning("El monto debe ser mayor que 0");
                    }
                });
            });

        });
    };

    exports.VerificarEstadoCaja = function () {
        return new Promise(function (resolve, reject) {
            $.post("cajaControlador", {
                accion: "verificar"
            }).done(function (response) {
                resolve(response);
            }).fail(function (response, jqxhr, error) {
                reject(error);
            });
        });
    };

    exports.AccionesCaja = function (accion) {
        $("#inicio").load('vistas/insertCierreCaja.html', function () {
            let reg = new RegExp('^[0-9]+$');
            accion === "insertar" ? "" : $("#extracciones").css("display", "none");
            $(".miles").on('keypress keyup', function () {
                if (reg.test(Entero(this.value))) {
                    this.value !== "" ? this.value = dot(Entero(this.value)) : 0;
                } else {
                    this.value = 0;
                }
            });

            $(".count").on('change', function () {
                let suma = 0;
                $(".count").each(function () {
                    suma = parseInt(suma) + parseInt(Entero(this.value));
                });
                $("#contado").val(dot(suma));
                $("#diferencia").val(dot(Entero($("#contado").val()) - Entero($("#calculado").val())));

                if (this.getAttribute('data-val') === 'efectivo') {
                    $("#retefectivo").val(this.value).trigger('change');
                } else if (this.getAttribute('data-val') === 'tc') {
                    $("#rettc").val(this.value).trigger('change');
                } else if (this.getAttribute('data-val') === 'giros') {
                    $("#retgiros").val(this.value).trigger('change');
                } else if (this.getAttribute('data-val') === 'transferencias') {
                    $("#rettransferencias").val(this.value).trigger('change');
                }
            });

            $(".retired").on('change', function () {
                let suma = 0;
                $(".retired").each(function () {
                    suma = parseInt(suma) + parseInt(Entero(this.value));
                });
                $("#totalret").val(dot(suma));
            });

            $.post("arqueocajaControlador", {
                accion: "arqueo"
            }).done(function (response) {
                let sum = 0;
                $(response).each(function (i) {
                    sum = sum + response[i].calculado;
                });

                $("#calculado").val(dot(sum));
                $("#form-arqueo").on('submit', function (e) {
                    e.preventDefault();
                    GuardarArqueo(accion);
                });
            }).fail(function (response, jqxhr, error) {
                toastr.error(error + ", por favor contacte con el desarrollador");
            }).always(function () {
                $('.miles').each(function (i) {
                    $("#diferencia").val(dot(Entero($("#contado").val()) - Entero($("#calculado").val())));
                });
            });

            $.post("monedasControlador", {
                accion: 'monedasxpais'
            }).done((response) => {
                for (var i = 0; i < response.length; i++) {
                    $("div.col-md-4 div.ibox-content table.table.table-responsive tbody").append(
                            `<tr>
                                <td>${dot(response[i].valor_moneda)} Gs.</td>
                                <td><input type="text" data-id="${response[i].id_moneda}" data-val="${response[i].valor_moneda}" class="form-control cantidad" name="cantidad_cierre" value="0" onclick="$(this).select();" /></td>                            
                            </tr>`);
                }
            }).fail(function (response) {
                console.log(response);
            }).always(function () {
                setTimeout(function () {
                    $(".cantidad").on('change', function () {
                        let tot = 0;
                        $("div.col-md-4 div.ibox-content table.table.table-responsive tbody tr").each(function (i) {
                            tot = tot + ($(this).find('input[name="cantidad_cierre"]').attr("data-val") * ($(this).find('input[name="cantidad_cierre"]').val() ? parseInt($(this).find('input[name="cantidad_cierre"]').val()) : 0));
                        });
                        $('input[name="efectivo"]').val(dot(tot)).trigger('change');
                        $("#contado")
                    });
                }, 1000);
            });
        });
    };

    exports.ContabilizarDinero = function () {
        $.post("cajaControlador", {
            accion: "verificar"
        }).done(function (response) {
            if (response === 0) {
                $.post("cajaControlador", {
                    accion: 'saldo'
                }).done((response) => {
                    $("#modal").load("vistas/modal_cierre.html", function () {
                        $(".modal-title strong").html("Apertura de Caja");
                        $(".modal-footer").html(`<button type="submit" class="btn btn-primary">Abrir Caja</button>`);
                        for (var i = 0; i < response.length; i++) {
                            $(".modal-body table tbody").append(`<tr>
                                                                    <td>${dot(response[i].valor_moneda)} Gs.</td>
                                                                    <td><input type="text" name="cantidad_cierre" value="${response[i].cantidad_cierre}" data-id="${response[i].id_moneda}" data-val="${response[i].valor_moneda}" class="form-control cantidad"/></td>
                                                                </tr>`);
                        }
                        $(".modal-body table").append(`<tfoot>
                                        <td><b>Total General</b></td>
                                        <td><input id="total" class="form-control" name="total" readOnly></td>
                                   </tfoot>`);
                        $("#modal-cierre").modal();
                        exports.BuscarSaldoAnterior();
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
                            $("#total").val(dot(tot));
                        });

                        $("#form-cierre").on('submit', function (e) {
                            e.preventDefault();
                            exports.AperturaCaja("APERTURA");
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
                estado_caja: "A",
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

    exports.ListarMovimientos = function (accion, id) {
        $("#table-cja").DataTable({
            "pageLength": 100,
            destroy: true,
            order: [[0, "desc"]],
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
            columnDefs: [
                {
                    'targets': [0, 1],
                    'visible': false,
                    'searchable': false
                }
            ],
            ajax: {
                url: "cajaControlador",
                type: "post",
                data: {accion: accion, id: id}
            },
            columns: [
                {data: "id_detalle"},
                {data: "fecha_detalle",
                    render: function (data, type, full, meta) {
                        $("#fecha").html(escribirFecha(data));
                        $(".now").html(data ? moment(data).format('DD/MM/YYYY') : moment().format('DD/MM/YYYY'));
                        return moment(data).format('DD/MM/YYYY');
                    }
                },
                {data: "hora_detalle"},
                {data: "concepto_detalle"},
                {data: "descripcion"},
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
        $("#inicio").load("vistas/historialdecaja.html", {}, function () {
            $("#tabla-historial").DataTable({
                "pageLength": 100,
                destroy: true,
                order: [[0, "desc"]],
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
                columnDefs: [
                    {
                        'targets': [0],
                        'visible': false,
                        'searchable': false
                    }
                ],
                ajax: {
                    url: "cajaControlador",
                    type: "post",
                    data: {accion: 'listarcajas'}
                },
                columns: [
                    {data: "id_detalle"},
                    {data: "fecha_detalle",
                        render: function (data, type, full, meta) {
                            return `${moment(data).format('DD/MM/YYYY')}`;
                        }
                    },
                    {data: "hora_detalle"},
                    {data: "concepto_detalle",
                        render: function (data, type, full, meta) {
                            return `Movimientos de Caja del día ${moment(full.fecha_detalle).format('dddd DD [de] MMMM [del] YYYY')} desde las ${full.hora_detalle}`;
                        }
                    },

                    {data: null,
                        render: function (data, type, full, meta) {
                            return `<a href="#/caja?_id=${data.id_detalle}" class="btn btn-info btn-xs see"><i class="fa fa-eye"></i> Ver historial</a>`;
                        }
                    }
                ]
            });
        });
    };

    exports.AperturaCaja = function (estado) {
        let row = [];
        $(".modal-body table tbody tr").each(function (i) {
            row[i] = {
                id_moneda: $(this).find('input[name="cantidad_cierre"]').attr('data-id'),
                cantidad_cierre: $(this).find('input[name="cantidad_cierre"]').val() === "" ? 0 : $(this).find('input[name="cantidad_cierre"]').val(),
                valor_moneda: $(this).find('input[name="cantidad_cierre"]').attr('data-val'),
                monto_total: parseInt($(this).find('input[name="cantidad_cierre"]').val()) * parseInt($(this).find('input[name="cantidad_cierre"]').attr("data-val"))
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
                    $('input[data-id="' + response[i].id_moneda + '"]').val(response[i].cantidad_cierre);
                }
            }
        }).fail((response, jqxhr, error) => {
            toastr.error(error);
        });
    };

    let GuardarArqueo = function (accion) {
        toastr.info('Guardando...');
        $('button[type="submit"]').attr("disabled", true);
        let arqueo = [];
        let fd = {
            contado: Entero($("#contado").val()),
            calculado: Entero($("#calculado").val()),
            diferencia: Entero($("#diferencia").val()),
            retirado: Entero($("#totalret").val())
        };

        $("#calculos tbody tr").each(function (i) {
            if ($(this).find("input").attr("data-val") === "efectivo") {
                arqueo[i] = {
                    id_tipo_pago: 1,
                    contado: Entero($(this).find("input[data-val='efectivo']").val()),
                    retirado: Entero($("#retefectivo").val())
                };
            } else if ($(this).find("input").attr("data-val") === "cheque") {
                arqueo[i] = {
                    id_tipo_pago: 2,
                    contado: Entero($(this).find("input[data-val='cheque']").val()),
                    retirado: Entero($("#retcheque").val())
                };
            } else if ($(this).find("input").attr("data-val") === "tc") {
                arqueo[i] = {
                    id_tipo_pago: 3,
                    contado: Entero($(this).find("input[data-val='tc']").val()),
                    retirado: Entero($("#rettc").val())
                };
            } else if ($(this).find("input").attr("data-val") === "td") {
                arqueo[i] = {
                    id_tipo_pago: 4,
                    contado: Entero($(this).find("input[data-val='td']").val()),
                    retirado: Entero($("#rettc").val())
                };
            } else if ($(this).find("input").attr("data-val") === "giros") {
                arqueo[i] = {
                    id_tipo_pago: 5,
                    contado: Entero($(this).find("input[data-val='giros']").val()),
                    retirado: Entero($("#retgiros").val())
                };
            } else if ($(this).find("input").attr("data-val") === "transferencias") {
                arqueo[i] = {
                    id_tipo_pago: 6,
                    contado: Entero($(this).find("input[data-val='transferencias']").val()),
                    retirado: Entero($("#rettransferencias").val())
                };
            }
        });

        $.post('arqueocajaControlador', {
            accion: accion,
            datos: JSON.stringify(fd),
            detalle: JSON.stringify(arqueo),
            cambio: Entero($('input[name="cambio"]').val())
        }).done(function (response, jqxhr, error) {
            if (response) {
                if (accion === "insertar") {
                    $.ajax({
                        url: "http://192.168.100.9:8080/apiprinter/printer",
                        type: "post",
                        contentType: "application/json; charset=utf-8",
                        dataType: "json",
                        data: JSON.stringify(response),
                        success: function (data) {
                            console.log(data);
                        },
                        error: function (data, jqxhr, error) {
                            console.log(data);
                        }
                    });
                    toastr.success("Cierre de Caja realizado con éxito");
                } else {
                    toastr.success("Apertura de Caja realizada con éxito");
                }
            } else {
                console.log(error);
            }
            if (window.location.hash.includes("caja/abrir")) {
                $('#cambio').on('hidden.bs.modal', function () {
                    window.history.back();
                });
            } else {
                window.history.back();
            }
        }).fail(function (response, jqxhr, error) {
            console.log(error);
            toastr.error(error + ", contacte con el desarrollador.");
        });
    };

    exports.CambioInicial = function () {
        $("#modal").load('vistas/modal_cambio.html', function () {
            $("#cambio").modal();
            $(".modal-title").html(`Cambio inicial del día ${moment().format('DD/MM/YYYY')}`);
            miles();

            $("#form-cambio").on('submit', function (e) {
                e.preventDefault();
                GuardarArqueo('abrir');
                $("#cambio").modal('hide');
            });
        });
    }

    return exports;
});