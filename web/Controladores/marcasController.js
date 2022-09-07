function marcas() {
    spinneron();
    $("#inicio").load("vistas/marcas.html", {}, function () {
        spinneroff();
        $(".active").removeClass("active");
        $("a[href='#/utilidades/categorias']").parent().find("ul.nav").removeClass("nav nav-second-level collapse in").addClass("nav nav-second-level collapse");
        $('a[href="#/utilidades/categorias"]').closest("ul").closest("li").addClass("active");
        ListarMarcas("listar");
        PermisosRender(10, (p) => {
            if (p.agregar === 'N') {
                $('a[href="#/utilidades/marcas/agregar"]').remove();
            }
        });
    });
}

function NuevaMarca() {
    $('a[href="#/utilidades/marcas/agregar"]').attr("disabled", true);
    $("#modal").load("vistas/modal_marca.html", function () {
        $("#Modal-Marca").modal();
        AgregarMarcas(0, 0);
        $("#Modal-Marca").on('hidden.bs.modal', function (e) {
            window.history.back();
        });
    });
}

function ListarMarcas(accion) {
    if (accion == "listar") {
        $("#table-marks").DataTable({
            "pageLength": 100,
            destroy: true,
            dom: '<"html5buttons"B>lTfgitp',
            buttons: [
                {extend: 'excel', title: 'Marcas',
                    exportOptions: {
                        columns: 'th:not(:last-child)'
                    }
                },
                {extend: 'pdf', title: 'Marcas',
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
                type: "POST",
                url: "marcasControlador",
                data: {accion: "listar"}
            },
            columns: [
                {"data": "id_marca"},
                {"data": "nombre_marca"},
                {"data": "descripcion_marca"},
                {"data": "estado_marca",
                    render: function (data, type, full, meta) {
                        return data === 0 ? `<span class="label label-primary">Disponible</span>` : `<span class="label label-danger">Inhabilitado</span>`;
                    }
                },
                {"data": null,
                    render: function (data, type, full, meta) {
                        return `<div class="btn-group"><a class="btn btn-primary btn-xs editar" href="#/utilidades/marcas/editar?_id=${data.id_marca}"><i class="far fa-edit"> Editar</i></a><a class="btn btn-warning btn-xs nulled" data-id="${data.id_marca}"><i class="fa fa-ban"> ${data.estado_marca === 0 ? `Inhabilitar` : `Habilitar`}</i></a></div>`;
                    }
                }
            ],
            initComplete: function () {
                PermisosRender(10, (p) => {
                    if (p.editar === 'N') {
                        $('.editar').remove();
                    }
                    if (p.eliminar === 'N') {
                        $(".nulled").remove();
                    }
                });
            }
        });
        InhabilitarMarcas();
    }

    if (accion == "select") {
        $.ajax({
            url: "marcasControlador",
            type: "post",
            data: {accion: accion},
            success: function (response) {
                for (var x = 0; x < response.length; x++) {
                    if (!$("#select-marca").find('option[value="' + response[x].id_marca + '"]').val()) {
                        var mar = document.getElementById("select-marca");
                        var option = document.createElement("option");
                        option.setAttribute("value", response[x].id_marca);
                        option.innerHTML = response[x].nombre_marca;
                        mar.appendChild(option);
                    }
                }
            }
        });
    }
}

function AgregarMarcas(accion, id) {
    $("#form-marca").submit(function (e) {
        $("#save").attr("disabled", true);
        e.preventDefault();
        $.ajax({
            url: "marcasControlador",
            type: "post",
            data: {
                "accion": accion,
                "id_marca": id,
                "nombre_marca": $("#nombre_marca").val(),
                "descripcion_marca": $("#descripcion_marca").val()
            },
            success: function (response) {
                if (response == 1) {
                    toastr.success("Guardado");
                    ListarMarcas("listar");
                    $("#Modal-Marca").modal("hide");
                    ListarMarcas("select");
                } else {
                    toastr.error("Error Inesperado");
                }
            }
        });
    });
}

function editarMarcas(id) {
    $("a.editar").attr("disabled", true);
    $("#modal").load("vistas/modal_marca.html", function () {
        $("#Modal-Marca").modal();
        AgregarMarcas(1, id);
        $.post("marcasControlador", {accion: "buscar", id_marca: id}, function (response) {
            $("#nombre_marca").val(response.nombre_marca);
            $("#descripcion_marca").val(response.descripcion_marca);
        });
        $("#Modal-Marca").on('hidden.bs.modal', function (e) {
            window.history.back();
        });
    });
}

function InhabilitarMarcas() {
    $("#table-marks").on('click', 'a.nulled', function () {
        var id = this.getAttribute('data-id');
        $.post("marcasControlador", {
            accion: "inhabilitar",
            id_marca: id
        }).done((response) => {
            if (response) {
                toastr.clear();
                toastr.success("¡Listo!");
                $("#table-marks").DataTable().ajax.reload();
            }
        }).fail((response, jqxhr, error) => {
            toastr.error(error + ", contacte con el desarrolador");
        });
    });
}



