define(function () {
    var exports = {};

    exports.Pais = function () {
        spinneron();

        $("#inicio").load("vistas/paises.html", function () {
            spinneroff();
            $(".active").removeClass("active");
            $(".utilidades").parent().find("ul.nav").removeClass("nav nav-second-level collapse").addClass("nav nav-second-level collapse in");
            $(".utilidades").parent().addClass("active");
            exports.ListarPaises();
            $(".add").on('click', function () {
                $("#modal").load("vistas/modal_pais.html", function () {
                    $("#modal-pais").modal();
                    exports.AgregarPaises(0);
                });
            });
        });
    };

    exports.ListarPaises = function () {
        $("#table-pais").DataTable({
            destroy: true,
            ajax: {
                type: "post",
                url: "paisesControlador",
                data: {accion: "listar"}
            },
            columns: [
                {data: "abreviacion_pais"},
                {data: "nombre_pais"},
                {data: "moneda_pais"},
                {data: "iso_mon_pais"},
                {data: null,
                    render: (data, type, full, meta) => {
                        return `<div class="btn-group">
                                    <a href="#/paises/editar?id=${data.id_pais}" class="btn btn-primary btn-xs det"><i class="far fa-edit"> Editar</i></a>
                                    <a href="#/paises/eliminar?id=${data.id_pais}" class="btn btn-warning btn-xs nulled"><i class="fa fa-ban"> Eliminar</i></a>
                                </div>`;
                    }
                }
            ]
        });
    };

    exports.AgregarPaises = function (id) {
        $("#form-pais").on('submit', function (e) {
            e.preventDefault();
            var fd = ConvertFormToJSON(this);
            fd.id_pais = id;
            $.post("paisesControlador", {
                accion: "insertar",
                datos: JSON.stringify(fd)
            }).done((response) => {
                if (response) {
                    toastr.success("¡Guardado!");
                    exports.ListarPaises();
                    $("#modal-pais").modal("hide");
                } else {
                    toastr.error("No se ha podido insertar");
                }
            }).fail((response, jqxhr, error) => {
                toastr.error(error);
            });
        });
    };


    exports.SelectPais = function () {
        const selectize = $('select[name="pais"]').selectize({
            placeholder: "País",
            valueField: 'phone_code',
            labelField: 'nombre_pais',
            searchField: 'nombre_pais',
            create: false,
            load: function (query, callback) {
                if (!query.length)
                    return callback();
                $.post('paisesControlador', {
                    accion: 'query',
                    q: query
                }).success((response, jqxhr, error) => {
                    callback(response);
                }).fail((response, jqxhr, error) => {
                    console.log(response);
                });
            },
            onInitialize: function () {
                var selectize = this;
                selectize.addOption({nombre_pais: "Paraguay", phone_code: "595"});
                selectize.setValue("595");
            }
        });
        return selectize;
    };

    exports.SeleccionarPais = function () {
        const select = exports.SelectPais();
        const selectize = select[0].selectize;

        const CambiarPais = function () {
            $('select[name="pais"]').on('change', function () {
                $('input[name="codigo"]').val(this.value);
            });
        };

        CambiarPais();

        $('input[name="codigo"]').on('input', function () {
            let codigo = this;
            $.post('paisesControlador', {
                accion: 'phonecode',
                phone_code: codigo.value
            }).success((response, jqxhr, error) => {
                if (response.phone_code) {
                    CambiarPais();
                    selectize.addOption(response);
                    selectize.setValue(response.phone_code);
                } else {
                    $('select[name="pais"]').off('change');
                    selectize.addOption({nombre_pais: "Desconocido", phone_code: ""});
                    selectize.setValue("");
                }
            }).fail((response, jqxhr, error) => {
                console.log(response);
            });
        });
    };

    exports.BuscarPaises = function (id) {
        $.post("paisesControlador", {
            accion: "buscar",
            id_pais: id
        }).done((response) => {
            $("#modal").load("vistas/modal_pais.html", function () {
                $('input[name="nombre_pais"]').val(response.nombre_pais);
                $('input[name="moneda_pais"]').val(response.moneda_pais);
                $('input[name="abreviacion_pais"]').val(response.abreviacion_pais);
                $('input[name="iso_mon_pais"]').val(response.iso_mon_pais);
                $("#modal-pais").modal();
                exports.AgregarPaises(id);
            });
        }).fail((response, jqxhr, error) => {
            toastr.error(error);
        });
    };

    exports.EliminarPaises = function (id) {
        $.post("paisesControlador", {
            accion: "eliminar",
            id: id
        }).done((response) => {
            if (response) {
                toastr.success("¡Eliminado!");
                exports.ListarPaises();
            } else {
                toastr.error("No se ha podido eliminar");
            }
        }).fail((response, jqxhr, error) => {
            toastr.error(error);
        });
    };

    return exports;
});