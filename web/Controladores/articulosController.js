
$(".close-ar").on('click', function () {
    listarArticulosInhabilitados();
});

function items() {
    $(".tool-container").remove();
    spinneron();
    $("#inicio").load("vistas/articulos.html", {}, function () {
        $(".active").removeClass("active");
        $(".level").parent().find("ul.nav").removeClass("nav nav-second-level collapse in").addClass("nav nav-second-level collapse");
        $('a[href="#/articulos"]').closest("li").addClass("active");
        PermisosRender(1, (p) => {
            if (p.agregar === 'N') {
                $('a[href="#/articulos/nuevo"]').remove();
            }
        });
        ListarArticulosGrid();
        spinneroff();
        listarArticulos();

        $("#table-art tbody").on('mouseover', '.btn-toolbar', function () {
            $(this).toolbar({
                content: '#toolbar-options',
                position: 'right',
                style: 'success',
                event: 'click',
                hideOnClick: true
            });
        });
        editarArticulos();
        verArticulo('toolbarItemClick', '.btn-toolbar');
        inhabilitarArticulos("cerrar", 'toolbarItemClick', '.btn-toolbar');
    });
}

function ListarArticulosGrid() {
    $("#option2").on('change', function () {
        if ($(this).is(":checked")) {
            $("input[name='buscar']").removeClass("hide");
            $("#contenido").addClass("row");
            $("#contenido").html("");
            $.post("articuloControlador", {
                accion: "completar"
            }).done(function (response) {
                if (!$.isEmptyObject(response)) {
                    $.each(response, function (i) {
                        if (response[i].nombre_e !== 'GASTOS') {
                            $.post("subirimagenControlador", {
                                accion: "buscar",
                                id_articulo: response[i].id_articulo
                            }).done(function (data) {
                                PermisosRender(1, (p) => {
                                    $("#contenido").append(`<div class="col-md-3" style="display: block;">
                                    <div class="ibox">
                                        <div class="ibox-content product-box">
                                            <div class="image">
                                                <img src="${data.image_path ? data.image_path : "img/box.png"}" style="width: 100%; height: auto;">
                                            </div>
                                            <div class="product-desc">
                                                <span class="product-price">
                                                    Gs. ${dot(response[i].precio_venta_ar)}
                                                </span>
                                                <small class="text-muted">${response[i].nombre_e}</small>
                                                ${p.editar === 'Y' ? `<a href="#/articulos/editar?_id=${response[i].id_articulo}" class="product-name"> ${response[i].nombre_ar}</a>` : `<a class="product-name"> ${response[i].nombre_ar}</a>`}
                                                <div class="small m-t-xs">
                                                    ${response[i].descripcion_ar}
                                                </div>
                                                <div class="m-t text-rigth">
                                                    <button class="btn btn-xs btn-outline btn-info see" data-id="${response[i].id_articulo}" onclick="mostrarArticulo(${response[i].id_articulo})">Info <i class="fa fa-long-arrow-right"></i> </button>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>`);
                                });

                            }).fail(function (data, jqxhr, error) {
                                toastr.error(error + ", contacte con el desarrollador");
                            });
                        }
                    });
                }
            }).fail((response, jqxhr, error) => {
                toastr.error(error + ", contacte con el desarrollador");
            }).always(function () {
                //e.which 8 => BackSpace
                //e.which 32 => Espacio
                $('input[name="buscar"]').on('keypress keyup', function (e) {
                    var nombres = $('.product-desc');
                    var buscando = $(this).val();
                    var item = '';
                    //if ($('div').find('[style="display: none;"]').length !== nombres.length) {
                    if (e.which === 32) {
                        nombres = $('div').find('[style="display: block;"]');
                    }
                    for (var i = 0; i < nombres.length; i++) {
                        item = $(nombres[i]).text().toLowerCase();
                        if (item.indexOf(buscando) > -1) {
                            $(nombres[i]).parents('.col-md-3').show();
                        } else {
                            $(nombres[i]).parents('.col-md-3').hide();
                        }
                    }
                    //}
                });
            });
        }
    });
    $("#option1").on('change', function () {
        if ($(this).is(":checked")) {
            $("input[name='buscar']").addClass("hide");
            $("#contenido").removeClass("row");
            $("#contenido").html(`<div class="table-responsive">
                                    <table id="table-art" class="table table-striped table-bordered table-hover dataTables-example" style="width: 100%">
                                        <thead>
                                            <tr>
                                                <th>Código</th>  
                                                <th>Nombre</th>
                                                <th>Descripción</th>
                                                <th>Marca</th>
                                                <th>Precio</th> 
                                                <th>Acción</th>
                                            </tr>
                                        </thead>
                                    </table>
                                </div>`);
            listarArticulos();
            $("#table-art tbody").on('mouseover', '.btn-toolbar', function () {
                $(this).toolbar({
                    content: '#toolbar-options',
                    position: 'right',
                    style: 'success',
                    event: 'click',
                    hideOnClick: true
                });
            });
            editarArticulos();
            verArticulo('toolbarItemClick', '.btn-toolbar');
            inhabilitarArticulos("cerrar", 'toolbarItemClick', '.btn-toolbar');
        }
    });
}

