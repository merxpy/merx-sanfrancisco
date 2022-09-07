define(function () {
    var exports = {};
    exports.Delivery = function (tipo) {
        spinneron();
        $("#inicio").load("vistas/delivery.html", () => {
            spinneroff();
            $(".active").removeClass("active");
            $(".level").closest("li").find("ul.nav").removeClass("nav nav-second-level collapse in").addClass("nav nav-second-level collapse");
            $('a.delivery').closest("li").addClass("active");
            $('a.delivery').closest("li").find("ul.nav").removeClass("nav nav-second-level collapse").addClass("nav nav-second-level collapse in");
            listarDeliveries(tipo);

            if (tipo === "ENVIO") {
                $('a[href="#/delivery/retiros/agregar"]').remove();
            }

            $("#tabla-delivery tbody").on('click', 'td a.clo', function (e) {
                exports.EliminarDeliveries(this.getAttribute('data-id'));
            });
            CambiarEstado('FINALIZADO');
        });
    };

    let AgregarDelivery = function (id) {
        $("#form-delivery").on('submit', function (e) {
            e.preventDefault();
            let datos = ConvertFormToJSON(this);
            datos.cliente_ref = $('input[name="cedula"]').attr('data-id');
            datos.ruc_dv = $('input[name="ruc_dv"]').val();
            $("#modal_delivery").modal('hide');
            $("#modal_delivery").on('hidden.bs.modal', function () {
                $("#modal").load("vistas/modal_modo_pago.html", function () {
                    $("#Modal-pago").modal();
                    $('input[name="delivery_ped"]').attr('checked');
                    $("#cambio").closest('div.row').remove();
                    $("div.col-sm-8.col-sm-offset-2").append(`
                                <div class="form-group">
                                    <div class="col-sm-12">
                                        <input type="text" class="form-control m-b text-center miles" id="efectivo" name="efectivo" required="">
                                    </div>
                                </div>`);
                    $("#efectivo").focus().select();
                    $("#efectivo").on('input', function () {
                        this.value = dot(this.value.replace(/[.+]/g, ""));
                    });
                    $("#select-tipopago").on('change', function () {
                        if (parseInt(this.value) !== 1) {
                            $("#efectivo").closest("div.form-group").hide();
                            $("#efectivo").removeAttr("required")
                        } else {
                            $("#efectivo").focus().select();
                            $("#efectivo").closest("div.form-group").show();
                            $("#efectivo").attr("required")
                        }
                    });

                    $("form#aceptar").on('submit', function (e) {
                        e.preventDefault();
                        if (parseInt($("#select-tipopago").val()) === 1) {
                            datos.total_pago = $("#efectivo").val().replace(/[.+]/g, "");
                            datos.id_tipo_pago = $("#select-tipopago").val();
                        } else {
                            datos.id_tipo_pago = $("#select-tipopago").val();
                        }

                        $.post('deliveriesControlador', {
                            id: id,
                            accion: 'insertar',
                            data: JSON.stringify(datos)
                        }).done((response) => {
                            let h = document.location.toString();
                            let l = h.substring(h.indexOf("#"));
                            if (l === "#/servicios/nuevo") {
                                $('input[name="delivery_ped"]').attr('data-id', response);
                            }
                            $("#Modal-pago").modal('hide');
                            toastr.success('¡Guardado!');
                        }).fail((response, jqxhr, error) => {
                            toastr.error('No se pudo registrar el delivery');
                        });
                    });

                    $("#Modal-pago").on('hidden.bs.modal', function () {
                        let h = document.location.toString();
                        let l = h.substring(h.indexOf("#"));
                        if (l !== "#/servicios/nuevo") {
                            history.pushState('', '', '#/delivery/retiros/');
                            $("#tabla-delivery").DataTable().ajax.reload();
                        } else {
                            $('input[name="delivery_ped"]').attr('data-id') === undefined ? $('input[name="delivery_ped"]').removeAttr('checked') : "";
                        }
                    });
                });
            });
        });
    };

    exports.BuscarDelivery = function (id) {
        $.post('deliveriesControlador', {
            accion: 'buscar',
            id: id
        }).done((response) => {
            $("#modal").load("vistas/modal_delivery.html", () => {
                $('input[name="cedula"]').attr('data-id', response.cliente_ref);
                $('input[name="cedula"]').val(response.cedula);
                $('input[name="nombre"]').val(response.nombre);
                $('input[name="referencia"]').val(response.referencia);
                $('input[name="razon_social"]').val(response.nombre_cli);
                $('input[name="ruc"]').val(response.ruc);
                $('input[name="ruc_dv"]').val(response.ruc_dv);
                $('input[name="celular"]').val(response.celular);
                $('input[name="ubicacion"]').val(response.ubicacion);
                $('input[name="direccion"]').val(response.direccion);
                $('input[name="telefono"]').val(response.telefono);
                $('input[name="fecha"]').val(response.fecha);
                $('input[name="hora_inicio"]').val(response.hora_inicio ? moment(response.hora_inicio, 'HH:mm:ss').format('HH:mm') : "");
                $('input[name="hora_fin"]').val(response.hora_fin ? moment(response.hora_fin, 'HH:mm:ss').format('HH:mm') : "");
                $('textarea[name="observaciones"]').html(response.observaciones);

                let h = document.location.toString();
                let l = h.substring(h.indexOf("#"));
                if (l === "#/servicios/nuevo") {
                    $.post('clientesControlador', {
                        accion: 'buscar',
                        id_cliente: $('select[name="id_cliente"]').val()
                    }).done((response) => {
                        $('input[name="cedula"]').attr('data-id', response.id_cliente);
                        $('input[name="nombre"]').val(response.nombre_cli);
                        $('input[name="cedula"]').val(response.ndocumento_cli);
                        $('input[name="razon_social"]').val(response.razon_social);
                        $('input[name="ruc"]').val(response.ruc);
                        $('input[name="ruc_dv"]').val(response.ruc_dv);
                        $('input[name="ubicacion"]').val(response.googlemaps_cli);
                        $('input[name="direccion"]').val(response.direccion_cli);
                        $('input[name="celular"]').val(response.celular_cli);
                        $('input[name="telefono"]').val(response.telefono_cli);
                        $('input[name="referencia"]').val(response.referencia_cli);
                        $('input[name="hora_inicio"]').val(moment(response.hora_inicio_cli, 'HH:mm:ss').format('HH:mm'));
                        $('input[name="hora_fin"]').val(moment(response.hora_fin_cli, 'HH:mm:ss').format('HH:mm'));
                    }).fail((response) => {
                        console.log("La puerca esta en la pocilga");
                    });
                }

                $('button[data-dismiss]').on('click', function () {
                    $("#modal_delivery").on('hidden.bs.modal', function () {
                        if (l !== "#/servicios/nuevo") {
                            history.pushState('', '', '#/delivery/retiros/');
                            $("#tabla-delivery").DataTable().ajax.reload();
                        } else {
                            $('input[name="delivery_ped"]').attr('data-id') === undefined ? $('input[name="delivery_ped"]').removeAttr('checked') : "";
                        }
                    });
                });
                $("#modal_delivery").modal({
                    backdrop: 'static',
                    keyboard: false
                });


                num();

                if ($('input[name="fecha_entrega"]').length > 0) {
                    $('input[name="fecha"]').val($('input[name="fecha_entrega"]').val());
                    $('.date input[name="fecha"]').datepicker({
                        format: "dd/mm/yyyy"
                    });
                } else {
                    $('.date input[name="fecha"]').datepicker({
                        format: "dd/mm/yyyy"
                    }).datepicker('setDate', "+1d");
                }

                ruc();

                $('input[name="hora_inicio"]').on('change', function () {
                    $('input[name="hora_fin"]').val(moment($('input[name="hora_inicio"]').val(), "HH:mm").add(moment.duration(3, 'hours')).format("HH:mm"));
                    validarHoras();
                });
                $('input[name="hora_fin"]').on('change', function () {
                    validarHoras();
                });
                $('input[name="cedula"]').on('change', function () {
                    if (this.value !== "") {
                        $.post('clientesControlador', {
                            accion: 'query',
                            q: this.value
                        }).done((response) => {
                            $('input[name="cedula"]').attr('data-id', response[0].id_cliente);
                            $('input[name="nombre"]').val(response[0].nombre_cli);
                            $('input[name="razon_social"]').val(response[0].razon_social);
                            $('input[name="ruc"]').val(response[0].ruc);
                            $('input[name="ruc_dv"]').val(response[0].ruc_dv);
                            $('input[name="direccion"]').val(response[0].direccion_cli);
                            $('input[name="ubicacion"]').val(response[0].googlemaps_cli);
                            $('input[name="celular"]').val(response[0].celular_cli);
                            $('input[name="telefono"]').val(response[0].telefono_cli);
                            $('input[name="referencia"]').val(response[0].referencia_cli);
                            $('input[name="hora_inicio"]').val(moment(response[0].hora_inicio_cli, 'HH:mm:ss').format('HH:mm'));
                            $('input[name="hora_fin"]').val(moment(response[0].hora_fin_cli, 'HH:mm:ss').format('HH:mm'));
                        }).fail((response, jqxhr, error) => {
                            console.log("Hello world bitch");
                        });
                    }
                });
                $("#modal_delivery").on('hidden.bs.modal', function () {
                    console.log($("#Modal-pago").length);
                });

                AgregarDelivery(id);
            });
        }).fail((response, jqxhr, error) => {
            console.log("No se pudo obtener los datos");
        });
    };

    exports.EliminarDeliveries = function (id) {
        $.post('deliveriesControlador', {
            accion: 'eliminar',
            id: id
        }).done((response) => {
            if (response === 1) {
                let h = document.location.toString();
                let l = h.substring(h.indexOf("#"));
                if (!l.includes('servicios')) {
                    $("#tabla-delivery").DataTable().ajax.reload();
                    toastr.clear();
                    toastr.success('Los Registros han sido borrados.');
                }
            } else {
                toastr.warning('Antes de borrar el registro debe anular la orden.');
                console.warn("Fuck whoever reads it.");
            }
        }).fail((response, jqxhr, error) => {
            console.warn("Fuck whoever reads it.");
        });
    };

    exports.CompartirDelivery = function (id) {
        $("#modal").load("vistas/modal_compartircliente.html", function () {
            $("#share_modal").modal();
            SeleccionarPais();
            $.post("deliveriesControlador", {
                accion: "buscar",
                id: id
            }, function (response) {
                $("textarea").html(response.nombre + "\n tel. " + response.telefono + "\n cel. " + response.celular
                        + "\n horario: " + moment(response.hora_inicio, 'HH:mm:ss').format('HH:mm') + '-' + moment(response.hora_fin, 'HH:mm:ss').format('HH:mm')
                        + "\n Facturar a nombre de " + response.nombre_cli + " RUC " + response.ndocumento_cli + "-" + response.dv_cli
                        + "\n" + response.direccion + "\n" + response.referencia + "\n" + response.descripcion + "\n" + response.observaciones + "\n" + response.ubicacion);
            });

            $("#compartir").on('submit', function (e) {
                e.preventDefault();
                let text = $("textarea").serialize();
                window.open(`https://web.whatsapp.com/send?phone=${$('input[name="codigo"]').val() + $('input[name="phone"]').val()}&${text}`);
            });

            $("#share_modal").on('hidden.bs.modal', function () {
                window.history.back();
            });
        });
    };

    const listarDeliveries = function (tipo) {
        $("#tabla-delivery").DataTable({
            pageLength: 100,
            destroy: true,
            dom: '<"html5buttons"B>lTfgitp',
            order: [[0, 'desc']],
            buttons: [
                {extend: 'excel', title: 'Clientes',
                    exportOptions: {
                        columns: 'th:not(:last-child)'
                    }
                },
                {extend: 'pdf', title: 'Clientes',
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
                url: "deliveriesControlador",
                data: {
                    accion: "listar",
                    estado: tipo
                }
            },
            columns: [
                {data: "id_delivery"},
                {data: "nombre"},
                {data: "hora_inicio",
                    render: function (data, type, full, meta) {
                        return `${moment(data, 'HH:mm:ss').format('HH:mm')} - ${moment(full.hora_fin, 'HH:mm:ss').format('HH:mm')}`;
                    }
                },
                {data: "direccion",
                    render: function (data, type, full, meta) {
                        return `<a href="${full.ubicacion}" target="_blank">${data}</a>`;
                    }
                },
                {data: "telefono"},
                {data: null,
                    render: function (data, type, full, meta) {
                        if (tipo !== "ENVIO") {
                            return `<div class="btn-group">
                                        <a class="btn btn-primary btn-xs" href="#/delivery/retiros/generar?_id=${data.id_delivery}"><i class="fas fa-cogs"></i> Generar</a>
                                        <a class="btn btn-info btn-xs" href="#/delivery/compartir?_id=${data.id_delivery}"><i class="fab fa-whatsapp"></i> Compartir</a>
                                        <a class="btn btn-warning btn-xs deliver" data-id=${data.id_delivery}><i class="fas fa-check"></i> Retirado</a>                            
                                        <a class="btn btn-danger btn-xs clo" data-id=${data.id_delivery}><i class="fas fa-trash-alt"></i> Borrar</a>
                                    </div>`;
                        } else {
                            return `<div class="btn-group">
                                        <a class="btn btn-default btn-xs" href="#/servicios/editar?_id=${data.id_ciudad}"><i class="fas fa-external-link-alt"></i> Orden</a>
                                        <a class="btn btn-info btn-xs" href="#/delivery/compartir?_id=${data.id_delivery}"><i class="fab fa-whatsapp"></i> Compartir</a>
                                        <a class="btn btn-warning btn-xs deliver" data-id=${data.id_delivery}><i class="fas fa-check"></i> Enviado</a>                             
                                        <a class="btn btn-danger btn-xs clo" data-id=${data.id_delivery}><i class="fas fa-trash-alt"></i> Borrar</a>
                                    </div>`;
                        }
                    }
                }
            ]
        });
    };

    const validarHoras = function () {
        if (moment($('input[name="hora_inicio"]').val(), "HH:mm") > moment($('input[name="hora_fin"]').val(), "HH:mm")) {
            $('input[name="hora_inicio"]').closest('div.form-group').addClass('has-error');
            $('span.text-danger').removeClass('hide').html('<i class="fas fa-times-circle"></i> <b>La hora inicial es mayor a la hora final</b>');
            $('button[type="submit"]').attr('disabled', true);
        } else {
            $('input[name="hora_inicio"]').closest('div.form-group').removeClass('has-error');
            $('span.text-danger').addClass('hide');
            $('button[type="submit"]').attr('disabled', false);
            if (moment.utc(moment($('input[name="hora_fin"]').val(), "HH:mm").diff(moment($('input[name="hora_inicio"]').val(), "HH:mm"))).format("H") < 3) {
                $('input[name="hora_inicio"]').closest('div.form-group').addClass('has-error');
                $('span.text-danger').removeClass('hide').html('<i class="fas fa-info-circle"></i> <b>Debe haber minimamente 3 horas de intervalo.</b>');
                $('button[type="submit"]').attr('disabled', true);
            } else {
                $('input[name="hora_inicio"]').closest('div.form-group').removeClass('has-error');
                $('span.text-danger').addClass('hide');
                $('button[type="submit"]').attr('disabled', false);
            }
        }
    };

    const CambiarEstado = function (estado) {
        $("#tabla-delivery tbody").on('click', 'td a.deliver', function () {
            let id = $(this).attr('data-id');
            $.post("deliveriesControlador", {
                accion: "entregar",
                datos: JSON.stringify({
                    id_delivery: id,
                    estado: estado
                })
            }).done((response) => {
                toastr.clear();
                toastr.success("¡Hecho!");
                $("#tabla-delivery").DataTable().ajax.reload();
            }).fail((response, jqxhr, error) => {
                console.log(error);
            });
        });
    };

    return exports;
});