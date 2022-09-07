define(['paisesControlador'], function (pais) {
    var exports = {};
    exports.Pedidos = function () {
        spinneroff();
        $("#inicio").load("vistas/pedidos.html", function () {
            $(".active").removeClass("active");
            $(".level").parent().find("ul.nav").removeClass("nav nav-second-level collapse in").addClass("nav nav-second-level collapse");
            $('a[href="#/servicios"]').parent().addClass("active");
            $("#t-3").addClass("active");
            $("#tab-3").addClass("active");
            exports.ListarPedidos();
            exports.ListarPedidosAprobados();
            exports.ListarPedidosFacturados();
            exports.ListarPedidosporRetirar();
            AprobarPedidos("#tabla-pedidos");
            AnularPedido();
            CompartirOrden();
        });
    };

    exports.ListarPedidos = function () {
        $("#tabla-pedidos").DataTable({
            "pageLength": 100,
            destroy: true,
            columnDefs: [
                {type: 'date-uk', targets: 1}
            ],
            order: [[0, 'desc']],
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
                url: "pedidosControlador",
                data: {accion: "listar"}
            },
            columns: [
                {data: "id_pedido",
                    render: function (data, type, full, meta) {
                        return `<a onclick="window.open('orden_de_servicio?parametro=${data}&accion=etiqueta', '', 'width=1200,height=780')" class="btn btn-default btn-xs"><i class="fas fa-eye"> ${cero(data.toString())}</i></a>`;
                    }
                },
                {data: "fecha_ped"},
                {data: "nombre_cli"},
                {data: "ndocumento_cli"},
                {data: "aprobado_ped",
                    render: function (data, type, full, meta) {
                        let label;
                        if (full.aprobado_ped === 0 && full.retirado_ped === 0 && full.facturado_ped === 0) {
                            label = `<label class="label label-danger est">Pendiente</label>`;
                        } else if (full.aprobado_ped === 1 && full.retirado_ped === 0 && full.facturado_ped === 0) {
                            label = `<label class="label label-warning est">En Proceso</label>`;
                        } else if (full.aprobado_ped === 1 && full.retirado_ped === 1 && full.facturado_ped === 0) {
                            label = `<label class="label label-info est">A retirar</label>`;
                        } else if (full.aprobado_ped === 1 && full.retirado_ped === 1 && full.facturado_ped === 1) {
                            label = `<label class="label label-success est">Retirado</label>`;
                        }
                        return label;
                    }
                },
                {data: "total_ped",
                    render: function (data, type, full, meta) {
                        return dot(data);
                    }
                },
                {data: null,
                    render: function (data, type, full, meta) {
                        let accion;
                        if (data.aprobado_ped === 0 && data.retirado_ped === 0 && data.facturado_ped === 0) {
                            accion = `<div class="btn-group">
                                    <a href="#/servicios/editar?_id=${data.id_pedido}" class="btn btn-default btn-xs see"><i class="fa fa-edit"> Editar</i></a>
                                    <a class="btn btn-primary btn-xs ap" data-id=${data.id_pedido}><i class="fas fa-calendar-check"></i> En Proceso</a>
                                    <a class="btn btn-warning btn-xs clo" data-id="${data.id_pedido}"><i class="fa fa-ban"></i> Anular</a>
                                    <a class="btn btn-info btn-xs share" data-id="${data.id_pedido}"><i class="fab fa-whatsapp"></i> Compartir</a>
                                </div>`;
                        } else if (data.aprobado_ped === 1 && data.retirado_ped === 0 && data.facturado_ped === 0) {
                            accion = `<div class="btn-group">
                            <a class="btn btn-default btn-xs change" data-id="${data.id_pedido}"> <i class="fas fa-people-carry"></i> Estado</a>
                            <a class="btn btn-primary btn-xs retirar" data-id="${data.id_pedido}"><i class="fas fa-clipboard-check"></i> A retirar</a>
                            <a class="btn btn-warning btn-xs ap" data-id="${data.id_pedido}"><i class="fa fa-undo"></i> Desaprobar</a>
                            <a class="btn btn-info btn-xs share" data-id="${data.id_pedido}"><i class="fab fa-whatsapp"></i> Compartir</a>
                            </div>`;
                        } else if (data.aprobado_ped === 1 && data.retirado_ped === 1 && data.facturado_ped === 0) {
                            accion = `<div class="btn-group">
                            <a href="#/servicios/retirar?_id=${data.id_pedido}" class="btn btn-default btn-xs"><i class="fas fa-check-double"></i> Retirado</a>                          
                            <a href="#/servicios/facturar?_id=${data.id_pedido}" class="btn btn-primary btn-xs"><i class="fas fa-money-check-alt"></i> Retirar y Facturar</a>
                            <a class="btn btn-info btn-xs share" data-id="${data.id_pedido}"><i class="fab fa-whatsapp"></i> Compartir</a>
                            </div>`;
                        } else if (data.aprobado_ped === 1 && data.retirado_ped === 1 && data.facturado_ped === 1) {
                            accion = `<a class="btn btn-default btn-xs" onclick="window.open('orden_de_servicio?parametro=${data.id_pedido}&accion=orden', '', 'width=1200,height=780')"><i class="fa fa-eye"> Ver</i></a>`;
                        }
                        return accion;
                    }
                }
            ],
            initComplete: function () {
                RetirarPedido();
                ModalEstadosPed();
            }
        });
    };

    exports.ListarPedidosAprobados = function () {
        $("#tabla-aprobados").DataTable({
            destroy: true,
            order: [[0, 'desc']],
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
                url: "pedidosControlador",
                data: {accion: "aprobados"}
            },
            columns: [
                {data: "id_pedido",
                    render: function (data, type, full, meta) {
                        return cero(data.toString());
                    }
                },
                {data: "fecha_ped"},
                {data: "nombre_cli"},
                {data: "aprobado_ped",
                    render: function (data, type, full, meta) {
                        return data === 0 ? `<label class="label label-danger est">Pendiente</label>` : `<label class="label label-info est">Aprobado</label>`;
                    }
                },
                {data: "total_ped",
                    render: function (data, type, full, meta) {
                        return dot(data);
                    }
                },
                {data: null,
                    render: function (data, type, full, meta) {
                        return `<div class="btn-group">
                                    <a class="btn btn-default btn-xs" onclick="window.open('orden_de_servicio?parametro=${data.id_pedido}&accion=orden', '', 'width=1200,height=780')"><i class="fa fa-eye"> Ver</i></a>
                                    <a class="btn btn-primary btn-xs retirar" data-id="${data.id_pedido}"><i class="fas fa-clipboard-check"></i> Finalizado</a>
                                    <a class="btn btn-info btn-xs ap" data-id="${data.id_pedido}"><i class="fa fa-undo"></i> Desaprobar</a>
                            </div>`;
                    }
                }
            ],
            initComplete: function () {
                RetirarPedido();
            }
        });
    };

    exports.ListarPedidosporRetirar = function () {
        $("#tabla-retirar").DataTable({
            destroy: true,
            order: [[0, 'desc']],
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
                url: "pedidosControlador",
                data: {accion: "porretirar"}
            },
            columns: [
                {data: "id_pedido",
                    render: function (data, type, full, meta) {
                        return cero(data.toString());
                    }
                },
                {data: "fecha_ped"},
                {data: "nombre_cli"},
                {data: "aprobado_ped",
                    render: function (data, type, full, meta) {
                        return data === 0 ? `<label class="label label-danger est">Pendiente</label>` : `<label class="label label-info est">Aprobado</label>`;
                    }
                },
                {data: "total_ped",
                    render: function (data, type, full, meta) {
                        return dot(data);
                    }
                },
                {data: null,
                    render: function (data, type, full, meta) {
                        return `<div class="btn-group">
                            <a class="btn btn-default btn-xs" onclick="window.open('orden_de_servicio?parametro=${data.id_pedido}&accion=ver', '', 'width=1200,height=780')"><i class="fa fa-eye"> Ver</i></a>                          
                            <a href="#/servicios/facturar?_id=${data.id_pedido}" class="btn btn-primary btn-xs"><i class="fas fa-shopping-bag"></i> Finalizado</a>
                            <a class="btn btn-info btn-xs share" data-id="${data.id_pedido}"><i class="fab fa-whatsapp"></i> Compartir</a>
                            </div>`;
                    }
                }
            ]
        });
    };

    exports.ListarPedidosFacturados = function () {
        $("#tabla-facturados").DataTable({
            destroy: true,
            order: [[0, 'desc']],
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
                url: "pedidosControlador",
                data: {accion: "facturados"}
            },
            columns: [
                {data: "id_pedido",
                    render: function (data, type, full, meta) {
                        return cero(data.toString());
                    }
                },
                {data: "fecha_ped"},
                {data: "nombre_cli"},
                {data: "aprobado_ped",
                    render: function (data, type, full, meta) {
                        return data === 0 ? `<label class="label label-danger est">Pendiente</label>` : `<label class="label label-info est">Aprobado</label>`;
                    }
                },
                {data: "total_ped",
                    render: function (data, type, full, meta) {
                        return dot(data);
                    }
                },
                {data: null,
                    render: function (data, type, full, meta) {
                        return `<a class="btn btn-default btn-xs" onclick="window.open('orden_de_servicio?parametro=${data.id_pedido}&accion=ver', '', 'width=1200,height=780')"><i class="fa fa-eye"> Ver</i></a>`;
                    }
                }
            ],
            initComplete: function () {
                PermisosRender(5, (p) => {
//                    if (p.eliminar === 'N') {
//                        $('.clo').remove();
//                    }
                });
            }
        });
    };

    exports.BuscarPedidos = function (id) {
        $("#inicio").load("vistas/insertarPedidos.html", function () {
            miles();
            SelectUsuario();

            $.post('pedidosControlador', {
                accion: 'buscar',
                id_pedido: id
            }).done(function (response) {
                if (response) {
                    $('input[name="fecha_ped"]').val(response.fecha_ped);
                    $('textarea[name="comentario_ped"]').html(response.comentario_ped);
                    $('input[name="fecha_entrega"]').val(response.fechahora_entrega ? moment(response.fechahora_entrega, "YYYY-MM-DD HH:mm").format("DD/MM/YYYY") : "");
                    $('input[name="hora_entrega"]').val(response.fechahora_entrega ? moment(response.fechahora_entrega, "YYYY-MM-DD HH:mm").format("HH:mm") : "17:00");
                    $('textarea[name="lugar_entrega"]').val(response.lugar_entrega);
                    $('textarea[name="nota_especial"]').val(response.nota_especial);
                    $('select[name="asignado_a"]').val(response.asignado_a).trigger("change");
                    response.delivery_ped === 1 ? $('input[name="delivery_ped"]').attr('checked', "checked") : "";
                    $('input[name="senha_ped"]').val(dot(response.senha_ped));
                    $("#tot").html(dot(response.total_ped));

                    $('#cliente').selectize({
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
                        },
                        onInitialize: function () {
                            var selectize = this;
                            selectize.addOption(response);
                            selectize.setValue(response.id_cliente);
                        },
                        onChange: function (value) {
                            $.post("clientesControlador", {
                                accion: "buscar",
                                id_cliente: value
                            }).done(function (data) {
                                if (!response.id_cliente !== 0) {
                                    if (response.id_cliente !== data.id_cliente) {
                                        $("textarea[name='lugar_entrega']").val(data.googlemaps_cli);
                                        $("textarea[name='comentario_ped']").val(data.observacion_cli);
                                    } else {
                                        $("textarea[name='lugar_entrega']").val(response.lugar_entrega);
                                        $("textarea[name='comentario_ped']").val(response.comentario_ped);
                                    }
                                } else {
                                    $("textarea[name='lugar_entrega']").val(data.googlemaps_cli);
                                    $("textarea[name='comentario_ped']").val(data.observacion_cli);
                                }
                            }).fail(function (data, jqxhr, error) {
                                toastr.error(error, " Contacte con el desarrollador");
                            });
                        }
                    });

                    if (response.id_pedido) {
                        $.post("pedidosControlador", {
                            accion: 'detalle',
                            id_pedido: id
                        }).done(function (response) {
                            if (response) {
                                for (var i = 0; i < response.length; i++) {
                                    let tr = `<tr>
                                            <td><span class="code" id=${response[i].id_articulo}> ${response[i].codigo} </span></td>
                                            <td><input type="text" class="touchspin2 cant" value=${response[i].cantidad}  min="1" required></td>
                                            <td><span class="desc"> ${response[i].descripcion} </span></td>
                                            <td><input type="text" class="form-control prec" value="${dot(response[i].precio)}" required></td>
                                            <td>
                                                <select class="form-control" name="id_categoria">
                                                </select>
                                            </td>
                                            <td><span class="iva">${response[i].iva}</span></td>
                                            <td><span class="subtotal">${dot(response[i].precio * response[i].cantidad)} </span></td>
                                            <td><a class="text-danger"><i class="glyphicon glyphicon-trash"></i></a></td>
                                        </tr>`;
                                    SelectCategoria($("#cuerpo").append(tr), response[i].id_articulo, response[i].id_categoria);
                                }
                                calcularSubtotalVenta();
                            } else {
                                toastr.error("Esto es embarazoso, no se pudo guardar el pedido. Por favor verifique los datos en el formulario.");
                            }
                        }).fail(function (response, jqxhr, error) {
                            toastr.error(error + ", contacte con el desarrollador.");
                        }).always(() => {
                            calcularTotalVenta();
                            $(".touchspin2").TouchSpin({
                                buttondown_class: 'btn btn-white',
                                buttonup_class: 'btn btn-white'
                            });
                            $("#cuerpo tr").on('click', "td a", function (e) {
                                e.preventDefault();
                                $(this).closest("tr").remove();
                                return false;
                            });
                        });
                    }
                }
            }).fail(function (response, jqxhr, error) {
                toastr.error(error + ", contacte con el desarrollador");
            }).always(() => {
                $('.date input[name="fecha_ped"]').datepicker({
                    format: "dd/mm/yyyy",
                    orientation: "bottom"
                }).datepicker("setDate", 'now');

                $('.date input[name="fecha_entrega"]').datepicker({
                    format: "dd/mm/yyyy",
                    orientation: "bottom"
                }).datepicker('setDate', "+1d");

                $(".touchspin1").TouchSpin({
                    buttondown_class: 'btn btn-white',
                    buttonup_class: 'btn btn-white'
                });

                $('.clockpicker').clockpicker();
                $("#buscar").on('submit', function (e) {
                    e.preventDefault();
                    VerificarCodigo($("#codigo").val());
                });
                AgregarArticulosFast('VENTAS', (cod) => {
                    VerificarCodigo(cod);
                });
                BuscarArticulosOrden();
                exports.AgregarPedidos(id);
            });
            $("#nuevo").on('click', function () {
                AgregarClienteVenta();
            });
        });
    };

    exports.AgregarPedidos = function (id) {
        $("#form-pedidos").on('submit', function (e) {
            e.preventDefault();
            if ($("#cabecera-pedidos").valid()) {
                if ($("#cuerpo tr").length > 0) {
                    let fd = ConvertFormToJSON(document.getElementById("cabecera-pedidos"));
                    fd.id_pedido = id;
                    fd.total_ped = $('#tot').html().replace(/[.+]/g, "");
                    fd.senha_ped = $('input[name="senha_ped"]').val().replace(/[.+]/g, "");
                    let detalle = [];
                    $("#cuerpo tr").each(function (i) {
                        detalle[i] = {
                            id_articulo: $(this).find("td span").attr("id"),
                            codigo: $(this).find("td span.code").html(),
                            descripcion: $(this).find("td span.desc").html(),
                            cantidad: $(this).find("td input.cant").val(),
                            precio: $(this).find("td input.prec").val().replace(/[.+]/g, ""),
                            iva: $(this).find("td span.iva").html(),
                            subtotal: $.trim($(this).find("td span.subtotal").html().replace(/[.+]/g, "")),
                            id_categoria: $(this).find('select[name="id_categoria"]').val()
                        };
                    });
                    
                    $.post("pedidosControlador", {
                        accion: "insertar",
                        datos: JSON.stringify(fd),
                        detalle: JSON.stringify(detalle),
                        fechahora_entrega: moment(($("input[name='fecha_entrega']").val() + " " + $("input[name='hora_entrega']").val()), "DD-MM-YYYY HH:mm").format('YYYY-MM-DD HH:mm')
                    }).done(function (response) {
                        if (response !== 0) {
                            swal({html: true,
                                title: `
                                <div class="progress progress-striped active">
                                        <div style="width: 100%" aria-valuemax="100" aria-valuemin="0" aria-valuenow="75" role="progressbar" class="progress-bar progress-bar-info">
                                            <span class="sr-only">40% Complete (success)</span>
                                        </div>
                                </div>`,
                                text: "Esto puede tardar varios minutos. Por favor espere...",
                                showConfirmButton: false,
                                allowOutsideClick: false
                            });
                            $.post('orden_de_servicio', {
                                accion: "imprimir_etiqueta",
                                parametro: response
                            }).done(function (response) {
                                swal.close();
                                window.history.back();
                            }).fail(function (response, jqxhr, error) {
                                toastr.error(`${response} No se pudo imprimir, contacte con el desarrollador`);
                            });
                        } else {
                            toastr.error("Esto es embarazoso, no se pudo guardar el pedido. Por favor verifique los datos en el formulario.");
                        }
                    }).fail(function (response, jqxhr, error) {
                        toastr.error(error + ", contacte con el desarrollador.");
                    });
                } else {
                    toastr.warning("Por favor, agregue los artículos");
                }
            }
        });
    };

    let AprobarPedidos = function (tabla) {
        $(tabla).on('click', 'tbody .ap', function (e) {
            e.preventDefault();
            let id = this.getAttribute("data-id");
            $.post("pedidosControlador", {
                accion: "aprobar",
                id_pedido: id
            }).done(function (response) {
                if (response) {
                    tabla === "#tabla-pedidos" ? toastr.success("¡Aprobado!") : toastr.success("¡Desaprobado!");
                    $("#tabla-pedidos").DataTable().ajax.reload();
                } else {
                    toastr.error("Esto es embarazoso, no se pudo guardar el pedido. Por favor verifique los datos en el formulario.");
                }
            }).fail(function (response, jqxhr, error) {
                toastr.error(error + ", contacte con el desarrollador.");
            });
        });
    };

    exports.FacturarPedidos = function (id) {
        $.post("pedidosControlador", {
            accion: "nueva_factura",
            id_pedido: id
        }).done(function (response) {
            if (response) {
                $("#inicio").load("vistas/insertVenta.html", function () {
                    var date = new Date();
                    var d = date.getDate();
                    var m = date.getMonth() + 1;
                    var y = date.getFullYear();
                    $("#fecha").val(toDate(d) + "/" + toDate(m) + "/" + y);
                    $("#cli_name").val(response.nombre_cli);
                    $("#ruc_cli").val(response.ndocumento_cli);
                    $("#cli_name").attr("data-id", response.id_cliente);
                    $("button:submit").on('click', function () {
                        setTimeout(() => {
                            $("#aceptar").on('submit', function (e) {
                                e.preventDefault();
                                exports.CulminarOrden(id);
                            });
                        }, 250);
                    });
                    agregarVenta();
                    BuscarArticulosOrden();
                    date_time("date", "time");
                    currentUser();
                    buscarClienteVen();
                    $("#buscar").on('submit', function (e) {
                        e.preventDefault();
                        VerificarCodigo($("#codigo").val());
                    });
                    UltimoNroFactura();
                    $.post("pedidosControlador", {
                        accion: 'detalle',
                        id_pedido: id
                    }).done(function (response) {
                        if (response) {
                            for (var i = 0; i < response.length; i++) {
                                $("#cuerpo").append('<tr>' +
                                        '<td><span class="code" id=' + response[i].id_articulo + '>' + response[i].codigo + '</span></td>' +
                                        '<td><input type="text" class="touchspin2 cant" value=' + response[i].cantidad + ' min="1" required></td>' +
                                        '<td><span class="desc">' + response[i].descripcion + '</span></td>' +
                                        '<td><input type="text" class="form-control prec" value="' + dot(response[i].precio) + '" required></td>' +
                                        '<td><input type="number" class="form-control descuento" value="0" min="0" max="100" required></td>' +
                                        '<td><span class="iva">' + response[i].iva + '</span></td>' +
                                        '<td><span class="subtotal">' + dot(response[i].subtotal) + '</span></td>' +
                                        '<td><a class="text-danger"><i class="glyphicon glyphicon-trash"></i></a></td>' +
                                        '</tr>');
                            }
                        } else {
                            toastr.error("Esto es embarazoso, no se pudo guardar el pedido. Por favor verifique los datos en el formulario.");
                        }
                    }).fail(function (response, jqxhr, error) {
                        toastr.error(error + ", contacte con el desarrollador.");
                    }).always(() => {
                        $("#cuerpo tr").on('click', "td a", function (e) {
                            e.preventDefault();
                            $(this).closest("tr").remove();
                            calcularTotalVenta();
                            return false;
                        });
                        $(".touchspin2").TouchSpin({
                            buttondown_class: 'btn btn-white',
                            buttonup_class: 'btn btn-white'
                        });
                        calcularTotalVenta();
                    });
                });
            } else {
                toastr.error("Esto es embarazoso, no se pudo generar la factura. Por favor contacte con el desarrollador.");
            }
        }).fail(function (response, jqxhr, error) {
            toastr.error(error + ", contacte con el desarrollador.");
        });
    };

    let AnularPedido = function () {
        $("#tabla-pedidos").on('click', 'a.clo', function () {
            $(this).attr("disabled", true);
            var id = this.getAttribute("data-id");
            $.post('pedidosControlador', {
                accion: 'anular',
                id_pedido: id
            }).done((response) => {
                if (response === 1) {
                    toastr.success("¡ANULADO!");
                    $("#tabla-pedidos").DataTable().ajax.reload();
                } else {
                    toastr.error("Esto es embarazoso, no se pudo anular el pedido. Por favor contacte con el desarrollador.");
                }
            }).fail((response, jqxhr, error) => {
                toastr.error(error + ", contacte con el desarrollador.");
            });
        });
    };

    const RetirarPedido = function () {
        $("#tabla-pedidos tbody").on('click', "td a.retirar", function (e) {
            let id_pedido = $(this).attr("data-id");
            $.post("pedidosControlador", {
                accion: "retirar",
                id_pedido: id_pedido
            }).done(function (response, jqxhr, error) {
                if (response === 1) {
                    toastr.success("¡Finalizado!");
                    $("#tabla-pedidos").DataTable().ajax.reload();
                } else {
                    toastr.error(error, " Ha ocurrido un error. Por favor contacte con el desarrollador.");
                }
            }).fail(function (response, jqxhr, error) {
                toastr.error(error, ". Por favor contacte con el desarrollador.");
            });
        });
    };

    const ModalEstadosPed = function () {
        $("#tabla-pedidos tbody").on('click', "td a.change", function (e) {
            let id_pedido = $(this).attr("data-id");
            $.post("pedidosControlador", {
                accion: 'detalle',
                id_pedido: id_pedido
            }).done(function (response, jqxhr, error) {
                $("#modal").load("vistas/modal_estado_ped.html", function () {
                    ActualizarEstados(id_pedido);
                    $("#modal-estados").modal();
                    $.post("pedidosControlador", {
                        accion: 'buscar',
                        id_pedido: id_pedido,
                        aprobado: 1
                    }).done((response) => {
                        $("#cliente").html(response.nombre_cli);
                        $("#usuario").html(response.nombre_emp);
                        $("#notas").html(response.notas_especiales);
                        $("#ruc").html(response.ndocumento_cli);
                        $("#fecha").html(moment(response.fechahora_entrega, "YYYY-MM-DD HH:mm").format("DD/MM/YYYY HH:mm"));
                        $("#obs").html(response.comentario_ped);
                    }).fail((response, jqxhr, error) => {
                        toastr.error(`${error}, por favor contacte con el desarrollador`);
                    });

                    if (response) {
                        for (var i = 0; i < response.length; i++) {
                            let tr = `<tr>
                                        <td><span class="code" id=${response[i].id_articulo}> ${response[i].codigo} </span></td>
                                        <td><span class="desc"> ${response[i].descripcion} </span></td>
                                        <td>
                                            <select class="form-control" name="id_categoria">
                                            </select>
                                        </td>
                                    </tr>`;
                            SelectCategoria($("#cuerpo").append(tr), response[i].id_articulo, response[i].id_categoria);
                        }
                    } else {
                        toastr.error("Hule perro.");
                    }
                });
            }).fail(function (response, jqxhr, error) {
                toastr.error(`${error}, Por favor contacte con el desarrollador.`);
            });
        });
    };

    const CompartirOrden = function () {
        $("#tabla-pedidos").on('click', ".share", function () {
            let id = this.getAttribute("data-id");
            $("#modal").load("vistas/modal_compartircliente.html", function () {
                $("#share_modal").modal();
                pais.SeleccionarPais();
                $.post("pedidosControlador", {
                    accion: "compartir",
                    id_pedido: id,
                    aprobado: 1
                }, function (response) {
                    $('input[name="phone"]').val(response.celular_cli);
                    $('textarea').html(`*SAN FRANCISCO LAVANDERIA & TINTORERIA*\n*Cliente:* ${response.nombre_cli}\n*Retirar el:* ${moment(response.fechahora_entrega, "YYYY-MM-DD HH:mm").format("DD/MM/YYYY HH:mm")}\n*Artículo*\t*Cant*\t*P.U*\n`);
                    $.post('pedidosControlador', {
                        accion: "detalle",
                        id_pedido: id
                    }).done((data, jqxhr, error) => {
                        $.each(data, function (i) {
                            $('textarea').append(`${data[i].descripcion}\t${data[i].cantidad}\t${dot(data[i].precio)}\n`);
                        });
                        $('textarea').append(`*Total:* ${dot(response.total_ped)}\n*Seña:* ${dot(response.senha_ped)}\n*Saldo:* ${dot((response.total_ped - response.senha_ped))}\n*¡Gracias por su Preferencia!* 😊`);
                    }).fail((response, jqxhr, error) => {
                        toastr.error(`${error}, contacte con el desarrollador`);
                    });
                });

                $("#compartir").on('submit', function (e) {
                    e.preventDefault();
                    window.open(`https://web.whatsapp.com/send?phone=${$('input[name="codigo"]').val() + $('input[name="phone"]').val()}&text=${$('textarea[name="text"]').val()}`);
                });
            });
        });
    };

    exports.CulminarOrden = function (id) {
        $.post("pedidosControlador", {
            accion: "facturar_pedido",
            id_pedido: id
        }).done(function (response) {
            if (response === 1) {
                toastr.success("¡Listo!");
                $("#tabla-pedidos").DataTable().ajax.reload();
            } else {
                toastr.error("Esto es embarazoso, no se pudo facturar el pedido. Por favor contacte con el desarrollador.");
            }
        }).fail(function (response, jqxhr, error) {
            toastr.error(error + ", contacte con el desarrollador.");
        });
    };

    const BuscarArticulosOrden = function () {
        $("#b-modal").on('click', function () {
            $("#modal").load('vistas/modal_articulosxcat.html', function () {
                $("#modal-b").modal();
                $.post('categoriasControlador', {
                    accion: 'select',
                    tipo: 1
                }).done(function (response) {
                    $.each(response, function (i) {
                        $("#modal-b").find("div.modal-body ul.nav.nav-tabs").append(`<li ${i === 0 ? "class='active'" : ""}><a data-toggle="tab" data-id="${response[i].id_categoria}" data-href="tabla-${i}" href="#tab-${i}">${response[i].categoria}</a></li>`);
                        $("#modal-b").find("div.modal-body div.tab-content").append(
                                `<div id="tab-${i}" class="tab-pane ${i === 0 ? "active" : ""}">
                            <div class="panel-body">
                                <div class="table-responsive">
                                    <table id="tabla-${i}" class="table" style="width: 100%">
                                        <thead>
                                            <tr>
                                                <th>Código</th>  
                                                <th>Nombre</th>
                                                <th>Marca</th>
                                                <th>Stock</th>
                                                <th>Cant.</th>
                                                <th>Precio</th>
                                                <th></th>
                                            </tr>
                                        </thead>
                                    </table>
                                </div>
                            </div>
                        </div>`);
                    });
                }).fail(function (response, jqxhr, error) {
                    toastr.error(`${error}, contacte con el desarrollador.`);
                }).always(() => {
                    let tabla = $(this).find("li.active a").attr("data-href");
                    let id = $(this).find("li.active a").attr("data-id");
                    $(this).find("li").on('click', function () {
                        tabla = $(this).find("a").attr("data-href");
                        id = $(this).find("a").attr("data-id");
                        TablaCategoria(tabla, id);
                        AgregarFilas(tabla);
                    });
                    num();
                    miles();
                    TablaCategoria(tabla, id);
                    AgregarFilas(tabla);
                    $('#modal-b').on('hidden.bs.modal', function (e) {
                        $("#cantidad").val(1);
                    });
                });
            });
        });
    };

    const TablaCategoria = function (tabla, id) {
        $("#" + tabla).DataTable({
            destroy: true,
            pageLength: 5,
            ajax: {
                url: "stockControlador",
                type: "POST",
                data: {
                    accion: "orden",
                    id_categoria: id
                }
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
    };

    const AgregarFilas = function (tabla) {
        $("#" + tabla).on('input', 'td input.cant', function () {
            if (parseInt(this.value) <= 0 || isNaN(parseInt(this.value))) {
                this.value = 1;
                this.select();
            }
        });

        $("#" + tabla).find("tbody").on('input', 'td input.num', function () {
            if (parseInt(Entero(this.value)) < 0) {
                var aux = Entero(this.value);
                $(this).val(dot(aux.substring(1, aux.length)));
            }
        });

        $("#" + tabla).find("tbody").on('click', 'a', function () {
            var tart = this;
            var tr = $(this).parent().parent();
            var c = parseInt(tr.find("td input.cant").val());
            $("#cantidad").val(tr.find("td input.cant").val());

            if (parseInt(Entero(tr.find("td input.num").val())) !== 0) {
                VerificarCodigo(this.getAttribute("data-id"));
            }

            tr.find("td input.cant").val(1);
            setTimeout(function () {
                if (parseInt(Entero(tr.find("td input.num").val())) !== 0) {
                    var tabla = $("#" + tart.getAttribute("data-id")).parent().parent();
                    var subt = Entero(tr.find("td input.num").val()) * c;
                    tabla.find("td input.prec").val(tr.find("td input.num").val());
                    tabla.find("td input.cant").val(c);
                    tabla.find("td span.subtotal").html(dot(subt));
                    calcularTotalVenta();
                } else {
                    toastr.warning("Error, no se puede insertar un artículo con precio 0");
                }
            }, 100);
        });
    };

    const VerificarCodigo = function (cod) {
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
                    let tr = `<tr>
                        <td><span class="code" id=${response.id_articulo}> ${response.codigo_ar} </span></td>
                        <td><input type="text" class="touchspin2 cant" value=${$("#cantidad").val()}  min="1" required></td>
                        <td><span class="desc"> ${response.nombre_ar} </span></td>
                        <td><input type="text" class="form-control prec" value="${dot(response.precio_venta_ar)}" required></td>
                        <td>
                            <select class="form-control" name="id_categoria">
                            </select>
                        </td>
                        <td><span class="iva">${response.iva_ar}</span></td>
                        <td><span class="subtotal">${dot(response.precio_venta_ar * $("#cantidad").val())} </span></td>
                        <td><a class="text-danger"><i class="glyphicon glyphicon-trash"></i></a></td>
                    </tr>`;
                    SelectCategoria($("#cuerpo").append(tr), response.id_articulo);
                    calcularTotalVenta();
                    calcularSubtotalVenta();
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
                return false;
            });
        });
        if ($('#cuerpo tr').length <= 0) {
            $('button.save').attr("disabled", true);
        } else {
            $('button.save').attr("disabled", false);
            $("div.alert.alert-danger").remove();
        }
    };

    const SelectCategoria = function (tr, id, value) {
        $.post("categoriasControlador", {
            accion: "select",
            tipo: 2
        }).done((response, jqxhr, error) => {
            let fila = $(tr).find('span[id="' + id + '"]').parent().parent();
            for (var i = 0; i < response.length; i++) {
                if (!$(fila).find('option[value="' + response[i].id_categoria + '"]').val()) {
                    var eti = $(fila).find('select[name="id_categoria"]');
                    var option = document.createElement("option");
                    option.setAttribute("value", response[i].id_categoria);
                    response[i].id_categoria === value ? option.setAttribute("selected", "selected") : "";
                    option.innerHTML = response[i].categoria;
                    eti.append(option);
                }
            }
        }).fail((reponse, jqxhr, error) => {
            toastr.error(`${error}, contacte con el desarrollador`);
        });
    };

    const SelectUsuario = function () {
        $.post("usuarioControlador", {
            accion: "asignar"
        }).done((response) => {
            $(response).each((i) => {
                $('select[name="asignado_a"]').append(`<option value="${response[i].id_usuario}">${response[i].nombre_emp} ${response[i].apellido_emp}</option>`);
            });
        }).fail((response, jqxhr, error) => {
            toastr.error(`${error}, contacte con el desarrollador`);
        }).always(() => {
            $('select[name="asignado_a"]').select2({
                placeholder: "Seleccione el Usuario",
                allowClear: true
            });
        });
    };

    const ActualizarEstados = function (id) {
        let detalle = [];
        $("#form-estados").on('submit', function (e) {
            e.preventDefault();
            $("#cuerpo tr").each(function (i) {
                detalle[i] = {
                    id_pedido: id,
                    id_articulo: $(this).find("td span").attr("id"),
                    id_categoria: $(this).find('select[name="id_categoria"]').val()
                };
            });
            $.post("pedidosControlador", {
                accion: "actualizardetalle",
                detalle: JSON.stringify(detalle)
            }).done((response) => {
                if (response) {
                    toastr.success("¡Guardado!");
                    $("#modal-estados").modal('hide');
                }
            }).fail((response, jqxhr, error) => {
                toastr.error(`${error} contacte con el desarrollador`);
            });
        });
    };

    return exports;


});