function nuevoArticulo() {
    $("#inicio").load('vistas/insertArticulos.html', {}, function () {
        spinneroff();
        itemsCrumb();
        stepForm();
        subirImagen();
        num();
        miles();
        $.post("articuloControlador", {accion: "code"}, function (response) {
            $("#codigo_ar").val(response);
        });
        ListarMarcas("select");
        listarUnidad("select");
        ListarEtiquetas("select");
        SelectCategoria(1);

        AgregarUndefineds();
        $("#select-unidad").select2({
            placeholder: "Seleccione la unidad",
            allowClear: true
        });
        $("#select-marca").select2({
            placeholder: "Seleccione la marca",
            allowClear: true
        });

        $("#select-etiqueta").select2({
            placeholder: "Seleccione una etiqueta",
            allowClear: true
        });

        $("#select-categoria").select2({
            placeholder: "Seleccione una categoria",
            allowClear: true
        });

        $('input[name="precio_venta_ar"]').on('change', function () {
            document.getElementById("iva").value = dot(Math.round(Entero($('input[name="precio_venta_ar"]').val()) * parseInt($("#iva_ar").val()) / (100 + parseInt($("#iva_ar").val()))));
        });

        $("#iva_ar").on('change', function () {
            Math.round(Entero(document.getElementById("precio_venta").value) * parseInt($("#iva_ar").val()) / (100 + parseInt($("#iva_ar").val())));
        });

        $("#utilidad").on('change', function () {
            utilidad();
        });

        $("#precio_compra_ar").on('change', function () {
            utilidad();
        });

        $("#iva_ar").on('change', function () {
            utilidad();
        });

        $(document).on("blur", "#codigo_ar", function () {
            verificarArticulos($(this).val());
        });

        $(document).on("blur", "#barcode_ar", function () {
            if ($(this).val() != "" && this.value != 0) {
                verificarArticulos($(this).val());
            }
        });
    });
}

function utilidad() {
    calculos();
    if ($("#iva_ar").val() !== "E") {
        calculos();
    } else {
        $("#iva").val(0);
        $("#precio_ar").val(0);
        var pc = Entero($("#precio_compra_ar").val());
        var u = Math.round(pc * Entero(document.getElementById("utilidad").value) / 100);
        document.getElementById("uti").value = dot(u);
        document.getElementById("precio_gan").value = dot(parseInt(u) + parseInt(pc));
        document.getElementById("precio_venta").value = dot(parseInt(u) + parseInt(pc));
    }
    function calculos() {
        document.getElementById("precio_ar").value = dot(Math.round((Entero(document.getElementById("precio_compra_ar").value) * 100) / (parseInt($("#iva_ar").val()) + 100)));
        var pc = Math.round((Entero(document.getElementById("precio_compra_ar").value) * 100) / (parseInt($("#iva_ar").val()) + 100));
        var u = Math.round(pc * Entero(document.getElementById("utilidad").value) / 100);
        var iva = Math.round((parseInt(u) + parseInt(pc)) * (parseInt($("#iva_ar").val()) / 100));
        document.getElementById("uti").value = dot(u);
        document.getElementById("precio_gan").value = dot(parseInt(u) + parseInt(pc));
        document.getElementById("precio_venta").value = dot(parseInt(u) + parseInt(pc) + iva);
        document.getElementById("iva").value = dot(iva);
    }
}

