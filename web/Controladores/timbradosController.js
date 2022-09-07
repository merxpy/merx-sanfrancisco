//function timbrado() {
//    spinneron();
//    
//    $("#inicio").load("vistas/timbrados.html", {}, function () {
//        spinneroff();
//        $(".active").removeClass("active");
//        $(".config").parent().find("ul.nav").removeClass("nav nav-second-level collapse").addClass("nav nav-second-level collapse in");
//        $(".config").parent().addClass("active");
//        listarTimbrados();
//        editarTimbrado();
//        $(".add").on('click', function () {
//            $("#modal").load("vistas/modaltim.html", {}, function () {
//                num();
//                $('.date input').datepicker({
//                    format: "dd/mm/yyyy"
//                });
//                $("#Modal-tim").modal();
//                agregarTimbrado(0, 0);
//            });
//        });
//    });
//}
//
//function listarTimbrados() {
//    $("#table-tim").DataTable({
//        destroy: true,
//        ajax: {
//            type: "post",
//            url: "timbradoControlador",
//            data: {accion: "listar"}
//        },
//        columns: [
//            {data: "id_timbrado"},
//            {data: "numero_tim"},
//            {data: "vencimiento_tim"},
//            {data: null,
//                render: function (data, type, full, meta) {
//                    return '<div class="btn-group"><button class="btn btn-primary btn-xs det" data-id="' + data.id_timbrado + '"><i class="far fa-edit"> Editar</i></button><button class="btn btn-warning btn-xs clo" data-id="' + data.id_timbrado + '"><i class="fa fa-ban"> Inhabilitar</i></button></div>';
//                }
//            }
//        ]
//    });
//}
//
//function agregarTimbrado(accion, id) {
//    $("#form-tim").submit(function (e) {
//        e.preventDefault();
//        $.post("timbradoControlador",
//                {
//                    accion: accion,
//                    id_timbrado: id,
//                    numero_tim: $("#numero_tim").val(),
//                    vencimiento_tim: $("#vencimiento_tim").val()
//                })
//                .done(function (response) {
//                    if (response == 1) {
//                        toastr.success("Guardado!");
//                        $("#Modal-tim").modal("hide");
//                        listarTimbrados();
//                    } else {
//                        toastr.error("Error Inesperado");
//                    }
//                })
//                .fail(function (response, jqxhr, error) {
//                    toastr.error(error);
//                });
//    });
//}
//
//function editarTimbrado() {
//    $("#table-tim tbody").on('click', '.det', function () {
//        var id = $(this).attr("data-id");
//        $("#modal").load("vistas/modaltim.html", {}, function () {
//            num();
//            agregarTimbrado(1, id);
//            $('.date input').datepicker({
//                format: "dd/mm/yyyy",
//                autoClose: true
//            }).on('changeDate', function (ev) {
//                $('.date input').datepicker('hide');
//            });
//
//            $("#Modal-tim").modal();
//            $.post("timbradoControlador",
//                    {
//                        accion: "buscar",
//                        id_timbrado: id
//
//                    })
//                    .done(function (response) {
//                        $("#numero_tim").val(response.numero_tim);
//                        $("#vencimiento_tim").val(response.vencimiento_tim);
//                    })
//                    .fail(function (response, jqxhr, error) {
//                        toastr.error(error);
//                    });
//        });
//    });
//}