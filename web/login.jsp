<%-- 
    Document   : login
    Created on : 09-jul-2018, 8:06:50
    Author     : akio
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>

    <head>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">

        <title>MERX | Login</title>

        <link href="css/bootstrap.min.css" rel="stylesheet" type="text/css"/>
        <link href="font-awesome/css/font-awesome.css" rel="stylesheet" type="text/css"/>
        <link rel="shortcut icon" href="img/favicon.ico" type="image/x-icon">
        <link rel="icon" href="img/favicon.ico" type="image/x-icon">
        <link href="css/animate.css" rel="stylesheet" type="text/css"/>
        <link href="css/style.css" rel="stylesheet" type="text/css"/>
        <link href="css/plugins/toastr/toastr.min.css" rel="stylesheet" type="text/css"/>
        <link href="css/plugins/csshake/csshake.min.css" rel="stylesheet" type="text/css"/>
        <!-- Google Analytics -->
        <script async src="https://www.googletagmanager.com/gtag/js?id=UA-139120790-1"></script>
        <script>
            window.dataLayer = window.dataLayer || [];
            function gtag() {
                dataLayer.push(arguments);
            }
            gtag('js', new Date());

            gtag('config', 'UA-139120790-1');
        </script>
        <!-- SmartLook -->
        <script type="text/javascript">
            window.smartlook || (function (d) {
                var o = smartlook = function () {
                    o.api.push(arguments)
                }, h = d.getElementsByTagName('head')[0];
                var c = d.createElement('script');
                o.api = new Array();
                c.async = true;
                c.type = 'text/javascript';
                c.charset = 'utf-8';
                c.src = 'https://rec.smartlook.com/recorder.js';
                h.appendChild(c);
            })(document);
            smartlook('init', '89864bd08ae2a39517e351b3250fef92c0c5586e');
        </script>
    </head>
    <body class="gray-bg">

        <div class="middle-box text-center loginscreen animated fadeInDown">
            <div>
                <div>
                    <img src="img/logo.png" style="margin-left:-15%" width="400" height="200">
                </div>
                <h3>Bienvenido a San Francisco</h3>
                <div class="m-t">
                    <form>
                        <div class="form-group">
                            <input type="text" class="form-control" placeholder="Usuario" name="usuario" required>
                        </div>
                        <div class="form-group">
                            <input type="password" class="form-control" placeholder="Contraseña" name="pass" required>
                        </div>
                        <div id="info">
                        </div>
                        <button type="submit" class="btn btn-primary block full-width m-b">Ingresar</button>
                    </form>
                </div>
                <a href="http://www.2hdev.com.py" target="_blank"><p class="m-t"> <small></small> </p></a>
            </div>
        </div>

        <!-- Mainly scripts -->

        <script src="js/jquery-2.1.1.js" type="text/javascript"></script>
        <script src="js/bootstrap.min.js" type="text/javascript"></script>
        <script src="js/plugins/toastr/toastr.min.js" type="text/javascript"></script>
        <script>
            $(document).ready(function () {
                $("form").on("submit", function (e) {
                    $("#info").html(`<div class="alert alert-info">
                            <center> 
                                <div class="windows8">
                                    <div class="wBall" id="wBall_1">
                                        <div class="wInnerBall"></div>
                                    </div>
                                    <div class="wBall" id="wBall_2">
                                        <div class="wInnerBall"></div>
                                    </div>
                                    <div class="wBall" id="wBall_3">
                                        <div class="wInnerBall"></div>
                                    </div>
                                    <div class="wBall" id="wBall_4">
                                        <div class="wInnerBall"></div>
                                    </div>
                                    <div class="wBall" id="wBall_5">
                                        <div class="wInnerBall"></div>
                                    </div>
                                </div>
                            </center>
                        </div>`);
                    e.preventDefault();
                    var datos = {
                        'usuario': $('input[name="usuario"]').val(),
                        'pass': $('input[name="pass"]').val()
                    };
                    $.ajax({
                        data: datos,
                        url: 'loginControlador',
                        type: 'post',
                        success: function (response) {
                            if (response === 1) {
                                window.location.href = "./";
                            } else if (response === 0) {
                                $(".alert").removeClass("hide alert-info").addClass("show shake-horizontal shake-constant").addClass("alert-warning").html("<b>¡Error! </b>Usuario o contraseña incorrectos");
                                setTimeout(function () {
                                    $(".alert").removeClass("shake-horizontal shake-constant");
                                }, 1000);
                            } else if (response === -1) {
                                $(".alert").removeClass("hide alert-info").addClass("show shake-rotate shake-constant").addClass("alert-danger").html("El usuario no existe o ha sido cambiado. Para más información contacte con <a class='alert-link' target='_blank' href='https://2hdev.com.py/'>2hdev</a>");
                                setTimeout(function () {
                                    $(".alert").removeClass("shake-rotate shake-constant");
                                }, 1000);
                            }
                        },
                        error: function (response, jqxhr, error) {
                            if (response.status === 500) {
                                toastr.info("Por favor. Verifique su conexión a internet");
                            }
                        }
                    });
                });
                var d = new Date();
                $("small").html('2HDEV "Desarrollando Soluciones Increíbles" &copy; ' + d.getFullYear());
            });
        </script>
    </body>

</html>