function calcularUtilidad() {
    var pv = Math.round((Entero(document.getElementById("precio_venta").value) * 100) / (parseInt($("#iva_ar").val()) + 100));
    var pc = Math.round((Entero(document.getElementById("precio_compra_ar").value) * 100) / (parseInt($("#iva_ar").val()) + 100));
    var iva = Math.round(Entero(document.getElementById("precio_venta").value) * parseInt($("#iva_ar").val()) / (100 + parseInt($("#iva_ar").val())));
    var u = pv - pc;
    if (pv) {
        document.getElementById("precio_ar").value = dot(pc);
        document.getElementById("uti").value = dot(u);
        document.getElementById("precio_gan").value = dot(pv);
        document.getElementById("utilidad").value = decimales(parseFloat(u * 100 / pc).toFixed(2));
        document.getElementById("iva").value = dot(iva);
    }
}

function listarArticulos() {
    $("#table-art").DataTable({
        "pageLength": 100,
        destroy: true,
        dom: '<"html5buttons"B>lTfgitp',
        buttons: [
            {
                extend: 'excel',
                title: 'Articulos',
                exportOptions: {
                    columns: [0, 1, 2, 3, 4],
                    format: {
                        body: function (data, row, column, node) {
                            data = $('<p>' + data + '</p>').text();
                            return $.isNumeric(data.replace(/[.+]/g, '')) ? data.replace(/[.+]/g, '') : data;
                        }
                    }
                }
            },
            {
                extend: 'pdf',
                title: 'Articulos',
                exportOptions: {
                    columns: [0, 1, 2, 3, 4]
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
                    columns: [0, 1, 2, 3, 4]
                }
            }
        ],
        ajax: {
            url: "articuloControlador",
            type: "POST",
            data: {accion: "listar"}
        },
        "columnDefs": [
            {
                "targets": [6],
                "visible": false
            }],
        columns: [
            {data: "codigo_ar"},
            {data: "nombre_ar"},
            {data: "descripcion_ar"},
            {data: "nombre_marca",
                render: function (data, type, full, meta) {
                    if (!data) {
                        return "-";
                    } else {
                        return data;
                    }
                }
            },
            {data: "precio_venta_ar",
                render: function (data, type, full, meta) {
                    return dot(data);
                }
            },
            {data: null,
                render: function (data, type, full, meta) {
                    return '<div id="toolbar-options" class="hidden">' +
                            '<a id="see" title="Ver artículo"><i class="fa fa-eye"></i></a>' +
                            '<a id="det" title="Editar artículo"><i class="fa fa-edit"></i></a>' +
                            '<a id="clo" title="Inhabilitar artículo"><i class="fa fa-ban"></i></a>' +
                            '</div>' +
                            '<div data-toolbar="toolbar-options" data-id="' + data.id_articulo + '" data-toolbar-animation="flip" class="btn-toolbar feature-toolbar btn-toolbar-success text-center"><i class="fa fa-cog" style="position: relative"></i></div>';
                }},
            {data: "nombre_e",
                render: function (data, type, full, meta) {
                    if (!data) {
                        return "NO POSEE";
                    } else {
                        return data;
                    }
                }
            }
        ],
        initComplete: function (settings, json) {
            PermisosRender(1, (p) => {
                if (p.editar === 'N') {
                    $("#det").remove();
                }
                if (p.eliminar === 'N') {
                    $("#clo").remove();
                }
            });
        }
    });
}

function agregarArticulos() {
    var form_art = document.getElementById("form-art");
    var fd = ConvertFormToJSON(form_art);
    fd.id_articulo = $("#codigo_ar").attr("data-code");
    $.ajax({
        url: "articuloControlador",
        data: {accion: 0, datos: JSON.stringify(fd)},
        type: "post",
        success: function (response) {
            if (response > 0) {
                $("#upphoto").attr("data-folder", response);
                $("#codigo_ar").attr("data-code", response);
                //toastr.success("¡GUARDADO!");
            } else {
                toastr.error("Error Inesperado");
            }
        }
    });
}

