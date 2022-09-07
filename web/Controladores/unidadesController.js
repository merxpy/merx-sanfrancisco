function unidades() {
    spinneron();
    $("#inicio").load("vistas/unidades.html", {}, function () {
        spinneroff();
        $(".active").removeClass("active");
        $("a[href='#/utilidades/categorias']").parent().find("ul.nav").removeClass("nav nav-second-level collapse in").addClass("nav nav-second-level collapse");
        $('a[href="#/utilidades/categorias"]').closest("ul").closest("li").addClass("active");
        listarUnidad("listar");
        PermisosRender(11, (p) => {
            if (p.agregar === 'N') {
                $('a[href="#/utilidades/unidades/nuevo"]').remove();
            }
        });
    });
}

function nuevaUnidad() {
    $('a[href="#/utilidades/unidades/nuevo"]').attr("disabled", true);
    $("#modal").load("vistas/modal_unidad.html", {}, function () {
        $("#Modal-unidad").modal();
        AgregarUnidad(0, 0);
        $("#Modal-unidad").on('hidden.bs.modal', function (e) {
            window.history.back();
        });
    });
}

function listarUnidad(accion) {
    if (accion == "select") {
        $.ajax({
            url: "unidadesControlador",
            type: "POST",
            data: {accion: accion},
            success: function (response) {
                for (var x = 0; x < response.length; x++) {
                    if (!$("#select-unidad").find('option[value="' + response[x].id_unidad + '"]').val()) {
                        var uni = document.getElementById("select-unidad");
                        var option = document.createElement("option");
                        option.setAttribute("value", response[x].id_unidad);
                        if (response[x].id_unidad == 12) {
                            option.setAttribute("selected", true);
                        }
                        option.innerHTML = response[x].nombre_u;
                        uni.appendChild(option);
                    }
                }
            },
            error: function (xhr, ajaxOptions, thrownError) {
                console.log(xhr.status + " " + thrownError);
            }

        });
    } else if (accion == "listar") {
        $("#table-unidades").DataTable({
            "pageLength": 100,
            destroy: true,
            dom: '<"html5buttons"B>lTfgitp',
            buttons: [
                {extend: 'excel', title: 'Unidades',
                    exportOptions: {
                        columns: 'th:not(:last-child)'
                    }
                },
                {extend: 'pdf', title: 'Unidades',
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
                url: "unidadesControlador",
                type: "POST",
                data: {accion: accion}
            },
            columns: [
                {data: "id_unidad"},
                {data: "nombre_u"},
                {data: "abreviatura_u"},
                {data: "estado_unidad",
                    render: function (data, type, full, meta) {
                        return data === 0 ? `<span class="label label-primary">Disponible</span>` : `<span class="label label-danger">Inhabilitado</span>`;
                    }
                },
                {data: null,
                    render: function (data, type, full, meta) {
                        return `<div class="btn-group"><a class="btn btn-primary btn-xs editar" href="#/utilidades/unidades/editar?_id=${data.id_unidad}"><i class="far fa-edit"> Editar</i></a><a class="btn btn-warning btn-xs nulled" data-id="${data.id_unidad}"><i class="fa fa-ban"> ${data.estado_unidad === 0 ? `Inhabilitar` : `Habilitar`}</i></a></div>`;
                    }
                }
            ],
            initComplete: function () {
                PermisosRender(11, (p) => {
                    if (p.editar === 'N') {
                        $('.editar').remove();
                    }
                    if (p.eliminar === 'N') {
                        $(".nulled").remove();
                    }
                });
            }

        });
        InhabilitarUnidad();
    }
}

function AgregarUnidad(accion, id) {
    $("#form-unidad").submit(function (e) {
        $("#save").attr("disabled", true);
        e.preventDefault();
        $.post("unidadesControlador", {
            accion: accion,
            id_unidad: id,
            nombre_u: $("#nombre_u").val(),
            abreviatura_u: $("#abreviatura_u").val()
        }, function (response) {
            if (response) {
                toastr.success("Guardado");
                $("#Modal-unidad").modal("hide");
                listarUnidad("listar");
                listarUnidad("select");
            } else {
                toastr.error("Error Inesperado");
            }
        });
    });
}

function editarUnidad(id) {
    $("a.editar").attr("disabled", true);
    $("#modal").load("vistas/modal_unidad.html", {}, function () {
        $("#Modal-unidad").modal();
        $.post("unidadesControlador", {accion: "buscar", id_unidad: id}, function (response) {
            $("#nombre_u").val(response.nombre_u);
            $("#abreviatura_u").val(response.abreviatura_u);
        });
        AgregarUnidad(1, id);
        $("#Modal-unidad").on('hidden.bs.modal', function (e) {
            window.history.back();
        });
    });
}

function InhabilitarUnidad() {
    $("#table-unidades").on('click', 'a.nulled', function () {
        var id = this.getAttribute("data-id");
        $.post('unidadesControlador', {
            accion: "inhabilitar",
            id_unidad: id
        }).done((response) => {
            if (response) {
                toastr.clear();
                toastr.success("¡Listo!");
                $("#table-unidades").DataTable().ajax.reload();
            }
        }).fail((response, jqxhr, error) => {
            toastr.error(error + ", contacte con el desarrolador");
        });
    });
}