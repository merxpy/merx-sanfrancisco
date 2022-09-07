define(['paisesControlador', 'deliveryControlador'], function (pais, delivery) {
    var exports = {};
    exports.Ordenes = function (p) {
        spinneroff();
        $("#inicio").load("vistas/pedidos.html", function () {
            $(".active").removeClass("active");
            $(".level").closest("li").find("ul.nav").removeClass("nav nav-second-level collapse in").addClass("nav nav-second-level collapse");
            $('a.orden-trabajo').closest("li").addClass("active");
            $("a.orden-trabajo").closest("li").find("ul.nav").removeClass("nav nav-second-level collapse").addClass("nav nav-second-level collapse in");

            if (p === "pendientes") {
                exports.ListarOrdenes("listarPendientes");
            } else if (p === "aretirar") {
                exports.ListarOrdenes("listarARetirar");
            } else if (p === "retirados") {
                exports.ListarOrdenes("listarRetirados");
            } else if (p === "verTodos") {
                exports.ListarOrdenes("verTodos");
            } else if (p === "anulados") {
                exports.ListarOrdenesAnuladas("anulados");
            } else if (p === "verfacturados") {
                exports.ListarOrdenes("verfacturados");
            } else if (p === "verdeliveries") {
                exports.ListarOrdenes("verdeliveries");
            }

            AprobarOrdenes("#tabla-pedidos");
            AnularOrden();
            CompartirOrden();
            HabilitarOrden();
            PagadoyRetirado();
            pagado();

        });

    };

    exports.ListarOrdenes = function (accion) {
        $("#tabla-pedidos").DataTable({
            pageLength: 100,
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
                data: {accion: accion}
            },
            columns: [
                {data: "id_pedido",
                    render: function (data, type, full, meta) {
                        return `<a onclick="window.open('orden_de_servicio?parametro=${data}&accion=etiqueta', '', 'width=1200,height=780')" class="btn btn-default btn-xs"><i class="fas fa-eye"> ${cero(data.toString())}</i></a>`;
                    }
                },
                {data: "fecha_ped"},
                {data: "fechahora_entrega"},
                {data: "nombre_cli"},
                {data: "ndocumento_cli"},
                {data: "nombre_sucursal"},
                {data: "aprobado_ped",
                    render: function (data, type, full, meta) {
                        let label;
                        if (full.aprobado_ped === 0 && full.retirado_ped === 'PENDIENTE' && full.close_ped !== 1) {
                            if (full.senha_ped === full.total_ped) {
                                label = `<label class="label label-danger est">Pagado|Pendiente</label>`;
                            } else {
                                label = `<label class="label label-danger est">Pendiente</label>`;
                            }
                        } else if (full.aprobado_ped === 1 && full.retirado_ped === 'PENDIENTE' && full.close_ped !== 1) {
                            if (full.senha_ped === full.total_ped) {
                                label = `<label class="label label-warning est">Pagado|En Proceso</label>`;
                            } else {
                                label = `<label class="label label-warning est">En Proceso</label>`;
                            }
                        } else if (full.aprobado_ped === 1 && full.retirado_ped === 'A RETIRAR' && full.close_ped !== 1) {
                            if (full.senha_ped === full.total_ped) {
                                label = `<label class="label label-info est">Pagado|A retirar</label>`;
                            } else {
                                label = `<label class="label label-info est">A retirar</label>`;
                            }
                        } else if (full.aprobado_ped === 1 && full.retirado_ped === 'RETIRADO' && full.close_ped !== 1) {
                            if (full.senha_ped === full.total_ped) {
                                label = `<label class="label label-success est">Pagado|Retirado</label>`;
                            } else {
                                label = `<label class="label label-success est">Retirado</label>`;
                            }

                        }
                        return label;
                    }
                },
                {data: "total_ped",
                    render: function (data, type, full, meta) {
                        return dot(data - full.senha_ped);
                    }
                },
                {data: null,
                    render: function (data, type, full, meta) {
                        let accion;
                        if (data.aprobado_ped === 0 && data.retirado_ped === 'PENDIENTE' && full.close_ped !== 1) {
                            if (full.senha_ped === full.total_ped) {
                                if (full.facturado_ped === 0) {
                                    accion = `<div class="btn-group">
                                                    <a href="#/servicios/editar?_id=${data.id_pedido}" class="btn btn-default btn-xs see"><i class="fa fa-edit"> Editar</i></a>
                                                    <a class="btn btn-primary btn-xs ap" data-id=${data.id_pedido}><i class="fas fa-calendar-check"></i> En Proceso</a>
                                                    <a class="btn btn-warning btn-xs clo" data-id="${data.id_pedido}"><i class="fa fa-ban"></i> Anular</a>
                                                    <a class="btn btn-info btn-xs share" data-id="${data.id_pedido}"><i class="fab fa-whatsapp"></i> Compartir</a>
                                                    <a href="#/servicios/facturar?_id=${data.id_pedido}" class="btn btn-success btn-xs"><i class="far fa-check-circle"></i> Facturar</a>
                                                    <a class="btn btn-default btn-xs" onclick="window.open('orden_de_servicio?parametro=${data.id_pedido}&accion=orden', '', 'width=1200,height=780')"><i class="fa fa-eye"> Ver</i></a>
                                                </div>`;
                                } else {
                                    accion = `<div class="btn-group">
                                                    <a href="#/servicios/editar?_id=${data.id_pedido}" class="btn btn-default btn-xs see"><i class="fa fa-edit"> Editar</i></a>
                                                    <a class="btn btn-primary btn-xs ap" data-id=${data.id_pedido}><i class="fas fa-calendar-check"></i> En Proceso</a>
                                                    <a class="btn btn-warning btn-xs clo" data-id="${data.id_pedido}"><i class="fa fa-ban"></i> Anular</a>
                                                    <a class="btn btn-info btn-xs share" data-id="${data.id_pedido}"><i class="fab fa-whatsapp"></i> Compartir</a>
                                                    <a class="btn btn-default btn-xs" onclick="window.open('orden_de_servicio?parametro=${data.id_pedido}&accion=orden', '', 'width=1200,height=780')"><i class="fa fa-eye"> Ver</i></a>
                                                </div>`;
                                }

                            } else {
                                accion = `<div class="btn-group">
                                    <a href="#/servicios/editar?_id=${data.id_pedido}" class="btn btn-default btn-xs see"><i class="fa fa-edit"> Editar</i></a>
                                    <a class="btn btn-primary btn-xs ap" data-id=${data.id_pedido}><i class="fas fa-calendar-check"></i> En Proceso</a>
                                    <a class="btn btn-warning btn-xs clo" data-id="${data.id_pedido}"><i class="fa fa-ban"></i> Anular</a>
                                    <a class="btn btn-info btn-xs share" data-id="${data.id_pedido}"><i class="fab fa-whatsapp"></i> Compartir</a>
                                    <a class="btn btn-success btn-xs pagado" data-id="${data.id_pedido}"><i class="far fa-money-bill-alt"></i> Pagado</a>
                                    <a class="btn btn-default btn-xs" onclick="window.open('orden_de_servicio?parametro=${data.id_pedido}&accion=orden', '', 'width=1200,height=780')"><i class="fa fa-eye"> Ver</i></a>
                                </div>`;
                            }

                        } else if (data.aprobado_ped === 1 && data.retirado_ped === 'PENDIENTE' && full.close_ped !== 1) {
                            if (full.senha_ped === full.total_ped) {
                                if (full.facturado_ped === 0) {
                                    accion = `<div class="btn-group">
                                                    <a class="btn btn-default btn-xs change" data-id="${data.id_pedido}"> <i class="fas fa-people-carry"></i> Estado</a>
                                                    <a class="btn btn-primary btn-xs retirar" data-id="${data.id_pedido}"><i class="fas fa-clipboard-check"></i> A retirar</a>
                                                    <a class="btn btn-warning btn-xs ap" data-id="${data.id_pedido}"><i class="fa fa-undo"></i> Desaprobar</a>
                                                    <a class="btn btn-info btn-xs share" data-id="${data.id_pedido}"><i class="fab fa-whatsapp"></i> Compartir</a>
                                                    <a href="#/servicios/facturar?_id=${data.id_pedido}" class="btn btn-success btn-xs" data-id="${data.id_pedido}"><i class="far fa-check-circle"></i> Facturar</a>
                                                    <a class="btn btn-default btn-xs" onclick="window.open('orden_de_servicio?parametro=${data.id_pedido}&accion=orden', '', 'width=1200,height=780')"><i class="fa fa-eye"> Ver</i></a>
                                                </div>`;
                                } else {
                                    accion = `<div class="btn-group">
                                                <a class="btn btn-default btn-xs change" data-id="${data.id_pedido}"> <i class="fas fa-people-carry"></i> Estado</a>
                                                <a class="btn btn-primary btn-xs retirar" data-id="${data.id_pedido}"><i class="fas fa-clipboard-check"></i> A retirar</a>
                                                <a class="btn btn-warning btn-xs ap" data-id="${data.id_pedido}"><i class="fa fa-undo"></i> Desaprobar</a>
                                                <a class="btn btn-info btn-xs share" data-id="${data.id_pedido}"><i class="fab fa-whatsapp"></i> Compartir</a>
                                                <a class="btn btn-default btn-xs" onclick="window.open('orden_de_servicio?parametro=${data.id_pedido}&accion=orden', '', 'width=1200,height=780')"><i class="fa fa-eye"> Ver</i></a>
                                            </div>`;
                                }
                            } else {
                                accion = `<div class="btn-group">
                                                <a class="btn btn-default btn-xs change" data-id="${data.id_pedido}"> <i class="fas fa-people-carry"></i> Estado</a>
                                                <a class="btn btn-primary btn-xs retirar" data-id="${data.id_pedido}"><i class="fas fa-clipboard-check"></i> A retirar</a>
                                                <a class="btn btn-warning btn-xs ap" data-id="${data.id_pedido}"><i class="fa fa-undo"></i> Desaprobar</a>
                                                <a class="btn btn-info btn-xs share" data-id="${data.id_pedido}"><i class="fab fa-whatsapp"></i> Compartir</a>
                                                <a class="btn btn-success btn-xs pagado" data-id="${data.id_pedido}"><i class="far fa-money-bill-alt"></i> Pagado</a>
                                                <a class="btn btn-default btn-xs" onclick="window.open('orden_de_servicio?parametro=${data.id_pedido}&accion=orden', '', 'width=1200,height=780')"><i class="fa fa-eye"> Ver</i></a>
                                            </div>`;
                            }

                        } else if (data.aprobado_ped === 1 && data.retirado_ped === 'A RETIRAR' && full.close_ped !== 1) {
                            if (full.senha_ped === full.total_ped) {
                                if (full.facturado_ped === 0) {
                                    accion = `
                                        <div class="btn-group">
                                            <a href="#/servicios/retirado?_id=${data.id_pedido}" class="btn btn-default btn-xs"><i class="fas fa-check-double"></i> Retirado</a>                          
                                            <a class="btn btn-info btn-xs share" data-id="${data.id_pedido}"><i class="fab fa-whatsapp"></i> Compartir</a>
                                            <a href="#/servicios/facturar?_id=${data.id_pedido}" class="btn btn-success btn-xs" data-id="${data.id_pedido}"><i class="far fa-check-circle"></i> Facturar</a>
                                            <a class="btn btn-default btn-xs" onclick="window.open('orden_de_servicio?parametro=${data.id_pedido}&accion=orden', '', 'width=1200,height=780')"><i class="fa fa-eye"> Ver</i></a>
                                        </div>`;
                                } else {
                                    accion = `
                                            <div class="btn-group">
                                                <a href="#/servicios/retirado?_id=${data.id_pedido}" class="btn btn-default btn-xs"><i class="fas fa-check-double"></i> Retirado</a>                          
                                                <a class="btn btn-info btn-xs share" data-id="${data.id_pedido}"><i class="fab fa-whatsapp"></i> Compartir</a>
                                                <a class="btn btn-default btn-xs" onclick="window.open('orden_de_servicio?parametro=${data.id_pedido}&accion=orden', '', 'width=1200,height=780')"><i class="fa fa-eye"> Ver</i></a>
                                            </div>`;
                                }

                            } else {
                                accion = `<div class="btn-group">
                                                <a href="#/servicios/retirado?_id=${data.id_pedido}" class="btn btn-default btn-xs"><i class="fas fa-check-double"></i> Retirado</a>                          
                                                <a class="btn btn-primary btn-xs payretired" data-id="${data.id_pedido}"><i class="fas fa-money-check-alt"></i> Pagado y Retirado</a>
                                                <a class="btn btn-info btn-xs share" data-id="${data.id_pedido}"><i class="fab fa-whatsapp"></i> Compartir</a>                               
                                                <a class="btn btn-default btn-xs" onclick="window.open('orden_de_servicio?parametro=${data.id_pedido}&accion=orden', '', 'width=1200,height=780')"><i class="fa fa-eye"> Ver</i></a>
                                            </div>`;
                            }
                        } else if (data.aprobado_ped === 1 && data.retirado_ped === 'RETIRADO' && full.close_ped !== 1) {
                            if (full.senha_ped === full.total_ped) {
                                if (full.facturado_ped === 0) {
                                    accion = `<div class="btn-group">
                                                <a href="#/servicios/facturar?_id=${data.id_pedido}" class="btn btn-success btn-xs" data-id="${data.id_pedido}"><i class="far fa-check-circle"></i> Facturar</a>
                                                <a class="btn btn-default btn-xs" onclick="window.open('orden_de_servicio?parametro=${data.id_pedido}&accion=orden', '', 'width=1200,height=780')"><i class="fa fa-eye"> Ver</i></a>
                                            </div>`;
                                } else {
                                    accion = `<div class="btn-group">
                                                <a class="btn btn-default btn-xs" onclick="window.open('orden_de_servicio?parametro=${data.id_pedido}&accion=orden', '', 'width=1200,height=780')"><i class="fa fa-eye"> Ver</i></a>
                                            </div>`;
                                }
                            } else {
                                accion = `<div class="btn-group">
                                            <a class="btn btn-success btn-xs pagado" data-id="${data.id_pedido}"><i class="far fa-money-bill-alt"></i> Pagado</a> 
                                            <a class="btn btn-default btn-xs" onclick="window.open('orden_de_servicio?parametro=${data.id_pedido}&accion=orden', '', 'width=1200,height=780')"><i class="fa fa-eye"> Ver</i></a>
                                        </div>`;
                            }
                        } else if (data.close_ped === 1) {
                            accion = `<a class="btn btn-default btn-xs" onclick="window.open('orden_de_servicio?parametro=${data.id_pedido}&accion=orden', '', 'width=1200,height=780')"><i class="fa fa-eye"> Ver</i></a>`;
                        }
                        return accion;
                    }
                }
            ],
            initComplete: function () {
                RetirarOrden();
                $("#tabla-pedidos tbody").on('click', "td a.facturar", function (e) {
                    exports.CulminarOrden(this.getAttribute("data-id"));

                });
                ModalEstadosOrden();
            }
        });
    };

    exports.ListarOrdenesAnuladas = function (accion) {
        $("#inicio").load("vistas/pedidosAnulados.html", function () {
            $(".active").removeClass("active");
            $("a.orden-trabajo").find("ul.nav").removeClass("nav nav-second-level collapse in").addClass("nav nav-second-level collapse");
            $('a.orden-trabajo').closest("li").addClass("active");
            HabilitarOrden();
            $("#tabla-pedidosAnulado").DataTable({
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
                    data: {accion: accion}
                },
                columns: [
                    {data: "id_pedido",
                        render: function (data, type, full, meta) {
                            return `<a onclick="window.open('orden_de_servicio?parametro=${data}&accion=etiqueta', '', 'width=1200,height=780')" class="btn btn-default btn-xs"><i class="fas fa-eye"> ${cero(data.toString())}</i></a>`;
                        }
                    },
                    {data: "fecha_ped",
                        render: function (data, type, full, meta) {
                            return `Recibido ${data}<br>Entrega ${full.fechahora_entrega}`;
                        }
                    },
                    {data: "motivo_anulado"},
                    {data: "nombre_cli"},
                    {data: "ndocumento_cli"},
                    {data: "nombre_sucursal"},
                    {data: "aprobado_ped",
                        render: function (data, type, full, meta) {
                            return `<label class="label label-warning est">Anulado</label>`;
                        }
                    },
                    {data: "total_ped",
                        render: function (data, type, full, meta) {
                            return dot(data - full.senha_ped);
                        }
                    },
                    {data: null,
                        render: function (data, type, full, meta) {
                            let accion;
                            if (data.close_ped === 1) {
                                accion = `<div class="btn-group">
                                            <a class="btn btn-default btn-xs" onclick="window.open('orden_de_servicio?parametro=${data.id_pedido}&accion=orden', '', 'width=1200,height=780')"><i class="fa fa-eye"> Ver</i></a>
                                            <a class="btn btn-success btn-xs habilitarOrden" data-id="${data.id_pedido}"><i class="fa fa-undo"></i> Habilitar</a>
                                        </div>`;
                            }
                            return accion;
                        }
                    }
                ],
                initComplete: function () {
                    RetirarOrden();
                    ModalEstadosOrden();
                }
            });
        });
    };

    exports.BuscarOrdenes = function (id, id_delivery) {
        $("#inicio").load("vistas/insertarOrdendeTrabajo.html", function () {
            miles();
            SelectUsuario();
            SenhaMenosTotal();
            DescMenosTotal();
            if (id_delivery === 0) {
                $('input[name="delivery_ped"]').on('change', function () {
                    if ($('input[name="delivery_ped"]').is(':checked')) {
                        if ($('select[name="id_cliente"]').val()) {
                            delivery.BuscarDelivery(0);
                        } else {
                            toastr.warning("Seleccione un cliente");
                            $('input[name="delivery_ped"]').prop('checked', false);
                        }
                    }
                });

                $('a[href="javascript:window.history.back();"]').on('click', function () {
                    if ($('input[name="delivery_ped"]').attr('data-id') !== undefined) {
                        delivery.EliminarDeliveries($('input[name="delivery_ped"]').attr('data-id'));
                    }
                });
            } else {
                $('input[name="delivery_ped"]').attr('data-id', id_delivery);
            }

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
                    $('input[name="descuento_ped"]').val(dot(response.descuento_ped));
                    $("#tot").html(dot(response.total_ped));
                    $("#descuento").html(dot(response.descuento_ped));

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
                            let h = document.location.toString();
                            let l = h.substring(h.indexOf("#"), h.indexOf("?"));
                            let selectize = this;
                            if (l !== "#/delivery/retiros/generar") {
                                selectize.addOption(response);
                                selectize.setValue(response.id_cliente);
                            } else {
                                $.post("deliveriesControlador", {
                                    accion: 'buscar',
                                    id: $('input[name="delivery_ped"]').attr('data-id')
                                }).done((response) => {
                                    selectize.addOption({
                                        nombre_cli: response.nombre,
                                        id_cliente: response.cliente_ref
                                    });
                                    selectize.setValue(response.cliente_ref);
                                    $('input[name="fecha_entraga"]').val(response.fecha);
                                }).fail((response, jqxhr, error) => {
                                    console.log("Not work for me");
                                });
                            }
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
                                            <td class="rowNumber"></td>
                                            <td><span class="code" id=${response[i].id_articulo}> ${response[i].codigo} </span></td>
                                            <td><input type="text" class="touchspin2 cant" value=${response[i].cantidad}  min="1" required></td>
                                            <td><span class="desc"> ${response[i].descripcion} </span></td>
                                            <td><span class="desc_servicio"> ${response[i].descripcion_ar}</span></td>
                                            <td><input type="text" class="form-control prec" value="${dot(response[i].precio)}" required></td>
                                            <td>
                                                <select class="form-control" name="id_categoria">
                                                </select>
                                            </td>
                                            <td><span class="iva">${response[i].iva}</span></td>
                                            <td><span class="subtotal">${dot(response[i].precio * response[i].cantidad)} </span></td>
                                            <td><a class="text-danger" data-id="${response[i].id_articulo}"><i class="glyphicon glyphicon-trash"></i></a></td>    
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
                                let id = this.getAttribute("data-id");
                                $(this).closest("tr").remove();
                                $('#articulos table').each(function () {
                                    const table = $(this).DataTable();
                                    table.$('span[data-id="' + id + '"]').closest('tr').find('input[name="cantidad"]').off("change");
                                    table.$('span[data-id="' + id + '"]').closest('tr').find('input[name="cantidad"]').val(0);
                                    table.$('span[data-id="' + id + '"]').closest('tr').find('input[name="cantidad"]').on('change', function () {
                                        const row = $(this).closest('tr');
                                        const table = $(row.closest('table')).DataTable();
                                        CalcularTotalOrden(table.row(row).data(), row);
                                    });
                                });
                                calcularTotalVenta();
                                calcularSubtotalVenta();
                            });
                        });
                    }
                }
                ListarArticulosOrden(id);
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
                exports.AgregarOrdenes(id);
            });
            $("#nuevo").on('click', function () {
                AgregarClienteVenta();
            });
        });
    };

    exports.AgregarOrdenes = function (id) {
        $("#form-pedidos").on('submit', function (e) {
            e.preventDefault();
            if ($("#cabecera-pedidos").valid()) {
                let fd = ConvertFormToJSON(document.getElementById("cabecera-pedidos"));
                fd.id_pedido = id;
                fd.total_ped = $('#tot').html().replace(/[.+]/g, "");
                fd.senha_ped = $('input[name="senha_ped"]').val().replace(/[.+]/g, "");
                fd.descuento_ped = $('input[name="descuento_ped"]').val().replace(/[.+]/g, "");
                if ($('input[name="delivery_ped"]').attr('data-id') !== undefined) {
                    fd.id_delivery = $('input[name="delivery_ped"]').attr("data-id");
                }

                let detalle = [];

                $("#cuerpo tr").each(function (i) {
                    detalle[i] = {
                        id_articulo: $(this).find("td span").attr("id"),
                        codigo: $(this).find("td span.code").html(),
                        descripcion: $(this).find("td span.desc").html(),
                        descripcion_ar: $(this).find("td span.desc_servicio").html(),
                        cantidad: $(this).find("td input.cant").val(),
                        precio: $(this).find("td input.prec").val().replace(/[.+]/g, ""),
                        iva: $(this).find("td span.iva").html(),
                        subtotal: $.trim($(this).find("td span.subtotal").html().replace(/[.+]/g, "")),
                        id_categoria: $(this).find('select[name="id_categoria"]').val()
                    };
                });

                if ($.isEmptyObject(detalle)) {
                    toastr.warning("Por favor, Ingrese las cantidades ☺");
                } else {
                    if (parseInt(fd.senha_ped) !== 0) {
                        $("#modal").load("vistas/modal_modo_pago.html", function () {
                            $("#Modal-pago").modal();
                            $("#select-tipopago").on('change', function () {
                                if (parseInt($(this).val()) === 2 || parseInt($(this).val()) === 6) {
                                    $(".aux").html(`<center>
                                                        <label class="control-label m-b" for="efectivo">Banco:</label>
                                                    </center>
                                                    <div class="col-sm-12">
                                                        <select name="banco" class="form-control" required>
                                                        </select>
                                                    </div>
                                                    <center>
                                                        <label class="control-label m-b" for="efectivo">Nro de Comprobante:</label>
                                                    </center>
                                                    <div class="col-sm-12">
                                                        <input type="text" class="form-control m-b text-center miles" id="cupon" autocomplete="off" required>
                                                    </div>`);
                                    exports.SelectizeBanco();
                                    $("#aceptar button[type=\"submit\"]").attr("disabled", true);

                                    $('select[name="banco"]').on('change', function () {
                                        this.value ? $("#cupon").val() ? $("#aceptar button[type=\"submit\"]").attr("disabled", false) : $("#aceptar button[type=\"submit\"]").attr("disabled", true) : $("#aceptar button[type=\"submit\"]").attr("disabled", true);
                                    });

                                    $("#cupon").on('input', function () {
                                        this.value ? $('select[name="banco"]').val() ? $("#aceptar button[type=\"submit\"]").attr("disabled", false) : $("#aceptar button[type=\"submit\"]").attr("disabled", true) : $("#aceptar button[type=\"submit\"]").attr("disabled", true);
                                    });
                                } else if (parseInt($(this).val()) === 3 || parseInt($(this).val()) === 4 || parseInt($(this).val()) === 5) {
                                    $("#aceptar button[type=\"submit\"]").attr("disabled", true);
                                    $(".aux").html(`<center><label class="control-label m-b" for="efectivo">Nro de Comprobante:</label></center>
                                    <div class="col-sm-12">
                                        <input type="text" class="form-control m-b text-center miles" id="cupon" autocomplete="off" required>
                                    </div>`);

                                    $("#cupon").on('input', function () {
                                        this.value ? $("#aceptar button[type=\"submit\"]").attr("disabled", false) : $("#aceptar button[type=\"submit\"]").attr("disabled", true);
                                    });
                                } else {
                                    $("#aceptar button[type=\"submit\"]").attr("disabled", false);
                                    $(".aux").html("");
                                }
                            });

                            $("#aceptar").on('submit', function (e) {
                                $('#form-pedidos button[type="submit"]').attr("disabled", true);
                                $('#aceptar button[type="submit"]').attr("disabled", true);
                                e.preventDefault();
                                fd.id_tipo_pago = $(this).find('select[name="select-tipopago"]').val();
                                fd.id_banco = $(this).find('select[name="banco"]').val();
                                fd.nro_movimiento = $("#cupon").val();
                                $("#Modal-pago").modal('hide');
                                $("#Modal-pago").on("hidden.bs.modal", function () {
                                    InsertarOrden(fd, detalle);
                                });
                            });
                        });
                    } else {
                        $('#form-pedidos button[type="submit"]').attr("disabled", true);
                        InsertarOrden(fd, detalle);
                    }

                }
            } else {
                toastr.info("Complete los campos requeridos");
            }
        });

        const InsertarOrden = function (fd, detalle) {
            $.post("pedidosControlador", {
                accion: "insertar",
                datos: JSON.stringify(fd),
                detalle: JSON.stringify(detalle),
                fechahora_entrega: moment(($("input[name='fecha_entrega']").val() + " " + $("input[name='hora_entrega']").val()), "DD-MM-YYYY HH:mm").format('YYYY-MM-DD HH:mm')
            }).done(function (response) {
                if (response !== 0) {
                    Imprimir(response);
                } else {
                    toastr.error("Esto es embarazoso, no se pudo guardar el pedido. Por favor verifique los datos en el formulario.");
                }
            }).fail(function (response, jqxhr, error) {
                toastr.error(error + ", contacte con el desarrollador.");
            });
        };

    };

    const Imprimir = function (data) {
//        swal({
//            title: "¿Imprimir Orden?",
//            text: "",
//            type: "info",
//            showCancelButton: true,
//            confirmButtonColor: "#DD6B55",
//            confirmButtonText: "No Imprimir",
//            cancelButtonText: "Imprimir",
//            closeOnConfirm: false,
//            closeOnCancel: false
//        }, function (rpta) {
//            if (rpta) {
//                swal({html: true,
//                    title: `<div class="progress progress-striped active">
//                                                    <div style="width: 100%" aria-valuemax="100" aria-valuemin="0" aria-valuenow="75" role="progressbar" class="progress-bar progress-bar-info">
//                                                        <span class="sr-only">40% Complete (success)</span>
//                                                    </div>
//                    </div>`,
//                    text: "Esto puede tardar varios minutos. Por favor espere...",
//                    showConfirmButton: false,
//                    allowOutsideClick: false
//                });
//                $.post('pedidosControlador', {
//                    accion: "print",
//                    parametro: data
//                }).done(function (response) {
//                    console.log("Response " + JSON.stringify(response));
//                    $.ajax({
//                        url: "http://localhost:5000/printer",
//                        type: "post",
//                        contentType: "application/json; charset=utf-8",
//                        dataType: "json",
//                        data: JSON.stringify(response),
//                        success: function (data) {
//                            swal.close();
//                            window.history.back();
//                        },
//                        error: function (data, jqxhr, error) {
//                            console.log(data);
//                        }
//                    });
//                }).fail(function (response, jqxhr, error) {
//                    toastr.error(`${response} No se pudo imprimir, contacte con el desarrollador`);
//                });
//            } else {
                swal.close();
                toastr.success("¡GUARDADO!");
                window.history.back();
//            }
//        });
    }
    let AprobarOrdenes = function (tabla) {
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

    exports.FacturarOrden = function (id) {
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
                    //$("#cli_name").val(response.nombre_cli);
                    console.log(response.nombre_cli);
                    //--
                    $("#cli_name").selectize({
                        placeholder: "Buscar nombre del cliente",
                        valueField: 'id_cliente',
                        labelField: 'nombre_cli',
                        searchField: ['nombre_cli', 'razon_social'],
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
                        }, onInitialize: function () {
                            var selectize = this;
                            selectize.addOption(response);
                            selectize.setValue(response.id_cliente);
                        }
                    });
                    //--
                    $("#ruc_cli").val(response.ndocumento_cli);
                    //$("#cli_name").attr("data-id", response.id_cliente);
                    $("button:submit").on('click', function () {
                        exports.CulminarOrden(id);
//                        setTimeout(() => {
//                            $("#aceptar").on('submit', function (e) {
//                                e.preventDefault();
//                                exports.CulminarOrden(id);
//                            });
//                        }, 250);
                    });
                    agregarVenta();
                    BuscarArticulosOrden();
                    date_time("date", "time");
                    currentUser();
                    //buscarClienteVen();
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
                                        '<td><input type="text" class="cant form-control" value=' + response[i].cantidad + ' min="1" required></td>' +
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
                        SetRowNumber();
                        calcularSubtotalVenta();
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

    let AnularOrden = function () {
        $("#tabla-pedidos").on('click', 'a.clo', function () {
            var id = this.getAttribute("data-id");
            let button = this;
            $("#modal").load("vistas/modal_anulados_des.html", function () {
                $(button).attr("disabled", true);
                $("#orden_anulada").modal();
                AgregarDescAnulado("anular", id);
                $("#orden_anulada").on('hidden.bs.modal', function () {
                    $("#tabla-pedidos").DataTable().ajax.reload();
                    $(button).attr("disabled", false);
                });
            });

        });
    };
    let AgregarDescAnulado = function (accion, id) {
        $("#form-anulado-orden").submit(function (e) {
            $("#save").attr("disabled", true);
            e.preventDefault();
            $.post("pedidosControlador", {
                accion: accion,
                id: id,
                motivo_anulado: $("#message-text").val()
            }).done(function (response) {
                if (response == 1) {
                    toastr.success("GUARDADO!");
                    $("#orden_anulada").modal("hide");
                } else {
                    toastr.error("Error Inesperado");
                }
            }).fail(function (response) {
                toastr.error(response);
            });
        });
    };

    const RetirarOrden = function () {
        $("#tabla-pedidos tbody").on('click', "td a.retirar", function (e) {
            let id_pedido = $(this).attr("data-id");
            $.post("pedidosControlador", {
                accion: "retirar",
                id_pedido: id_pedido
            }).done(function (response, jqxhr, error) {
                if (response === 1) {
                    toastr.success("¡LISTO!");
                    $("#tabla-pedidos").DataTable().ajax.reload();
                } else {
                    toastr.error(error, " Ha ocurrido un error. Por favor contacte con el desarrollador.");
                }
            }).fail(function (response, jqxhr, error) {
                toastr.error(error, ". Por favor contacte con el desarrollador.");
            });
        });
    };

    const ModalEstadosOrden = function () {
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
                    $('textarea').html(`*¡Hola ${response.nombre_cli}!😃*\nTe adjunto el detalle de tus prendas, que estarán listas el ${moment(response.fechahora_entrega, "YYYY-MM-DD HH:mm").format("DD/MM/YYYY")} a las ${moment(response.fechahora_entrega, "YYYY-MM-DD HH:mm").format("HH:mm")} 👕👖\n\n¡Gracias por la preferencia!✨\n\n*_SAN FRANCISCO_*\n*lavandería & tintorería*`);
                });
                $("#compartir").on('submit', function (e) {
                    e.preventDefault();
                    let text = $("textarea").serialize();
                    window.open(`https://web.whatsapp.com/send?phone=${$('input[name="codigo"]').val() + $('input[name="phone"]').val()}&${text}`);
                });
            });
        });
    };

    exports.CulminarOrden = function (id) {
        console.log("hola holA");
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

    exports.OrdenRetirada = function (id) {
        $.post("pedidosControlador", {
            accion: "retirado",
            id_pedido: id
        }).done(function (response, jqxhr, error) {
            if (response === 1) {
                toastr.success("¡LISTO!");
                $("#tabla-pedidos").DataTable().ajax.reload();
            } else {
                toastr.error(error, " Ha ocurrido un error. Por favor contacte con el desarrollador.");
            }
        }).fail(function (response, jqxhr, error) {
            toastr.error(error, ". Por favor contacte con el desarrollador.");
        });
    }

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
            var tr = $(this).closest('tr');
            var c = parseInt(tr.find("td input.cant").val());
            $("#cantidad").val(tr.find("td input.cant").val());
            if (parseInt(Entero(tr.find("td input.num").val())) !== 0) {
                VerificarCodigo(this.getAttribute("data-id"));
            }

            tr.find("td input.cant").val(1);
            setTimeout(function () {
                if (parseInt(Entero(tr.find("td input.num").val())) !== 0) {
                    var tabla = $("#" + tart.getAttribute("data-id")).closest('tr');
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
                    var tr = $("#" + response.id_articulo).closest('tr');
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
            let fila = $(tr).find('span[id="' + id + '"]').closest('tr');
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

    const ListarArticulosOrden = function (id) {
        $("div.table-responsive ul.nav.nav-tabs").append(`<li class="active"><a data-toggle="tab" href="#general">GENERAL</a></li>`);
        $("div.table-responsive div.tab-content").append(
                `<div id="general" class="tab-pane active">
                    <div class="panel-body">
                        <div class="table-responsive">
                            <table id="tabla-general" class="table">
                                <thead>
                                    <tr>
                                        <th>Código</th>  
                                        <th>Nombre</th>
                                        <th>Descripción</th>
                                        <th>Cant.</th>
                                        <th>Precio</th>
                                        <th></th>
                                    </tr>
                                </thead>
                            </table>
                        </div>
                    </div>
                </div>`);

        $("#tabla-general").DataTable({
            detroy: true,
            bAutoWidth: false,
            ajax: {
                url: 'stockControlador',
                type: 'post',
                data: {accion: 'listarhabilitados'}
            },
            columns: [
                {data: "codigo_ar",
                    render: function (data, type, full, meta) {
                        return `<span data-id="${full.id_articulo}" class="code">${data}</span>`;
                    }
                },
                {data: "nombre_ar",
                    render: function (data, type, full, meta) {
                        return `<span class="desc">${data}</span>`;
                    }
                },
                {data: "descripcion_ar",
                    render: function (data, type, full, meta) {
                        if ($.isEmptyObject(data)) {
                            return `-`;
                        } else {
                            return data;
                        }
                    }
                },
                {data: null,
                    render: function (data, type, full, meta) {
                        return `<input type="text" name="cantidad" value="0" class="form-control touchspin"/>`;
                    }
                },
                {data: "precio_venta_ar",
                    render: function (data, type, full, meta) {
                        return `<input name="precio" value="${dot(data)}" class="form-control"/>`;
                    }
                }
            ],
            drawCallback: function () {
                $('input[name="cantidad"]').TouchSpin({
                    buttondown_class: 'btn btn-white',
                    buttonup_class: 'btn btn-white'
                });

                $('input[name="cantidad"]').on('change', function () {
                    const row = $(this).closest('tr');
                    const table = $(row.closest('table')).DataTable();
                    CalcularTotalOrden(table.row(row).data(), row);
                });

                $('input[name="cantidad"]').on('input', function () {
                    if (parseInt(this.value) <= 0 || isNaN(parseInt(this.value))) {
                        this.value = 0;
                        this.select();
                    }
                });

                $('input[name="precio"]').on('input', function () {
                    const row = $(this).closest('tr');
                    const table = $(row.closest('table')).DataTable();
                    if (parseInt(this.value) <= 0 || isNaN(parseInt(this.value))) {
                        this.value = 0;
                        this.select();
                    }
                    CalcularTotalOrden(table.row(row).data(), row);
                });
            }
        });

        $.post('categoriasControlador', {
            accion: 'select',
            tipo: 1
        }).done(function (data) {
            $.each(data, function (i) {
                $("#articulos div.table-responsive ul.nav.nav-tabs").append(`<li><a data-toggle="tab" data-id="${data[i].id_categoria}" data-href="tabla-${i}" href="#tab-${i}">${data[i].categoria}</a></li>`);
                $("#articulos div.table-responsive div.tab-content").append(
                        `<div id="tab-${i}" class="tab-pane">
                        <div class="panel-body">
                            <div class="table-responsive">
                                <table id="tabla-${i}" class="table">
                                    <thead>
                                        <tr>
                                            <th>Código</th>  
                                            <th>Nombre</th>
                                            <th>Marca</th>
                                            <th>Cant.</th>
                                            <th>Precio</th>
                                            <th></th>
                                        </tr>
                                    </thead>
                                </table>
                            </div>
                        </div>
                    </div>`);
                $(`#tabla-${i}`).DataTable({
                    destroy: true,
                    bAutoWidth: false,
                    ajax: {
                        type: "post",
                        url: "stockControlador",
                        data: {
                            accion: "orden",
                            id_categoria: data[i].id_categoria
                        }
                    },
                    columns: [
                        {data: "codigo_ar",
                            render: function (data, type, full, meta) {
                                return `<span data-id="${full.id_articulo}" class="code">${data}</span>`;
                            }
                        },
                        {data: "nombre_ar",
                            render: function (data, type, full, meta) {
                                return `<span class="desc">${data}</span>`;
                            }
                        },
                        {data: "nombre_marca",
                            render: function (data, type, full, meta) {
                                if ($.isEmptyObject(data)) {
                                    return `-`;
                                } else {
                                    return data;
                                }
                            }
                        },
                        {data: null,
                            render: function (data, type, full, meta) {
                                return `<input type="text" name="cantidad" value="0" class="form-control touchspin" autocomplete="off"/>`;
                            }
                        },
                        {data: "precio_venta_ar",
                            render: function (data, type, full, meta) {
                                return `<input name="precio" value="${dot(data)}" class="form-control" autocomplete="off"/>`;
                            }
                        }
                    ],
                    drawCallback: function () {
                        $('input[name="cantidad"]').TouchSpin({
                            buttondown_class: 'btn btn-white',
                            buttonup_class: 'btn btn-white'
                        });

                        $('input[name="cantidad"]').on('change', function () {
                            const row = $(this).closest('tr');
                            const table = $(row.closest('table')).DataTable();
                            CalcularTotalOrden(table.row(row).data(), row);
                        });

                        $('input[name="cantidad"]').on('input', function () {
                            if (parseInt(this.value) <= 0 || isNaN(parseInt(this.value))) {
                                this.value = 0;
                                this.select();
                            }
                        });

                        $('input[name="precio"]').on('input', function () {
                            this.value = dot(this.value.replace(/[.+]/g, ""));
                            const row = $(this).closest('tr');
                            const table = $(row.closest('table')).DataTable();
                            if (parseInt(this.value) <= 0 || isNaN(parseInt(this.value))) {
                                this.value = 0;
                                this.select();
                            }
                            CalcularTotalOrden(table.row(row).data(), row);
                        });
                    }
                });
            });
        }).fail(function (response, jqxhr, error) {
            toastr.error(`${error}, contacte con el desarrollador.`);
        }).always(() => {
            $.post("pedidosControlador", {
                accion: "detalle",
                id_pedido: id
            }).done(function (response) {
                $(response).each(function (i) {
                    $('#articulos table').each(function () {
                        const table = $(this).DataTable();
                        table.$(`span[data-id="${response[i].id_articulo}"]`).each(function () {
                            const tr = $(this).closest('tr');
                            tr.find(`input[name="cantidad"]`).val(response[i].cantidad);
                            tr.find(`input[name="precio"]`).val(dot(response[i].precio));
                        });
                    });
                });
            }).fail(function (response, jqxhr, error) {
                toastr.error(`${error}, contacte con el desarrollador`);
            });
        });
    };

    const CalcularTotalOrden = function (data, tr) {
        ObservarCuerpo();
        if (!$("#cuerpo").find(`span.code[id="${data.id_articulo}"]`).length && parseInt(tr.find('input[name="cantidad"]').val()) !== 0) {
            let row = `<tr>
                            <td><span class="code" id=${data.id_articulo}> ${data.codigo_ar} </span></td>
                            <td><input type="text" class="touchspin2 cant" value="${tr.find('input[name="cantidad"]').val()}"  min="1" required></td>
                            <td><span class="desc"> ${data.nombre_ar} </span></td>
                            <td><span class="desc_servicio"> ${data.descripcion_ar} </span></td>
                            <td><input type="text" class="form-control prec" value="${tr.find('input[name="precio"]').val()}" required></td>
                            <td>
                                <select class="form-control" name="id_categoria">
                                </select>
                            </td>
                            <td><span class="iva">${data.iva_ar}</span></td>
                            <td><span class="subtotal">${dot(parseInt(tr.find('input[name="precio"]').val().replace(/[.+]/g, "")) * parseInt(tr.find('input[name="cantidad"]').val()))} </span></td>
                            <td><a class="text-danger" data-id="${data.id_articulo}"><i class="glyphicon glyphicon-trash"></i></a></td>            
                        </tr>`;
            SelectCategoria($("#cuerpo").append(row), data.id_articulo);
        } else {
            if (parseInt(tr.find('input[name="cantidad"]').val()) !== 0) {
                const row = $("#cuerpo").find(`span.code[id="${data.id_articulo}"]`).closest('tr');
                row.find('input.cant').val(tr.find('input[name="cantidad"]').val());
                row.find('input.prec').val(tr.find('input[name="precio"]').val());
                row.find('span.subtotal').html(dot(parseInt(tr.find('input[name="precio"]').val().replace(/[.+]/g, "")) * parseInt(tr.find('input[name="cantidad"]').val())));
            } else {
                $("#cuerpo").find(`span.code[id="${data.id_articulo}"]`).closest('tr').remove();
            }
        }

        $('#articulos table').each(function () {
            const table = $(this).DataTable();
            table.$('span[data-id="' + data.id_articulo + '"]').closest('tr').find('input[name="cantidad"]').off("change");
            table.$('span[data-id="' + data.id_articulo + '"]').closest('tr').find('input[name="cantidad"]').val(tr.find('input[name="cantidad"]').val());
            table.$('span[data-id="' + data.id_articulo + '"]').closest('tr').find('input[name="cantidad"]').on('change', function () {
                const row = $(this).closest('tr');
                const table = $(row.closest('table')).DataTable();
                CalcularTotalOrden(table.row(row).data(), row);
            });
        });

        calcularTotalVenta();
        calcularSubtotalVenta();

        $("#cuerpo tr").on('click', "td a", function (e) {
            let id = this.getAttribute("data-id");
            $(this).closest("tr").remove();
            $('#articulos table').each(function () {
                const table = $(this).DataTable();
                table.$('span[data-id="' + id + '"]').closest('tr').find('input[name="cantidad"]').off("change");
                table.$('span[data-id="' + id + '"]').closest('tr').find('input[name="cantidad"]').val(0);
                table.$('span[data-id="' + id + '"]').closest('tr').find('input[name="cantidad"]').on('change', function () {
                    const row = $(this).closest('tr');
                    const table = $(row.closest('table')).DataTable();
                    CalcularTotalOrden(table.row(row).data(), row);
                });
            });
            calcularTotalVenta();
            calcularSubtotalVenta();
        });
    };

    const ObservarCuerpo = function () {
        // Select the node that will be observed for mutations
        const targetNode = document.getElementById('cuerpo');

        // Options for the observer (which mutations to observe)
        const config = {childList: true, subtree: true};

        // Callback function to execute when mutations are observed
        const callback = function (mutationsList, observer) {
            // Use traditional 'for loops' for IE 11
            for (let mutation of mutationsList) {
                if (mutation.type === 'childList') {
                    $(".touchspin2").TouchSpin({
                        buttondown_class: 'btn btn-white',
                        buttonup_class: 'btn btn-white'
                    });
                }
            }
        };

        // Create an observer instance linked to the callback function
        const observer = new MutationObserver(callback);

        // Start observing the target node for configured mutations
        observer.observe(targetNode, config);
    };

    const SenhaMenosTotal = function () {
        $("input[name='senha_ped']").on('input', function () {
            $("#saldo").html(dot(parseInt($("#tot").html().replace(/[.+]/g, "")) - parseInt(this.value.replace(/[.+]/g, "")) - parseInt($("input[name='descuento_ped']").val().replace(/[.+]/g, ""))));
        });
    };

//    exports.HabilitarOrden = function () {
//        $("#tabla-pedidosAnulado").on('click', '.habilitarOrden', function () {
//            console.log("asdfghjkl;");
//            var id = this.getAttribute("data-id");
//            swal({
//                title: "¿Está seguro?",
//                text: "De Habilitar Orden",
//                type: "warning",
//                showCancelButton: true,
//                confirmButtonColor: "#FFC107",
//                confirmButtonText: "¡Sí, Habilitar ",
//                cancelButtonText: "Cancelar",
//                closeOnConfirm: false
//            }, function () {
//                $.post("pedidosControlador", {
//                    accion: "habilitar",
//                    id: id
//                }).done((response) => {
//                    if (response) {
//                        swal("¡Listo!", "", "success");
//                        $("#tabla-pedidosAnulado").DataTable.ajax.reload();
//                    }
//                }).fail((response, jqxhr, error) => {
//                    alert(error);
//                });
//            });
//        });
//    };
    let HabilitarOrden = function () {
        $("#tabla-pedidosAnulado").on('click', '.habilitarOrden', function () {
            var id = this.getAttribute("data-id");
            let button = this;
            $("#modal").load("vistas/modalHabilitarOrden.html", function () {
                $(button).attr("disabled", true);
                $("#orden_habilitada").modal();
                AgregarDescHabilitada("habilitar", id);
                $("#orden_habilitada").on('hidden.bs.modal', function () {
                    $("#tabla-pedidosAnulado").DataTable().ajax.reload();
                    $(button).attr("disabled", false);
                });
            });

        });
    };
    let AgregarDescHabilitada = function (accion, id) {
        $("#form-orden-habilitada").submit(function (e) {
            $("#save").attr("disabled", true);
            e.preventDefault();
            $.post("pedidosControlador", {
                accion: accion,
                id: id,
                motivo_anulado: $("#message-text").val()
            }).done(function (response) {
                if (response == 1) {
                    toastr.success("GUARDADO!");
                    $("#orden_habilitada").modal("hide");
                } else {
                    toastr.error("Error Inesperado");
                }
            }).fail(function (response) {
                toastr.error(response);
            });
        });
    };

    let pagado = function () {
        $("#tabla-pedidos").on('click', '.pagado', function () {
            var id = this.getAttribute("data-id");
            $("#modal").load("vistas/modal_modo_pago.html", function () {
                $("#Modal-pago").modal();
                exports.modo_pago();

                $.post('pedidosControlador', {
                    accion: 'buscar_orden',
                    id_pedido: id
                }).done(function (response) {
                    CalcularVueltoOrden(response);
                }).fail(function (response, jqxhr, error) {
                    toastr.error(`${error}, algo ha salido muy mal.`);
                });
                AgregarPagos(id, 0);
            });
        });
    };

    let PagadoyRetirado = function () {
        $("#tabla-pedidos").on('click', '.payretired', function () {
            let id = this.getAttribute("data-id");
            $("#modal").load("vistas/modal_modo_pago.html", function () {
                $("#Modal-pago").modal();
                exports.modo_pago();
                $.post('pedidosControlador', {
                    accion: 'buscar_orden',
                    id_pedido: id
                }).done(function (response) {
                    CalcularVueltoOrden(response);
                }).fail(function (response, jqxhr, error) {
                    toastr.error(`${error}, algo ha salido muy mal.`);
                });
                AgregarPagos(id, 'RETIRADO');
            });
        });
    };

    exports.modo_pago = function () {
        $('#Modal-pago').on('shown.bs.modal', function () {
            $("#efectivo").focus().select();
        });

        $("div.col-sm-8.col-sm-offset-2").append(`
                                <div class="form-group">
                                    <div class="col-sm-12">
                                        <input type="text" class="form-control m-b text-center miles" id="efectivo" name="efectivo" required="">
                                    </div>
                                </div>`);

        $(".modal-body").append(`
                    <div class="row">
                        <div class="col-sm-1">
                            <img src="img/manager.png" style="width: 32px; height: 32px;" class="m-b">
                        </div> 
                        <div class="col-sm-4">
                            <p id="customer"></p>
                            <p id="documento"></p>
                        </div>
                        <div class="col-sm-7">
                            <div class="form-group pull-right">
                                <p class="text-green"><b>Vuelto</b> </p>
                                <h1 class="text-green" id="cambio"><b>0 Gs.</b></h1>
                            </div>
                        </div>
                    </div>`);

        $("#select-tipopago").on('change', function () {
            if (parseInt($(this).val()) === 2 || parseInt($(this).val()) === 6) {
                $("img[src='img/manager.png']").closest("div.row").hide();
                $("#efectivo").closest("div.form-group").hide();
                $(".aux").html(`<center>
                                    <label class="control-label m-b" for="efectivo">Banco:</label>
                                </center>
                                <div class="col-sm-12">
                                    <select name="banco" class="form-control" required>
                                    </select>
                                </div>
                                <center>
                                    <label class="control-label m-b" for="efectivo">Nro de Comprobante:</label>
                                </center>
                                <div class="col-sm-12">
                                    <input type="text" class="form-control m-b text-center miles" id="cupon" autocomplete="off" required>
                                </div>`);
                exports.SelectizeBanco();
                $("#aceptar button[type=\"submit\"]").attr("disabled", true);

                $('select[name="banco"]').on('change', function () {
                    this.value ? $("#cupon").val() ? $("#aceptar button[type=\"submit\"]").attr("disabled", false) : $("#aceptar button[type=\"submit\"]").attr("disabled", true) : $("#aceptar button[type=\"submit\"]").attr("disabled", true);
                });

                $("#cupon").on('input', function () {
                    this.value ? $('select[name="banco"]').val() ? $("#aceptar button[type=\"submit\"]").attr("disabled", false) : $("#aceptar button[type=\"submit\"]").attr("disabled", true) : $("#aceptar button[type=\"submit\"]").attr("disabled", true);
                });
            } else if (parseInt($(this).val()) === 3 || parseInt($(this).val()) === 4 || parseInt($(this).val()) === 5) {
                $("#aceptar button[type=\"submit\"]").attr("disabled", true);
                $("img[src='img/manager.png']").closest("div.row").hide();
                $("#efectivo").closest("div.form-group").hide();
                $(".aux").html(`<center><label class="control-label m-b" for="efectivo">Nro de Comprobante:</label></center>
                                    <div class="col-sm-12">
                                        <input type="text" class="form-control m-b text-center miles" id="cupon" autocomplete="off" required>
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

    const CalcularVueltoOrden = function (response) {
        if (response) {
            $("#customer").html(response.nombre_cli);
            $("#documento").html(response.ndocumento_cli);
            $("#pagar").html("<b>" + (response.total_ped - response.senha_ped - response.descuento_ped) + " Gs.</b>");
            $("#letras").html("<b>" + NumeroALetras((response.total_ped - response.senha_ped - response.descuento_ped)) + "</b>");
            $("#efectivo").val(dot((response.total_ped - response.senha_ped - response.descuento_ped)));

            document.getElementById("efectivo").oninput = function () {
                var v = calcularVuelto(parseInt(this.value.replace(/[.+]/g, "")), parseInt((response.total_ped - response.senha_ped)));
                if (v < 100000) {
                    $("#cambio").html("<b>" + dot(v) + "</b>");
                } else {
                    var aux = Entero(this.value);
                    $("#efectivo").val(aux.substring(0, aux.length - 1));
                }

                if (this.value !== "") {
                    parseInt(this.value.replace(/[.+]/g, "")) < parseInt((response.total_ped - response.senha_ped)) ? $("#aceptar button[type=\"submit\"]").attr("disabled", true) : $("#aceptar button[type=\"submit\"]").attr("disabled", false);
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
        }
    };

    const AgregarPagos = function (id, retirado) {
        $("#aceptar").submit(function (e) {
            $("button:submit").attr('disabled', true);
            e.preventDefault();
            $.post("pedidosControlador", {
                accion: "pago_total",
                id: id,
                retirado: retirado,
                tipo_pago: $(this).find('select[name="select-tipopago"]').val(),
                banco: $(this).find('select[name="banco"]').val(),
                numero_operacion: $("#cupon").val()
            }).done(function (response) {
                if (response === 1) {
                    $("#tabla-pedidos").DataTable().ajax.reload();
                    toastr.success("¡GUARDADO!");
                    $("#Modal-pago").modal("hide");
                } else {
                    $("button:submit").attr('disabled', false);
                    toastr.error("Error Inesperado");
                }
            }).fail(function (response) {
                toastr.error(response);
            });
        });
        $("button.close").on('click', function (e) {
            $("button.save").attr("disabled", false);
        });
    };

    exports.SelectizeBanco = function () {
        $('select[name="banco"]').selectize({
            placeholder: 'Buscar banco ...',
            valueField: 'id_banco',
            labelField: 'nombre_banco',
            searchField: ['nombre_banco'],
            options: [],
            create: false,
            load: function (query, callback) {
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
    const DescMenosTotal = function () {
        $("input[name='descuento_ped']").on('input', function () {
            $("#descuento").html(dot($("input[name='descuento_ped']").val().replace(/[.+]/g, "")));
            $("#saldo").html(dot(parseInt($("#tot").html().replace(/[.+]/g, "")) - parseInt($("input[name='senha_ped']").val().replace(/[.+]/g, "")) - parseInt(this.value.replace(/[.+]/g, ""))));
        });
    };

    return exports;
});