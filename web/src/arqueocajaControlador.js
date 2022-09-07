define(function () {
    var exports = {};
    
    exports.ArqueoCaja = function () {
        spinneron();
        $("#inicio").load("vistas/cortecaja.html", {}, function () {
            spinneroff();
            $(".level").parent().find("ul.nav").removeClass("nav nav-second-level collapse in").addClass("nav nav-second-level collapse");
            $(".active").removeClass("active");
            $('a[href="#/arqueocaja"]').parent().addClass("active");
            ListarArqueos();
            AgregarArqueo();
        });
    };
    
    function ListarArqueos() {
        $("#tabla-arqueo").DataTable({
            "pageLength": 100,
            ajax: {
                type: 'post',
                url: 'arqueocajaControlador',
                data: {accion: "listar"}
            },
            columns: [
                {data: "codigo_caja"},
                {data: "alias_usu"},
                {data: "fecha_arqueo"},
                {data: "hora_arqueo"}
            ]
                    
        });
    }
    
    function AgregarArqueo() {
        $("#arqueo").on('click', function (e) {
            e.preventDefault();
            $("#modal").load("vistas/modal_caja.html", function () {
                $("#modal-cja").modal();
                let reg = new RegExp('^[0-9]+$');
                $(".miles").on('keypress keyup', function () {
                    if (reg.test(Entero(this.value))) {
                        this.value !== "" ? this.value = dot(Entero(this.value)) : 0;
                    } else {
                        this.value = 0;
                    }
                });
                
                $(".count").on('change', function () {
                    let suma = 0;
                    $(".count").each(function () {
                        suma = parseInt(suma) + parseInt(Entero(this.value));
                    });
                    $("#contado").val(dot(suma));
                    CalcularDiferencias(this);
                });
                
                $(".retired").on('change', function () {
                    let suma = 0;
                    $(".retired").each(function () {
                        suma = parseInt(suma) + parseInt(Entero(this.value));
                    });
                    $("#totalret").val(dot(suma));
                });
                
                $.post("arqueocajaControlador", {
                    accion: "arqueo"
                }).done(function (response) {
                    let sum = 0;
                    $(response).each(function (i) {
                        switch (response[i].id_tipo_pago) {
                            case 1:
                                $("#efectivo").val(dot(response[i].calculado));
                                break;
                            case 2:
                                $("#cheque").val(dot(response[i].calculado));
                                break;
                            case 3:
                                $("#tc").val(dot(response[i].calculado));
                                break;
                            case 4:
                                $("#td").val(dot(response[i].calculado));
                                break;
                        }
                        sum = sum + response[i].calculado;
                    });
                    $("#calculado").val(dot(sum));
                }).fail(function (response, jqxhr, error) {
                    toastr.error(error + ", por favor contacte con el desarrollador");
                }).always(function () {
                    $('.miles').each(function (i) {
                        CalcularDiferencias(this);
                    });
                });
                GuardarArqueo();
            });
        });
    }
    
    function CalcularDiferencias(e) {
        
        switch ($(e).attr("data-val")) {
            case "efectivo":
                $("#difefectivo").val(dot(parseInt(Entero($(e).val())) - parseInt(Entero($("#efectivo").val()))));
                $("#retefectivo").val(dot(parseInt(Entero($(e).val()))));
                break;
            case "cheque":
                $("#difcheque").val(dot(parseInt(Entero($(e).val())) - parseInt(Entero($("#cheque").val()))));
                $("#retcheque").val(dot(parseInt(Entero($(e).val()))));
                break;
            case "tc":
                $("#diftc").val(dot(parseInt(Entero($(e).val())) - parseInt(Entero($("#tc").val()))));
                $("#rettc").val(dot(parseInt(Entero($(e).val()))));
                break;
            case "td":
                $("#diftd").val(dot(parseInt(Entero($(e).val())) - parseInt(Entero($("#td").val()))));
                $("#rettd").val(dot(parseInt(Entero($(e).val()))));
                break;
        }
        let dif = parseInt(Entero($("#difefectivo").val())) + parseInt(Entero($("#difcheque").val())) + parseInt(Entero($("#diftc").val())) + parseInt(Entero($("#diftd").val()));
        $("#diferencia").val(dot(dif));
        
        let suma = 0;
        $(".retired").each(function () {
            suma = parseInt(suma) + parseInt(Entero(this.value));
        });
        $("#totalret").val(dot(suma));
    }
    
    function GuardarArqueo() {
        $("#form-arqueo").on('submit', function (e) {
            e.preventDefault();
            let arqueo = [];
            let fd = {
                contado: Entero($("#contado").val()),
                calculado: Entero($("#calculado").val()),
                diferencia: Entero($("#diferencia").val()),
                retirado: Entero($("#totalret").val())
            };
            
            $("#calculos tbody tr").each(function (i) {
                if ($(this).find("input").attr("data-val") === "efectivo") {
                    arqueo[i] = {
                        id_tipo_pago: 1,
                        contado: Entero($(this).find("input[data-val='efectivo']").val()),
                        calculado: Entero($("#efectivo").val()),
                        diferencia: Entero($("#difefectivo").val()),
                        retirado: Entero($("#retefectivo").val())
                    };
                } else if ($(this).find("input").attr("data-val") === "cheque") {
                    arqueo[i] = {
                        id_tipo_pago: 2,
                        contado: Entero($(this).find("input[data-val='cheque']").val()),
                        calculado: Entero($("#cheque").val()),
                        diferencia: Entero($("#difcheque").val()),
                        retirado: Entero($("#retcheque").val())
                    };
                } else if ($(this).find("input").attr("data-val") === "tc") {
                    arqueo[i] = {
                        id_tipo_pago: 3,
                        contado: Entero($(this).find("input[data-val='tc']").val()),
                        calculado: Entero($("#tc").val()),
                        diferencia: Entero($("#diftc").val()),
                        retirado: Entero($("#rettc").val())
                    };
                } else if ($(this).find("input").attr("data-val") === "td") {
                    arqueo[i] = {
                        id_tipo_pago: 4,
                        contado: Entero($(this).find("input[data-val='td']").val()),
                        calculado: Entero($("#td").val()),
                        diferencia: Entero($("#diftd").val()),
                        retirado: Entero($("#rettd").val())
                    };
                }
            });
            
            $.post('arqueocajaControlador', {
                accion: "insertar",
                datos: JSON.stringify(fd),
                detalle: JSON.stringify(arqueo)
            }).done(function (response, jqxhr, error) {
                if (response === 1) {
                    toastr.success("¡Realizado!");
                    $("#tabla-arqueo").DataTable().ajax.reload();
                    $("#modal-cja").modal('hide');
                } else {
                    console.log(jqxhr);
                    toastr.error(error + ", error inesperado, contacte con el desarrollador");
                }
            }).fail(function (response, jqxhr, error) {
                toastr.error(error + ", contacte con el desarrollador.");
            });
        });
    }
    
    return exports;
});