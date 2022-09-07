define(['comprasControlador'], function (compra) {
    var exports = {};
    exports.Gastos = function () {
        spinneron();
        $(".level").parent().find("ul.nav").removeClass("nav nav-second-level collapse in").addClass("nav nav-second-level collapse");
        $("#inicio").load("vistas/gastos.html", () => {
            spinneroff();
            ListarGastos();
            AnularGastos();
        });
    };

    exports.BuscarGastos = function (id) {
        $("#inicio").load("vistas/insertGastos.html", () => {
            $.post("gastosControlador", {
                accion: 'buscar',
                id: id
            }).done((response) => {
                $('input[name=nro_factura]').val(response.nro_factura);
                $('input[name="fecha"]').val(response.fecha ? moment(response.fecha, 'YYYY-MM-DD').format('DD/MM/YYYY') : "");
                $('input[name="timbrado"]').val(response.timbrado);
                $('input[value="' + response.tipo_gasto + '"]').prop("checked", true);
                $('input[name="total_10"]').val(dot(response.total_10));
                $('input[name="total_5"]').val(dot(response.total_5));
                $('input[name="total_exenta"]').val(dot(response.total_exenta));
                $('input[name="total_gasto"]').val(dot(response.total_gasto));
                $('#pro').selectize({
                    placeholder: 'Buscar proveedor ...',
                    valueField: 'id_proveedor',
                    labelField: 'nombre_pro',
                    searchField: ['nombre_pro', 'ndocumento_pro'],
                    options: [],
                    create: false,
                    load: function (query, callback) {
                        if (!query.length)
                            return callback();
                        $.ajax({
                            url: 'proveedorControlador',
                            type: 'POST',
                            data: {
                                q: query,
                                accion: "query"
                            },
                            error: function (response, jqxhr, error) {
                                callback(error);
                            },
                            success: function (res) {
                                callback(res);
                            }
                        });
                    },
                    onInitialize: function () {
                        var selectize = this;
                        selectize.addOption(response);
                        selectize.setValue(response.id_proveedor);
                    }
                });

                $('.date input').datepicker({
                    format: "dd/mm/yyyy",
                    orientation: "bottom"
                });

                $(".touchspin1").TouchSpin({
                    buttondown_class: 'btn btn-white',
                    buttonup_class: 'btn btn-white'
                });

                $("#nuevo").on('click', function () {
                    compra.NuevoProveedor();
                });
            }).fail((response, jqxhr, error) => {
                toastr.error("Error interno del servidor: " + response);
            }).always(() => {
                $('input.miles').on('input', function () {
                    if (parseInt(Entero(this.value)) < 0) {
                        var aux = Entero(this.value);
                        $(this).val(dot(aux.substring(1, aux.length)));
                    }
                    if (!isNaN(parseInt(Entero(this.value)))) {
                        this.value = dot(Entero(this.value));
                    } else {
                        this.value = 0;
                        this.select();
                    }
                    this.value = dot(Entero(this.value));
                });
                $('input.miles').on('click', function () {
                    this.select();
                });
                $('input.miles').on('change, input', function () {
                    let sum = 0;
                    $('input.miles').each(function () {
                        sum = sum + parseInt(Entero(this.value));
                    });
                    $('input[name="total_gasto"]').val(dot(sum));
                });
                GuardarGasto(id);
            });
        });
    };

    const ListarGastos = function () {
        $("#tabla-gasto").DataTable({
            destroy: true,
            columnDefs: [
                {type: 'date-uk', targets: 2}
            ],
            order: [[0, 'desc']],
            dom: '<"html5buttons"B>lTfgitp',
            buttons: [
                {extend: 'excel', title: 'Gastos',
                    exportOptions: {
                        columns: 'th:not(:last-child)'
                    }
                },
                {extend: 'pdf', title: 'Gastos',
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
                url: "gastosControlador",
                data: {
                    accion: "listar"
                }
            },
            columns: [
                {data: "id_gasto",
                    render: function (data, type, full, meta) {
                        return cero(data);
                    }
                },
                {data: "nro_factura"},
                {data: "fecha",
                    render: function (data, type, full, meta) {
                        return moment(data, 'YYYY-MM-DD').format('DD/MM/YYYY');
                    }
                },
                {data: "tipo_gasto",
                    render: function (data, type, full, meta) {
                        if (data === 'contado') {
                            return `<label class="label label-warning">Contado</label>`;
                        } else {
                            return `<label class="label label-danger">Crédito</label>`;
                        }
                    }
                },
                {data: "total_gasto",
                    render: function (data, type, full, meta) {
                        return dot(data);
                    }
                },
                {data: "nombre_pro"},
                {data: null,
                    render: function (data, type, full, meta) {
                        return `<div class="btn-group">
                                        <a class="btn btn-primary btn-xs" href="#/gastos/editar?_id=${data.id_gasto}"><i class="far fa-edit"> Editar</i></a>
                                        <a class="btn btn-info btn-xs clo" data-id=${data.id_gasto}><i class="fa fa-ban"> Anular</i></a>
                                    </div>`;
                    }
                }
            ],
            initComplete: function () {
                PermisosRender(4, (p) => {
                    if (p.editar === 'N') {
                        $('.editar').remove();
                    }
                    if (p.eliminar === 'N') {
                        $('.clo').remove();
                    }
                });
            }
        });
    };

    const GuardarGasto = function (id) {
        $("#form-gasto").on('submit', function (e) {
            e.preventDefault();
            var datos = ConvertFormToJSON(this);
            datos.id_gasto = id;
            datos.fecha = moment(datos.fecha, 'DD/MM/YYYY').format('YYYY-MM-DD');
            datos.total_10 = Entero(datos.total_10);
            datos.total_5 = Entero(datos.total_5);
            datos.total_exenta = Entero(datos.total_exenta);
            datos.total_gasto = Entero(datos.total_gasto);
            $.post("gastosControlador", {
                accion: 'insertar',
                datos: JSON.stringify(datos)
            }).done((response) => {
                if (response === 1) {
                    toastr.success("¡Guardado!");
                    document.getElementById("form-gasto").reset();
                } else if (response === 3) {
                    toastr.warning("Ya existe un registro con esos datos.");
                } else {
                    toastr.error("Error inesperado, no se han podido guardar los datos.");

                }
            }).fail((response, jqxhr, error) => {
                toastr.error("Error interno del servidor", response);
            });
        });
    };

    const AnularGastos = function () {
        $('#tabla-gasto tbody ').on('click', 'a.clo', function () {
            var id = this.getAttribute("data-id");
            $.post('gastosControlador', {
                accion: "anular",
                id: id
            }).done((response, jqxhr, error) => {
                if (response === 1) {
                    toastr.success("¡Hecho!");
                    $("#tabla-gasto").DataTable().ajax.reload();
                } else {
                    toastr.warning("Error, no se han podido realizar los cambios");
                }
            }).fail((response, jqxhr, error) => {
                toastr.error("Error interno del servidor " + response);
            });
        });
    };
    return exports;
});


