define(['mermasControlador'], function (mermas) {
    var exports = {};

    exports.BajadeStock = function () {
        spinneron();
        $("#inicio").load("vistas/historial_de_bajas.html", () => {
            spinneroff();
            $(".active").removeClass("active");
            $("a[href='#/inventario']").parent().find("ul.nav").removeClass("nav nav-second-level collapse in").addClass("nav nav-second-level collapse");
            $('a[href="#/inventario"]').closest("ul").closest("li").addClass("active");
            exports.HistorialdeBajas();
        });
    };

    exports.HistorialdeBajas = function () {
        $("#table-baja").DataTable({
            "pageLength": 100,
            destroy: true,
            dom: '<"html5buttons"B>lTfgitp',
            buttons: [
                {extend: 'excel', title: 'Baja de Stock',
                    exportOptions: {
                        columns: 'th:not(:last-child)'
                    }
                },
                {extend: 'pdf', title: 'Baja de Stock',
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
                url: "historial_de_bajasControlador",
                type: "post",
                data: {accion: "listar"}
            },
            columns: [
                {data: "id_historial_de_baja",
                    render: (data, type, full, meta) => {
                        return cero(data.toString());
                    }
                },
                {data: "nombre_ar"},
                {data: "concepto_merma"},
                {data: "cantidad_baja"},
                {data: "total_baja",
                    render: (data, type, full, meta) => {
                        return dot(data);
                    }
                },
                {data: null,
                    render: (data, type, full, meta) => {
                        return `<div class="btn-group">                          
                                    <a href="#/bajastock/eliminar?id=${data.id_historial_de_baja}" class="btn btn-danger btn-xs"><i class="fa fa-trash"> Eliminar</i></a>
                                </div>`;
                    }
                }
            ]
        });
    };

    exports.BuscarHistorial = function () {
        spinneron();
        $("#inicio").load("vistas/baja_de_stock.html", () => {
            spinneroff();
            exports.ModalBuscarArticulos();
            $("#buscar").on('submit', function (e) {
                e.preventDefault();
                exports.RederizarBaja($("#codigo").val());
            });
            exports.AgregarBaja();
        });
    };

    exports.RederizarBaja = function (cod) {
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
                    $("#cuerpo").append(`<tr>
                            <td><span id="${response.id_articulo}">${response.codigo_ar}</span></td>
                            <td>
                                <select class="form-control merma" name="merma" required>
                                    <option></option>
                                </select>
                            </td>
                            <td><input type="number" class="form-control cant" min="1" value="${$("#cantidad").val()}" style="width:60px;" required></td>
                            <td><span>${response.nombre_ar}</span></td>
                            <td><span class="prec">${dot(response.precio_venta_ar)}</span></td>
                            <td><span class="subtotal">${dot(response.precio_venta_ar * $("#cantidad").val())}</span></td>
                            <td><a class="text-danger"><i class="glyphicon glyphicon-trash"></i></a></td>
                            </tr>`);
                    calcularSubtotalVenta();
                    mermas.SelectMermas();
                } else {
                    var tr = $("#" + response.id_articulo).parent().parent();
                    var c = parseInt(tr.find("td input.cant").val()) + parseInt($("#cantidad").val());
                    var subt = response.precio_venta_ar * c;
                    tr.find("td input.cant").val(c);
                    tr.find("td span.subtotal").html(dot(subt));
                }
            } else {
                toastr.clear();
                toastr.warning("Producto no encontrado");
            }


            $("#cuerpo tr").on('click', "td a", function (e) {
                e.preventDefault();
                $(this).closest("tr").remove();
                return false;
            });
        });
    };

    exports.ModalBuscarArticulos = function () {
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
                        data: {accion: "listar"}
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
                        {data: "cantidad_stock"},
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
                exports.AgregarFila();
                $('#modal-b').on('hidden.bs.modal', function (e) {
                    $("#cantidad").val(1);
                });
            });
        });
    };

    exports.AgregarFila = function () {
        $("#table-art tbody").on('click', 'a', function () {
            var tart = this;
            var tr = $(this).parent().parent();
            var c = parseInt(tr.find("td input.cant").val());
            $("#cantidad").val(tr.find("td input.cant").val());
            exports.RederizarBaja(this.getAttribute("data-id"));
            tr.find("td input.cant").val(1);
            setTimeout(function () {
                var tabla = $("#" + tart.getAttribute("data-id")).parent().parent();
                var subt = Entero(tr.find("td input.num").val()) * c;
                tabla.find("td span.prec").html(tr.find("td input.num").val());
                tabla.find("td input.cant").val(c);
                tabla.find("td span.subtotal").html(dot(subt));
            }, 100);
        });
    };

    exports.AgregarBaja = function () {
        var detalle = [];
        $("#baja").on('submit', (e) => {
            e.preventDefault();

            $("#cuerpo tr").each(function (i) {
                detalle[i] = {
                    id_articulo: $(this).find("td span").attr("id"),
                    id_merma: $(this).find('td select[name="merma"]').val(),
                    cantidad_baja: $(this).find("td input.cant").val(),
                    total_baja: Entero($(this).find("td span.subtotal").html()),
                };
            });

            $.post("historial_de_bajasControlador", {
                datos: JSON.stringify(detalle),
                accion: "insertar"
            }).done((response) => {
                if (response) {
                    toastr.success("¡Guardado!");
                    window.history.back();
                } else {
                    toastr.error("No se ha podido guardar, contacte con el desarrollador");
                }
            }).fail((response, jqxhr, error) => {
                toastr.error(error + ", contacte con el desarrollador");
            });
        });
    };

    exports.EliminarBaja = function (id) {
        swal({
            title: "¿Está seguro?",
            text: "No podrá recuperar este registro",
            type: "warning",
            showCancelButton: true,
            confirmButtonColor: "#DD6B55",
            confirmButtonText: "Sí, eliminar",
            cancelButtonText: "Cancelar",
            closeOnConfirm: false
        }, function () {
            $.post("historial_de_bajasControlador", {
                accion: "eliminar",
                id: id
            }).done((response) => {
                if (response) {
                    swal.close();
                    toastr.success("¡Eliminado!");
                    window.history.back();
                } else {
                    swal.close();
                    toastr.error("No se ha podido eliminar, contacte con el desarrollador");
                }
            }).fail((response, jqxhr, error) => {
                swal.close();
                toastr.error(error + ", contacte con el administrador");
            });
        });
    };

    return exports;
});