function completarArticulos(callback) {
    $.post("articuloControlador", {accion: "completar"}, function (response) {
        var y = [];
        var x = [];
        for (var i = 0; i < response.length; i++) {
            y[i] = {
                id: response[i].id_articulo,
                name: response[i].nombre_ar,
                codigo_ar: response[i].codigo_ar,
                precio_compra_ar: response[i].precio_compra_ar,
                precio_ar: response[i].precio_ar,
                iva_ar: response[i].iva_ar
            };
            x[i] = {
                id: response[i].id_articulo,
                name: response[i].codigo_ar,
                nombre_ar: response[i].nombre_ar,
                precio_compra_ar: response[i].precio_compra_ar,
                precio_ar: response[i].precio_ar,
                iva_ar: response[i].iva_ar
            };
        }
        callback(x, y);
    });
}

function verificarArticulos(cod) {
    $.post("articuloControlador", {accion: "verificar", cod: cod}, function (response) {
        if (response.codigo_ar) {
            $("#codigo_ar").attr("data-code", response.id_articulo);
            $("#codigo_ar").val(response.codigo_ar);
            $("#barcode_ar").val(response.barcode_ar);
            $("#nombre_ar").val(response.nombre_ar);
            $("#descripcion_ar").val(response.descripcion_ar);
            $("#precio_ar").val(dot(response.precio_neto_ar));
            $("#precio_compra_ar").val(dot(response.precio_compra_ar));
            $("#uti").val(dot(response.monto_utilidad_ar));
            $("#precio_gan").val(dot(response.precio_utilidad_ar));
            $("#iva").val(dot(response.iva_venta_ar));
            $("#precio_venta").val(dot(response.precio_venta_ar));
            $("#utilidad").val(response.utilidad.replace(".", ","));
            $("#stock_min").val(response.stock_minimo_ar);
            $("#iva_ar").val(response.iva_ar);
            $("#select-unidad").val(response.id_unidad).trigger("change");
            $("#select-marca").val(response.id_marca).trigger("change");
            $("#select-etiqueta").val(response.id_etiqueta).trigger("change");
            $("#select-categoria").val(response.id_categoria).trigger("change");
            if (parseInt(response.precio_neto_ar) !== 0 && parseInt(response.precio_venta_ar) !== 0 && response.utilidad) {
                let ganancia = parseInt(response.precio_venta_ar) - parseInt(response.precio_neto_ar) - parseInt(response.iva_venta_ar);
                let u = ganancia * 100 / parseInt(response.precio_neto_ar);
                $("#uti").val(dot(ganancia));
                $("#precio_gan").val(dot(parseInt(response.precio_neto_ar) + parseInt(ganancia)));
                $("#utilidad").val(parseFloat(u).toFixed(2).replace(".", ","));
            }
        }
    });
}

function editarArticulos() {
    $("#table-art tbody").on('toolbarItemClick', '.btn-toolbar', function (event, btnclicked) {
        var cod = $(this).attr("data-id");
        $(".tool-container").remove();
        if (btnclicked.id === "det") {
            window.location.href = "#/articulos/editar?_id=" + cod;
        }
    });
}

