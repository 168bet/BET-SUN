var dialogUrl;
var dialogResultId = "";
var dialogResultName = "";
var dialogMultiResult;
var dialogIsMultiSelect = false;
var dialogCallback = "";
var trigger = false;

$(function() {
    $('body').append('\<div id="chooseDialog" class="modal fade bs-example-modal-lg" tabindex="-1" role="dialog" aria-labelledby="myLargeModalLabel" aria-hidden="true">\
        <div class="modal-dialog modal-lg">\
            <div class="modal-content">\
                <div class="modal-header"></div>\
                <div class="modal-body">\
                    <div id="treeview"></div>\
                </div>\
                <div class="modal-footer">\
                    <button class="btn btn-primary" data-dismiss="modal"><span class="language" data-word="confirm">确定</span></button>\
                </div>\
            </div>\
        </div>\
    </div>');

    $.ajax({
        type: "get",
        async: "false",
        url: "/game/js/kendo.web.min.js",
        success: function(data) {
            $("head").append($("<script>").html(data));
        }
    });

    $.ajax({
        type: "get",
        async: "false",
        url: "/game/css/kendo.common.min.css",
        success: function(data) {
            $("head").append($("<style>").html(data));
        }
    });

    $.ajax({
        type: "get",
        async: "false",
        url: "/game/css/kendo.default.min.css",
        success: function(data) {
            $("head").append($("<style>").html(data));
        }
    });

    $('#chooseDialog').on('shown.bs.modal', function(e) {
        if (trigger != $(this)) {
            initTree();
            trigger = $(this);
        }
    });
    $('#chooseDialog').on('hidden.bs.modal', function(e) {
        if (dialogIsMultiSelect == true)
            dialogMultiResult = getAllCheckedNodeIds();
        $("#treeview").data('kendoTreeView').dataSource.read();
        dialogCallback();
    });

    initCheckInfoModal();
    initDeleteConfirmDialog();
});

function showModal(title, url, isMultiSelect, callback) {
    dialogUrl = url;
    dialogIsMultiSelect = isMultiSelect;
    dialogCallback = callback;
    $('#chooseDialog .modal-header').html(title);
    $('#chooseDialog').modal('show');
}

function initTree() {
    data = new kendo.data.HierarchicalDataSource({
        transport: {
            read: {
                url: dialogUrl,
                dataType: "json"
            }
        },
        schema: {
            model: {
                id: "uuid",
                hasChildren: "hasAgent"
            }
        }
    });

    $("#chooseDialog .modal-body").html("");
    $("#chooseDialog .modal-body").html("<div id='treeview'></div>");

    if (dialogIsMultiSelect == true) {
        $("#treeview").kendoTreeView({
            checkboxes: {
                checkChildren: false
            },
            dataSource: data,
            dataTextField: "username",
            select: onSelect
        });
    } else {
        $("#treeview").kendoTreeView({
            dataSource: data,
            dataTextField: "username",
            select: onSelect
        });
    }
}

function onSelect(e) {
    var tv = $("#treeview").data('kendoTreeView');
    var item = tv.dataItem(e.node);
    dialogResultId = item.uuid;
    dialogResultName = item.username;
}

function checkedNodeIds(nodes, checkedNodes) {
    for (var i = 0; i < nodes.length; i++) {
        if (nodes[i].checked) {
            var keyValue = {};
            keyValue.uuid = nodes[i].id;
            keyValue.username = nodes[i].username;
            checkedNodes.push(keyValue);
        }

        if (nodes[i].hasChildren) {
            checkedNodeIds(nodes[i].children.view(), checkedNodes);
        }
    }
}

function getAllCheckedNodeIds() {
    var checkedNodes = [];
    var treeView = $("#treeview").data("kendoTreeView");

    checkedNodeIds(treeView.dataSource.view(), checkedNodes);
    return checkedNodes;
}

function initCheckInfoModal() {
    $('body').append('<div id="checkInfoDialog" class="modal fade bs-example-modal-lg" tabindex="-1" role="dialog" aria-labelledby="myLargeModalLabel" aria-hidden="true">\
            <div class="modal-dialog modal-lg">\
                <div class="modal-content">\
                    <div class="modal-header"><span class="language" data-word="checkInfo">核对信息</span></div>\
                    <div class="modal-body">\
                        <div class="container-fluid">\
                            <form id="checkForm" class="form-horizontal" role="form">\
                                <div class="form-group">\
                                    <label class="control-label">\
                                        <span class="language" data-word="userName">用户名</span>\
                                        ：\
                                    </label>\
                                    <div class="controls">\
                                        <input type="text" class="input-large" disabled="true" id="checkUserName" name="checkUserName"/>\
                                    </div>\
                                </div>\
                                <div class="form-group">\
                                    <label class="control-label">\
                                        <span class="language" data-word="phone">电话</span>\
                                        ：\
                                    </label>\
                                    <div class="controls">\
                                        <input type="text" class="input-large" disabled="true" id="checkPhone" name="checkPhone"/>\
                                    </div>\
                                </div>\
                                <div class="form-group">\
                                    <label class="control-label">\
                                        <span class="language" data-word="email">邮箱</span>\
                                        ：\
                                    </label>\
                                    <div class="controls">\
                                        <input type="text" class="input-large" disabled="true" id="checkEmail" name="checkEmail"/>\
                                    </div>\
                                </div>\
                            </form>\
                        </div>\
                    </div>\
                    <div class="modal-footer">\
                        <button class="btn btn-primary" data-dismiss="modal">\
                            <span class="language" data-word="confirm">确定</span>\
                        </button>\
                    </div>\
                </div>\
            </div>\
        </div>');
}

function showCheckInfoModal(data) {
    $("#checkInfoDialog #checkUserName").val(data.username);
    $("#checkInfoDialog #checkPhone").val(data.phone);
    $("#checkInfoDialog #checkEmail").val(data.email);
    $("#checkInfoDialog").modal("show");
}

function initDeleteConfirmDialog() {
    $("body").append('<div id="deleteConfrimDialog" class="modal fade bs-example-modal-lg" tabindex="-1" role="dialog" aria-labelledby="myLargeModalLabel" aria-hidden="true">\
            <div class="modal-dialog modal-lg">\
                <div class="modal-content">\
                    <div class="modal-header"><span class="language" data-word="confirmDelete">确认删除</span></div>\
                    <div class="modal-footer">\
                        <button class="btn btn-primary" data-dismiss="modal" id="confirm">\
                            <span class="language" data-word="confirm">确定</span>\
                        </button>\
                        <button class="btn btn-primary" data-dismiss="modal">\
                            <span class="language" data-word="cancel">取消</span>\
                        </button>\
                    </div>\
                </div>\
            </div>\
        </div>');
}

function showDeleteConfirmDialog(callback) {
    $("#deleteConfrimDialog #confirm").unbind("click");
    $("#deleteConfrimDialog #confirm").click(function() {
        callback();
    });
    $("#deleteConfrimDialog").modal("show");
}