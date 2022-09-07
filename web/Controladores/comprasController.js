function compras() {
    spinneron();
    $(".level").parent().find("ul.nav").removeClass("nav nav-second-level collapse in").addClass("nav nav-second-level collapse");
    $("#inicio").load("vistas/compras.html", {}, function () {
        $(".active").removeClass("active");
        $(".purch").parent().addClass("active");
        spinneroff();
        listarCompras();
        anularCompra("anular");
        PermisosRender(4, (p) => {
            if (p.agregar === 'N') {
                $('a[href="#/compras/nuevo"]').remove();
            }
        });
    });
}

function listarCompras() {
    $("#table-com").DataTable({
        "pageLength": 100,
        destroy: true,
        dom: '<"html5buttons"B>lTfgitp',
        buttons: [
            {extend: 'excel', title: 'Compras',
                exportOptions: {
                    columns: 'th:not(:last-child)'
                }
            },
            {extend: 'pdf', title: 'Compras',
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
            url: "comprasControlador",
            data: {accion: "listar"}
        },
        columns: [
            {data: "id_compra"},
            {data: "factura_compra"},
            {data: "fecha_compra"},
            {data: "tipo_compra",
                render: function (data, type, full, meta) {
                    if (data == 1) {
                        return "Crédito";
                    } else {
                        return "Contado";
                    }
                }
            },
            {data: "total_compra",
                render: function (data, type, full, meta) {
                    return dot(data);
                }
            },
            {data: "nombre_pro"},
            {data: null,
                render: function (data, type, full, meta) {
                    return '<div class="btn-group">' +
                            '<a class="btn btn-default btn-xs" href="#/compras/ver?_id=' + data.id_compra + '"><i class="fa fa-eye"> Ver</i></a>' +
                            '<a class="btn btn-primary btn-xs editar" href="#/compras/editar?_id=' + data.id_compra + '"><i class="far fa-edit"> Editar</i></a>' +
                            '<a class="btn btn-info btn-xs clo" data-id=' + data.id_compra + '><i class="fa fa-ban"> Anular</i></a>' +
                            '</div>';
                }
            }
        ],
        initComplete: function () {
            PermisosRender(4, (p) => {
                if (p.editar === 'N') {
                    $('.editar').remove();
                }
                if (p.eliminar === 'N') {
                    $('.clo').remove();
                }
            });
        }
    });
}

function nuevaCompra() {
    spinneron();
    $('a[href="#/compras/nuevo]"').attr("disabled", true);
    $("#inicio").load('vistas/insertCompra.html', {}, function () {
        spinneroff();
        purchCrumb();
        $('.date input').datepicker({
            format: "dd/mm/yyyy",
            orientation: "bottom"
        });
        newProveedor();
        nfac();
        buscar();
        agregarCompras(0, 0, 0);
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

function buscarArticulos() {
    completarArticulos(function (cod, nom) {
        $("input.nom").each(function () {
            $(this).typeahead({
                source: nom,
                autoSelect: true

            });
        });
        $("input.cod").each(function () {
            $(this).typeahead({
                source: cod,
                autoSelect: true
            });
        });
        $(".cod").change(function () {
            verificarCodigo(this.value, $(this).parent().parent());
        });
        $(".nom").change(function () {
            var c = $(this).typeahead("getActive");
            $(this).parent().parent().find("td input.cod").val(c.codigo_ar);
            $(this).parent().parent().find("td input.cod").attr("data-id", c.id);
            $(this).parent().parent().find("td input.cant").val(1);
            $(this).parent().parent().find("td input.price").val(dot(c.precio_compra_ar));
            $(this).parent().parent().find("td input.iva").val(c.iva_ar);
            $(this).parent().parent().find("td input.total").val(dot(c.precio_compra_ar));
            calcularTotales("select", "total");
        });
    });
}

function addRow() {
    var cuerpo = $("#cuerpo");
    $("td input.chan").on("change", function () {
        if (this.value != "") {
            var u = this.getAttribute("data-row");
            this.removeAttribute("data-row");
            $(this).parent().parent().find("td input.chan").removeAttr("data-row");
            $(this).parent().parent().find("td button").removeClass("hide");
            if (u == "u") {
                cuerpo.append(row);
                buscarArticulos();
                addRow();
                miles();
                changeEvents();
            }
        }

        $("#cuerpo").on('click', "td button", function (e) {
            e.preventDefault();
            $(this).closest("tr").remove();
            calcularTotales("select", "total");
            return false;
        });
    });
}

function changeEvents() {
    $(document).on('change', '.cant', function () {
        $(this).parent().parent().find("td input.total").val(calcularTotal($(this).parent().parent()));
        calcularTotales("select", "total");
    });
    $(document).on('change', '.price', function () {
        $(this).parent().parent().find("td input.total").val(calcularTotal($(this).parent().parent()));
        calcularTotales("select", "total");
    });
    $(document).on('change', '.iva', function () {
        calcularTotales("select", "total");
    });
}

function verificarCodigo(cod, tr) {
    $.post("articuloControlador",
            {
                accion: "verificar",
                cod: cod
            })
            .done(function (response) {
                if (jQuery.isEmptyObject(response)) {
                    $(tr).find("td input.cod").val("");
                    $(tr).find("td input.cod").removeAttr("data-id");
                    $(tr).find("td input.nom").val("");
                    $(tr).find("td input.price").val("");
                    $(tr).find("td input.price_ar").val("");
                    $(tr).find("td input.iva").val("");
                    $(tr).find("td input.total").val("");
                    $(tr).find("td input.total_ar").val("");
                } else {
                    $(tr).find("td input.cant").val(1);
                    $(tr).find("td input.cod").attr("data-id", response.id_articulo);
                    $(tr).find("td input.nom").val(response.nombre_ar);
                    $(tr).find("td input.price").val(dot(response.precio_compra_ar)); //form-compra
                    $(tr).find("td input.price_ar").val(dot(response.precio_ar)); //form-venta
                    $(tr).find("td input.iva").val(response.iva_ar);
                    var tot = parseInt($(tr).find("td input.cant").val()) * parseInt(response.precio_compra_ar);
                    var tot_ar = parseInt($(tr).find("td input.cant").val()) * parseInt(response.precio_ar);
                    $(tr).find("td input.total").val(dot(tot)); //form-compra
                    $(tr).find("td input.total_ar").val(dot(tot_ar)); //form-venta
                }
            })
            .fail(function (response, jqxhr, error) {
                toastr.error(error);
            });
}

function calcularTotales(type, clase) {
    var t10 = 0;
    var t5 = 0;
    var tE = 0;
    var total = 0;
    $("#cuerpo tr").each(function (i) {
        var tot = $(this).find("td input." + clase).val();
        if (tot != 0) {
            if ($(this).find("td " + type + ".iva").val() == "10") {
                t10 += calcularIva($(this).find("td " + type + ".iva").val(), tot);
            } else if ($(this).find("td " + type + ".iva").val() == "5") {
                t5 += calcularIva($(this).find("td " + type + ".iva").val(), tot);
            } else {
                tE += calcularIva($(this).find("td " + type + ".iva").val(), tot);
            }
            total += parseInt(Entero(tot));
        }
    });
    $("#i10").val(dot(t10));
    $("#i5").val(dot(t5));
    $("#iE").val(dot(tE));
    $("#ti").val(dot(t5 + t10));
    $("#tot").val(dot(total));
}

function agregarCompras(id, accion, id_orden) {
    var detalle = [];
    $("#send").submit(function (e) {
        e.preventDefault();
        $("#send").attr("disbled", true);
        $("#cuerpo tr").each(function (i) {
            if ($(this).find("td input.cod").attr("data-id")) {
                detalle[i] = {
                    id_articulo: $(this).find("td input.cod").attr("data-id"),
                    cantidad: $(this).find("td input.cant").val(),
                    precio: Entero($(this).find("td input.price").val()),
                    iva: Entero($(this).find("td select.iva").val()),
                    subtotal: Entero($(this).find("td input.total").val())
                };
            }
        });
        $.ajax({
            url: "comprasControlador",
            type: "post",
            data: {
                accion: accion,
                id_compra: id,
                id_proveedor: $("#ruc").attr("data-id"),
                fecha_compra: $("#fecha").val(),
                factura_compra: $("#nro").val(),
                obs_compra: $("#observacion").val(),
                tipo_compra: $("input[type='radio'][name='radio1']:checked").val(),
                total_iva_compra: Entero($("#ti").val()),
                total_compra: Entero($("#tot").val()),
                detalle_compra: JSON.stringify(detalle)
            },
            success: function (response) {
                if (response == 1) {
                    toastr.success("¡GUARDADO!");
                    window.history.back();
                    if (id_orden != 0) {
                        recibirOrden(id_orden);
                    }
                    BuscarStockMinimo();
                } else {
                    toastr.error("Ha ocurrido un error");
                }
            },
            error: function (xhr, ajaxOptions, thrownError) {
                alert(xhr.status);
                alert(thrownError);
            }

        });
    });
}

function editarCompras(id) {
    $("#inicio").load('vistas/insertCompra.html', {}, function () {
        purchCrumb();
        var cuerpo = $("#cuerpo");
        $('.date input').datepicker({
            format: "dd/mm/yyyy",
            autoClose: true
        }).on('changeDate', function (ev) {
            $('.date input').datepicker('hide');
        });
        $.post("comprasControlador", {accion: "buscar", id_compra: id}, function (response) {
            $("#proveedor").val(response.nombre_pro);
            $("#ruc").val(response.ndocumento_pro + "-" + response.dv_pro);
            $("#ruc").attr("data-id", response.id_proveedor);
            $("#fecha").val(response.fecha_compra);
            $("#nro").val(response.factura_compra);
            $("input[type='radio'][name='radio1'][value='" + response.tipo_compra + "']").prop("checked", true);
        });
        $.post("comprasControlador",
                {
                    accion: "detalle",
                    id_compra: id
                },
                function (response) {
                    for (var i = 0; i <= response.length; i++) {
                        cuerpo.append(row);
                    }
                    $("#cuerpo tr").each(function (i) {
                        if (i < response.length) {
                            $(this).find("td input.chan").removeAttr("data-row");
                            $(this).find("td button").removeClass("hide");
                            $(this).find("input.cod").val(response[i].codigo);
                            $(this).find("input.cod").attr("data-id", response[i].id_articulo);
                            $(this).find("input.cant").val(response[i].cantidad);
                            $(this).find("input.nom").val(response[i].descripcion);
                            $(this).find("input.price").val(dot(response[i].precio));
                            $(this).find("select.iva").val(response[i].iva);
                            $(this).find("input.total").val(dot(response[i].subtotal));
                            $("#cuerpo tr").on('click', "td button", function (e) {
                                e.preventDefault();
                                $(this).closest("tr").remove();
                                calcularTotales("select", "total");
                                return false;
                            });
                            changeEvents();
                        }
                        buscarArticulos();
                        addRow();
                    });
                    calcularTotales("select", "total");
                });
        nfac();
        buscar();
        agregarCompras(id, 1, 0);
    });
}

function anularCompra(accion) {
    $("#table-com").on('click', '.clo', function () {
        $(this).attr("disbled", true);
        var id = $(this).attr("data-id");
        $.post("comprasControlador", {
            accion: accion,
            id_compra: id
        }).done(function (response) {
            if (response == 1) {
                toastr.success("Listo!");
                if (accion == "anular") {
                    listarCompras();
                } else if (accion == "reestablecer") {
                    listarComprasInhabilitadas();
                }
            } else {
                toastr.error("Error Inesperado");
            }
        }).fail(function (response, jqxhr, error) {
            toastr.error(error);
        });
    });
}

function listarComprasInhabilitadas() {
    spinneron();
    $("#inicio").load("vistas/compras.html", {}, function () {
        spinneroff();
        $(".active").removeClass("active");
        $("a[href='#/anulados/articulos']").parent().find("ul.nav").removeClass("nav nav-second-level collapse in").addClass("nav nav-second-level collapse");
        $('a[href="#/anulados/articulos"]').closest("ul").closest("li").addClass("active");
        $('a[href="#/compras/nuevo"]').remove();
        anularCompra("reestablecer");
        $("#table-com").DataTable({
            "pageLength": 100,
            destroy: true,
            dom: '<"html5buttons"B>lTfgitp',
            buttons: [
                {extend: 'excel', title: 'Compras Inhabilitados'},
                {extend: 'pdf', title: 'Compras Inhabilitados'},
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
                url: "comprasControlador",
                data: {accion: "inhabilitados"}
            },
            columns: [
                {data: "id_compra"},
                {data: "factura_compra"},
                {data: "fecha_compra"},
                {data: "tipo_compra",
                    render: function (data, type, full, meta) {
                        if (data == 1) {
                            return "Crédito";
                        } else {
                            return "Contado";
                        }
                    }
                },
                {data: "total_compra",
                    render: function (data, type, full, meta) {
                        return dot(data);
                    }
                },
                {data: "nombre_pro"},
                {data: null,
                    render: function (data, type, full, meta) {
                        return '<div class="btn-group"><button class="btn btn-primary btn-xs clo" data-id="' + data.id_compra + '"><i class="fa fa-check"> Restablecer</i></button><button class="btn btn-info btn-xs see" data-id="' + data.id_compra + '"><i class="fa fa-eye"> Ver</i></button></div>';
                    }
                }
            ]
        });
    });
}


function verCompras(id) {
    $("#inicio").load('vistas/insertCompra.html', {}, function () {
        var cuerpo = $("#cuerpo");
        $.post("comprasControlador", {accion: "buscar", id_compra: id}, function (response) {
            $(".subtitle").html('<button type="button" class="btn btn-outline btn-default btn-xs pull-right" onclick="window.history.back();">Cancelar</button><h3>Nueva Compra</h3>');
            $("#proveedor").val(response.nombre_pro).attr("readOnly", true);
            $("#ruc").val(response.ndocumento_pro + "-" + response.dv_pro).attr("readOnly", true);
            $("#ruc").attr("data-id", response.id_proveedor).attr("readOnly", true);
            $("#fecha").val(response.fecha_compra).attr("readOnly", true);
            $("#nro").val(response.factura_compra).attr("readOnly", true);
//            $("#observacion")
            $("input[type='radio'][name='radio1'][value='" + response.tipo_compra + "']").prop("checked", true);
        });
        purchCrumb();
        $.post("comprasControlador",
                {
                    accion: "detalle",
                    id_compra: id

                },
                function (response) {
                    for (var i = 0; i < response.length; i++) {
                        cuerpo.append(row);
                    }
                    $("#cuerpo tr").each(function (i) {
                        if (i < response.length) {
                            $(this).find("td input.chan").removeAttr("data-row");
                            $(this).find("input.cod").val(response[i].codigo).attr("readOnly", true);
                            $(this).find("input.cant").val(response[i].cantidad).attr("readOnly", true);
                            $(this).find("input.nom").val(response[i].descripcion).attr("readOnly", true);
                            $(this).find("input.price").val(dot(response[i].precio)).attr("readOnly", true);
                            $(this).find("select.iva").val(response[i].iva).attr("disabled", true);
                            $(this).find("input.total").val(dot(response[i].subtotal)).attr("readOnly", true);
                            $("#cuerpo tr").on('click', "td button", function (e) {
                                e.preventDefault();
                                $(this).closest("tr").remove();
                                calcularTotales("select", "total");
                                return false;
                            });
                            changeEvents();
                        }
                    });
                    calcularTotales("select", "total");
                });
        $("#new").remove();
        $("#sav").parent().remove();
    });
}