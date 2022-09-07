function cargarCliente() {
    spinneron();
    $(".level").parent().find("ul.nav").removeClass("nav nav-second-level collapse in").addClass("nav nav-second-level collapse");
    $("#inicio").load("vistas/clientes.html", {}, function () {
        $(".active").removeClass("active");
        $(".client").parent().addClass("active");
        PermisosRender(2, (p) => {
            if (p.agregar === 'N') {
                $('a[href="#/clientes/nuevo"]').remove();
            }
        });
        spinneroff();
        listarClientes();
        CompartirClientes();
        VerCliente();
    });
}

function nuevoCliente() {
    $('a[href="#/clientes/nuevo"]').attr('disabled', true);
    $("#modal").load("vistas/modal_cli.html", function () {
        $("#Modal-cli").modal();

        $("#nombre_cli").on('change', function () {
            $('input[name="razon_social"]').val() === "" ? $('input[name="razon_social"]').val(this.value) : "";
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
        $("#Modal-cli").on('hidden.bs.modal', function (e) {
            window.history.back();
        });
        $('.date input[name="fecha_nac"]').datepicker({
            format: "dd/mm/yyyy"
        });
    });
}

function listarClientes() {
    $("#table-clientes").DataTable({
        "pageLength": 100,
        destroy: true,
        dom: '<"html5buttons"B>lTfgitp',
        buttons: [
            {extend: 'excel', title: 'Clientes',
                exportOptions: {
                    columns: 'th:not(:last-child)'
                }
            },
            {extend: 'pdf', title: 'Clientes',
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
            url: "clientesControlador",
            data: {accion: "listar"}
        },
        columns: [
            {data: "id_cliente"},
            {data: "nombre_cli"},
            {data: "ndocumento_cli",
                render: function (data, type, full, meta) {
                    if (full.ndocumento_cli !== "-") {
                        return full.ndocumento_cli + "-" + full.dv_cli;
                    } else {
                        return "-";
                    }
                }
            },
            {data: "celular_cli"},
            {data: "direccion_cli",
                render: function (data, type, full, meta) {
                    return `<a href="${full.googlemaps_cli}" target="_blank">${data}</a>`;
                }
            },
            {data: "nombre_ciu"},
            {data: null,
                render: function (data, type, full, meta) {
                    return `<div class="btn-group">
                                <a class="btn btn-default btn-xs share" data-id="${data.id_cliente}"><i class="fab fa-whatsapp"></i> Compartir</a>
                                <a class="btn btn-primary btn-xs" href="#/clientes/editar?_id=${data.id_cliente}"><i class="far fa-edit"> Editar</i></a>
                                <a class="btn btn-warning btn-xs clo" data-id=${data.id_cliente}><i class="fa fa-ban"> Inhabilitar</i></a>
                                <a class="btn btn-info btn-xs" href="#/clientes/historial?_id=${data.id_cliente}"><i class="fas fa-history"></i> Historial</a>
                                <a class="btn btn-default btn-xs ver" data-id="${data.id_cliente}"><i class="fas fa-eye"></i> Ver</a>
                            </div>`;
                }
            }
        ],
        initComplete: function () {
            PermisosRender(2, (p) => {
                if (p.editar === 'N') {
                    $('.editar').remove();
                }
                if (p.eliminar === 'N') {
                    $('.clo').remove();
                }
            });
        }
    });
    cerrarClientes("cerrar");
}

function AgregarClientes(accion, id) {
    $("#form-cli").submit(function (e) {
        $("#save").attr('disabled', true);
        e.preventDefault();
        $.post("clientesControlador", {
            accion: accion,
            id_cliente: id,
            nombre_cli: $("#nombre_cli").val(),
            ndocumento_cli: $("#ndocumento_cli").val(),
            dv_cli: $("#dv_cli").val(),
            telefono_cli: $("#telefono_cli").val(),
            celular_cli: $("#celular_cli").val(),
            direccion_cli: $("#direccion_cli").val(),
            email_cli: $("#email_cli").val(),
            googlemaps_cli: $("#google_maps").val(),
            descuento_cli: $('input[name="descuento_cli"]').val(),
            observacion_cli: $("#observacion_cli").val(),
            fecha_nac: $("#fecha_nac ").val(),
            id_ciudad: $("#ciudad").val(),
            razon_social: $('input[name="razon_social"]').val(),
            ruc: $('input[name="ruc"]').val(),
            ruc_dv: $('input[name="ruc_dv"]').val(),
            referencia_cli: $('input[name="referencia_cli"]').val()
        }, function (response) {
            if (response) {
                if (response === 1) {
                    toastr.success("¡GUARDADO!");
                    $("#Modal-cli").modal("hide");
                    buscarClienteVen();
                } else if (response === 30001) {
                    toastr.error("NO POSEE LOS PERMISOS SUFICIENTES PARA REALIZAR ESTA ACCIÓN");
                }
            } else {
                toastr.error("Error Inesperado");
            }
        });
    });
}

function completarClientes(callback) {
    $.post("clientesControlador", {accion: "completar"}, function (response) {
        var x = [];
        var y = [];
        for (var i = 0; i < response.length; i++) {
            x[i] = {
                name: response[i].razon_social,
                ruc_cli: response[i].ruc + "-" + response[i].ruc_dv,
                id_cliente: response[i].id_cliente
            };
            y[i] = {
                name: response[i].ruc + "-" + response[i].ruc_dv,
                nombre_cli: response[i].razon_social,
                id_cliente: response[i].id_cliente
            };
        }
        callback(x, y);
    });
}

function editarClientes(id) {
    $("a.editar").attr("disabled", true);
    $("#modal").load("vistas/modal_cli.html", function () {
        $("#Modal-cli").modal();
        $("#ndocumento_cli").on('change', function () {
            ValidarClientes();
        });
        $('.date input[name="fecha_nac"]').datepicker({
            format: "dd/mm/yyyy"
        });
        AgregarClientes(1, id);
        listarCiudades("select");
        $("#ciudad").select2({
            dropdownParent: $(".parent"),
            placeholder: "Seleccione la ciudad",
            allowClear: true
        });
        ruc();

        $.post("clientesControlador", {
            accion: "buscar",
            id_cliente: id
        }, function (response) {
            $("#nombre_cli").val(response.nombre_cli);
            $("#ndocumento_cli").val(response.ndocumento_cli);
            $("#dv_cli").val(response.dv_cli);
            $("#telefono_cli").val(response.telefono_cli);
            $("#celular_cli").val(response.celular_cli);
            $("#direccion_cli").val(response.direccion_cli);
            $("#observacion_cli").val(response.observacion_cli);
            $("#google_maps").val(response.googlemaps_cli);
            $("#email_cli").val(response.email_cli !== "-" ? response.email_cli : "");
            $("input[name='descuento_cli']").val(response.descuento_cli);
            $("#fecha_nac").val(response.fecha_nac);
            $('input[name="razon_social"]').val(response.razon_social);
            $('input[name="ruc"]').val(response.ruc);
            $('input[name="ruc_dv"]').val(response.ruc_dv);
            $('input[name="referencia_cli"]').val(response.referencia_cli);
            setTimeout(() => {
                $("#ciudad").val(response.id_ciudad).trigger("change");
            }, 250);
        });

        $("#Modal-cli").on('hidden.bs.modal', function (e) {
            window.history.back();
        });
    });
}

function cerrarClientes(accion) {
    $("#table-clientes tbody").on('click', '.clo', function () {
        $(this).attr("disabled", true);
        var id = $(this).attr("data-id");
        $.post("clientesControlador", {
            accion: accion,
            id_cliente: id
        }).done(function (response) {
            if (response == 1) {
                toastr.success("¡Listo!");
                if (accion == "cerrar") {
                    listarClientes();
                } else if (accion == "habilitar") {
                    listarClientesInhabilitados();
                }
            }
        }).fail(function (response, jqxhr, error) {
            toastr.error(error);
        });
    });
}

function listarClientesInhabilitados() {
    spinneron();
    $("#inicio").load("vistas/clientes.html", {}, function () {
        spinneroff();
        $(".active").removeClass("active");
        $("a[href='#/anulados/articulos']").parent().find("ul.nav").removeClass("nav nav-second-level collapse in").addClass("nav nav-second-level collapse");
        $('a[href="#/anulados/articulos"]').closest("ul").closest("li").addClass("active");
        $('a[href="#/clientes/nuevo"]').remove();
        $("#table-clientes").DataTable({
            "pageLength": 100,
            destroy: true,
            dom: '<"html5buttons"B>lTfgitp',
            buttons: [
                {extend: 'excel', title: 'Clientes Inhabilitados'},
                {extend: 'pdf', title: 'Clientes Inhabilitados'},

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
                url: "clientesControlador",
                data: {accion: "inhabilitados"}
            },
            columns: [
                {data: "id_cliente"},
                {data: "nombre_cli"},
                {data: "ndocumento_cli",
                    render: function (data, type, full, meta) {
                        return full.ndocumento_cli + "-" + full.dv_cli;
                    }
                },
                {data: "celular_cli"},
                {data: "direccion_cli"},
                {data: "nombre_ciu"},
                {data: null,
                    render: function (data, type, full, meta) {
                        return '<div class="btn-group"><button class="btn btn-primary btn-xs clo" data-id="' + data.id_cliente + '"><i class="fa fa-check"> Habilitar</i></button><button class="btn btn-info btn-xs see" data-id="' + data.id_cliente + '"><i class="fa fa-eye"> Ver</i></button></div>';
                    }
                }
            ]
        });
        verClientes();
        cerrarClientes("habilitar");
    });
}

function verClientes() {
    $("#table-clientes tbody").on('click', ".see", function () {
        var id = $(this).attr("data-id");
        $("#modal").load("vistas/modal_cli.html", function () {
            $("#Modal-cli").modal();
            listarCiudades("select");
            $("#save").remove();
            $("#ciudad").select2({
                dropdownParent: $(".parent"),
                placeholder: "Seleccione la ciudad",
                allowClear: true
            });
            ruc();
            $.post("clientesControlador", {
                accion: "buscar",
                id_cliente: id
            }, function (response) {
                $("#nombre_cli").val(response.nombre_cli).attr("readOnly", true);
                $("#ndocumento_cli").val(response.ndocumento_cli).attr("readOnly", true);
                $("#dv_cli").val(response.dv_cli).attr("readOnly", true);
                $("#telefono_cli").val(response.telefono_cli).attr("readOnly", true);
                $("#celular_cli").val(response.celular_cli).attr("readOnly", true);
                $("#direccion_cli").val(response.direccion_cli).attr("readOnly", true);
                $("#observacion_cli").val(response.observacion_cli).attr("readOnly", true);
                $("#ciudad").val(response.id_ciudad).trigger("change").attr("disabled", true);
            });
        });
    });
}

function ValidarClientes() {
    $.post("clientesControlador", {
        accion: "validar",
        ndoc: $("#ndocumento_cli").val()
    }).done(function (response) {
        if (response.id_cliente) {
            $(".ruc").remove();
            $("#ndocumento_cli").parent().parent().append(`<div class="ruc col-sm-8 col-sm-offset-3">
                                            <label class="control-label text-danger"><i class="fa fa-exclamation-circle"></i> El cliente ya esta registrado como ${response.nombre_cli}</label>
                                        </div>`);
            $("#save").prop("disabled", true);
        } else {
            $(".ruc").remove();
            if ($('input[name="ruc"]').val() === "") {
                $('input[name="ruc"]').val($("#ndocumento_cli").val()).trigger('change');
                $('input[name="ruc_dv"]').val($("#dv_cli").val());
            }

            $("#save").prop("disabled", false);
        }
    }).fail(function (response, jqxhr, error) {
        toastr.error(error + ", contacte con el desarrollador");
    });
}


const CompartirClientes = function () {
    $("#table-clientes tbody").on('click', ".share", function () {
        let id = this.getAttribute("data-id");
        $("#modal").load("vistas/modal_compartircliente.html", function () {
            $("#share_modal").modal();
            SeleccionarPais();
            $.post("clientesControlador", {
                accion: "buscar",
                id_cliente: id
            }, function (response) {
                $("textarea").html(response.nombre_cli + "\n" + response.googlemaps_cli);
            });

            $("#compartir").on('submit', function (e) {
                e.preventDefault();
                let text = $("textarea").serialize();
                window.open(`https://web.whatsapp.com/send?phone=${$('input[name="codigo"]').val() + $('input[name="phone"]').val()}&${text}`);
            });
        });
    });
};

const SelectPais = function () {
    const selectize = $('select[name="pais"]').selectize({
        placeholder: "País",
        valueField: 'phone_code',
        labelField: 'nombre_pais',
        searchField: 'nombre_pais',
        create: false,
        load: function (query, callback) {
            if (!query.length)
                return callback();
            $.post('paisesControlador', {
                accion: 'query',
                q: query
            }).success((response, jqxhr, error) => {
                callback(response);
            }).fail((response, jqxhr, error) => {
                console.log(response);
            });
        },
        onInitialize: function () {
            var selectize = this;
            selectize.addOption({nombre_pais: "Paraguay", phone_code: "595"});
            selectize.setValue("595");
        }
    });
    return selectize;
};

const SeleccionarPais = function () {
    const select = SelectPais();
    const selectize = select[0].selectize;

    const CambiarPais = function () {
        $('select[name="pais"]').on('change', function () {
            $('input[name="codigo"]').val(this.value);
        });
    };

    CambiarPais();

    $('input[name="codigo"]').on('input', function () {
        let codigo = this;
        $.post('paisesControlador', {
            accion: 'phonecode',
            phone_code: codigo.value
        }).success((response, jqxhr, error) => {
            if (response.phone_code) {
                CambiarPais();
                selectize.addOption(response);
                selectize.setValue(response.phone_code);
            } else {
                $('select[name="pais"]').off('change');
                selectize.addOption({nombre_pais: "Desconocido", phone_code: ""});
                selectize.setValue("");
            }
        }).fail((response, jqxhr, error) => {
            console.log(response);
        });
    });
};

const Historial = function (id) {
    $("#inicio").load('vistas/historial_cliente.html', function () {
        $("#table-clientes-historial").DataTable({
            "pageLength": 100,
            destroy: true,
            order: [[1, 'desc']],
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
                data: {accion: "clientes_historial", id: id}
            },
            columns: [
                {data: null,
                    render: function (data, type, full, meta) {
                        return `<input type="checkbox" name="orden" value="${full.id_pedido}"/>`
                    }
                },
                {data: "id_pedido",
                    render: function (data, type, full, meta) {
                        return cero(data.toString());
                    }
                },
                {data: "fecha_ped"},
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
                }
            ]
        });
        FacturarSeleccionados(id);
        FacturarHistorial(id);
    });

    const FacturarSeleccionados = function (id) {
        $("a.facturar").on('click', function () {
            let table = $("#table-clientes-historial").DataTable();
            if (table.$("input:checked").length) {
                let ids = [];
                table.$("input:checked").each(function (i) {
                    ids[i] = parseInt(this.value);
                });
                swal({
                    title: "¿Que desea hacer?",
                    text: "",
                    type: "info",
                    showCancelButton: true,
                    confirmButtonColor: "#F44336",
                    confirmButtonText: "Generar PDF",
                    cancelButtonText: "Descargar Excel"
                }, function (rpta) {
                    if (rpta) {
                        var newwindow = window.open("ResumenControlador?accion=historial&cliente=" + id + "&ids=" + JSON.stringify(ids).replace(/(\[|\])+/g, ""), "", "width=1200,height=780");
                        return newwindow;
                    } else {
                        window.location = "ResumenControlador?accion=historialxls&cliente=" + id + "&ids=" + JSON.stringify(ids).replace(/(\[|\])+/g, "");
                    }
                });

            } else {
                toastr.info("Seleccione las ordenes para generar el reporte");
            }
        });
    };
};

const FacturarHistorial = function (id) {
    $("a.invoice").on('click', function () {
        let table = $("#table-clientes-historial").DataTable();
        if (table.$("input:checked").length) {
            let ids = [];
            table.$("input:checked").each(function (i) {
                ids[i] = parseInt(this.value);
            });
            $.post("pedidosControlador", {
                accion: "verificar_historial",
                ids: JSON.stringify(ids).replace(/(\[|\])+/g, ""),
                id_cliente: id
            }).done(function (response) {
                if (response === 1) {
                    toastr.warning("Se encontraron ordenes facturadas entre las seleccionadas, por favor verifiquelas.");
                } else {
                    $.post("pedidosControlador", {
                        accion: "facturar_historial",
                        ids: JSON.stringify(ids).replace(/(\[|\])+/g, ""),
                        id_cliente: id
                    }).done(function (response) {
                        toastr.clear();
                        if (response) {
                            $("#inicio").load("vistas/insertVenta.html", function () {
                                var date = new Date();
                                var d = date.getDate();
                                var m = date.getMonth() + 1;
                                var y = date.getFullYear();
                                $("#fecha").val(toDate(d) + "/" + toDate(m) + "/" + y);
                                $("#cli_name").val(response.razon_social);
                                $("#ruc_cli").val(response.ruc);
                                $("#cli_name").attr("data-id", response.id_cliente);

                                agregarVenta();

                                date_time("date", "time");
                                currentUser();
                                buscarClienteVen();
                                $("#buscar").on('submit', function (e) {
                                    e.preventDefault();
                                    VerificarCodigo($("#codigo").val());
                                });

                                $("#form-venta").on('submit', function (e) {
                                    e.preventDefault();
                                    setTimeout(() => {
                                        $.post('pedidosControlador', {
                                            accion: 'OrdenesFacturadas',
                                            ides: JSON.stringify(ids)
                                        }).success((response, jqxhr, error) => {
                                            if (response == 1) {
                                                toastr.success("Facturados!");
                                                $("#table-clientes-historial").DataTable().ajax.reload();

                                            } else {
                                                toastr.error("Error al Facturar!");
                                            }
                                        }).fail((response, jqxhr, error) => {
                                            console.log(response);
                                        });
                                    }, 250);
                                });

                                UltimoNroFactura();
                                $.post("pedidosControlador", {
                                    accion: 'detalle_historial',
                                    ids: JSON.stringify(ids).replace(/(\[|\])+/g, "")
                                }).done(function (response) {
                                    if (response) {
                                        $("#cuerpo").attr('data-id', JSON.stringify(ids).replace(/(\[|\])+/g, ""));
                                        for (var i = 0; i < response.length; i++) {
                                            $("#cuerpo").append('<tr>' +
                                                    '<td class="rowNumber">' + (i + 1) + '</td>' +
                                                    '<td><span class="code" id=' + response[i].id_articulo + '>' + response[i].codigo + '</span></td>' +
                                                    '<td><input type="text" class="cant form-control" value=' + response[i].cantidad + ' min="1" required></td>' +
                                                    '<td><span class="desc">' + response[i].descripcion + '</span></td>' +
                                                    '<td><input type="text" class="form-control prec" value="' + dot(response[i].precio) + '" onclick="this.select()" required></td>' +
                                                    '<td><input type="text" class="form-control descuento" value="0"></td>' +
                                                    '<td><span class="iva">' + response[i].iva + '</span></td>' +
                                                    '<td><span class="subtotal">' + dot(response[i].subtotal) + '</span></td>' +
                                                    '<td><a class="text-danger"><i class="glyphicon glyphicon-trash"></i></a></td>' +
                                                    '</tr>');
                                        }
                                        $("#cuerpo_info").html(`Mostrando ${$("#cuerpo tr").length} registros`);
                                    } else {
                                        toastr.error("Esto es embarazoso, no se pudo guardar el pedido. Por favor verifique los datos en el formulario.");
                                    }
                                }).fail(function (response, jqxhr, error) {
                                    toastr.error(error + ", contacte con el desarrollador.");
                                }).always(() => {
                                    $("#cuerpo tr").on('click', "td a.text-danger", function (e) {
                                        e.preventDefault();
                                        $(this).closest("tr").remove();
                                        calcularTotalVenta();
                                        SetRowNumber();
                                        $("#cuerpo_info").html(`Mostrando ${$("#cuerpo tr").length} registros`);
                                        return false;
                                    });

                                    calcularSubtotalVenta();
                                    calcularTotalVenta();
                                    $("#totaldescuento").html(dot(response.descuento_ped));
                                    $("#tot").html(dot(Entero($("#tot").html()) - response.descuento_ped));
                                });
                            });
                        } else {
                            toastr.error("Esto es embarazoso, no se pudo generar la factura. Por favor contacte con el desarrollador.");
                        }
                    }).fail(function (response, jqxhr, error) {
                        toastr.error(error + ", error al tratar de facturar la/s orden/es.");
                    });
                }
            }).fail((response) => {
                toastr.error("Error al verificar la/s orden/es.");
            })
        } else {
            toastr.info("Seleccione las ordenes para generar el reporte");
        }
    });
};

const VerCliente = function () {
    $("#table-clientes tbody").on('click', '.ver', function () {
        let id = this.getAttribute('data-id');
        $("#table-clientes").closest("div.col-sm-12").removeClass('col-sm-12').addClass('col-sm-8');
        $("div.client-detail").closest("div.col-sm-4").show();
        $.post("clientesControlador", {
            accion: "buscar",
            id_cliente: id
        }).done((response) => {
            $('div.full-height-scroll').html(`
                                        <strong>Datos del Cliente</strong>
                                        <ul class="list-group clear-list">
                                            <li class="list-group-item fist-item">
                                                <span class="pull-right"> ${response.nombre_cli} </span>
                                                Nombre
                                            </li>
                                            <li class="list-group-item">
                                                <span class="pull-right"> ${response.ndocumento_cli} </span>
                                                CI.
                                            </li>
                                            <li class="list-group-item">
                                                <span class="pull-right"> ${response.razon_social} </span>
                                                Razón Social
                                            </li>
                                            <li class="list-group-item">
                                                <span class="pull-right"> ${response.ruc}-${response.ruc_dv} </span>
                                               RUC
                                            </li>
                                            <li class="list-group-item">
                                                <span class="pull-right">${response.celular_cli} </span>
                                                Celular
                                            </li>
                                            <li class="list-group-item">
                                                <span class="pull-right">${response.telefono_cli} </span>
                                                Teléfono
                                            </li>
                                            <li class="list-group-item" style="padding:10px">
                                                <div class="row">
                                                    Dirección                                    
                                                    <span class="pull-right">${response.direccion_cli} </span>
                                                </div>
                                            </li>
                                            <li class="list-group-item" style="padding:10px">
                                                <div class="row">
                                                    Referencia                                    
                                                    <span class="pull-right">${response.referencia_cli} </span>
                                                </div>
                                            </li>
                                             <li class="list-group-item">
                                                <span class="pull-right">${response.nombre_ciu} </span>
                                                Ciudad
                                            </li>
                                            <li class="list-group-item">
                                                <span class="pull-right">${response.email_cli} </span>
                                                E-mail
                                            </li>
                                            <li class="list-group-item">
                                                <span class="pull-right">${response.fecha_nac} </span>
                                                Fecha de Nacimiento
                                            </li>            
                                        </ul>
                                        <strong>Observaciones</strong>
                                        <p>${response.observacion_cli}</p>
                                        <hr/>
                                `);

        }).fail((response, jqxhr, error) => {
            console.log(error);
        });
        $("button.close").on('click', function () {
            $(this).closest("div.col-sm-4").hide();
            $("#table-clientes").closest("div.col-sm-8").removeClass('col-sm-8').addClass('col-sm-12');
        });
    });
};