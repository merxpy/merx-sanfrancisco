function ordenCompra() {
    spinneron();
    $("#inicio").load("vistas/ordencompra.html", {}, function () {
        $(".active").removeClass("active");
        $(".level").parent().find("ul.nav").removeClass("nav nav-second-level collapse in").addClass("nav nav-second-level collapse");
        $(".order").parent().addClass("active");
        $("#t-3").addClass("active");
        $("#tab-3").addClass("active");
        spinneroff();
        aprobarOrden();
        GenerarCompra();
        listarOrdenes();
        listarOrdenesAprobadas();
        listarOrdenesRecibidas();

        anularOrden("anular", "#table-ord");
    });
}

function listarOrdenes() {
    $("#table-ord").DataTable({
        destroy: true,
        dom: '<"html5buttons"B>lTfgitp',
        buttons: [
            {extend: 'excel', title: 'Ordenes de Compra',
                exportOptions: {
                    columns: 'th:not(:last-child)'
                }
            },
            {extend: 'pdf', title: 'Ordenes de Compra',
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
            url: "ordencompraControlador",
            data: {accion: "listar"}
        },
        columns: [
            {data: "id_orden_compra",
                render: function (data, type, full, meta) {
                    return cero(data);
                }
            },
            {data: "fecha_ord"},
            {data: "nombre_pro"},
            {data: "aprobado_ord",
                render: function (data, type, full, meta) {
                    if (data == 0) {
                        return '<div class="lebal"><label class="label label-danger est">Pendiente</label></div>';
                    } else {
                        return '<div class="lebal"><label class="label label-info est">Aprobado</label></div>';
                    }
                }
            },
            {data: "total_ord",
                render: function (data, type, full, meta) {
                    return dot(data);
                }
            },
            {data: null,
                render: function (data, type, full, meta) {
                    return '<div class="btn-group"><a class="btn btn-default btn-xs" href="#/ordencompras/editar?_id=' + data.id_orden_compra + '"><i class="far fa-edit"></i> Editar</a><a class="btn btn-primary btn-xs ap" data-id=' + data.id_orden_compra + '><i class="fa fa-check-circle"></i> Aprobar</a><a class="btn btn-info btn-xs nulled" data-id=' + data.id_orden_compra + '><i class="fa fa-ban"></i> Anular</a></div>';
                }
            }
        ]
    });
}

function listarOrdenesAprobadas() {
    visualisarAp();
    $("#table-ord-ap").DataTable({
        destroy: true,
        dom: '<"html5buttons"B>lTfgitp',
        buttons: [
            {extend: 'excel', title: 'Ordenes de Compras Aprobadas',
                exportOptions: {
                    columns: 'th:not(:last-child)'
                }
            },
            {extend: 'pdf', title: 'Ordenes de Compras Aprobadas',
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
            url: "ordencompraControlador",
            data: {accion: "listarap"}
        },
        columns: [
            {data: "id_orden_compra",
                render: function (data, type, full, meta) {
                    return cero(data);
                }
            },
            {data: "fecha_ord"},
            {data: "nombre_pro"},
            {data: "aprobado_ord",
                render: function (data, type, full, meta) {
                    if (data == 0) {
                        return '<div class="lebal"><label class="label label-danger est">Pendiente</label></div>';
                    } else {
                        return '<div class="lebal"><label class="label label-info est">Aprobado</label></div>';
                    }
                }
            },
            {data: "total_ord",
                render: function (data, type, full, meta) {
                    return dot(data);
                }
            },
            {data: null,
                render: function (data, type, full, meta) {
                    if (data.aprobado_ord == 1) {
                        return '<div class="btn-group"><a class="btn btn-default btn-xs" onclick="FuncionImprimirOrden(' + data.id_orden_compra + ')"><i class="fa fa-eye"></i> Ver</a><a class="btn btn-primary btn-xs rec" data-id=' + data.id_orden_compra + '><i class="glyphicon glyphicon-download"></i> Recibido</a><a class="btn btn-info btn-xs nulled" data-id=' + data.id_orden_compra + '><i class="fa fa-ban"></i> Anular</a></div>';
                    } else {
                        return '<div class="btn-group"><button class="btn btn-default btn-xs ed" data-id=' + data.id_orden_compra + '><i class="far fa-edit"></i> Editar</button><button class="btn btn-primary btn-xs ap" data-id=' + data.id_orden_compra + '><i class="fa fa-check-circle"></i> Aprobar</button><button class="btn btn-info btn-xs" data-id=' + data.id_orden_compra + '><i class="fa fa-ban"></i> Anular</button></div>';
                    }
                }
            }
        ]
    });
}

function listarOrdenesRecibidas() {
    visualisarRec();
    $("#table-ord-rec").DataTable({
        destroy: true,
        dom: '<"html5buttons"B>lTfgitp',
        buttons: [
            {extend: 'excel', title: 'Ordenes de Compras Recibidas',
                exportOptions: {
                    columns: 'th:not(:last-child)'
                }
            },
            {extend: 'pdf', title: 'Ordenes de Compras Recibidas',
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
            url: "ordencompraControlador",
            data: {accion: "listarrec"}
        },
        columns: [
            {data: "id_orden_compra",
                render: function (data, type, full, meta) {
                    return cero(data);
                }
            },
            {data: "fecha_ord"},
            {data: "nombre_pro"},
            {data: "aprobado_ord",
                render: function (data, type, full, meta) {
                    if (data == 0) {
                        return '<div class="lebal"><label class="label label-danger est">Pendiente</label></div>';
                    } else {
                        return '<div class="lebal"><label class="label label-info est">Aprobado</label></div>';
                    }
                }
            },
            {data: "total_ord",
                render: function (data, type, full, meta) {
                    return dot(data);
                }
            },
            {data: null,
                render: function (data, type, full, meta) {
                    if (data.aprobado_ord == 1) {
                        return '<a class="btn btn-default btn-xs" onclick="FuncionImprimirOrden(' + data.id_orden_compra + ')"><i class="fa fa-eye"></i> Ver</a>';
                    }
                }
            }
        ]
    });
}

function nuevaOrden() {
    $("#inicio").load("vistas/insertOrden.html", {}, function () {
        orderCrumb();
        newProveedor();
        buscar();
        insertOrden(0, 0);
    });
}

function insertOrden(accion, id) {
    $("#form-orden").submit(function (e) {
        e.preventDefault();
        var detalle = [];

        $("#cuerpo tr").each(function (i) {
            if ($(this).find("input.cod").attr("data-id")) {
                detalle[i] = {
                    id_articulo: $(this).find("input.cod").attr("data-id"),
                    codigo_ord: $(this).find("input.cod").val(),
                    descripcion_ord: $(this).find("input.nom").val(),
                    cantidad_ord: $(this).find("input.cant").val(),
                    precio_ord: Entero($(this).find("input.price").val()),
                    iva_ord: Entero($(this).find("select.iva").val()),
                    subtotal_ord: Entero($(this).find("input.total").val())
                };
            }
        });

        $.post("ordencompraControlador", {
            accion: accion,
            id_orden_compra: id,
            id_proveedor: $("#ruc").attr("data-id"),
            observacion_ord: $("#observacion").val(),
            total_ord: Entero($("#tot").val()),
            total_iva_ord: Entero($("#ti").val()),
            comentario_ord: $("#observacion").val(),
            detalle_ord: JSON.stringify(detalle)
        }).done(function (response) {
            if (response == 1) {
                window.history.back();
                toastr.success("¡GUARDADO!");
            } else {
                toastr.error("Error inesperado");
            }
        }).fail(function (response, jqxhr, error) {
            toastr.error(error);
        });
    });
}

function aprobarOrden() {
    $("#table-ord").on('click', 'tbody .ap', function () {
        $.post("ordencompraControlador", {
            accion: "aprobar",
            id_orden_compra: this.getAttribute("data-id")
        }).done(function (response) {
            if (response == 1) {
                listarOrdenes();
                listarOrdenesAprobadas();
                toastr.success("¡Aprobado!");
            } else {
                toastr.error("Error inesperado");
            }
        }).fail(function (response, jqxhr, error) {
            toastr.error(error);
        });
    });
}

function GenerarCompra(id) {

    $("#inicio").load("vistas/insertCompra.html", {}, function () {

        $('.date input').datepicker({
            format: "dd/mm/yyyy",
            autoClose: true
        }).on('changeDate', function (ev) {
            $('.date input').datepicker('hide');
        });

        $.post("ordencompraControlador", {
            accion: "buscar",
            id_orden_compra: id
        }).done(function (response) {
            $("#proveedor").val(response.nombre_pro).attr("readOnly", true);
            $("#ruc").attr("data-id", response.id_proveedor);
            $("#ruc").val(response.ndocumento_pro + "-" + response.dv_pro).attr("readOnly", true);

        }).fail(function (response, jqxhr, error) {
            toastr.error(error);
        });

        $.post("ordencompraControlador", {
            accion: "detalle",
            id_orden_compra: id
        }).done(function (response) {
            for (var i = 0; i <= response.length; i++) {
                $("#cuerpo").append(row);
            }
            $("#cuerpo tr").each(function (i) {
                if (i < response.length) {
                    $(this).find("td input.chan").removeAttr("data-row");
                    $(this).find("td button").removeClass("hide");
                    $(this).find("input.cod").val(response[i].codigo_ord);
                    $(this).find("input.cod").attr("data-id", response[i].id_articulo);
                    $(this).find("input.cant").val(response[i].cantidad_ord);
                    $(this).find("input.nom").val(response[i].descripcion_ord);
                    $(this).find("input.price").val(dot(response[i].precio_ord));
                    $(this).find("select.iva").val(response[i].iva_ord);
                    $(this).find("input.total").val(dot(response[i].subtotal_ord));
                    $("#cuerpo tr").on('click', "td button", function (e) {
                        e.preventDefault();
                        $(this).closest("tr").remove();
                        calcularTotales("select", "total");
                        return false;
                    });
                }
                buscarArticulos();
                addRow();
            });
            changeEvents();
            calcularTotales("select", "total");
            nfac();
            agregarCompras(0, 0, id);
        }).fail(function (response, jqxhr, error) {
            toastr.error(error);
        });
    });
}

function recibirOrden(id_orden) {
    $.post("ordencompraControlador", {
        accion: "recibir",
        id_orden_compra: id_orden
    }).done(function (response) {
        toastr.success("¡Recibido!");
    }).fail(function (response, jqxhr, error) {
        toastr.error(error);
    });
}

function cargarOrden(id) {
    $("#inicio").load("vistas/insertOrden.html", {}, function () {
        newProveedor();
        buscar();
        orderCrumb();
        $.post("ordencompraControlador", {
            accion: "buscar",
            id_orden_compra: id
        }).done(function (response) {
            $("#proveedor").val(response.nombre_pro);
            $("#ruc").attr("data-id", response.id_proveedor);
            $("#ruc").val(response.ndocumento_pro + "-" + response.dv_pro);
            $("#observacion").val(response.comentario_ord);
        }).fail(function (response, jqxhr, error) {
            toastr.error(error);
        });

        $.post("ordencompraControlador", {
            accion: "buscarDetalle",
            id_orden_compra: id
        }).done(function (response) {
            for (var i = 0; i <= response.length; i++) {
                $("#cuerpo").append(row);
            }
            $("#cuerpo tr").each(function (i) {
                if (i < response.length) {
                    $(this).find("td input.chan").removeAttr("data-row");
                    $(this).find("td button").removeClass("hide");
                    $(this).find("input.cod").val(response[i].codigo_ord);
                    $(this).find("input.cod").attr("data-id", response[i].id_articulo);
                    $(this).find("input.cant").val(response[i].cantidad_ord);
                    $(this).find("input.nom").val(response[i].descripcion_ord);
                    $(this).find("input.price").val(dot(response[i].precio_ord));
                    $(this).find("select.iva").val(response[i].iva_ord);
                    $(this).find("input.total").val(dot(response[i].subtotal_ord));
                    $("#cuerpo tr").on('click', "td button", function (e) {
                        e.preventDefault();
                        $(this).closest("tr").remove();
                        calcularTotales("select", "total");
                        return false;
                    });
                }
                buscarArticulos();
                addRow();
            });
            changeEvents();
            calcularTotales("select", "total");
            insertOrden(1, id);
        }).fail(function (response, jqxhr, error) {
            toastr.error(error);
        });
    });
}

function visualisarAp() {
    $("#table-ord-ap").on('click', 'tbody .see', function () {
        var id = this.getAttribute("data-id");
        $("#inicio").load("vistas/insertOrden.html", {}, function () {
            $("#sav").parent().remove();
            $("#new").remove();
            $(".title").html("Ver")
            $.post("ordencompraControlador", {
                accion: "buscar",
                id_orden_compra: id
            }).done(function (response) {
                $(".subtitle").html('<button type="button" class="btn btn-outline btn-info btn-xs pull-right desap" data-id=' + response.id_orden_compra + '>Desaprobar</button><button type="button" class="btn btn-default btn-xs pull-left breadorder"><i class="fa fa-arrow-circle-o-left"></i></button><h3>&nbsp;Orden N° ' + cero(response.id_orden_compra) + '</h3>');
                $("#proveedor").val(response.nombre_pro).attr("readOnly", true);
                $("#ruc").val(response.ndocumento_pro + "-" + response.dv_pro).attr("readOnly", true);
                $("#observacion").val(response.comentario_ord).attr("readOnly", true);
                orderCrumb();
                desaprobarOrden();
            }).fail(function (response, jqxhr, error) {
                toastr.error(error);
            });

            $.post("ordencompraControlador", {
                accion: "buscarDetalle",
                id_orden_compra: id
            }).done(function (response) {
                for (var i = 0; i < response.length; i++) {
                    $("#cuerpo").append(row);
                }
                $("#cuerpo tr").each(function (i) {
                    if (i < response.length) {
                        $(this).find("td input.chan").removeAttr("data-row").attr("readOnly", true);
                        $(this).find("input.cod").val(response[i].codigo_ord).attr("readOnly", true);
                        $(this).find("input.cant").val(response[i].cantidad_ord).attr("readOnly", true);
                        $(this).find("input.nom").val(response[i].descripcion_ord).attr("readOnly", true);
                        $(this).find("input.price").val(dot(response[i].precio_ord)).attr("readOnly", true);
                        $(this).find("select.iva").val(response[i].iva_ord).attr("disabled", true);
                        $(this).find("input.total").val(dot(response[i].subtotal_ord)).attr("readOnly", true);
                    }
                });
                calcularTotales("select", "total");
            }).fail(function (response, jqxhr, error) {
                toastr.error(error);
            });
        });
        orderCrumb();
    });
}

function visualisarRec() {
    $("#table-ord-rec").on('click', 'tbody .see', function () {
        var id = this.getAttribute("data-id");
        $("#inicio").load("vistas/insertOrden.html", {}, function () {
            $("#sav").parent().remove();
            $("#new").remove();
            $(".title").html("Ver")
            $.post("ordencompraControlador", {
                accion: "buscar",
                id_orden_compra: id
            }).done(function (response) {
                $(".subtitle").html('<button type="button" class="btn btn-default btn-xs pull-left breadorder"><i class="fa fa-arrow-circle-o-left"></i></button><h3>&nbsp;Orden N° ' + cero(response.id_orden_compra) + '</h3>');
                $("#proveedor").val(response.nombre_pro).attr("readOnly", true);
                $("#ruc").val(response.ndocumento_pro + "-" + response.dv_pro).attr("readOnly", true);
                $("#observacion").val(response.comentario_ord).attr("readOnly", true);
                orderCrumb();
            }).fail(function (response, jqxhr, error) {
                toastr.error(error);
            });

            $.post("ordencompraControlador", {
                accion: "buscarDetalle",
                id_orden_compra: id
            }).done(function (response) {
                for (var i = 0; i < response.length; i++) {
                    $("#cuerpo").append(row);
                }
                $("#cuerpo tr").each(function (i) {
                    if (i < response.length) {
                        $(this).find("td input.chan").removeAttr("data-row");
                        $(this).find("input.cod").val(response[i].codigo_ord).attr("readOnly", true);
                        $(this).find("input.cant").val(response[i].cantidad_ord).attr("readOnly", true);
                        $(this).find("input.nom").val(response[i].descripcion_ord).attr("readOnly", true);
                        $(this).find("input.price").val(dot(response[i].precio_ord)).attr("readOnly", true);
                        $(this).find("select.iva").val(response[i].iva_ord).attr("disabled", true);
                        $(this).find("input.total").val(dot(response[i].subtotal_ord)).attr("readOnly", true);
                    }
                });
                changeEvents();
                calcularTotales("select", "total");
            }).fail(function (response, jqxhr, error) {
                toastr.error(error);
            });
        });
        orderCrumb();
    });
}

function desaprobarOrden() {
    $(".desap").on("click", function () {
        var id = $(this).attr("data-id");
        $.post("ordencompraControlador", {
            id_orden_compra: id,
            accion: "desaprobar"
        }).done(function (response) {
            if (response == "1") {
                cargarOrden(id);
                toastr.success("Desaprobado");
            } else {
                toastr.error("Error inesperado");
            }
        }).fail(function (response, jqxhr, error) {
            toastr.error(error);
        });
    });
}

function anularOrden(accion, table) {
    $(table).on('click', "tbody .nulled", function () {
        var id = this.getAttribute("data-id");
        $.post("ordencompraControlador", {
            accion: accion,
            id_orden_compra: id
        }).done(function (response) {
            if (response == "1") {
                listarOrdenes();
                toastr.success("Anulado");
            } else {
                toastr.error("Error Inesperado");
            }
        }).fail(function (response, jqxhr, error) {
            toastr.error(error);
        });
    });
}

function newProveedor() {
    $("#new").on("click", function () {
        $("#modal").load("vistas/modal_prov.html", {}, function () {
            ruc();
            listarCiudades("select");
            ListarMarcas("select");
            AgregarUndefineds();
            $("#ciudad").select2({
                dropdownParent: $(".parent"),
                placeholder: "Seleccione la ciudad",
                allowClear: true
            });
            $("#select-marca").select2({
                dropdownParent: $("#marcas"),
                allowClear: true
            });
            $("#Modal-pro").modal();
            AgregarProveedor(0, 0);
        });
    });
}

function buscar() {
    completar(function (nom, ruc) {
        $("#proveedor").typeahead("destroy");
        $("#proveedor").typeahead({
            source: nom,
            autoSelect: true
        }
        );
        $("#proveedor").change(function () {
            var current = $("#proveedor").typeahead("getActive");
            document.getElementById("ruc").setAttribute("data-id", current.id);
            $("#ruc").val(current.ruc);
            document.getElementById("ruc").readOnly = true;
            if ($("#cuerpo tr").length == 0) {
                $("#cuerpo").html(row);
                $("input.cod").attr("required", true);
            }
            buscarArticulos();
            addRow();
        });
        $("#ruc").typeahead("destroy");
        $("#ruc").typeahead({
            source: ruc,
            autoSelect: true
        });
    });
    $("#ruc").change(function () {
        var current = $("#ruc").typeahead("getActive");
        document.getElementById("ruc").setAttribute("data-id", current.id);
        $("#proveedor").val(current.nombre_pro);
        document.getElementById("proveedor").readOnly = true;
        if ($("#cuerpo tr").length == 0) {
            $("#cuerpo").html(row);
        }
        buscarArticulos();
        addRow();
    });
    document.getElementById("proveedor").addEventListener("blur", veri2);
    document.getElementById("ruc").addEventListener("blur", veri1);
    function veri1() {
        if ($("#ruc").val() == '') {
            $("#proveedor").val('');
            document.getElementById("proveedor").readOnly = false;
        }
    }

    function veri2() {
        if ($("#proveedor").val() == '') {
            $("#ruc").val('');
            document.getElementById("ruc").readOnly = false;
        }
    }
}

function FuncionImprimirOrden(id) {
    var newwindow = window.open("http://localhost:5000/ferro/orden?parametro=" + id, "", "width=1200,height=780");
}