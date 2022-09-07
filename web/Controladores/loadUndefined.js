function AgregarUndefineds() {
    $(".undefined").on('click', function () {
        switch ($(this).attr("data-href")) {
            case "marca":
                $("div.modal.fade.in").addClass("modal-over");
                $("#modal").load("vistas/modal_marca.html", {}, function () {
                    $("#Modal-Marca").modal();
                    AgregarMarcas(0, 0);
                    $("#Modal-Marca").on('hidden.bs.modal', function (e) {
                        if ($(".modal").length > 1) {
                            $("div.modal.fade.in").removeClass("modal-over");
                            $("body").addClass("modal-open");
                        }
                    });
                });
                break;
            case "etiqueta":
                $("div.modal.fade.in").addClass("modal-over");
                $("#modal").load("vistas/modal_eti.html", {}, function () {
                    $("#Modal-etiqueta").modal();
                    AgregarEtiquetas(0, 0);
//                    $("#Modal-etiqueta").on('hidden.bs.modal', function (e) {
//                        $("div.modal.fade.in").removeClass("modal-over");
//                        $("body").addClass("modal-open");
//                    });
                });
                break;
            case "unidad":
                $("div.modal.fade.in").addClass("modal-over");
                $("#modal").load("vistas/modal_unidad.html", {}, function () {
                    $("#Modal-unidad").modal();
                    AgregarUnidad(0, 0);
//                    $("#Modal-unidad").on('hidden.bs.modal', function (e) {
//                        $("div.modal.fade.in").removeClass("modal-over");
//                        $("body").addClass("modal-open");
//                    });
                });
                break;
            case "categoria":
                $("div.modal.fade.in").addClass("modal-over");
                $("#modal").load("vistas/modal_categoria.html", {}, function () {
                    $("#modal-categoria").modal();
                    $("#form-cat").on('submit', function (e) {
                        e.preventDefault();
                        let fd = ConvertFormToJSON(this);
                        fd.id_categoria = 0;
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
                });
                break;
        }
    });
}