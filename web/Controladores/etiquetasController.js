function etiquetas() {

    spinneron();
    $("#inicio").load("vistas/etiquetas.html", {}, function () {
        spinneroff();
        $(".active").removeClass("active");
        $("a[href='#/utilidades/categorias']").parent().find("ul.nav").removeClass("nav nav-second-level collapse in").addClass("nav nav-second-level collapse");
        $('a[href="#/utilidades/categorias"]').closest("ul").closest("li").addClass("active");
        ListarEtiquetas("listar");
        PermisosRender(9, (p) => {
            if (p.agregar === 'N') {
                $('a[href="#/utilidades/etiquetas/nuevo"]').remove();
            }
        });
    });
}

function nuevaEtiqueta() {
    $('a[href="#/utilidades/etiquetas/nuevo"]').attr("disabled", true);
    $("#modal").load("vistas/modal_eti.html", {}, function () {
        $("#Modal-etiqueta").modal();
        AgregarEtiquetas(0, 0);
        $("#Modal-etiqueta").on('hidden.bs.modal', function (e) {
            window.history.back();
        });
    });
}

function AgregarEtiquetas(accion, id) {
    $("#form-etiqueta").submit(function (e) {
        $("#save").attr("disabled", true);
        e.preventDefault();
        $.ajax({
            url: "etiquetasControlador",
            type: "POST",
            data: {
                accion: accion,
                id_etiqueta: id,
                nombre_e: $("#nombre_e").val(),
                abreviacion_e: $("#abreviacion_e").val()
            },
            success: function (response) {
                if (response) {
                    toastr.success("¡GUARDADO!");
                    $("#Modal-etiqueta").modal("hide");
                    ListarEtiquetas("listar");
                    if ($("#select-etiqueta")) {
                        ListarEtiquetas("select");
                    }
                } else {
                    toastr.error("Error Inesperado");
                }
            }
        });
    });
}

function ListarEtiquetas(accion) {
    var btn;
    if (accion == "listar") {
        $("#table-label").DataTable({
            "pageLength": 100,
            destroy: true,
            dom: '<"html5buttons"B>lTfgitp',
            buttons: [
                {extend: 'excel', title: 'Etiquetas',
                    exportOptions: {
                        columns: 'th:not(:last-child)'
                    }
                },
                {extend: 'pdf', title: 'Etiquetas',
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
                url: "etiquetasControlador",
                data: {accion: accion}
            },
            columns: [
                {data: "id_etiqueta"},
                {data: "nombre_e"},
                {data: "abreviacion_e"},
                {data: "estado_etiqueta",
                    render: function (data, type, full, meta) {
                        return data === 0 ? `<span class="label label-primary">Disponible</span>` : `<span class="label label-danger">Inhabilitado</span>`;
                    }
                },
                {data: null,
                    render: function (data, type, full, meta) {
                        return `<div class="btn-group"><a class="btn btn-primary btn-xs editar" href="#/utilidades/etiquetas/editar?id=${data.id_etiqueta}"><i class="far fa-edit"> Editar</i></a><a class="btn btn-warning btn-xs nulled" data-id="${data.id_etiqueta}"><i class="fa fa-ban"> ${data.estado_etiqueta === 0 ? `Inhabilitar` : `Habilitar`}</i></a></div>`;
                    }
                }
            ],
            initComplete: function () {
                PermisosRender(9, (p) => {
                    if (p.editar === 'N') {
                        $('.editar').remove();
                    }
                    if (p.eliminar === 'N') {
                        $(".nulled").remove();
                    }
                });
            }
        });
        InhabilitarEtiqueta();
    } else if (accion == "select") {
        $.ajax({
            url: "etiquetasControlador",
            type: "post",
            data: {accion: accion},
            success: function (response) {
                for (var i = 0; i < response.length; i++) {
                    if (!$("#select-etiqueta").find('option[value="' + response[i].id_etiqueta + '"]').val()) {
                        var eti = document.getElementById("select-etiqueta");
                        var option = document.createElement("option");
                        option.setAttribute("value", response[i].id_etiqueta);
                        option.innerHTML = response[i].nombre_e;
                        eti.appendChild(option);
                    }
                }
            }
        });
    }
}

function editarEtiquetas(id) {
    $("a.editar").attr("disabled", true);
    $("#modal").load("vistas/modal_eti.html", {}, function () {
        $("#Modal-etiqueta").modal();
        $.post("etiquetasControlador", {
            accion: "buscar",
            id_etiqueta: id
        }).done(function (response) {
            $("#nombre_e").val(response.nombre_e);
            $("#abreviacion_e").val(response.abreviacion_e);
        }).fail(function (response, jqrxml, error) {
            toastr.error(error);
        });
        AgregarEtiquetas(1, id);
        $("#Modal-etiqueta").on('hidden.bs.modal', function (e) {
            window.history.back();
        });
    });
}

function InhabilitarEtiqueta() {
    $("#table-label").on('click', 'a.nulled', function () {
        var id = this.getAttribute("data-id");
        $.post('etiquetasControlador', {
            accion: "inhabilitar",
            id_etiqueta: id
        }).done((response) => {
            if (response) {
                toastr.clear();
                toastr.success("¡Listo!");
                $("#table-label").DataTable().ajax.reload();
            }
        }).fail((response, jqxhr, error) => {
            toastr.error(error + ", contacte con el desarrollador");
        });
    });
}