function mostrarFormEditar(cod) {
    $("#inicio").load('vistas/insertArticulos.html', {}, function () {
        $("#stock_ini").parent().parent().remove();
        spinneroff();
        stepForm();
        itemsCrumb();
        num();
        miles();
        ListarMarcas("select");
        listarUnidad("select");
        ListarEtiquetas("select");
        SelectCategoria(1);

        $("#utilidad").on('change', function () {
            utilidad();
        });

        $("#precio_compra_ar").on('change', function () {
            utilidad();
        });

        $("#precio_venta").on('change', function () {
            document.getElementById("iva").value = dot(Math.round(Entero(document.getElementById("precio_venta").value) * parseInt($("#iva_ar").val()) / (100 + parseInt($("#iva_ar").val()))));
        });

        $("#iva_ar").on('change', function () {
            document.getElementById("iva").value = dot(Math.round(Entero(document.getElementById("precio_venta").value) * parseInt($("#iva_ar").val()) / (100 + parseInt($("#iva_ar").val()))));
        });

        subirImagen();

        $("#upphoto").attr("data-folder", cod);
        $("#codigo_ar").attr("data-code", cod);
        AgregarUndefineds();
        setTimeout(() => {
            $("#select-unidad").select2({
                placeholder: "Seleccione la unidad",
                allowClear: true
            });
            $("#select-marca").select2({
                placeholder: "Seleccione la marca",
                allowClear: true
            });
            $("#select-etiqueta").select2({
                placeholder: "Seleccione una etiqueta",
                allowClear: true
            });
            $("#select-categoria").select2({
                placeholder: "Seleccione una categoria",
                allowClear: true
            });
            verificarArticulos(cod);
        }, 300);

        $.post("subirimagenControlador", {
            accion: "listar",
            id_articulo: cod
        }).done(function (response) {
            if (!jQuery.isEmptyObject(response)) {
                $("#timg").html("");
                for (var i = 0; i < response.length; i++) {
                    $("#timg").append('<tr style="background: #FFF">' +
                            '<td>' +
                            '<img src="' + response[i].image_path + '" style="height:100px;">' +
                            '</td>' +
                            '<td>' +
                            obtenerN(response[i].image_path) +
                            '</td>' +
                            '<td>' +
                            '<button type="button" class="btn btn-danger" data-folder="' + response[i].id_articulo + '" data-name="' + obtenerN(response[i].image_path) + '" data-art="' + response[i].id_imagen + '"><i class="fa fa-trash"></i></button>' +
                            '</td>' +
                            '</tr>');
                }
            }
        }).fail(function (response, jqxhr, error) {
            toastr.error(error);
        });
    });
}

function inhabilitarArticulos(accion, evento, clase) {
    $("#table-art tbody").on(evento, clase, function (event, btnclicked) {
        var id = $(this).attr("data-id");
        if (btnclicked) {
            if (btnclicked.id === "clo") {
                estadoArticulo(id);
            }
        } else if (clase === ".clo") {
            estadoArticulo(id);
        }
    });

    function estadoArticulo(id) {
        $.post("articuloControlador", {
            accion: accion,
            id_articulo: id
        }).done(function (response) {
            if (response == 1) {
                toastr.success("¡Listo!");
                if (accion === "cerrar") {
                    $('#table-art').DataTable().ajax.reload();
                } else if (accion === "habilitar") {
                    listarArticulosInhabilitados();
                }
            } else {
                toastr.error("Error Inesperado");
            }
        }).fail(function (response, jqxhr, error) {
            toastr.success(error);
        });
    }

}

function listarArticulosInhabilitados() {
    spinneron();

    $("#inicio").load("vistas/articulos.html", {}, function () {
        spinneroff();
        console.log('llegue jojojo');
        $(".active").removeClass("active");
        $("a[href='#/anulados/articulos']").parent().find("ul.nav").removeClass("nav nav-second-level collapse in").addClass("nav nav-second-level collapse");
        $('a[href="#/anulados/articulos"]').closest("ul").closest("li").addClass("active");
        $("#table-art").DataTable({
            destroy: true,
            ajax: {
                url: "articuloControlador",
                type: "POST",
                data: {accion: "inhabilitados"}
            },
            columns: [
                {data: "codigo_ar"},
                {data: "nombre_ar"},
                {data: "descripcion_ar"},
                {data: "abreviatura_u"},
                {data: "precio_venta_ar"},
                {data: null,
                    render: function (data, type, full, meta) {
                        return '<div class="btn-group"><button class="btn btn-primary btn-xs clo" data-id="' + data.id_articulo + '"><i class="fa fa-check"> Habilitar</i></button><button class="btn btn-info btn-xs see" data-id="' + data.id_articulo + '"><i class="fa fa-eye"> Ver</i></button></div>';
                    }
                }
            ]
        });
        verArticulo('click', '.see');
        inhabilitarArticulos("habilitar", 'click', '.clo');
    });
}

