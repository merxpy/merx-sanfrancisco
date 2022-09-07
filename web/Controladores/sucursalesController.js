function sucursales() {
    spinneron();
    $("#inicio").load("vistas/sucursales.html", {}, function () {
        spinneroff();
        $(".active").removeClass("active");
        $("a[href='#/sistema/empresa']").parent().find("ul.nav").removeClass("nav nav-second-level collapse in").addClass("nav nav-second-level collapse");
        $('a[href="#/sistema/empresa"]').closest("ul").closest("li").addClass("active");
        listarSucursales();
    });
}

function nuevaSucursal() {
    $("#modal").load("vistas/modal_sucursal.html", {}, function () {
        $("#Modal-sucursal").modal();
        agregarSucursales(0, 0);
        $("#Modal-sucursal").on('hidden.bs.modal', () => {
            window.history.back();
        });
    });
}

function listarSucursales() {
    $("#table-suc").DataTable({
        destroy: true,
        ajax: {
            type: "post",
            url: "sucursalesControlador",
            data: {accion: "listar"}
        },
        columns: [
            {data: "codigo_sucursal"},
            {data: "nombre_sucursal"},
            {data: null,
                render: function (data, type, full, meta) {
                    return "No Aplicable";
                }
            }
        ]
    });
}

function agregarSucursales(accion, id) {
    $("#form-sucursal").submit(function (e) {
        e.preventDefault();
        $.post("sucursalesControlador",
                {
                    accion: accion,
                    id_sucursal: id,
                    codigo_sucursal: $("#codigo").val(),
                    nombre_sucursal: $("#nombre").val()
                })
                .done(function (response) {
                    if (response) {
                        toastr.success("¡GUARDADO!");
                        $("#Modal-sucursal").modal("hide");
                        listarSucursales();
                    } else {
                        toastr.error("Error inesperado");
                    }
                })
                .fail(function (response, jqxhr, error) {
                    toastr.error(error);
                });
    });
}

function selectSucursales() {
    $.post("sucursalesControlador", {
        accion: "select"
    }).done(function (response) {
        if (!jQuery.isEmptyObject(response)) {
            for (var i = 0; i < response.length; i++) {
                var s = document.getElementById("sucursal");
                var option = document.createElement("option");
                option.innerHTML = response[i].nombre_sucursal;
                option.setAttribute("value", response[i].id_sucursal);
                s.appendChild(option);
            }
        } else {
            toastr.error("Algo ha salido mal :(");
        }
    }).fail(function (response, jqxhr, error) {
        toastr.error(error);
    });
}
