function verStock() {
    spinneron();

    $("#inicio").load("vistas/stock.html", {}, function () {
        spinneroff();
        $(".active").removeClass("active");
        $("a[href='#/inventario']").parent().find("ul.nav").removeClass("nav nav-second-level collapse in").addClass("nav nav-second-level collapse");
        $('a[href="#/inventario"]').closest("ul").closest("li").addClass("active");
        listarStock();
    });
}

function listarStock() {
    $("#table-stock").DataTable({
        destroy: true,
        "pageLength": 100,
        dom: '<"html5buttons"B>lTfgitp',
        buttons: [
            {
                extend: 'excel',
                title: 'Lista de Stock'
            },
            {
                extend: 'pdf',
                title: 'Lista de Stock'
            },

            {
                extend: 'print',
                customize: function (win) {
                    $(win.document.body).addClass('white-bg');
                    $(win.document.body).css('font-size', '10px');
                    $(win.document.body).find('h1').html("Lista de Stock");
                    $(win.document.body).find('table')
                            .addClass('compact')
                            .css('font-size', 'inherit');
                }
            }
        ],
        ajax: {
            url: "stockControlador",
            type: "post",
            data: {accion: "listar"}
        },
        columns: [
            {data: "codigo_ar"},
            {data: "nombre_ar"},
            {data: "s",
                render: function (data, type, full, meta) {
                    if (data == "" || data == null) {
                        return "-";
                    } else {
                        return data;
                    }
                }
            },
            {data: null,
                render: (data, type, full, meta) => {
                    var msg;
                    if (parseInt(data.cantidad_stock) > parseInt(data.stock_minimo_ar)) {
                        msg = `<span class="label label-primary">En stock</span>`;
                    } else if (parseInt(data.cantidad_stock) <= 0) {
                        msg = `<span class="label label-danger">Agotado</span>`;
                    } else {
                        msg = `<span class="label label-warning">Stock Bajo</span>`;
                    }
                    return msg;
                }
            },
            {data: "cantidad_stock"},
            {data: "total_stock",
                render: function (data, type, full, meta) {
                    return dot(data);
                }
            }
        ]
    });
}