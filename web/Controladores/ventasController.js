function ventas() {
    spinneron();
    $("#inicio").load("vistas/ventas.html", {}, function () {
        spinneroff();
        $(".active").removeClass("active");
        $(".level").parent().find("ul.nav").removeClass("nav nav-second-level collapse in").addClass("nav nav-second-level collapse");
        $(".sales").parent().addClass("active");
        listarVentas();
        anularVenta("anular");
        PermisosRender(5, (p) => {
            if (p.agregar === 'N') {
                $('a[href="#/ventas/nuevo"]').remove();
            }
        });
    });
}

function nuevaVenta() {
    $("#inicio").load("vistas/insertVenta.html", {}, function () {
        var date = new Date();
        var d = date.getDate();
        var m = date.getMonth() + 1;
        var y = date.getFullYear();
        $("#fecha").val(toDate(d) + "/" + toDate(m) + "/" + y);
//        $("#ruc_cli").on('blur', function () {
//            $("input.chan.cod").focus();
//        });
        AgregarArticulosFast('VENTAS', (cod) => {
            verificarCodigoVen(cod);
        });
        //buscarClienteVen();
        buscarArticulosVenta();
        date_time("date", "time");
        currentUser();
        searchCustomer();

        $("#buscar").on('submit', function (e) {
            e.preventDefault();
            verificarCodigoVen($("#codigo").val());
            $("#codigo").val("");
        });
        UltimoNroFactura();
        agregarVenta();
        $(".touchspin1").TouchSpin({
            buttondown_class: 'btn btn-white',
            buttonup_class: 'btn btn-white'
        });
        $('input[name="cantidad"]').on('input change', function () {
            if (parseInt(this.value) <= 0 || isNaN(parseInt(this.value))) {
                this.value = 1;
                this.select();
            }
        });
    });
}
function searchCustomer() {
    return $("#cli_name").selectize({
        placeholder: "Buscar nombre del cliente",
        valueField: 'id_cliente',
        labelField: 'nombre_cli',
        searchField: ['nombre_cli'],
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
                    callback(error);
                },
                success: function (res) {
                    callback(res);
                }
            });
        }, onChange: function (value) {
            if ($("#cli_name").val() !== "") {
                $.post("clientesControlador", {
                    accion: "buscar",
                    id_cliente: value
                }).done((response) => {
                    $("#ruc_cli").val(response.ndocumento_cli + "-" + response.ruc_dv)
                }).fail((response, jqxhr, error) => {
                    console.log(error);
                });
            }
        }

    });
}
function listarVentas() {
    $("#table-ventas").DataTable({
        pageLength: 100,
        destroy: true,
        order: [[0, 'desc']],
        columnDefs: [
            {type: 'date-uk', targets: 1}
        ],
        dom: '<"html5buttons"B>lTfgitp',
        buttons: [
            {extend: 'excel', title: 'Ventas',
                exportOptions: {
                    columns: 'th:not(:last-child)'
                }
            },
            {extend: 'pdf', title: 'Ventas',
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
            type: "post",
            url: "ventasControlador",
            data: {accion: "listar"}
        },
        columns: [
            {data: "nfactura_venta",
                render: function (data, type, full, meta) {
                    return cero(data);
                }
            },
            {data: "fecha_venta"},
            {data: "total_venta",
                render: function (data, type, full, meta) {
                    return dot(data);
                }
            },
            {data: "tipo_venta",
                render: function (data, type, full, meta) {
                    if (data == 0) {
                        return '<label class="label label-warning">Contado</label>';
                    } else {
                        return '<label class="label label-danger">Crédito</label>';
                    }
                }
            },
            {data: "razon_social",
                render: function (data, type, full, meta) {
                    return data != "-" ? data : full.nombre_cli;
                }
            },
            {data: null,
                render: function (data, type, full, meta) {
                    return `<div class="btn-group">
                            <a onclick="FuncionImprimir(${data.id_venta})" class="btn btn-default btn-xs see"><i class="fa fa-eye"> Ver</i></a>
                            <a href="javascript:void(0)" onclick="Reimprimir(${data.id_venta});" class="btn btn-primary btn-xs"><i class="fa fa-print"> Reimprimir</i></a>
                            <a class="btn btn-info btn-xs clo" data-id="${data.id_venta}"><i class="fa fa-ban"></i> Anular</a>
                            </div>`;
                }
            }
        ],
        initComplete: function () {
            PermisosRender(5, (p) => {
                if (p.eliminar === 'N') {
                    $('.clo').remove();
                }
            });
        }
    });
}

function currentUser() {
    $.post("usuarioControlador", {accion: "cajero"}, function (response) {
        $("#caj").html(response.nombre_emp + " " + response.apellido_emp);
    });
}

/*function buscarClienteVen() {
 completarClientes(function (nom, ruc) {
 $('#cli_name').typeahead('destroy');
 $('#ruc_cli').typeahead('destroy');
 $("#cli_name").typeahead({
 source: nom,
 autoSelect: true
 });
 $("#ruc_cli").typeahead({
 source: ruc,
 autoSelect: true
 });
 $("#cli_name").change(function () {
 var current = $(this).typeahead("getActive");
 $("#ruc_cli").val(current.ruc_cli);
 $(this).attr("data-id", current.id_cliente);
 });
 $("#ruc_cli").change(function () {
 var current = $(this).typeahead("getActive");
 $("#cli_name").val(current.nombre_cli);
 $("#cli_name").attr("data-id", current.id_cliente);
 });
 });
 }*/

function buscarArticulosVenta() {
    $("#b-modal").on('click', function () {
        $("#modal").load('vistas/buscarArticulos.html', {}, function () {
            $("#modal-b").modal();
            num();
            miles();
            $("#table-art").DataTable({
                destroy: true,
                pageLength: 5,
                ajax: {
                    url: "stockControlador",
                    type: "POST",
                    data: {accion: "listarhabilitados"}
                },
                columnDefs: [
                    {
                        className: "text-center",
                        targets: [3]
                    }
                ],
                columns: [
                    {data: "codigo_ar"},
                    {data: "nombre_ar"},
                    {data: "nombre_marca",
                        render: function (data, type, full, meta) {
                            if (!data) {
                                return "NO POSEE";
                            } else {
                                return data;
                            }
                        }
                    },
                    {data: "cantidad_stock",
                        render: function (data, type, full, meta) {
                            console.log(data === "0");
                            return data === "0" ? "-" : data;
                        }
                    },
                    {data: null,
                        render: function (data, type, full, meta) {
                            return '<input type="number" class="form-control cant" value="1" style="width:60px;">';
                        }
                    },
                    {data: null,
                        render: function (data, type, full, meta) {
                            return '<input type="text" class="form-control miles num" value=' + dot(full.precio_venta_ar) + '>';
                        }
                    },
                    {data: null,
                        render: function (data, type, full, meta) {
                            return '<a class="text text-info" data-id=' + data.id_articulo + '><i class="far fa-check-circle fa-2x"></i></a>';
                        }
                    }
                ]
            });
            agregarFilaAr();
            $('#modal-b').on('hidden.bs.modal', function (e) {
                $("#cantidad").val(1);
            });
        });
    });
}



function verificarCodigoVen(cod) {
    $('#cuerpo').append('<tr id="img">' +
            '<td colspan="5">' +
            '<center>' +
            '<img src="img/spinner/Rolling-1s-200px.gif" style="width: 30px; height: 30px;" alt=""/>' +
            ' Buscando...' +
            '</center>' +
            '</td>' +
            '</tr>');
    $.post("articuloControlador", {accion: "verificar", cod: cod}, function (response) {
        $("#img").remove();
        if (response.id_articulo) {
            if (!document.getElementById(response.id_articulo)) {
                $("#cuerpo").append('<tr>' +
                        '<td class="rowNumber"></td>' +
                        '<td><span class="code" id=' + response.id_articulo + '>' + response.codigo_ar + '</span></td>' +
                        '<td><input type="text" class="form-control cant" value=' + $("#cantidad").val() + ' min="1" required></td>' +
                        '<td><span class="desc">' + response.nombre_ar + '</span></td>' +
                        '<td><input type="text" class="form-control prec" value="' + dot(response.precio_venta_ar) + '" required></td>' +
                        '<td><input type="text" class="form-control descuento" value="0" ></td>' +
                        '<td><span class="iva">' + response.iva_ar + '</span></td>' +
                        '<td><span class="subtotal">' + dot(response.precio_venta_ar * $("#cantidad").val()) + '</span></td>' +
                        '<td><a class="text-danger"><i class="glyphicon glyphicon-trash"></i></a></td>' +
                        '</tr>');
                calcularTotalVenta();
                calcularSubtotalVenta();
                SetRowNumber();
                $(".touchspin2").TouchSpin({
                    buttondown_class: 'btn btn-white',
                    buttonup_class: 'btn btn-white'
                });
            } else {
                var tr = $("#" + response.id_articulo).parent().parent();
                var c = parseInt(tr.find("td input.cant").val()) + parseInt($("#cantidad").val());
                var subt = response.precio_venta_ar * c;
                tr.find("td input.cant").val(c);
                tr.find("td span.subtotal").html(dot(subt));
                calcularTotalVenta();
            }
        } else {
            toastr.clear();
            toastr.warning("Producto no encontrado");
        }
        $("#cuerpo tr").on('click', "td a", function (e) {
            e.preventDefault();
            $(this).closest("tr").remove();
            calcularTotalVenta();
            SetRowNumber();
            $("#cuerpo_info").html(`Mostrando ${$("#cuerpo tr").length} registro/s`);
            return false;
        });
        $("#cuerpo_info").html(`Mostrando ${$("#cuerpo tr").length} registro/s`);
    });
    if ($('#cuerpo tr').length <= 0) {
        $('button.save').attr("disabled", true);
    } else {
        $('button.save').attr("disabled", false);
        $("div.alert.alert-danger").remove();
    }

}

function agregarVenta() {
    $("#form-venta").submit(function (e) {
        e.preventDefault();
        if ($("#cuerpo tr").length > 0) {
            $("button.save").attr("disabled", true);
            if (!$("#cli_name").val()) {
                alertify.confirm('¿Guardar como Cliente Ocasional?', '', function () {
                    guardar(0, 0);
                }, function () {
                    $('button.save').attr("disabled", false);
                });
            } else {
                guardar(0, 0);
            }

        } else {
            $('button.save').attr("disabled", true);
            $("div.table-responsive").append(`<center><div class="alert alert-danger">Debes seleccionar almenos un articulo.</div></center>`);
        }
    });
}

function anularVenta(accion) {
    $("#table-ventas").on('click', '.clo', function () {
        $(this).attr("disabled", true);
        var id = $(this).attr("data-id");
        $.post("ventasControlador", {
            accion: accion,
            id_venta: id
        }).done(function (response) {
            if (response == 1) {
                if (accion == "anular") {
                    toastr.clear();
                    toastr.success("¡Anulado!");
                    listarVentas();
                } else if (accion == "reestablecer") {
                    listarVentasInhabilitadas();
                }
            } else {
                toastr.error("Error Inesperado");
            }
        }).fail(function (response, jqxhm, error) {
            toastr.error(error);
        });
    });
}

function listarVentasInhabilitadas() {
    spinneron();
    $("#inicio").load("vistas/ventas.html", {}, function () {
        spinneroff();
        $(".active").removeClass("active");
        $("a[href='#/anulados/articulos']").parent().find("ul.nav").removeClass("nav nav-second-level collapse in").addClass("nav nav-second-level collapse");
        $('a[href="#/anulados/articulos"]').closest("ul").closest("li").addClass("active");
        anularVenta("reestablecer");
        $('a[href="#/ventas/nuevo"]').remove();
        $("#table-ventas").DataTable({
            "pageLength": 100,
            destroy: true,
            dom: '<"html5buttons"B>lTfgitp',
            buttons: [
                {extend: 'excel', title: 'Ventas Inhabilitados'},
                {extend: 'pdf', title: 'Ventas Inhabilitados'},
                {extend: 'print',
                    customize: function (win) {
                        $(win.document.body).addClass('white-bg');
                        $(win.document.body).css('font-size', '10px');

                        $(win.document.body).find('table')
                                .addClass('compact')
                                .css('font-size', 'inherit');
                    }
                }
            ],
            ajax: {
                type: "post",
                url: "ventasControlador",
                data: {accion: "inhabilitados"},
            },
            columns: [
                {data: "nfactura_venta",
                    render: (data, type, full, meta) => {
                        return cero(data);
                    }
                },
                {data: "fecha_venta"},
                {data: "total_venta"},
                {data: "tipo_venta",
                    render: function (data, type, full, meta) {
                        if (data == 0) {
                            return '<label class="label label-warning">Contado</label>';
                        } else {
                            return '<label class="label label-danger">Crédito</label>';
                        }
                    }
                },
                {data: "nombre_cli"},
                {data: null,
                    render: function (data, type, full, meta) {
                        return '<div class="btn-group"><a class="btn btn-primary btn-xs clo" data-id="' + data.id_venta + '"><i class="fa fa-check"></i> Restablecer</a></div>'
                    }
                }
            ]
        });
    });
}

function obtenerNumero(callback) {
    $.post("ventasControlador", {
        accion: "nuevo"
    }).done(function (response) {
        callback(response);
    }).fail(function (response, jqxhr, error) {
        toastr.error(error);
    });
}

function vuelto() {
    $("#modal").load("vistas/modal_pago.html", {}, function () {
        $("#Modal-pago").modal();
        $("#customer").html($("#cli_name").val());
        $("#documento").html($("#ruc_cli").val());
        $("#pagar").html("<b>" + $("#tot").html() + " Gs.</b>");
        $("#letras").html("<b>" + NumeroALetras(Entero($("#tot").html())) + "</b>");
        $("#efectivo").val($("#tot").html());
        $('#Modal-pago').on('shown.bs.modal', function () {
            $("#efectivo").focus().select();
        });

        modo_pago();

        document.getElementById("efectivo").oninput = function () {
            var v = calcularVuelto(parseInt(this.value.replace(/[.+]/g, "")), parseInt($("#tot").html().replace(/[.+]/g, "")));
            if (v < 100000) {
                $("#cambio").html("<b>" + dot(v) + "</b>");
            } else {
                var aux = Entero(this.value);
                $("#efectivo").val(aux.substring(0, aux.length - 1));
            }
            if (this.value !== "") {
                parseInt(this.value.replace(/[.+]/g, "")) < parseInt($("#tot").html().replace(/[.+]/g, "")) ? $("#aceptar button[type=\"submit\"]").attr("disabled", true) : $("#aceptar button[type=\"submit\"]").attr("disabled", false);
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
        $("#aceptar").on('submit', function (e) {
            $('#aceptar button[type="submit"]').attr("disabled", true);
            e.preventDefault();
            guardar(0, 0);
        });
        $("button.close").on('click', function (e) {
            $("button.save").attr("disabled", false);
        });
    });
}

function calcularVuelto(pag, tot) {
    var vuelto = pag - tot;
    return vuelto;
}


function guardar(id, accion) {
    let detalle = [];
    let id_pedido = null;
    $("#cuerpo tr").each(function (i) {
        detalle[i] = {
            id_articulo: $(this).find("td span").attr("id"),
            cantidad: $(this).find("td input.cant").val(),
            precio: Entero($(this).find("td input.prec").val()),
            subtotal: Entero($(this).find("td span.subtotal").html()),
            descuento: Entero($(this).find("td input.descuento").val())
        };
    });
    if (window.location.hash.includes("servicios")) {
        id_pedido = window.location.hash.substring(window.location.hash.indexOf("=") + 1);
    } else if (window.location.hash.includes("historial")) {
        id_pedido = $("#cuerpo").attr('data-id')
    }
    $.post("ventasControlador", {
        id: id,
        accion: accion,
        id_cliente: $("#cli_name").val() === "" ? 1 : $("#cli_name").val(),
        nfactura_venta: parseInt($('input[name="factura_numero"]').val()),
        tipo_venta: $('input[name="tipo_venta"]:checked').val(),
        total_iva_venta: Entero($("#totiva").html()),
        total_venta: Entero($("#tot").html()),
        total_descuento: Entero($("#totaldescuento").html()),
        totaliva10: Entero($("#i10").html()),
        totaliva5: Entero($("#i5").html()),
        id_pedido: id_pedido,
        detalle_venta: JSON.stringify(detalle)
    }, function (response) {
        if (response) {
            $("#Modal-pago").modal("hide");
            toastr.success("¡GUARDADO!");
            window.swal({
                title: "¿Desea imprimir la factura?",
                text: "",
                type: "info",
                showCancelButton: true,
                confirmButtonColor: "#DD6B55",
                confirmButtonText: "No Imprimir",
                cancelButtonText: "Imprimir",
                closeOnConfirm: false,
                closeOnCancel: false
            }, function (rpta) {
                if (rpta) {
                    swal.close();
                    if (window.location.hash.includes('ventas')) {
                        nuevaVenta();
                    } else {
                        window.history.back();
                    }
                } else {
                    swal({html: true,
                        title: `<div class="progress progress-striped active">
                                    <div style="width: 100%" aria-valuemax="100" aria-valuemin="0" aria-valuenow="75" role="progressbar" class="progress-bar progress-bar-info">
                                        <span class="sr-only">40% Complete (success)</span>
                                    </div>
                                </div>`,
                        text: "Esto puede tardar varios minutos. Por favor espere...",
                        showConfirmButton: false,
                        allowOutsideClick: false
                    });
                    if (response.length > 1) {
                        response.sort(function (a, b) {
                            return b - a;
                        });
                        $.each(response, function (i) {
                            console.groupCollapsed();
                            console.log(response[i]);
                            console.groupEnd();
                            $.post("merx", {
                                accion: "imprimir",
                                parametro: response[i]
                            }).done(function (response) {
                                $.ajax({
                                    url: "http://localhost:5000/printer",
                                    type: "post",
                                    contentType: "application/json; charset=utf-8",
                                    dataType: "json",
                                    data: JSON.stringify(response),
                                    success: function (data) {
                                        swal.close();
                                        window.history.back();
                                    },
                                    error: function (data, jqxhr, error) {
                                        console.log(data);
                                    }
                                });
                            }).fail(function (response, jqxhr, error) {
                                toastr.error(error, ", por favor contacte con el desarrollador.");
                            });
                        });
                    } else {
                        $.post("merx", {
                            accion: "imprimir",
                            parametro: response
                        }).done(function (response) {
                            $.ajax({
                                url: "http://localhost:5000/printer",
                                type: "post",
                                contentType: "application/json; charset=utf-8",
                                dataType: "json",
                                data: JSON.stringify(response),
                                success: function (data) {
                                    swal.close();
                                    window.history.back();
                                },
                                error: function (data, jqxhr, error) {
                                    console.log(data);
                                }
                            });
                        }).fail(function (response, jqxhr, error) {
                            toastr.error(error, ", por favor contacte con el desarrollador.");
                        });
                    }
                }
            });
        } else {
            toastr.clear();
            toastr.error("Error Inesperado");
        }
    }).fail(function (response, jqxhr, error) {
        toastr.error(error + ", contacte con el desarrollador");
    });
}

function calcularTotalVenta() {
    var total10 = 0;
    var total5 = 0;
    var totalE = 0;
    var total = 0;
    var totaliva = 0;
    var totaldescuentos = 0;
    $("#cuerpo tr").each(function (i) {
        if ($(this).find("td span.iva").html() === "10") {
            total10 += calcularIva($(this).find("td span.iva").html(), Entero($(this).find("td span.subtotal").html())) - (Descuento(Entero($(this).find("td span.subtotal").html()), $(this).find("td input.descuento").val()) / 11);
        } else if ($(this).find("td span.iva").html() === "5") {
            total5 += calcularIva($(this).find("td span.iva").html(), Entero($(this).find("td span.subtotal").html())) - (Descuento(Entero($(this).find("td span.subtotal").html()), $(this).find("td input.descuento").val()) / 21);
        } else if ($(this).find("td span.iva").html() === "E") {
            totalE += calcularIva($(this).find("td span.iva").html(), Entero($(this).find("td span.subtotal").html())) - (Descuento(Entero($(this).find("td span.subtotal").html()), $(this).find("td input.descuento").val()));
        }
        totaldescuentos += Descuento(Entero($(this).find("td span.subtotal").html()), $(this).find("td input.descuento").val());
        total += parseInt(Entero($(this).find("td span.subtotal").html()));
        totaliva = total10 + total5;
    });
    $("#i10").html(dot(total10));
    $("#i5").html(dot(total5));
    $("#ex").html(dot(totalE));
    $("#totiva").html(dot(totaliva));
    $("#totaldescuento").html(dot(totaldescuentos));
    if (isNaN(totaldescuentos)) {
        totaldescuentos = 0;
    }
    let redondeo = total - totaldescuentos;
    let decenas = redondeo.toString().substring(redondeo.toString().length - 2);
    if (parseInt(decenas) < 50) {
        total = total - totaldescuentos - parseInt(decenas);
        $("#tot").html(dot(total));
    } else if (parseInt(decenas) > 50) {
        total = total - totaldescuentos - parseInt(decenas) + 50;
        $("#tot").html(dot(total));
    } else {
        $("#tot").html(dot(total));
    }
    if ($("#saldo").length) {
        $("#saldo").html(dot(total - $("input[name='senha_ped']").val().replace(/[.+]/g, "") - Entero($('input[name="descuento_ped"]').val())));
    }
}

function calcularSubtotalVenta() {
    $("#cuerpo td input.cant").on('input change', function () {
        this.value = this.value.replace(".", ",");
        if (parseFloat(this.value.replace(",", ".")) < 0 || isNaN(parseFloat(this.value.replace(",", ".")))) {
            this.value = 0;
            this.select();
        }
        var tr = $(this).parent().parent();
        var precio = Entero(tr.find("td input.prec").val());
        var subt = parseFloat(this.value.replace(",", ".")) * parseFloat(precio);
        ;
        tr.find("td span.subtotal").html(dot(subt));
        calcularTotalVenta();
    });

    $("#cuerpo td input.prec").on('click', function () {
        this.select();
    }).on('change', function () {
        if (parseInt(Entero(this.value)) === 0) {
            $(this).closest("tr").remove();
        }
    }).on('input', function () {
        if (parseInt(Entero(this.value)) < 0) {
            var aux = Entero(this.value);
            $(this).val(dot(aux.substring(1, aux.length)));
        }

        if (!isNaN(parseInt(Entero(this.value)))) {
            this.value = dot(Entero(this.value));
        } else {
            this.value = 0;
            this.select();
        }

        let tr = $(this).closest('tr');
        let cantidad = tr.find("td input.cant").val();
        cantidad = cantidad.toString().indexOf(",") !== -1 ? parseFloat(cantidad.replace(",", ".")) : parseInt(cantidad);
        var subt = Entero(this.value) * cantidad;
        console.log(`${this.value} ${cantidad} ${subt}`);

        tr.find("td span.subtotal").html(dot(subt));
        calcularTotalVenta();
    });

    $("#cuerpo td input.descuento").on('input', function () {
        if (parseInt(this.value) > 100) {
            $('button.save').attr("disabled", true);
        } else {
            $('button.save').attr("disabled", false);
        }
        calcularTotalVenta();
    });
}

function agregarFilaAr() {
    $("#table-art tbody").on('input', 'td input.cant', function () {
        if (parseInt(this.value) <= 0 || isNaN(parseInt(this.value))) {
            this.value = 1;
            this.select();
        }
    });

    $("#table-art tbody").on('input', 'td input.num', function () {
        if (parseInt(Entero(this.value)) < 0) {
            var aux = Entero(this.value);
            $(this).val(dot(aux.substring(1, aux.length)));
        }
    });

    $("#table-art tbody").on('click', 'a', function () {
        var tart = this;
        var tr = $(this).parent().parent();
        var c = parseInt(tr.find("td input.cant").val());
        $("#cantidad").val(tr.find("td input.cant").val());

        if (parseInt(Entero(tr.find("td input.num").val())) !== 0) {
            verificarCodigoVen(this.getAttribute("data-id"));
        }

        tr.find("td input.cant").val(1);
        setTimeout(function () {
            if (parseInt(Entero(tr.find("td input.num").val())) !== 0) {
                var tabla = $("#" + tart.getAttribute("data-id")).parent().parent();
                var subt = Entero(tr.find("td input.num").val()) * c;
                tabla.find("td input.prec").val(tr.find("td input.num").val());
                tabla.find("td input.cant").val(c);
                tabla.find("td span.subtotal").html(dot(subt));
            } else {
                toastr.warning("Error, no se puede insertar un artículo con precio 0");
            }
        }, 100);
    });
}

function AgregarClienteVenta() {

    $("#modal").load("vistas/modal_cli.html", function () {
        $("#Modal-cli").modal();

        $("#nombre_cli").on('change', function () {
            $('input[name="razon_social"]').val(this.value);
        });

        $("#ndocumento_cli").on('change', function () {
            ValidarClientes();
        });

        AgregarClientes(0, 0);
        listarCiudades("select");
        $("#ciudad").select2({
            dropdownParent: $(".parent"),
            placeholder: "Seleccione la ciudad",
            allowClear: true
        });
        ruc();
        $('.date input[name="fecha_nac"]').datepicker({
            format: "dd/mm/yyyy"
        });
    });
}

function UltimoNroFactura() {
    $.post("ventasControlador", {
        accion: "ultimo"
    }).done((response) => {
        $('input[name="factura_numero"]').val(response);
    }).fail((response, jqxhr, error) => {
        toastr.error(error + ", contacte con el desarrollador");
    });
}

function FuncionImprimir(id) {
    var newwindow = window.open("merx?parametro=" + id, "", "width=1200,height=780");
    return newwindow;
}

function Reimprimir(id) {
    swal({
        title: "¿Reimprimir factura original?",
        text: "",
        type: "info",
        showCancelButton: true,
        confirmButtonColor: "#DD6B55",
        confirmButtonText: "Imprimir",
        cancelButtonText: "Cancelar",
        closeOnConfirm: false
    }, function () {
        window.swal({
            html: true,
            title: `<div class="progress progress-striped active">
                        <div style="width: 100%" aria-valuemax="100" aria-valuemin="0" aria-valuenow="75" role="progressbar" class="progress-bar progress-bar-info">
                            <span class="sr-only">40% Complete (success)</span>
                        </div>
                    </div>`,
            text: "Esto puede tardar varios minutos. Por favor espere...",
            showConfirmButton: false,
            allowOutsideClick: false
        });

        $.post("merx", {
            accion: "imprimir",
            parametro: id
        }).done(function (response) {
            $.ajax({
                url: "http://localhost:5000/printer",
                type: "post",
                contentType: "application/json; charset=utf-8",
                dataType: "json",
                data: JSON.stringify(response),
                success: function (data) {
                    console.log(data);
                }
            });
            swal.close();
        }).fail(function (response, jqxhr, error) {
            toastr.error(error, ", por favor contacte con el desarrollador.");
        });
    });
}

const modo_pago = function () {
    $("#Modal-pago").modal();
    $('#Modal-pago').on('shown.bs.modal', function () {
        $("#efectivo").focus().select();
    });

    $("#select-tipopago").on('change', function () {
        if (parseInt($(this).val()) === 2) {
            $(".aux").html(`<center><label class="control-label m-b" for="efectivo">Banco:</label></center>
                                    <div class="col-sm-12">
                                        <select name="banco" class="form-control" required>
                                        </select>
                                    </div>
                                    <center><label class="control-label m-b" for="efectivo">Nro de Comprobante:</label></center>
                                    <div class="col-sm-12">
                                        <input type="text" class="form-control m-b text-center miles" id="cupon" required>
                                    </div>`);
            SelectizeBanco();
        } else if (parseInt($(this).val()) === 3 || parseInt($(this).val()) === 4) {
            $(".aux").html(`<center><label class="control-label m-b" for="efectivo">Nro de Recibo:</label></center>
                                    <div class="col-sm-12">
                                        <input type="text" class="form-control m-b text-center miles" id="cupon" required>
                                    </div>`);
        } else if (parseInt($(this).val()) === 5) {
            $(".aux").html(`<center><label class="control-label m-b" for="efectivo">Nro de Comprobante:</label></center>
                                    <div class="col-sm-12">
                                        <input type="text" class="form-control m-b text-center miles" id="cupon" required>
                                    </div>`);
        } else if (parseInt($(this).val()) === 6) {
            $(".aux").html(`<center><label class="control-label m-b" for="efectivo">Banco:</label></center>
                                    <div class="col-sm-12">
                                        <select name="banco" class="form-control" required>
                                        </select>
                                    </div>
                                    <center><label class="control-label m-b" for="efectivo">Nro de Comprobante:</label></center>
                                    <div class="col-sm-12">
                                        <input type="text" class="form-control m-b text-center miles" id="cupon" required>
                                    </div>`);
            SelectizeBanco();
        } else {
            $(".aux").html("");
        }
    });
};

const SelectizeBanco = function () {
    $('select[name="banco"]').selectize({
        placeholder: 'Buscar banco ...',
        valueField: 'id_banco',
        labelField: 'nombre_banco',
        searchField: ['nombre_banco'],
        options: [],
        create: false,
        load: function (query, callback) {
            console.log(query);
            if (!query.length)
                return callback();
            $.ajax({
                url: 'bancosControlador',
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
};
const SetRowNumber = () => {
    $("#cuerpo tr td.rowNumber").each(function (i) {
        this.innerHTML = i + 1;
    });
};
function Descuento(subtotal, descuento) {
    let total = 0;
    total += parseInt(descuento);
    return total;
}

function popupClosing() {
    nuevaVenta();
}
