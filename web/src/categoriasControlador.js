define(function () {
    const exports = {};
    exports.Categorias = function () {
        spinneron();
        $("#inicio").load("vistas/categorias.html", {}, function () {
            spinneroff();
            $(".active").removeClass("active");
            $("a[href='#/utilidades/categorias']").parent().find("ul.nav").removeClass("nav nav-second-level collapse in").addClass("nav nav-second-level collapse");
            $('a[href="#/utilidades/categorias"]').closest("ul").closest("li").addClass("active");
            ListarCategorias();
        });
    };

    const ListarCategorias = function () {
        $("#tabla-categorias").DataTable({
            "pageLength": 100,
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
                url: "categoriasControlador",
                type: "post",
                data: {accion: "listar"}
            },
            columns: [
                {data: "id_categoria"},
                {data: "categoria"},
                {data: "descripcion_cat"},
                {data: "close_cat",
                    render: function (data, type, full, meta) {
                        return !data ? `<span class="label label-primary">Activo</span>` : `<span class="label label-warning">Inactivo</span>`;
                    }
                },
                {data: null,
                    render: function (data, type, full, meta) {
                        let btn;
                        if (!full.close_cat) {
                            btn = `<div class="btn-group">
                                        <a class="btn btn-primary btn-xs" href="#/utilidades/categorias/editar?_id=${data.id_categoria}"><i class="far fa-edit"> Editar</i></a>
                                        <a class="btn btn-warning btn-xs clo" data-id="${data.id_categoria}"><i class="fas fa-ban"> Inhabilitar</i></a>
                                    </div>`;
                        } else {
                            btn = `<div class="btn-group">
                                        <a class="btn btn-info btn-xs clo" data-id="${data.id_categoria}"><i class="fas fa-check"> Habilitar</i></a>
                                    </div>`;
                        }
                        return btn;
                    }
                }
            ]
        });
        CambiarEstadoCategoria();
    };

    exports.BuscarCategorias = function (id) {
        $("#modal").load("vistas/modal_categoria.html", function () {
            $("#modal-categoria").modal();
            $.post("categoriasControlador", {
                accion: "buscar",
                id: id
            }).done((response) => {
                $('input[name="categoria"]').val(response.categoria);
                $('textarea[name="descripcion_cat"]').val(response.descripcion_cat);
                $('select[name="tipo_cat"]').val(response.tipo_cat);
            }).fail((response, jqxhr, error) => {
                toastr.error(error, " contacte con el desarrollador");
            }).always(function () {
                AgregarCategorias(id);
            });
            $("#modal-categoria").on('hidden.bs.modal', function (e) {
                window.history.back();
            });
        });
    };

    const AgregarCategorias = function (id) {
        $("#form-cat").on('submit', function (e) {
            e.preventDefault();
            let fd = ConvertFormToJSON(this);
            fd.id_categoria = id;
            $.post("categoriasControlador", {
                accion: "insertar",
                datos: JSON.stringify(fd)
            }).done(function (response, jqxhr, error) {
                if (response === 1) {
                    toastr.success("¡Guardado!");
                    $("#modal-categoria").modal("hide");
                } else {
                    toastr.error(error, " Por favor contacte con el desarrollador");
                }
            }).fail(function (response, jqxhr, error) {
                toastr.error(error, " Por favor contacte con el desarrollador");
            });
        });
    };

    const CambiarEstadoCategoria = function () {
        $("#tabla-categorias").on('click', 'a.clo', function () {
            let id = this.getAttribute('data-id');
            $.post('categoriasControlador', {
                accion: 'estado',
                id_categoria: id
            }).done((response, jqxhr, error) => {
                if (response === 1) {
                    toastr.success("¡Realizado!");
                    $("#tabla-categorias").DataTable().ajax.reload();
                } else {
                    toastr.error(`${error}, no se ha podido cambiar el estado de la categoria.`);
                }
            }).fail((response, jqxhr, error) => {
                toastr.error(`${error}, contacte con el desarrollador`);
            });
        });
    };

    return exports;
});

