"use strict"
function auditoria() {

    spinneron();
    $(".active").removeClass("active");
    $("#inicio").load("vistas/auditoria.html", {}, function () {
        spinneroff();
        $("a[href='#/sistema/auditoria']").parent().find("ul.nav").removeClass("nav nav-second-level collapse in").addClass("nav nav-second-level collapse");
        $('a[href="#/sistema/auditoria"]').closest("ul").closest("li").addClass("active");
        var t = "articulos";
        $("#htab-1").addClass("active");
        listarAuditoria(t);

        $("#htab-1").on('click', function () {
            t = "articulos";
            listarAuditoria(t);
        });

        $("#htab-2").on('click', function () {
            t = "clientes";
            listarAuditoria(t);
        });

        $("#htab-3").on('click', function () {
            t = "compras";
            listarAuditoria(t);
        });

        $("#htab-4").on('click', function () {
            t = "ordendecompra";
            listarAuditoria(t);
        });

        $("#htab-5").on('click', function () {
            t = "proveedores";
            listarAuditoria(t);
        });
        
        $("#htab-6").on('click', function () {
            t = "pedidos";
            listarAuditoria(t);
        });
        
        $("#htab-7").on('click', function () {
            t = "ventas";
            listarAuditoria(t);
        });

        $("#htab-8").on('click', function () {
            t = "usuarios";
            listarAuditoria(t);
        });
    });
}

function listarAuditoria(t) {
    $("#table-aud").DataTable({
        "pageLength": 100,
        destroy: true,
        ajax: {
            type: "post",
            url: "auditoriaControlador",
            data: {
                accion: "listar",
                tabla_au: t
            }
        },
        columns: [
            {data: "id_auditoria",
                render: function (data, type, full, meta) {
                    return cero(data);
                }
            },
            {data: "fecha_hora_au"},
            {data: "detalle_au"},
            {data: "alias_usu"},
            {data: "nombre_emp"}

        ]
    });
}


