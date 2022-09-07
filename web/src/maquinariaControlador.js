define(function () {
    const exports = {};
    exports.Maquinarias = function (accion) {
        spinneron();
        $("#inicio").load("vistas/maquinarias.html", function () {
            spinneroff();
            ListarMaquinarias(accion);
        });
    };

    exports.BuscarMaquinarias = function (id) {
        $("#modalmaq").load("vistas/modal_maquinarias.html", function () {
            $.post("maquinariaControlador", {
                accion: "buscar",
                id_maquinaria: id
            }).done(function (response) {
                $('input[name="tipo_maquinaria"]').val(response.tipo_maquinaria);
                $('input[name="modelo"]').val(response.modelo);
                $('input[name="serie"]').val(response.serie);
                $('select[name="id_marca"]').val(response.id_marca);
            }).fail(function (response, jqxhr, error) {
                toastr.error(error, " Por favor contacte con el desarrollador");
            }).always(function () {
                AgregarMaquinaria(id);
                ListarMarcas("select");
                AgregarUndefineds();
                $("#select-marca").select2({
                    dropdownParent: $("#select-marca").parent(),
                    allowClear: true
                });
                $("#modal-maq").modal();
                $("#modal-maq").on('hidden.bs.modal', function (e) {
                    window.history.back();
                });
            });
        });
    };

    exports.MaquinariasSelectize = function (data) {
        $("#maquinaria").selectize({
            placeholder: 'Buscar Maquinaria ...',
            valueField: 'id_maquinaria',
            labelField: 'tipo_maquinaria',
            searchField: ['tipo_maquinaria', 'modelo', 'serie'],
            options: [],
            render: {
                item: function (item, escape) {
                    return `<div class="item">${item.tipo_maquinaria} ${item.modelo} ${item.serie !== "-" ? item.serie : ""}</div>`;
                },
                option: function (item, escape) {
                    return `<div class="option">${item.tipo_maquinaria} ${item.modelo} ${item.serie !== "-" ? item.serie : ""}</div>`;
                }
            },
            create: false,
            load: function (query, callback) {
                if (!query.length)
                    return callback();
                $.ajax({
                    url: 'maquinariaControlador',
                    type: 'POST',
                    data: {
                        q: query,
                        accion: "query"
                    },
                    error: function (response, jqxhr, error) {
                        console.log(response);
                        callback(error);
                    },
                    success: function (res) {
                        callback(res);
                    }
                });
            },
            onInitialize: function () {
                let selectize = this;
                if (data.id_maquinaria) {
                    selectize.addOption(data);
                    selectize.setValue(data.id_maquinaria);
                }
            }
        }
        );
    };

    const AgregarMaquinaria = function (id) {
        $("#form-maq").on('submit', function (e) {
            e.preventDefault();
            let fd = ConvertFormToJSON(this);
            fd.id_maquinaria = id;
            $.post("maquinariaControlador", {
                accion: "insertar",
                datos: JSON.stringify(fd)
            }).done(function (response, jqxhr, error) {
                if (response === 1) {
                    toastr.success("¡Guardado!");
                    $("#modal-maq").modal("hide");
                } else {
                    toastr.error(error, " Por favor contacte con el desarrollador");
                }
            }).fail(function (response, jqxhr, error) {
                toastr.error(error, " Por favor contacte con el desarrollador");
            });
        });
    };

    const ListarMaquinarias = function (accion) {
        $("#tabla-maq").DataTable({
            destroy: true,
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
                url: "maquinariaControlador",
                type: "post",
                data: {accion: accion}
            },
            columns: [
                {data: "id_maquinaria"},
                {data: "tipo_maquinaria"},
                {data: "modelo"},
                {data: "serie"},
                {data: "nombre_marca"},
                {data: null,
                    render: function (data, type, full, meta) {
                        let btn;
                        if (accion === 'listar') {
                            btn = `<div class="btn-group">
                                        <a class="btn btn-primary btn-xs editar" href="#/utilidades/maquinarias/editar?_id=${data.id_maquinaria}"><i class="far fa-edit"> Editar</i></a>
                                        <a class="btn btn-warning btn-xs clo" data-id="${data.id_maquinaria}"><i class="fas fa-ban"> Inhabilitar</i></a>
                                    </div>`;
                        } else {
                            btn = `<div class="btn-group">
                                        <a class="btn btn-info btn-xs clo" data-id="${data.id_maquinaria}"><i class="fas fa-check"> Habilitar</i></a>
                                    </div>`;
                        }
                        return btn;
                    }
                }
            ]
        });
        InhabilitarMaquinaria();
    };


    const InhabilitarMaquinaria = function () {
        $("#tabla-maq tbody").on('click', '.clo', function () {
            let id = $(this).attr('data-id');
            $.post("maquinariaControlador", {
                accion: "anular",
                id_maquinaria: id
            }).done(function (response, jqxhr, error) {
                if (response === 1) {
                    toastr.success("¡Listo!");
                    ListarMaquinarias();
                } else {
                    toastr.error(error, "Contacte con del desarrollador");
                }
            }).fail(function (response, jqxhr, error) {
                toastr.error(error, "Contacte con del desarrollador");
            });
        });
    };

    return exports;
});