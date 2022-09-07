function proveedores() {
    spinneron();
    $("#inicio").load("vistas/proveedores.html", {}, function () {
        spinneroff();
        $(".active").removeClass("active");
        $(".level").parent().find("ul.nav").removeClass("nav nav-second-level collapse in").addClass("nav nav-second-level collapse");
        $(".supplier").parent().addClass("active");
        listarProveedores();
        PermisosRender(3, (p) => {
            if (p.agregar === 'N') {
                $('a[href="#/proveedores/nuevo"]').remove();
            }
        });
        inhabilitarProveedor("cerrar");
    });
}

function nuevoProveedor() {
    $('a[href="#/proveedores/nuevo"]').attr("disabled", true);
    $("#modal-pro").load("vistas/modal_prov.html", {}, function () {
        ruc();
        listarCiudades("select");
        ListarMarcas("select");
        AgregarUndefineds();
        $("#ciudad").select2({
            dropdownParent: $(".parent"),
            placeholder: "Seleccione la ciudad",
            allowClear: true
        });
        $("#select-marca").select2({
            dropdownParent: $("#marcas"),
            allowClear: true
        });
        $("#Modal-pro").on('hidden.bs.modal', function (e) {
            window.history.back();
        });
        $("#Modal-pro").modal();
        AgregarProveedor(0, 0);
    });
}

function AgregarProveedor(accion, id) {
    $("#form-pro").submit(function (e) {
        $("#save").attr("disabled", true);
        e.preventDefault();
        $.ajax({
            url: "proveedorControlador",
            type: "post",
            data: {
                accion: accion,
                id_proveedor: id,
                nombre: $("#nombre").val(),
                representante: $("#repre").val(),
                ndocumento: $("#doc").val(),
                dv: $("#dv").val(),
                direccion: $("#dir").val(),
                telefono: $("#tel").val(),
                celular: $("#cel").val(),
                correo: $("#correo").val(),
                obs: $("#obs").val(),
                marcas: $("#select-marca").val() ? JSON.stringify($("#select-marca").val()) : "[]",
                ciudad: $("#ciudad").val()
            },
            success: function (response) {
                if (response) {
                    toastr.success("¡GUARDADO!");
                    $("#Modal-pro").modal("hide");
                    buscar();
                } else {
                    toastr.error("ERROR CAPA 8!: " + response);
                }
            }
        });
    });
}

