function departamentos() {
    spinneron();

    $("#inicio").load("vistas/departamentos.html", {}, function () {
        spinneroff();
        $(".active").removeClass("active");
        $(".utilidades").parent().find("ul.nav").removeClass("nav nav-second-level collapse").addClass("nav nav-second-level collapse in");
        $(".utilidades").parent().addClass("active");
        listarDepartamentos("listar");
        PermisosRender(8, (p) => {
            if (p.agregar === 'N') {
                $('a[href="#/utilidades/departamentos/nuevo"]').remove();
            }
        });
    });
}

function nuevoDpto() {
    $('a[href="#/utilidades/departamentos/nuevo"]').attr("disabled", true);
    $("#modal").load("vistas/modal_dptos.html", function () {
        $("#Modal-dpto").modal();
        agregarDptos(0, 0);
        $("#Modal-dpto").on('hidden.bs.modal', function (e) {
            window.history.back();
        });
    });
}

function listarDepartamentos(accion) {
    if (accion == "listar") {
        $("#table-dptos").DataTable({
            destroy: true,
            dom: '<"html5buttons"B>lTfgitp',
            buttons: [
                {extend: 'excel', title: 'Departamentos',
                    exportOptions: {
                        columns: 'th:not(:last-child)'
                    }
                },
                {extend: 'pdf', title: 'Departamentos',
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
                url: "departamentoControlador",
                data: {accion: accion}
            },
            columns: [
                {data: "id_departamento"},
                {data: "nombre_dep"},
                {data: null,
                    render: function (data, type, full, meta) {
                        return '<div class="btn-group"><a class="btn btn-primary btn-xs editar" href="#/utilidades/departamentos/editar?_id=' + data.id_departamento + '"><i class="far fa-edit"> Editar</i></a></div>';
                    }
                }
            ],
            initComplete: function () {
                PermisosRender(8, (p) => {
                    if (p.editar === 'N') {
                        $('.editar').remove();
                    }
                });
            }
        });
    }
    if (accion == "select") {
        $.post("departamentoControlador", {accion: "completar"}, function (response) {
            var select = document.getElementById("dpto");
            for (var i = 0; i < response.length; i++) {
                var option = document.createElement("option");
                option.setAttribute("value", response[i].id_departamento);
                option.innerHTML = response[i].nombre_dep;
                select.appendChild(option);
            }
        });
    }
}

function agregarDptos(accion, id) {
    $("#form-dpto").submit(function (e) {
        $("#save").attr("disabled", true);
        e.preventDefault();
        $.post("departamentoControlador", {
            accion: accion,
            id_departamento: id,
            nombre_dep: $("#dpto").val()
        }).done(function (response) {
            if (response == 1) {
                toastr.success("GUARDADO!");
                $("#Modal-dpto").modal("hide");
            } else {
                toastr.error("Error Inesperado");
            }
        }).fail(function (response) {
            toastr.error(response);
        });
    });
}

function editarDpto(id) {
    $("a.editar").attr("disabled", true);
    $("#modal").load("vistas/modal_dptos.html", function () {
        $("#Modal-dpto").modal();
        $.post("departamentoControlador", {accion: "buscar", id_departamento: id})
                .done(function (response) {
                    $("#dpto").val(response.nombre_dep);
                })
                .fail(function (response) {
                    toastr.error(response);
                });
        agregarDptos(1, id);
        $("#Modal-dpto").on('hidden.bs.modal', function (e) {
            window.history.back();
        });
    });
}