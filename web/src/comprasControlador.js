define(function () {
    var exports = {};
    var table;
    exports.Compras = function (tipo_dato) {
        spinneron();
        $(".level").parent().find("ul.nav").removeClass("nav nav-second-level collapse in").addClass("nav nav-second-level collapse");
        $("#inicio").load("vistas/compras.html", {}, function () {
            spinneroff();
            if (tipo_dato === 'COMPRA') {
                $('.breadcrumb').parent().html(`<h2>Compras</h2>
                                            <ol class="breadcrumb">
                                                <li>
                                                    <a href="#/inicio">Inicio</a>
                                                </li>
                                                <li class="active">
                                                    <strong>Compras</strong>
                                                </li>
                                            </ol>`);
                $('.ibox-title').html(`<a href="#/compras/nuevo" class="btn btn-primary btn-xs pull-right"><i class="fa fa-plus-square"></i> Compra</a>
                    <h5>Lista de Compras</h5>`);
            } else {
                $('.breadcrumb').parent().html(`<h2>Gastos</h2>
                                            <ol class="breadcrumb">
                                                <li>
                                                    <a href="#/inicio">Inicio</a>
                                                </li>
                                                <li class="active">
                                                    <strong>Gastos</strong>
                                                </li>
                                            </ol>`);
                $('.ibox-title').html(`<a href="#/gastos/nuevo" class="btn btn-primary btn-xs pull-right"><i class="fa fa-plus-square"></i> Gasto</a>
                    <h5>Lista de Gastos</h5>`);
            }

            PermisosRender(4, (p) => {
                if (p.agregar === 'N') {
                    $('a[href="#/compras/nuevo"]').remove();
                    $('a[href="#/gastos/nuevo"]').remove();
                }
            });
            exports.ListarCompras(tipo_dato);
            exports.AnularCompra("anular");
        });
    };

    exports.ListarCompras = function (tipo_dato) {
        table = $("#table-com").DataTable({
            "pageLength": 100,
            destroy: true,
            columnDefs: [
                {type: 'date-uk', targets: 2}
            ],
            order: [[0, 'desc']],
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
                data: {
                    accion: "listar",
                    tipo_dato: tipo_dato
                }
            },
            columns: [
                {data: "id_compra"},
                {data: "factura_compra"},
                {data: "fecha_compra"},
                {data: "tipo_compra",
                    render: function (data, type, full, meta) {
                        if (data === 'contado') {
                            return `<label class="label label-warning">Contado</label>`;
                        } else {
                            return `<label class="label label-warning">Crédito</label>`;
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
                        if (tipo_dato === 'COMPRA') {
                            return `<div class="btn-group">
                                        <a class="btn btn-primary btn-xs" href="#/compras/editar?_id=${data.id_compra}"><i class="far fa-edit"> Editar</i></a>
                                        <a class="btn btn-info btn-xs clo" data-id=${data.id_compra}><i class="fa fa-ban"> Anular</i></a>
                                    </div>`;
                        } else if (tipo_dato === 'GASTO') {
                            return `<div class="btn-group">
                                        <a class="btn btn-primary btn-xs" href="#/gastos/editar?_id=${data.id_compra}"><i class="far fa-edit"> Editar</i></a>
                                        <a class="btn btn-info btn-xs clo" data-id=${data.id_compra}><i class="fa fa-ban"> Anular</i></a>
                                    </div>`;
                        }
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
    };

    exports.BuscarCompra = function (id, tipo_dato) {
        $.post("comprasControlador", {
            accion: "buscar",
            id_compra: id
        }).done((response) => {
            $("#inicio").load("vistas/insertCompra.html", function () {
                if (tipo_dato === 'COMPRA') {
                    $('.breadcrumb').parent().html(`<h2>Compras</h2>
                                                    <ol class="breadcrumb">
                                                        <li>
                                                            <a href="#/inicio">Inicio</a>
                                                        </li>
                                                        <li class="active">
                                                            <a href="#/compras">Compras</a>
                                                        </li>
                                                        <li class="active">
                                                            <strong>Nuevo</strong>
                                                        </li>
                                                    </ol>`);
                    $('.ibox-title').html(`<h5>Nueva Compra</h5>`);
                } else {
                    $('.breadcrumb').parent().html(`<h2>Gastos</h2>
                                                    <ol class="breadcrumb">
                                                        <li>
                                                            <a href="#/inicio">Inicio</a>
                                                        </li>
                                                        <li class="active">
                                                            <a href="#/gastos">Gastos</a>
                                                        </li>
                                                        <li class="active">
                                                            <strong>Nuevo</strong>
                                                        </li>
                                                    </ol>`);
                    $('.ibox-title').html(`<h5>Nuevo Gasto</h5>`);
                }

                $('input[name="factura_compra"]').val(response.factura_compra);
                $('input[name="fecha_compra"]').val(response.fecha_compra);
                $('input[name="timbrado_compra"]').val(response.timbrado_compra);
                $('input[value="' + response.tipo_compra + '"]').prop("checked", true);
                $("#totaldescuento").html(dot(response.total_descuento));
                $("#tot").html(dot(response.total_compra));
                $('#pro').selectize({
                    placeholder: 'Buscar proveedor ...',
                    valueField: 'id_proveedor',
                    labelField: 'nombre_pro',
                    searchField: ['nombre_pro', 'ndocumento_pro'],
                    options: [],
                    create: false,
                    load: function (query, callback) {
                        if (!query.length)
                            return callback();
                        $.ajax({
                            url: 'proveedorControlador',
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
                    },
                    onInitialize: function () {
                        var selectize = this;
                        selectize.addOption(response);
                        selectize.setValue(response.id_proveedor);
                    }
                });
                $('.date input').datepicker({
                    format: "dd/mm/yyyy",
                    orientation: "bottom"
                });
                $(".touchspin1").TouchSpin({
                    buttondown_class: 'btn btn-white',
                    buttonup_class: 'btn btn-white'
                });
                $("#nuevo").on('click', function () {
                    exports.NuevoProveedor();
                });
                if (response.id_compra) {
                    $.post("comprasControlador", {
                        accion: "detalle",
                        id_compra: response.id_compra
                    }).done((response) => {
                        $.each(response, function (i) {
                            var tr = document.createElement("tr");
                            tr.setAttribute("id", response[i].id_articulo);
                            $(tr).append(`<td><span class="code">${response[i].codigo}</span></td>
                                            <td><input type="text" class="touchspin2 cant" value="${response[i].cantidad}" min="1" required=""></td>
                                            <td><span class="desc">${response[i].descripcion}</span></td>
                                            <td><input type="text" class="form-control prec" value="${dot(response[i].precio)}" required></td>
                                            <td><input type="number" class="form-control descuento" value="${dot(response[i].descuento)}" min="0" max="100" required></td>                            
                                            <td><span class="iva">${response[i].iva}</span></td>
                                            <td><span class="subtotal">${dot(response[i].subtotal)}</span></td>
                                            <td><a class="text-danger"><i class="glyphicon glyphicon-trash"></i></a></td>`);
                            $("#cuerpo").append(tr);
                            $(".touchspin2").TouchSpin({
                                buttondown_class: 'btn btn-white',
                                buttonup_class: 'btn btn-white'
                            });
                        });
                    }).fail((response, jqxhr, error) => {
                        toastr.error(error + ", contacte con el desarrollador");
                    }).always(() => {
                        exports.CalcularSubtotal();
                        exports.CalcularTotal();
                        setTimeout(() => {
                            $("#cuerpo tr").on('click', "td a", function (e) {
                                e.preventDefault();
                                $(this).closest("tr").remove();
                                exports.CalcularTotal();
                                return false;
                            });
                        }, 250);
                    });
                }
                $("#buscar").on('submit', function (e) {
                    e.preventDefault();
                    exports.AgregarArticulos($("#codigo").val(), $('input[name="cantidad"]').val());
                });
                exports.BuscarArticulosCompra(tipo_dato);
                exports.AgregarCompras(id, tipo_dato);

                $('input[name="cantidad"]').on('input change', function () {
                    if (parseInt(this.value) <= 0 || isNaN(parseInt(this.value))) {
                        this.value = 1;
                        this.select();
                    }
                });
                AgregarArticulosFast('COMPRAS', (cod) => {
                    exports.AgregarArticulos(cod, 1);
                });
            });
        }).fail((response, jqxhr, error) => {
            toastr.error(error + ", contacte con el desarrollador");
        });
    };

    exports.AgregarArticulos = function (cod, cantidad, precio) {
        $('#cuerpo').append(`<tr id="img">
                                <td colspan="5">
                                    <center><img src="img/spinner/Rolling-1s-200px.gif" style="width: 30px; height: 30px;" alt=""/>Buscando...</center>
                                </td>
                            </tr>`);
        $.post("articuloControlador", {
            accion: "verificar", cod: cod
        }).done((response, jqxhr, error) => {
            $("#img").remove();
            if (response.id_articulo) {
                if (!document.getElementById(response.id_articulo)) {
                    $("#cuerpo").append(`<tr id=${response.id_articulo}>
                                            <td><span class="code">${response.codigo_ar}</span></td>
                                            <td><input type="text" class="touchspin2 cant" value=${cantidad} min="1" required></td>
                                            <td><span class="desc">${response.nombre_ar}</span></td>
                                            <td><input type="text" class="form-control prec" value="${precio ? dot(precio) : dot(response.precio_compra_ar)}" required></td>
                                            <td><input type="number" class="form-control descuento" value="0" min="0" max="100" required></td>                             
                                            <td><span class="iva">${response.iva_ar}</span></td>           
                                            <td><span class="subtotal">${precio ? dot(parseInt(precio) * parseInt(cantidad)) : dot(response.precio_compra_ar * parseInt(cantidad))}</span></td>
                                            <td><a class="text-danger"><i class="glyphicon glyphicon-trash"></i></a></td>
                                        </tr>`);
                    $(".touchspin2").TouchSpin({
                        buttondown_class: 'btn btn-white',
                        buttonup_class: 'btn btn-white'
                    });
                } else {
                    var tr = document.getElementById(response.id_articulo);
                    var cant = $(tr).find("td input.cant");
                    $(tr).find("td input.prec").val(precio ? dot(precio) : dot(response.precio_compra_ar));
                    setTimeout(function () {
                        if (cantidad) {
                            cant.val(cantidad).trigger('change');
                        } else {
                            cant.val(parseInt($(cant).val()) + 1).trigger('change');
                        }
                    }, 250);
                }
            } else {
                toastr.clear();
                toastr.warning("Producto no encontrado");
            }
            $("#cuerpo tr").on('click', "td a", function (e) {
                e.preventDefault();
                $(this).closest("tr").remove();
                exports.CalcularTotal();
                return false;
            });
        }).fail((response, jqxhr, error) => {
            toastr.error(error + ", contacte con el desarrollador");
        }).always((response, jqxhr, error) => {
            if (jqxhr === 'success') {
                exports.CalcularSubtotal();
                exports.CalcularTotal();
            }
        });

        if ($('#cuerpo tr').length <= 0) {
            $('#form-compras button[type="submit"]').attr("disabled", true);
        } else {
            $('#form-compras button[type="submit"]').attr("disabled", false);
            $("div.alert.alert-danger").remove();
        }
    };

    exports.CalcularSubtotal = function () {
        $(".cant").on('input change', function () {
            if (parseInt(this.value) <= 0 || isNaN(parseInt(this.value))) {
                this.value = 1;
                this.select();
            }

            var precio = $(this).parent().parent().parent().find("td input.prec");
            var subtotal = $(this).parent().parent().parent().find("td span.subtotal");
            subtotal.html(dot(parseInt($(precio).val().replace(/[.+]/g, "")) * this.value));
            exports.CalcularTotal();
        });

        $(".prec").on('click', function () {
            this.select();
        });

        $(".prec").on('change', function () {
            if (parseInt(Entero(this.value)) === 0) {
                $(this).closest("tr").remove();
                exports.CalcularTotal();
            }
        });

        $(".prec").on('input', function () {
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

            this.value = dot(Entero(this.value));
            var cantidad = $(this).parent().parent().find("td .cant");
            var subtotal = $(this).parent().parent().find("td span.subtotal");
            subtotal.html(dot(parseInt($(this).val().replace(/[.+]/g, "")) * $(cantidad).val()));
            exports.CalcularTotal();
        });

        $(".descuento").on('input', function () {
            if (parseInt(this.value) > 100) {
                $('#form-compras button[type="submit"]').attr("disabled", true);
            } else {
                $('#form-compras button[type="submit"]').attr("disabled", false);
            }
            exports.CalcularTotal();
        });
    };

    exports.CalcularTotal = function () {
        var total10 = 0;
        var total5 = 0;
        var totalE = 0;
        var total = 0;
        var totaliva = 0;
        var totaldescuento = 0;
        $("#cuerpo tr").each(function (i) {
            if ($(this).find("td span.iva").html() === "10") {
                total10 += calcularIva($(this).find("td span.iva").html(), Entero($(this).find("td span.subtotal").html())) - (Descuento(Entero($(this).find("td span.subtotal").html()), $(this).find("td input.descuento").val()) / 11);
            } else if ($(this).find("td span.iva").html() === "5") {
                total5 += calcularIva($(this).find("td span.iva").html(), Entero($(this).find("td span.subtotal").html())) - (Descuento(Entero($(this).find("td span.subtotal").html()), $(this).find("td input.descuento").val()) / 21);
            } else if ($(this).find("td span.iva").html() === "E") {
                totalE += calcularIva($(this).find("td span.iva").html(), Entero($(this).find("td span.subtotal").html())) - Descuento(Entero($(this).find("td span.subtotal").html()), $(this).find("td input.descuento").val());
            }
            total += parseInt(Entero($(this).find("td span.subtotal").html()));
            totaldescuento += Descuento(Entero($(this).find("td span.subtotal").html()), $(this).find("td input.descuento").val());
            totaliva = total10 + total5;
        });
        $("#i10").html(dot(total10));
        $("#i5").html(dot(total5));
        $("#ex").html(dot(totalE));
        $("#totiva").html(dot(totaliva));
        $("#totaldescuento").html(dot(totaldescuento));
        $("#tot").html(dot(total - totaldescuento));
    };

    exports.BuscarArticulosCompra = function (tipo_dato) {
        let accion = tipo_dato === 'COMPRA' ? 'listar' : 'listargastos';
        $("#b-modal").on('click', function () {
            $("#modal").load('vistas/buscarArticulos.html', {}, function () {
                $("#modal-b").modal();
                $("#table-art").DataTable({
                    destroy: true,
                    pageLength: 5,
                    ajax: {
                        url: "stockControlador",
                        type: "POST",
                        data: {accion: accion}
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
                                    return "-";
                                } else {
                                    return data;
                                }
                            }
                        },
                        {data: "cantidad_stock",
                            render: function (data, type, full, meta) {
                                return tipo_dato === 'COMPRA' ? data : '-';
                            }
                        },
                        {data: null,
                            render: function (data, type, full, meta) {
                                return '<input type="number" class="form-control cantidad" value="1">';
                            }
                        },
                        {data: null,
                            render: function (data, type, full, meta) {
                                return '<input type="text" class="form-control precio" value=' + dot(full.precio_compra_ar) + '>';
                            }
                        },
                        {data: null,
                            render: function (data, type, full, meta) {
                                return '<a class="text text-info" data-id=' + data.id_articulo + '><i class="far fa-check-circle fa-2x"></i></a>';
                            }
                        }
                    ]
                });

                $("#table-art tbody").on('input', 'td input.cantidad', function () {
                    if (parseInt(this.value) <= 0 || isNaN(parseInt(this.value))) {
                        this.value = 1;
                        this.select();
                    }
                });

                $("#table-art tbody").on('input', ' input.precio', function () {
                    if (parseInt(Entero(this.value)) < 0) {
                        var aux = Entero(this.value);
                        $(this).val(dot(aux.substring(1, aux.length)));
                    }

                    this.value = dot(Entero($(this).val()));
                });

                $("#table-art tbody").on('click', "a", function () {
                    var tr = $(this).parent().parent();
                    if (parseInt(Entero(tr.find("td input.precio").val())) !== 0) {
                        exports.AgregarArticulos(this.getAttribute('data-id'), $(tr).find("td input.cantidad").val(), Entero($(tr).find("td input.precio").val()));
                    } else {
                        toastr.warning("Error, no se puede insertar un artículo con precio 0");
                    }
                });

                $('#modal-b').on('hidden.bs.modal', function (e) {
                    $("#cantidad").val(1);
                });
            });
        });
    };

    exports.AgregarCompras = function (id, tipo_dato) {
        $("#form-compras").on('submit', function (e) {
            if ($("#cabecera-compra").valid()) {
                if ($("#cuerpo tr").length > 0) {
                    $('#form-compras button[type="submit"]').attr("disabled", true);
                    e.preventDefault();
                    var detalle = [];
                    var fd = {
                        id_compra: id,
                        factura_compra: $('input[name="factura_compra"]').val(),
                        fecha_compra: $('input[name="fecha_compra"]').val(),
                        tipo_compra: $('input[name="tipo_compra"]:checked').val(),
                        total_compra: $('#tot').html().replace(/[.+]/g, ""),
                        total_descuento: $("#totaldescuento").html().replace(/[.+]/g, ""),
                        total_iva_compra: $("#totiva").html().replace(/[.+]/g, ""),
                        id_proveedor: $('select[name="id_proveedor"]').val(),
                        timbrado_compra: $('input[name="timbrado_compra"]').val(),
                        tipo_dato: tipo_dato
                    };

                    $("#cuerpo tr").each(function (i) {
                        detalle[i] = {
                            id_articulo: this.getAttribute("id"),
                            codigo: $(this).find("td span.code").html(),
                            descripcion: $(this).find("td span.desc").html(),
                            cantidad: $(this).find("td input.cant").val(),
                            precio: $(this).find("td input.prec").val().replace(/[.+]/g, ""),
                            descuento: $(this).find("td input.descuento").val() !== "" ? $(this).find("td input.descuento").val().replace(/[.+]/g, "") : 0,
                            iva: $(this).find("td span.iva").html(),
                            subtotal: $(this).find("td span.subtotal").html().replace(/[.+]/g, "")
                        };
                    });

                    $.post("comprasControlador", {
                        accion: "insertar",
                        cabecera: JSON.stringify(fd),
                        detalle: JSON.stringify(detalle)
                    }).done(function (response) {
                        console.log(response);
                        if (response === 1) {
                            toastr.success("¡Guardado!");
                            window.history.back();
                            BuscarStockMinimo();
                        } else if (response === 3) {
                            toastr.warning("Ya existe un registro con esos datos.");
                        } else {
                            toastr.error("Error inesperado, no se han podido guardar los datos.");
                        }
                    }).fail(function (response, jqxhr, error) {
                        toastr.error(error + ", contacte con el desarrollador");
                    });

                } else {
                    $('#form-compras button[type="submit"]').attr("disabled", true);
                    $("div.table-responsive").append(`<center><div class="alert alert-danger">Debes seleccionar almenos un articulo.</div></center>`);
                }
            }
        });
    };

    exports.NuevoProveedor = function () {
        $("#modal-x").load("vistas/modal_prov.html", function () {
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
    };

    exports.AnularCompra = function (accion) {
        $("#table-com").on('click', 'a.clo', function () {
            $(this).attr('disabled', true);
            var id = this.getAttribute("data-id");
            $.post("comprasControlador", {
                accion: accion,
                id_compra: id
            }).done((response) => {
                if (response) {
                    toastr.success("¡Listo!");
                    table.ajax.reload();
                }
            }).fail((response, jqxhr, error) => {
                toastr.error(error + ", contacte con el desarrolador");
            });
        });
    };

    exports.ListarComprasAnuladas = function (tipo_dato) {
        spinneron();
        $("#inicio").load("vistas/compras.html", {}, function () {
            spinneroff();
            $(".active").removeClass("active");
            $("a[href='#/anulados/articulos']").parent().find("ul.nav").removeClass("nav nav-second-level collapse in").addClass("nav nav-second-level collapse");
            $('a[href="#/anulados/articulos"]').closest("ul").closest("li").addClass("active");
            $('a[href="#/compras/nuevo"]').remove();
            table = $("#table-com").DataTable({
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
                    data: {accion: "inhabilitados", tipo_dato: tipo_dato}
                },
                columns: [
                    {data: "id_compra"},
                    {data: "factura_compra"},
                    {data: "fecha_compra"},
                    {data: "tipo_compra",
                        render: function (data, type, full, meta) {
                            if (data === 'contado') {
                                return "Contado";
                            } else {
                                return "Crédito";
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
                            return `<div class="btn-group">
                                        <a class="btn btn-primary btn-xs clo" data-id=${data.id_compra}><i class="fa fa-check"> Restablecer</i></a>
                                    </div>`;
                        }
                    }
                ]
            });
            exports.AnularCompra("reestablecer");
        });
    };

    function Descuento(subtotal, descuento) {
        let total = 0;
        if (parseInt(descuento) !== 0 && parseInt(descuento) <= 100) {
            total = (parseInt(subtotal) * parseInt(descuento)) / 100;
        }
        return total;
    }

    return exports;
});