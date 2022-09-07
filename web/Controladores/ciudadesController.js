function ciudades() {
    spinneron();

    $("#inicio").load("vistas/ciudades.html", {}, function () {
        spinneroff();
        $(".active").removeClass("active");
        $(".utilidades").parent().find("ul.nav").removeClass("nav nav-second-level collapse").addClass("nav nav-second-level collapse in");
        $(".utilidades").parent().addClass("active");
        listarCiudades("listar");
        PermisosRender(7, (p) => {
            if (p.agregar === 'N') {
                $('a[href="#/utilidades/ciudades/nuevo"]').remove();
            }
        });
    });
}

function nuevaCiudad() {
    $('a[href="#/utilidades/ciudades/nuevo"]').attr("disable", true);
    $("#modal").load("vistas/modal_ciudad.html", {}, function () {
        $("#Modal-ciudad").modal();
        $("#dpto").select2({
            dropdownParent: $(".dep"),
            placeholder: "Seleccione el Departamento",
            allowClear: true
        });
        listarDepartamentos("select");
        agregarCiudades(0, 0);
        $("#Modal-ciudad").on("hidden.bs.modal", () => {
            window.history.back();
        });
    });
}

function listarCiudades(accion) {
    if (accion == 'select') {
        $.ajax({
            url: "ciudadesControlador",
            type: "post",
            data: {accion: accion},
            success: function (response) {
                for (var z = 0; z < response.length; z++) {
                    var city = document.getElementById("ciudad");
                    var option = document.createElement("option");
                    option.setAttribute("value", response[z].id_ciudad);
                    option.innerHTML = response[z].nombre_ciu;
                    city.appendChild(option);
                }
            }
        });
    } else if (accion == "listar") {
        $("#table-ciudad").DataTable({
            destroy: true,
            dom: '<"html5buttons"B>lTfgitp',
            buttons: [
                {extend: 'excel', title: 'Caja',
                    exportOptions: {
                        columns: 'th:not(:last-child)'
                    }
                },
                {extend: 'pdf', title: 'Caja',
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
                url: "ciudadesControlador",
                data: {accion: "listar"}
            },
            columns: [
                {data: "id_ciudad"},
                {data: "nombre_ciu"},
                {data: "nombre_dep"},
                {data: null,
                    render: function (data, type, full, meta) {
                        return '<div class="btn-group"><a class="btn btn-primary btn-xs editar" href="#/utilidades/ciudades/editar?_id=' + data.id_ciudad + '"><i class="fa fa-edit"> Editar</i></a></div>';
                    }
                }
            ],
            initComplete: function () {
                PermisosRender(7, (p) => {
                    if (p.editar === 'N') {
                        $('.editar').remove();
                    }
                });
            }
        });
    }
}

function agregarCiudades(accion, id) {
    $("#form-ciudad").submit(function (e) {
        $("#save").attr("disabled", true);
        e.preventDefault();
        $.post("ciudadesControlador",
                {
                    accion: accion,
                    id_ciudad: id,
                    nombre_ciu: $("#nombre_ciu").val(),
                    id_departamento: $("#dpto").val()
                })
                .done(function (response) {
                    if (response == 1) {
                        toastr.success("¡GUARDADO!");
                        $("#Modal-ciudad").modal("hide");
                    } else {
                        toastr.error("Error Inesperado");
                    }
                })
                .fail(function (response, jqxrml, error) {
                    toastr.error(error);
                });
    });
}

function editarCiudades(id) {
    $('a.editar').attr("disabled", true);
    $("#modal").load("vistas/modal_ciudad.html", {}, function () {
        $("#dpto").select2({
            dropdownParent: $(".dep"),
            placeholder: "Seleccione el Departamento",
            allowClear: true
        });
        listarDepartamentos("select");
        $("#Modal-ciudad").modal();
        $.post("ciudadesControlador", {accion: "buscar", id_ciudad: id})
                .done(function (response) {
                    $("#nombre_ciu").val(response.nombre_ciu);
                    $("#dpto").val(response.id_departamento).trigger("change");
                })
                .fail(function (response, jqrxml, error) {
                    toastr.error(error);
                });
        agregarCiudades(1, id);
        $("#Modal-ciudad").on("hidden.bs.modal", () => {
            window.history.back();
        });
    });
}