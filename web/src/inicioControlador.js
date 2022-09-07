define(['anotacionesControlador'], function (anotacion) {
    var exports = {};
    exports.CargarAdminPanel = function () {
        spinneron();
        $("#inicio").load("vistas/paneladministrador.html", {}, function () {
            spinneroff();
            $(".active").removeClass("active");
            $(".level").parent().find("ul.nav").removeClass("nav nav-second-level collapse in").addClass("nav nav-second-level collapse");
            $(".inicio").parent().addClass("active");
            $.post("ventasControlador", {
                accion: "grafico"
            }).done((response) => {
                var etiquetas = [];
                var datos = [];
                for (var i = 0; i < response.length; i++) {
                    etiquetas[i] = response[i].fecha_venta;
                    datos[i] = parseInt(response[i].total_venta);

                }
                var lineData = {
                    labels: etiquetas,
                    datasets: [
                        {
                            label: "Ventas",
                            fillColor: "rgba(26,179,148,0.5)",
                            strokeColor: "rgba(26,179,148,0.7)",
                            pointColor: "rgba(26,179,148,1)",
                            pointStrokeColor: "#fff",
                            pointHighlightFill: "#fff",
                            pointHighlightStroke: "rgba(26,179,148,1)",
                            data: datos
                        }
                    ]
                };

                var lineOptions = {
                    scaleShowGridLines: true,
                    scaleGridLineColor: "rgba(0,0,0,.05)",
                    scaleGridLineWidth: 1,
                    bezierCurve: true,
                    bezierCurveTension: 0.4,
                    pointDot: true,
                    pointDotRadius: 4,
                    pointDotStrokeWidth: 1,
                    pointHitDetectionRadius: 20,
                    datasetStroke: true,
                    datasetStrokeWidth: 2,
                    datasetFill: true,
                    responsive: true
                };
                var ctx = document.getElementById("lineChart").getContext("2d");
                var myNewChart = new Chart(ctx).Line(lineData, lineOptions);
            }).fail((reponse, jqxhr, error) => {
                toastr.error(error + ", contacte con el desarrollador");
            });

            $.post("comprasControlador", {
                accion: "grafico"
            }).done((response) => {
                var etiquetas = [];
                var datos = [];
                for (var i = 0; i < response.length; i++) {
                    etiquetas[i] = response[i].fecha_compra;
                    datos[i] = parseInt(response[i].total_compra);

                }
                var lineData = {
                    labels: etiquetas,
                    datasets: [
                        {
                            label: "Compras",
                            fillColor: "rgba(26,179,148,0.5)",
                            strokeColor: "rgba(26,179,148,0.7)",
                            pointColor: "rgba(26,179,148,1)",
                            pointStrokeColor: "#fff",
                            pointHighlightFill: "#fff",
                            pointHighlightStroke: "rgba(26,179,148,1)",
                            data: datos
                        }
                    ]
                };

                var lineOptions = {
                    scaleShowGridLines: true,
                    scaleGridLineColor: "rgba(0,0,0,.05)",
                    scaleGridLineWidth: 1,
                    bezierCurve: true,
                    bezierCurveTension: 0.4,
                    pointDot: true,
                    pointDotRadius: 4,
                    pointDotStrokeWidth: 1,
                    pointHitDetectionRadius: 20,
                    datasetStroke: true,
                    datasetStrokeWidth: 2,
                    datasetFill: true,
                    responsive: true,
                };
                var ctx = document.getElementById("lineChart2").getContext("2d");
                var myNewChart = new Chart(ctx).Line(lineData, lineOptions);
            }).fail((reponse, jqxhr, error) => {
                toastr.error(error + ", contacte con el desarrollador");
            });

            $.post("comprasControlador", {
                accion: "graficogastos"
            }).done((response) => {
                var etiquetas = [];
                var datos = [];
                for (var i = 0; i < response.length; i++) {
                    etiquetas[i] = response[i].fecha_compra;
                    datos[i] = parseInt(response[i].total_compra);

                }
                var lineData = {
                    labels: etiquetas,
                    datasets: [
                        {
                            label: "Compras",
                            fillColor: "rgba(26,179,148,0.5)",
                            strokeColor: "rgba(26,179,148,0.7)",
                            pointColor: "rgba(26,179,148,1)",
                            pointStrokeColor: "#fff",
                            pointHighlightFill: "#fff",
                            pointHighlightStroke: "rgba(26,179,148,1)",
                            data: datos
                        }
                    ]
                };

                var lineOptions = {
                    scaleShowGridLines: true,
                    scaleGridLineColor: "rgba(0,0,0,.05)",
                    scaleGridLineWidth: 1,
                    bezierCurve: true,
                    bezierCurveTension: 0.4,
                    pointDot: true,
                    pointDotRadius: 4,
                    pointDotStrokeWidth: 1,
                    pointHitDetectionRadius: 20,
                    datasetStroke: true,
                    datasetStrokeWidth: 2,
                    datasetFill: true,
                    responsive: true,
                };
                var ctx = document.getElementById("lineChart3").getContext("2d");
                var myNewChart = new Chart(ctx).Line(lineData, lineOptions);
            }).fail((reponse, jqxhr, error) => {
                toastr.error(error + ", contacte con el desarrollador");
            });

            setTimeout(() => {
                startTime();
                $("h5 small.text-white").html(escribirFecha());
            }, 250);

            $.post("ventasControlador", {
                accion: "inicio"
            }).done((response) => {
                $(".ventu").html(dot(response));
            }).fail((response, jqxhr, error) => {
                toastr.error(error + ", contacte con el desarrollador");
            });

            $.post("comprasControlador", {
                accion: "inicio"
            }).done((response) => {
                $(".compru").html(dot(response));
            }).fail((response, jqxhr, error) => {
                toastr.error(error + ", contacte con el desarrollador");
            });

            $.post("stockControlador", {
                accion: "inicio"
            }).done((response) => {
                $(".stocku").html(dot(response));
            }).fail((response, jqxhr, error) => {
                toastr.error(error + ", contacte con el desarrollador");
            });
        });
    };

    exports.CargarUserPanel = function () {
        spinneron();
        $("#inicio").load("vistas/panelusuario.html", function () {
            spinneroff();
            $(".active").removeClass("active");
            $(".level").parent().find("ul.nav").removeClass("nav nav-second-level collapse in").addClass("nav nav-second-level collapse");
            $(".inicio").parent().addClass("active");
            anotacion.Notas();
            setTimeout(() => {
                startTime();
                $("h2 small.text-white").html(escribirFecha());
            }, 250);
        });
    };

    return exports;
});
