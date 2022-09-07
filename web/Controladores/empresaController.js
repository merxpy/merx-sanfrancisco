define(function () {
    var exports = {};

    exports.Empresa = function () {
        $(".active").removeClass("active");
        $("a[href='#/sistema/auditoria']").parent().find("ul.nav").removeClass("nav nav-second-level collapse in").addClass("nav nav-second-level collapse");
        $('a[href="#/sistema/auditoria"]').closest("ul").closest("li").addClass("active");
        $("#inicio").load("vistas/empresa.html", function () {
            exports.MostrarEmpresa();
            PermisosRender(13, (p) => {
                if (p.editar === 'N') {
                    $("#editar").remove();
                }
            });
        });
    };
    exports.MostrarEmpresa = function () {
        $.post("empresaControlador", {
            accion: "mostrar"
        }).done((response) => {
            $(".ibox.float-e-margins").html(`
                <div class="ibox-title">
                    <a id="editar" class="btn btn-primary btn-xs pull-right"><i class="fa fa-edit"></i> Editar</a>
                    <h5>Mi Empresa</h5>
                </div>
                <div class="ibox-content">
                    <div class="row">
                        <div class="col-sm-4">
                            <img src="${response.logo_empresa}" alt="225x225" class="img-thumbnail"/>
                        </div>
                        <div class="col-sm-8">
                            <div class="col-lg-8">
                                <h4>Nombre: ${response.nombre_empresa ? response.nombre_empresa : "NO DEFINIDO"}</h4>
                            </div>
                            <div class="col-lg-8">
                                <h4>Representante Legal: ${response.representante_legal ? response.representante_legal : "NO DEFINIDO"}</h4>
                            </div>
                            <div class="col-lg-8">
                                <h4>Teléfono: ${response.telefono_empresa ? response.telefono_empresa : "NO DEFINIDO"}</h4>
                            </div>
                            <div class="col-lg-8">
                                <h4>R.U.C.: ${response.ruc_empresa ? response.ruc_empresa : "4444441"}-${response.dv_empresa ? response.dv_empresa : "9"}</h4>
                            </div>
                        </div>
                    </div>
                </div>`);
        }).fail((response, jqxhr, error) => {
            toastr.error(error + ", verifique que el servidor esté encendido o contacte con el desarrollador.");
        }).always(() => {
            exports.BuscarEmpresa();
        });
    };
    exports.BuscarEmpresa = function () {
        $("#editar").on('click', function () {
            $("#modal").load("vistas/modal_empresa.html", function () {
                $("#modal-empresa").modal();
                $("#editar").attr("disabled", true);
                $('.input-daterange').datepicker({
                    keyboardNavigation: false,
                    forceParse: false,
                    autoclose: true,
                    format: "dd/mm/yyyy"
                });
                exports.SubirImagen();
                $.post("empresaControlador", {
                    accion: "mostrar"
                }).done((response) => {
                    if (!$.isEmptyObject(response)) {
                        $("#nombre_empresa").val(response.nombre_empresa);
                        $("#nombre_empresa").attr("data-id", response.id_empresa);
                        $("#legal").val(response.representante_legal);
                        $("#ruc_empresa").val(response.ruc_empresa);
                        $("#dv_empresa").val(response.dv_empresa);
                        $("#telefono_empresa").val(response.telefono_empresa);
                        $("#celular_empresa").val(response.celular_empresa);
                        $("#sitio_empresa").val(response.sitio_empresa);
                        $("#timbrado_empresa").val(response.timbrado_empresa);
                        $('input[name="inicio_vigencia"]').val(response.inicio_vigencia);
                        $('input[name="fin_vigencia"]').val(response.fin_vigencia);
                        setTimeout(() => {
                            $("#ciudad").val(response.id_ciudad).trigger('change');
                            $("#impresora").val(response.impresora);
                            $("#thermal_printer").val(response.thermal_printer);
                        }, 250);
                        $("#preview").attr('src', response.logo_empresa);
                        if (response.logo_empresa) {
                            $(".fileinput-filename").html("logo");
                            $("div.fileinput.fileinput-new").addClass("fileinput-exists").removeClass("fileinput-new");
                            $(".close.fileinput-exists").on('click', function () {
                                $("#preview").removeAttr("src");
                            });
                        }
                    }
                    $("#modal-empresa").on('hidden.bs.modal', function (e) {
                        $("#editar").attr("disabled", false);
                    });
                }).fail((response, jqxhr, error) => {
                    toastr.error(jqxhr.status);
                }).always(() => {
                    listarCiudades("select");
                    Impresoras();
                    $("#ciudad").select2({
                        allowClear: true,
                        destroy: true,
                        dropdownParent: $("#ciudad").parent().parent()
                    });
                    exports.AgregarEmpresa();
                });
            });
        });
    };

    exports.AgregarEmpresa = function () {
        $("#form-empresa").on("submit", function (e) {
            $("#save").attr("disabled", true);
            e.preventDefault();
            var fd = ConvertFormToJSON(this);
            fd.id_empresa = $("#nombre_empresa").attr("data-id") ? $("#nombre_empresa").attr("data-id") : 0;
            fd.dv_empresa = $("#dv_empresa").val();
            fd.logo_empresa = $('#preview').attr('src');
            $.post("empresaControlador", {
                accion: "insertar",
                datos: JSON.stringify(fd)
            }).done((response) => {
                if (response) {
                    toastr.success("¡Guardado!");
                    $("#modal-empresa").modal("hide");
                    exports.MostrarEmpresa();
                } else {
                    toastr.error("No se ha podido guardar la empresa");
                }
            }).fail((response, jqxhr, error) => {
                toastr.error(error);
            });
        });
    };
    exports.SubirImagen = function () {
        $('input[name="logo_emp"]').on('change', function () {
            var logo = this;
            if (logo.files && logo.files[0]) {
                var reader = new FileReader();
                reader.onload = function (e) {
                    $("#preview").attr('src', e.target.result);
                };
                reader.readAsDataURL(logo.files[0]);
            }
            $(".close.fileinput-exists").on('click', function () {
                $("#preview").removeAttr("src");
            });
        });
    };
    exports.VerificarTimbrado = function (callback) {
        $.post("empresaControlador", {
            accion: 'verificar'
        }).done(function (response) {
            if (response === 1) {
                Push.create("TIMBRADO VENCIDO", {//Titulo de la notificación
                    body: "El número de timbrado ha vencido por favor actualícelo para utilizar el módulo de ventas", //Texto del cuerpo de la notificación
                    icon: 'img/icon.png', //Icono de la notificación
                    timeout: 10000, //Tiempo de duración de la notificación
                    onClick: function () {//Función que se cumple al realizar clic cobre la notificación
                        this.close(); //Cierra la notificación
                    }
                });
            }
            callback(response);
        }).fail(function (response, jqxhr, error) {
            toastr.error(error + ", contacte con el desarrollador");
        });
    };   
    const Impresoras = function () {
        $.get("http://localhost:5000/printer").done(function (response) {
            $(response).each((i) => {
                $("#impresora").append(`<option>${response[i]}</option>`);
                $("#thermal_printer").append(`<option>${response[i]}</option>`);
            });
        }).fail(function (response, jqxhr, error) {
            toastr.error("Error! se puedo conectar con el servidor");
        });
    };
    return exports;
});

