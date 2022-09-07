define(function () {
    var exports = {};
    var tabla;
    exports.ListarArticulosInhabilitados = function () {
        spinneron();
        $("#inicio").load("vistas/articulos.html", {}, function () {
            spinneroff();
            $(".active").removeClass("active");
            $("a[href='#/anulados/articulos']").parent().find("ul.nav").removeClass("nav nav-second-level collapse in").addClass("nav nav-second-level collapse");
            $('a[href="#/anulados/articulos"]').closest("ul").closest("li").addClass("active");
            $('a[href="#/articulos/nuevo"]').remove();
            tabla = $("#table-art").DataTable({
                "pageLength": 100,
                destroy: true,
                dom: '<"html5buttons"B>lTfgitp',
                buttons: [
                    {extend: 'excel', title: 'Articulos Inhabilitados'},
                    {extend: 'pdf', title: 'Articulos Inhabilitados'},

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
                    url: "articuloControlador",
                    type: "POST",
                    data: {accion: "inhabilitados"}
                },
                columns: [
                    {data: "codigo_ar"},
                    {data: "nombre_ar"},
                    {data: "descripcion_ar"},
                    {data: "nombre_marca",
                        render: function (data, type, full, meta) {
                            if (!data) {
                                return "NO POSEE";
                            } else {
                                return data;
                            }
                        }
                    },
                    {data: "precio_venta_ar",
                        render: (data, type, full, meta) => {
                            return dot(data);
                        }
                    },
                    {data: null,
                        render: function (data, type, full, meta) {
                            return `<div class="btn-group">
                                        <a href="#/anulados/articulos/habilitar?id=${data.id_articulo}" class="btn btn-primary btn-xs"><i class="fa fa-check"> Habilitar</i></a>
                                        <a href="#/anulados/articulos/ver?id=${data.id_articulo}" class="btn btn-info btn-xs"><i class="fa fa-eye"> Ver</i></a>
                                    </div>`;
                        }
                    }
                ]
            });
        });
    };

    exports.VerArticulos = function (cod) {
        $("#modal").load('vistas/modal_art.html', {}, function () {
            $("#Modal-art").modal();

            $('#Modal-art').on('hidden.bs.modal', function (e) {
                $(".tool-container").remove();
            });

            $.post("subirimagenControlador", {
                accion: "listar",
                id_articulo: cod
            }).done(function (response) {
                if (!jQuery.isEmptyObject(response)) {
                    $("#timg").html("");
                    for (var i = 0; i < response.length; i++) {
                        $(".product-images").append('<div class="image">' +
                                '<img src="' + response[i].image_path + '" style="width: 100%; height: auto;">' +
                                '<div>');
                    }
                } else {
                    $(".product-images").append('<div>' +
                            '<div class="image-imitation">' +
                            '[IMAGEN]' +
                            '</div>' +
                            '</div>');
                }
            }).fail(function (response, jqxhr, error) {
                toastr.error(error);
            });

            $.post("articuloControlador", {accion: "buscar", cod: cod}, function (response) {
                if (!jQuery.isEmptyObject(response)) {
                    $(".codigo").html(response.codigo_ar)
                    $(".t1").html(response.nombre_ar);
                    $(".description").html(response.descripcion_ar);
                    $(".product-main-price").html(dot(response.precio_venta_ar) + '<small class="text-muted"> Gs.</small>');
                    $("#precio_compra_ar").val(response.precio_compra_ar);
                    $("#stock_min").val(response.stock_minimo_ar);
                    $("#iva_ar").val(response.iva_ar);
                    $(".t2").html(response.nombre_marca);
                    $(".t3").html(response.nombre_u);
                    $(".t4").html(response.nombre_e);
                }
            });

            $('#Modal-art').on('shown.bs.modal', function () {
                $('.product-images').slick({
                    dots: true,
                    infinite: true,
                    slidesToShow: 1,
                    slidesToScroll: 1
                });
            });
        });
    };

    exports.EliminarArticulos = function (id) {
        $.post("articuloControlador", {
            accion: "eliminar",
            id: id
        }).done((response) => {
            if (response) {
                toastr.success("¡Eliminado!");
                window.history.back();
            } else {
                toastr.error("Error, contacte con el desarrolador.");
            }
        }).fail((response, jqxhr, error) => {
            toastr.error(error + " contacte con el desarrollador.");
        });
    };

    exports.HabilitarArticulos = function (id) {
        $.post("articuloControlador", {
            accion: "habilitar",
            id_articulo: id
        }).done((response) => {
            if (response) {
                toastr.success("¡Listo!");
                tabla.ajax.reload();
            } else {
                toastr.error("Error, contacte con el desarrollador");
            }
        }).fail((response, jqxhr, error) => {
            toastr.error(error + ", contacte con el desarrollador");
        });
    };

    return exports;

});