function listarProveedores() {
    $("#table-pro").DataTable({
        "pageLength": 100,
        destroy: true,
        dom: '<"html5buttons"B>lTfgitp',
        buttons: [
            {extend: 'excel', title: 'Proveedores',
                exportOptions: {
                    columns: 'th:not(:last-child)'
                }
            },
            {extend: 'pdf', title: 'Proveedores',
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
            type: 'POST',
            url: 'proveedorControlador',
            data: {accion: "listar"}
        },
        columns: [
            {data: "id_proveedor"},
            {data: "nombre_pro"},
            {data: "representante_legal_pro"},
            {data: "ndocumento_pro",
                render: function (data, type, full, meta) {
                    return full.ndocumento_pro + "-" + full.dv_pro;
                }
            },
            {data: "direccion_pro"},
            {data: "celular_pro"},
            {data: null,
                render: function (data, type, row, meta) {
                    return '<div class="btn-group"><a class="btn btn-primary btn-xs editar" href="#/proveedores/editar?_id=' + data.id_proveedor + '"><i class="far fa-edit"> Editar</i></a><a class="btn btn-warning btn-xs clo" data-id="' + data.id_proveedor + '"><i class="fa fa-ban"> Inhabilitar</i></a></div>';
                }
            }
        ],
        initComplete: function () {
            PermisosRender(3, (p) => {
                if (p.editar === 'N') {
                    $('.editar').remove();
                }
                if (p.eliminar === 'N') {
                    $('.clo').remove();
                }
            });
        }
    });
}

function completar(callback) {
    $.post("proveedorControlador", {"accion": "select"}, function (data) {
        var x = [];
        var y = [];
        for (var i = 0; i < data.length; i++) {
            x[i] = {
                "id": data[i].id_proveedor,
                "name": data[i].nombre_pro,
                "ruc": data[i].ndocumento_pro + "-" + data[i].dv_pro
            };
            y[i] = {
                "id": data[i].id_proveedor,
                "name": data[i].ndocumento_pro + "-" + data[i].dv_pro,
                "nombre_pro": data[i].nombre_pro
            };
        }
        callback(x, y);
    });
}

function editarProveedores(id) {
    $("#modal-pro").load("vistas/modal_prov.html", function () {
        $("a.editar").attr("disabled", true);
        $("#Modal-pro").modal();
        ruc();
        listarCiudades("select");
        ListarMarcas("select");
        AgregarUndefineds();

        $("#ciudad").select2({
            dropdownParent: $(".parent"),
            placeholder: "Seleccione la ciudad",
            allowClear: true
        });

        $("#select-marca").select2({
            dropdownParent: $("#marcas"),
            allowClear: true
        });

        $.post("proveedorControlador", {accion: "buscar", id_proveedor: id}, function (response) {
            $("#nombre").val(response.nombre_pro);
            $("#repre").val(response.representante_legal_pro);
            $("#doc").val(response.ndocumento_pro);
            $("#dv").val(response.dv_pro);
            $("#dir").val(response.direccion_pro);
            $("#tel").val(response.telefono_pro);
            $("#cel").val(response.celular_pro);
            $("#correo").val(response.correo_pro);
            $("#obs").val(response.observacion_pro);
            $("#ciudad").val(response.id_ciudad).trigger("change");
        });

        $.post("proveedorControlador", {accion: "selectMarcas", id_proveedor: id}, function (response) {
            var selected = [];
            for (var i = 0; i < response.length; i++) {
                selected[i] = response[i].id_marca;
            }
            $("#select-marca").val(selected).trigger("change");
        });

        $("#Modal-pro").modal();
        $("#Modal-pro").on('hidden.bs.modal', function (e) {
            window.history.back();
        });
        AgregarProveedor(1, id);
    });
}

function inhabilitarProveedor(accion) {
    $("#table-pro tbody").on('click', '.clo', function () {
        $(this).attr("disabled", true);
        var id = $(this).attr("data-id");
        $.post("proveedorControlador",
                {
                    id_proveedor: id,
                    accion: accion
                })
                .done(function (response) {
                    if (response == 1) {
                        toastr.success("¡Listo!");
                        if (accion == "cerrar") {
                            listarProveedores();
                        } else if (accion == "habilitar") {
                            listarProveedoresInhabilitados();
                        }
                    } else {
                        toastr.error("Error Inesperado");
                    }
                })
                .fail(function (response, jqxhr, error) {
                    toastr.error(error);
                });
    });
}

function listarProveedoresInhabilitados() {
    spinneron();
    $("#inicio").load("vistas/proveedores.html", {}, function () {
        spinneroff();
        $(".active").removeClass("active");
        $("a[href='#/anulados/articulos']").parent().find("ul.nav").removeClass("nav nav-second-level collapse in").addClass("nav nav-second-level collapse");
        $('a[href="#/anulados/articulos"]').closest("ul").closest("li").addClass("active");
        $('a[href="#/proveedores/nuevo"]').remove();
        $("#table-pro").DataTable({
            "pageLength": 100,
            destroy: true,
            dom: '<"html5buttons"B>lTfgitp',
            buttons: [
                {extend: 'excel', title: 'Proveedores Inhabilitados'},
                {extend: 'pdf', title: 'Proveedores Inhabilitados'},
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
                type: 'POST',
                url: 'proveedorControlador',
                data: {accion: "inhabilitados"}
            },
            columns: [
                {data: "id_proveedor"},
                {data: "nombre_pro"},
                {data: "representante_legal_pro"},
                {data: "ndocumento_pro",
                    render: function (data, type, full, meta) {
                        return full.ndocumento_pro + "-" + full.dv_pro;
                    }
                },
                {data: "direccion_pro"},
                {data: "celular_pro"},
                {data: null,
                    render: function (data, type, row, meta) {
                        return '<div class="btn-group"><button class="btn btn-primary btn-xs clo" data-id="' + data.id_proveedor + '"><i class="fa fa-check"> Habilitar</i></button><button class="btn btn-info btn-xs see" data-id="' + data.id_proveedor + '"><i class="fa fa-eye"> Ver</i></button><div>';
                    }
                }
            ]
        });
        verProveedores();
        inhabilitarProveedor("habilitar")
    });
}

function verProveedores() {
    $("#table-pro tbody").on('click', '.see', function () {
        var id = $(this).attr("data-id");
        $("#modal-pro").load("vistas/modal_prov.html", function () {
            $("#Modal-pro").modal();
            ruc();
            listarCiudades("select");
            ListarMarcas("select");

            $("#ciudad").select2({
                dropdownParent: $(".parent"),
                placeholder: "Seleccione la ciudad",
                allowClear: true
            });
            $("#select-marca").select2({
                dropdownParent: $("#marcas"),
                allowClear: true
            });

            $.post("proveedorControlador", {accion: "buscar", id_proveedor: id}, function (response) {
                $("#nombre").val(response.nombre_pro).attr("readOnly", true);
                $("#repre").val(response.representante_legal_pro).attr("readOnly", true);
                $("#doc").val(response.ndocumento_pro).attr("readOnly", true);
                $("#dv").val(response.dv_pro).attr("readOnly", true);
                $("#dir").val(response.direccion_pro).attr("readOnly", true);
                $("#tel").val(response.telefono_pro).attr("readOnly", true);
                $("#cel").val(response.celular_pro).attr("readOnly", true);
                $("#correo").val(response.correo_pro).attr("readOnly", true);
                $("#obs").val(response.observacion_pro).attr("readOnly", true);
                $("#ciudad").val(response.id_ciudad).trigger("change").attr("disabled", true);
            });

            $.post("proveedorControlador", {accion: "selectMarcas", id_proveedor: id}, function (response) {
                var selected = [];
                for (var i = 0; i < response.length; i++) {
                    selected[i] = response[i].id_marca;
                }
                $("#select-marca").val(selected).trigger("change").attr("disabled", true);
            });
            $("#save").remove();
        });
    });
}