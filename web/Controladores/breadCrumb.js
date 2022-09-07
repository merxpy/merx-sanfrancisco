function itemsCrumb() {
    $(".breaditem").on('click', function () {
        items();
    });
}

function purchCrumb() {
    $(".breadpurch").on('click', function () {
        compras();
    });
}

function salesCrumb() {
    $(".breadsales").on('click', function () {
        ventas();
    });
}

function userCrumb() {
    $(".breaduser").on('click', function () {
        usuarios();
    });
}

function orderCrumb() {
    $(".breadorder").on('click', function () {
        ordenCompra();
    });
}