function verificarPrecioAr(accion, id) {
    if (parseInt($("#precio_compra_ar").val()) > parseInt($("#precio_ar").val())) {
        swal({
            title: "El precio de compra es mayor que el precio de venta, ¿Desea realizar la operacion?",
            text: "",
            type: "warning",
            showCancelButton: true,
            confirmButtonColor: "#DD6B55",
            confirmButtonText: "Confirmar",
            cancelButtonText: "Cancelar",
            closeOnConfirm: false
        }, function () {
            sweetAlert.close();
            agregarArticulos();
        });
    } else {
        agregarArticulos();
    }
}

function verArticulo(evento, clase) {
    $("#table-art tbody").on(evento, clase, function (event, btnclicked) {
        var cod = $(this).attr("data-id");
        $(".tool-container").remove();
        if (btnclicked) {
            if (btnclicked.id === 'see') {
                mostrarArticulo(cod);
            }
        } else if (clase === ".see") {
            mostrarArticulo(cod);
        }
    });
}

function mostrarArticulo(cod) {
    $("#modal").load('vistas/modal_art.html', {}, function () {
        $("#Modal-art").modal();

        $('#Modal-art').on('hidden.bs.modal', function (e) {
            $(".tool-container").remove();
        });

        $.post("subirimagenControlador",
                {
                    accion: "listar",
                    id_articulo: cod
                })
                .done(function (response) {
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
                })
                .fail(function (response, jqxhr, error) {
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
}

function subirImagen() {
    $("#nimg").on('click', function () {
        document.getElementById("upphoto").click();
        $("#upphoto").on('change', function () {
            var fd = new FormData();
            var foto = document.getElementById("upphoto");
            var nom = foto.files[0];
            fd.append("accion", "subir");
            fd.append("foto_ar", foto.files[0]);
            fd.append("data_folder", foto.getAttribute("data-folder"));
            var img = this;
            if (img.files && img.files[0]) {
                var reader = new FileReader();
                reader.onload = function (e) {
                    $("#nulo").remove();
                    $("#timg").append('<tr style="background: #FFF">' +
                            '<td>' +
                            '<img src="' + e.target.result + '" style="height:100px;">' +
                            '</td>' +
                            '<td>' +
                            nom.name +
                            '</td>' +
                            '<td>' +
                            '<button type="button" class="btn btn-danger last" data-folder="' + foto.getAttribute("data-folder") + '" data-name="' + nom.name + '"><i class="fa fa-trash"></i></button>' +
                            '</td>' +
                            '</tr>');
                };

                reader.readAsDataURL(img.files[0]);

                $("#upphoto").val("");

                $.ajax({
                    url: "subirimagenControlador",
                    data: fd,
                    contentType: false,
                    processData: false,
                    type: "post",
                    success: function (response) {
                        if (response) {
                            toastr.success("¡SUBIDO!");
                            $(".last").attr("data-art", response);
                            $(".last").removeClass("last");
                        } else {
                            toastr.error("Error Inesperado");
                        }
                    }
                });
            }
        });
    });

    $("#timg").on('click', "td button", function () {
        var t = this;
        var data = {
            accion: "eliminar",
            data_folder: $(this).attr("data-folder"),
            data_name: $(this).attr("data-name"),
            data_id: $(this).attr("data-art")
        };

        borrarImg(data, function (response) {
            if (response) {
                $(t).closest("tr").remove();
                if (!$("#timg tr").length) {
                    $("#timg").html('<tr id="nulo"><td colspan="3" class="text-center">Ningun archivo seleccionado</td></tr>');
                }
            }
        });
    });
}

function stepForm() {
    $("#form-art").steps({
        bodyTag: "fieldset",
        onStepChanging: function (event, currentIndex, newIndex) {
            // Always allow going backward even if the current step contains invalid fields!
            if (currentIndex > newIndex) {
                return true;
            }

            // Forbid suppressing "Warning" step if the user is to young

            var form = $(this);

            // Clean up if user went backward before
            if (currentIndex < newIndex) {
                // To remove error styles
                $(".body:eq(" + newIndex + ") label.error", form).remove();
                $(".body:eq(" + newIndex + ") .error", form).removeClass("error");
            }

            // Disable validation on fields that are disabled or hidden.
            form.validate().settings.ignore = ":disabled,:hidden";
            if (Number(currentIndex === 0)) {
                if (form.valid()) {
                    agregarArticulos();
                    toastr.success("¡GUARDADO!");
                }
            }
            return form.valid();
        },
        onStepChanged: function (event, currentIndex, priorIndex)
        {
            if (currentIndex === 0) {
                verificarArticulos($("#codigo_ar").val());
            }
        },
        onFinishing: function (event, currentIndex)
        {
            var form = $(this);
            if (Number(currentIndex === 0)) {
                if (form.valid()) {
                    agregarArticulos();
                    toastr.clear();
                    toastr.success("¡GUARDADO!");
                }
            }
            return form.valid();
        },
        onFinished: function (event, currentIndex)
        {
            window.history.back();
        }
    }).validate({
        errorPlacement: function (error, element)
        {
            element.before(error);
        }
    });
    $("#cancel").on('click', function () {
        window.history.back();
    });
}

function borrarImg(data, r) {
    $.post("subirimagenControlador", data)
            .done(function (response) {
                if (response == 1) {
                    r(true);
                    toastr.success("¡Eliminado!")
                } else {
                    toastr.error("No se pudo eliminar");
                    r(false);
                }
            })
            .fail(function (response, jqxhr, error) {
                toastr.error(error);
                r(false);
            });
}

function obtenerN(n) {
    var c = n.split("/").length - 1;
    for (var i = 0; i < c; i++) {
        n = n.substring(n.indexOf("/") + 1);
    }
    return n;
}

function miles() {
    $(".miles").on('focus', function () {
        this.oninput = function () {
            var miles = dot(this.value.replace(/[.+]/g, ""));
            if (miles) {
                this.value = miles;
            } else {
                this.value = "0";
            }
        }
    });

    $(".decimals").on('focus', function () {
        $(this).on('keypress keyup', function (e) {
            let tecla = (document.all) ? e.keyCode : e.which;
            var dec = decimales(this.value);
            this.value = dec;
            if (dec.includes(",")) {
                if (tecla === 46) {
                    return false;
                } else {
                    return String.fromCharCode(tecla);
                }
            }
        });
    });

}

const AgregarArticulosFast = function (ref, done) {
    $("#fast").on('click', function () {
        $("#modal").load("vistas/modal_insert_articulos.html", function () {
            miles();
            ref === 'COMPRAS' ? $("#precio_ar").parent().parent().remove() : $("#precio_compra_ar").parent().parent().remove();
            $("#modal-insert-art").modal();
            $.post("articuloControlador", {accion: "code"}, function (response) {
                $("#codigo_ar").val(response);
            });

            $("#form-art").on('submit', function (e) {
                e.preventDefault();
                var form_art = document.getElementById("form-art");
                var fd = ConvertFormToJSON(form_art);
                fd.stock_inicial = 0;
                fd.id_unidad = 12;
                $.ajax({
                    url: "articuloControlador",
                    data: {accion: 0, datos: JSON.stringify(fd)},
                    type: "post",
                    success: function (response) {
                        if (response > 0) {
                            toastr.success("¡GUARDADO!");
                            document.getElementById("form-art").reset();
                            $.post("articuloControlador", {accion: "code"}, function (response) {
                                $("#codigo_ar").val(response);
                                $("#nombre_ar").focus();
                            });
                            done(fd.codigo_ar);
                        } else {
                            toastr.error("Error Inesperado");
                        }
                    },
                    error: function (response, jqxhr, error) {
                        toastr.error(error);
                    }
                });
            });

        });
    });
};

const SelectCategoria = function (tipo) {
    $.post("categoriasControlador", {
        accion: "select",
        tipo: tipo
    }).done((response, jqxhr, error) => {
        for (var i = 0; i < response.length; i++) {
            if (!$("#select-categoria").find('option[value="' + response[i].id_categoria + '"]').val()) {
                var eti = document.getElementById("select-categoria");
                var option = document.createElement("option");
                option.setAttribute("value", response[i].id_categoria);
                option.innerHTML = response[i].categoria;
                eti.appendChild(option);
            }
        }
    }).fail((reponse, jqxhr, error) => {
        toastr.error(`${error}, contacte con el desarrollador`);
    });
};