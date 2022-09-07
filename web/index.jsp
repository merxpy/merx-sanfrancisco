<%-- 
    Document   : inicio
    Created on : 12-feb-2018, 10:51:46
    Author     : akio
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="httpSession.jsp"%>
<!DOCTYPE html>
<html>
    <head>
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Panel de Control</title>
        <link rel="shortcut icon" href="img/favicon.ico" type="image/x-icon">
        <link rel="icon" href="img/favicon.ico" type="image/x-icon">
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="js/plugins/selectize/css/selectize.bootstrap3.css" rel="stylesheet" type="text/css"/>
        <link href="css/plugins/fontawesome-free-5.6.3-web/css/all.min.css" rel="stylesheet" type="text/css"/>
        <link href="css/animate.css" rel="stylesheet">
        <link href="css/plugins/blueimp/css/blueimp-gallery.css" rel="stylesheet" type="text/css"/>
        <link href="css/plugins/toastr/toastr.min.css" rel="stylesheet">
        <link href="css/plugins/intro/introjs.min.css" rel="stylesheet" type="text/css"/>
        <link href="css/plugins/intro/introjs-modern.css" rel="stylesheet" type="text/css"/>
        <link href="css/plugins/dataTables/datatables.min.css" rel="stylesheet"> 
        <link href="css/plugins/select2/select2.css" rel="stylesheet" type="text/css"/>
        <link href="css/plugins/touchspin/jquery.bootstrap-touchspin.min.css" rel="stylesheet">
        <link href="css/plugins/jsTree/style.min.css" rel="stylesheet" type="text/css"/>
        <link href="css/plugins/sweetalert/sweetalert.css" rel="stylesheet" type="text/css"/>
        <link href="css/plugins/alertify/alertify.min.css" rel="stylesheet" type="text/css"/>
        <link href="js/plugins/notiflt/css/notifIt.min.css" rel="stylesheet" type="text/css"/>
        <link href="js/bootstrap-datepicker/css/bootstrap-datepicker3.min.css" rel="stylesheet" type="text/css"/>
        <link href="css/plugins/awesome-bootstrap-checkbox/awesome-bootstrap-checkbox.css" rel="stylesheet">
        <link href="css/plugins/steps/jquery.steps.css" rel="stylesheet" type="text/css"/>
        <link href="css/plugins/slick/slick.css" rel="stylesheet" type="text/css"/>
        <link href="css/plugins/slick/slick-theme.css" rel="stylesheet" type="text/css"/>
        <link href="css/plugins/fullcalendar/fullcalendar.css" rel="stylesheet">
        <link href="css/plugins/fullcalendar/fullcalendar.print.css" rel='stylesheet' media='print'>
        <link href="css/plugins/toolbar/jquery.toolbar.css" rel="stylesheet" type="text/css"/>
        <link href="css/plugins/csshake/csshake.min.css" rel="stylesheet" type="text/css"/>
        <link href="css/plugins/jasny/jasny-bootstrap.min.css" rel="stylesheet">
        <link href="css/plugins/clockpicker/clockpicker.css" rel="stylesheet">
        <link href="css/plugins/palette-colorpicker/palette-color-picker.css" rel="stylesheet" type="text/css"/>
        <link href="css/plugins/perferct-scrollbar/perfect-scrollbar.css" rel="stylesheet" type="text/css"/>
        <!--<link href="css/plugins/sweetalert/sweetalert2.min.css" rel="stylesheet" type="text/css"/>-->
        <link href="css/style.css" rel="stylesheet">

    </head>
    <body>
        <div id="wrapper">
            <nav class="navbar-default navbar-static-side" role="navigation">
                <div class="sidebar-collapse">
                    <ul class="nav metismenu" id="side-menu">
                        <li class="nav-header mini-menu-user">
                            <div class="dropdown profile-element">
                                <span>
                                    <button class="btn btn-default btn-circle btn-lg" type="button"></button>
                                </span>
                                <a data-toggle="dropdown" class="dropdown-toggle">
                                    <span class="clear"> 
                                        <span class="block m-t-xs"> 
                                            <strong class="font-bold nombre-user"></strong>
                                        </span> 
                                    </span> 
                                </a>
                                <ul class="dropdown-menu animated fadeInRight m-t-xs">
                                    <li><a href="#/perfil">Perfil</a></li>
                                    <li class="divider"></li>
                                    <li><a href="salir">Cerrar Sesion</a></li>
                                </ul>
                            </div>
                            <div class="logo-element">
                                MX
                            </div>
                        </li>
                        <li>
                            <a href="#/inicio" class="inicio" title="Inicio"><i class="fa fa-home"></i> <span class="nav-label">Inicio</span></a>
                        </li>
                        <li>
                            <a href="#/articulos" class="item" title="Artículos"><i class="far fa-gem"></i> <span class="nav-label">Artículos</span></a>
                        </li>
                        <li>
                            <a href="#/clientes" class="client" title="Clientes"><i class="fa fa-user"></i> <span class="nav-label">Clientes</span>  </a>
                        </li>
                        <li>
                            <a href="#/proveedores" class="supplier" title="Proveedores"><i class="fa fa-truck"></i> <span class="nav-label">Proveedores</span></a>
                        </li>
                        <li>
                            <a class="level orden-trabajo" title="Orden de Trabajo"><i class="fa fa-archive"></i> 
                                <span class="nav-label">Orden de Trabajo</span>
                                <span class="fa arrow"></span>
                            </a>
                            <ul class="nav nav-second-level collapse">
                                <li><a href="#/servicios/pendientes" title="Pendientes">Pendientes</a></li>
                                <li><a href="#/servicios/aretirar" title="A Retirar">A Retirar</a></li>
                                <li><a href="#/servicios/retirados" title="Retirados">Retirados</a></li>
                                <li><a href="#/servicios/verfacturados" title="Ver Facturados">Facturados</a></li>
                                <li><a href="#/servicios/verdeliveries" title="Ver Delivery">Delivery</a></li>
                                <li><a href="#/servicios/vertodos" title="Ver Todos">Ver Todos</a></li>
                                <li><a href="#/servicios/anulados" title="Anulados">Anulados</a></li>
                            </ul>
                        </li>
                        <li>
                            <a class="level delivery" title="Deliveries"><i class="fas fa-motorcycle"></i> 
                                <span class="nav-label">Deliveries</span>
                                <span class="fa arrow"></span>
                            </a>
                            <ul class="nav nav-second-level collapse">
                                <li><a href="#/delivery/retiros" title="Retiros">Retiros</a></li>
                                <li><a href="#/delivery/envios" title="Envios">Envios</a></li>
                            </ul>
                        </li>
                        <li>
                            <a href="#/cuentas" title="Cuentas"><i class="fas fa-book-dead"></i> <span class="nav-label">Cuentas</span></a>
                        </li>
                        <li>
                            <a href="#/compras" title="Compras"><i class="fas fa-shopping-bag"></i> <span class="nav-label">Compras</span></a>
                        </li>
                        <li>
                            <a href="#/gastos" title="Gastos"><i class="fas fa-calculator"></i> <span class="nav-label">Gastos</span></a>
                        </li>
                        <li>
                            <a href="#/ventas" class="sales" title="Ventas"><i class="fa fa-shopping-cart"></i> <span class="nav-label">Ventas</span></a>
                        </li>
                        <li>
                            <a href="#/caja" title="Caja"><i class="fas fa-cash-register"></i> <span class="nav-label">Caja</span></a>
                        </li>
                        <li>
                            <a href="#/reportes" title="Reportes"><i class="fab fa-readme"></i><span class="nav-label">Reportes</span></a>
                        </li>
                        <li>
                            <a class="level inventario" title="Stock"><i class="fa fa-archive"></i> 
                                <span class="nav-label">Control de Inventario</span>
                                <span class="fa arrow"></span>
                            </a>
                            <ul class="nav nav-second-level collapse">
                                <li><a href="#/inventario" class="stock" title="Inventario">Inventario</a></li>
                                <li><a href="#/bajastock">Ajuste de Stock</a></li>
                                <li><a href="#/mermas">Descripcion de Movimientos</a></li>
                            </ul>
                        </li>
                        <li>
                            <a class="level anulado" title="Anulados"><i class="fa fa-ban"></i> 
                                <span class="nav-label">Anulados/<br>Inhabilitados</span>
                                <span class="fa arrow"></span>
                            </a>
                            <ul class="nav nav-second-level collapse">
                                <li><a href="#/anulados/articulos">Artículos</a></li>
                                <li><a href="#/anulados/clientes" class="close-cli">Clientes</a></li>
                                <li><a href="#/anulados/compras" class="close-com">Compras</a></li>
                                <li><a href="#/anulados/proveedores" class="close-pro">Proveedores</a></li>
                                <li><a href="#/anulados/ventas" class="close-ven">Ventas</a></li>
                            </ul>
                        </li>
                        <li>
                            <a class="level utilidades" title="Utilidades"><i class="fa fa-wrench"></i> 
                                <span class="nav-label">Utilidades</span>
                                <span class="fa arrow"></span>
                            </a>
                            <ul class="nav nav-second-level collapse">
                                <li><a href="#/utilidades/categorias">Categorías</a></li>
                                <li><a href="#/utilidades/etiquetas" class="tags">Etiquetas</a></li>
                                <li><a href="#/utilidades/marcas" class="marks">Marcas</a></li>
                                <li><a href="#/utilidades/unidades" class="unit">Unidades</a></li>
                            </ul>
                        </li>
                        <li>
                            <a class="level sistema" title="Sistema"><i class="fa fa-desktop"></i> 
                                <span class="nav-label">Sistema</span>
                                <span class="fa arrow"></span>
                            </a>
                            <ul class="nav nav-second-level collapse">
                                <li><a href="#/sistema/auditoria" class="audit">Auditoría</a></li>
                                <li><a href="#/sistema/empresa">Empresa</a></li>
                                <li><a href="#/sistema/usuarios" class="user">Usuarios</a></li>
                                <li><a href="#/sistema/sucursales" class="sucursales">Sucursales</a></li>
                            </ul>
                        </li>
                    </ul>
                </div>
            </nav>

            <div id="page-wrapper" class="gray-bg">
                <div class="row border-bottom">
                    <nav class="navbar navbar-static-top white-bg" role="navigation" style="margin-bottom: 0">
                        <div class="navbar-header">
                            <a class="navbar-minimalize minimalize-styl-2 btn btn-primary "><i class="fa fa-bars"></i> </a>
                        </div>
                        <ul class="nav navbar-top-links navbar-right">
                            <li class="dropdown">
                                <a class="dropdown-toggle count-info" data-toggle="dropdown">
                                    <i class="fa fa-bell"></i><span class="label label-primary"></span>
                                </a>
                                <ul class="dropdown-menu dropdown-alert">
                                </ul>
                            </li>
                            <li class="dropdown">
                                <a href="Reportes/manual.pdf" target="_blank">
                                    <i class="fa fa-question-circle"></i>
                                </a>
                            </li>
                            <li>
                                <a href="salir">
                                    <i class="fa fa-sign-out"></i> Salir
                                </a>
                            </li>
                        </ul>
                    </nav>
                </div>
                <div id="inicio">
                </div>
                <div class="footer">
                    <div>
                        <strong>Copyright</strong> <a href="http://www.2hdev.com.py" target="_blank">2HDEV&copy;</a>
                    </div>
                </div>
                <div id="modal-ext">
                </div>
            </div>
        </div>

        <!-- Mainly scripts -->
        <script src="js/plugins/fullcalendar/moment.min.js"></script>
        <script src="js/jquery.min.js" type="text/javascript"></script>
        <!--blueimp-gallery-->
        <script src="js/plugins/blueimp/jquery.blueimp-gallery.min.js"></script>
        <!--selectize-->
        <script src="js/plugins/selectize/js/standalone/selectize.js" type="text/javascript"></script>
        <script src="js/bootstrap.min.js"></script>
        <script src="js/plugins/metisMenu/jquery.metisMenu.js"></script>
        <script src="js/plugins/slimscroll/jquery.slimscroll.min.js"></script>
        <!-- Flot -->
        <script src="js/plugins/flot/jquery.flot.js"></script>
        <script src="js/plugins/flot/jquery.flot.tooltip.min.js"></script>
        <script src="js/plugins/flot/jquery.flot.spline.js"></script>
        <script src="js/plugins/flot/jquery.flot.resize.js"></script>
        <script src="js/plugins/flot/jquery.flot.pie.js"></script>
        <script src="js/plugins/flot/jquery.flot.symbol.js"></script>
        <script src="js/plugins/flot/jquery.flot.time.js"></script>

        <!-- Peity -->
        <script src="js/plugins/peity/jquery.peity.min.js"></script>
        <script src="js/demo/peity-demo.js"></script>

        <!-- Custom and plugin javascript -->
        <script src="js/inspinia.js"></script>
        <script src="js/plugins/pace/pace.min.js"></script>
        <script src="js/plugins/slick/slick.min.js" type="text/javascript"></script>

        <!-- jQuery UI -->
        <script src="js/plugins/jquery-ui/jquery-ui.min.js"></script>

        <!-- Jvectormap -->
        <script src="js/plugins/jvectormap/jquery-jvectormap-2.0.2.min.js"></script>
        <script src="js/plugins/jvectormap/jquery-jvectormap-world-mill-en.js"></script>

        <!-- EayPIE -->
        <script src="js/plugins/easypiechart/jquery.easypiechart.js"></script>

        <!-- Sparkline -->
        <script src="js/plugins/sparkline/jquery.sparkline.min.js"></script>

        <!-- Sparkline demo data  -->
        <script src="js/demo/sparkline-demo.js"></script>
        <!--Palette color Picker-->
        <script src="js/plugins/palette-colorpicker/palette-color-picker.min.js" type="text/javascript"></script>
        <!--dataTables-->
        <script src="js/plugins/dataTables/datatables.min.js" type="text/javascript"></script>
        <script src="js/plugins/intro/intro.min.js" type="text/javascript"></script>
        <script src="js/plugins/toastr/toastr.min.js"></script>
        <script src="js/ruc.js" type="text/javascript"></script>
        <script src="js/plugins/select2/nuevo/select2.full.js" type="text/javascript"></script>
        <script src="js/plugins/alertify/alertify.min.js" type="text/javascript"></script>
        <script src="js/plugins/sweetalert/sweetalert.min.js" type="text/javascript"></script>
        <!--<script src="js/plugins/sweetalert/sweetalert2.min.js" type="text/javascript"></script>-->
        <script src="js/plugins/typeahead/bootstrap3-typeahead.min.js" type="text/javascript"></script>
        <!--notiflt-->
        <script src="js/plugins/notiflt/js/notifIt.min.js" type="text/javascript"></script>
        <!-- Full Calendar -->
        <script src="js/plugins/fullcalendar/fullcalendar.min.js"></script>
        <!--Toolbar-->
        <script src="js/plugins/toolbar/jquery.toolbar.min.js" type="text/javascript"></script>
        <!--file-->
        <script src="js/file.js" type="text/javascript"></script>
        <!--jsTree-->
        <script src="js/plugins/jsTree/jstree.min.js" type="text/javascript"></script>
        <!--dateparser-->
        <script src="js/dateparser.js" type="text/javascript"></script>
        <!--num-->
        <script src="js/num.js" type="text/javascript"></script>
        <!--nfac-->
        <script src="js/nfac.js" type="text/javascript"></script>
        <!--loadPage-->
        <script src="js/loadPage.js" type="text/javascript"></script>
        <!--datePicker-->
        <script src="js/bootstrap-datepicker/js/bootstrap-datepicker.min.js" type="text/javascript"></script>
        <!--Currency-->
        <script src="js/currency.js" type="text/javascript"></script>
        <!--Date Time-->
        <script src="js/date_time.js" type="text/javascript"></script> 
        <!-- Idle Timer plugin -->
        <script src="js/plugins/idle-timer/idle-timer.min.js"></script>
        <!--breadCrumb-->
        <script src="Controladores/breadCrumb.js" type="text/javascript"></script>
        <!--spinnermode-->
        <script src="js/spinnermode.js" type="text/javascript"></script>
        <!--steps-->
        <script src="js/plugins/staps/jquery.steps.min.js" type="text/javascript"></script>
        <!-- Jquery Validate -->
        <script src="js/plugins/validate/jquery.validate.min.js"></script>
        <script src="js/plugins/chartJs/Chart.min.js"></script>
        <!--Perfect-Scrollbar-->
        <script src="js/plugins/perfect-scrollbar/perfect-scrollbar.js" type="text/javascript"></script>
        <!--Push-->
        <script src="js/push.min.js" type="text/javascript"></script>
        <!--NumeroALetras-->
        <script src="js/NumeroALetras.js" type="text/javascript"></script>
        <!--funciones-->
        <script src="js/funciones.js" type="text/javascript"></script>
        <!--Require js-->
        <script data-main="config" src="js/require.min.js" type="text/javascript"></script>
        <!--bootstrapTour-->
        <script src="js/plugins/bootstrapTour/bootstrap-tour.min.js" type="text/javascript"></script>
        <!-- Input Mask-->
        <script src="js/plugins/jasny/jasny-bootstrap.min.js"></script>
        <!-- TouchSpin -->
        <script src="js/plugins/touchspin/jquery.bootstrap-touchspin.min.js"></script>
        <!-- Clock picker -->
        <script src="js/plugins/clockpicker/clockpicker.js"></script>
        <!--Controladores-->
        <script src="Controladores/articulosController.js" type="text/javascript"></script>
        <script src="Controladores/auditoriaController.js" type="text/javascript"></script>
        <script src="Controladores/clientesController.js" type="text/javascript"></script>
        <script src="Controladores/etiquetasController.js" type="text/javascript"></script>
        <script src="Controladores/loadUndefined.js" type="text/javascript"></script>
        <script src="Controladores/marcasController.js" type="text/javascript"></script>
        <script src="Controladores/permisosController.js" type="text/javascript"></script>
        <script src="Controladores/proveedoresController.js" type="text/javascript"></script>
        <script src="Controladores/sessionController.js" type="text/javascript"></script>
        <script src="Controladores/stockController.js" type="text/javascript"></script>
        <script src="Controladores/strings.js" type="text/javascript"></script>
        <script src="Controladores/unidadesController.js" type="text/javascript"></script>
        <script src="Controladores/ventasController.js" type="text/javascript"></script>
        <script src="Controladores/ciudadesController.js" type="text/javascript"></script>
        <script src="Controladores/deptosController.js" type="text/javascript"></script>
    </body>
</html>
